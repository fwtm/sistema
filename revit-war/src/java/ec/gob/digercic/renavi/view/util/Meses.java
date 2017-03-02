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
public class Meses {

    private int idMes;
    private String descripcion;
    
    public Meses() {
        this.idMes = 0;
        this.descripcion = "";

    }
    
    public Meses(int idMes, String descripcion) {
        this.idMes = idMes;
        this.descripcion = descripcion;

    }

    public int getIdMes() {
        return idMes;
    }

    public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }



}
