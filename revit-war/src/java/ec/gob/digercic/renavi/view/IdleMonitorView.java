/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.view;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author daniel.porras
 */
@ManagedBean(name = "idleMonitorView")
public class IdleMonitorView {
    
    public void onIdle() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "No se ha detectado actividad.", "Su sesión ha finalizado"));
    }
 
    public void onActive() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "Bienvenido nuevamente al sistema", null));
    }
}
