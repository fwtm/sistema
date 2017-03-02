/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.util;

import ec.gob.digercic.saureg.entities.TblSauregCompRol;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author daniel.porras
 */
public class GestorMenu implements Serializable{
    
    /**
     * Metodo que verifica si hay un elemento repetido en el menú
     * @param menus
     * @param Padsismen es el campo de rtecursividad
     * @return 
     */
    public static Boolean menuRepetido(TblSauregCompRol compRol, String repetido){
        Boolean resp = false;
        String rpe = compRol.getTblSauregRol().getIdRol() + "," +
                compRol.getTblSauregPermiso().getIdPermiso() + "," +
                compRol.getTblSauregEstrucSist().getIdEstrucSist();
        if(rpe.equals(repetido)){
            resp = true;
        }
        return resp;
    }
    
    /**
     * Metodo que verifica el número de elementos repetidos en el menú
     * @param menus
     * @param Padsismen es el campo de rtecursividad
     * @return 
     */
    public static List<String> elementosRepMenu(List<TblSauregCompRol> compRol, Long idEstrucSist){
        List<String> idcompRol = new ArrayList<String>();
        for(TblSauregCompRol item : compRol){
            if(item.getTblSauregEstrucSist().getIdEstrucSist().equals(idEstrucSist)){
                idcompRol.add(item.getTblSauregRol().getIdRol() + "," 
                        + item.getTblSauregPermiso().getIdPermiso() + "," 
                        + item.getTblSauregEstrucSist().getIdEstrucSist());
            }
        }
        return idcompRol;
    }
    
    public static List<TblSauregCompRol> compRolMenosDuplic(List<TblSauregCompRol> comRoles,
            List<String> comRoleDuplicados){
        List<TblSauregCompRol> paraEliminar = new ArrayList<TblSauregCompRol>();
        for(String itemRep : comRoleDuplicados){
            for(TblSauregCompRol item : comRoles){
                if(menuRepetido(item, itemRep)){
                    paraEliminar.add(item);
                    break;
                }
            }
        }
        for(TblSauregCompRol itemRemuve : paraEliminar){
            comRoles.remove(itemRemuve);
        }
        return comRoles;
    }
    
    /**
     * 
     * @param roles
     * @return 
     */
    public static List<TblSauregCompRol> quitarDuplicados(List<TblSauregCompRol> comRoles){
        int i = 0;
        int j = comRoles.size();
        while(i != j){
            TblSauregCompRol comRol = comRoles.get(i);
            List<String> elemRep = elementosRepMenu(comRoles, comRol.getTblSauregEstrucSist().getIdEstrucSist());
            if(elemRep.size() > 1){
                //Dejo solo un elemento
                elemRep.remove(0);
                comRoles = compRolMenosDuplic(comRoles,elemRep);
                i = 0;
                j = comRoles.size();
            }else{
                i++;
            }
        }
        return comRoles;
    }
}
