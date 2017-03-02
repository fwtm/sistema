/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.CierreRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.CierreRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.entities.TblSauregAgencia;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.context.RequestContext;

/**
 *
 * @author henry.aguilar
 */
@ManagedBean(name = "cierreRenaviEVController")
@ViewScoped
public class CierreRenaviEVController implements Serializable {

    private CierreRenavi current;
    private TblSauregUsuario userLgn;
    private TblSauregAgencia agencia;
    private Long id;
    private Date fechaInicio;

    private BigDecimal numPartos;
    private BigDecimal numPartosIndoc;
    private BigDecimal numRevit;
    private BigDecimal numRevitIndoc;
    private BigDecimal numIncomp;
    private BigDecimal numIncompIndoc;
    private BigDecimal numAnulados;
    private BigDecimal numAnuladosIndoc;
    private BigInteger totalNumPartSiste;
    byte[] pdf1;
    String reportPath1;
    private JasperPrint jasperPrint;
    List<Object[]> obj = new ArrayList<Object[]>();
    @EJB
    private ec.gob.digercic.renavi.ejb.CierreRenaviFacadeLocal ejbFacade;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregAgenciaFacadeLocal ejbFacadeAgencia;
    @EJB
    private LogAccesosRenaviFacadeLocal ejbLogs;

    public CierreRenaviEVController() {
    }

    @PostConstruct
    public void iniciar() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            setUserLgn((TblSauregUsuario) httpSession.getAttribute("usuarioSesion"));
            
            System.out.println("---> entra a edid del ev controler");
            String cedUsu = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fkUsuSaureg");
            id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fkAgenciaSaureg"));
            String mesCierre = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("ultMesCierre");
//            ParametroConsulta paramAgencia = new ParametroConsulta("fkAgenciaSaureg", userLgn.getAgenciaInUser().getIdAgencia().toString());
            agencia = ejbFacadeAgencia.find(id);

            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            parametros.add(new ParametroConsulta("fkAgenciaSaureg", agencia.getCodMsp()));
            parametros.add(new ParametroConsulta("fkUsuSaureg", cedUsu));
//            parametros.add(paramAgencia);
//            parametros.add(new ParametroConsulta("fkAgenciaSaureg", agencia));
            parametros.add(new ParametroConsulta("ultMesCierre", mesCierre));
            parametros.add(new ParametroConsulta("status", JsfUtil.ESTADO_GUARDADO));
            //current = getEjbFacade().consultarTablaResultList("CierreRenavi.findByFkAgenSaureg", parametros).get(0);
            current = (CierreRenavi) getEjbFacade().consultarTablaSingleResult("CierreRenavi.findAllByUsuarioUltMesCierre", parametros);

