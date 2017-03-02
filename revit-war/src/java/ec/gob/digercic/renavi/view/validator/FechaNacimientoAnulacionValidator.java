/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.validator;

import java.util.Calendar;
import java.util.Date;
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
@FacesValidator("fechaNacimientoAnulacionValidator")
public class FechaNacimientoAnulacionValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        Object fs = component.getAttributes().get("fechaSis");
        System.out.println(fs);
        Object ff = value;
        System.out.println(ff); 
       
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime((Date) fs);
        cal2.setTime((Date) ff);
        boolean sameDay =      cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                            && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                            && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        if (sameDay) {
            FacesMessage message = new FacesMessage("La fecha de nacimiento debe ser diferente a la fecha registrada en el sistema.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
