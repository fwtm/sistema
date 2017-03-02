/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.AnulacionRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.Error1RenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.EstadoFirmaRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.EstadoRegistroRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.Anulacion;
/*LOGS REASIGNACION*/
import ec.gob.digercic.saureg.entities.TblSauregUsuAgencia;
import ec.gob.digercic.renavi.entities.LogsReasignacion;
import java.util.logging.Logger;
import java.util.logging.Level;
import ec.gob.digercic.renavi.entities.Error1;
import ec.gob.digercic.renavi.entities.EstadoFirmaRenavi;
import ec.gob.digercic.renavi.entities.EstadoRegistroRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "autAnulacionController")
@ViewScoped
public class AutAnulacionController implements Serializable {

    private String cedulaStringHijo = null, cedulaUserAnul = null;
    private String queryConsulta = "";
    private Date fechaNacIni = null; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaNacFin = null; //fecha inicio Nacimiento Nacido Vivo
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
    private List<Anulacion> resultado;
    private Anulacion registroSeleccionado;
    private List<Error1> erroresAnulacion = new ArrayList<Error1>();
    private List<Error1> selectedErroresAnulacion = new ArrayList<Error1>();
    private Anulacion current = new Anulacion();
    private Boolean buttonStatus = true;
    private String fechaFormato;
    /*Variable para ingresos de logs*/
    private String cedulaProf;
    private String estadoProf;
    private String cedulaCambio = null;
    private String idNacVivo = null;
    /*cambios para reasignacion*/
    private List<TblSauregUsuAgencia> listaUsuario;
    private boolean errorTran = false;
    private String mensajeError = null;

    
    @Resource
    private UserTransaction tran;
    /**/
    @EJB
    private Error1RenaviFacadeLocal ejbErrorAnulacion;
    @EJB
    private AnulacionRenaviFacadeLocal ejbAnulacion;
    @EJB
    private NacidoVivoRenaviFacadeLocal ejbNacidoVivo;
    @EJB
    private EstadoRegistroRenaviFacadeLocal ejbEstRegistro;
    @EJB
    private EstadoFirmaRenaviFacadeLocal ejbEstFirma;
    @EJB
    private LogAccesosRenaviFacadeLocal ejbLogs;
    /*LOGS REASIGNACION*/
    @EJB
    private ec.gob.digercic.renavi.ejb.LogsReasignacionFacadeLocal ejbFacadeLogReasig;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregUsuAgenciaFacadeLocal ejbFacadeUsuAgencia;
    

    public AutAnulacionController() {
    }

