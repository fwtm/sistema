/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.EstadoFirmaRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.EstadoRegistroRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.FallecidoFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.EstadoFirmaRenavi;
import ec.gob.digercic.renavi.entities.EstadoRegistroRenavi;
import ec.gob.digercic.renavi.entities.Fallecido;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.view.util.RegistroRenavi;
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
import java.sql.Connection;
import java.sql.DriverManager;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "reporteDefControler")
@ViewScoped
public class ReporteDefControler implements Serializable {

    private Date fechaNacIni; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaNacFin; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaActual; //Fecha actual del sistema
    private List<TblSauregInstitucion> instituciones;
    private String idInstitucion;
    private List<TblSauregAgencia> agencias;
    private String codMSP;
    private List<TblSauregUsuario> usuarios;
    private String userCedula;
    private String cedulaDef;
    private List<EstadoFirmaRenavi> estadosFirma;
    private String idEstadoFirma;
    //Usuario en sesión
    private TblSauregUsuario userLgn;
    //Los roles para los reportes
    private Boolean rol_pro;
    private Boolean rol_inst;
    private Boolean rol_rc;
    private Boolean rol_age;
    private String rolSesion;
    //Donde se retornaran los resultados
    private List<Fallecido> resultado;
    JasperPrint jasperPrint;

    @EJB
    private FallecidoFacadeLocal ejbFallecidoFacade;
    @EJB
    private EstadoFirmaRenaviFacadeLocal ejbEstadoFirma;
    @EJB
    private TblSauregAgenciaFacadeLocal ejbAgenciaFacade;
    @EJB
    private TblSauregUsuarioFacadeLocal ejbUsuarioFacade;
    @EJB
    private TblSauregInstitucionFacadeLocal ejbInstitucionFacade;
    @EJB
    private LogAccesosRenaviFacadeLocal ejbLogs;
    //Cambio Henry
    @EJB
    private EstadoRegistroRenaviFacadeLocal ejbEstadoRegFacade;
    //Cambio Henry
    private List<EstadoRegistroRenavi> listEstReg;
    // private EstadoRegistroRenavi estadoReg;
    private Integer regEst;
    private List<RegistroRenavi> listRegEst = new ArrayList<RegistroRenavi>();

    public ReporteDefControler() {
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
            estadosFirma = ejbEstadoFirma.findAll();
            //Obtengo que rol posee el usuario (esto es en base a la jerarquia.)
            rol_inst = false;
            rol_rc = false;
            rol_age = false;
            rol_pro = false;
            for (int i = 0; i < userLgn.getTblSauregCompRolList().size(); i++) {
                TblSauregRol rol = userLgn.getTblSauregCompRolList().get(i).getTblSauregRol();
                if (rol.getIdRol() == 22) {
                    rol_pro = true;
                }
                if (rol.getIdRol() == 25) {
                    rol_age = true;
                }
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
            } else if (rol_age) {
                rolSesion = "RA"; //Rol INSTITUCIONES
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
            } else if (rol_pro) {
                rolSesion = "RP"; //Rol PROFESIONAL MEDICO
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
            /*Aquí va el código que carga las agencias en las que esta asignado el profesional*/
            if (rolSesion.equals("RC") || rolSesion.equals("RI")) {
                agencias = ejbAgenciaFacade.getAgenciasPorInstitucion(value); //carga todas las agencias dado un id de Institucion
            } else {
                agencias = new ArrayList<TblSauregAgencia>();
                agencias.add(userLgn.getAgenciaInUser());
            }
        } else {
            agencias = null;
        }
    }

