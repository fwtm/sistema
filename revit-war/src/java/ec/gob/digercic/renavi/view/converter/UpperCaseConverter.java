/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.converter;

import ec.gob.digercic.renavi.view.util.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author santiago.tapia
 */
@FacesConverter(value="uperCaseConverter")
public class UpperCaseConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         return value.trim().toUpperCase();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString().trim().toUpperCase();
    }
    
}
