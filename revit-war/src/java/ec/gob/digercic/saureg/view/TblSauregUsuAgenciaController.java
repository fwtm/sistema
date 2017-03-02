package ec.gob.digercic.saureg.view;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.saureg.entities.TblSauregUsuAgencia;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.ejb.TblSauregUsuAgenciaFacadeLocal;

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

@ManagedBean(name = "tblSauregUsuAgenciaController")
@ViewScoped
public class TblSauregUsuAgenciaController implements Serializable {

    private TblSauregUsuAgencia current;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregUsuAgenciaFacadeLocal ejbFacade;

    public TblSauregUsuAgenciaController() {
    }

    public TblSauregUsuAgencia getSelected() {
        if (current == null) {
            current = new TblSauregUsuAgencia();
            current.setTblSauregUsuAgenciaPK(new ec.gob.digercic.saureg.entities.TblSauregUsuAgenciaPK());
        }
        return current;
    }

    private TblSauregUsuAgenciaFacadeLocal getFacade() {
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

    @FacesConverter(forClass = TblSauregUsuAgencia.class)
    public static class TblSauregUsuAgenciaControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                TblSauregUsuAgenciaController controller = (TblSauregUsuAgenciaController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "tblSauregUsuAgenciaController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        ec.gob.digercic.saureg.entities.TblSauregUsuAgenciaPK getKey(String value) {
            ec.gob.digercic.saureg.entities.TblSauregUsuAgenciaPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new ec.gob.digercic.saureg.entities.TblSauregUsuAgenciaPK();
            key.setIdAgencia(Long.parseLong(values[0]));
            key.setNomUsuario(values[1]);
            return key;
        }

        String getStringKey(ec.gob.digercic.saureg.entities.TblSauregUsuAgenciaPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdAgencia());
            sb.append(SEPARATOR);
            sb.append(value.getNomUsuario());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TblSauregUsuAgencia) {
                TblSauregUsuAgencia o = (TblSauregUsuAgencia) object;
                return getStringKey(o.getTblSauregUsuAgenciaPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TblSauregUsuAgencia.class.getName());
            }
        }

    }

}
