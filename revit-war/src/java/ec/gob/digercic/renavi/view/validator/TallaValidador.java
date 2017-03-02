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
@FacesValidator(value = "tallaValidador")
public class TallaValidador implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String valorIng = value.toString();
		if (valorIng != null) {
			Double talla = new Double(valorIng);
                        if(talla < 38.0 || talla > 52.0){
                            FacesMessage msg = new FacesMessage("El rango de la talla no es válida");
                                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                                    throw new ValidatorException(msg);
                        }
		}
	}

}
