/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.AnulacionRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.Error1RenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.Anulacion;
import ec.gob.digercic.renavi.entities.Error1;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "anulacionController")
@ViewScoped
public class AnulacionController implements Serializable {

    private String cedulaString;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
    private List<NacidoVivoRenavi> resultado = null;
    private List<Object[]> lstResultado = new ArrayList<Object[]>(); /// FWTM 12-07-2016
    private NacidoVivoRenavi registroSeleccionado;
    private List<Error1> erroresAnulacion = new ArrayList<Error1>();
    private List<Error1> selectedErroresAnulacion = new ArrayList<Error1>();
    private Anulacion current = new Anulacion();
    private Boolean buttonStatus = true;
    private String fechaFormato;
    private String idMadreIn = null;
    //
    private String cedulaStringHijo;
    private String estadoRegistro;
    private String estFirma;
    private String estReg;
    private String estAnul;

    @EJB
    private NacidoVivoRenaviFacadeLocal ejbNacidoVivoRenaviFacade;
    @EJB
    private Error1RenaviFacadeLocal ejbErrorAnulacion;
    @EJB
    private AnulacionRenaviFacadeLocal ejbAnulacion;
    @EJB
    private LogAccesosRenaviFacadeLocal ejbLogs;

    public AnulacionController() {
    }

