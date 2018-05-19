/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author talitha
 */
@ServerEndpoint("/chat/{sala}/{usuario}")
public class ServerWebSocket {

    
    @OnOpen
    public void conectar(Session ses, @PathParam("sala")String sala, @PathParam("usuario")String usuario){
       
    }
    @OnMessage
    public void onMessage(String message) {
        
    }
    @OnClose
    public void desconectar(Session ses){
        
    }
}
