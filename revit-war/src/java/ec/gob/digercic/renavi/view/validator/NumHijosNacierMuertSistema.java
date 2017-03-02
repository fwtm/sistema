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
@FacesValidator(value = "numHijosNacierMuertSistema")
public class NumHijosNacierMuertSistema implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String valorIng = value.toString();
        if (valorIng != null) {
            Short valor = new Short(valorIng);
            if (valor >= 0 && valor <= 9) {
                Object peTemp = component.getAttributes().get("prodEmbrazo");
                Object hvsTemp = component.getAttributes().get("hijosVivosSistema");
                if(peTemp != null && hvsTemp != null){
                    int pe = (int)peTemp;
                    short hvs = (short)hvsTemp;
                    if((valor + hvs) != pe){
                        FacesMessage msg = new FacesMessage("La suma de hijos nacidos vivos y nacidos muertos correspondientes a este parto debe ser igual al producto del embarazo.");
                        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                        throw new ValidatorException(msg);
                    }
                }else{
                    FacesMessage msg = new FacesMessage("Primero de be ingresar los valores de producto del embarazo e hijos vivos de este parto.");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            } else {
                FacesMessage msg = new FacesMessage("El número de hijos que nacieron muertos en este parto no es válido.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

}
