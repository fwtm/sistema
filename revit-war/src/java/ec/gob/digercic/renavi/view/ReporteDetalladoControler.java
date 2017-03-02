/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
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
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "reporteDetalladoControler")
@ViewScoped
public class ReporteDetalladoControler implements Serializable {

    private Date fechaNacIni; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaNacFin; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaActual; //Fecha actual del sistema
    private List<ec.gob.digercic.renavi.ws.TblSauregInstitucion> instituciones;//JJHF
    private String idInstitucion;
    private List<ec.gob.digercic.renavi.ws.TblSauregAgencia> agencias;//JJHF
    private String codMSP;
    private List<ec.gob.digercic.renavi.ws.TblSauregUsuario> usuarios;//JJHF CAMBIO LOGIN
    private String userCedula;
    //Usuario en sesión
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn;//JJHF CAMBIO LOGIN
    //Los roles para los reportes
    private Boolean rol_inst;
    private Boolean rol_rc;
    private String rolSesion;
    //Donde se retornaran los resultados
    private List<NacidoVivoReporte> resultado;
    private BigDecimal totalIncompletos;
    private BigDecimal totalNoFirmados;
    private BigDecimal totalFirmados;
    ///cambios FWTM
    private BigDecimal totalInscritos;
    private BigDecimal totalAnulados;
    private BigDecimal total;
    JasperPrint jasperPrint;

    @EJB
    private NacidoVivoRenaviFacadeLocal ejbNacidoVivoRenaviFacade;
    @EJB
    private TblSauregInstitucionFacadeLocal ejbInstitucionFacade;
    @EJB
    private TblSauregAgenciaFacadeLocal ejbAgenciaFacade;
    @EJB
    private TblSauregUsuarioFacadeLocal ejbUsuarioFacade;

    public ReporteDetalladoControler() {
    }

    @PostConstruct
    public void init() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            userCedula=userLgn.getNomUsuario();//JJHF CAMBIO LOGIN
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fechaActual = new Date();
            fechaNacFin = fechaActual;
            fechaNacIni = sdf.parse(sdf.format(fechaActual));
            totalIncompletos = new BigDecimal("0");
            totalNoFirmados = new BigDecimal("0");
            totalFirmados = new BigDecimal("0");
            totalInscritos = new BigDecimal("0");
            totalAnulados = new BigDecimal("0");
            totalInscritos = new BigDecimal("0");
            
            total = new BigDecimal("0");
            //Obtengo que rol posee el usuario (esto es en base a la jerarquia.)
            rol_inst = false;
            rol_rc = false;
            for (int i = 0; i < userLgn.getTblSauregCompRolList().size(); i++) {
                ec.gob.digercic.renavi.ws.TblSauregRol rol = userLgn.getTblSauregCompRolList().get(i).getTblSauregRol();//JJHF CAMBIO LOG IN
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
//                StringBuilder qrc = new StringBuilder("select i.* from tbl_saureg_institucion i ");//JJHF CAMBIO LOG IN
//                qrc.append("where i.id_instituc in ( ");//JJHF CAMBIO LOG IN
//                qrc.append("select si.id_instituc from tbl_saureg_sist_institucion si ");//JJHF CAMBIO LOG IN
//                qrc.append("where si.id_sistema = 1 and si.status = '");//JJHF CAMBIO LOG IN
//                qrc.append(JsfUtil.ESTADO_REG_ACTIVO);//JJHF CAMBIO LOG IN
//                qrc.append("')");//JJHF CAMBIO LOG IN
//                instituciones = ejbInstitucionFacade.executeNativeQueryListResultGenerico(qrc.toString(), TblSauregInstitucion.class);
                instituciones= this.InstitucionBySistema("1", "A");
            } else if (rol_inst) {
                rolSesion = "RI"; //Rol REG. CIVIL
                instituciones = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregInstitucion>();
                instituciones= this.InstitucionBySistema("1", "A");
             //  instituciones= userLgn.getListTblSauregInstitucion();//JJHF CAMBIO LOG IN
//                for (TblSauregSistInstitucion item//JJHF CAMBIO LOGIN
//                        : userLgn.getAgenciaInUser().getIdInstituc().getTblSauregSistInstitucionList()) {//JJHF CAMBIO LOGIN
//                    if (userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().//JJHF CAMBIO LOGIN
//                            equals(item.getIdInstituc().getIdInstituc())//JJHF CAMBIO LOGIN
//                            && item.getIdSistema().getIdSistema().equals(1L)) {//1L cod sistema revit//JJHF CAMBIO LOGIN
//                        instituciones.add(item.getIdInstituc());//JJHF CAMBIO LOGIN
//                        break;//JJHF CAMBIO LOGIN
//                    }//JJHF CAMBIO LOGIN
//                }//JJHF CAMBIO LOGIN
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
         //JJHF CAMBIO LOGIN
       private  List<ec.gob.digercic.renavi.ws.TblSauregInstitucion> InstitucionBySistema(java.lang.String idSistema,java.lang.String estado) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario usuario = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
        List<ec.gob.digercic.renavi.ws.TblSauregInstitucion> instituciones;
        instituciones = port.getInstitucionesBySistema(idSistema, estado);
       return instituciones;

    }
 //JJHF CAMBIO LOGIN

