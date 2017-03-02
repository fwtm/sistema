package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.EstadoRegistroFallecido;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.view.util.JsfUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "estadoRegistroFallecidoLController")
@SessionScoped
public class EstadoRegistroFallecidoLController implements Serializable {

    private List<EstadoRegistroFallecido> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.EstadoRegistroFallecidoFacadeLocal ejbFacade;

    public EstadoRegistroFallecidoLController() {
    }

    @PostConstruct
    public void iniciar() {
        try{
            items = ejbFacade.findByNamedQueryResultList("CausaMuerteC.findAll");
            if(items.isEmpty()){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoDatosParaMostrar"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            }  
        }catch(Exception e){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaIniciar"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        }
    }
    
    public String prepareEdit() {
        return "Create";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        return "Create";
    }

    public List<EstadoRegistroFallecido> getItems() {
        if(items == null){
            items = new ArrayList<EstadoRegistroFallecido>();
        }
        return items;
    }


    public SelectItem[] getItemsAvailableSelectMany() {
        try{
            items.clear();
            items = ejbFacade.findByNamedQueryResultList("CausaMuerteC.findAll");
            return JsfUtil.getSelectItems(items, false);
        }catch(EntidadException ee){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return null;
        }   
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        try{
            items.clear();
            items = ejbFacade.findByNamedQueryResultList("CausaMuerteC.findAll");
            return JsfUtil.getSelectItems(items, true);
        }catch(EntidadException ee){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return null;
        }
    }

    @FacesConverter(forClass = EstadoRegistroFallecido.class)
    public static class EstadoRegistroFallecidoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                EstadoRegistroFallecidoLController controller = (EstadoRegistroFallecidoLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "estadoRegistroFallecidoController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoConverterObject"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("mssgsGrwl");
                return null;
            }
        }

        Integer getKey(String value) {
            Integer key;
            key = Integer.parseInt(value);
            return key;
        }

        String getStringKey(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EstadoRegistroFallecido) {
                EstadoRegistroFallecido o = (EstadoRegistroFallecido) object;
                return getStringKey(o.getIdEstRegFal());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EstadoRegistroFallecido.class.getName());
            }
        }

    }

}
