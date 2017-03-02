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
@FacesValidator(value = "pesoValidador")
public class PesoValidador implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String valorIng = value.toString();
		if (valorIng != null) {
			Double peso = new Double(valorIng);
                        if(peso < 500.0 || peso > 5000.0){
                            FacesMessage msg = new FacesMessage("El rango para el peso no es válido");
                                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                                    throw new ValidatorException(msg);
                        }
		}
	}

}
