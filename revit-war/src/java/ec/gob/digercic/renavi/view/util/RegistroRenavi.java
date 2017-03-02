/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.view.util;

/**
 *
 * @author henry.aguilar
 */
public class RegistroRenavi {
    
    private int id;
    private String descripcion;
    
    
    
    public RegistroRenavi(){
    }
  public RegistroRenavi(int id, String descripcion){
      this.id = id;
      this.descripcion = descripcion;
      
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcio(String descripcion) {
        
        this.descripcion = descripcion;
    }

    
}
