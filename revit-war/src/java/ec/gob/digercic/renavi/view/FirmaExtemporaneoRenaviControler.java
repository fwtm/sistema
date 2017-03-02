/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.CatalogoProfesionalRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.CatalogoProfesionalRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ws.cedula.Cedula;
import ec.gob.digercic.renavi.ws.qrbc.Barras;
import ec.gob.digercic.renavi.ws.qrbc.GeneratorCode;
import ec.gob.digercic.renavi.ws.qrbc.QR;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import ec.gob.digercic.saureg.ejb.TblSauregUsuarioFacadeLocal;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "firmaExtemporaneoRenaviController")
@ViewScoped
public class FirmaExtemporaneoRenaviControler implements Serializable {

    private List<NacidoVivoRenavi> listNacidoVivoPorFirmar;
    private Date fechaActual;
    private Date fechaPreFirma;
    private Date fechaInicio;
    private Date fechaFin;
    @EJB
    private NacidoVivoRenaviFacadeLocal ejbNacidoVivoRenaviFacade;
    @EJB
    private TblSauregUsuarioFacadeLocal ejbUsuarioFacade;
    @EJB
    private ec.gob.digercic.renavi.ejb.VariableRenaviFacadeLocal ejbFacadeVariable;
    @EJB    
    private LogAccesosRenaviFacadeLocal ejbLogs;
    /**/
    @EJB
    private CatalogoProfesionalRenaviFacadeLocal ejbFacadeCatalogoProfe;
    /**/
    private List<NacidoVivoRenavi> listSelectNacidoVivo;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn;//JJHF CAMBIO LOGIN
    private JasperPrint jasperPrint;
    private Short registrosVencidos;
    private Short registrosPorVencer;
    List<NacidoVivoRenavi> listNacidosVivos;
    byte[] pdf1;
    ///Cambio para validacion de madre indocumentada
//    private String variableIdNac = null;
    /**/
    private List<CatalogoProfesionalRenavi> lstProfesion = new ArrayList<CatalogoProfesionalRenavi>();
    private List<ec.gob.digercic.renavi.ws.TblSauregRol> listaRol = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregRol>();

    /**/
    private Boolean flagRol = false;
    private String asistencia;

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public FirmaExtemporaneoRenaviControler() {
    }

    public void actualizaTablaPorFirmar(ActionEvent event) {
        buscarNacidoVivosNoFirmados();
        RequestContext.getCurrentInstance().update("form:checkboxDT");
        RequestContext.getCurrentInstance().update("form:porVencer");
    }

    @PostConstruct
    public void iniciar() throws ParseException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");//JJHF CAMBIO LOGIN
            //Obtengo la fecha actual del sistema
            fechaActual = new Date();
            fechaInicio = fechaActual;
            //Obtengo el tiempo preFirma
            //Long tp = new Long(ejbFacadeVariable.find(3).getVarValor());
            //fechaPreFirma = new Date(fechaActual.getTime() - tp);

            fechaFin = new Date(fechaInicio.getTime() - 31540000000L);

             //Obtengo el tiempo preFirma
            Long tp = new Long(ejbFacadeVariable.find(3).getVarValor());
            fechaInicio = new Date(fechaActual.getTime() - tp);
            //restamos un dia mas 
            fechaInicio = new Date(fechaInicio.getTime() - 86400000);             
            
            fechaPreFirma = new Date(fechaFin.getTime() - (2 * 31540000000L)); //3 años DFJ
            //fechaPreFirma = fechaFin; //DFJ
            //Busco los nacidos vivos no firmados
            buscarNacidoVivosNoFirmados();
            //Cuento los registros vencidos a la fecha de hoy
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //formateo la fecha prefirma para que sea a las 23:59:59
            //  fechaPreFirma = new Date(sdf.parse(sdf.format(fechaPreFirma)).getTime() + 86399000L);

