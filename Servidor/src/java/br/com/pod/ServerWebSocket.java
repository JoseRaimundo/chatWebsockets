/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pod;


import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
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
 
    private static HashMap<String, Sala> salas = new HashMap<String, Sala>();

    @OnOpen
    public void conectar(Session ses, @PathParam("sala")String sala, @PathParam("usuario")String usuario) throws IOException{
       if(salas.containsKey(sala)){
           if(salas.get(sala).buscaPeloNome(usuario) != null){
               //cria com nome alternativo
           }else{
               // falta verificar se o usuários já está em alguma sala
               salas.get(sala).addUsuario(new Usuario(usuario, ses, false));
           }
       }else{
           //cria uma sala e um criador 
           //falta verificar se o criador já está em outra sala
           salas.put(sala, new Sala(sala, new Usuario(usuario, ses, true)));
       }
    }
    
    @OnMessage
    public void onMessage(Session ses, String message, @PathParam("sala")String sala) throws IOException {
        //pega a hora local
        Calendar data = Calendar.getInstance();
        //quabrando a mensagem
        String[] list = message.split(" ");
        //pega as informações de quem mandou a mensagem
        Usuario remetente       = salas.get(sala).buscaPeloID(ses.getId());
        System.out.println(">>>>>>>>> " + remetente.getNome());
        if (list[0].equals("send")) {
            if(list[1].equals("-u")){
                Usuario destinatario  = salas.get(sala).buscaPeloNome(list[2]);
                destinatario.getSession().getBasicRemote().sendText(remetente.getNome() + " | "  + data.getTime() + " | reservadamente : " + message);
            }else{               
                for (Usuario user : salas.get(sala).todosUsuarios()) {
                    user.getSession().getBasicRemote().sendText(remetente.getNome() + " | "  + data.getTime() + " : " + message);
                }
            }
   
        }else if(list[0].equals("rename")){
            String antigo_nome = remetente.getNome();
            salas.get(sala).alteraNome(remetente.getId(), list[1]);
            for (Usuario user : salas.get(sala).todosUsuarios()) {
                 user.getSession().getBasicRemote().sendText(antigo_nome + " | " + data.getTime() + " | rename > " + list[1] );
            }
        }
   
    }
    @OnClose
    public void desconectar(Session ses){
         
    }
}
