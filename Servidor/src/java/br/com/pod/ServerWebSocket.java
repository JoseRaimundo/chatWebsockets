package br.com.pod;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author pc 
 */
@ServerEndpoint("/chat/{sala}/{usuario}")
public class ServerWebSocket {
 
    private static Map<String, Sala> salas = Collections.synchronizedMap(new HashMap<String, Sala>());
    private static Map<String, String> global_nomes = Collections.synchronizedMap(new HashMap<String, String>());
     
    //este método manda uma mensagem formatada em broadcast (para todos usuários de uma sala especifica)
    public void sendBroadCast(Sala sala,  String usuario, String mensagem){
        Calendar data = Calendar.getInstance();
        for (Usuario user : sala.todosUsuarios()) {
            try {
                user.getSession().getBasicRemote().sendText("- "+usuario + " " + new SimpleDateFormat("hh:mm:ss").format(data.getTime()) + " : " + mensagem);
            } catch (IOException ex) {
                System.out.println("Erro no sendBroadCast " + ex.toString());
            }
        }
    }
    
    //atribui um nome alternativo para o usuário que solicitou um nome já usado
    private String atribuiNome(Map<String, String> nomes, String nome){
        String nome_alternativo = nome;
        int cont = 1;
        while(nomes.containsKey(nome_alternativo)){
            nome_alternativo = nome_alternativo + cont;
            cont++;
        }
        return nome_alternativo;
    }
    
    //notifica todos os usuários sobre quem entrou ou saiu da sala, ou trocou de nome
    public void usuariosOnline(Sala sala){
        for (Usuario user : sala.todosUsuarios()) {
            try {
                user.getSession().getBasicRemote().sendText(sala.toString());
            } catch (IOException ex) {
                System.out.println("Erro no usuariosOnline " + ex.toString());
            }
        }  
    }    
    
    @OnOpen
    public void conectar(Session ses, @PathParam("sala")String sala, @PathParam("usuario")String nome) throws IOException{
        //varifica se a sala existe e 
        if(salas.containsKey(sala)){
           //caso o nome já esteja sendo utilizado, é criado um nome alternativo não usado seguindo nome_solicitado+número
           if(global_nomes.containsKey(nome) ){
               nome = atribuiNome(global_nomes, nome);
               //informa o motivo do nome alternativo
               ses.getBasicRemote().sendText("O nome solicitado já está em uso, seu nome foi \n"
                                           + "configurado como " + nome + " (Você pode renomeá-lo \n"
                                           + "utilizando o comando [rename <novo_nome>])");
           }
           //adiciona um novo usuário na sala
            salas.get(sala).addUsuario(new Usuario(nome, ses, false));

           //notifica todo mundo sobre o usuário que entrou
            sendBroadCast(salas.get(sala), nome, "Entrou na sala!");        

       }else{
           //caso o nome já esteja sendo utilizado, é criado um nome alternativo não usado seguindo nome_solicitado+número
           if(global_nomes.containsKey(nome) ){
               nome = atribuiNome(global_nomes, nome);
               //informa o motivo do nome alternativo
               ses.getBasicRemote().sendText("O nome solicitado já está em uso em outra sala, \n"
                                              + "seu nome foi configurado como " + nome + " \n"
                                              + "(Você pode renomeá-lo utilizando o comando \n"
                                              + "[rename <novo_nome>])");
           }
           //cria uma sala e atribui um criador 
           salas.put(sala, new Sala(sala, new Usuario(nome, ses, true)));
           //informa para o criador que a sala foi criada
           ses.getBasicRemote().sendText("Sala \"" + sala + "\" criada com sucesso!");
       }
        global_nomes.put(nome, nome);
        //Exibe os usuários online naquela sala
        usuariosOnline(salas.get(sala));
    }
    
    @OnMessage
    public void onMessage(Session ses, String message, @PathParam("sala")String sala) throws IOException {
        //quabrando a mensagem
        String[] list = message.split(" ");
        //pega as informações de quem mandou a mensagem
        Usuario remetente = salas.get(sala).buscaPeloID(ses.getId());
        if (list[0].equals("send")) {
            if(list[1].equals("-u")){
                Usuario destinatario  = salas.get(sala).buscaPeloNome(list[2]);
                if (destinatario == null) {
                    remetente.getSession().getBasicRemote().sendText("Este usuário não existe!");
                }else{
                    destinatario.getSession().getBasicRemote().sendText("- "+remetente.getNome() + " "  + new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime()) + " reservadamente : " + message);
                }
            }else{ 
                sendBroadCast(salas.get(sala), remetente.getNome(), message);
            }
   
        }else if(list[0].equals("rename")){
            if(global_nomes.containsKey(list[1])){
               //informa o motivo do nome alternativo
               ses.getBasicRemote().sendText("O nome solicitado já está em uso!");
            }else{
                //muda o nome e notifica todo mundo
                String antigo_nome = remetente.getNome();
                salas.get(sala).alteraNome(remetente.getId(), list[1]);
                //atualiza a lista global
                global_nomes.remove(antigo_nome);
                global_nomes.put(list[1], list[1]);
                sendBroadCast(salas.get(sala), antigo_nome, "Renomeado para " + list[1]);
            }
            usuariosOnline(salas.get(sala));
        }else{
            //caso o usuário informe o protocolo errado
            ses.getBasicRemote().sendText("Verifique os protocolos do chat:\n"
                    + "Para enviar uma mensagem: send <mensagem>\n"
                    + "Para enviar uma mensagem para alguém especifico: send -u <destinatario> <mensagem>\n"
                    + "Para renomear: rename <nome que deseja>");
        }
        
    }
    
    @OnClose
    public void desconectar(Session ses, @PathParam("sala")String sala) throws IOException{
        //recupera o usuário que está sendo removido para remover corretamente
        Usuario usuario       = salas.get(sala).buscaPeloID(ses.getId());
        //remove o usuário da sala
        salas.get(sala).removeUsuario(ses.getId());
        //notifica todo mundo que alguém saiu da sala
        sendBroadCast(salas.get(sala), usuario.getNome(), "Saiu da sala!");
        usuariosOnline(salas.get(sala));
        //remove o nome da lista global
        global_nomes.put(usuario.getNome(), usuario.getNome());
        //se for o ultimo usuário da sala, deleta a sala
        if (salas.get(sala).salaVazia()) {
            salas.remove(sala);
        }
    }
}