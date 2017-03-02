package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.IdentificacionEtnicaRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.IdentificacionEtnicaRenaviFacadeLocal;
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

@ManagedBean(name = "identificacionEtnicaRenaviCDLController")
@ViewScoped
public class IdentificacionEtnicaRenaviCDLController implements Serializable {

    private IdentificacionEtnicaRenavi current;
    private List<IdentificacionEtnicaRenavi> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.IdentificacionEtnicaRenaviFacadeLocal ejbFacade;

    public IdentificacionEtnicaRenaviCDLController() {
    }

    public IdentificacionEtnicaRenavi getSelected() {
        if (current == null) {
            current = new IdentificacionEtnicaRenavi();
        }
        return current;
    }

    private IdentificacionEtnicaRenaviFacadeLocal getFacade() {
        return ejbFacade;
    }
    
    public List<IdentificacionEtnicaRenavi> getItems() {
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
            if(items == null){
                items = ejbFacade.findAll();
            }
            return JsfUtil.getSelectItems(items, true);
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
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("IdentificacionEtnicaRenaviCreado"), JsfUtil.INFO_MESSAGE);
            current = new IdentificacionEtnicaRenavi();
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
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("IdentificacionEtnicaRenaviEliminado"), JsfUtil.INFO_MESSAGE);
            return "List";
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "List";
        }
    }

    @FacesConverter(forClass = IdentificacionEtnicaRenavi.class)
    public static class IdentificacionEtnicaRenaviControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                IdentificacionEtnicaRenaviCDLController controller = (IdentificacionEtnicaRenaviCDLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "identificacionEtnicaRenaviCDLController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                ee.printStackTrace();
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
            if (object instanceof IdentificacionEtnicaRenavi) {
                IdentificacionEtnicaRenavi o = (IdentificacionEtnicaRenavi) object;
                return getStringKey(o.getIdIdeEtn());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + IdentificacionEtnicaRenavi.class.getName());
            }
        }

    }

}
