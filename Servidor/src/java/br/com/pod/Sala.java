/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pod;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author raimundo
 */
public class Sala {

    private HashMap<String, Usuario> sala = new HashMap<String, Usuario>();;
    
    public Sala(String nome, Usuario criador){
        
        this.sala.put(criador.getId(), criador);
    }
    
    public void addUsuario(Usuario usuario){
        this.sala.put(usuario.getId(), usuario);
    }
       
    public Usuario buscaPeloID(String id){
        if (sala.containsKey(id)) {
            return sala.get(id);
        }
        return null;
    }
    
    public Usuario buscaPeloNome (String nome){
        Set<String> chaves = sala.keySet();
        for (String chave : chaves) {
            if(sala.get(chave).getNome().equals(nome)){
                return sala.get(chave);
            }
        }
        return null;
    }
    
    public Collection<Usuario> todosUsuarios(){
        return sala.values();
    }
    
   
    public void alteraNome(String id, String novo_nome){
        sala.get(id).setNomeUser(novo_nome);
    }
}
