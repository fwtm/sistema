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
import ec.gob.digercic.renavi.entities.Error1;
import ec.gob.digercic.renavi.entities.EstadoFirmaRenavi;
import ec.gob.digercic.renavi.entities.EstadoRegistroRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.LogsReasignacion;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.entities.TblSauregUsuAgencia;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
import javax.annotation.Resource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "anulacionSupervisorController")
@ViewScoped
public class AnulacionSupervisorController implements Serializable {
    
    private String cedulaString;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
    private List<NacidoVivoRenavi> resultado;
    private NacidoVivoRenavi registroSeleccionado;
    private List<Error1> erroresAnulacion = new ArrayList<Error1>();
    private List<Error1> selectedErroresAnulacion = new ArrayList<Error1>();
    private String fechaFormato;
    private Boolean buttonStatus = true;
    private Integer idMadreIn = null;
    /*Variable para ingresos de logs*/
    private String cedulaProf;
    private String estadoProf;;
    /*cambios para reasignacion*/
    private List<TblSauregUsuAgencia> listaUsuario;
    private String estadoRegistro;
    private Boolean flag = false;
    /*Cambio para anulacion con rollback*/
    private boolean errorTran = false;
    private String mensajeError = null;
    private Anulacion current = new Anulacion();
    private String estFirma;
    private String estReg;
    private String estAnul;

    @Resource
    private UserTransaction tran;
    /**/
    @EJB
    private NacidoVivoRenaviFacadeLocal ejbNacidoVivoRenaviFacade;
    @EJB
    private Error1RenaviFacadeLocal ejbErrorAnulacion;
    @EJB
    private AnulacionRenaviFacadeLocal ejbAnulacion;
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
    
    public AnulacionSupervisorController() {
    }
    
