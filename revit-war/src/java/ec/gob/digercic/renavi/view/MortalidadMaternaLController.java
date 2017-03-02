package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.MortalidadMaterna;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.excepciones.EntidadException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "mortalidadMaternaLController")
@ViewScoped
public class MortalidadMaternaLController implements Serializable {

    private List<MortalidadMaterna> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.MortalidadMaternaFacadeLocal ejbFacade;

    public MortalidadMaternaLController() {
    }

    @PostConstruct
    public void iniciar() {
        try{
            items = ejbFacade.findByNamedQueryResultList("MortalidadMaterna.findAll");
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

    public List<MortalidadMaterna> getItems() {
        if(items == null){
            items = new ArrayList<MortalidadMaterna>();
        }
        return items;
    }


    public SelectItem[] getItemsAvailableSelectMany() {
        try{
            items.clear();
            items = ejbFacade.findByNamedQueryResultList("MortalidadMaterna.findAll");
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
            items = ejbFacade.findByNamedQueryResultList("MortalidadMaterna.findAll");
            return JsfUtil.getSelectItems(items, true);
        }catch(EntidadException ee){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return null;
        }
    }

    @FacesConverter(forClass = MortalidadMaterna.class)
    public static class MortalidadMaternaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                MortalidadMaternaLController controller = (MortalidadMaternaLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "mortalidadMaternaLController");
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
            if (object instanceof MortalidadMaterna) {
                MortalidadMaterna o = (MortalidadMaterna) object;
                return getStringKey(o.getIdMorMat());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + MortalidadMaterna.class.getName());
            }
        }

    }

}
