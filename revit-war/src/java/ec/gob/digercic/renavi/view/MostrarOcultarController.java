/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author daniel.porras
 */
@ManagedBean(name = "mostrarOcultarController")
@ViewScoped
public class MostrarOcultarController implements Serializable {

    private int edad;
    private String mensaje;
    private Date fechaNacimiento;
    
    @PostConstruct
    public void iniciar() {
        edad = 0;
    }

    public int getEdad() {
        return edad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    } 
    
    /**
     * Método para calcular la edad de una persona.
     *
     * @param fechaNacimiento Fecha de nacimiento de la persona.
     * @return Edad en años de la persona.
     */
    public short calcularEdad(Date fechaNacimiento) {
        Calendar fechaNacim = Calendar.getInstance();
        fechaNacim.setTime(fechaNacimiento);
        Calendar calFechaActual = Calendar.getInstance();
        int año = calFechaActual.get(Calendar.YEAR) - fechaNacim.get(Calendar.YEAR);
        int mes = calFechaActual.get(Calendar.MONTH) - fechaNacim.get(Calendar.MONTH);
        int dia = calFechaActual.get(Calendar.DATE) - fechaNacim.get(Calendar.DATE);
        if (mes < 0 || (mes == 0 && dia < 0)) {
            año--;
        }
        short edad = (short) año;
        return edad;
    }
    
    /**
     * Método para calcular la edad de la madre al seleccionar 
     * la fecha del calendario.
     *
     * @param event
     */
    public void seleccionFechaNacimiento(SelectEvent event) {
        edad = calcularEdad(fechaNacimiento);
        if(edad >= 18){
            mensaje = "Se muestra porque es mayor de edad.";
        }else{
            mensaje = null;
        }
    }
}
