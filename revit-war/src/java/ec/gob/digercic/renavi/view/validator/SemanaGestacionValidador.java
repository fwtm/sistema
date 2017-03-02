package ec.gob.digercic.renavi.view.validator;

import ec.gob.digercic.renavi.view.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


/**
 * Validador de numeros
 * 
 * @author JAVA
 * 
 */
@FacesValidator(value = "semanaGestacionValidador")
public class SemanaGestacionValidador implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String valorIng = value.toString();
		if (valorIng != null) {
			Double semanas = new Double(valorIng);
                        if(semanas < 22.0 || semanas > 42.0){
                            FacesMessage msg = new FacesMessage("El número de semanas de gestación no es válido.");
                                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                                    throw new ValidatorException(msg);
                        }
		}
	}

}
