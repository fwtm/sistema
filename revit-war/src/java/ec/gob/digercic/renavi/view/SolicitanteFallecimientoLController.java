package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.SolicitanteFallecimiento;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.view.util.JsfUtil;

import java.io.Serializable;
import java.math.BigInteger;
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

@ManagedBean(name = "solicitanteFallecimientoLController")
@ViewScoped
public class SolicitanteFallecimientoLController implements Serializable {

    private List<SolicitanteFallecimiento> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.SolicitanteFallecimientoFacadeLocal ejbFacade;

    public SolicitanteFallecimientoLController() {
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

    public List<SolicitanteFallecimiento> getItems() {
        if(items == null){
            items = new ArrayList<SolicitanteFallecimiento>();
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

    @FacesConverter(forClass = SolicitanteFallecimiento.class)
    public static class SolicitanteFallecimientoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                SolicitanteFallecimientoLController controller = (SolicitanteFallecimientoLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "solicitanteFallecimientoLController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoConverterObject"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("mssgsGrwl");
                return null;
            }
        }

        ec.gob.digercic.renavi.entities.SolicitanteFallecimientoPK getKey(String value) {
            ec.gob.digercic.renavi.entities.SolicitanteFallecimientoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new ec.gob.digercic.renavi.entities.SolicitanteFallecimientoPK();
            key.setIdPar(new BigInteger(values[0]));
            key.setIdFal(new BigInteger(values[1]));
            key.setIdTipInv(new BigInteger(values[2]));
            key.setIdCiuInv(new BigInteger(values[3]));
            return key;
        }

        String getStringKey(ec.gob.digercic.renavi.entities.SolicitanteFallecimientoPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdPar());
            sb.append(SEPARATOR);
            sb.append(value.getIdFal());
            sb.append(SEPARATOR);
            sb.append(value.getIdTipInv());
            sb.append(SEPARATOR);
            sb.append(value.getIdCiuInv());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SolicitanteFallecimiento) {
                SolicitanteFallecimiento o = (SolicitanteFallecimiento) object;
                return getStringKey(o.getSolicitanteFallecimientoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SolicitanteFallecimiento.class.getName());
            }
        }

    }

}
