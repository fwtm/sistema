/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.validator;

import ec.gob.digercic.renavi.view.util.*;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
//import org.primefaces.component.calendar.Calendar;

/**
 *
 * @author santiago.tapia
 */
@FacesValidator("primeDateRangeValidator")
public class PrimeDateRangeValidator implements Validator{
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException { 
        if (value == null) {
            return;
        }
        //Leave the null handling of startDate to required="true"
        Object startDateValue = component.getAttributes().get("finicial");
        System.out.println("Fecha Obtenida: "+startDateValue);
        if (startDateValue==null) {
            return;
        }
         
        Date startDate = (Date)startDateValue;
        Date endDate = (Date)value;
        if (endDate.before(startDate)) {
             FacesMessage message = new FacesMessage("La fecha Final no puede ser anterior a la fecha Inicial.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

   
}
