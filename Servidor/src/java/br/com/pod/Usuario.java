package br.com.pod;


import javax.websocket.Session;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pc
 */
public class Usuario {
    //session para armazenar as informações de conexão
    private Session session;
    //id para o usuario
    private String  id;
    //alias para o usuario 
    private String  nome_user;
    //status flag para determinar se ele é o criador
    private boolean status;
    
    public Usuario(String nome, Session session, boolean status){
        this.id = session.getId();
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
        return this.id;
    }
    
    public Session getSession(){
        return this.session;
    }

    @Override
    public String toString() {
        return "Usuario{" + "session=" + session + ", nome_key=" + id + ", nome_user=" + nome_user + ", status=" + status + '}';
    }
   
}