    public void poblaUsuariosPorAgencias(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            usuarios.clear();
            String[] codNomAge = event.getNewValue().toString().split(";");
            try {
                if (rolSesion.equals("RC") || rolSesion.equals("RI") || rolSesion.equals("RA")) {
                    usuarios = ejbUsuarioFacade.getUsuariosByAgenciaMSP(codNomAge[0]);
                } else {
                    usuarios = new ArrayList<TblSauregUsuario>();
                    usuarios.add(userLgn);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void generaReporte() throws SQLException, JRException, IOException {
        resultado.clear();
        DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder query = new StringBuilder("select f.* ");
                query.append("from fallecido f, ESTADO_FIRMA_RENAVI EF ");
                query.append("where ( to_date (to_char( f.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
                query.append(dt1.format(fechaNacIni));
                query.append("' AND  date '");
                query.append(dt1.format(fechaNacFin));
                query.append("') ");
                query.append("and f.fk_id_estado_firma = ef.id_est_fir ");
                if (codMSP != null) {
                    String[] codNomAge = codMSP.split(";");
                    query.append("and f.fk_agencia_saureg  = ");
                    query.append(codNomAge[0]);
                    query.append(" ");
                }
                if (userCedula != null) {
                    query.append("and ( f.fk_usu_firma_saureg = '");
                    query.append(userCedula);
                    query.append("' and f.fk_usu_saureg = '");
                    query.append(userCedula);
                    query.append("' ) ");
                } 
              
                // Estado Incompleto
                if (this.getRegEst() == 1) {
                    query.append(" and f.fk_id_est_reg_fal in (2,3,7)");
                    query.append(" and EF.ID_EST_FIR = 1 ");
                }
                //Estado Firmado
                if (this.getRegEst() == 2) {
                    query.append(" and f.fk_id_est_reg_fal in (4,5,8) ");
                    query.append(" and EF.ID_EST_FIR = 2 ");
                }
                //Esatdo Anulados
                if (this.getRegEst() == 3) {
                    query.append(" f.fk_id_est_reg_fal in (6)");
                    query.append(" and (EF.ID_EST_FIR = 1 or EF.ID_EST_FIR = 2)");
                }
                //Esatdo Sin Firma
                if (this.getRegEst() == 4) {
                    query.append(" and f.fk_id_est_reg_fal in (4,8)");
                    query.append(" and EF.ID_EST_FIR = 1 ");
                }
                 if(!cedulaDef.equals("")){
                   query.append(" and f.cedula_fal='");
                   query.append(cedulaDef);
                   query.append("'");
                }
                query.append(" order by f.fk_id_estado_firma, f.fecha_nacimiento_fal, f.fk_agencia_firma_saureg");
        System.out.println("--> " + query.toString());
        try {
            resultado = ejbFallecidoFacade.executeNativeQueryListResultGenerico(query.toString(), Fallecido.class);
            if (resultado.isEmpty()) {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("growl");
            }
            RequestContext.getCurrentInstance().update("tblResultados");
        } catch (EntidadException e) {
            e.printStackTrace();
        }
    }

    public void generaReportePDF() throws SQLException, JRException, IOException {
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
            log.setAccion("REVIT. REPORTES. EJECUTAR REPORTE GENERAL");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }

        try {
            if (resultado.size() > 0) {
                DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                StringBuilder query = new StringBuilder("select f.cedula_fal, f.nombre_fal, f.fecha_nacimiento_fal, f.fecha_fallecimiento_fal, f.edad_fal, ");
                query.append("f.provincia_ds_fllcmnt_fal, f.canton_ds_fllcmnt_fal,ciudad_fllcmnt_fal,parroquia_ds_fllcmnt_fal, f.fk_usu_saureg, ");
                query.append("(SELECT u.nombre ||' '||u.apellido FROM ADMUSUARIOS.tbl_saureg_usuario u WHERE u.nom_usuario=f.fk_usu_firma_saureg) AS usuario,");
                query.append(" ef.desc_est_fir, f.fk_agencia_saureg  ");
                query.append("from fallecido f, ESTADO_FIRMA_RENAVI EF ");
                query.append("where ( to_date (to_char( f.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
                query.append(dt1.format(fechaNacIni));
                query.append("' AND  date '");
                query.append(dt1.format(fechaNacFin));
                query.append("') ");
                query.append("and f.fk_id_estado_firma = ef.id_est_fir ");
                if (codMSP != null) {
                    String[] codNomAge = codMSP.split(";");
                    query.append("and f.fk_agencia_saureg  = ");
                    query.append(codNomAge[0]);
                    query.append(" ");
                }
                if (userCedula != null) {
                    query.append("and ( f.fk_usu_firma_saureg = '");
                    query.append(userCedula);
                    query.append("' and f.fk_usu_saureg = '");
                    query.append(userCedula);
                    query.append("' ) ");
                } 
                // Estado Incompleto
                if (this.getRegEst() == 1) {
                    query.append(" and f.fk_id_est_reg_fal in (2,3,7)");
                    query.append(" and EF.ID_EST_FIR = 1 ");
                }
                //Estado Firmado
                if (this.getRegEst() == 2) {
                    query.append(" and f.fk_id_est_reg_fal in (4,5,8) ");
                    query.append(" and EF.ID_EST_FIR = 2 ");
                }
                //Esatdo Anulados
                if (this.getRegEst() == 3) {
                    query.append(" f.fk_id_est_reg_fal in (6)");
                    query.append(" and (EF.ID_EST_FIR = 1 or EF.ID_EST_FIR = 2)");
                }
                //Esatdo Sin Firma
                if (this.getRegEst() == 4) {
                    query.append(" and f.fk_id_est_reg_fal in (4,8)");
                    query.append(" and EF.ID_EST_FIR = 1 ");
                }
                if(!cedulaDef.equals("")){
                   query.append(" and f.cedula_fal='");
                   query.append(cedulaDef);
                   query.append("'");
                }
                query.append(" order by f.fk_id_estado_firma, f.fecha_nacimiento_fal, f.fk_agencia_firma_saureg");
                //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.24.16.38:1521:INTDB", "revit", "revit");
                //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.91.16.45:1521:intdb", "revit", "revit2015$");
                Connection conn =JsfUtil.getConecction();//DFJ
                Map<String, Object> parametros = new HashMap<String, Object>();
                parametros.put("fechaNacIni", fechaNacIni);
                parametros.put("fechaNacFin", fechaNacFin);
                parametros.put("usuario", userLgn.getNombre() + " " + userLgn.getApellido());
                parametros.put("query", query.toString());
                System.out.println("--->Henry " + query.toString());
                //parametros.put("unidadMedica","vacio");
                if(regEst == 1){
                    parametros.put("tipoReporte", "INCOMPLETOS");
                }else if(regEst == 2){
                    parametros.put("tipoReporte", "FIRMADOS");
                }else if(regEst == 3){
                    parametros.put("tipoReporte", "ANULADOS");
                }else{
                    parametros.put("tipoReporte", "SIN FIRMAR");
                }
                
                if (codMSP != null) {
                    String[] codNomAge = codMSP.toString().split(";");
                    parametros.put("unidadMedica", codNomAge[1]);
                }
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/reporteDefRevit.jasper");
                jasperPrint = JasperFillManager.fillReport(reportPath, parametros, conn);
                HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                String fi = sdf.format(fechaNacIni);
                String ff = sdf.format(fechaNacFin);
                String fecha = fi.toString() + "_" + ff.toString();
                httpServletResponse.addHeader("Content-disposition", "attachment; filename=reporte_" + fecha + ".pdf");
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                System.out.println("No tiene datos");
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistrosReporte"), JsfUtil.WARN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistrosReporte"), JsfUtil.WARN_MESSAGE);
        }
    }

    public void recuperaDatosFirma(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            String value = event.getNewValue().toString();
            System.out.println("Estado de Firma:" + value);

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
            log.setAccion("REVIT. REPORTES. VER PDF REPORTE GENERAL");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        System.out.println("Desde reporte: verPDFConFirma()");
        try {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            String cedula = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedula");
            System.out.println("Cedula desde la vista: " + cedula);
            ParametroConsulta param = new ParametroConsulta("cedulaFal", cedula);
            parametros.add(param);
            System.out.println("Recuperando el pdf firmado");
            Fallecido objeto = (Fallecido) ejbFallecidoFacade.consultarTablaSingleResult("Fallecido.findByCedulaFal", parametros);
            System.out.println("el Array es:" + objeto.getPdfConFirma());
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
        } catch (EntidadException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().responseComplete();
            JsfUtil.displayMessage("No se puede visualizar el PDF del registro.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form:messages");
        }
    }

    public void verPDFSinFirma() {
        try {
            Long idFal = new Long(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idFal"));
            Fallecido objeto = ejbFallecidoFacade.find(idFal);
            byte[] pdfData = objeto.getPdfSinFirma();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            // Empieza proceso de response Initialization.
            response.reset();
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "attachment;filename=Certificado-Temp_" + idFal + ".pdf");
            // Write file to response.
            response.getOutputStream().write(pdfData);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            // Inform JSF to not take the response in hands.
            facesContext.responseComplete(); // 
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().responseComplete();
            JsfUtil.displayMessage("No se puede visualizar el PDF.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form:messages");
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
        if (instituciones == null) {
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
        if (agencias == null) {
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
        if (usuarios == null) {
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

    public String getCedulaDef() {
        return cedulaDef;
    }

    public void setCedulaDef(String cedulaDef) {
        this.cedulaDef = cedulaDef;
    }

  

    public List<EstadoFirmaRenavi> getEstadosFirma() {
        if (estadosFirma == null) {
            estadosFirma = new ArrayList<EstadoFirmaRenavi>();
        }
        return estadosFirma;
    }

    public String getIdEstadoFirma() {
        return idEstadoFirma;
    }

    public void setIdEstadoFirma(String idEstadoFirma) {
        this.idEstadoFirma = idEstadoFirma;
    }

    public List<Fallecido> getResultado() {
        if (resultado == null) {
            resultado = new ArrayList<Fallecido>();
        }
        return resultado;
    }

    public Boolean isRol_pro() {
        return rol_pro;
    }

    public Boolean isRol_inst() {
        return rol_inst;
    }

    public Boolean isRol_rc() {
        return rol_rc;
    }

    public Boolean isRol_age() {
        return rol_age;
    }

    public String getRolSesion() {
        return rolSesion;
    }

    public List<RegistroRenavi> getListRegEst() {
        if (listRegEst.size() <= 0) {
            listRegEst.add(new RegistroRenavi(1, "INCOMPLETOS"));
            listRegEst.add(new RegistroRenavi(2, "FIRMADOS"));
            listRegEst.add(new RegistroRenavi(3, "ANULADOS"));
            listRegEst.add(new RegistroRenavi(4, "SIN FIRMAR"));
        }
        return listRegEst;
    }

    public void setListRegEst(List<RegistroRenavi> listRegEst) {
        this.listRegEst = listRegEst;
    }

    public Integer getRegEst() {
        return regEst;
    }

    public void setRegEst(Integer regEst) {
        this.regEst = regEst;
    }

    public void prueba(ValueChangeEvent event) {
        
        this.setRegEst((Integer) event.getNewValue());
        System.out.println("reistro===>>" + regEst);
    }

}