            this.calcularCierre(current.getFechaUltCierre());
        } catch (EntidadException ex) {
            ex.printStackTrace();
            Logger.getLogger(CierreRenaviEVController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TblSauregUsuario getUserLgn() {
        return userLgn;
    }

    public void setUserLgn(TblSauregUsuario userLgn) {
        this.userLgn = userLgn;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public BigDecimal getNumPartos() {
        return numPartos;
    }

    public void setNumPartos(BigDecimal numPartos) {
        this.numPartos = numPartos;
    }

    public BigDecimal getNumPartosIndoc() {
        return numPartosIndoc;
    }

    public void setNumPartosIndoc(BigDecimal numPartosIndoc) {
        this.numPartosIndoc = numPartosIndoc;
    }

    public BigDecimal getNumRevit() {
        return numRevit;
    }

    public void setNumRevit(BigDecimal numRevit) {
        this.numRevit = numRevit;
    }

    public BigDecimal getNumRevitIndoc() {
        return numRevitIndoc;
    }

    public void setNumRevitIndoc(BigDecimal numRevitIndoc) {
        this.numRevitIndoc = numRevitIndoc;
    }

    public BigDecimal getNumIncomp() {
        return numIncomp;
    }

    public void setNumIncomp(BigDecimal numIncomp) {
        this.numIncomp = numIncomp;
    }

    public BigDecimal getNumIncompIndoc() {
        return numIncompIndoc;
    }

    public void setNumIncompIndoc(BigDecimal numIncompIndoc) {
        this.numIncompIndoc = numIncompIndoc;
    }

    public BigDecimal getNumAnulados() {
        return numAnulados;
    }

    public void setNumAnulados(BigDecimal numAnulados) {
        this.numAnulados = numAnulados;
    }

    public BigDecimal getNumAnuladosIndoc() {
        return numAnuladosIndoc;
    }

    public void setNumAnuladosIndoc(BigDecimal numAnuladosIndoc) {
        this.numAnuladosIndoc = numAnuladosIndoc;
    }

    public TblSauregAgencia getAgencia() {
        return agencia;
    }

    public void setAgencia(TblSauregAgencia agencia) {
        this.agencia = agencia;
    }

    public CierreRenavi getSelected() {
        return current;
    }

    public CierreRenaviFacadeLocal getEjbFacade() {
        return ejbFacade;
    }

    public BigInteger getTotalNumPartSiste() {
        return totalNumPartSiste;
    }

    public void setTotalNumPartSiste(BigInteger totalNumPartSiste) {
        this.totalNumPartSiste = totalNumPartSiste;
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
        return "Edit";
    }

    public String prepareFind() {
        return "Find";
    }

    public void calcularTotalPartos() {
        if (current.getDiferPartos() == null || current.getDiferPartos().equals( new BigInteger("0"))) {
            current.setDiferPartos(new BigInteger(String.valueOf(0)));
            current.setNumTotalCalculado(current.getNumPartosSistema().add(current.getNumPartosSisIndoc()).add(current.getDiferPartos()));
            current.setObservacion(null);
        } else {
            current.setNumTotalCalculado(current.getNumPartosSistema().add(current.getNumPartosSisIndoc()).add(current.getDiferPartos()));
        }

    }

    public void calcularTotalNac() {
        if (current.getDiferNacViv() == null || current.getDiferNacViv().equals(new BigInteger("0"))) {
            current.setDiferNacViv(new BigInteger(String.valueOf(0)));
            System.out.println("---> current.getDiferNacViv() " + current.getDiferNacViv());
            System.out.println("-----> numFi" + current.getNumRegFisicos());
            current.setNumNacimientosSistema(current.getNumNacVivFe().add(current.getNumNacVivFeIndoc()).add(current.getNumRegFisicos()).add(current.getNumRegIncompletos()).add(current.getNumRegIncompletosIndoc()).add(current.getDiferNacViv()));
            current.setObservacionInc(null);
        } else {
            System.out.println("-----> numFi" + current.getNumRegFisicos());
            current.setNumNacimientosSistema(current.getNumNacVivFe().add(current.getNumNacVivFeIndoc()).add(current.getNumRegFisicos()).add(current.getNumRegIncompletos()).add(current.getNumRegIncompletosIndoc()).add(current.getDiferNacViv()));
        }
    }

    public void calcularCierre(Date fechaUltCierre) {
        try {
            if (fechaUltCierre != null) {
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

                this.setFechaInicio(fechaUltCierre);

                StringBuilder valor = new StringBuilder();
                System.out.println("------>" + this.getFechaInicio());
                this.getPrimerDiaDelMes();
                this.getUltimoDiaDelMes();
                valor.append("SELECT * FROM (");
                    valor.append("SELECT COUNT(n.NUM_PARTO_SISTEMA),'1' AS id FROM NACIDO_VIVO_RENAVI n INNER JOIN MADRE_RENAVI m ON n.FK_CEDUL_MAD = m.ID_MAD WHERE n.FK_ID_EST_FIR =");
                    valor.append(JsfUtil.STAT_FIR_CON);
                    valor.append(" AND n.FK_ID_EST_REG IN (");
                    valor.append(JsfUtil.STAT_DAT_DOCTOR);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_DAT_DIGERCIG);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_ANULADO_FE);
                    valor.append(")");
                    valor.append(" AND (n.FECHA_NACIM_NAC_VIV BETWEEN to_date('");
                    valor.append(format1.format(this.getPrimerDiaDelMes()));
                    valor.append("','dd/MM/yyyy') AND to_date('");
                    valor.append(format1.format(this.getUltimoDiaDelMes()));
                    valor.append("','dd/MM/yyyy')) AND m.FK_ID_TIPO_IDENT = 1 ");
                    valor.append("AND n.FK_AGENCIA_SAUREG =");
                    valor.append(current.getFkAgenSaureg());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("')");
                    valor.append(" UNION");
                    valor.append(" SELECT count(*),'2' AS id FROM NACIDO_VIVO_RENAVI n INNER JOIN MADRE_RENAVI m ON n.FK_CEDUL_MAD = m.ID_MAD WHERE n.FK_ID_EST_FIR =");
                    valor.append(JsfUtil.STAT_FIR_CON);
                    valor.append(" AND n.FK_ID_EST_REG IN (");
                    valor.append(JsfUtil.STAT_DAT_DOCTOR);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_DAT_DIGERCIG);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_ANULADO_FE);
                    valor.append(")");
                    valor.append(" AND (n.FECHA_NACIM_NAC_VIV BETWEEN to_date('");
                    valor.append(format1.format(this.getPrimerDiaDelMes()));
                    valor.append("','dd/MM/yyyy') AND to_date('");
                    valor.append(format1.format(this.getUltimoDiaDelMes()));
                    valor.append("','dd/MM/yyyy')) AND m.FK_ID_TIPO_IDENT = 1 ");
                    valor.append("AND n.FK_AGENCIA_SAUREG =");
                    valor.append(current.getFkAgenSaureg());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("')");
                    valor.append(" UNION");
                    valor.append(" SELECT count(*),'3' AS id FROM NACIDO_VIVO_RENAVI n INNER JOIN MADRE_RENAVI m ON n.FK_CEDUL_MAD = m.ID_MAD WHERE n.FK_ID_EST_FIR =");
                    valor.append(JsfUtil.STAT_FIR_SIN);
                    valor.append(" AND n.FK_ID_EST_REG IN(");
                    valor.append(JsfUtil.STAT_DAT_MADRE);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_DAT_NACVIVO);
//                    valor.append(",");
//                    valor.append(JsfUtil.STAT_DAT_DOCTOR);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_PEND_EDIT);
                    valor.append(")");
                    valor.append(" AND (n.FECHA_NACIM_NAC_VIV BETWEEN to_date('");
                    valor.append(format1.format(this.getPrimerDiaDelMes()));
                    valor.append("','dd/MM/yyyy') AND to_date('");
                    valor.append(format1.format(this.getUltimoDiaDelMes()));
                    valor.append("','dd/MM/yyyy')) AND m.FK_ID_TIPO_IDENT = 1");
                    valor.append(" AND n.FK_AGENCIA_SAUREG =");
                    valor.append(current.getFkAgenSaureg());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("')");
                    valor.append(" UNION");
                    valor.append(" SELECT count(*),'4' AS id FROM NACIDO_VIVO_RENAVI n WHERE (n.FK_ID_EST_FIR =");
                    valor.append(JsfUtil.STAT_FIR_CON);
                     valor.append(" OR n.FK_ID_EST_FIR = ");
                    valor.append(JsfUtil.STAT_FIR_SIN);
                    valor.append(")");
                    valor.append(" AND n.FK_ID_EST_REG =");
                    valor.append(JsfUtil.STAT_ANULADO);
                    valor.append(" AND (n.FECHA_NACIM_NAC_VIV BETWEEN to_date('");
                    valor.append(format1.format(this.getPrimerDiaDelMes()));
                    valor.append("','dd/MM/yyyy') AND to_date('");
                    valor.append(format1.format(this.getUltimoDiaDelMes()));
                    valor.append("','dd/MM/yyyy'))");
                    valor.append(" AND n.FK_AGENCIA_SAUREG =");
                    valor.append(current.getFkAgenSaureg());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("')");
                    valor.append(" UNION");
                    valor.append(" SELECT COUNT(n.NUM_PARTO_SISTEMA),'5' AS id FROM NACIDO_VIVO_RENAVI n INNER JOIN MADRE_RENAVI m ON n.FK_CEDUL_MAD = m.ID_MAD WHERE n.FK_ID_EST_FIR =");
                    valor.append(JsfUtil.STAT_FIR_CON);
                    valor.append(" AND n.FK_ID_EST_REG IN (");
                    valor.append(JsfUtil.STAT_DAT_DOCTOR);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_DAT_DIGERCIG);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_ANULADO_FE);
                    valor.append(")");
                    valor.append(" AND (n.FECHA_NACIM_NAC_VIV BETWEEN to_date('");
                    valor.append(format1.format(this.getPrimerDiaDelMes()));
                    valor.append("','dd/MM/yyyy') AND to_date('");
                    valor.append(format1.format(this.getUltimoDiaDelMes()));
                    valor.append("','dd/MM/yyyy')) AND m.FK_ID_TIPO_IDENT = 2");
                    valor.append(" AND n.FK_AGENCIA_SAUREG =");
                    valor.append(current.getFkAgenSaureg());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("')");
                    valor.append(" UNION");
                    valor.append(" SELECT count(*),'6' AS id FROM NACIDO_VIVO_RENAVI n INNER JOIN MADRE_RENAVI m ON n.FK_CEDUL_MAD = m.ID_MAD WHERE n.FK_ID_EST_FIR =");
                    valor.append(JsfUtil.STAT_FIR_CON);
                    valor.append(" AND n.FK_ID_EST_REG IN (");
                    valor.append(JsfUtil.STAT_DAT_DOCTOR);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_DAT_DIGERCIG);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_ANULADO_FE);
                    valor.append(")");
                    valor.append(" AND (n.FECHA_NACIM_NAC_VIV BETWEEN to_date('");
                    valor.append(format1.format(this.getPrimerDiaDelMes()));
                    valor.append("','dd/MM/yyyy') AND to_date('");
                    valor.append(format1.format(this.getUltimoDiaDelMes()));
                    valor.append("','dd/MM/yyyy')) AND m.FK_ID_TIPO_IDENT = 2");
                    valor.append(" AND n.FK_AGENCIA_SAUREG =");
                    valor.append(current.getFkAgenSaureg());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("')");
                    valor.append(" UNION");
                    valor.append(" SELECT count(*),'7' AS id FROM NACIDO_VIVO_RENAVI n INNER JOIN MADRE_RENAVI m ON n.FK_CEDUL_MAD = m.ID_MAD WHERE n.FK_ID_EST_FIR =");
                    valor.append(JsfUtil.STAT_FIR_SIN);
                    valor.append(" AND n.FK_ID_EST_REG IN(");
                    valor.append(JsfUtil.STAT_DAT_MADRE);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_DAT_NACVIVO);
