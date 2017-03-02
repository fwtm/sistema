package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.TipoInscripcionRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.TipoInscripcionRenaviFacadeLocal;
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

@ManagedBean(name = "tipoInscripcionRenaviCDLController")
@ViewScoped
public class TipoInscripcionRenaviCDLController implements Serializable {

    private TipoInscripcionRenavi current;
    private List<TipoInscripcionRenavi> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.TipoInscripcionRenaviFacadeLocal ejbFacade;

    public TipoInscripcionRenaviCDLController() {
    }

    public TipoInscripcionRenavi getSelected() {
        if (current == null) {
            current = new TipoInscripcionRenavi();
        }
        return current;
    }

    private TipoInscripcionRenaviFacadeLocal getFacade() {
        return ejbFacade;
    }
    
    public List<TipoInscripcionRenavi> getItems() {
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
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("TipoInscripcionRenaviCreado"), JsfUtil.INFO_MESSAGE);
            current = new TipoInscripcionRenavi();
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
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("TipoInscripcionRenaviEliminado"), JsfUtil.INFO_MESSAGE);
            return "List";
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "List";
        }
    }

    @FacesConverter(forClass = TipoInscripcionRenavi.class)
    public static class TipoInscripcionRenaviControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                TipoInscripcionRenaviCDLController controller = (TipoInscripcionRenaviCDLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "tipoInscripcionRenaviCDLController");
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
            if (object instanceof TipoInscripcionRenavi) {
                TipoInscripcionRenavi o = (TipoInscripcionRenavi) object;
                return getStringKey(o.getIdTipIns());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoInscripcionRenavi.class.getName());
            }
        }

    }

}
