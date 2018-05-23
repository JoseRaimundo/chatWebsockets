/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste.java;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;

/**
 *
 * @author gpds-gpu
 */
public class teste {
    public static void main(String[] args) {

//         //teste da api calendar
//        Calendar data = Calendar.getInstance();
//        System.out.println(data.getTime());


//        // teste da api split
//        String message = "José Raimundo Barbosa";
//         String[] list = message.split(" ");
//         
//         for (String string : list) {
//             System.out.println(string);
//        }


		HashMap<String, String> mapa = new HashMap<String, String>();
		mapa.put("Diegoo", " Ricardo");
		mapa.put(null, "Teste");
		mapa.put(null, "Outro Teste");
		mapa.put("Diego", " ;)");
		Set<String> chaves = mapa.keySet();
		for (Iterator<String> iterator = chaves.iterator(); iterator.hasNext();)
		{
			String chave = iterator.next();
		
				System.out.println(chave + mapa.get(chave));
		}
	}	

    
}
