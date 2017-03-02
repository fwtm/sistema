package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.Parentesco;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.view.util.JsfUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "parentescoLController")
@ViewScoped
public class ParentescoLController implements Serializable {

    private Parentesco current;
    private List<Parentesco> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.ParentescoFacadeLocal ejbFacade;

    public ParentescoLController() {
    }

    @PostConstruct
    public void iniciar() {
        try{
            items = ejbFacade.findByNamedQueryResultList("CausaMuerteC.findAll");
            if(items.isEmpty()){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoDatosParaMostrar"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            }  
        }catch(Exception e){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaIniciar"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        }
    }
    
    public String prepareEdit() {
        return "Create";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        return "Create";
    }

    public List<Parentesco> getItems() {
        if(items == null){
            items = new ArrayList<Parentesco>();
        }
        return items;
    }


    public SelectItem[] getItemsAvailableSelectMany() {
        try{
            items.clear();
            items = ejbFacade.findByNamedQueryResultList("CausaMuerteC.findAll");
            return JsfUtil.getSelectItems(items, false);
        }catch(EntidadException ee){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return null;
        }   
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        try{
            items.clear();
            items = ejbFacade.findByNamedQueryResultList("CausaMuerteC.findAll");
            return JsfUtil.getSelectItems(items, true);
        }catch(EntidadException ee){
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return null;
        }
    }

    @FacesConverter(forClass = Parentesco.class)
    public static class ParentescoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                ParentescoLController controller = (ParentescoLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "parentescoLController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoConverterObject"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("mssgsGrwl");
                return null;
            }
        }

        Integer getKey(String value) {
            Integer key;
            key = Integer.parseInt(value);
            return key;
        }

        String getStringKey(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Parentesco) {
                Parentesco o = (Parentesco) object;
                return getStringKey(o.getIdPar());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Parentesco.class.getName());
            }
        }

    }

}
