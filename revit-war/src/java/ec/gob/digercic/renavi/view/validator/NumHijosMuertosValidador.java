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
@FacesValidator(value = "numHijosMuertosValidador")
public class NumHijosMuertosValidador implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String valorIng = value.toString();
        if (valorIng != null) {
            Short valor = new Short(valorIng);
            if (valor >= 0 && valor <= 18) {
                Object npTemp = component.getAttributes().get("numeroParto");
                Object hvTemp = component.getAttributes().get("hijosVivos");
                Object hmTemp = component.getAttributes().get("hijosNVMuertos");
                if(npTemp != null && hvTemp != null && hmTemp != null){
                    short np = (short) npTemp;
                    short hv = (short)hvTemp;
                    short hm = (short)hmTemp;
                    if(np > ((short)(valor + hv + hm))){
                        FacesMessage msg = new FacesMessage("La suma de hijos vivos, hijos vivos que han muerto e hijos que nacieron muertos debe ser mayor o igual al número del parto.");
                        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                        throw new ValidatorException(msg);
                    }
                }else{
                    FacesMessage msg = new FacesMessage("Primero de be ingresar los valores de número de parto, hijos vivos, hijos vivos que han muerto e hijos que nacieron muerto.");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            } else {
                FacesMessage msg = new FacesMessage("El número de hijos que nacieron muertos no es válido.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

}
