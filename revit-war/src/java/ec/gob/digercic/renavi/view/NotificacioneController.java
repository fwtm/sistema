package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "notificacioneController")
@ViewScoped
public class NotificacioneController implements Serializable {

    private String notificaSuper;
    private String notificaMed;
    /**/
    private String totalSuper = null;
    private String totalMed = null;
    private String totalReasig = null;
    private List<NacidoVivoRenavi> ediciones;
    /**/
    private List<ec.gob.digercic.renavi.ws.TblSauregCompRol> rolUser = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregCompRol>();

    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn;//JJHF CAMBIO LOG IN
    @EJB
    private ec.gob.digercic.renavi.ejb.AnulacionRenaviFacadeLocal ejbFacadeAnulacion;
    @EJB
    private ec.gob.digercic.renavi.ejb.LogsReasignacionFacadeLocal ejbFacadeReasigna;
    /**/
    @EJB
    private ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal ejbFacadeNacidoVivoRenavi;

    public NotificacioneController() {
    }

    @PostConstruct
    public void init() throws EntidadException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");//JJHF CAMBIO LOG IN
        Boolean flagSuper = false;
        Boolean flagMed = false;
        try {
            rolUser = this.getCompRolUsuario(userLgn.getNomUsuario(), userLgn.getAgenciaInUser().getIdAgencia().toString(), "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rolUser.size() > 0) {
            for (int i = 0; i < rolUser.size(); i++) {
                if (rolUser.get(i).getTblSauregRol().getIdRol() == JsfUtil.ROL_SUPERVISOR) {
                    flagSuper = true;
                }
                if (rolUser.get(i).getTblSauregRol().getIdRol() == JsfUtil.ROL_MEDICO
                        || rolUser.get(i).getTblSauregRol().getIdRol() == JsfUtil.ROL_DIGITADOR) {
                    flagMed = true;
                }
            }
        }
        List<Object[]> itemsObjectDatos = new ArrayList<Object[]>();
        List<Object[]> itemsObjReasig = new ArrayList<Object[]>();
        try {
            if (flagSuper) {
                StringBuilder queryAnulacion = new StringBuilder();
                queryAnulacion.append("select count(1) from anulacion a");
                queryAnulacion.append(" where a.id_agencia_anul = ");
                queryAnulacion.append(userLgn.getAgenciaInUser().getCodMsp());
                queryAnulacion.append(" and a.est_anul_nac_viv = '1'");
                itemsObjectDatos = ejbFacadeAnulacion.executeNativeQueryListResult(queryAnulacion.toString());
                for (Object datos : itemsObjectDatos) {
                    totalSuper = datos.toString();
                }
                if (!totalSuper.equals("0")) {
                    notificaSuper = totalSuper + " POR APROBAR";
                } else {
                    notificaSuper = null;
                }
            }
            if (flagMed) {
                StringBuilder queryreasi = new StringBuilder();
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
                List<String> ids = new ArrayList<String>();
                ids.add("5");
                ids.add("1");
                ids.add("2");
                ids.add("3");
                ids.add("4");
                ids.add("9");
                ids.add("10");
                ids.add("11");
                ParametroConsulta paramEstadoRegistro = new ParametroConsulta("estRegistro", ids);
                parametros.add(paramEstadoRegistro);
                //////////////////////////////////////////////////
                queryreasi.append("select (select count(distinct(id_nac_viv)) from Logs_Reasignacion where usuario_reg_asig ='");
                queryreasi.append(userLgn.getNomUsuario());
                queryreasi.append("' and to_char(fecha_asig_reg,'dd/mm/yyyy') = to_char(sysdate,'dd/mm/yyyy') and fk_agencia_saureg = ");
                queryreasi.append(userLgn.getAgenciaInUser().getCodMsp());
                queryreasi.append(") - (select  count(distinct(id_nac_viv)) as total from Logs_Reasignacion where usuario_reg_ant ='");
                queryreasi.append(userLgn.getNomUsuario());
                queryreasi.append("' and to_char(fecha_asig_reg,'dd/mm/yyyy') = to_char(sysdate,'dd/mm/yyyy') and fk_agencia_saureg = ");
                queryreasi.append(userLgn.getAgenciaInUser().getCodMsp());
                queryreasi.append(") from dual");
                ////////////////////////////////////////////////////
                ediciones = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findAllByInstitucion", parametros);
                itemsObjReasig = ejbFacadeReasigna.executeNativeQueryListResult(queryreasi.toString());

                for (Object item : itemsObjReasig) {
                    totalReasig = item.toString();
                }
                totalMed = String.valueOf(ediciones.size());

                if (!totalReasig.equals("0")) {
                    notificaMed = totalReasig + " REASIGNADOS";
                } else {
                    notificaMed = null;
                }
                if (!totalMed.equals("0")) {
                    notificaMed = totalMed + " POR EDITAR";
                    totalMed = null;
                } else {
                    notificaMed = null;
                }
                //Rechazadas
                StringBuilder queryRec = new StringBuilder();
                queryRec.append("select count(1) from anulacion a");
                queryRec.append(" where a.id_agencia_anul = ");
                queryRec.append(userLgn.getAgenciaInUser().getCodMsp());
                queryRec.append(" and est_anul_nac_viv = '3'");
                queryRec.append(" and observacion_rechazo_leido = '0'");
                List<Object[]> itemsObjectDatosRec = new ArrayList<Object[]>();
                itemsObjectDatosRec = ejbFacadeAnulacion.executeNativeQueryListResult(queryRec.toString());
                for (Object datos : itemsObjectDatosRec) {
                    totalMed = datos.toString();
                }
                if (!totalMed.equals("0")) {
                    if (notificaSuper != null) {
                        notificaSuper = notificaSuper + ", " + totalMed + " RECHAZADA(S) ";
                    } else {
                        notificaSuper = totalMed + " RECHAZADA(S) ";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNotificaSuper() {
        return notificaSuper;
    }

    public void setNotificaSuper(String notificaSuper) {
        this.notificaSuper = notificaSuper;
    }

    public String getNotificaMed() {
        return notificaMed;
    }

    public void setNotificaMed(String notificaMed) {
        this.notificaMed = notificaMed;
    }

    private java.util.List<ec.gob.digercic.renavi.ws.TblSauregCompRol> getCompRolUsuario(java.lang.String nomUsuario, java.lang.String idAgencia, java.lang.String idSistema) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        return port.getCompRolUsuario(nomUsuario, idAgencia, idSistema);
    }

}
