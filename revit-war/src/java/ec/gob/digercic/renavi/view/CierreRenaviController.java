package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.CierreRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.CierreRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.TipoInscripcionRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.view.util.Meses;
import ec.gob.digercic.renavi.view.util.UtilitarioFechaCalendario;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "cierreRenaviController")
@ViewScoped
public class CierreRenaviController implements Serializable {

    private CierreRenavi current;
    private Date fechaInicio;
    private BigDecimal numPartos;
    private BigDecimal numPartosIndoc;
    private BigDecimal numRevit;
    private BigDecimal numRevitIndoc;
    private BigDecimal numIncomp;
    private BigDecimal numIncompIndoc;
    private BigDecimal numAnulados;
    private BigDecimal numAnuladosIndoc;
    private BigInteger numTotalNac;
    private BigInteger totalNumPartSiste;
    CierreRenavi cierre = new CierreRenavi();
    private TblSauregUsuario usuSesion;
    private TblSauregAgencia agenciaUsu;
    private List<TblSauregAgencia> listAgencias;
    private TblSauregAgencia agen;
    private List<Meses> listMeses;
    private String mes;
    private String anioCierre;
    private String fechaC;
    private Date fechaSistema = new Date();
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

    private int selectedItemIndex;

    public CierreRenaviController() {
    }

    @PostConstruct
    public void init() throws EntidadException {

//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//        HttpSession httpSession = request.getSession(false);
//        setUsuSesion((TblSauregUsuario) httpSession.getAttribute("usuarioSesion"));
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        usuSesion = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
    }

    public Date getFechaSistema() {

        return fechaSistema;

    }

    public TblSauregUsuario getUsuSesion() {
        return usuSesion;
    }

