/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.NacidoVivoReporteDetallado;
import ec.gob.digercic.renavi.entities.UsuarioReporteDetallado;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.EnvioMail;
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
import java.util.Collections;
import java.util.Comparator;
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
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "reporteDetalladoUsuarioControler")
@ViewScoped
public class ReporteDetalladoUsuarioControler implements Serializable {

    private Date fechaNacIni; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaNacFin; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaActual; //Fecha actual del sistema
    private List<ec.gob.digercic.renavi.ws.TblSauregInstitucion> instituciones;//JJHF CAMBIO LOGIN 
    private String idInstitucion;
    private List<ec.gob.digercic.renavi.ws.TblSauregAgencia> agencias;//JJHF CAMBIO LOGIN 
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
    private List<UsuarioReporteDetallado> resultado;
    private BigDecimal totalIncompletos;
    private BigDecimal totalNoFirmados;
    private BigDecimal totalFirmados;
    private BigDecimal totalInscritos;
    private BigDecimal totalAnulados;
    private BigDecimal total;
    JasperPrint jasperPrint;
    ////Cambio para reporte detallados
    
    private Integer totalRegistros;

    @EJB
    private NacidoVivoRenaviFacadeLocal ejbNacidoVivoRenaviFacade;
    @EJB
    private TblSauregInstitucionFacadeLocal ejbInstitucionFacade;
    @EJB
    private TblSauregAgenciaFacadeLocal ejbAgenciaFacade;
    @EJB
    private TblSauregUsuarioFacadeLocal ejbUsuarioFacade;

    public ReporteDetalladoUsuarioControler() {
    }

    @PostConstruct
    public void init() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");//JJHF CAMBIO LOGIN
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
                ec.gob.digercic.renavi.ws.TblSauregRol rol = userLgn.getTblSauregCompRolList().get(i).getTblSauregRol();//JJHF CAMBIO LOGIN
                if (rol.getIdRol() == 21) {
                    rol_inst = true;
                }
                if (rol.getIdRol() == 20) {
                    rol_rc = true;
                }
            }
            //dependiendo del rol obtengo las instituciones
            //List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            if (rol_rc) {
                rolSesion = "RC"; //Rol REG. CIVIL