    public void poblaAgencias(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            agencias.clear();
            usuarios.clear();
            String value = event.getNewValue().toString();
            agencias =this.AgenciaByInstitucion(value); //carga todas las agencias dado un id de Institucion JJHF CAMBIO LOGIN
        } else {
            agencias = null;
        }
    }
     //JJHF CAMBIO LOGIN
                    //JJHF CAMBIO LOGIN
       private  List<ec.gob.digercic.renavi.ws.TblSauregAgencia> AgenciaByInstitucion(java.lang.String idInstitucion) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario usuario = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
        List<ec.gob.digercic.renavi.ws.TblSauregAgencia> agencias;
        agencias = port.getAgenciasPorInstitucion(idInstitucion);
       return agencias;

    }
 //JJHF CAMBIO LOGIN
    public void poblaUsuariosPorAgencias(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            usuarios.clear();
            String[] codNomAge = event.getNewValue().toString().split(";");
            try {
                usuarios = this.UsuariosByAgenciaMSP(codNomAge[0]);//JJHF CAMBIO LOGIN
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     //JJHF CAMBIO LOGIN
       private  List<ec.gob.digercic.renavi.ws.TblSauregUsuario> UsuariosByAgenciaMSP(java.lang.String codMsp) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario usuario = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
        List<ec.gob.digercic.renavi.ws.TblSauregUsuario> Usuarios;
        Usuarios = port.getUsuariosByAgenciaMSP(codMsp);
       return Usuarios;

    }
 //JJHF CAMBIO LOGIN
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
                //System.out.println("---> agencia: " + agencias.get(i).getNomAgencia() + " codigo: " + agencias.get(i).getCodMsp());
                if(agencias.get(i).getCodMsp() != null){
                   idagencias.append(agencias.get(i).getCodMsp()); 
                   if(i < agencias.size() - 1) {
                        idagencias.append("','");
                    }
                }  
            }            
            if("','".equals((idagencias.toString().substring(idagencias.toString().length()-1,idagencias.toString().length())))){
                idagencias = idagencias.deleteCharAt(idagencias.toString().length()-1);
            }
        }
        //System.out.println("---> idagencia: " +  idagencias);
       query.append("(select u.derscripcion from admusuarios.tbl_saureg_ubicacion u,admusuarios.tbl_saureg_agencia a2  where u.id_ubicacion = a2.id_provincia and a2.cod_msp = to_char( nv.fk_agencia_saureg )) as provincia, ");
        query.append("(select i.nom_corto from admusuarios.tbl_saureg_institucion i,admusuarios.tbl_saureg_agencia a1  where i.id_instituc = a1.id_instituc and a1.cod_msp = to_char( nv.fk_agencia_saureg )) as institucion, ");
        query.append("nv.fk_agencia_saureg as cod_MSP, ");
        query.append("(select a.nom_agencia from admusuarios.tbl_saureg_agencia a where a.cod_msp = to_char( nv.fk_agencia_saureg )) as nom_agencia, ");
        
        query.append("(select count(*) from nacido_vivo_renavi nv1 where nv1.fk_id_est_fir = 1 and nv1.fk_id_est_reg in (2,3,7) and nv.fk_agencia_saureg = to_char( nv1.fk_agencia_saureg ) ");
        query.append("and ( to_date (to_char( nv1.fecha_nacim_nac_viv,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and nv1.fk_agencia_saureg  in('");
        query.append(idagencias.toString());
        query.append("')");
        if(userCedula != null){
            query.append("and ( nv1.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and nv1.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as incompletos, ");
        
        query.append("(select count(*) from nacido_vivo_renavi nv2 where nv2.fk_id_est_fir = 1 and nv2.fk_id_est_reg in (4,8) and nv.fk_agencia_saureg = to_char( nv2.fk_agencia_saureg ) ");
        query.append("and ( to_date (to_char( nv2.fecha_nacim_nac_viv,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and nv2.fk_agencia_saureg in ('");
        query.append(idagencias.toString());
        query.append("')");
        if(userCedula != null){
            query.append("and ( nv2.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and nv2.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as no_firmados, ");
        
        query.append("(select count(*) from nacido_vivo_renavi nv3 where nv3.fk_id_est_fir = 2 and nv3.fk_id_est_reg in (4,8) and nv.fk_agencia_saureg = to_char( nv3.fk_agencia_saureg ) ");
        query.append("and ( to_date (to_char( nv3.fecha_nacim_nac_viv,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and nv3.fk_agencia_saureg in('");
        query.append(idagencias.toString());
        query.append("')");
        if(userCedula != null){
            query.append("and ( nv3.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and nv3.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as firmados, ");
        
        query.append("(select count(*) from nacido_vivo_renavi nv4 where nv4.fk_id_est_fir = 2 and nv4.fk_id_est_reg = 5 and nv.fk_agencia_saureg = to_char( nv4.fk_agencia_saureg ) ");
        query.append("and ( to_date (to_char( nv4.fecha_nacim_nac_viv,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and nv4.fk_agencia_saureg in ('");
        query.append(idagencias.toString());
        query.append("')");
        if(userCedula != null){
            query.append("and ( nv4.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and nv4.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as inscritos, ");
        
        query.append("(select count(*) from nacido_vivo_renavi nv5 where nv5.fk_id_est_fir = 2 and nv5.fk_id_est_reg = 6 and nv.fk_agencia_saureg = to_char( nv5.fk_agencia_saureg ) ");
        query.append("and ( to_date (to_char( nv5.fecha_nacim_nac_viv,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and nv5.fk_agencia_saureg in ('");
        query.append(idagencias.toString());
        query.append("')");
        if(userCedula != null){
            query.append("and ( nv5.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and nv5.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append(") as anulados, ");
        
        query.append("count(*) as total ");
        query.append("from nacido_vivo_renavi nv ");
        query.append("where ( to_date (to_char( nv.fecha_nacim_nac_viv,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("and nv.fk_agencia_saureg in ('");
        query.append(idagencias.toString());
        query.append("')");
        query.append(" and nv.fk_id_est_reg not in (9) ");
        if(userCedula != null){
            query.append("and ( nv.fk_usu_firma_saureg = '");
            query.append(userCedula);
            query.append("' and nv.fk_usu_saureg = '");
            query.append(userCedula);
            query.append("' ) ");
        }
        query.append("group by nv.fk_agencia_saureg order by nom_agencia asc");
        System.out.println("--> " + query.toString());
        try {
            List<Object[]> items = new ArrayList<Object[]>();
            items = ejbNacidoVivoRenaviFacade.executeNativeQueryListResult(query.toString());
            
            System.out.println("resumido query " + query);
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
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/reporteDetalladoRevit.jasper");
                System.out.println("Lista----->" + resultado.size());
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
    
    public void generaReporteXLSX() throws SQLException, JRException, IOException {
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
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/reporteDetalladoRevit.jasper");
                JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(resultado);
                jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
                HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                String fi = sdf.format(fechaNacIni);
                String ff = sdf.format(fechaNacFin);
                String fecha = fi.toString() + "_" + ff.toString();
//                httpServletResponse.addHeader("Content-disposition", "attachment; filename=reporte_" + fecha + ".pdf");
//                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
//                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//                FacesContext.getCurrentInstance().responseComplete();
//                System.gc();
                httpServletResponse.addHeader("Content-disposition", "attachment; filename=reporte_" + fecha + ".xlsx");
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                JRXlsxExporter docxExporter = new JRXlsxExporter();
                docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                docxExporter.exportReport();
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                System.out.println("No tiene datos");
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistrosReporte"), JsfUtil.WARN_MESSAGE);
            }
        } catch (Exception e) {
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

    public List<ec.gob.digercic.renavi.ws.TblSauregInstitucion> getInstituciones() {
        if(instituciones == null){
            instituciones = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregInstitucion>();
        }
        return instituciones;
    }

    public String getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public List<ec.gob.digercic.renavi.ws.TblSauregAgencia> getAgencias() {
        if(agencias == null){
            agencias = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregAgencia>();
        }
        return agencias;
    }

    public String getCodMSP() {
        return codMSP;
    }

    public void setCodMSP(String codMSP) {
        this.codMSP = codMSP;
    }

    public List<ec.gob.digercic.renavi.ws.TblSauregUsuario> getUsuarios() {//JJHF CAMBIO LOGIN
        if(usuarios == null){//JJHF CAMBIO LOGIN
            usuarios = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregUsuario>();//JJHF CAMBIO LOGIN
        }//JJHF CAMBIO LOGIN
        return usuarios;//JJHF CAMBIO LOGIN
    }//JJHF CAMBIO LOGIN

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
