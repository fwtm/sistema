/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.validator;

import ec.gob.digercic.renavi.entities.SexoRenavi;
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
@FacesValidator("sexoAnulacionValidator")
public class SexoAnulacionValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        Object ss = component.getAttributes().get("sexoSis");
        System.out.println(ss);
        Object sf = value;
        System.out.println(sf); 
       
        SexoRenavi sexoSistema = (SexoRenavi) ss;
        SexoRenavi sexoForm    = (SexoRenavi) sf;  
               
        boolean sameSex =  sexoSistema.getDescrSexo().equals(sexoForm.getDescrSexo());
        if (sameSex) {
            FacesMessage message = new FacesMessage("La información de sexo del Nacido Vivo debe ser diferente al sexo registrado en el sistema.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