//                StringBuilder qrc = new StringBuilder("select i.* from tbl_saureg_institucion i ");
//                qrc.append("where i.id_instituc in ( ");
//                qrc.append("select si.id_instituc from tbl_saureg_sist_institucion si ");
//                qrc.append("where si.id_sistema = 1 and si.status = '");
//                qrc.append(JsfUtil.ESTADO_REG_ACTIVO);
//                qrc.append("')");
//                instituciones = ejbInstitucionFacade.executeNativeQueryListResultGenerico(qrc.toString(), TblSauregInstitucion.class);
                instituciones= this.InstitucionBySistema("1", "A");
                
            } else if (rol_inst) {
                rolSesion = "RI"; //Rol REG. CIVIL
                instituciones = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregInstitucion>();
                instituciones= this.InstitucionBySistema("1", "A");
//                for (TblSauregSistInstitucion item
//                        : userLgn.getAgenciaInUser().getIdInstituc().getTblSauregSistInstitucionList()) {
//                    if (userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().
//                            equals(item.getIdInstituc().getIdInstituc())
//                            && item.getIdSistema().getIdSistema().equals(1L)) {//1L cod sistema revit
//                        instituciones.add(item.getIdInstituc());
//                        break;
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void poblaAgencias(ValueChangeEvent event) {
        agencias.clear();
        usuarios.clear();
        if (event.getNewValue() != null) {
            String value = event.getNewValue().toString();
            agencias = this.AgenciaByInstitucion(value); //carga todas las agencias dado un id de Institucion JJHF CAMBIO LOGIN
        }
    }

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
    /**
     * Comparador para ordenar la lista de hijos de acuerdo al número de
     * producto del embrazo
     */
    private static Comparator<NacidoVivoReporteDetallado> COMPARATOR = new Comparator<NacidoVivoReporteDetallado>() {
        // This is where the sorting happens.
        @Override
        public int compare(NacidoVivoReporteDetallado n1, NacidoVivoReporteDetallado n2) {
            return n1.getEstado().compareTo(n2.getEstado());
        }
    };

    public void generaReporte() throws SQLException, JRException, IOException {
        resultado.clear();
        DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder query = new StringBuilder("select nv.cedul_nac_viv, ");
        query.append("nv.apell_nac_viv || ' ' || nv.nombr_nac_viv, ");
        query.append("nv.fecha_nacim_nac_viv, nv.pdf_sin_firma_nac_viv, ");
        //Usuario
        query.append("(SELECT u.apellido ||' '|| u.nombre FROM ADMUSUARIOS.tbl_saureg_usuario u WHERE u.nom_usuario = nv.fk_usu_saureg) AS nombreUsuario, ");
        query.append("(SELECT a.nom_agencia FROM ADMUSUARIOS.Tbl_Saureg_Agencia a WHERE a.cod_msp = to_char(nv.fk_agencia_saureg)) AS agencia, ");
        query.append("nv.fk_agencia_saureg, nv.fk_usu_saureg, ");
        query.append("nv.fk_id_est_fir , nv.fk_id_est_reg, m.nombr_mad,");
        query.append("(select p.nombr_pad from padre_renavi p where p.id_pad = nv.fk_cedul_mad) AS padre, ");
        query.append("(select i.nom_corto from admusuarios.tbl_saureg_institucion i,admusuarios.tbl_saureg_agencia a1 \n" +
                       " where i.id_instituc = a1.id_instituc and a1.cod_msp = to_char(nv.fk_agencia_saureg)) as Institucion, ");
        query.append("(select u.derscripcion from admusuarios.tbl_saureg_ubicacion u,admusuarios.tbl_saureg_agencia a2  \n" +
                       " where u.id_ubicacion = a2.id_provincia and a2.cod_msp = to_char(nv.fk_agencia_saureg)) as provincia ");
        //condiciones
        query.append("from nacido_vivo_renavi nv, madre_renavi m ");
        query.append("where nv.fk_cedul_mad = m.id_mad ");
        query.append("and to_char(nv.fk_agencia_saureg) = ");
        String[] codNomAge = codMSP.split(";");
        query.append(codNomAge[0]);
        query.append(" ");
        if (userCedula != null) {
            query.append("and (nv.fk_usu_saureg in (");
            query.append("'");
            query.append(userCedula);
            query.append("') and ");
            query.append(" nv.fk_usu_firma_saureg in ('");
            query.append(userCedula);
            query.append("')) ");
        }
        query.append("and ( to_date (to_char( nv.fecha_nacim_nac_viv,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("order by agencia,nombreUsuario asc");
        System.out.println("---> " + query.toString());
        try {
            List<Object[]> items = new ArrayList<Object[]>();
            items = ejbNacidoVivoRenaviFacade.executeNativeQueryListResult(query.toString());
            List<NacidoVivoReporteDetallado> itemsUsuario = new ArrayList<NacidoVivoReporteDetallado>();
            int anulados = 0;
            int firmados = 0;
            int noFirmados = 0;
            int inscritos = 0;
            int incompletos = 0;
            for (int i = 0; i < items.size(); i++) {
                NacidoVivoReporteDetallado obj = new NacidoVivoReporteDetallado();
                if (items.get(i)[0] != null) {
                    obj.setCedula(items.get(i)[0].toString());
                }
                if (items.get(i)[1] != null) {
                    obj.setNombres(items.get(i)[1].toString());
                }
                if (items.get(i)[2] != null) {
                    obj.setFechaNacimiento((Date)items.get(i)[2]);
                }
                if (items.get(i)[3] != null) {
                    obj.setPdf(items.get(i)[3].toString().getBytes());
                }               
                if ((((BigDecimal) items.get(i)[9]).intValue() == 4
                        || ((BigDecimal) items.get(i)[9]).intValue() == 8)
                        && ((BigDecimal) items.get(i)[8]).intValue() == 2) {
                    obj.setEstado("FIRMADO");
                    firmados ++;
                } else if ((((BigDecimal) items.get(i)[9]).intValue() == 4
                        //|| ((BigDecimal)items.get(i)[9]).intValue() == 7 
                        || ((BigDecimal) items.get(i)[9]).intValue() == 8)
                        && ((BigDecimal) items.get(i)[8]).intValue() == 1) {
                    obj.setEstado("NO FIRMADO");
                    noFirmados ++;
                } else if (((BigDecimal) items.get(i)[9]).intValue() == 5
                        && ((BigDecimal) items.get(i)[8]).intValue() == 2) {
                    obj.setEstado("INSCRITO");
                    inscritos ++;
                } else if (((BigDecimal) items.get(i)[9]).intValue() == 6
                        && (((BigDecimal) items.get(i)[8]).intValue() == 1
                        || ((BigDecimal) items.get(i)[8]).intValue() == 2)) {
                    obj.setEstado("ANULADO");
                    anulados ++;
                } else if ((((BigDecimal) items.get(i)[9]).intValue() == 2
                        || ((BigDecimal) items.get(i)[9]).intValue() == 3
                        || ((BigDecimal) items.get(i)[9]).intValue() == 7)
                        && ((BigDecimal) items.get(i)[8]).intValue() == 1) {
                    obj.setEstado("INCOMPLETO");
                    incompletos ++;
                }
                if(items.get(i)[10] != null){//madre
                    obj.setNombresMadre(items.get(i)[10].toString());
                }
                if(items.get(i)[11] != null){//padre
                    obj.setNombresPadre(items.get(i)[11].toString());
                }
                itemsUsuario.add(obj);
                if (i <= items.size() - 2) {
                    if (!items.get(i)[7].toString().equals(items.get(i+1)[7].toString())) {
                        List<NacidoVivoReporteDetallado> list = new ArrayList<>(itemsUsuario);
//                        Collections.sort(list, COMPARATOR);
                        UsuarioReporteDetallado userRep = new UsuarioReporteDetallado();
                        userRep.setNacVivDetallado(list);
                        userRep.setInstitucion(items.get(i)[12].toString());
                        userRep.setNombres(items.get(i)[4].toString());                        
                        userRep.setAgencia(items.get(i)[5].toString());
                        userRep.setCodMSP(items.get(i)[6].toString());
                        userRep.setCedula(items.get(i)[7].toString());
                        userRep.setAnulados(anulados);
                        userRep.setFirmados(firmados);
                        userRep.setNoFirmados(noFirmados);
                        userRep.setIncompletos(incompletos);
                        userRep.setInscritos(inscritos);
                        totalRegistros = (anulados + firmados + noFirmados + incompletos + inscritos);
                        userRep.setTotal(totalRegistros);
                        //inicializo los contadores
                        anulados = 0;
                        firmados = 0;
                        noFirmados = 0;
                        inscritos = 0;
                        incompletos = 0;
                        resultado.add(userRep);
                        itemsUsuario.clear();
                    }
                } else {
                    List<NacidoVivoReporteDetallado> list = new ArrayList<>(itemsUsuario);
//                    Collections.sort(list, COMPARATOR);
                    UsuarioReporteDetallado userRepFinal = new UsuarioReporteDetallado();
                    userRepFinal.setNacVivDetallado(list);
                    userRepFinal.setInstitucion(items.get(i)[3].toString());
                    userRepFinal.setNombres(items.get(i)[4].toString());                    
                    userRepFinal.setAgencia(items.get(i)[5].toString());
                    userRepFinal.setCodMSP(items.get(i)[6].toString());
                    userRepFinal.setCedula(items.get(i)[7].toString());
                    userRepFinal.setAnulados(anulados);
                    userRepFinal.setFirmados(firmados);
                    userRepFinal.setNoFirmados(noFirmados);
                    userRepFinal.setIncompletos(incompletos);
                    userRepFinal.setInscritos(inscritos);
                    totalRegistros = (anulados + firmados + noFirmados + incompletos + inscritos);
                    userRepFinal.setTotal(totalRegistros);
                    resultado.add(userRepFinal);
                }
            }
            if (resultado.isEmpty()) {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("growl");
            }
            RequestContext.getCurrentInstance().update("tblResultados");
            System.gc();
        } catch (EntidadException e) {
//            e.printStackTrace();
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
                parametros.put("institucion", resultado.get(0).getInstitucion());
                //parametros.put("nombres", resultado.get(3).getNombres().toString());
                if (codMSP != null) {
                    String[] codNomAge = codMSP.toString().split(";");
                    parametros.put("unidadMedica", codNomAge[1]);
                }
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/reporteDetalladoUsuariosRevit.jasper");
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
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println("Error iReport: " + e.getMessage());
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
                parametros.put("institucion", resultado.get(0).getInstitucion());
                //parametros.put("nombres", resultado.get(3).getNombres().toString());
                if (codMSP != null) {
                    String[] codNomAge = codMSP.toString().split(";");
                    parametros.put("unidadMedica", codNomAge[1]);
                }
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/reporteDetalladoUsuariosRevit.jasper");
                JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(resultado);
                jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
                HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                String fi = sdf.format(fechaNacIni);
                String ff = sdf.format(fechaNacFin);
                String fecha = fi.toString() + "_" + ff.toString();
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
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println("Error iReport: " + e.getMessage());
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

    public List<ec.gob.digercic.renavi.ws.TblSauregInstitucion> getInstituciones() {
        if (instituciones == null) {
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
        if (agencias == null) {
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

    public List<ec.gob.digercic.renavi.ws.TblSauregUsuario> getUsuarios() {
        if (usuarios == null) {
            usuarios = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregUsuario>();
        }
        return usuarios;
    }

    public String getUserCedula() {
        return userCedula;
    }

    public void setUserCedula(String userCedula) {
        this.userCedula = userCedula;
    }

    public List<UsuarioReporteDetallado> getResultado() {
        if (resultado == null) {
            resultado = new ArrayList<UsuarioReporteDetallado>();
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
