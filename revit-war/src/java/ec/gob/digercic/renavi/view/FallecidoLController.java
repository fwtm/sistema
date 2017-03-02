package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.Fallecido;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "fallecidoLController")
@ViewScoped
public class FallecidoLController implements Serializable {

    private List<Fallecido> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.FallecidoFacadeLocal ejbFacade;
    private TblSauregUsuario userLgn;

    public FallecidoLController() {
    }

    @PostConstruct
    public void iniciar() {
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            parametros.add(new ParametroConsulta("fkAgenciaSaureg", userLgn.getAgenciaInUser().getCodMsp()));
            parametros.add(new ParametroConsulta("fkUsuSaureg", userLgn.getNomUsuario()));
            parametros.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
            parametros.add(new ParametroConsulta("estFirma", JsfUtil.STAT_FIR_SIN));
            //Parametro para estado de registro = 5 (DIGERCIC)
            parametros.add(new ParametroConsulta("estRegistro", 5));
            items = ejbFacade.consultarTablaResultList("Fallecido.findAllByInstitucion",parametros);
            if(items.isEmpty()){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoResultException"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }  
        }catch(Exception e){
            e.printStackTrace();
            //JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaIniciar"), JsfUtil.FATAL_MESSAGE);
            //RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        }
    }
    
    public String prepareEdit() {
        return "/pages/contenedor/CreateFallecidoCont";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        return "Create";
    }

    public List<Fallecido> getItems() {
        if(items == null){
            items = new ArrayList<Fallecido>();
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

    @FacesConverter(forClass = Fallecido.class)
    public static class FallecidoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
                if (value == null || value.length() == 0) {
                    return null;
                }
                FallecidoLController controller = (FallecidoLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "fallecidoLController");
                return controller.ejbFacade.find(getKey(value));
            }catch(EntidadException ee){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoConverterObject"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("mssgsGrwl");
                return null;
            }
        }

        Long getKey(String value) {
            Long key;
            key = Long.parseLong(value);
            return key;
        }

        String getStringKey(Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Fallecido) {
                Fallecido o = (Fallecido) object;
                return getStringKey(o.getIdFal());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Fallecido.class.getName());
            }
        }

    }

}
