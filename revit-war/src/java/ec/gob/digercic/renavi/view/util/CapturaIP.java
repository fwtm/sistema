/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author santiago.tapia
 * 
 * NOTA: servletRequest.getRemoteAddr() obtiene la direccion IP del cliente que accesa a la aplicacion web, sin embargo,
 * si el usuario se oculta tras un servidor proxy o accesa a travez de una balanceador de carga (ej. cloud hosting), 
 * servletRequest.getRemoteAddr() obtendra la direccion IP del servidor proxy o el servidor de balanceo de carga, no la 
 * IP original del cliente; para solucionar este inconveniente, usamos el header X-FORWARDED-FOR.
 * 
 */
public class CapturaIP {

    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    String ipAddress = request.getHeader("X-FORWARDED-FOR");

    public String obieneDireccionIP() {
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        System.out.println("ipAddress(local):" + ipAddress); 
        return ipAddress;  
    }
    
  
}
