package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.RespuestaMuerteenestablecim;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.view.util.JsfUtil;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "respuestaMuerteenestablecimCDUVController")
@ViewScoped
public class RespuestaMuerteenestablecimCDUVController implements Serializable {

    private RespuestaMuerteenestablecim current;
    @EJB
    private ec.gob.digercic.renavi.ejb.RespuestaMuerteenestablecimFacadeLocal ejbFacade;
    private String tituloPantalla;

    public RespuestaMuerteenestablecimCDUVController() {
    }

    @PostConstruct
    public void iniciar() {
        try{
            String operacion = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("operacion");
            if(operacion != null){
                Short id = Short.parseShort(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
                current = ejbFacade.find(id);
                tituloPantalla = "Editar tipo muerte C";
            }else{
                tituloPantalla = "Crear tipo muerte C";
            } 
        }catch(EntidadException ee){
            tituloPantalla = "Error en la pantalla";
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaIniciar"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        }
    }
    
    public String create() {
        try {
            ejbFacade.create(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("CreacionRegistro"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return prepareCreate();
        } catch (EntidadException ee) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCreacionRegistro"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return "";
        }
    }

    public String update() {
        try {
            ejbFacade.edit(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("EdicionRegistro"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return prepareList();
        } catch (EntidadException ee) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoEdicionRegistro"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return null;
        }
    }

    private void destroy() {
        try {
            ejbFacade.remove(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("EliminacionRegistro"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        } catch (EntidadException ee) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoEliminacionRegistro"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        }
    }
    
    public String prepareList() {
        return "List";
    }
    
    public String prepareEdit() {
        return "Edit";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        return "Create";
    }

    public RespuestaMuerteenestablecim getSelected() {
        if (current == null) {
            current = new RespuestaMuerteenestablecim();
        }
        return current;
    }

    public String getTituloPantalla() {
        return tituloPantalla;
    }

}
