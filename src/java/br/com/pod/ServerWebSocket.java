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
 * consertar: Verificar forma de mudar o nome vinculado a session
 * @author talitha
 */
@ServerEndpoint("/chat/{sala}/{usuario}")
public class ServerWebSocket {
    
    //ver possibilidades de usar duas maps para proibir usuário em mais chats
    
    //aqui será uma map1<map, string> de map2<session, string>    
    //map1 = chat_geral<nome_do_sala, sala> 
    //map2 = sala_do_chat<nome_usuario, session_usuario>    
    private static HashMap<String, HashMap<String, Session>> geral_salas = new HashMap<String, HashMap<String, Session>>();
    private static HashMap<String, Boolean> geral_usuarios = new HashMap<String, Boolean>();

    @OnOpen
    public void conectar(Session ses, @PathParam("sala")String sala, @PathParam("usuario")String usuario){
       System.out.println("Alguém tentou se conctar");
       if(geral_salas.containsKey(sala)){
           geral_salas.get(sala).put(usuario, ses);
       }else{
           HashMap<String, Session> sala_temp = new HashMap<>();
           sala_temp.put(usuario, ses);
           geral_salas.put(sala, sala_temp);
       }
    }
    
    @OnMessage
    public void onMessage(String message, @PathParam("sala")String sala, @PathParam("usuario")String usuario) throws IOException {
        Calendar data = Calendar.getInstance();
        String[] list = message.split(" ");

        if (list[0].equals("send")) {
            if(list[1].equals("-u")){
                Session temp_ses  = geral_salas.get(sala).get(list[2]);
                temp_ses.getBasicRemote().sendText(usuario + " | "  + data.getTime() + " | reservadamente : " + message);
            }else{
                for (Session ses : geral_salas.get(sala).values()) {
                    ses.getBasicRemote().sendText(usuario + " | "  + data.getTime() + " : " + message);
                }
            }
   
        }else if(list[0].equals("rename")){
            Session temp_ses = geral_salas.get(sala).get(usuario);
            geral_salas.get(sala).remove(usuario);
            geral_salas.get(sala).put(list[1], temp_ses);
            for (Session ses : geral_salas.get(sala).values()) {
                    ses.getBasicRemote().sendText(usuario + " | rename to > " + list[1] + data.getTime() + " : ");
            }
        }
   
    }
    @OnClose
    public void desconectar(Session ses){
         
    }
}
