package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogEliminacionFacadeLocal;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.LogsEliminacion;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;


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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "madreRenaviFController")
@ViewScoped
public class MadreRenaviFController implements Serializable {

    private NacidoVivoRenavi hijoCurrent;
    private List<NacidoVivoRenavi> hijoItems;
    private List<NacidoVivoRenavi> hijoItems2;
    @EJB
    private ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal ejbFacadeNacidoVivoRenavi;
    @EJB
    private ec.gob.digercic.renavi.ejb.MadreRenaviFacadeLocal ejbFacade;
    @EJB
    private LogAccesosRenaviFacadeLocal ejbLogs;
    @EJB
    private LogEliminacionFacadeLocal ejbLogsEli;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn; //JJHF CAMBIO LOGIN
    private List<NacidoVivoRenavi> listnacidovivo = new ArrayList<NacidoVivoRenavi>();
    private int e;

    public MadreRenaviFController() {
    }

    public NacidoVivoRenavi getHijoCurrent() {
        if (hijoCurrent == null) {
            hijoCurrent = new NacidoVivoRenavi();
        }
        return hijoCurrent;
    }

    public List<NacidoVivoRenavi> getHijoItems() {
        //NacidoVivoRenavi.findAllByInstitucion
        if (hijoItems == null) {
            hijoItems = new ArrayList<NacidoVivoRenavi>();
        }
        return hijoItems;
    }

    public List<NacidoVivoRenavi> getHijoItems2() {
        return hijoItems2;
    }

    public void setHijoItems2(List<NacidoVivoRenavi> hijoItems2) {
        this.hijoItems2 = hijoItems2;
    }

    public List<NacidoVivoRenavi> getListnacidovivo() {
        return listnacidovivo;
    }

    public void setListnacidovivo(List<NacidoVivoRenavi> listnacidovivo) {
        this.listnacidovivo = listnacidovivo;
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
        //return "/pages/contenedor/EditCont";
        String cedulMad = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedulMad");
        if (!cedulMad.equals("null")) {
            return "/pages/contenedor/EditCont";
        } else {
            return "/pages/contenedor/EditCont_NI";
        }
    }

