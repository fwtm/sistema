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
@FacesValidator(value = "cedulaValidadorReporte")
public class CedulaValidatorReporte implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        int num_provincias = 24;
        String cedula = value.toString();
        if (cedula.length() == 10) {

        //verifica que los dos primeros d�gitos correspondan a un valor entre 1 y NUMERO_DE_PROVINCIAS
            //verifica que el �ltimo d�gito de la c�dula sea v�lido
            int[] d = new int[10];
            //Asignamos el string a un array
            for (int i = 0; i < d.length; i++) {
                d[i] = Integer.parseInt(cedula.charAt(i) + "");
            }

            int imp = 0;
            int par = 0;

            //sumamos los duplos de posici�n impar
            for (int i = 0; i < d.length; i += 2) {
                d[i] = ((d[i] * 2) > 9) ? ((d[i] * 2) - 9) : (d[i] * 2);
                imp += d[i];
            }

            //sumamos los digitos de posici�n par
            for (int i = 1; i < (d.length - 1); i += 2) {
                par += d[i];
            }

            //Sumamos los dos resultados
            int suma = imp + par;

            //Restamos de la decena superior
            int d10 = Integer.parseInt(String.valueOf(suma + 10).substring(0, 1)
                    + "0") - suma;

            //Si es diez el d�cimo d�gito es cero        
            d10 = (d10 == 10) ? 0 : d10;

            //si el d�cimo d�gito calculado es igual al digitado la c�dula es correcta
            if (d10 == d[9]) {

            } else {
                //addError("La c�dula ingresada no es v�lida");
                FacesMessage msg
                        = new FacesMessage("Cédula no Válida");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        } else {
            if(cedula.length() == 0){
                System.out.println("Cedula viene vacia");
            }else{
            FacesMessage msg = new FacesMessage("Cédula debe tener 10 digitos");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);}

        }
    }

}