//                    valor.append(",");
//                    valor.append(JsfUtil.STAT_DAT_DOCTOR);
                    valor.append(",");
                    valor.append(JsfUtil.STAT_PEND_EDIT);
                    valor.append(")");
                    valor.append(" AND (n.FECHA_NACIM_NAC_VIV BETWEEN to_date('");
                    valor.append(format1.format(this.getPrimerDiaDelMes()));
                    valor.append("','dd/MM/yyyy') AND to_date('");
                    valor.append(format1.format(this.getUltimoDiaDelMes()));
                    valor.append("','dd/MM/yyyy')) AND m.FK_ID_TIPO_IDENT = 2");
                    valor.append(" AND n.FK_AGENCIA_SAUREG =");
                    valor.append(current.getFkAgenSaureg());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("' AND n.fk_usu_saureg = '");
                    valor.append(current.getFkUsuSaureg());
                    valor.append("')");
                    valor.append(") b ORDER BY b.id");
                System.out.println("SQL-------->" + valor.toString());

                obj = ejbFacade.executeNativeQueryListResult(valor.toString());

                System.out.println("obj.get(0)[0]-------->" + obj.get(0)[0]);
                System.out.println("obj.get(1)[0]-------->" + obj.get(1)[0]);
                this.setNumPartos((BigDecimal) obj.get(0)[0]);
                current.setNumPartosSistema(this.getNumPartos().toBigInteger());
                this.setNumRevit((BigDecimal) obj.get(1)[0]);
                current.setNumNacVivFe(this.getNumRevit().toBigInteger());
                this.setNumIncomp((BigDecimal) obj.get(2)[0]);
                current.setNumRegIncompletos(this.getNumIncomp().toBigInteger());
                this.setNumAnulados((BigDecimal) obj.get(3)[0]);
                current.setNumRegAnulNacVivFe(this.getNumAnulados().toBigInteger());
                this.setNumPartosIndoc((BigDecimal) obj.get(4)[0]);
                current.setNumPartosSisIndoc(this.getNumPartosIndoc().toBigInteger());
                this.setNumRevitIndoc((BigDecimal) obj.get(5)[0]);
                current.setNumNacVivFeIndoc(this.getNumRevitIndoc().toBigInteger());
                this.setNumIncompIndoc((BigDecimal) obj.get(6)[0]);
                current.setNumRegIncompletosIndoc(this.getNumIncompIndoc().toBigInteger());

            }
        } catch (EntidadException ex) {
            ex.printStackTrace();
            Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Date getPrimerDiaDelMes() {
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fechatemp = sdf.format(this.getFechaInicio());
            Date date = sdf.parse(fechatemp);
//            String dateInString = "22-01-2015 10:20:56";
//            Date date = sdf.parse(dateInString);
            cal.setTime(date);

//            Calendar cal = Calendar.getInstance();
//            cal.setTime(this.getFechaInicio());
            cal.set(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.getActualMinimum(Calendar.DAY_OF_MONTH),
                    cal.getMinimum(Calendar.HOUR_OF_DAY),
                    cal.getMinimum(Calendar.MINUTE),
                    cal.getMinimum(Calendar.SECOND));
            System.out.println("primer dia del mes ------>" + cal.getTime());

        } catch (ParseException ex) {
            Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return cal.getTime();

    }

    public Date getUltimoDiaDelMes() {
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fechatemp = sdf.format(this.getFechaInicio());
            Date date = sdf.parse(fechatemp);
            cal.setTime(date);

            cal.set(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.getActualMaximum(Calendar.DAY_OF_MONTH),
                    cal.getMaximum(Calendar.HOUR_OF_DAY),
                    cal.getMaximum(Calendar.MINUTE),
                    cal.getMaximum(Calendar.SECOND));
            System.out.println("ultimo dia del mes ------>" + cal.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return cal.getTime();
    }

    public byte[] pdfSinFirma(String id, String ultimoMesCierre, String agencia) throws JRException, IOException, SQLException {
        try {
            //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.24.16.38:1521:INTDB", "revit", "revit");
            Connection conn =JsfUtil.getConecction();//DFJ
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("idUsuario", id);
            parametros.put("usuario", current.getUsuario());
            parametros.put("ultimoMesCierre", ultimoMesCierre);
            parametros.put("fechaCierre", current.getFechaCierre());
            parametros.put("agenciaSaureg", agencia);
            parametros.put("fkUsuSaureg", current.getFkUsuSaureg());
            parametros.put("numPartos", current.getNumPartos());
            parametros.put("numPartosSistema", current.getNumPartosSistema());
            parametros.put("numTotalCalculado", current.getNumTotalCalculado());
            parametros.put("numPartosSisIndoc", current.getNumPartosSisIndoc());
            parametros.put("diferPartos", current.getDiferPartos());
            parametros.put("observacion", current.getObservacion());
            parametros.put("numNacimientos", current.getNumNacimientos());
            parametros.put("numNacVivFe", current.getNumNacVivFe());
            parametros.put("numNacVivFeIndoc", current.getNumNacVivFeIndoc());
            parametros.put("numRegFisicos", current.getNumRegFisicos());
            parametros.put("numRegIncompletos", current.getNumRegIncompletos());
            parametros.put("numRegIncompletosIndoc", current.getNumRegIncompletosIndoc());
            parametros.put("diferNacViv", current.getDiferNacViv());
            parametros.put("numRegAnulNacVivFe", current.getNumRegAnulNacVivFe());
            parametros.put("numNacimientosSistema", current.getNumNacimientosSistema());
            parametros.put("observacionInc", current.getObservacionInc());
           

            reportPath1 = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report70_Cierre.jasper");
            jasperPrint = JasperFillManager.fillReport(reportPath1, parametros, conn);
            pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return pdf1;

    }

    public void editar() {
        //this.getTotalNumPartSiste()
        try{
            LogsAcceso log = new LogsAcceso();
            log.setFechaAcceso(new Date());
            log.setUsuario(userLgn.getNomUsuario());//Username del usuario que genera el log
            log.setAgenId(userLgn.getAgenciaInUser().getCodMsp());//código de agencia a la que pertenece dicho ususario
            log.setAgenNom(userLgn.getAgenciaInUser().getNomAgencia());//nombre de agenciaa la que pertenece el usuario
            log.setNomUsu(userLgn.getNombre());//nombre del usuario
            log.setApeUsu(userLgn.getApellido());//apellido del usuario
            log.setInstId(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());//código de la institución a la que pertenece el usuario
            log.setInstNombre(userLgn.getAgenciaInUser().getIdInstituc().getNomInstituc());//nombre de la institución a la que pertenece el usuario
            log.setAccion("REVIT. REGISTRIO DE CONTROL. GUARDAR REGISTRO CIERRE");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        }catch(EntidadException e){
            e.printStackTrace();
        }
        System.out.println("------>" + current.getNumTotalCalculado());
        if (current.getNumTotalCalculado().equals(current.getNumPartos())) {
            if (current.getNumNacimientosSistema().equals(current.getNumNacimientos())) {
                try {
                    byte[] pdf;
                    System.out.println("ENTRO EN EDIT");
                    fechaInicio = new Date();
                    current.setFechaCierre(fechaInicio);
                    current.setEstatus(JsfUtil.ESTADO_CERRADO);
                    try {
                        pdf = this.pdfSinFirma(current.getFkUsuSaureg(), current.getUltMesCierre(), current.getFkAgenSaureg());;
                    } catch (Exception ex) {
                        pdf = null;
                        ex.printStackTrace();
                        Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    current.setPdfSinFirmar(pdf);
                    getEjbFacade().edit(current);
                    FacesContext contex = FacesContext.getCurrentInstance();
                    contex.getExternalContext().redirect("Find.jsf");
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistroGuardadoExito"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");

                } catch (EntidadException ex) {
                    ex.printStackTrace();
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorGuardarInfo"), JsfUtil.FATAL_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
                    Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CierreRenaviEVController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorValorNumNacNoCoinciden"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
            }
        } else {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorValorNumPartNoCoinciden"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
        }

    }
}
