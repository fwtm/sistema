package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.IdentificacionEtnicaRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.IdentificacionEtnicaRenaviFacadeLocal;
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

@ManagedBean(name = "identificacionEtnicaRenaviEVController")
@ViewScoped
public class IdentificacionEtnicaRenaviEVController implements Serializable {

    private IdentificacionEtnicaRenavi current;
    @EJB
    private ec.gob.digercic.renavi.ejb.IdentificacionEtnicaRenaviFacadeLocal ejbFacade;
    private String operacion;
    private String tituloPantalla;
    private BigDecimal id;
	
    public IdentificacionEtnicaRenaviEVController() {
    }

    public IdentificacionEtnicaRenavi getSelected() {
        return current;
    }

    private IdentificacionEtnicaRenaviFacadeLocal getFacade() {
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
                id = new BigDecimal(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idIdeEtn"));
                current = getFacade().find(id);
                tituloPantalla = "Ver catalogo general";
            }else if(operacion.equals("editar")){
                id = new BigDecimal(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idIdeEtn"));
                current = getFacade().find(id);
                tituloPantalla = "Editar catalogo";
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            current = new IdentificacionEtnicaRenavi();
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
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("IdentificacionEtnicaRenaviActualizado"), JsfUtil.INFO_MESSAGE);
            return "View";
        }catch (EntidadException ee) {
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "/index";
        }
    }


}