            StringBuilder sqlVencidos = new StringBuilder("select count(1), n.fk_usu_saureg from nacido_vivo_renavi n, madre_renavi m ");
            sqlVencidos.append("where n.fk_cedul_mad = m.id_mad ");
            sqlVencidos.append("and to_date(to_char(n.fecha_creacion_nac_viv, 'dd/MM/yyyy'),'dd/MM/yyyy') < to_date('");
            sqlVencidos.append(sdf.format(fechaPreFirma));
            sqlVencidos.append("', 'dd/MM/yyyy') and n.status = 'A' ");
            sqlVencidos.append("and m.status = 'A' ");
            sqlVencidos.append("and n.fk_usu_firma_saureg = '");
            sqlVencidos.append(userLgn.getNomUsuario());
            sqlVencidos.append("' and n.fk_agencia_firma_saureg = ");
            sqlVencidos.append(userLgn.getAgenciaInUser().getCodMsp());
            sqlVencidos.append(" and n.fk_id_est_reg =");
            sqlVencidos.append(JsfUtil.STAT_DAT_DOCTOR);
            sqlVencidos.append(" and n.fk_id_est_fir =");
            sqlVencidos.append(JsfUtil.STAT_FIR_SIN);
            sqlVencidos.append(" and n.pdf_sin_firma_nac_viv is not null ");
            sqlVencidos.append("group by n.fk_usu_saureg");
            List<Object[]> itemsObjectFDatos = new ArrayList<Object[]>();
            /**/
            lstProfesion = ejbFacadeCatalogoProfe.findAll();
            listaRol = this.rolesUsuarios(userLgn.getNomUsuario(), String.valueOf(userLgn.getAgenciaInUser().getIdAgencia()));
            /**/
            for (int i = 0; i < lstProfesion.size(); i++) {
                for (int j = 0; j < listaRol.size(); j++) {
                    if (listaRol.get(j).getDescripcion().toString().equals(lstProfesion.get(i).getNombreCatProf().toString())) {
                        flagRol = true;
                        if (lstProfesion.get(i).getNombreCatProf().toString().equals("DIGITADOR")) {
                            setAsistencia("MEDICO");
                        } else {
                            setAsistencia(lstProfesion.get(i).getNombreCatProf().toString());
                        };
                        break;
                    }
                    if (flagRol) {
                        break;
                    }
                }
            }
            /**/
            try {
                itemsObjectFDatos = ejbNacidoVivoRenaviFacade.executeNativeQueryListResult(sqlVencidos.toString());
                if (!itemsObjectFDatos.isEmpty()) {
                    for (Object[] objTemFDatos : itemsObjectFDatos) {
                        registrosVencidos = ((BigDecimal) objTemFDatos[0]).shortValue();
                    }
                } else {
                    registrosVencidos = 0;
                }
            } catch (Exception e) {
                registrosVencidos = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //userLgn = null;
            fechaPreFirma = new Date();
            fechaActual = new Date();
        }

    }

    public void buscarNacidoVivosNoFirmados() {
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
            log.setAccion("REVIT. FIRMA ELECTRONICA. BUSCAR REGISTRO");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        try {
            if (userLgn.getAgenciaInUser().getCodMsp() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fi = sdf.format(fechaInicio);
                fechaInicio = sdf.parse(fi);

                //DFJ               fechaFin = new Date(fechaInicio.getTime() - 31540000000L);
                String ff = sdf.format(fechaFin);
                fechaFin = sdf.parse(ff);
                StringBuilder query = new StringBuilder("select na.id_nac_viv, lr.id_nac_viv, na.cedul_nac_viv, na.fk_cedul_mad, na.nombr_nac_viv, na.apell_nac_viv,");
                query.append(" na.fecha_nacim_nac_viv, na.fecha_creacion_nac_viv, lr.fk_agencia_saureg, lr.usuario_reg_asig, lr.fecha_asig_reg");
                query.append(" from nacido_vivo_renavi na");
                query.append(" left join logs_reasignacion lr on na.id_nac_viv = lr.id_nac_viv");
                query.append(" where na.fk_id_est_fir = 1");
                query.append(" and to_date(to_char(na.fecha_creacion_nac_viv,'dd/mm/yyyy'), 'dd/mm/yyyy')");
                query.append(" between to_date('");
                query.append(ff);
                query.append("','dd/mm/yyyy') and to_date('");
                query.append(fi);
                query.append("','dd/mm/yyyy') and na.fk_agencia_saureg = ");
                query.append(userLgn.getAgenciaInUser().getCodMsp());
                query.append(" and na.status='A' and na.fk_usu_saureg = '");
                query.append(userLgn.getNomUsuario());
                query.append("' and na.fk_id_est_reg in (4, 8)");
                query.append(" order by lr.fecha_asig_reg DESC");
                System.out.println("FIRMA EXTRATEMPORAL" + query.toString());
                listNacidoVivoPorFirmar = ejbNacidoVivoRenaviFacade.executeNativeQueryListResultGenerico(query.toString(), NacidoVivoRenavi.class);
                /*Metodo para eliminar repetidos   FWTM  06-07-2016 */
                Set<NacidoVivoRenavi> lstUnica = new LinkedHashSet<NacidoVivoRenavi>(listNacidoVivoPorFirmar);
                listNacidoVivoPorFirmar.clear();
                listNacidoVivoPorFirmar.addAll(lstUnica);
                /**/
                if (listNacidoVivoPorFirmar.isEmpty()) {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("messages");
                    registrosPorVencer = 0;
                }
                StringBuilder sqlPorVencer = new StringBuilder("select count(1), n.fk_usu_saureg from nacido_vivo_renavi n, madre_renavi m ");
                sqlPorVencer.append("where n.fk_cedul_mad = m.id_mad ");
                sqlPorVencer.append("and to_date(to_char(n.fecha_creacion_nac_viv, 'dd/MM/yyyy'),'dd/MM/yyyy') = to_date('");
                sqlPorVencer.append(sdf.format(fechaFin)); //DFJ
                sqlPorVencer.append("','dd/MM/yyyy') ");
                sqlPorVencer.append("and n.status = 'A' ");
                sqlPorVencer.append("and m.status = 'A' ");
                sqlPorVencer.append("and n.fk_usu_firma_saureg = '");
                sqlPorVencer.append(userLgn.getNomUsuario());
                sqlPorVencer.append("' and n.fk_agencia_firma_saureg = ");
                sqlPorVencer.append(userLgn.getAgenciaInUser().getCodMsp());
                sqlPorVencer.append(" and n.fk_id_est_reg =");
                sqlPorVencer.append(JsfUtil.STAT_DAT_DOCTOR);
                sqlPorVencer.append(" and n.fk_id_est_fir =");
                sqlPorVencer.append(JsfUtil.STAT_FIR_SIN);
                sqlPorVencer.append(" and n.pdf_sin_firma_nac_viv is not null ");
                sqlPorVencer.append("group by n.fk_usu_saureg");
                System.out.println("sql pro vencer " + sqlPorVencer.toString());
                List<Object[]> itemsObjectFDatos = new ArrayList<Object[]>();
                try {
                    itemsObjectFDatos = ejbNacidoVivoRenaviFacade.executeNativeQueryListResult(sqlPorVencer.toString());
                    if (!itemsObjectFDatos.isEmpty()) {
                        for (Object[] objTemFDatos : itemsObjectFDatos) {
                            registrosPorVencer = ((BigDecimal) objTemFDatos[0]).shortValue();
                        }
                    } else {
                        registrosPorVencer = 0;
                    }
                } catch (Exception e) {
                    registrosPorVencer = 0;
                }
            } else {
                JsfUtil.displayMessage("El usuario no está trabajando en una agencia del M.S.P.", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("messages");
            }

        } catch (Exception ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("EntidadException"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("messages");
        }
    }

    /**
     * @return the listNacidoVivoPorFirmar
     */
    public List<NacidoVivoRenavi> getListNacidoVivoPorFirmar() {
        return listNacidoVivoPorFirmar;
    }

    /**
     * @param listNacidoVivoPorFirmar the listNacidoVivoPorFirmar to set
     */
    public void setListNacidoVivoPorFirmar(List<NacidoVivoRenavi> listNacidoVivoPorFirmar) {
        this.listNacidoVivoPorFirmar = listNacidoVivoPorFirmar;
    }

    public List<NacidoVivoRenavi> getListSelectNacidoVivo() {
        return listSelectNacidoVivo;
    }

    public void setListSelectNacidoVivo(List<NacidoVivoRenavi> listSelectNacidoVivo) {
        this.listSelectNacidoVivo = listSelectNacidoVivo;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public Date getFechaPreFirma() {
        return fechaPreFirma;
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

    public Short getRegistrosVencidos() {
        return registrosVencidos;
    }

    public Short getRegistrosPorVencer() {
        return registrosPorVencer;
    }

    public void enviarApplet() throws EntidadException { //Recupero los id's de los registros a los que voy a crear el número de cédula y PDF.
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. ENVIAR A FIRMAR");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        StringBuilder temp = new StringBuilder();
        StringBuilder fkIdMad = new StringBuilder();
        StringBuilder queryFir = new StringBuilder();
        for (int i = 0; i < listSelectNacidoVivo.size(); i++) {
                fkIdMad.append(listSelectNacidoVivo.get(i).getFkCedulMad().getIdMad());
                if (i < listSelectNacidoVivo.size() - 1) {
                    fkIdMad.append(",");
                }
            }
        queryFir.append("select * from revit.nacido_vivo_renavi b");
        queryFir.append(" where b.fk_cedul_mad in (");
        queryFir.append(fkIdMad);
        queryFir.append(")");
        queryFir.append(" and b.fk_id_est_fir = 1");
        queryFir.append(" and b.fk_id_est_reg in (4, 8)");
        queryFir.append(" order by b.fk_cedul_mad");
        listSelectNacidoVivo.clear();
        listSelectNacidoVivo = ejbNacidoVivoRenaviFacade.executeNativeQueryListResultGenerico(queryFir.toString(), NacidoVivoRenavi.class);
        /*Metodo para eliminar repetidos   FWTM  28-07-2016 */
        Set<NacidoVivoRenavi> lstUnica = new LinkedHashSet<NacidoVivoRenavi>(listSelectNacidoVivo);
        listSelectNacidoVivo.clear();
        listSelectNacidoVivo.addAll(lstUnica);
        /**/
            if (listSelectNacidoVivo.size() > 0) {
            for (int i = 0; i < listSelectNacidoVivo.size(); i++) {
                temp.append(listSelectNacidoVivo.get(i).getFkCedulMad().getIdMad().toString());
                temp.append("_");
                temp.append(listSelectNacidoVivo.get(i).getNumPartoSistema());
                if (i < listSelectNacidoVivo.size() - 1) {
                    temp.append(";");
                }
            }
            String[] tempArr = temp.toString().split(";");
            List<String> tempList = new ArrayList<String>();
            for (int j = 0; j < tempArr.length; j++) {
                tempList.add(tempArr[j]);
            }
            HashSet<String> hashSet = new HashSet<String>(tempList);
            tempList.clear();
            tempList.addAll(hashSet);
            listNacidosVivos = new ArrayList<NacidoVivoRenavi>();
            for (String recorre : tempList) {
                List<NacidoVivoRenavi> temNacviv = new ArrayList<NacidoVivoRenavi>();
                StringBuilder sql = new StringBuilder("select id_nac_viv, cedul_nac_viv, fk_cedul_mad, fk_id_pro_emb, nombr_nac_viv, apell_nac_viv, fecha_inscr_nac_viv,");
                sql.append(" talla_nac_viv, acta_inscr_nac_viv, peso_nac_viv, seman_gstcn_nac_viv, fecha_nacim_nac_viv, campo_inec_rc_pcp_nac_viv, campo_inec_rc_ofi_nac_viv,");
                sql.append(" pasap_nac_viv, fk_id_sexo_nv, numer_parto_nac_viv, cntrl_prntl_nac_viv, fk_id_tip_ins, fk_id_tip_par, ofici_rc_nac_viv, provi_rcid_nac_viv,");
                sql.append(" provi_rcdsc_nac_viv, cantn_rcid_nac_viv, cantn_rcdsc_nac_viv, parrq_rcid_nac_viv, paarq_rcdsc_nac_viv, obsrv_atprt_nac_viv, ofici_rcid_nac_viv,");
                sql.append(" campo_inec_fec_cri_nac_viv, fk_cedul_pad, anio_inscr_nac_viv, mes_inscr_nac_viv, dia_inscr_nac_viv, anio_nacim_nac_viv, mes_nacim_nac_viv,");
                sql.append(" dia_nacim_nac_viv, lug_nac_especifique, fk_id_est_fir, fk_id_est_reg, anio_fec_cri_nac_viv, mes_fec_cri_nac_viv, dia_fec_cri_nac_viv,");
                sql.append(" campo_inec_cod_cri_cod, tip_asi_especifique, edad_mad, foto_mad, condic_ced_mad, conyuge_mad, fec_fallec_mad, hijos_vivsa_mad, hijos_nvmrt_mad,");
                sql.append(" hijos_nmrts_mad, fk_id_est_civ_mad, fk_id_sabe_leer_mad, fk_id_nivel_instr, residn_provid_mad, residn_provds_mad, residn_cantid_mad, residn_cantds_mad,");
                sql.append(" residn_parrid_mad, residn_parrds_mad, residn_local_mad, residn_direc_mad, fk_id_pais_mad, fk_id_nac_mad, campo_inec_cod_pais_mad, campo_inec_area_mad,");
                sql.append(" fk_id_tipo_area_mad, fecha_creacion_nac_viv, obsrv_estado_mad, status, numero_producto_nac_viv, sexo_mad, pdf_sin_firma_nac_viv, pdf_con_firma_nac_viv,");
                sql.append(" fk_id_ide_etn_mad, num_parto_sistema, hijos_nacierviv_sistema, hijos_naciermuert_sistema, fk_usu_saureg, fk_agencia_saureg, fk_usu_firma_saureg,");
                sql.append(" fk_agencia_firma_saureg, fecha_firma_nac_viv, numero_embarazos_mad, apgar1_nac_viv, apgar2_nac_viv, nombr_insc_nac_viv, apell_insc_nac_viv, fecha_actualizacion_nac_viv,");
                sql.append(" numero_historia_nac_viv, obsrv_nac_viv, fk_id_pais_residn_mad, codigo_rc_nac_viv,MALFOMACIONES_NAC_VIV,TIPO_SANGRE_NAC_VIV");
                sql.append(" from nacido_vivo_renavi n where n.fk_cedul_mad = ");
                sql.append(recorre.subSequence(0, recorre.lastIndexOf("_")));
                sql.append(" and n.num_parto_sistema = ");
                sql.append(recorre.subSequence(recorre.lastIndexOf("_") + 1, recorre.length()));
                temNacviv = ejbNacidoVivoRenaviFacade.executeNativeQueryListResultGenerico(sql.toString(), NacidoVivoRenavi.class);
                listNacidosVivos.addAll(temNacviv);
            }
            //Inicializo la variable que contiene los id a ser enviados al applet
            temp = null;
            temp = new StringBuilder();
            for (int k = 0; k < listNacidosVivos.size(); k++) {
                temp.append(listNacidosVivos.get(k).getIdNacViv());
                if (k < listNacidosVivos.size() - 1) {
                    temp.append(",");
                }
            }
            int contarCedula = 0;
            Date fechaFirma = new Date();
            StringBuilder errorSendAplet = new StringBuilder();
            for (NacidoVivoRenavi item : listNacidosVivos) {
                //Verificación si la madre es documentada o indoc.
                if (item.getFkCedulMad().getFkIdTipoIdent().getIdTipoIdent() == 1) {//Identificada
                    /* 2) Asignación del número de la cédula */
                    if (item.getCedulNacViv() == null) {
                        //Obtengo los datos del usuario Logeado para la generación del número de cédula
                        Cedula cedula = new Cedula();
                        try {
                            cedula = this.numeroCedula(userLgn.getAgenciaInUser().getIdProvincia().getCodDpa());
                            if (cedula.getError().equals("000")) {
                                item.setCedulNacViv(cedula.getCedula());
                                cedula = null;
                                contarCedula++;
                            } else {
                                cedula = null;
                                errorSendAplet.append("No se genera el número de identificación para el registro: ");
                                errorSendAplet.append(item.getIdNacViv());
                                errorSendAplet.append(". ");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            cedula = null;
                            errorSendAplet.append("Error de conexión con el WS de número de identificación para para el registro: ");
                            errorSendAplet.append(item.getIdNacViv());
                            errorSendAplet.append(". ");
                        }
                    } else {
                        contarCedula++;
                    }
                    /* Fin Generación asignación del número de cédula*/
                } else {
                    //Asignación de codigo a madres no identificadas
                    String codProv = userLgn.getAgenciaInUser().getIdProvincia().getCodExterno1();
                    item.setCodigoRcNacViv(codProv.concat(userLgn.getAgenciaInUser().getCodMsp()).concat(item.getIdNacViv().toString()));
                    contarCedula++;
                }
                //Logica para editar el registro que ya se encuentra con cedula o 
                //con numerod ehistoria clínica
                try {
                    item.setFechaFirmaNacViv(fechaFirma);
                    //ejbNacidoVivoRenaviFacade.edit(item);
                    FacesContext contex = FacesContext.getCurrentInstance();
                    String ruta = "REGISTRO CIVIL DEL ECUADOR. Verifica Formulario de Registro de Nacido Vivo(http://servicios1.registrocivil.gob.ec" + contex.getExternalContext().getRequestContextPath() + "/consultaCiudadana.jsf?nccftndjh=" + item.getCedulNacViv() + ")";
                    QR qr = generarQR(ruta);
                    if (item.getFkCedulMad().getFkIdTipoIdent().getIdTipoIdent() == 1) {
                        Barras bc = generarBarras(item.getCedulNacViv());
                        if (bc.getCodigoError().equals("000")
                                && qr.getCodigoError().equals("000")) {
                            item.setPdfSinFirmaNacViv(pdfSinFirmaConCedula(item, userLgn, bc.getImagenBarra(), qr.getImagenQR()));
                        } else {
                            errorSendAplet.append("Error de generación de códigos de identificación del documento para el registro: ");
                            errorSendAplet.append(item.getIdNacViv());
                            errorSendAplet.append(". ");
                            contarCedula = -1;
                        }
                    } else {
                        Barras bc = generarBarras(item.getCodigoRcNacViv());
                        if (bc.getCodigoError().equals("000")
                                && qr.getCodigoError().equals("000")) {
                            item.setPdfSinFirmaNacViv(pdfSinFirmaConCedula(item, userLgn, bc.getImagenBarra(), qr.getImagenQR()));
                        } else {
                            errorSendAplet.append("Error de generación de códigos de identificación del documento para el registro: ");
                            errorSendAplet.append(item.getIdNacViv());
                            errorSendAplet.append(". ");
                            contarCedula = -1;
                        }
                    }
                    ejbNacidoVivoRenaviFacade.edit(item);
                } catch (Exception eef) {
                    contarCedula = -1;
                    errorSendAplet.append("Error de conexión con el WS para generación de codigos de identificación del documento para el registro: ");
                    errorSendAplet.append(item.getIdNacViv());
                    errorSendAplet.append(". ");
                }
            }
            /* Fin recuperación Nacidos Vivos*/
            if (contarCedula == listNacidosVivos.size()) {
                RequestContext.getCurrentInstance().execute("PF('firmaDialog').show();");
                RequestContext.getCurrentInstance().execute("AppletFirmaDigital.setIds('" + temp.toString() + "');");
                RequestContext.getCurrentInstance().execute("AppletFirmaDigital.setProceso('nacimiento');");
                RequestContext.getCurrentInstance().execute("AppletFirmaDigital.setCedula('" + userLgn.getNomUsuario() + "');");
                RequestContext.getCurrentInstance().execute("AppletFirmaDigital.setManeraParaFirma('" + ejbUsuarioFacade.getManeraFirma(userLgn.getNomUsuario(), userLgn.getAgenciaInUser().getIdAgencia().toString()) + "');");
                String validarFirma = ejbUsuarioFacade.getManeraFirma(userLgn.getNomUsuario(), userLgn.getAgenciaInUser().getIdAgencia().toString());
                System.out.println("Manera de Firmar:" + validarFirma.toString());
            } else {
                JsfUtil.displayMessage(errorSendAplet.toString(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("messages");
            }
        } else {
            JsfUtil.displayMessage("No se ha seleccionado ningún registro para realizar el proceso de firma.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("messages");

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
            log.setAccion("REVIT. ANULACION DE REGISTRO. VER PDF SIN FIRMA");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        try {
            String idNac = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idNacViv");
            NacidoVivoRenavi objeto = ejbNacidoVivoRenaviFacade.find(Long.parseLong(idNac));
            byte[] pdfData = objeto.getPdfSinFirmaNacViv();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            // Empieza proceso de response Initialization.
            response.reset();
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "attachment;filename=Certificado-Temp_" + idNac + ".pdf");
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

    private Cedula numeroCedula(java.lang.String provincia) {
        ec.gob.digercic.renavi.ws.cedula.GeneraCedula_Service service_cedula = new ec.gob.digercic.renavi.ws.cedula.GeneraCedula_Service();
        ec.gob.digercic.renavi.ws.cedula.GeneraCedula port = service_cedula.getGeneraCedulaPort();
        return port.numeroCedula(provincia);
    }
    
    /*Cambio para identificar los roles de los usuarios  FWTM  25-02-2016*/

    private List<ec.gob.digercic.renavi.ws.TblSauregRol> rolesUsuarios(java.lang.String userI, String agencia) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        List<ec.gob.digercic.renavi.ws.TblSauregRol> lstUserRol;
        lstUserRol = port.getRolesByUsuarioByAgenciaBySistema(userI, agencia, "1");
        return lstUserRol;
    }
    public byte[] pdfSinFirmaConCedula(NacidoVivoRenavi nv, ec.gob.digercic.renavi.ws.TblSauregUsuario usuarioFirma, byte[] bc, byte[] qr) throws JRException, IOException, SQLException {//JJHF CAMBIO LOGIN
        try {
            List<NacidoVivoRenavi> items = new ArrayList<NacidoVivoRenavi>();
            items.add(nv);
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("fechaFirmaNacViv", nv.getFechaFirmaNacViv());
            parametros.put("institucion", userLgn.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("usuarioLogeado", userLgn.getNomUsuario().toString());
            parametros.put("lugar", userLgn.getAgenciaInUser().getIdInstituc().getNomInstituc().toString());
            parametros.put("nombre", userLgn.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("provincia", userLgn.getAgenciaInUser().getIdProvincia().getDerscripcion().toString());
            parametros.put("canton", userLgn.getAgenciaInUser().getIdCanton().getDerscripcion().toString());
            parametros.put("parroquia", userLgn.getAgenciaInUser().getIdParroquia().getDerscripcion().toString());
            parametros.put("direccion", userLgn.getAgenciaInUser().getDireccion().toString());
            parametros.put("asistidoPor", getAsistencia());
            parametros.put("telefono", userLgn.getAgenciaInUser().getTelefono().toString());
            parametros.put("nombreFirma", usuarioFirma.getNombre() + " " + usuarioFirma.getApellido());
            parametros.put("telefonoFirma", usuarioFirma.getAgenciaInUser().getTelefonoUsuario() == null ? "" : usuarioFirma.getAgenciaInUser().getTelefonoUsuario().toString());
            parametros.put("cedula", usuarioFirma.getNomUsuario().toString());
            parametros.put("ciud_loc", usuarioFirma.getAgenciaInUser().getLocalidad() == null ? "" : usuarioFirma.getAgenciaInUser().getLocalidad().toString());
            parametros.put("codMSP", userLgn.getAgenciaInUser().getCodMsp());
            parametros.put("codigoBarras", bc);
            parametros.put("codigoQR", qr);
            //Cambio Henry
            if (nv.getFkCedulMad().getCedulMad() != null) {
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users.jasper");
                JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(items);
                jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
                pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
                return pdf1;
            } else {
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users1.jasper");
                JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(items);
                jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
                pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
                return pdf1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] pdfSinFirmaConCedula_anterior(Long id, TblSauregUsuario usuarioFirma, Date fechaFirma, byte[] bc, byte[] qr) throws JRException, IOException, SQLException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            TblSauregUsuario userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");

            //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.24.16.38:1521:INTDB", "revit", "revit");
            Connection conn =JsfUtil.getConecction();//DFJ
            //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.91.16.45:1521:INTDB", "revit", "revit2015$");
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("id", id);
            parametros.put("institucion", userLgn.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("fechaFirma", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaFirma));
            parametros.put("usuarioLogeado", userLgn.getNomUsuario().toString());
            parametros.put("lugar", userLgn.getAgenciaInUser().getIdInstituc().getNomInstituc().toString());
            parametros.put("nombre", userLgn.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("provincia", userLgn.getAgenciaInUser().getIdProvincia().getDerscripcion().toString());
            parametros.put("canton", userLgn.getAgenciaInUser().getIdCanton().getDerscripcion().toString());
            parametros.put("parroquia", userLgn.getAgenciaInUser().getIdParroquia().getDerscripcion().toString());
            parametros.put("direccion", userLgn.getAgenciaInUser().getDireccion().toString());
            parametros.put("asistidoPor", usuarioFirma.getIdTipUsu().getDescripcion().toString());
            parametros.put("telefono", userLgn.getAgenciaInUser().getTelefono().toString());
            parametros.put("nombreFirma", usuarioFirma.getNombre() + " " + usuarioFirma.getApellido());
            parametros.put("telefonoFirma", usuarioFirma.getAgenciaInUser().getTelefonoUsuario() == null ? "" : usuarioFirma.getAgenciaInUser().getTelefonoUsuario().toString());
            parametros.put("cedula", usuarioFirma.getNomUsuario().toString());
            parametros.put("ciud_loc", usuarioFirma.getAgenciaInUser().getLocalidad() == null ? "" : usuarioFirma.getAgenciaInUser().getLocalidad().toString());
            parametros.put("codMSP", userLgn.getAgenciaInUser().getCodMsp());
            parametros.put("codigoBarras", bc);
            parametros.put("codigoQR", qr);
            //Cambio Henry
            for (NacidoVivoRenavi nacViv : listNacidosVivos) {
                if (nacViv.getFkCedulMad().getCedulMad() != null) {
                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, parametros, conn);
                    pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
                    return pdf1;
                } else {
                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users1.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, parametros, conn);
                    pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
                    return pdf1;

                }

            }//fin cambio

//            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users.jasper");
//            jasperPrint = JasperFillManager.fillReport(reportPath, parametros, conn);
//            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
//            return pdf;
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Barras generarBarras(java.lang.String msg) {
        String proxyHost = System.getProperty("http.proxyHost");
        String portProxy = System.getProperty("http.proxyPort");
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "");
        ec.gob.digercic.renavi.ws.qrbc.GeneratorCode service = new GeneratorCode();
        ec.gob.digercic.renavi.ws.qrbc.GeneratorCodeSoap port = service.getGeneratorCodeSoap();
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", portProxy);
        return port.generarBarras(msg);
    }

    private QR generarQR(java.lang.String msg) {
        String proxyHost = System.getProperty("http.proxyHost");
        String portProxy = System.getProperty("http.proxyPort");
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "");
        ec.gob.digercic.renavi.ws.qrbc.GeneratorCode service = new GeneratorCode();
        ec.gob.digercic.renavi.ws.qrbc.GeneratorCodeSoap port = service.getGeneratorCodeSoap();
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", portProxy);
        return port.generarQR(msg);
    }
}
