/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.view.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daniel.porras
 */
public class UtilitarioNombre {
    
    public static List<String> separacion(String nombre) {
        nombre.trim();
        List<String> nombres = new ArrayList<String>();
        String[] nmbrSplit = nombre.split(" ");
        StringBuilder articulos = new StringBuilder();
        for(int r = 0; r < nmbrSplit.length; r++){
            switch(nmbrSplit[r]){
                case "DE":
                    articulos.append(nmbrSplit[r]);
                    break;
                case "DEL":
                    articulos.append(nmbrSplit[r]);
                    break;
                case "EL":
                    articulos.append(nmbrSplit[r]);
                    break;
                case "LA":
                    articulos.append(nmbrSplit[r]);
                    break;
                case "LAS":
                    articulos.append(nmbrSplit[r]);
                    break;
                case "LOS":
                    articulos.append(nmbrSplit[r]);
                    break;
                case "CH":
                    articulos.append(nmbrSplit[r]);
                    break;
                case "LL":
                    articulos.append(nmbrSplit[r]);
                    break;
                default:
                    if(!articulos.toString().equals("")){
                        nombres.add(articulos.toString().trim() + " " + nmbrSplit[r]);
                        articulos = new StringBuilder();
                        continue;
                    } 
                    break;
            }
            if(!articulos.toString().equals("")){
                articulos.append(" ");
            }else{
                nombres.add(nmbrSplit[r]);
            }
        }
        List<String> tempNombres = new ArrayList<String>();
        if(nombres.size() >= 4){
            tempNombres.add(nombres.get(0) + " " + nombres.get(1));
            StringBuilder all = new StringBuilder();
            for(int t = 3; t < nombres.size(); t++){
                all.append(nombres.get(t));
                all.append(" ");
            }
            tempNombres.add(nombres.get(2) + " " + all.toString().trim());
            nombres = null;
            nombres = tempNombres;
        }else if(nombres.size() == 3){
            tempNombres.add(nombres.get(0) + " " + nombres.get(1));
            tempNombres.add(nombres.get(2));
            nombres = null;
            nombres = tempNombres;
        }/*else if(nmbrSplit.length == 2){
            tempNombres.add(nombres.get(0));
            tempNombres.add(nombres.get(1));
        }*/
        return nombres;
    }
    
    /*public static void main(String[] args){
        //List<String> s = separacion("FERNANDEZ DE CORDOVA MONCAYO DAVID FERNANDO");
        //List<String> s = separacion("ESPINOSA DE LOS MONTEROS JARAMILLO CARLOS ALFONSO");
        //List<String> s = separacion("DE LA CUADRA LOGRONO MARIA DEL PILAR");
        //List<String> s = separacion("DE LOS SANTOS DE LA CUADRA MARIA DE LAS MERCEDES");
        //List<String> s = separacion("DE LOS ALTOS MONTES FERNANDEZ DE CORDOVA CARMEN MARIA DE LOS ANGELES");
        //List<String> s = separacion("DE LA A ALAY ANGELA BALTASARA DE LOY");
        List<String> s = separacion("PORRAS ORTIZ GABRIELA ELIZABETH");
        //List<String> s = separacion("DE LOS SANTOS DE LOS SANTOS BALTASARA DE LOS ANGELES");
        //List<String> s = separacion("DE LOS SANTOS DE LA A BALTASARA DE LOS ANGELES");
        for(int i = 0; i < s.size(); i++){
            System.out.println(i + " " + s.get(i));
        }
    }*/
}
