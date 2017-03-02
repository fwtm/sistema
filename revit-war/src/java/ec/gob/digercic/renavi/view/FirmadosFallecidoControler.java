/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.FallecidoFacadeLocal;
import ec.gob.digercic.renavi.entities.Fallecido;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import ec.gob.digercic.saureg.ejb.TblSauregUsuarioFacadeLocal;
import java.io.IOException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "firmadosFallecidoControler")
@ViewScoped
public class FirmadosFallecidoControler implements Serializable {

    private List<Fallecido> listFallecidoFirmados;
    private Date fechaActual;
    private Date fechaPreVerFirma;
    private Date fechaInicio;
    private Date fechaFin;
    @EJB
    private FallecidoFacadeLocal ejbFallecidoFacade;
    @EJB
    private TblSauregUsuarioFacadeLocal ejbUsuarioFacade;
    @EJB
    private ec.gob.digercic.renavi.ejb.VariableRenaviFacadeLocal ejbFacadeVariable;
    private TblSauregUsuario userLgn;

    public FirmadosFallecidoControler() {
    }

    @PostConstruct
    public void iniciar() throws ParseException {
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            //Obtengo la fecha actual del sistema
            fechaActual = new Date();
            fechaFin = fechaActual;
            //Obtengo el tiempo preFirma
            Long tp = new Long(ejbFacadeVariable.find(4).getVarValor());
            fechaPreVerFirma = new Date(fechaActual.getTime() - tp);
            fechaInicio  = fechaPreVerFirma;
            //Busco los nacidos vivos no firmados
            buscarFallecidosFirmados();
        }catch(Exception e){
            e.printStackTrace();
            //userLgn = null;
            fechaPreVerFirma = new Date();
            fechaActual = new Date();
        }
            
    }

    public void buscarFallecidosFirmados() {
        try {
            if(userLgn.getAgenciaInUser().getCodMsp() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                String fi = sdf.format(fechaInicio);
                fechaInicio = sdf.parse(fi);
                String ff = sdf.format(fechaFin);
                fechaFin = sdf.parse(ff);
                fechaFin = new Date(fechaFin.getTime()+86399000L);
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                ParametroConsulta param = new ParametroConsulta("fechaInicio", fechaInicio);
                //ParametroConsulta param = new ParametroConsulta("fechaInicio", fi);
                parametros.add(param);
                param = new ParametroConsulta("fechaFin", fechaFin);
                //param = new ParametroConsulta("fechaFin", ff);
                parametros.add(param);
                ParametroConsulta paramInstitucion = new ParametroConsulta("idInstitucion", userLgn.getAgenciaInUser().getCodMsp());
                parametros.add(paramInstitucion);
                ParametroConsulta paramEstadoF = new ParametroConsulta("statusF", JsfUtil.ESTADO_REG_ACTIVO);
                parametros.add(paramEstadoF);
                ParametroConsulta paramEstadoFirma = new ParametroConsulta("estFirma", JsfUtil.STAT_FIR_CON);
                parametros.add(paramEstadoFirma);
                //Parametro para estado de registro = 5 (DIGERCIC)
                List<String> estadoRegistro = new ArrayList<String>();
                estadoRegistro.add(JsfUtil.STAT_DAT_DOCTOR.toString());
                estadoRegistro.add(JsfUtil.STAT_DAT_DIGERCIG.toString());
                estadoRegistro.add("8");
                ParametroConsulta paramEstadoRegistro = new ParametroConsulta("estRegistro", estadoRegistro);
                parametros.add(paramEstadoRegistro);
                ParametroConsulta usuario = new ParametroConsulta("usuario", userLgn.getNomUsuario());
                parametros.add(usuario);
                listFallecidoFirmados = ejbFallecidoFacade.consultarTablaResultList("Fallecido.findInFechaFirma", parametros);
                if(listFallecidoFirmados.isEmpty()){
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("messages");
                }
            }else{
                JsfUtil.displayMessage("El usuario no está trabajando en una agencia del M.S.P.", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("messages");
            }
                
        } catch (Exception ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("EntidadException"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("messages");
        }
    }

    public List<Fallecido> getListFallecidoFirmados() {
        return listFallecidoFirmados;
    }

    public void setListFallecidoFirmados(List<Fallecido> listFallecidoFirmados) {
        this.listFallecidoFirmados = listFallecidoFirmados;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public Date getFechaPreVerFirma() {
        return fechaPreVerFirma;
    } 

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void verPDFConFirma() throws IOException {
        try {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            String cedula = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedula");
            ParametroConsulta param = new ParametroConsulta("cedulaFal", cedula);
            parametros.add(param);
            Fallecido objeto = (Fallecido) ejbFallecidoFacade.consultarTablaSingleResult("Fallecido.findByCedulaFal", parametros);
            byte[] pdfData = objeto.getPdfConFirma();
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
    
    public void verPDFCertificado() throws IOException {
        try {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            String cedula = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedula");
            ParametroConsulta param = new ParametroConsulta("cedulaFal", cedula);
            parametros.add(param);
            Fallecido objeto = (Fallecido) ejbFallecidoFacade.consultarTablaSingleResult("Fallecido.findByCedulaFal", parametros);
            byte[] pdfData = objeto.getPdfCertificado();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            // Empieza proceso de response Initialization.
            response.reset();
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "attachment;filename=CertificadoInhumación_" + cedula + ".pdf");
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