    @PostConstruct
    public void iniciar() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");//JJHF CAMBIO LOG IN
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            ParametroConsulta paramAgencia = new ParametroConsulta("fkAgenciaSaureg", Long.valueOf(userLgn.getAgenciaInUser().getCodMsp()));
            parametros.add(paramAgencia);
            ParametroConsulta paramUsuario = new ParametroConsulta("fkUsuSaureg", userLgn.getNomUsuario());
            parametros.add(paramUsuario);
            ParametroConsulta paramEstadoNV = new ParametroConsulta("statusNV", JsfUtil.ESTADO_REG_ACTIVO);
            parametros.add(paramEstadoNV);
            ParametroConsulta paramEstadoM = new ParametroConsulta("statusM", JsfUtil.ESTADO_REG_ACTIVO);
            parametros.add(paramEstadoM);
            ParametroConsulta paramEstadoFirma = new ParametroConsulta("estFirma", JsfUtil.STAT_FIR_SIN);
            parametros.add(paramEstadoFirma);
            //Parametro para estado de registro = 5 (DIGERCIC)
            List<String> ids = new ArrayList<String>();
            ids.add("5");
            ids.add("6");
            ids.add("7");
            ids.add("8");
            ids.add("9");
            ids.add("10");
            ids.add("11");
            ParametroConsulta paramEstadoRegistro = new ParametroConsulta("estRegistro", ids);
            parametros.add(paramEstadoRegistro);
            hijoItems = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findAllByInstitucion", parametros);
            if (hijoItems.isEmpty()) {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoResultException"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
           userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");//JJHF CAMBIO LOGIN
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            ParametroConsulta paramAgencia = new ParametroConsulta("fkAgenciaSaureg", Long.valueOf(userLgn.getAgenciaInUser().getCodMsp()));
            parametros.add(paramAgencia);
            ParametroConsulta paramUsuario = new ParametroConsulta("fkUsuSaureg", userLgn.getNomUsuario());
            parametros.add(paramUsuario);
            ParametroConsulta paramEstadoNV = new ParametroConsulta("statusNV", JsfUtil.ESTADO_REG_ACTIVO);
            parametros.add(paramEstadoNV);
            ParametroConsulta paramEstadoM = new ParametroConsulta("statusM", JsfUtil.ESTADO_REG_ACTIVO);
            parametros.add(paramEstadoM);
            ParametroConsulta paramEstadoFirma = new ParametroConsulta("estFirma", JsfUtil.STAT_FIR_SIN);
            parametros.add(paramEstadoFirma);
            //Parametro para estado de registro = 5 (DIGERCIC)
            List<String> ids = new ArrayList<String>();
            ids.add("5");
            //  ids.add("6");
            ids.add("1");
            ids.add("2");
            ids.add("3");
            ids.add("4");
            ids.add("9");
            ids.add("10");
            ids.add("11");
            ParametroConsulta paramEstadoRegistro = new ParametroConsulta("estRegistro", ids);
            parametros.add(paramEstadoRegistro);
            hijoItems2 = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findAllByInstitucion", parametros);
            if (hijoItems2.isEmpty()) {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoResultException"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminar() {
        String cedula = null;
        Long codigoMad = null;
//        for (NacidoVivoRenavi item : listnacidovivo) {
            cedula = listnacidovivo.get(0).getFkCedulMad().getCedulMad();
            codigoMad = listnacidovivo.get(0).getFkCedulMad().getIdMad();
            
            if (cedula != null) {
                try {
                    StringBuilder updateCed = new StringBuilder("UPDATE REVIT.MADRE_RENAVI");
                    updateCed.append(" SET CEDUL_MAD = '");
                    updateCed.append(cedula.substring(0, 8));
                    updateCed.append("AB'");
                    updateCed.append(", STATUS = 'I'");
                    updateCed.append(" WHERE CEDUL_MAD = '");
                    updateCed.append(cedula);
                    updateCed.append("' AND ID_MAD = ");
                    updateCed.append(codigoMad);
                    

                    e = ejbFacade.executeNativeQuery(updateCed.toString());
                    if (e > 0) {
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
                            log.setAccion("REVIT. ADMINISTRACION. ELIMINACION MADRE RENAVI");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
                            CapturaIP ip = new CapturaIP();
                            log.setIp(ip.obieneDireccionIP());
                            ejbLogs.create(log);
                            //logs en la tabla logs_eliminacion
                            LogsEliminacion logEli = new LogsEliminacion();
                            logEli.setFechaEli(new Date());
                            logEli.setUsuarioEli(userLgn.getNomUsuario());
                            logEli.setAgenIdEli(userLgn.getAgenciaInUser().getCodMsp());
                            logEli.setNomUsuEli(userLgn.getNombre());
                            logEli.setApeUsuEli(userLgn.getApellido());
                            logEli.setInstIdEli(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());
                            CapturaIP ip2 = new CapturaIP();
                            logEli.setIpEli(ip2.obieneDireccionIP());
                            ejbLogsEli.create(logEli);
                            //cambiar este query multiparto sin considerar                  
                            StringBuilder valor = new StringBuilder();
                            valor.append("update logs_eliminacion set id_mad_eli = (select id_mad from madre_renavi where id_mad = ");
                            valor.append(codigoMad);
                            valor.append("), cedul_mad_eli = '");
                            valor.append(cedula);
                            valor.append("', nombr_mad_eli = (select nombr_mad from madre_renavi where id_mad = ");
                            valor.append(codigoMad);
                            valor.append("), pasap_mad_eli = (select pasap_mad from madre_renavi where id_mad = ");
                            valor.append(codigoMad);
                            valor.append("), estado_registro_eli = (select status from madre_renavi where id_mad = ");
                            valor.append(codigoMad);
                            valor.append(") where id_log_eli = ");
                            valor.append(logEli.getIdEli());

                            System.out.println(" QUERY UPDATE:   " + valor.toString());
                            int i = ejbFacade.executeNativeQuery(valor.toString());
                            for (NacidoVivoRenavi item : listnacidovivo) { 
                                //update para el estado del recien nacido vivo de la madre eliminada a estado 9
                                StringBuilder valup = new StringBuilder();
                                valup.append("update nacido_vivo_renavi set fk_id_est_reg = 9 where fk_cedul_mad = (select id_mad from madre_renavi where cedul_mad = '");
                                valup.append(item.getFkCedulMad().getCedulMad().substring(0, 8));
                                valup.append("AB'");
                                valup.append(" and id_mad = ");
                                valup.append(codigoMad);
                                valup.append(")");
                                int u = ejbFacade.executeNativeQuery(valup.toString());
                            }
                                JsfUtil.displayMessage("Registro del Nacido Vivo eliminado.", JsfUtil.INFO_MESSAGE);
                                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                        } catch (EntidadException o) {
                            o.printStackTrace();
                            JsfUtil.displayMessage(o.getMessage(), JsfUtil.ERROR_MESSAGE);
                            RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                        }
                    }
                } catch (Exception t) {
                    t.printStackTrace();
                    JsfUtil.displayMessage(t.getMessage(), JsfUtil.ERROR_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                }
            }
            else{
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
                    log.setAccion("REVIT. ADMINISTRACION. ELIMINACION MADRE RENAVI");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
                    CapturaIP ip = new CapturaIP();
                    log.setIp(ip.obieneDireccionIP());
                    ejbLogs.create(log);
                    //logs en la tabla logs_eliminacion
                    LogsEliminacion logEli = new LogsEliminacion();
                    logEli.setFechaEli(new Date());
                    logEli.setUsuarioEli(userLgn.getNomUsuario());
                    logEli.setAgenIdEli(userLgn.getAgenciaInUser().getCodMsp());
                    logEli.setNomUsuEli(userLgn.getNombre());
                    logEli.setApeUsuEli(userLgn.getApellido());
                    logEli.setInstIdEli(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());
                    CapturaIP ip2 = new CapturaIP();
                    logEli.setIpEli(ip2.obieneDireccionIP());
                    ejbLogsEli.create(logEli);
                    //cambiar este query multiparto sin considerar                  
                    StringBuilder valor = new StringBuilder();
                    valor.append("update logs_eliminacion set id_mad_eli = (select id_mad from madre_renavi where id_mad = ");
                    valor.append(codigoMad);
                    valor.append("), cedul_mad_eli = ");
                    valor.append(codigoMad);
                    valor.append(", nombr_mad_eli = (select nombr_mad from madre_renavi where id_mad = ");
                    valor.append(codigoMad);
                    valor.append("), pasap_mad_eli = (select pasap_mad from madre_renavi where id_mad = ");
                    valor.append(codigoMad);
                    valor.append("), estado_registro_eli = (select status from madre_renavi where id_mad = ");
                    valor.append(codigoMad);
                    valor.append(") where id_log_eli = ");
                    valor.append(logEli.getIdEli());
                    System.out.println(" QUERY UPDATE:   " + valor.toString());
                    int i = ejbFacade.executeNativeQuery(valor.toString());
                    for (NacidoVivoRenavi item : listnacidovivo) {
                        StringBuilder valup = new StringBuilder();
                        valup.append("update nacido_vivo_renavi set fk_id_est_reg = 9 where fk_cedul_mad = ");
                        valup.append(item.getFkCedulMad().getIdMad());

                        int u = ejbFacade.executeNativeQuery(valup.toString());
                    }
                        JsfUtil.displayMessage("Registro del Nacido Vivo eliminado.", JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                    JsfUtil.displayMessage("Error, no se eliminó el registro, revise que el código de la madre, sea correcto.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                }
            }
            this.iniciar();
        listnacidovivo.clear();
    }

}
