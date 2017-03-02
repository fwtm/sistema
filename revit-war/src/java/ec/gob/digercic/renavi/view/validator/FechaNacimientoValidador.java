/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.validator;

import ec.gob.digercic.renavi.view.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@FacesValidator("fechaNacimientoValidador")
public class FechaNacimientoValidador implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        try {
            //Leave the null handling of startDate to required="true"
            Object fpTemp = component.getAttributes().get("fechaPrenacimiento");
            Object fcTemp = component.getAttributes().get("fechaCreacion");
            if (fpTemp == null && fcTemp == null) {
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
            //Fecha actual
            String fc = sdf.format((Date) fcTemp);
            Date fechaCreacion = sdf.parse(fc);
            //Fecha prenacimiento
            String fp = sdf.format((Date) fpTemp);
            Date fechaPrenacimiento = sdf.parse(fp);
            //Fecha de nacimiento
            String fn = sdf.format((Date) value);
            Date fechaNacimiento = sdf.parse(fn);;
            System.out.println("fecha nacimTime: " + fechaNacimiento.getTime());
            System.out.println("fecha PrenacimTime: " + fechaPrenacimiento.getTime());
            System.out.println("fecha nacim: " + fechaNacimiento);
            System.out.println("fecha Prenacim: " + fechaPrenacimiento);
            if (!(fechaNacimiento.getTime() >= fechaPrenacimiento.getTime()
                    && fechaNacimiento.getTime() <= fechaCreacion.getTime())) {
                FacesMessage message = new FacesMessage("La fecha de nacimiento no está en el rango establecido.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("La fecha de nacimiento no está en el rango establecido.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

}