    public void setUsuSesion(TblSauregUsuario usuSesion) {
        this.usuSesion = usuSesion;
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

    public BigDecimal getNumRevit() {
        return numRevit;
    }

    public void setNumRevit(BigDecimal numRevit) {
        this.numRevit = numRevit;
    }

    public BigDecimal getNumIncomp() {
        return numIncomp;
    }

    public void setNumIncomp(BigDecimal numIncomp) {
        this.numIncomp = numIncomp;
    }

    public BigDecimal getNumAnulados() {
        return numAnulados;
    }

    public void setNumAnulados(BigDecimal numAnulados) {
        this.numAnulados = numAnulados;
    }

    public BigInteger getNumTotalNac() {
        return numTotalNac;
    }

    public void setNumTotalNac(BigInteger numTotalNac) {
        this.numTotalNac = numTotalNac;
    }

    public BigDecimal getNumPartosIndoc() {
        return numPartosIndoc;
    }

    public void setNumPartosIndoc(BigDecimal numPartosIndoc) {
        this.numPartosIndoc = numPartosIndoc;
    }

    public BigDecimal getNumRevitIndoc() {
        return numRevitIndoc;
    }

    public void setNumRevitIndoc(BigDecimal numRevitIndoc) {
        this.numRevitIndoc = numRevitIndoc;
    }

    public BigDecimal getNumIncompIndoc() {
        return numIncompIndoc;
    }

    public void setNumIncompIndoc(BigDecimal numIncompIndoc) {
        this.numIncompIndoc = numIncompIndoc;
    }

    public BigDecimal getNumAnuladosIndoc() {
        return numAnuladosIndoc;
    }

    public void setNumAnuladosIndoc(BigDecimal numAnuladosIndoc) {
        this.numAnuladosIndoc = numAnuladosIndoc;
    }

    public TblSauregAgencia getAgen() {
        if (agen == null) {
            agen = new TblSauregAgencia();
        }
        return agen;
    }

    public void setAgen(TblSauregAgencia agen) {
        this.agen = agen;
    }

    public String getAnioCierre() {

        return anioCierre;
    }

    public void setAnioCierre(String anioCierre) {
        this.anioCierre = anioCierre;
    }

    public String getFechaC() {
        return fechaC;
    }

    public void setFechaC(String fechaC) {
        this.fechaC = fechaC;
    }

    public BigInteger getTotalNumPartSiste() {
        return totalNumPartSiste;
    }

    public void setTotalNumPartSiste(BigInteger totalNumPartSiste) {
        this.totalNumPartSiste = totalNumPartSiste;
    }

    public void cambiarAgencia(ValueChangeEvent event) {

        TblSauregAgencia agen = (TblSauregAgencia) event.getNewValue();
        Date fechaOpera = null;
        Date fechaSistema = new Date();
        try {
            fechaOpera = agen.getFechaCreacion();
        } catch (Exception ef) {
            fechaOpera = null;
        }
        List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
        parametros.add(new ParametroConsulta("fkAgenSaureg", agen.getCodMsp().toString()));
        List<CierreRenavi> listCierre = new ArrayList<CierreRenavi>();
        CierreRenavi current1 = new CierreRenavi();
        try {
            listCierre = ejbFacade.consultarTablaResultList("CierreRenavi.findByFkAgenSaureg", parametros);
            if (listCierre.size() > 0) {
                current1 = listCierre.get(0);
            }

        } catch (EntidadException ex) {
            Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
        }

        listMeses = new ArrayList<Meses>();
        if (fechaOpera != null && current1.getFechaUltCierre() == null) {
            int rec = 0;
            fechaSistema = UtilitarioFechaCalendario.aniadirMeses(fechaSistema, -1).getTime();
            while (fechaOpera.before(fechaSistema)) {
                Meses item = new Meses();
                int anio = UtilitarioFechaCalendario.obtenerAnio(fechaOpera);
                String mesAnio = anio + " - " + mesCorrespondencia(UtilitarioFechaCalendario.obtenerMes(fechaOpera));
                item.setIdMes(rec);
                item.setDescripcion(mesAnio);
                listMeses.add(item);
                rec++;
                fechaOpera = UtilitarioFechaCalendario.aniadirMeses(fechaOpera, 1).getTime();
                this.setMes(null);

            }
        } else if (current1.getFechaUltCierre() != null) {
            int rec = 0;
            fechaSistema = UtilitarioFechaCalendario.aniadirMeses(fechaSistema, -1).getTime();
            System.out.println("fechaSistema------->" + fechaSistema);
            current1.setFechaUltCierre(UtilitarioFechaCalendario.aniadirMeses(current1.getFechaUltCierre(), 1).getTime());
            System.out.println("current1.getFechaUltCierre() al principio------->" + current1.getFechaUltCierre());
            while (current1.getFechaUltCierre().before(fechaSistema)) {
                Meses item = new Meses();
                int anio = UtilitarioFechaCalendario.obtenerAnio(current1.getFechaUltCierre());
                String mesAnio = anio + " - " + mesCorrespondencia(UtilitarioFechaCalendario.obtenerMes(current1.getFechaUltCierre()));
                item.setIdMes(rec);
                item.setDescripcion(mesAnio);
                listMeses.add(item);
                rec++;
                current1.setFechaUltCierre(UtilitarioFechaCalendario.aniadirMeses(current1.getFechaUltCierre(), 1).getTime());
                this.setMes(null);

            }
        }
    }

    public String mesCorrespondencia(int mes) {
        String mesStr = "";
        switch (mes) {
            case 0:
                mesStr = "01 [Enero]";
                break;
            case 1:
                mesStr = "02 [Febrero]";
                break;
            case 2:
                mesStr = "03 [Marzo]";
                break;
            case 3:
                mesStr = "04 [Abril]";
                break;
            case 4:
                mesStr = "05 [Mayo]";
                break;
            case 5:
                mesStr = "06 [Junio]";
                break;
            case 6:
                mesStr = "07 [Julio]";
                break;
            case 7:
                mesStr = "08 [Agosto]";
                break;
            case 8:
                mesStr = "09 [Septiembre]";
                break;
            case 9:
                mesStr = "10 [Octubre]";
                break;
            case 10:
                mesStr = "11 [Noviembre]";
                break;
            case 11:
                mesStr = "12 [Diciembre]";
                break;
        }
        return mesStr;
    }

    public List<Meses> getListMeses() {
        if (listMeses == null) {
            listMeses = new ArrayList<Meses>();
        }
        return listMeses;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setListMeses(List<Meses> listMeses) {
        this.listMeses = listMeses;
    }

    public List<TblSauregAgencia> getListAgencias() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//        HttpSession httpSession = request.getSession(false);
//        usuSesion = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
        listAgencias = usuSesion.getListTblSauregAgencia();
        return listAgencias;
    }

    public void setListAgencias(List<TblSauregAgencia> listAgencias) {
        this.listAgencias = listAgencias;
    }

    public CierreRenavi getSelected() {
        if (current == null) {
            current = new CierreRenavi();
            selectedItemIndex = -1;
        }
        return current;
    }

    public CierreRenaviFacadeLocal getEjbFacade() {
        return ejbFacade;
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

    public void limpiar() {
        this.current = null;
        this.numTotalNac = null;
        this.totalNumPartSiste = null;
        this.agen = null;
        this.mes = null;
    }

    public byte[] pdfSinFirma(String id, String ultimoMesCierre, String agencia) throws JRException, IOException, SQLException {
        try {
            // , String estado , String agencia
            System.out.println("ID METODO ------> " + id);
            System.out.println("MES ------> " + ultimoMesCierre);
            //System.out.println("agencia  ------> " + agencia);
            // System.out.println("estado  ------> " + estado);
            //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.24.16.38:1521:INTDB", "revit", "revit");
            //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.91.16.45:1521:intdb", "revit", "revit2015$");
            Connection conn =JsfUtil.getConecction();//DFJ
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("idUsuario", id);
            parametros.put("usuario", current.getUsuario());
            parametros.put("ultimoMesCierre", ultimoMesCierre);
            parametros.put("fechaCierre", current.getFechaCierre());
            parametros.put("agenciaSaureg", agen.getNomAgencia());
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

            //parametros.put("agenciaSaureg",usuSesion.getAgenciaInUser().getNomAgencia());
            //  parametros.put("est",estado);
            reportPath1 = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report70_Cierre.jasper");
            jasperPrint = JasperFillManager.fillReport(reportPath1, parametros, conn);
            pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdf1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    public void saveM() {

        try {
            LogsAcceso log = new LogsAcceso();
            log.setFechaAcceso(new Date());
            log.setUsuario(usuSesion.getNomUsuario());//Username del usuario que genera el log
            log.setAgenId(usuSesion.getAgenciaInUser().getCodMsp());//código de agencia a la que pertenece dicho ususario
            log.setAgenNom(usuSesion.getAgenciaInUser().getNomAgencia());//nombre de agenciaa la que pertenece el usuario
            log.setNomUsu(usuSesion.getNombre());//nombre del usuario
            log.setApeUsu(usuSesion.getApellido());//apellido del usuario
            log.setInstId(usuSesion.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());//código de la institución a la que pertenece el usuario
            log.setInstNombre(usuSesion.getAgenciaInUser().getIdInstituc().getNomInstituc());//nombre de la institución a la que pertenece el usuario
            log.setAccion("REVIT. REGISTRO DE CONTROL. GUARDAR REGISTRO CIERRE");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.getTotalNumPartSiste() != null) {
            if (this.getTotalNumPartSiste().equals(current.getNumPartos())) {
                if (this.getNumTotalNac().equals(current.getNumNacimientos())) {
                    try {
                        byte[] pdf;
                        current.setFkInstSaureg(agen.getIdAgencia().toString());
                        current.setFkAgenSaureg(agen.getCodMsp());
                        //NumTotalCalculado para almacenar el numero total de partos calculados por el sistema
                        current.setNumTotalCalculado(this.getTotalNumPartSiste());
                        current.setUltMesCierre(this.getFechaC());
                        current.setFechaUltCierre(this.getFechaInicio());
                        current.setEstatus(JsfUtil.ESTADO_CERRADO);
                        current.setMesOperacionAgen(agen.getFechaCreacion());
                        current.setFechaCierre(fechaSistema);
                        current.setFkUsuSaureg(usuSesion.getNomUsuario());
                        current.setUsuario(usuSesion.getNombre().concat(" ").concat(usuSesion.getApellido()));
                        current.setNumNacimientosSistema(this.getNumTotalNac());
                        try {
                            pdf = this.pdfSinFirma(current.getFkUsuSaureg(), current.getUltMesCierre(), current.getFkAgenSaureg()); //,current.getEstatus() 
                        } catch (Exception ex) {
                            pdf = null;
                            ex.printStackTrace();
                            Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        current.setPdfSinFirmar(pdf);
                        ejbFacade.create(current);
                        JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistroGuardadoExito"), JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
                        limpiar();
                    } catch (EntidadException ex) {
                        ex.printStackTrace();
                        limpiar();
                        JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorGuardarInfo"), JsfUtil.FATAL_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
                        Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);

                    }
                } else {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorValorNumNacNoCoinciden"), JsfUtil.ERROR_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
                }

            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorValorNumPartNoCoinciden"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
            }

        } else {

            limpiar();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorGuardarInfo"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");

        }
    }

    public void saveM1() {
        try {
            LogsAcceso log = new LogsAcceso();
            log.setFechaAcceso(new Date());
            log.setUsuario(usuSesion.getNomUsuario());//Username del usuario que genera el log
            log.setAgenId(usuSesion.getAgenciaInUser().getCodMsp());//código de agencia a la que pertenece dicho ususario
            log.setAgenNom(usuSesion.getAgenciaInUser().getNomAgencia());//nombre de agenciaa la que pertenece el usuario
            log.setNomUsu(usuSesion.getNombre());//nombre del usuario
            log.setApeUsu(usuSesion.getApellido());//apellido del usuario
            log.setInstId(usuSesion.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());//código de la institución a la que pertenece el usuario
            log.setInstNombre(usuSesion.getAgenciaInUser().getIdInstituc().getNomInstituc());//nombre de la institución a la que pertenece el usuario
            log.setAccion("REVIT. REGISTRO DE CONTROL. GUARDAR REGISTRO");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.getNumTotalNac() != null) {
            try {
                byte[] pdf;
                System.out.println("--------->" + this.getTotalNumPartSiste());
                current.setFkInstSaureg(agen.getIdAgencia().toString());
                current.setFkAgenSaureg(agen.getCodMsp());
                // current.setFkAgenSaureg(agen.getIdAgencia().toString());
                current.setUltMesCierre(this.getFechaC());
                current.setEstatus(JsfUtil.ESTADO_GUARDADO);
                current.setNumTotalCalculado(this.getTotalNumPartSiste());
                current.setMesOperacionAgen(agen.getFechaCreacion());
                current.setFechaCierre(fechaSistema);
                current.setFechaUltCierre(this.getFechaInicio());
                current.setFkUsuSaureg(usuSesion.getNomUsuario());
                current.setUsuario(usuSesion.getNombre().concat(" ").concat(usuSesion.getApellido()));
                current.setNumNacimientosSistema(this.getNumTotalNac());
                System.out.println("iD ------->" + current.getFkUsuSaureg());
                System.out.println("iD ------->" + current.getUltMesCierre());
                System.out.println("iD ------->" + current.getFkAgenSaureg());
                System.out.println("iD ------->" + current.getEstatus());
                try {
                    pdf = this.pdfSinFirma(current.getFkUsuSaureg(), current.getUltMesCierre(), current.getFkAgenSaureg()); //,current.getEstatus()
                } catch (Exception ex) {
                    pdf = null;
                    ex.printStackTrace();
                    Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                }
                current.setPdfSinFirmar(pdf);
                ejbFacade.create(current);
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistroGuardadoExito"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
                limpiar();

            } catch (EntidadException ex) {
                ex.printStackTrace();
                limpiar();
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorGuardarInfo"), JsfUtil.FATAL_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
                Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else {
            limpiar();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorGuardarInfo"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
        }

    }

    public String create() {
        try {
            getEjbFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CierreRenaviCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String update() {
        try {
            getEjbFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CierreRenaviUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    private void destroy() {
        try {
            ejbFacade.remove(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("EliminacionRegistro"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoEliminacionRegistro"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        try {
            return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        try {
            return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
    }

    public void calcularCierre(ValueChangeEvent event) {
        try {
            if (event.getNewValue() != null) {
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                String value = event.getNewValue().toString();
                System.out.println("value---->" + value);
                int posicion = Integer.parseInt(value.substring(0, value.indexOf(".-")));
                System.out.println("posicion------->" + posicion);
                fechaC = value.substring(value.indexOf(".-") + 3, value.length());

                System.out.println("fechaC------->" + fechaC);
                String anioCierre = fechaC.substring(0, 4);
                String mesCierre = fechaC.substring(7, 9);
                System.out.println("anio------->" + anioCierre);
                System.out.println("mes------->" + mesCierre);
                String dia1 = "01/";
                String fechaUltimoCierre = dia1.concat(mesCierre).concat("/").concat(anioCierre);
                System.out.println("fecha Ult cierr " + fechaUltimoCierre);
                if (posicion <= 0) {
                    try {
                        this.setFechaInicio(format1.parse(fechaUltimoCierre));
                    } catch (ParseException ex) {
                        Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    StringBuilder valor = new StringBuilder();
                    System.out.println("------>" + this.getFechaInicio());
                    this.getPrimerDiaDelMes();
                    this.getUltimoDiaDelMes();
                    valor.append("SELECT * FROM (");
                     //Numero de partos en el sistema, Estado Firmado, FK_ID_TIPO_IDENT = 1
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
                    valor.append(agen.getCodMsp());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("')");
                    valor.append(" UNION");
                    //Numero de Nacimientos, Firmados, FK_ID_TIPO_IDENT = 1
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
                    valor.append(agen.getCodMsp());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("')");
                    valor.append(" UNION");
                    //Numero registros imcompletos, no firmados, FK_ID_TIPO_IDENT = 1
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
                    valor.append(agen.getCodMsp());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("')");
                    valor.append(" UNION");
                    //Anulados
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
                    valor.append(agen.getCodMsp());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("')");
                    valor.append(" UNION");
                     ////Numero de partos en el sistema, Estado Firmado, FK_ID_TIPO_IDENT = 2
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
                    valor.append(agen.getCodMsp());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("' AND n.fk_usu_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
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
                    valor.append(agen.getCodMsp());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("' AND n.fk_usu_saureg = '");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("')");
                    valor.append(" UNION");
                    //Numero registros imcompletos, no firmados, FK_ID_TIPO_IDENT = 2
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
                    valor.append(agen.getCodMsp());
                    valor.append(" AND ( n.fk_usu_firma_saureg ='");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("' AND n.fk_usu_saureg = '");
                    valor.append(usuSesion.getNomUsuario());
                    valor.append("')");
                    valor.append(") b ORDER BY b.id");
                    System.out.println("SQL-------->" + valor.toString());

                    obj = ejbFacade.executeNativeQueryListResult(valor.toString());

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
                    current.setNumRegFisicos(new BigInteger(String.valueOf(0)));
                    current.setDiferNacViv(new BigInteger(String.valueOf(0)));
                    this.setNumTotalNac(current.getNumNacVivFe().add(current.getNumNacVivFeIndoc()).add(current.getNumRegFisicos()).add(current.getNumRegIncompletos()).add(current.getNumRegIncompletosIndoc()).add(current.getDiferNacViv()));

                } else {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorMensajeMesSeleccionado"), JsfUtil.FATAL_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
                    limpiar();
                }
            }
        } catch (EntidadException ex) {
            ex.printStackTrace();
            Logger.getLogger(CierreRenaviController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void calcularTotalPartos() {

        if (current.getDiferPartos() == null || current.getDiferPartos().equals(new BigInteger("0")) ) {
            current.setDiferPartos(new BigInteger(String.valueOf(0)));
            current.setObservacion(null);
            if ((current.getNumPartosSistema() != null) && (current.getNumPartosSisIndoc() != null)) {
                this.setTotalNumPartSiste(current.getNumPartosSistema().add(current.getNumPartosSisIndoc()).add(current.getDiferPartos()));
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorMensajeMesSeleccionado"), JsfUtil.FATAL_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
            }
        } else {
            if ((current.getNumPartosSistema() != null) && (current.getNumPartosSisIndoc() != null)) {
                this.setTotalNumPartSiste(current.getNumPartosSistema().add(current.getNumPartosSisIndoc()).add(current.getDiferPartos()));
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorMensajeMesSeleccionado"), JsfUtil.FATAL_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
            }
        }

    }

    public void calcularTotalNac() {

        if ((current.getDiferNacViv() == null || current.getDiferNacViv().equals(new BigInteger("0")) )) {
            current.setDiferNacViv(new BigInteger(String.valueOf(0)));
            current.setObservacionInc(null);

            System.out.println("---> current.getDiferNacViv() " + current.getDiferNacViv());
            System.out.println("-----> numFi" + current.getNumRegFisicos());
            if ((current.getNumNacVivFe() != null) && (current.getNumNacVivFeIndoc() != null) && (current.getNumRegIncompletos() != null) && (current.getNumRegIncompletosIndoc() != null)) {
                this.setNumTotalNac(current.getNumNacVivFe().add(current.getNumNacVivFeIndoc()).add(current.getNumRegFisicos()).add(current.getNumRegIncompletos()).add(current.getNumRegIncompletosIndoc()).add(current.getDiferNacViv()));
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorMensajeMesSeleccionado"), JsfUtil.FATAL_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
            }
        } else {
            if ((current.getNumNacVivFe() != null) && (current.getNumNacVivFeIndoc() != null) && (current.getNumRegIncompletos() != null) && (current.getNumRegIncompletosIndoc() != null)) {
                System.out.println("-----> numFi" + current.getNumRegFisicos());
                this.setNumTotalNac(current.getNumNacVivFe().add(current.getNumNacVivFeIndoc()).add(current.getNumRegFisicos()).add(current.getNumRegIncompletos()).add(current.getNumRegIncompletosIndoc()).add(current.getDiferNacViv()));
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorMensajeMesSeleccionado"), JsfUtil.FATAL_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cierre:mssgsBusquedaM");
            }
        }
    }

    public Date getPrimerDiaDelMes() {
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fechatemp = sdf.format(this.getFechaInicio());
            Date date = sdf.parse(fechatemp);

            cal.setTime(date);

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

    @FacesConverter(forClass = TipoInscripcionRenavi.class)

    public static class TipoInscripcionRenaviControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try {
                if (value == null || value.length() == 0) {
                    return null;
                }
                CierreRenaviController controller = (CierreRenaviController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "cierreRenaviController");
                return controller.ejbFacade.find(getKey(value));
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        java.math.BigDecimal getKey(String value) {
            java.math.BigDecimal key;
            key = new java.math.BigDecimal(value);
            return key;
        }

        String getStringKey(java.math.BigDecimal value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TipoInscripcionRenavi) {
                CierreRenavi o = (CierreRenavi) object;
                return getStringKey(o.getIdCierre());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoInscripcionRenavi.class.getName());
            }
        }

    }
}
