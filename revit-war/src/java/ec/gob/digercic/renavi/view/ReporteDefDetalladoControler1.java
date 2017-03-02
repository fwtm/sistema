/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.FallecidoFacadeLocal;
import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.Fallecido;
import ec.gob.digercic.renavi.entities.NacidoVivoReporte;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.entities.TblSauregAgencia;
import ec.gob.digercic.saureg.entities.TblSauregInstitucion;
import ec.gob.digercic.saureg.entities.TblSauregRol;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import ec.gob.digercic.saureg.ejb.TblSauregAgenciaFacadeLocal;
import ec.gob.digercic.saureg.ejb.TblSauregInstitucionFacadeLocal;
import ec.gob.digercic.saureg.ejb.TblSauregUsuarioFacadeLocal;
import ec.gob.digercic.saureg.entities.TblSauregSistInstitucion;
import java.io.IOException;
import javax.faces.event.ValueChangeEvent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "reporteDefDetalladoControler1")
@ViewScoped
public class ReporteDefDetalladoControler1 implements Serializable {

    private Date fechaNacIni; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaNacFin; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaActual; //Fecha actual del sistema
    private List<TblSauregInstitucion> instituciones;
    private String idInstitucion;
    private List<TblSauregAgencia> agencias;
    private String codMSP;
    private List<TblSauregUsuario> usuarios;
    private String userCedula;
    //Usuario en sesión
    private TblSauregUsuario userLgn;
    //Los roles para los reportes
    private Boolean rol_inst;
    private Boolean rol_rc;
    private String rolSesion;
    //Donde se retornaran los resultados
    private List<NacidoVivoReporte> resultado;
    private BigDecimal totalIncompletos;
    private BigDecimal totalNoFirmados;
    private BigDecimal totalFirmados;
    private BigDecimal totalInscritos;
    private BigDecimal totalAnulados;
    private BigDecimal total;
    JasperPrint jasperPrint;

    @EJB
    private FallecidoFacadeLocal ejbFallecidoFacade;
    @EJB
    private TblSauregInstitucionFacadeLocal ejbInstitucionFacade;
    @EJB
    private TblSauregAgenciaFacadeLocal ejbAgenciaFacade;
    @EJB
    private TblSauregUsuarioFacadeLocal ejbUsuarioFacade;

    public ReporteDefDetalladoControler1() {
    }

