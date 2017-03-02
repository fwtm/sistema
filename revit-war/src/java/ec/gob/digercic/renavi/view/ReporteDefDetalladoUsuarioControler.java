/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.FallecidoFacadeLocal;
import ec.gob.digercic.renavi.entities.NacidoVivoReporteDetallado;
import ec.gob.digercic.renavi.entities.UsuarioReporteDetallado;
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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "reporteDefDetalladoUsuarioControler")
@ViewScoped
public class ReporteDefDetalladoUsuarioControler implements Serializable {

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
    private List<UsuarioReporteDetallado> resultado;
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

    public ReporteDefDetalladoUsuarioControler() {
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
        agencias.clear();
        usuarios.clear();
        if (event.getNewValue() != null) {
            String value = event.getNewValue().toString();
            agencias = ejbAgenciaFacade.getAgenciasPorInstitucion(value); //carga todas las agencias dado un id de Institucion
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
        StringBuilder query = new StringBuilder("select f.cedula_fal, ");
        query.append("f.nombre_fal, ");
        query.append("f.fecha_fallecimiento_fal, f.pdf_sin_firma,f.nombre_madre_fal,f.nombre_padre_fal, ");
        //Usuario
        query.append("(SELECT u.apellido ||' '|| u.nombre FROM ADMUSUARIOS.tbl_saureg_usuario u WHERE u.nom_usuario = f.fk_usu_saureg) AS nombreUsuario, ");
        query.append("(SELECT a.nom_agencia FROM ADMUSUARIOS.Tbl_Saureg_Agencia a, ADMUSUARIOS.Tbl_Saureg_Usu_Agencia ua, ADMUSUARIOS.tbl_saureg_usuario u   WHERE a.id_agencia = ua.id_agencia and u.nom_usuario = ua.nom_usuario and u.nom_usuario = f.fk_usu_saureg and a.cod_msp = f.fk_agencia_saureg) AS agencia, ");
        query.append("f.fk_agencia_saureg, f.fk_usu_saureg, ");

        query.append("f.fk_id_estado_firma , f.fk_id_est_reg_fal");
      
        
        query.append(" from fallecido f ");
        //condiciones
        query.append(" where f.fk_agencia_saureg =  ");
        String[] codNomAge = codMSP.split(";");
        query.append(codNomAge[0]);
        query.append(" ");
        if (userCedula != null) {
            query.append("and (f.fk_usu_saureg in (");
            query.append("'");
            query.append(userCedula);
            query.append("') and ");
            query.append(" f.fk_usu_firma_saureg in ('");
            query.append(userCedula);
            query.append("')) ");
        } 
        query.append("and ( to_date (to_char( f.fecha_fallecimiento_fal,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN date '");
        query.append(dt1.format(fechaNacIni));
        query.append("' AND  date '");
        query.append(dt1.format(fechaNacFin));
        query.append("') ");
        query.append("order by agencia,nombreUsuario asc");
        System.out.println("---> " + query.toString());
        try {
            List<Object[]> items = new ArrayList<Object[]>();
            items = ejbFallecidoFacade.executeNativeQueryListResult(query.toString());
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
                if ((((BigDecimal) items.get(i)[11]).intValue() == 4
                        || ((BigDecimal) items.get(i)[11]).intValue() == 8)
                        && ((BigDecimal) items.get(i)[10]).intValue() == 2) {
                    obj.setEstado("FIRMADO");
                    firmados ++;
                } else if ((((BigDecimal) items.get(i)[11]).intValue() == 4
                        //|| ((BigDecimal)items.get(i)[9]).intValue() == 7 
                        || ((BigDecimal) items.get(i)[11]).intValue() == 8)
                        && ((BigDecimal) items.get(i)[10]).intValue() == 1) {
                    obj.setEstado("NO FIRMADO");
                    noFirmados ++;
                } else if (((BigDecimal) items.get(i)[11]).intValue() == 5
                        && ((BigDecimal) items.get(i)[10]).intValue() == 2) {
                    obj.setEstado("INSCRITO");
                    inscritos ++;
                } else if (((BigDecimal) items.get(i)[11]).intValue() == 6
                        && (((BigDecimal) items.get(i)[10]).intValue() == 1
                        || ((BigDecimal) items.get(i)[10]).intValue() == 2)) {
                    obj.setEstado("ANULADO");
                    anulados ++;
                } else if ((((BigDecimal) items.get(i)[11]).intValue() == 2
                        || ((BigDecimal) items.get(i)[11]).intValue() == 3
                        || ((BigDecimal) items.get(i)[11]).intValue() == 7)
                        && ((BigDecimal) items.get(i)[10]).intValue() == 1) {
                    obj.setEstado("INCOMPLETO");
                    incompletos ++;
                }
                if(items.get(i)[4] != null){//madre
                    obj.setNombresMadre(items.get(i)[4].toString());
                }
                if(items.get(i)[5] != null){//padre
                    obj.setNombresPadre(items.get(i)[5].toString());
                }
                itemsUsuario.add(obj);
                if (i <= items.size() - 2) {
                    if (!items.get(i)[9].toString().equals(items.get(i+1)[9].toString())) {
                        List<NacidoVivoReporteDetallado> list = new ArrayList<>(itemsUsuario);
                        Collections.sort(list, COMPARATOR);
                        UsuarioReporteDetallado userRep = new UsuarioReporteDetallado();
                        userRep.setNacVivDetallado(list);
                        userRep.setNombres(items.get(i)[6].toString());
                        userRep.setAgencia(items.get(i)[7].toString());
                        userRep.setCodMSP(items.get(i)[8].toString());
                        userRep.setCedula(items.get(i)[9].toString());
                        userRep.setAnulados(anulados);
                        userRep.setFirmados(firmados);
                        userRep.setNoFirmados(noFirmados);
                        userRep.setIncompletos(incompletos);
                        userRep.setIncompletos(incompletos);
                        userRep.setTotal(list.size());
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
                    Collections.sort(list, COMPARATOR);
                    UsuarioReporteDetallado userRepFinal = new UsuarioReporteDetallado();
                    userRepFinal.setNacVivDetallado(list);
                    userRepFinal.setNombres(items.get(i)[6].toString());
                    userRepFinal.setAgencia(items.get(i)[7].toString());
                    userRepFinal.setCodMSP(items.get(i)[8].toString());
                    userRepFinal.setCedula(items.get(i)[9].toString());
                    userRepFinal.setAnulados(anulados);
                    userRepFinal.setFirmados(firmados);
                    userRepFinal.setNoFirmados(noFirmados);
                    userRepFinal.setIncompletos(incompletos);
                    userRepFinal.setIncompletos(incompletos);
                    userRepFinal.setTotal(list.size());
                    resultado.add(userRepFinal);
                }
            }
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
            if (resultado.size() > 0) {
                Map<String, Object> parametros = new HashMap<String, Object>();
                parametros.put("fechaNacIni", fechaNacIni);
                parametros.put("fechaNacFin", fechaNacFin);
                parametros.put("usuario", userLgn.getNombre() + " " + userLgn.getApellido());
                parametros.put("Institucion", resultado.get(0).getInstitucion());
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
