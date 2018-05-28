package br.com.pod;


import javax.websocket.Session;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gpds-gpu
 */
public class Usuario {
    private Session session;
    private String  nome_key;
    private String  nome_user;
    private boolean status;
    
    public Usuario(String nome, Session session, boolean status){
        this.nome_key = session.getId();
        this.nome_user = nome;
        this.session = session;
        this.status = true;        
    }
    
    public void setNomeUser(String nome){
        this.nome_user = nome;
    }
    
    public String getNome(){
        return this.nome_user;
    }
    
    public String getId(){
        return this.nome_key;
    }
    
    public void setStatus(boolean status){
        this.status = status;
    }
    
   
    
    public boolean setStatus(){
        return this.status;
    }
    
    public Session getSession(){
        return this.session;
    }
    
   
}