    @PostConstruct
    public void init() throws EntidadException {
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
    
    public String getCedulaProf() {
        return cedulaProf;
    }

    public void setCedulaProf(String cedulaProf) {
        this.cedulaProf = cedulaProf;
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

    public Integer getIdMadreIn() {
        return idMadreIn;
    }

    public void setIdMadreIn(Integer idMadreIn) {
        this.idMadreIn = idMadreIn;
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

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }
    
    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
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

    /**
     * @return the fechaFormato
     */
    public String getFechaFormato() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        if (registroSeleccionado == null) {
            fechaFormato = null;
        } else {
            try {                
                fechaFormato = formateador.format(registroSeleccionado.getFechaNacimNacViv());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        
        System.out.println("fechaFormato: " + fechaFormato);
        return fechaFormato;
    }
    
    public Boolean getButtonStatus() {
        return buttonStatus;
    }
    
    public void setButtonStatus(Boolean buttonStatus) {
        this.buttonStatus = buttonStatus;
    }
    
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
        queryConsulta.append(getUserLgn().getNomUsuario());
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
    
    public void create() throws EntidadException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        getErroresAnulacion();
        selectedErroresAnulacion = erroresAnulacion;
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. CREA REGISTRO");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            getEjbLogs().create(log);
        } catch (Exception e) {
            errorTran = true;
            mensajeError = e.getMessage();
        }
        if (registroSeleccionado.getCedulNacViv() != null) {
            if (registroSeleccionado.getFkIdProEmb().getIdProEmb() > 1) {
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("numParto", registroSeleccionado.getNumPartoSistema()));
                parametros.add(new ParametroConsulta("idMad", registroSeleccionado.getFkCedulMad()));
                List<NacidoVivoRenavi> productosMultiples = ejbNacidoVivoRenaviFacade.consultarTablaResultList("NacidoVivoRenavi.findProductosPartoMultiple", parametros);
                for (NacidoVivoRenavi item : productosMultiples) {
                        try {
                            Anulacion anula = new Anulacion();
                            anula.setApellMedicoAnulNacViv(userLgn.getApellido());
                            anula.setNomMedicoAnulNacViv(userLgn.getNombre());
                            anula.setUserMedicoAnulNacViv(item.getFkUsuFirmaSaureg());
                            anula.setCedSupervisorAnulNacViv(userLgn.getNomUsuario());
                            anula.setFechaAnul(new Date());
                            anula.setFkCedNacViv(item);
                            anula.setErr(selectedErroresAnulacion);
                            anula.setJustificacion("PARTO MÚLTIPLE ANULADO POR SUPERVISOR: " + userLgn.getNomUsuario() + " MOTIVO: " + current.getJustificacion());
                            anula.setIdAgenciaAnul(new BigDecimal(userLgn.getAgenciaInUser().getCodMsp()));
                            anula.setEstAnulNacViv("2"); //de momento 1:por verificar, 2: aprobado, 3: rechazado 
                            anula.setObservacionRechazoLeido("0");
                            anula.setObsRech("SOLICITUD DE ANULACIÓN APROBADA.");
                            getEjbAnulacion().create(anula);
                            /* aquí va la logica de clonación del registro de NV*/
                            /*recupero y guardo el nacido vivo original*/
                            NacidoVivoRenavi nacVivoRecuperado = new NacidoVivoRenavi();
                            nacVivoRecuperado = item;
                            String cedula = nacVivoRecuperado.getCedulNacViv();
                            EstadoRegistroRenavi estReg, estRegDr;
                            estReg = ejbEstRegistro.find(6);   // 6 = ANULADO.
                            nacVivoRecuperado.setFkIdEstReg(estReg);
                            nacVivoRecuperado.setCedulNacViv(cedula + "-1");
                            ejbNacidoVivoRenaviFacade.edit(nacVivoRecuperado);
                            /*creo y guardo el nacido vivo nuevo para editar*/
                            EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                            NacidoVivoRenavi nacVivCreado = nacVivoRecuperado;
                            estRegDr = ejbEstRegistro.find(7);  //7 = REGISTRO ANULACION PENDIENTE EDICION
                            nacVivCreado.setFkIdEstReg(estRegDr);
                            nacVivCreado.setFkIdEstFir(estFirm);
                            nacVivCreado.setIdNacViv(null);
                            nacVivCreado.setFechaActualizacionNacViv(new Date());
                            nacVivCreado.setFechaCreacionNacViv(new Date());
                            nacVivCreado.setCedulNacViv(cedula);
                            ejbNacidoVivoRenaviFacade.detach(nacVivCreado);
                            ejbNacidoVivoRenaviFacade.create(nacVivCreado);
                        } catch (Exception ee) {
                            errorTran = true;
                            mensajeError = ee.getMessage();
                            break;
                        }
              }
            } else {
                try {
                    Anulacion anula = new Anulacion();
                    anula.setApellMedicoAnulNacViv(userLgn.getApellido());
                    anula.setNomMedicoAnulNacViv(userLgn.getNombre());
                    anula.setUserMedicoAnulNacViv(registroSeleccionado.getFkUsuFirmaSaureg());
                    anula.setCedSupervisorAnulNacViv(userLgn.getNomUsuario());
                    anula.setFechaAnul(new Date());
                    anula.setFkCedNacViv(registroSeleccionado);
                    anula.setErr(selectedErroresAnulacion);
                    anula.setJustificacion("ANULADO POR SUPERVISOR: " + userLgn.getNomUsuario() + " MOTIVO:" + current.getJustificacion());
                    anula.setIdAgenciaAnul(new BigDecimal(userLgn.getAgenciaInUser().getCodMsp()));
                    anula.setEstAnulNacViv("2"); //de momento 1:por verificar, 2: aprobado, 3: rechazado 
                    anula.setObservacionRechazoLeido("0");
                    anula.setObsRech("SOLICITUD DE ANULACIÓN APROBADA.");
                    getEjbAnulacion().create(anula);
                    /*logica de clonacion de nacido vivos*/
                    NacidoVivoRenavi nacVivoRecuperado = new NacidoVivoRenavi();
                    nacVivoRecuperado = ejbNacidoVivoRenaviFacade.find(registroSeleccionado.getIdNacViv());
                    String cedula = nacVivoRecuperado.getCedulNacViv();
                    EstadoRegistroRenavi estReg, estRegDr;
                    estReg = ejbEstRegistro.find(6);   // 6 = ANULADO.
                    nacVivoRecuperado.setFkIdEstReg(estReg);
                    nacVivoRecuperado.setCedulNacViv(cedula + "-1");
                    ejbNacidoVivoRenaviFacade.edit(nacVivoRecuperado);
                    /*creo y guardo el nacido vivo nuevo para editar*/
                    EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                    NacidoVivoRenavi nacVivCreado = nacVivoRecuperado;
                    estRegDr = ejbEstRegistro.find(7);  //7 = REGISTRO ANULACION PENDIENTE EDICION
                    nacVivCreado.setFkIdEstReg(estRegDr);
                    nacVivCreado.setFkIdEstFir(estFirm);
                    nacVivCreado.setIdNacViv(null);
                    nacVivCreado.setFechaCreacionNacViv(new Date());
                    nacVivCreado.setFechaActualizacionNacViv(new Date());
                    nacVivCreado.setCedulNacViv(cedula);
                    ejbNacidoVivoRenaviFacade.detach(nacVivCreado);
                    ejbNacidoVivoRenaviFacade.create(nacVivCreado);
                } catch (Exception ee) {
                    errorTran = true;
                    mensajeError = ee.getMessage();
                }
            }
        } else { /*cambio para anular registros de madres indocumentadas   FWTM  07-03-2016*/
                if (registroSeleccionado.getFkIdProEmb().getIdProEmb() > 1) {
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("numParto", registroSeleccionado.getNumPartoSistema()));
                parametros.add(new ParametroConsulta("idMad", registroSeleccionado.getFkCedulMad()));
                List<NacidoVivoRenavi> productosMultiples = ejbNacidoVivoRenaviFacade.consultarTablaResultList("NacidoVivoRenavi.findProductosPartoMultiple", parametros);
                for (NacidoVivoRenavi item : productosMultiples) {
                        try {
                            Anulacion anula = new Anulacion();
                            anula.setApellMedicoAnulNacViv(userLgn.getApellido());
                            anula.setNomMedicoAnulNacViv(userLgn.getNombre());
                            anula.setUserMedicoAnulNacViv(item.getFkUsuFirmaSaureg());
                            anula.setCedSupervisorAnulNacViv(userLgn.getNomUsuario());
                            anula.setFechaAnul(new Date());
                            anula.setFkCedNacViv(item);
                            anula.setErr(selectedErroresAnulacion);
                            anula.setJustificacion("PARTO MÚLTIPLE ANULADO POR SUPERVISOR: " + userLgn.getNomUsuario() + " MOTIVO: " + current.getJustificacion());
                            anula.setIdAgenciaAnul(new BigDecimal(userLgn.getAgenciaInUser().getCodMsp()));
                            anula.setEstAnulNacViv("2"); //de momento 1:por verificar, 2: aprobado, 3: rechazado 
                            anula.setObservacionRechazoLeido("0");
                            anula.setObsRech("SOLICITUD DE ANULACIÓN APROBADA.");
                            getEjbAnulacion().create(anula);
                            /* aquí va la logica de clonación del registro de NV*/
                            /*recupero y guardo el nacido vivo original*/
                            NacidoVivoRenavi nacVivoRecuperado = new NacidoVivoRenavi();
                            nacVivoRecuperado = item;
                            String historiaCli = nacVivoRecuperado.getNumeroHistoriaNacViv();
                            String codigoRc = nacVivoRecuperado.getCodigoRcNacViv();
                            EstadoRegistroRenavi estReg, estRegDr;
                            estReg = ejbEstRegistro.find(6);   // 6 = ANULADO.
                            nacVivoRecuperado.setFkIdEstReg(estReg);
                            nacVivoRecuperado.setNumeroHistoriaNacViv(historiaCli + "-1");
                            nacVivoRecuperado.setCodigoRcNacViv(codigoRc + "-1");
                            ejbNacidoVivoRenaviFacade.edit(nacVivoRecuperado);
                            /*creo y guardo el nacido vivo nuevo para editar*/
                            EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
                            NacidoVivoRenavi nacVivCreado = nacVivoRecuperado;
                            estRegDr = ejbEstRegistro.find(7);  //7 = REGISTRO ANULACION PENDIENTE EDICION
                            nacVivCreado.setFkIdEstReg(estRegDr);
                            nacVivCreado.setFkIdEstFir(estFirm);
                            nacVivCreado.setIdNacViv(null);
                            nacVivCreado.setFechaActualizacionNacViv(new Date());
                            nacVivCreado.setFechaCreacionNacViv(new Date());
                            nacVivCreado.setNumeroHistoriaNacViv(historiaCli);
                            nacVivCreado.setCodigoRcNacViv(codigoRc);
                            ejbNacidoVivoRenaviFacade.detach(nacVivCreado);
                            ejbNacidoVivoRenaviFacade.create(nacVivCreado);
                        } catch (Exception ee) {
                            errorTran = true;
                            mensajeError = ee.getMessage();
                            break;
                        }
                }
            } else {
                try {
                    Anulacion anula = new Anulacion();
                    anula.setApellMedicoAnulNacViv(userLgn.getApellido());
                    anula.setNomMedicoAnulNacViv(userLgn.getNombre());
                    anula.setUserMedicoAnulNacViv(registroSeleccionado.getFkUsuFirmaSaureg());
                    anula.setCedSupervisorAnulNacViv(userLgn.getNomUsuario());
                    anula.setFechaAnul(new Date());
                    anula.setFkCedNacViv(registroSeleccionado);
                    anula.setErr(selectedErroresAnulacion);
                    anula.setJustificacion("ANULADO POR SUPERVISOR: " + userLgn.getNomUsuario() + " MOTIVO:" + current.getJustificacion());
                    anula.setIdAgenciaAnul(new BigDecimal(userLgn.getAgenciaInUser().getCodMsp()));
                    anula.setEstAnulNacViv("2"); //de momento 1:por verificar, 2: aprobado, 3: rechazado 
                    anula.setObservacionRechazoLeido("0");
                    anula.setObsRech("SOLICITUD DE ANULACIÓN APROBADA.");
                    getEjbAnulacion().create(anula);
                    /*logica de clonacion de nacido vivos*/
                    NacidoVivoRenavi nacVivoRecuperado = new NacidoVivoRenavi();
                    nacVivoRecuperado = ejbNacidoVivoRenaviFacade.find(registroSeleccionado.getIdNacViv());;
                    String historiaCli = nacVivoRecuperado.getNumeroHistoriaNacViv();
                    String codigoRc = nacVivoRecuperado.getCodigoRcNacViv();
                    EstadoRegistroRenavi estReg, estRegDr;
                    estReg = ejbEstRegistro.find(6);   // 6 = ANULADO.
                    nacVivoRecuperado.setFkIdEstReg(estReg);
                    nacVivoRecuperado.setNumeroHistoriaNacViv(historiaCli + "-1");
                    nacVivoRecuperado.setCodigoRcNacViv(codigoRc + "-1");
                    ejbNacidoVivoRenaviFacade.edit(nacVivoRecuperado);
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
                    ejbNacidoVivoRenaviFacade.detach(nacVivCreado);
                    ejbNacidoVivoRenaviFacade.create(nacVivCreado);
                } catch (Exception ee) {
                    errorTran = true;
                    mensajeError = ee.getMessage();
                }
            }
        }/*cambio para anular registros de madres indocumentadas   FWTM  07-03-2016*/
        /*Cambio para cambiar los usuarios e ingresar el Log FWTM*/
        if (!errorTran && !estadoProf.equals("ACTIVO")) {
            try {
                NacidoVivoRenavi getItem = ejbNacidoVivoRenaviFacade.find(registroSeleccionado.getIdNacViv());
                System.out.println("Cedula Re-asignado--> " + cedulaProf);
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("numParto", getItem.getNumPartoSistema()));
                parametros.add(new ParametroConsulta("idMad", getItem.getFkCedulMad()));
                List<NacidoVivoRenavi> productosMultiples = ejbNacidoVivoRenaviFacade.consultarTablaResultList("NacidoVivoRenavi.findProductosPartoMultiple", parametros);
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
                            ejbNacidoVivoRenaviFacade.edit(item);
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
            limpiar();
        } else {
            tran.commit();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("AnulacionRenaviCreado"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('nvDialog').hide();");
            setCurrent(new Anulacion());
            limpiar();
        }
    }

    public void generaReporte() throws SQLException, EntidadException {
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. BUSCA REGISTRO");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            getEjbLogs().create(log);
        } catch (Exception e) {
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. BUSQUEDA DE REGISTROS MADRE INDOCUMENTADA");//accion realizada, con el siguiente en formato:
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
    
    public void verPDFConFirma() throws IOException {
        
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. REVISA PDF");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
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
    
     public String buscarInactivo() {
        if (registroSeleccionado != null) {
            String usuarioMed = registroSeleccionado.getFkUsuSaureg();
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
     
     public void limpiar() {
         getRegistroSeleccionado().setFkCedulMad(null);
         cedulaString = null;
         idMadreIn = 0;
     }
}