    @PostConstruct
    public void init() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaActual = new Date();
            fechaNacFin = fechaActual;
            fechaNacIni = sdf.parse(sdf.format(fechaActual));
            totalIncompletos = new BigDecimal("0");
            totalNoFirmados = new BigDecimal("0");
            totalFirmados = new BigDecimal("0");
            totalInscritos = new BigDecimal("0");
            totalAnulados = new BigDecimal("0");
            total = new BigDecimal("0");
            //Obtengo que rol posee el usuario (esto es en base a la jerarquia.)
            rol_inst = false;
            rol_rc = false;
            for (int i = 0; i < userLgn.getTblSauregCompRolList().size(); i++) {
                TblSauregRol rol = userLgn.getTblSauregCompRolList().get(i).getTblSauregRol();
                if (rol.getIdRol() == 21) {
                    rol_inst = true;
                }
                if (rol.getIdRol() == 20) {
                    rol_rc = true;
                }
            }
            //dependiendo del rol obtengo las instituciones
            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            if (rol_rc) {
                rolSesion = "RC"; //Rol REG. CIVIL
                StringBuilder qrc = new StringBuilder("select i.* from tbl_saureg_institucion i ");
                qrc.append("where i.id_instituc in ( ");
                qrc.append("select si.id_instituc from tbl_saureg_sist_institucion si ");
                qrc.append("where si.id_sistema = 1 and si.status = '");
                qrc.append(JsfUtil.ESTADO_REG_ACTIVO);
                qrc.append("')");
                instituciones = ejbInstitucionFacade.executeNativeQueryListResultGenerico(qrc.toString(), TblSauregInstitucion.class);
            } else if (rol_inst) {
                rolSesion = "RI"; //Rol REG. CIVIL
                instituciones = new ArrayList<TblSauregInstitucion>();
                for (TblSauregSistInstitucion item
                        : userLgn.getAgenciaInUser().getIdInstituc().getTblSauregSistInstitucionList()) {
                    if (userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().
                            equals(item.getIdInstituc().getIdInstituc())
                            && item.getIdSistema().getIdSistema().equals(1L)) {//1L cod sistema revit
                        instituciones.add(item.getIdInstituc());
                        break;
                    }
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void poblaAgencias(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            agencias.clear();
            usuarios.clear();
            String value = event.getNewValue().toString();
            agencias = ejbAgenciaFacade.getAgenciasPorInstitucion(value); //carga todas las agencias dado un id de Institucion
        } else {
            agencias = null;
        }
    }
    
    public void poblaUsuariosPorAgencias(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            usuarios.clear();
            String[] codNomAge = event.getNewValue().toString().split(";");
            try {
                usuarios = ejbUsuarioFacade.getUsuariosByAgenciaMSP(codNomAge[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void generaReporte() throws SQLException, JRException, IOException {
        resultado.clear();
        DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder query = new StringBuilder("select ");
        StringBuilder idagencias = new StringBuilder();
        if(codMSP != null){
            String[] codNomAge = codMSP.split(";");
            idagencias.append(codNomAge[0]);
        }else{
            for(int i = 0; i < agencias.size(); i++){
                if(agencias.get(i).getCodMsp() != null){
                   idagencias.append(agencias.get(i).getCodMsp()); 
                   if(i < agencias.size() - 2) {
                        idagencias.append(",");
                    }
                }  
            }
        }
        query.append("(select u.derscripcion from admusuarios.tbl_saureg_ubicacion u,admusuarios.tbl_saureg_agencia a2 where u.id_ubicacion = a2.id_provincia and a2.cod_msp = f.fk_agencia_saureg) as provincia, ");
        query.append("(select i.nom_corto from admusuarios.tbl_saureg_institucion i,admusuarios.tbl_saureg_agencia a1 where i.id_instituc = a1.id_instituc and a1.cod_msp = f.fk_agencia_saureg) as institucion, ");
        query.append("f.fk_agencia_saureg as cod_MSP, ");
        query.append("(select a.nom_agencia from admusuarios.tbl_saureg_agencia a where a.cod_msp = f.fk_agencia_saureg) as nom_agencia, ");
        
        query.append("(select count(*) from fallecido f1 where f1.fk_id_estado_firma = 1 and f1.fk_id_est_reg_fal in (2,3,7) and f.fk_agencia_saureg = f1.fk_agencia_saureg ");
        query.append("and ( to_date (to_char( f1.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and f1.fk_agencia_saureg  in (");
        query.append(idagencias.toString());
        query.append(") ");
        if(userCedula != null){
            query.append("and ( f1.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and f1.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as incompletos, ");
        
        query.append("(select count(*) from fallecido f2 where f2.fk_id_estado_firma = 1 and f2.fk_id_est_reg_fal in (4,8) and f.fk_agencia_saureg = f2.fk_agencia_saureg ");
        query.append("and ( to_date (to_char( f2.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and f2.fk_agencia_saureg  in (");
        query.append(idagencias.toString());
        query.append(") ");
        if(userCedula != null){
            query.append("and ( f2.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and f2.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as no_firmados, ");
        
        query.append("(select count(*) from fallecido f3 where f3.fk_id_estado_firma = 2 and f3.fk_id_est_reg_fal in (4,5,8) and f.fk_agencia_saureg = f3.fk_agencia_saureg ");
        query.append("and ( to_date (to_char( f3.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and f3.fk_agencia_saureg  in (");
        query.append(idagencias.toString());
        query.append(") ");
        if(userCedula != null){
            query.append("and ( f3.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and f3.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as firmados, ");
        
        query.append("(select count(*) from fallecido f4 where f4.fk_id_estado_firma = 2 and f4.fk_id_est_reg_fal in (5) and f.fk_agencia_saureg = f4.fk_agencia_saureg ");
        query.append("and ( to_date (to_char( f4.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and f4.fk_agencia_saureg  in (");
        query.append(idagencias.toString());
        query.append(") ");
        if(userCedula != null){
            query.append("and ( f4.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and f4.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as inscritos, ");
        
        query.append("(select count(*) from fallecido f5 where f5.fk_id_estado_firma = 2 and f5.fk_id_est_reg_fal in (6) and f.fk_agencia_saureg = f5.fk_agencia_saureg ");
        query.append("and ( to_date (to_char( f5.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and f5.fk_agencia_saureg  in (");
        query.append(idagencias.toString());
        query.append(") ");
        if(userCedula != null){
            query.append("and ( f5.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and f5.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as anulados, ");
        
        query.append("count(*) as total ");
        query.append("from fallecido f ");
        query.append("where ( to_date (to_char( f.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and f.fk_agencia_saureg  in (");
        query.append(idagencias.toString());
        query.append(") ");
        if(userCedula != null){
            query.append("and ( f.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and f.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append("group by f.fk_agencia_saureg order by nom_agencia asc");
        System.out.println("--> " + query.toString());
        try {
            List<Object[]> items = new ArrayList<Object[]>();
            items = ejbFallecidoFacade.executeNativeQueryListResult(query.toString());
            for(Object[] item : items){
                NacidoVivoReporte obj = new NacidoVivoReporte();
                if(item[0] != null){
                    obj.setProvincia(item[0].toString());
                }
                if(item[1] != null){
                   obj.setInstitucion(item[1].toString()); 
                }    
                if(item[2] != null){
                    obj.setCodMSP(item[2].toString());
                }    
                if(item[3] != null){
                    obj.setAgencia(item[3].toString());
                }     
                obj.setIncompletos((BigDecimal)item[4]);
                obj.setNoFirmados((BigDecimal)item[5]);
                obj.setFirmados((BigDecimal)item[6]);
                obj.setInscritos((BigDecimal)item[7]);
                obj.setAnulados((BigDecimal)item[8]);
                obj.setTotalPorAgencia((BigDecimal)item[9]);
                resultado.add(obj);
                totalIncompletos.add((BigDecimal)item[4]);
                totalNoFirmados.add((BigDecimal)item[5]);
                totalFirmados.add((BigDecimal)item[6]);
                totalInscritos.add((BigDecimal)item[7]);
                totalAnulados.add((BigDecimal)item[8]);
                total.add((BigDecimal)item[9]);
            }
            if(resultado.isEmpty()){
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("growl");
            }
            RequestContext.getCurrentInstance().update("tblResultados");
            System.gc();
        } catch (EntidadException e) {
            System.gc();
        }
    }
    
    public void generaReportePDF() throws SQLException, JRException, IOException {
        try {
            if (resultado.size() > 0) {
                Map<String, Object> parametros = new HashMap<String, Object>();
                parametros.put("fechaNacIni", fechaNacIni);
                parametros.put("fechaNacFin", fechaNacFin);
                parametros.put("usuario", userLgn.getNombre() + " " + userLgn.getApellido());
                if (codMSP != null) {
                    String[] codNomAge = codMSP.toString().split(";");
                    parametros.put("unidadMedica", codNomAge[1]);
                }
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/reporteDefDetalladoRevit.jasper");
                JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(resultado);
                jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
                HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                String fi = sdf.format(fechaNacIni);
                String ff = sdf.format(fechaNacFin);
                String fecha = fi.toString() + "_" + ff.toString();
                httpServletResponse.addHeader("Content-disposition", "attachment; filename=reporte_" + fecha + ".pdf");
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                FacesContext.getCurrentInstance().responseComplete();
                System.gc();
            } else {
                System.gc();
                System.out.println("No tiene datos");
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistrosReporte"), JsfUtil.WARN_MESSAGE);
            }
        } catch (Exception e) {
            System.gc();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistrosReporte"), JsfUtil.WARN_MESSAGE);
        }
    }

    public void recuperaDatosFirma(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            String value = event.getNewValue().toString();
            System.out.println("Estado de Firma:" + value);

        }
    }
    
    //********************
    public Date getFechaNacIni() {
        return fechaNacIni;
    }

    public void setFechaNacIni(Date fechaNacIni) {
        this.fechaNacIni = fechaNacIni;
    }

    public Date getFechaNacFin() {
        return fechaNacFin;
    }

    public void setFechaNacFin(Date fechaNacFin) {
        this.fechaNacFin = fechaNacFin;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public List<TblSauregInstitucion> getInstituciones() {
        if(instituciones == null){
            instituciones = new ArrayList<TblSauregInstitucion>();
        }
        return instituciones;
    }

    public String getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public List<TblSauregAgencia> getAgencias() {
        if(agencias == null){
            agencias = new ArrayList<TblSauregAgencia>();
        }
        return agencias;
    }

    public String getCodMSP() {
        return codMSP;
    }

    public void setCodMSP(String codMSP) {
        this.codMSP = codMSP;
    }

    public List<TblSauregUsuario> getUsuarios() {
        if(usuarios == null){
            usuarios = new ArrayList<TblSauregUsuario>();
        }
        return usuarios;
    }

    public String getUserCedula() {
        return userCedula;
    }

    public void setUserCedula(String userCedula) {
        this.userCedula = userCedula;
    }
    
    public List<NacidoVivoReporte> getResultado() {
        if(resultado == null){
            resultado = new ArrayList<NacidoVivoReporte>();
        }
        return resultado;
    }

    public Boolean isRol_inst() {
        return rol_inst;
    }

    public Boolean isRol_rc() {
        return rol_rc;
    }

    public String getRolSesion() {
        return rolSesion;
    }

    public BigDecimal getTotalIncompletos() {
        return totalIncompletos;
    }

    public BigDecimal getTotalNoFirmados() {
        return totalNoFirmados;
    }

    public BigDecimal getTotalFirmados() {
        return totalFirmados;
    }

    public BigDecimal getTotalInscritos() {
        return totalInscritos;
    }

    public BigDecimal getTotalAnulados() {
        return totalAnulados;
    }

    public BigDecimal getTotal() {
        return total;
    }
    
    
}
