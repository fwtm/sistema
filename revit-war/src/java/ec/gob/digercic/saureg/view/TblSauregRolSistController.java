package ec.gob.digercic.saureg.view;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.saureg.entities.TblSauregRolSist;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.ejb.TblSauregRolSistFacadeLocal;

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

@ManagedBean(name = "tblSauregRolSistController")
@ViewScoped
public class TblSauregRolSistController implements Serializable {

    private TblSauregRolSist current;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregRolSistFacadeLocal ejbFacade;

    public TblSauregRolSistController() {
    }

    public TblSauregRolSist getSelected() {
        if (current == null) {
            current = new TblSauregRolSist();
            current.setTblSauregRolSistPK(new ec.gob.digercic.saureg.entities.TblSauregRolSistPK());
        }
        return current;
    }

    private TblSauregRolSistFacadeLocal getFacade() {
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

    @FacesConverter(forClass = TblSauregRolSist.class)
    public static class TblSauregRolSistControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                TblSauregRolSistController controller = (TblSauregRolSistController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "tblSauregRolSistController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        ec.gob.digercic.saureg.entities.TblSauregRolSistPK getKey(String value) {
            ec.gob.digercic.saureg.entities.TblSauregRolSistPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new ec.gob.digercic.saureg.entities.TblSauregRolSistPK();
            key.setIdRol(Long.parseLong(values[0]));
            key.setIdSistema(Long.parseLong(values[1]));
            return key;
        }

        String getStringKey(ec.gob.digercic.saureg.entities.TblSauregRolSistPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdRol());
            sb.append(SEPARATOR);
            sb.append(value.getIdSistema());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TblSauregRolSist) {
                TblSauregRolSist o = (TblSauregRolSist) object;
                return getStringKey(o.getTblSauregRolSistPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TblSauregRolSist.class.getName());
            }
        }

    }

}
