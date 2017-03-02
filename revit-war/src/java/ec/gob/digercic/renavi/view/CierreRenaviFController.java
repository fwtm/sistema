/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.CierreRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author henry.aguilar
 */
@ManagedBean(name = "cierreRenaviFController")
@ViewScoped
public class CierreRenaviFController implements Serializable {

    private CierreRenavi cierreCurrent;
    private List<CierreRenavi> cierreItems;
    @EJB
    private ec.gob.digercic.renavi.ejb.CierreRenaviFacadeLocal ejbFacadeCierreRenavi;
    @EJB
    private LogAccesosRenaviFacadeLocal ejbLogs;
    private TblSauregUsuario userLgn;

    public CierreRenavi getCierreCurrent() {
        return cierreCurrent;
    }

    public void setCierreCurrent(CierreRenavi cierreCurrent) {
        this.cierreCurrent = cierreCurrent;
    }

    public List<CierreRenavi> getCierreItems() {

        if (cierreItems == null) {
            cierreItems = new ArrayList<CierreRenavi>();
        }
        return cierreItems;
    }

    public void setCierreItems(List<CierreRenavi> cierreItems) {
        this.cierreItems = cierreItems;
    }

    public String prepareList() {
        return "List";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        return "Create";
    }

    public String prepareEdit() {
        return "Edit";

    }

    @PostConstruct
    public void iniciar() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            ParametroConsulta paramAgencia = new ParametroConsulta("fkAgenciaSaureg", userLgn.getAgenciaInUser().getCodMsp());
            parametros.add(paramAgencia);
            ParametroConsulta paramUsuario = new ParametroConsulta("fkUsuSaureg", userLgn.getNomUsuario());
            parametros.add(paramUsuario);
//            ParametroConsulta paramEstado = new ParametroConsulta("status", JsfUtil.ESTADO_GUARDADO);
//            parametros.add(paramEstado);
            cierreItems = ejbFacadeCierreRenavi.consultarTablaResultList("CierreRenavi.findAllByUsuarioCierre", parametros);
            if (cierreItems.isEmpty()) {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoResultException"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verPDFSinFirma() {
        try {
            LogsAcceso log = new LogsAcceso();
            log.setFechaAcceso(new Date());
            log.setUsuario(userLgn.getNomUsuario());//Username del usuario que genera el log
            log.setAgenId(userLgn.getAgenciaInUser().getCodMsp());//código de agencia a la que pertenece dicho ususario
            log.setAgenNom(userLgn.getAgenciaInUser().getNomAgencia());//nombre de agenciaa la que pertenece el usuario
            log.setNomUsu(userLgn.getNombre());//nombre del usuario
            log.setApeUsu(userLgn.getApellido());//apellido del usuario
            log.setInstId(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());//código de la institución a la que pertenece el usuario
            log.setInstNombre(userLgn.getAgenciaInUser().getIdInstituc().getNomInstituc());//nombre de la institución a la que pertenece el usuario
            log.setAccion("REVIT. REGISTRO DE CONTROL. CONSULTA PDF PREVIO A LA EDICION");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        try {
            BigDecimal idCierre = new BigDecimal(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idCierre"));
            System.out.println("------>" + idCierre);
            CierreRenavi objeto = ejbFacadeCierreRenavi.find(idCierre);
            System.out.println("---->Objeto" + objeto);
            byte[] pdfData = objeto.getPdfSinFirmar();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            // Empieza proceso de response Initialization.
            response.reset();
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "attachment;filename=Certificado-Temp_" + idCierre + ".pdf");
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
