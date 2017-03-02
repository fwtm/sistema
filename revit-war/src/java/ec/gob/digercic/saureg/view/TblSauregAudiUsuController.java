package ec.gob.digercic.saureg.view;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.saureg.entities.TblSauregAudiUsu;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.ejb.TblSauregAudiUsuFacadeLocal;

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

@ManagedBean(name = "tblSauregAudiUsuController")
@ViewScoped
public class TblSauregAudiUsuController implements Serializable {

    private TblSauregAudiUsu current;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregAudiUsuFacadeLocal ejbFacade;

    public TblSauregAudiUsuController() {
    }

    public TblSauregAudiUsu getSelected() {
        if (current == null) {
            current = new TblSauregAudiUsu();
        }
        return current;
    }

    private TblSauregAudiUsuFacadeLocal getFacade() {
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

    @FacesConverter(forClass = TblSauregAudiUsu.class)
    public static class TblSauregAudiUsuControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                TblSauregAudiUsuController controller = (TblSauregAudiUsuController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "tblSauregAudiUsuController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        java.math.BigDecimal getKey(String value) {
            java.math.BigDecimal key;
            key = new java.math.BigDecimal(value);
            return key;
        }

        String getStringKey(java.math.BigDecimal value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TblSauregAudiUsu) {
                TblSauregAudiUsu o = (TblSauregAudiUsu) object;
                return getStringKey(o.getIdAudiUsu());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TblSauregAudiUsu.class.getName());
            }
        }

    }

}
