/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.validator;

import ec.gob.digercic.renavi.view.util.JsfUtil;
import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@FacesValidator(value="passwordValidator")
public class PasswordValidator implements Validator, Serializable{

    public void validate(FacesContext context, UIComponent component, Object value)
        throws ValidatorException
    {
        String password = (String) value;
        UIInput confirmComponent = (UIInput) component.getAttributes().get("passwordComponent");
        String confirm = (String) confirmComponent.getValue();
        if (password == null || password.isEmpty() || confirm == null || confirm.isEmpty()) {
            return; 
        }
        if (!password.equals(confirm)) {
            confirmComponent.setValid(false); 
            JsfUtil.addErrorMessage("Contraseñas no coinciden.");
            RequestContext.getCurrentInstance().update("frm:msg");
            //para el login por primera vez
            RequestContext.getCurrentInstance().update("loginform:msg");
            
        }
    }

    
}
