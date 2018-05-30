/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pod;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author raimundo
 */
public class Sala {

    private HashMap<String, Usuario> sala = new HashMap<String, Usuario>();
    private String nome;
    
    public Sala(String nome, Usuario criador){
        this.sala.put(criador.getId(), criador);
        this.nome = nome;
    }
    
    //adiciona um usuário
    public void addUsuario(Usuario usuario){
        this.sala.put(usuario.getId(), usuario);
    }
    
    public String getNome(){
        return this.nome;
    }
       
    
    //busca um usuário pelo id
    public Usuario buscaPeloID(String id){
        if (sala.containsKey(id)) {
            return sala.get(id);
        }
        return null;
    }
    
    //busca um usuário pelo nome (alias)
    public Usuario buscaPeloNome (String nome){
        Set<String> chaves = sala.keySet();
        for (String chave : chaves) {
            if(sala.get(chave).getNome().equals(nome)){
                return sala.get(chave);
            }
        }
        return null;
    }
    
    //retorna todos os usuários na sala
    public Collection<Usuario> todosUsuarios(){
        return sala.values();
    }
    
    //altera o nome do usuário (a alias)
    public void alteraNome(String id, String novo_nome){
        sala.get(id).setNomeUser(novo_nome);
    }
    
    
    //remove um usuário pelo id, utilizado quando ele desconecta
    public void removeUsuario(String id){
        sala.remove(id);
    }
    
    //verifica se a sala esta vazia
    public boolean salaVazia(){
        if (sala.isEmpty()) {
            return true;
        }
        return false;
    }
    
    //retorna o tamanho da sala para a
    public int getTamanho(){
        return sala.size();
    }

    @Override
    public String toString() {
        String saida = "$Sala: " + nome + "\n";
        Set<String> chaves = sala.keySet();
        for (String chave : chaves) {
            saida += sala.get(chave).getNome()+ "\n";
        }
        return saida;
    }

    
    
    
    
}
