package ec.gob.digercic.saureg.view;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.saureg.entities.TblSauregCompRol;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.ejb.TblSauregCompRolFacadeLocal;

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

@ManagedBean(name = "tblSauregCompRolController")
@ViewScoped
public class TblSauregCompRolController implements Serializable {

    private TblSauregCompRol current;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregCompRolFacadeLocal ejbFacade;

    public TblSauregCompRolController() {
    }

    public TblSauregCompRol getSelected() {
        if (current == null) {
            current = new TblSauregCompRol();
            current.setTblSauregCompRolPK(new ec.gob.digercic.saureg.entities.TblSauregCompRolPK());
        }
        return current;
    }

    private TblSauregCompRolFacadeLocal getFacade() {
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

    @FacesConverter(forClass = TblSauregCompRol.class)
    public static class TblSauregCompRolControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                TblSauregCompRolController controller = (TblSauregCompRolController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "tblSauregCompRolController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        ec.gob.digercic.saureg.entities.TblSauregCompRolPK getKey(String value) {
            ec.gob.digercic.saureg.entities.TblSauregCompRolPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new ec.gob.digercic.saureg.entities.TblSauregCompRolPK();
            key.setIdEstrucSist(Long.parseLong(values[0]));
            key.setIdRol(Long.parseLong(values[1]));
            key.setIdPermiso(Long.parseLong(values[2]));
            return key;
        }

        String getStringKey(ec.gob.digercic.saureg.entities.TblSauregCompRolPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdEstrucSist());
            sb.append(SEPARATOR);
            sb.append(value.getIdRol());
            sb.append(SEPARATOR);
            sb.append(value.getIdPermiso());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TblSauregCompRol) {
                TblSauregCompRol o = (TblSauregCompRol) object;
                return getStringKey(o.getTblSauregCompRolPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TblSauregCompRol.class.getName());
            }
        }

    }

}
