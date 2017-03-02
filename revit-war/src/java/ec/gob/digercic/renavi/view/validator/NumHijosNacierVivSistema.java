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
@FacesValidator(value = "numHijosNacierVivSistema")
public class NumHijosNacierVivSistema implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String valorIng = value.toString();
		if (valorIng != null) {
			Double valor = new Double(valorIng);
                        if(valor < 1.0 || valor > 10.0){
                            FacesMessage msg = new FacesMessage("El número de hijos que nacieron vivos en este parto no es válido.");
                                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                                    throw new ValidatorException(msg);
                        }
		}
    }
    
}
