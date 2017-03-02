package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.PadreRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.PadreRenaviFacadeLocal;
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

@ManagedBean(name = "padreRenaviController")
@ViewScoped
public class PadreRenaviCDLController implements Serializable {

    private PadreRenavi current;
    private List<PadreRenavi> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.PadreRenaviFacadeLocal ejbFacade;

    public PadreRenaviCDLController() {
    }

    public PadreRenavi getSelected() {
        if (current == null) {
            current = new PadreRenavi();
        }
        return current;
    }

    private PadreRenaviFacadeLocal getFacade() {
        return ejbFacade;
    }
    
    public List<PadreRenavi> getItems() {
        try{
            items = getFacade().findAll();
            return items;
        }catch(EntidadException ee){
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
    }
    
    public SelectItem[] getItemsAvailableSelectMany() {
        try{
            return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
        }catch(EntidadException ee){
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
        }   
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        try{
            return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
        }catch(EntidadException ee){
            ee.printStackTrace();
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
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("PadreRenaviCreado"), JsfUtil.INFO_MESSAGE);
            current = new PadreRenavi();
            return "Create";
        }catch (Exception ee) {
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "Create";
        }
    }

    public String destroy() {
        try {
            Integer id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            current = getFacade().find(id);
            getFacade().remove(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("PadreRenaviEliminado"), JsfUtil.INFO_MESSAGE);
            return "List";
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "List";
        }
    }

    @FacesConverter(forClass = PadreRenavi.class)
    public static class PadreRenaviControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                PadreRenaviCDLController controller = (PadreRenaviCDLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "padreRenaviCDLController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        Long getKey(String value) {
            Long key;
            key = Long.parseLong(value);
            return key;
        }

        String getStringKey(Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PadreRenavi) {
                PadreRenavi o = (PadreRenavi) object;
                return getStringKey(o.getIdPad());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PadreRenavi.class.getName());
            }
        }

    }

}
