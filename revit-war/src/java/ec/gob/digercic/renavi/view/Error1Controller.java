/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.Error1RenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.Error1;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "error1Controller")
@ViewScoped
public class Error1Controller implements Serializable{

    @EJB
    private Error1RenaviFacadeLocal ejbErrorAnulacion;

    /**
     * @return the ejbErrorAnulacion
     */
    public Error1RenaviFacadeLocal getEjbErrorAnulacion() {
        return ejbErrorAnulacion;
    }

    /**
     * @param ejbErrorAnulacion the ejbErrorAnulacion to set
     */
    public void setEjbErrorAnulacion(Error1RenaviFacadeLocal ejbErrorAnulacion) {
        this.ejbErrorAnulacion = ejbErrorAnulacion;
    }

    @FacesConverter(forClass = Error1.class, value = "a")
    public static class Error1ControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            //System.out.println("-ENTRA AL CONVERTER DE INSTITUCION getAsObject");
            if (value == null || value.length() == 0) {
                return null;
            }
            Error1Controller controller = (Error1Controller) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "error1Controller");
            try {           
                System.out.println("-RETORNA DEL CONVERTER DE error, EL OBJETO: " + controller.ejbErrorAnulacion.find(getKey(value)).getDescErrorAnul()  + " - " + controller.ejbErrorAnulacion.find(getKey(value)).getClass());
                return controller.ejbErrorAnulacion.find(getKey(value));
            } catch (EntidadException ex) {
                Logger.getLogger(Error1Controller.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
           
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Error1) {
                Error1 o = (Error1) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Error1.class.getName());
            }
        }
    }
}
