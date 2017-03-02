package ec.gob.digercic.renavi.view.validator;

import ec.gob.digercic.renavi.view.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validador para el rango de valor que tiene que tener las notas
* @author Daniel Porras
*/

public class RangoValidador implements Validator 
{
	public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
		String valmin = (String)uic.getAttributes().get("min");
		String valmax = (String)uic.getAttributes().get("max");
		String strValue = (String)o;
		try
		{
			int min,max;
			min = Integer.parseInt(valmin);
			max = Integer.parseInt(valmax);
			if(strValue.length()<min||strValue.length()>max)
			{
				throw new ValidatorException(new FacesMessage("Ingrese min "+min+" y max "+max+" caracteres."));
			}
		}
		catch(NumberFormatException eni)
		{
			throw new ValidatorException(new FacesMessage("Error de conversion: "+eni.getMessage()));
		}
	}
}
