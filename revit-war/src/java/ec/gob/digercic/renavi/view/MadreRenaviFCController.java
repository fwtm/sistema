package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import java.io.IOException;
/*cambio para validar el numero de cedula de la madre  FWTM  19-02-2016 */
import ec.gob.digercic.renavi.view.validator.CedulaValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "madreRenaviFCController")
@ViewScoped
public class MadreRenaviFCController implements Serializable {
    private List<NacidoVivoRenavi> hijoItems;
    @EJB
    private ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal ejbFacadeNacidoVivoRenavi;
    /*nuevas variables para los criterios de busqueda y parametro de busqueda   FWTM  16-02-2016*/
    private String criterioBusqueda;
    private String parametroBusqueda;

    public String getParametroBusqueda() {
        return parametroBusqueda;
    }

    public void setParametroBusqueda(String parametroBusqueda) {
        this.parametroBusqueda = parametroBusqueda;
    }

    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }
      
    public MadreRenaviFCController() {
    }

    public List<NacidoVivoRenavi> getHijoItems() {
        if(hijoItems == null){
            hijoItems = new ArrayList<NacidoVivoRenavi>();
        }
        return hijoItems;
    }
   
    @PostConstruct
    public void iniciar() { }
    
    public void consultarPorCedulaMadre(String cedulaMadre) {
        try{
            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            params.add(new ParametroConsulta("cedulMad", cedulaMadre));
            params.add(new ParametroConsulta("statusNV", JsfUtil.ESTADO_REG_ACTIVO));
            params.add(new ParametroConsulta("statusM", JsfUtil.ESTADO_REG_ACTIVO));
            params.add(new ParametroConsulta("estFirma", JsfUtil.STAT_FIR_CON));
            hijoItems = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByCedulMad", params);
            if(hijoItems.isEmpty()){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }else{
                RequestContext.getCurrentInstance().update("form_cont:cntndrResultados");
            }
        }catch(EntidadException ee){
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("EntidadException"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
        }
    }
    
    public void consultarPorCedulaHijo(String cedulaHijo) {
        try{
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                params.add(new ParametroConsulta("cedulNacViv", cedulaHijo));
                params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                params.add(new ParametroConsulta("estFir", JsfUtil.STAT_FIR_CON));
                hijoItems = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByCedulNacVivConsultaCiudadana", params);
            if(hijoItems.isEmpty()){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }else{
                RequestContext.getCurrentInstance().update("form_cont:cntndrResultados");
            }
        }catch(EntidadException ee){
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("EntidadException"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
        }
    }
    
    /*Nuevo metodoa para la busqueda de madre indocumentada sea por pasaporte o codigo revit  FWTM 16-02-2016*/
    public void consultarPorDocumento (String documento, String tipo) {
        try{
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                if(tipo.equals("pasaporteMadre")){
                    params.add(new ParametroConsulta("pasaporteMad", documento));
                } else {
                    params.add(new ParametroConsulta("idMad", Long.parseLong(documento)));
                }
                params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                params.add(new ParametroConsulta("estFir", JsfUtil.STAT_FIR_CON));
                if(tipo.equals("pasaporteMadre")){
                    hijoItems = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByPasaporteMad", params);
                } else {
                    hijoItems = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByCodigoMad", params);
                }
            if(hijoItems.isEmpty()){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }else{
                RequestContext.getCurrentInstance().update("form_cont:cntndrResultados");
            }
        }catch(EntidadException ee){
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("EntidadException"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
        }
    }
    
    public void limpiar() {
        hijoItems = null;
        parametroBusqueda = null;
        criterioBusqueda = null;
    }
    
    public void limpiarConsulta() {
        limpiar();
    }
    /*Nuevo metodoa para la busqueda de madre indocumentada sea por pasaporte o codigo revit  FWTM 16-02-2016*/
    public void seleccionBusqueda() {
        CedulaValidator validar = new CedulaValidator();
        boolean caracter = false;
        boolean numero = false;
        if(criterioBusqueda.equals("idMadre") || criterioBusqueda.equals("idNacidoVivo") || criterioBusqueda.equals("codigoMadre")) {
            for (int i = 0; i < parametroBusqueda.length(); i++) {
                if (Character.isDigit(parametroBusqueda.charAt(i))) {
                        numero = true;
                } else {
                        caracter = true;
                }
            }
            if(numero && !caracter){
                if(criterioBusqueda.equals("codigoMadre")){
                    consultarPorDocumento(parametroBusqueda, criterioBusqueda);
                } else {
                if (validar.validarCedula(parametroBusqueda) == "000") {
                            if(criterioBusqueda.equals("idMadre")) {
                                consultarPorCedulaMadre(parametroBusqueda);
                            } else {
                                consultarPorCedulaHijo(parametroBusqueda);
                            }
                    } else {
                        if (validar.validarCedula(parametroBusqueda) == "001") {
                            JsfUtil.displayMessage("Cédula no Válida", JsfUtil.ERROR_MESSAGE);
                            RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                        } else {
                            JsfUtil.displayMessage("Cédula Incompleta", JsfUtil.ERROR_MESSAGE);
                            RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                        }
                    }
                }
            } else if (caracter){
                JsfUtil.displayMessage("Para búsqueda por Cédula o Código REVIT ingrese números", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }
        } if (criterioBusqueda.equals("pasaporteMadre")) {
                consultarPorDocumento(parametroBusqueda, criterioBusqueda);
        }
    }
    
    public void verPDFConFirma() throws IOException {
        try {
            String cedula = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedulNacViv");
            NacidoVivoRenavi objeto = new NacidoVivoRenavi();
            for(NacidoVivoRenavi item : hijoItems){
                if(item.getCedulNacViv().equals(cedula)){
                    objeto = item;
                    break;
                }
            }
            byte[] pdfData = objeto.getPdfConFirmaNacViv();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            // Empieza proceso de response Initialization.
            response.reset();
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "attachment;filename=Certificado_" + cedula + ".pdf");
            // Write file to response.
            response.getOutputStream().write(pdfData);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            // Inform JSF to not take the response in hands.
            facesContext.responseComplete(); // 
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().responseComplete();
            JsfUtil.displayMessage("No se puede visualizar el PDF del registro.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form:messages");
        }
    }
    
}
