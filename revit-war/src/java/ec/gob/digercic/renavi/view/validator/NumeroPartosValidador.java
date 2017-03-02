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
@FacesValidator(value = "numeroPartosValidador")
public class NumeroPartosValidador implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String valorIng = value.toString();
		if (valorIng != null) {
			Double semanas = new Double(valorIng);
                        if(semanas < 1.0 || semanas > 19.0){
                            FacesMessage msg = new FacesMessage("El número de partos no es válido.");
                                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                                    throw new ValidatorException(msg);
                        }
		}
    }
    
}
