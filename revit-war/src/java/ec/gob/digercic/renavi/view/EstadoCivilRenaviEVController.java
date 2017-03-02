package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.EstadoCivilRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.EstadoCivilRenaviFacadeLocal;
import ec.gob.digercic.renavi.excepciones.EntidadException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "estadoCivilRenaviEVController")
@ViewScoped
public class EstadoCivilRenaviEVController implements Serializable {

    private EstadoCivilRenavi current;
    @EJB
    private ec.gob.digercic.renavi.ejb.EstadoCivilRenaviFacadeLocal ejbFacade;
    private String operacion;
    private String tituloPantalla;
    private BigDecimal id;

    public EstadoCivilRenaviEVController() {
    }

    public EstadoCivilRenavi getSelected() {
        return current;
    }

    private EstadoCivilRenaviFacadeLocal getFacade() {
        return ejbFacade;
    }
	
	public String getOperacion() {
        return operacion;
    }

    public String getTituloPantalla() {
        return tituloPantalla;
    }
    
    @PostConstruct
    public void iniciar() {
        try{
            operacion = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("operacion");
            if(operacion.equals("ver")){
                id = new BigDecimal(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idEstCiv"));
                current = getFacade().find(id);
                tituloPantalla = "Ver catalogo general";
            }else if(operacion.equals("editar")){
                id = new BigDecimal(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idEstCiv"));
                current = getFacade().find(id);
                tituloPantalla = "Editar catalogo";
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            current = new EstadoCivilRenavi();
            operacion = null;
            tituloPantalla = "No hay operaci�n seleccionada";
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("SeleccionDeOperacionNula"), JsfUtil.ERROR_MESSAGE);
        }
    }

   

    public String prepareList() {
        return "List";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        return "Create";
    }

    public String prepareEdit() {
        return "Edit";
    }

	public String editar(){
        try{
            //Seteo el usuario actualiza y la fecha de actualizaci�n
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            Date fechaActual = new Date();
            //current.setCatGenFechaActualiza(fechaActual);
            getFacade().edit(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("EstadoCivilRenaviActualizado"), JsfUtil.INFO_MESSAGE);
            return "View";
        }catch (EntidadException ee) {
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "/index";
        }
    }

 

}