    @PostConstruct
    public void init() throws EntidadException {
        resultado = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        setUserLgn((ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion"));
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println("Fired from onRowSelect");
        this.setButtonStatus(false);
        FacesMessage msg = new FacesMessage("Selected", "ok");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * @return the cedulaString
     */
    public String getCedulaStringHijo() {
        return cedulaStringHijo;
    }

    /**
     * @param cedulaString the cedulaString to set
     */
    public void setCedulaStringHijo(String cedulaStringHijo) {
        this.cedulaStringHijo = cedulaStringHijo;
    }

    /**
     * @return the cedulaString
     */
    public String getCedulaString() {
        return cedulaString;
    }

    /**
     * @param cedulaString the cedulaString to set
     */
    public void setCedulaString(String cedulaString) {
        this.cedulaString = cedulaString;
    }

    /**
     * @return the resultado
     */
    public List<NacidoVivoRenavi> getResultado() {
        return resultado;
    }

    /**
     * @param resultado the resultado to set
     */
    public void setResultado(List<NacidoVivoRenavi> resultado) {
        this.resultado = resultado;
    }

    /**
     * @return the registroSeleccionado
     */
    public NacidoVivoRenavi getRegistroSeleccionado() {

        return registroSeleccionado;
    }

    /**
     * @param registroSeleccionado the registroSeleccionado to set
     */
    public void setRegistroSeleccionado(NacidoVivoRenavi registroSeleccionado) {
        this.registroSeleccionado = registroSeleccionado;
    }

    /**
     * @return the userLgn
     */
    public ec.gob.digercic.renavi.ws.TblSauregUsuario getUserLgn() {
        return userLgn;
    }

    public void setUserLgn(ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn) {
        this.userLgn = userLgn;
    }

    public String getIdMadreIn() {
        return idMadreIn;
    }

    public void setIdMadreIn(String idMadreIn) {
        this.idMadreIn = idMadreIn;
    }

   public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    /**
     * @return the erroresAnulacion
     */
    public List<Error1> getErroresAnulacion() {
        try {
            erroresAnulacion = ejbErrorAnulacion.findAll();
            Error1 itemRemove = new Error1();
            Error1 itemRemove2 = new Error1();
            for (Error1 item : erroresAnulacion) {
                if (item.getId() == 1) {
                    itemRemove = item;
                    break;
                }

            }
            for (Error1 item : erroresAnulacion) {
                if (item.getId() == 2) {
                    itemRemove2 = item;
                    break;
                }

            }
            erroresAnulacion.remove(itemRemove);
            erroresAnulacion.remove(itemRemove2);
            return erroresAnulacion;
        } catch (EntidadException ee) {
            ee.printStackTrace();
            return null;
        }
    }

    /**
     * @return the selectedErroresAnulacion
     */
    public List<Error1> getSelectedErroresAnulacion() {
        return selectedErroresAnulacion;
    }

    /**
     * @return the ejbErrorAnulacion
     */
    public Error1RenaviFacadeLocal getEjbErrorAnulacion() {
        return ejbErrorAnulacion;
    }

    /**
     * @param selectedErroresAnulacion the selectedErroresAnulacion to set
     */
    public void setSelectedErroresAnulacion(List<Error1> selectedErroresAnulacion) {
        this.selectedErroresAnulacion = selectedErroresAnulacion;
    }

    /**
     * @return the ejbAnulacion
     */
    public AnulacionRenaviFacadeLocal getEjbAnulacion() {
        return ejbAnulacion;
    }

    /**
     * @return the current
     */
    public Anulacion getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(Anulacion current) {
        this.current = current;
    }

    public Boolean getButtonStatus() {
        return buttonStatus;
    }

    public void setButtonStatus(Boolean buttonStatus) {
        this.buttonStatus = buttonStatus;
    }
    
    /*FWTM 12-07-2016*/
    public String getEstFirma() {
        return estFirma;
    }

    public void setEstFirma(String estFirma) {
        this.estFirma = estFirma;
    }

    public String getEstReg() {
        return estReg;
    }

    public void setEstReg(String estReg) {
        this.estReg = estReg;
    }

    public String getEstAnul() {
        return estAnul;
    }

    public void setEstAnul(String estAnul) {
        this.estAnul = estAnul;
    }
    /**/
    
    public void actualizaTablaPorAnular(ActionEvent event) {
        buscarNacidoVivosParaAnular();
        RequestContext.getCurrentInstance().update(":form:tblResultados");
    }

    public void buscarNacidoVivosParaAnular() {
        resultado = null;
        StringBuilder queryConsulta = new StringBuilder("select * from nacido_vivo_renavi nv, madre_renavi m");
        queryConsulta.append(" where nv.fk_cedul_mad = m.id_mad");
        queryConsulta.append(" and nv.fk_id_est_fir = 2");
        queryConsulta.append(" and nv.fk_usu_firma_saureg = '");
        queryConsulta.append(userLgn.getNomUsuario());
        queryConsulta.append("' and m.cedul_mad = '");
        if (cedulaString != null) {
            queryConsulta.append(cedulaString);
        } else {
            queryConsulta.append(idMadreIn);
        }
        queryConsulta.append("' and nv.fk_id_est_reg = 4");
        queryConsulta.append(" and nv.id_nac_viv not in(select a.fk_ced_nac_viv from anulacion a)");
        queryConsulta.append(" order by nv.fk_id_est_fir, nv.fecha_nacim_nac_viv, nv.fk_agencia_firma_saureg");
        try {
            resultado = ejbNacidoVivoRenaviFacade.executeNativeQueryListResultGenerico(queryConsulta.toString(), NacidoVivoRenavi.class);
            RequestContext.getCurrentInstance().update("tblResultados");
        } catch (EntidadException e) {
            e.printStackTrace();
            JsfUtil.displayMessage(e.getMessage(), JsfUtil.ERROR_MESSAGE);
        }

    }

    //método para solicitar una anulación 
    public void create() throws EntidadException {
        try {
            getErroresAnulacion();
            selectedErroresAnulacion = erroresAnulacion;
            LogsAcceso log = new LogsAcceso();
            log.setFechaAcceso(new Date());
            log.setUsuario(userLgn.getNomUsuario());//Username del usuario que genera el log
            log.setAgenId(userLgn.getAgenciaInUser().getCodMsp());//código de agencia a la que pertenece dicho ususario
            log.setAgenNom(userLgn.getAgenciaInUser().getNomAgencia());//nombre de agenciaa la que pertenece el usuario
            log.setNomUsu(userLgn.getNombre());//nombre del usuario
            log.setApeUsu(userLgn.getApellido());//apellido del usuario
            log.setInstId(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());//código de la institución a la que pertenece el usuario
            log.setInstNombre(userLgn.getAgenciaInUser().getIdInstituc().getNomInstituc());//nombre de la institución a la que pertenece el usuario
            log.setAccion("REVIT. ANULACION DE REGISTRO. SOLICITA ANULACIÓN");//accion realizada, con el siguiente en formato:
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (registroSeleccionado.getFkIdProEmb().getIdProEmb() > 1) {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            parametros.add(new ParametroConsulta("numParto", registroSeleccionado.getNumPartoSistema()));
            parametros.add(new ParametroConsulta("idMad", registroSeleccionado.getFkCedulMad()));
            List<NacidoVivoRenavi> productosMultiples = ejbNacidoVivoRenaviFacade.consultarTablaResultList("NacidoVivoRenavi.findProductosPartoMultiple", parametros);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("AnulacionPartoMultiple"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
            for (NacidoVivoRenavi item : productosMultiples) {
                try {
                    Anulacion anula = new Anulacion();
                    anula.setApellMedicoAnulNacViv(userLgn.getApellido());
                    anula.setNomMedicoAnulNacViv(userLgn.getNombre());
                    anula.setUserMedicoAnulNacViv(userLgn.getNomUsuario());
                    anula.setFechaAnul(new Date());
                    anula.setFkCedNacViv(item);
                    anula.setErr(selectedErroresAnulacion);
                    anula.setJustificacion("PARTO MÚLTIPLE:" + current.getJustificacion());
                    anula.setIdAgenciaAnul(new BigDecimal(userLgn.getAgenciaInUser().getCodMsp()));
                    anula.setEstAnulNacViv("1"); //de momento 1:por verificar, 2: aprobado, 3: rechazado , 4:finalizada
                    anula.setObservacionRechazoLeido("0");
                    getEjbAnulacion().create(anula);
                } catch (Exception ee) {
                    ee.getCause();
                    JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                }
            }
            RequestContext.getCurrentInstance().execute("PF('nvDialog').hide();");
            resultado.clear();
            RequestContext.getCurrentInstance().update("form:tblResultados");
        } else {
            try {
                current.setApellMedicoAnulNacViv(userLgn.getApellido());
                current.setNomMedicoAnulNacViv(userLgn.getNombre());
                current.setUserMedicoAnulNacViv(userLgn.getNomUsuario());
                current.setFechaAnul(new Date());
                current.setFkCedNacViv(registroSeleccionado);
                current.setErr(selectedErroresAnulacion);
                current.setJustificacion(current.getJustificacion());
                current.setIdAgenciaAnul(new BigDecimal(userLgn.getAgenciaInUser().getCodMsp()));
                current.setEstAnulNacViv("1"); //de momento 1:por verificar, 2: aprobado, 3: rechazado , 4: finalizado
                current.setObservacionRechazoLeido("0");
                getEjbAnulacion().create(current);
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("AnulacionRenaviCreado"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("growl");
                RequestContext.getCurrentInstance().execute("PF('nvDialog').hide();");
                setCurrent(new Anulacion());
                selectedErroresAnulacion = new ArrayList<>();
            } catch (Exception ee) {
                ee.getCause();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            }
        }
    }

    public void busqueda() throws SQLException, EntidadException, ParseException {
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. BUSQUEDA DE REGISTROS MADRE IDENTIFICADA");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        
        resultado = null;
        StringBuilder queryAnulacion = new StringBuilder("SELECT * FROM NACIDO_VIVO_RENAVI NV, MADRE_RENAVI M");
        queryAnulacion.append(" WHERE NV.FK_CEDUL_MAD = M.ID_MAD");
        queryAnulacion.append(" AND NV.FK_AGENCIA_FIRMA_SAUREG = '");
        queryAnulacion.append(getUserLgn().getAgenciaInUser().getCodMsp());
        queryAnulacion.append("' AND M.CEDUL_MAD = '");
        queryAnulacion.append(cedulaString);
        queryAnulacion.append("' AND NV.FK_ID_EST_REG NOT IN (6)");
        queryAnulacion.append(" ORDER BY NV.FK_ID_EST_FIR, NV.FECHA_NACIM_NAC_VIV, NV.FK_AGENCIA_FIRMA_SAUREG");
        
        try {
            resultado = ejbNacidoVivoRenaviFacade.executeNativeQueryListResultGenerico(queryAnulacion.toString(), NacidoVivoRenavi.class);
            for (NacidoVivoRenavi item : resultado) {
                setEstFirma(item.getFkIdEstFir().getIdEstFir().toString());
                setEstReg(item.getFkIdEstReg().getIdEstReg().toString());
                Set<Anulacion> lstUnicAnul =
                            new LinkedHashSet<Anulacion>(item.getAnulacionList());
                item.getAnulacionList().clear();
                item.getAnulacionList().addAll(lstUnicAnul);
                for (Iterator<Anulacion> it = item.getAnulacionList().iterator(); it.hasNext();) {
                    Anulacion tbl = it.next();
                    if (tbl.getEstAnulNacViv().equalsIgnoreCase("3")) {
                        it.remove();
                    }
                }
                for (int i = 0; i < item.getAnulacionList().size(); i++) {
                    setEstAnul(item.getAnulacionList().get(i).getEstAnulNacViv());
                    System.out.println(estAnul);
                }
                if (estReg.equals("5")) {
                    setEstadoRegistro("Registro se encuentra Inscrito");
                }
                if (estReg.equals("7") && estFirma.equals("1")) {
                    setEstadoRegistro("Bandeja Editar Anulados");
                }
                if ((estReg.equals("8") && estFirma.equals("2")) || 
                        (estReg.equals("8") && estFirma.equals("1"))) {
                    setEstadoRegistro("Anulado/Editado/Firmado.");
                } 
                if (item.getAnulacionList().size() > 0) {
                    if (estFirma.equals("2") && estReg.equals("4") && estAnul.equals("1")) {
                        setEstadoRegistro("Solicitud Enviada.");
                    }
                }
                else if (estReg.equals("4") && estFirma.equals("2")) {
                    setEstadoRegistro(" ");
                }
            }
            
            RequestContext.getCurrentInstance().update("tblResultados");
        } catch (EntidadException e) {
            e.printStackTrace();
        }
    }

    public void busquedaMadIdoc() throws SQLException, EntidadException, ParseException {
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. BUSQUEDA DE REGISTROS MADRE INDOCUMENTADA");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        resultado = null;
        StringBuilder queryAnulacion = new StringBuilder("SELECT * FROM NACIDO_VIVO_RENAVI NV, MADRE_RENAVI M");
        queryAnulacion.append(" WHERE NV.FK_CEDUL_MAD = M.ID_MAD");
        queryAnulacion.append(" AND NV.FK_AGENCIA_FIRMA_SAUREG = '");
        queryAnulacion.append(getUserLgn().getAgenciaInUser().getCodMsp());
        queryAnulacion.append("' AND M.ID_MAD = '");
        queryAnulacion.append(idMadreIn);
        queryAnulacion.append("' AND NV.FK_ID_EST_REG NOT IN (6)");
        queryAnulacion.append(" ORDER BY NV.FK_ID_EST_FIR, NV.FECHA_NACIM_NAC_VIV, NV.FK_AGENCIA_FIRMA_SAUREG");
        
        try {
            resultado = ejbNacidoVivoRenaviFacade.executeNativeQueryListResultGenerico(queryAnulacion.toString(), NacidoVivoRenavi.class);
            for (NacidoVivoRenavi item : resultado) {
                setEstFirma(item.getFkIdEstFir().getIdEstFir().toString());
                setEstReg(item.getFkIdEstReg().getIdEstReg().toString());
                Set<Anulacion> lstUnicAnul =
                            new LinkedHashSet<Anulacion>(item.getAnulacionList());
                item.getAnulacionList().clear();
                item.getAnulacionList().addAll(lstUnicAnul);
                for (Iterator<Anulacion> it = item.getAnulacionList().iterator(); it.hasNext();) {
                    Anulacion tbl = it.next();
                    if (tbl.getEstAnulNacViv().equalsIgnoreCase("3")) {
                        it.remove();
                    }
                }
                for (int i = 0; i < item.getAnulacionList().size(); i++) {
                    setEstAnul(item.getAnulacionList().get(i).getEstAnulNacViv());
                    System.out.println(estAnul);
                }
                if (estReg.equals("5")) {
                    setEstadoRegistro("Registro se encuentra Inscrito");
                }
                if (estReg.equals("7") && estFirma.equals("1")) {
                    setEstadoRegistro("Bandeja Editar Anulados");
                }
                if ((estReg.equals("8") && estFirma.equals("2")) || 
                        (estReg.equals("8") && estFirma.equals("1"))) {
                    setEstadoRegistro("Anulado/Editado/Firmado.");
                } 
                if (item.getAnulacionList().size() > 0) {
                    if (estFirma.equals("2") && estReg.equals("4") && estAnul.equals("1")) {
                        setEstadoRegistro("Solicitud Enviada.");
                    }
                }
                else if (estReg.equals("4") && estFirma.equals("2")) {
                    setEstadoRegistro(" ");
                }
            }            
            RequestContext.getCurrentInstance().update("tblResultados");
        } catch (EntidadException e) {
            e.printStackTrace();
        }
    }

    public void verPDFConFirma() throws IOException, EntidadException {
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. CONSULTA PDF PREVIO A LA ANULACION");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }

        System.out.println("Desde reporte: verPDFConFirma()");
       try {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id").toString();
            ParametroConsulta param = new ParametroConsulta("idNacViv", Long.parseLong(id));
            parametros.add(param);
            NacidoVivoRenavi objeto = (NacidoVivoRenavi) ejbNacidoVivoRenaviFacade.consultarTablaSingleResult("NacidoVivoRenavi.findByIdNacViv", parametros);
            byte[] pdfData = objeto.getPdfConFirmaNacViv();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            // Empieza proceso de response Initialization.
            response.reset();
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "attachment;filename=Certificado_" + id + ".pdf");
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

    public String getFechaFormato() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        if (registroSeleccionado == null) {
            fechaFormato = null;
        } else {
            try {
                fechaFormato = formateador.format(registroSeleccionado.getFechaNacimNacViv());
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
        System.out.println("fechaFormato: " + fechaFormato);
        return fechaFormato;
    }

    public void setFechaFormato(String fechaFormato) {
        this.fechaFormato = fechaFormato;
    }

    /**
     * @return the ejbLogs
     */
    public LogAccesosRenaviFacadeLocal getEjbLogs() {
        return ejbLogs;
    }

    /**
     * @param ejbLogs the ejbLogs to set
     */
    public void setEjbLogs(LogAccesosRenaviFacadeLocal ejbLogs) {
        this.ejbLogs = ejbLogs;
    }
}