    @PostConstruct
    public void init() throws EntidadException {
        current = registroSeleccionado;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        setUserLgn((ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion"));
        resultado = null;
        StringBuilder queryConsulta = new StringBuilder("select *");
        queryConsulta.append(" from anulacion an");
        queryConsulta.append(" where an.id_agencia_anul = ");
        queryConsulta.append(userLgn.getAgenciaInUser().getCodMsp());
        queryConsulta.append(" order by an.fecha_anul DESC");

        try {
            resultado = ejbAnulacion.executeNativeQueryListResultGenerico(queryConsulta.toString(), Anulacion.class);
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. SUPERVISOR REVISA PDF ANULACIÓN");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            getEjbLogs().create(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id").toString();
            ParametroConsulta param = new ParametroConsulta("idNacViv", Long.parseLong(id));
            parametros.add(param);
            NacidoVivoRenavi objeto = (NacidoVivoRenavi) ejbNacidoVivo.consultarTablaSingleResult("NacidoVivoRenavi.findByIdNacViv", parametros);
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

    public String cambiaFormatoFecha(Date date) {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fechaConFormato = formateador.format(date);
        return fechaConFormato;
    }

    /**
     * @return the fechaFormato
     */
    public String getFechaFormato() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        if (registroSeleccionado == null) {
            fechaFormato = null;
        } else {
            try {
                fechaFormato = formateador.format(registroSeleccionado.getFkCedNacViv().getFechaNacimNacViv());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fechaFormato;
    }

    public void onRowSelect(SelectEvent event) {
        this.setButtonStatus(false);
        FacesMessage msg = new FacesMessage("Selected", "ok");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * @return the fechaNacIni
     */
    public Date getFechaNacIni() {
        return fechaNacIni;
    }

    /**
     * @param fechaNacIni the fechaNacIni to set
     */
    public void setFechaNacIni(Date fechaNacIni) {
        this.fechaNacIni = fechaNacIni;
    }

    /**
     * @return the fechaNacFin
     */
    public Date getFechaNacFin() {
        return fechaNacFin;
    }

    /**
     * @param fechaNacFin the fechaNacFin to set
     */
    public void setFechaNacFin(Date fechaNacFin) {
        this.fechaNacFin = fechaNacFin;
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
     * @return the resultado
     */
    public List<Anulacion> getResultado() {
        return resultado;
    }

    /**
     * @param resultado the resultado to set
     */
    public void setResultado(List<Anulacion> resultado) {
        this.resultado = resultado;
    }

    /**
     * @return the queryConsulta
     */
    public String getQueryConsulta() {
        return queryConsulta;
    }

    /**
     * @param queryConsulta the queryConsulta to set
     */
    public void setQueryConsulta(String queryConsulta) {
        this.queryConsulta = queryConsulta;
    }

    /**
     * @return the registroSeleccionado
     */
    public Anulacion getRegistroSeleccionado() {

        return registroSeleccionado;
    }

    /**
     * @param registroSeleccionado the registroSeleccionado to set
     */
    public void setRegistroSeleccionado(Anulacion registroSeleccionado) {
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

    /**
     * @return the cedulaUserAnul
     */
    public String getCedulaUserAnul() {
        return cedulaUserAnul;
    }

    /**
     * @param cedulaUserAnul the cedulaUserAnul to set
     */
    public void setCedulaUserAnul(String cedulaUserAnul) {
        this.cedulaUserAnul = cedulaUserAnul;
    }

    /**
     * @return the ejbNacidoVivo
     */
    public NacidoVivoRenaviFacadeLocal getEjbNacidoVivo() {
        return ejbNacidoVivo;
    }

    /**
     * @param ejbNacidoVivo the ejbNacidoVivo to set
     */
    public void setEjbNacidoVivo(NacidoVivoRenaviFacadeLocal ejbNacidoVivo) {
        this.ejbNacidoVivo = ejbNacidoVivo;
    }

    /**
     * @return the ejbEstRegistro
     */
    public EstadoRegistroRenaviFacadeLocal getEjbEstRegistro() {
        return ejbEstRegistro;
    }

    /**
     * @param ejbEstRegistro the ejbEstRegistro to set
     */
    public void setEjbEstRegistro(EstadoRegistroRenaviFacadeLocal ejbEstRegistro) {
        this.ejbEstRegistro = ejbEstRegistro;
    }

    public void actualizaTablaAnulaciones(ActionEvent event) throws SQLException {
        resultado.clear();
        buscar();
        RequestContext.getCurrentInstance().update("form:tblResultados");
    }

    public Boolean getButtonStatus() {
        return buttonStatus;
    }

    public void setButtonStatus(Boolean buttonStatus) {
        this.buttonStatus = buttonStatus;
    }

    public String getCedulaProf() {
        return cedulaProf;
    }

    public void setCedulaProf(String cedulaProf) {
        this.cedulaProf = cedulaProf;
    }

    public String buscarInactivo() {
        if (registroSeleccionado != null) {
            String usuarioMed = registroSeleccionado.getUserMedicoAnulNacViv();
            ///CAMBIO FWTM
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            parametros.add(new ParametroConsulta("nomUsuario", usuarioMed));
            parametros.add(new ParametroConsulta("status", 'A'));
            try {
                listaUsuario = ejbFacadeUsuAgencia.consultarTablaResultList("TblSauregUsuAgencia.findByNomUsuario", parametros);
                if (listaUsuario.size() >= 1) {
                    estadoProf = "ACTIVO";
                } else {
                    estadoProf = "INACTIVO";
                }
            } catch (EntidadException e) {
                System.out.println("Resulatdo --->" + e.getMessage().toString());
            }
            return estadoProf;
        }
        return null;
    }

    public void aprobar() throws EntidadException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        tran.begin();
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. SUPERVISOR APRUEBA ANULACIÓN");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            getEjbLogs().create(log);
        } catch (EntidadException e) {
            errorTran = true;
            mensajeError = e.getMessage();
            e.printStackTrace();
        }
        current = registroSeleccionado;
        try {
            /*recupero y guardo el nacido vivo original*/
            NacidoVivoRenavi nacVivoRecuperado = new NacidoVivoRenavi();
            nacVivoRecuperado = ejbNacidoVivo.find(current.getFkCedNacViv().getIdNacViv());
            cedulaCambio = nacVivoRecuperado.getCedulNacViv();  //guardo la cédula original

            if (cedulaCambio != null) {
                if (nacVivoRecuperado.getFkIdProEmb().getIdProEmb() > 1) {
                    List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                    parametros.add(new ParametroConsulta("numParto", nacVivoRecuperado.getNumPartoSistema()));
                    parametros.add(new ParametroConsulta("idMad", nacVivoRecuperado.getFkCedulMad()));
                    List<NacidoVivoRenavi> productosMultiples = ejbNacidoVivo.consultarTablaResultList("NacidoVivoRenavi.findProductosPartoMultiple", parametros);

                    for (NacidoVivoRenavi item : productosMultiples) {
                        System.out.println("Cedula Encontrada--->" + cedulaCambio);
                        if (item.getCedulNacViv().equals(nacVivoRecuperado.getCedulNacViv())) {
                            try {
                                /*cambios fwtm*/
                                StringBuilder queryUpdate = new StringBuilder("UPDATE ANULACION SET EST_ANUL_NAC_VIV = '2', ");
                                queryUpdate.append("OBS_RECH = '");
                                queryUpdate.append("SOLICITUD DE ANULACIÓN APROBADA POR: " + current.getObsRech());
                                queryUpdate.append("', CED_SUPERVISOR_ANUL_NAC_VIV = ");
                                queryUpdate.append(userLgn.getNomUsuario());
                                queryUpdate.append(", FECHA_APROBACION = ");
                                queryUpdate.append("(SYSDATE)");
                                queryUpdate.append(" WHERE FK_CED_NAC_VIV = ");
                                queryUpdate.append(item.getIdNacViv());
                                queryUpdate.append(" AND EST_ANUL_NAC_VIV = 1");
                                System.out.println("Query Actualizacion --->" + queryUpdate.toString());
                                int i = ejbAnulacion.executeNativeQuery(queryUpdate.toString());
                                /*final*/
                                EstadoRegistroRenavi estReg, estRegDr;
                                estReg = ejbEstRegistro.find(6);   // 6 = ANULADO.
                                nacVivoRecuperado.setFkIdEstReg(estReg);
                                nacVivoRecuperado.setCedulNacViv(cedulaCambio + "-1");
                                nacVivoRecuperado.setFechaActualizacionNacViv(new Date());
                                ejbNacidoVivo.edit(nacVivoRecuperado); // al nacido vivo original le guardo con cedula(-1) y estado anulado.
                                /*creo y guardo el nacido vivo nuevo para editar*/
                                EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                                NacidoVivoRenavi nacVivCreado = nacVivoRecuperado;
                                estRegDr = ejbEstRegistro.find(7);  //7 = REGISTRO ANULACION PENDIENTE EDICION
                                nacVivCreado.setFkIdEstReg(estRegDr);
                                nacVivCreado.setFkIdEstFir(estFirm);
                                nacVivCreado.setIdNacViv(null);
                                nacVivCreado.setFechaCreacionNacViv(new Date());
                                nacVivCreado.setFechaActualizacionNacViv(new Date());
                                nacVivCreado.setCedulNacViv(cedulaCambio);
                                ejbNacidoVivo.detach(nacVivCreado);
                                ejbNacidoVivo.create(nacVivCreado);
                                /**/
                                StringBuilder queryUpdateAnulacion = new StringBuilder("Update anulacion set fk_ced_nac_viv = ");
                                queryUpdateAnulacion.append(nacVivCreado.getIdNacViv());
                                queryUpdateAnulacion.append(" where fk_ced_nac_viv = ");
                                queryUpdateAnulacion.append(item.getIdNacViv().toString());
                                System.out.println("Query Resulatdo --->" + queryUpdateAnulacion.toString());
                                int j = ejbAnulacion.executeNativeQuery(queryUpdateAnulacion.toString());
                                /**/
                            } catch (EntidadException pme1) {
                                errorTran = true;
                                mensajeError = pme1.getMessage();
                                System.out.println("Resulatdo Error --->" + pme1.getMessage());
                                break;
                            }
                        } else {
                            try {
                                EstadoRegistroRenavi estReg, estRegDr;
                                NacidoVivoRenavi nacVivCreado = item;
                                /*cambios fwtm*/
                                String ced = item.getCedulNacViv();
                                StringBuilder queryUpdate = new StringBuilder("UPDATE ANULACION SET EST_ANUL_NAC_VIV = '2', ");
                                queryUpdate.append("OBS_RECH = '");
                                queryUpdate.append("SOLICITUD DE ANULACIÓN APROBADA POR: " + current.getObsRech());
                                queryUpdate.append("', CED_SUPERVISOR_ANUL_NAC_VIV = ");
                                queryUpdate.append(userLgn.getNomUsuario());
                                queryUpdate.append(", FECHA_APROBACION = ");
                                queryUpdate.append("(SYSDATE)");
                                queryUpdate.append(" where FK_CED_NAC_VIV = ");
                                queryUpdate.append(item.getIdNacViv());
                                queryUpdate.append(" AND EST_ANUL_NAC_VIV = 1");
                                System.out.println("Query Actualizacion --->" + queryUpdate.toString());
                                int i = ejbAnulacion.executeNativeQuery(queryUpdate.toString());
                                /*final*/
                                estReg = ejbEstRegistro.find(6);
                                item.setFkIdEstReg(estReg);
                                item.setCedulNacViv(ced + "-1");
                                ejbNacidoVivo.edit(item);
                                /*creo y guardo el nacido vivo nuevo para editar*/
                                EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                                estRegDr = ejbEstRegistro.find(7);
                                nacVivCreado.setFkIdEstReg(estRegDr);
                                nacVivCreado.setFkIdEstFir(estFirm);
                                nacVivCreado.setIdNacViv(null);
                                nacVivCreado.setFechaActualizacionNacViv(new Date());
                                nacVivCreado.setCedulNacViv(ced);
                                ejbNacidoVivo.detach(nacVivCreado);
                                ejbNacidoVivo.create(nacVivCreado);
                                /**/
                                StringBuilder queryUpdateAnulacion = new StringBuilder("Update anulacion set fk_ced_nac_viv = ");
                                queryUpdateAnulacion.append(nacVivCreado.getIdNacViv());
                                queryUpdateAnulacion.append(" where fk_ced_nac_viv = ");
                                queryUpdateAnulacion.append(item.getIdNacViv().toString());
                                System.out.println("Query Resulatdo --->" + queryUpdateAnulacion.toString());
                                int j = ejbAnulacion.executeNativeQuery(queryUpdateAnulacion.toString());
                                /**/
                            } catch (EntidadException pme2) {
                                errorTran = true;
                                mensajeError = pme2.getMessage();
                                System.out.println("Resulatdo Error --->" + pme2.getMessage());
                                break;
                            }
                        }
                    }
                } else {
                    try {
                        current.setEstAnulNacViv("2"); //1:por verificar, 2: aprobado, 3: rechazado, 4: finalizada
                        current.setObsRech("SOLICITUD DE ANULACIÓN APROBADA POR: " + current.getObsRech());
                        current.setCedSupervisorAnulNacViv(userLgn.getNomUsuario());
                        current.setFechaAprobacion(new Date());
                        getEjbAnulacion().edit(current); //guardo anulacion con el nuevo estado (2= aprobado)
                        EstadoRegistroRenavi estReg, estRegDr;
                        estReg = ejbEstRegistro.find(6);   // 6 = ANULADO.
                        nacVivoRecuperado.setFkIdEstReg(estReg);
                        nacVivoRecuperado.setCedulNacViv(cedulaCambio + "-1");
                        ejbNacidoVivo.edit(nacVivoRecuperado); // al nacido vivo original le guardo con cedula(-1) y estado anulado.
                        /*creo y guardo el nacido vivo nuevo para editar*/
                        EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                        NacidoVivoRenavi nacVivCreado = nacVivoRecuperado;
                        estRegDr = ejbEstRegistro.find(7);  //7 = REGISTRO ANULACION PENDIENTE EDICION
                        nacVivCreado.setFkIdEstReg(estRegDr);
                        nacVivCreado.setFkIdEstFir(estFirm);
                        nacVivCreado.setIdNacViv(null);
                        nacVivCreado.setFechaCreacionNacViv(new Date());
                        nacVivCreado.setFechaActualizacionNacViv(new Date());
                        nacVivCreado.setCedulNacViv(cedulaCambio);
                        ejbNacidoVivo.detach(nacVivCreado);
                        ejbNacidoVivo.create(nacVivCreado);
                        /**/
                        StringBuilder queryUpdateAnulacion = new StringBuilder("Update anulacion set fk_ced_nac_viv = ");
                        queryUpdateAnulacion.append(nacVivCreado.getIdNacViv());
                        queryUpdateAnulacion.append(" where fk_ced_nac_viv = ");
                        queryUpdateAnulacion.append(nacVivoRecuperado.getIdNacViv().toString());
                        System.out.println("Query Resulatdo --->" + queryUpdateAnulacion.toString());
                        int j = ejbAnulacion.executeNativeQuery(queryUpdateAnulacion.toString());
                        /**/
                    } catch (EntidadException e) {
                        errorTran = true;
                        mensajeError = e.getMessage();
                    }
                }
            } else {
                if (nacVivoRecuperado.getFkIdProEmb().getIdProEmb() > 1) {
                    List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                    parametros.add(new ParametroConsulta("numParto", nacVivoRecuperado.getNumPartoSistema()));
                    parametros.add(new ParametroConsulta("idMad", nacVivoRecuperado.getFkCedulMad()));
                    List<NacidoVivoRenavi> productosMultiples = ejbNacidoVivo.consultarTablaResultList("NacidoVivoRenavi.findProductosPartoMultiple", parametros);

                    for (NacidoVivoRenavi item : productosMultiples) {
                        System.out.println("Cedula Encontrada--->" + cedulaCambio);
                        if (item.getIdNacViv().equals(nacVivoRecuperado.getIdNacViv())) {
                            try {
                                /*cambios fwtm*/
                                StringBuilder queryUpdate = new StringBuilder("UPDATE ANULACION SET EST_ANUL_NAC_VIV = '2', ");
                                queryUpdate.append("OBS_RECH = '");
                                queryUpdate.append("SOLICITUD DE ANULACIÓN APROBADA POR: " + current.getObsRech());
                                queryUpdate.append("', CED_SUPERVISOR_ANUL_NAC_VIV = ");
                                queryUpdate.append(userLgn.getNomUsuario());
                                queryUpdate.append(", FECHA_APROBACION = ");
                                queryUpdate.append("(SYSDATE)");
                                queryUpdate.append(" where FK_CED_NAC_VIV = ");
                                queryUpdate.append(item.getIdNacViv());
                                queryUpdate.append(" AND EST_ANUL_NAC_VIV = 1");
                                System.out.println("Query Actualizacion --->" + queryUpdate.toString());
                                int i = ejbAnulacion.executeNativeQuery(queryUpdate.toString());
                                /*final*/
                                EstadoRegistroRenavi estReg, estRegDr;
                                String historiaCli = nacVivoRecuperado.getNumeroHistoriaNacViv();
                                String codigoRc = nacVivoRecuperado.getCodigoRcNacViv();
                                estReg = ejbEstRegistro.find(6);   // 6 = ANULADO.
                                nacVivoRecuperado.setFkIdEstReg(estReg);
                                nacVivoRecuperado.setNumeroHistoriaNacViv(historiaCli + "-1");
                                nacVivoRecuperado.setCodigoRcNacViv(codigoRc + "-1");
                                nacVivoRecuperado.setFechaActualizacionNacViv(new Date());
                                ejbNacidoVivo.edit(nacVivoRecuperado); // al nacido vivo original le guardo con cedula(-1) y estado anulado.
                                /*creo y guardo el nacido vivo nuevo para editar*/
                                EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                                NacidoVivoRenavi nacVivCreado = nacVivoRecuperado;
                                estRegDr = ejbEstRegistro.find(7);  //7 = REGISTRO ANULACION PENDIENTE EDICION
                                nacVivCreado.setFkIdEstReg(estRegDr);
                                nacVivCreado.setFkIdEstFir(estFirm);
                                nacVivCreado.setIdNacViv(null);
                                nacVivCreado.setFechaActualizacionNacViv(new Date());
                                nacVivCreado.setNumeroHistoriaNacViv(historiaCli);
                                nacVivCreado.setCodigoRcNacViv(codigoRc);
                                ejbNacidoVivo.detach(nacVivCreado);
                                ejbNacidoVivo.create(nacVivCreado);
                                /**/
                                StringBuilder queryUpdateAnulacion = new StringBuilder("Update anulacion set fk_ced_nac_viv = ");
                                queryUpdateAnulacion.append(nacVivCreado.getIdNacViv());
                                queryUpdateAnulacion.append(" where fk_ced_nac_viv = ");
                                queryUpdateAnulacion.append(item.getIdNacViv().toString());
                                System.out.println("Query Resulatdo --->" + queryUpdateAnulacion.toString());
                                int j = ejbAnulacion.executeNativeQuery(queryUpdateAnulacion.toString());
                                /**/
                            } catch (EntidadException pme1) {
                                errorTran = true;
                                mensajeError = pme1.getMessage();
                                System.out.println("Resulatdo Error --->" + pme1.getMessage());
                                break;
                            }
                        } else {
                            try {
                                EstadoRegistroRenavi estReg, estRegDr;
                                NacidoVivoRenavi nacVivCreado = item;
                                /*cambios fwtm*/
                                StringBuilder queryUpdate = new StringBuilder("UPDATE ANULACION SET EST_ANUL_NAC_VIV = '2', ");
                                queryUpdate.append("OBS_RECH = '");
                                queryUpdate.append("SOLICITUD DE ANULACIÓN APROBADA POR: " + current.getObsRech());
                                queryUpdate.append("', CED_SUPERVISOR_ANUL_NAC_VIV = ");
                                queryUpdate.append(userLgn.getNomUsuario());
                                queryUpdate.append(", FECHA_APROBACION = ");
                                queryUpdate.append("(SYSDATE)");
                                queryUpdate.append(" where FK_CED_NAC_VIV = ");
                                queryUpdate.append(item.getIdNacViv());
                                queryUpdate.append(" AND EST_ANUL_NAC_VIV = 1");
                                System.out.println("Query Actualizacion --->" + queryUpdate.toString());
                                int i = ejbAnulacion.executeNativeQuery(queryUpdate.toString());
                                /*final*/
                                String historiaCli = item.getNumeroHistoriaNacViv();
                                String codigoRc = item.getCodigoRcNacViv();
                                estReg = ejbEstRegistro.find(6);
                                item.setFkIdEstReg(estReg);
                                item.setNumeroHistoriaNacViv(historiaCli + "-1");
                                item.setCodigoRcNacViv(codigoRc + "-1");
                                ejbNacidoVivo.edit(item);
                                /*creo y guardo el nacido vivo nuevo para editar*/
                                EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                                estRegDr = ejbEstRegistro.find(7);
                                nacVivCreado.setFkIdEstReg(estRegDr);
                                nacVivCreado.setFkIdEstFir(estFirm);
                                nacVivCreado.setIdNacViv(null);
                                nacVivCreado.setFechaActualizacionNacViv(new Date());
                                nacVivCreado.setNumeroHistoriaNacViv(historiaCli);
                                nacVivCreado.setCodigoRcNacViv(codigoRc);
                                ejbNacidoVivo.detach(nacVivCreado);
                                ejbNacidoVivo.create(nacVivCreado);
                                /**/
                                StringBuilder queryUpdateAnulacion = new StringBuilder("Update anulacion set fk_ced_nac_viv = ");
                                queryUpdateAnulacion.append(nacVivCreado.getIdNacViv());
                                queryUpdateAnulacion.append(" where fk_ced_nac_viv = ");
                                queryUpdateAnulacion.append(item.getIdNacViv().toString());
                                System.out.println("Query Resulatdo --->" + queryUpdateAnulacion.toString());
                                int j = ejbAnulacion.executeNativeQuery(queryUpdateAnulacion.toString());
                                /**/
                            } catch (EntidadException pme2) {
                                errorTran = true;
                                mensajeError = pme2.getMessage();
                                System.out.println("Resulatdo Error --->" + pme2.getMessage());
                                break;
                            }
                        }
                    }
                } else {
                    try {
                        current.setEstAnulNacViv("2"); //1:por verificar, 2: aprobado, 3: rechazado, 4: finalizada
                        current.setObsRech("SOLICITUD DE ANULACIÓN APROBADA POR: " + current.getObsRech());
                        current.setCedSupervisorAnulNacViv(userLgn.getNomUsuario());
                        current.setFechaAprobacion(new Date());
                        String historiaCli = nacVivoRecuperado.getNumeroHistoriaNacViv();
                        String codigoRc = nacVivoRecuperado.getCodigoRcNacViv();
                        nacVivoRecuperado.setNumeroHistoriaNacViv(historiaCli + "-1");
                        nacVivoRecuperado.setCodigoRcNacViv(codigoRc + "-1");
                        getEjbAnulacion().edit(current); //guardo anulacion con el nuevo estado (2= aprobado)
                        EstadoRegistroRenavi estReg, estRegDr;
                        estReg = ejbEstRegistro.find(6);   // 6 = ANULADO.
                        nacVivoRecuperado.setFkIdEstReg(estReg);
                        ejbNacidoVivo.edit(nacVivoRecuperado); // al nacido vivo original le guardo con cedula(-1) y estado anulado.
                        /*creo y guardo el nacido vivo nuevo para editar*/
                        EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                        NacidoVivoRenavi nacVivCreado = nacVivoRecuperado;
                        estRegDr = ejbEstRegistro.find(7);  //7 = REGISTRO ANULACION PENDIENTE EDICION
                        nacVivCreado.setFkIdEstReg(estRegDr);
                        nacVivCreado.setFkIdEstFir(estFirm);
                        nacVivCreado.setIdNacViv(null);
                        nacVivCreado.setFechaCreacionNacViv(new Date());
                        nacVivCreado.setFechaActualizacionNacViv(new Date());
                        nacVivCreado.setNumeroHistoriaNacViv(historiaCli);
                        nacVivCreado.setCodigoRcNacViv(codigoRc);
                        ejbNacidoVivo.detach(nacVivCreado);
                        ejbNacidoVivo.create(nacVivCreado);
                        /**/
                        StringBuilder queryUpdateAnulacion = new StringBuilder("Update anulacion set fk_ced_nac_viv = ");
                        queryUpdateAnulacion.append(nacVivCreado.getIdNacViv());
                        queryUpdateAnulacion.append(" where fk_ced_nac_viv = ");
                        queryUpdateAnulacion.append(nacVivoRecuperado.getIdNacViv().toString());
                        System.out.println("Query Resulatdo --->" + queryUpdateAnulacion.toString());
                        int j = ejbAnulacion.executeNativeQuery(queryUpdateAnulacion.toString());
                        /**/
                    } catch (EntidadException e) {
                        errorTran = true;
                        mensajeError = e.getMessage();
                    }
                }
            }

        } catch (EntidadException ee) {
            errorTran = true;
            mensajeError = ee.getMessage();
        }
        /*Cambio para cambiar los usuarios e ingresar el Log FWTM*/
        if (!errorTran && !estadoProf.equals("ACTIVO")) {
            try {
                NacidoVivoRenavi getItem = ejbNacidoVivo.find(registroSeleccionado.getFkCedNacViv().getIdNacViv());
                System.out.println("Cedula Re-asignado--> " + cedulaProf);
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("numParto", getItem.getNumPartoSistema()));
                parametros.add(new ParametroConsulta("idMad", getItem.getFkCedulMad()));
                List<NacidoVivoRenavi> productosMultiples = ejbNacidoVivo.consultarTablaResultList("NacidoVivoRenavi.findProductosPartoMultiple", parametros);
                    for (NacidoVivoRenavi item : productosMultiples) {
                        Date horaReasignacion = new Date();
                        LogsReasignacion logReasig = new LogsReasignacion();
                        logReasig.setIdNacViv(item.getIdNacViv());
                        logReasig.setFechaAsigReg(horaReasignacion);
                        logReasig.setFkAgenciaSaureg(new Long(userLgn.getAgenciaInUser().getCodMsp()));
                        logReasig.setUsuarioEjecAsig(userLgn.getNomUsuario());
                        logReasig.setUsuarioRegAnt(item.getFkUsuSaureg());
                        logReasig.setUsuarioRegAsig(cedulaProf);
                        item.setFkUsuSaureg(cedulaProf);
                        item.setFkUsuFirmaSaureg(cedulaProf);
                        try {
                            ejbFacadeLogReasig.create(logReasig);
                            ejbNacidoVivo.edit(item);
                        } catch (EntidadException ex) {
                            errorTran = true;
                            mensajeError = ex.getMessage();
                            break;
                        }
                    }
            } catch (EntidadException e) {
                errorTran = true;
                mensajeError = e.getMessage();
            }
        }
        
        if (errorTran) {
            tran.rollback();
            JsfUtil.displayMessage(" '"+ mensajeError +"', se realiza Rollback.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
        } else {
            tran.commit();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("AnulacionRenaviAprobado"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().update("motAprob");
            RequestContext.getCurrentInstance().execute("PF('dlg2').hide();");
            RequestContext.getCurrentInstance().execute("PF('nvDialog').hide();");
            setCurrent(new Anulacion());
        }
        limpiar();
    }

    public void rechazar() throws EntidadException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        List<Object[]> lstVerificacion = new ArrayList<Object[]>();
        List<Anulacion> lstAnulacion = new ArrayList<Anulacion>();
        StringBuilder idNacidos = new StringBuilder();
        StringBuilder queryConsulta2 = new StringBuilder();

        current = registroSeleccionado;
        tran.begin();
        if (registroSeleccionado.getFkCedNacViv().getFkCedulMad().getCedulMad() != null) {
            queryConsulta2.append("select a.id_mad, a.cedul_mad, b.id_nac_viv");
            queryConsulta2.append(" from madre_renavi a, nacido_vivo_renavi b, anulacion c");
            queryConsulta2.append(" where a.id_mad = b.fk_cedul_mad");
            queryConsulta2.append(" and b.id_nac_viv = c.fk_ced_nac_viv");
            queryConsulta2.append(" and a.cedul_mad = '");
            queryConsulta2.append(registroSeleccionado.getFkCedNacViv().getFkCedulMad().getCedulMad());
            queryConsulta2.append("'");
            queryConsulta2.append(" and c.est_anul_nac_viv = '1' ");
        } else {
            queryConsulta2.append("select a.id_mad, a.cedul_mad, b.id_nac_viv");
            queryConsulta2.append(" from madre_renavi a, nacido_vivo_renavi b, anulacion c");
            queryConsulta2.append(" where a.id_mad = b.fk_cedul_mad");
            queryConsulta2.append(" and b.id_nac_viv = c.fk_ced_nac_viv");
            queryConsulta2.append(" and a.id_mad= '");
            queryConsulta2.append(registroSeleccionado.getFkCedNacViv().getFkCedulMad().getIdMad());
            queryConsulta2.append("'");
            queryConsulta2.append(" and c.est_anul_nac_viv = '1' ");
        }
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. SUPERVISOR RECHAZA ANULACIÓN");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            getEjbLogs().create(log);
            lstVerificacion = getEjbAnulacion().executeNativeQueryListResult(queryConsulta2.toString());
        } catch (EntidadException e) {
            errorTran = true;
            mensajeError = e.getMessage();
        }
        /*Cambio para rechazar registros para partos Multiples*/
        for (int i = 0; i < lstVerificacion.size(); i++) {
            idNacidos.append(lstVerificacion.get(i)[2].toString());
            if (i < lstVerificacion.size() - 1) {
                idNacidos.append(",");
            }
        }
        StringBuilder queryBusqueda = new StringBuilder("select id, fk_ced_nac_viv, nom_medico_anul_nac_viv, apell_medico_anul_nac_viv,");
        queryBusqueda.append(" user_medico_anul_nac_viv, fecha_anul, justificacion, ced_supervisor_anul_nac_viv, est_anul_nac_viv, obs_rech,");
        queryBusqueda.append(" id_agencia_anul, fecha_aprobacion, fecha_rechazo, fecha_finalizacion, observacion_rechazo_leido");
        queryBusqueda.append(" from anulacion");
        queryBusqueda.append(" where fk_ced_nac_viv in (");
        queryBusqueda.append(idNacidos.toString());
        queryBusqueda.append(")");
        try {
            if (lstVerificacion.size() > 1) {
                lstAnulacion = getEjbAnulacion().executeNativeQueryListResultGenerico(queryBusqueda.toString(), Anulacion.class);
                for (Anulacion lstRegistros : lstAnulacion) {
                    try {
                        lstRegistros.setEstAnulNacViv("3");
                        lstRegistros.setObsRech("SOLICITUD DE ANULACIÓN RECHAZADA POR: " + registroSeleccionado.getObsRech());
                        lstRegistros.setCedSupervisorAnulNacViv(userLgn.getNomUsuario());
                        lstRegistros.setFechaRechazo(new Date());
                        getEjbAnulacion().edit(lstRegistros);
                    } catch (Exception am) {
                        errorTran = true;
                        mensajeError = am.getMessage();
                        break;
                    }
                }
            } else {
                current.setEstAnulNacViv("3"); //de momento 1:por verificar, 2: aprobado, 3: rechazado 
                current.setObsRech("SOLICITUD DE ANULACIÓN RECHAZADA POR: " + registroSeleccionado.getObsRech());
                current.setCedSupervisorAnulNacViv(userLgn.getNomUsuario());
                current.setFechaRechazo(new Date());
                getEjbAnulacion().edit(current);
            }/* Cambio para rechazar registros de partos Multiples   FWTM  23-12-2015 */
        } catch (Exception ee) {
            errorTran = true;
            mensajeError = ee.getMessage();
        }
        if (errorTran) {
            tran.rollback();
            JsfUtil.displayMessage("Error en la Base de Datos, se realiza Rollback.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
        } else {
            tran.commit();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("AnulacionRenaviRechazado"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().update("motRec");
            RequestContext.getCurrentInstance().execute("PF('dlg1').hide();");
            RequestContext.getCurrentInstance().execute("PF('nvDialog').hide();");
            setCurrent(new Anulacion());
        }
        limpiar();
    }

    public void buscar() throws SQLException {
        resultado.clear();
        StringBuilder queryConsulta = new StringBuilder("select *");
        queryConsulta.append(" from anulacion an");
        queryConsulta.append(" where an.id_agencia_anul = ");
        queryConsulta.append(userLgn.getAgenciaInUser().getCodMsp());
        queryConsulta.append(" order by an.fecha_anul DESC");
        try {
            resultado = ejbAnulacion.executeNativeQueryListResultGenerico(queryConsulta.toString(), Anulacion.class);
            RequestContext.getCurrentInstance().update("form:tblResultados");
        } catch (EntidadException e) {
            e.getCause();
            e.printStackTrace();
            System.out.println("El Error es --->" + e.getMessage());
        }
    }
    
    /**/
    public void limpiar() {
        getRegistroSeleccionado().setJustificacion(null);
        getRegistroSeleccionado().setObsRech(null);
    }
    /**/

    /**
     * @return the ejbEstFirma
     */
    public EstadoFirmaRenaviFacadeLocal getEjbEstFirma() {
        return ejbEstFirma;
    }

    /**
     * @param ejbEstFirma the ejbEstFirma to set
     */
    public void setEjbEstFirma(EstadoFirmaRenaviFacadeLocal ejbEstFirma) {
        this.ejbEstFirma = ejbEstFirma;
    }

    /**
     * @param fechaFormato the fechaFormato to set
     */
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
