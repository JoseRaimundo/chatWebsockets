/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pod;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author talitha
 */
@ServerEndpoint("/chat")
public class ServerWebSocket {

    @OnMessage
    public String onMessage(String message) {
        return null;
    }
    
}
