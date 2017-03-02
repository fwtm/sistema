package ec.gob.digercic.saureg.view;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.saureg.entities.TblSauregSistema;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.ejb.TblSauregSistemaFacadeLocal;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@ManagedBean(name = "tblSauregSistemaController")
@ViewScoped
public class TblSauregSistemaController implements Serializable {

    private TblSauregSistema current;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregSistemaFacadeLocal ejbFacade;

    public TblSauregSistemaController() {
    }

    public TblSauregSistema getSelected() {
        if (current == null) {
            current = new TblSauregSistema();
        }
        return current;
    }

    private TblSauregSistemaFacadeLocal getFacade() {
        return ejbFacade;
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

    @FacesConverter(forClass = TblSauregSistema.class)
    public static class TblSauregSistemaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                TblSauregSistemaController controller = (TblSauregSistemaController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "tblSauregSistemaController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TblSauregSistema) {
                TblSauregSistema o = (TblSauregSistema) object;
                return getStringKey(o.getIdSistema());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TblSauregSistema.class.getName());
            }
        }

    }

}
