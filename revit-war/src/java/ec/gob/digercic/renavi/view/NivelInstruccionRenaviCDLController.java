package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.NivelInstruccionRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.NivelInstruccionRenaviFacadeLocal;
import ec.gob.digercic.renavi.excepciones.EntidadException;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@ManagedBean(name = "nivelInstruccionRenaviCDLController")
@ViewScoped
public class NivelInstruccionRenaviCDLController implements Serializable {

    private NivelInstruccionRenavi current;
    private List<NivelInstruccionRenavi> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.NivelInstruccionRenaviFacadeLocal ejbFacade;

    public NivelInstruccionRenaviCDLController() {
    }

    public NivelInstruccionRenavi getSelected() {
        if (current == null) {
            current = new NivelInstruccionRenavi();
        }
        return current;
    }

    private NivelInstruccionRenaviFacadeLocal getFacade() {
        return ejbFacade;
    }

    public List<NivelInstruccionRenavi> getItems() {
        try{
            items = getFacade().findAll();
            return items;
        }catch(EntidadException ee){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
    }
    
    public SelectItem[] getItemsAvailableSelectMany() {
        try{
            return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
        }catch(EntidadException ee){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
        }   
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        try{
            return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
        }catch(EntidadException ee){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
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

    public String create() {
        try{
            getFacade().create(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NivelInstruccionRenaviCreado"), JsfUtil.INFO_MESSAGE);
            current = new NivelInstruccionRenavi();
            return "Create";
        }catch (EntidadException ee) {
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "Create";
        }
    }

    public String destroy() {
        try {
            Integer id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            current = getFacade().find(id);
            getFacade().remove(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NivelInstruccionRenaviEliminado"), JsfUtil.INFO_MESSAGE);
            return "List";
        } catch (EntidadException ee) {
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "List";
        }
    }

    @FacesConverter(forClass = NivelInstruccionRenavi.class)
    public static class NivelInstruccionRenaviControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                NivelInstruccionRenaviCDLController controller = (NivelInstruccionRenaviCDLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "nivelInstruccionRenaviCDLController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
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
            if (object instanceof NivelInstruccionRenavi) {
                NivelInstruccionRenavi o = (NivelInstruccionRenavi) object;
                return getStringKey(o.getIdNivIns());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + NivelInstruccionRenavi.class.getName());
            }
        }
    }
}
