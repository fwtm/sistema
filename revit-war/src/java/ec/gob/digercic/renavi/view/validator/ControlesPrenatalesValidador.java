/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.validator;

import ec.gob.digercic.renavi.view.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author santiago.tapia
 */
@FacesValidator(value = "controlesPrenatalesValidador")
public class ControlesPrenatalesValidador implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value != null) {
            String valorIng = value.toString().trim();
            if (!valorIng.equals("")) {
                Integer semanas = new Integer(valorIng);
                if (semanas < 0 || semanas > 25) {
                    FacesMessage msg = new FacesMessage("El número de controles prenatales no es válido.");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }
        }
    }
}
