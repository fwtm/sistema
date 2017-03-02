/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.validator;

import ec.gob.digercic.renavi.view.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author santiago.tapia
 */
@FacesValidator(value = "cedulaValidadorAnulacion")
public class CedulaValidatorAnulacion implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        System.out.println("Entra al validador: cedulaValidadorAnulacion");
        UIInput cedMa = (UIInput) context.getViewRoot().findComponent((String) component.getAttributes().get("cedMa"));
        //UIInput cedHijo = (UIInput) context.getViewRoot().findComponent((String) component.getAttributes().get("cedHijo"));
         
        Object value1 = cedMa.getSubmittedValue();
        System.out.println("cedMa:"+value1);
//        Object value2 = cedHijo.getSubmittedValue();
//        System.out.println("cedHijo"+value2);
        
        /*CAMBIO para validacion de cedula de identidad FWTM*/
        if (value1.toString().isEmpty()){
            FacesMessage msg = new FacesMessage("Se debe ingresar al menos uno de los dos criterios de búsqueda.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);}
        }

       


}


