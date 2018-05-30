/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pod;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author talitha
 */
@ServerEndpoint("/chat/{sala}/{usuario}")
public class ServerWebSocket {
 
    private static Map<String, Sala> salas = Collections.synchronizedMap(new HashMap<String, Sala>());
//    private static Map<String, String> global_nomes = Collections.synchronizedMap(new HashMap<String, String>());
 
    
    //este método manda uma mensagme formatada em breadcast (para todos usuários de uma sala especifica)
    public void sendBroadCast(Sala sala,  String usuario, String mensagem){
        Calendar data = Calendar.getInstance();
        for (Usuario user : sala.todosUsuarios()) {
            try {
                user.getSession().getBasicRemote().sendText(usuario + " " + new SimpleDateFormat("hh:mm:ss").format(data.getTime()) + " : " + mensagem);
            } catch (IOException ex) {
                System.out.println("Erro no sendBroadCast " + ex.toString());
            }
        }
    }
    
    public void usuariosOnline(Sala sala){
        for (Usuario user : sala.todosUsuarios()) {
            try {
                user.getSession().getBasicRemote().sendText(sala.toString());
            } catch (IOException ex) {
                System.out.println("Erro no sendBroadCast " + ex.toString());
            }
        }  
    }    
    
    @OnOpen
    public void conectar(Session ses, @PathParam("sala")String sala, @PathParam("usuario")String nome) throws IOException{
        //varifica se a sala existe e 
        if(salas.containsKey(sala)){
           // falta verificar se o usuários já está em alguma sala     
           //caso o nome já esteja sendo utilizado, é criado um nome alternativo seguindo nome_solicitado+número_de_pessoas_já_na_sala
           if(salas.get(sala).buscaPeloNome(nome) != null){
               nome += salas.get(sala).getTamanho();
               //informa o motivo do nome alternativo
               ses.getBasicRemote().sendText("O nome solicitado já está em uso, seu nome foi configurado como " + nome + " (Vocês pode renomear utilizando o comando [rename <novo_nome>])");
           }
           //adiciona um novo usuário na sala
            salas.get(sala).addUsuario(new Usuario(nome, ses, false));

           //notifica todo mundo sobre o usuário que entrou
            sendBroadCast(salas.get(sala), nome, "Entrou na sala!");        

       }else{
           //cria uma sala e atribui um criador 
           //falta verificar se o criador já está em outra sala
           salas.put(sala, new Sala(sala, new Usuario(nome, ses, true)));
           //informa para o criador que a sala foi criada
           ses.getBasicRemote().sendText("Sala: " + sala + " Criada!");
       }
        //Exibe os usuários online naquela sala
        usuariosOnline(salas.get(sala));

    }
    
    
    
    @OnMessage
    public void onMessage(Session ses, String message, @PathParam("sala")String sala) throws IOException {
        //quabrando a mensagem
        String[] list = message.split(" ");
        //pega as informações de quem mandou a mensagem
        Usuario remetente       = salas.get(sala).buscaPeloID(ses.getId());
        if (list[0].equals("send")) {
            if(list[1].equals("-u")){
                Usuario destinatario  = salas.get(sala).buscaPeloNome(list[2]);
                destinatario.getSession().getBasicRemote().sendText(remetente.getNome() + " | "  + new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime()) + " | reservadamente : " + message);
            }else{ 
                sendBroadCast(salas.get(sala), remetente.getNome(), message);
            }
   
        }else if(list[0].equals("rename")){
            if(salas.get(sala).buscaPeloNome(list[1]) != null){
               //informa o motivo do nome alternativo
               ses.getBasicRemote().sendText("O nome solicitado já está em uso!");
            }else{
                //muda o nome e notifica todo mundo
                String antigo_nome = remetente.getNome();
                salas.get(sala).alteraNome(remetente.getId(), list[1]);
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
        salas.get(sala).removeUsuario(ses.getId());
        //notifica todo mundo que alguém sai da sala
        sendBroadCast(salas.get(sala), usuario.getNome(), "Saiu da sala!");
        usuariosOnline(salas.get(sala));
        //se for o ultimo usuário da sala, deleta a sala
        if (salas.get(sala).salaVazia()) {
            salas.remove(sala);
        }
    }
}
