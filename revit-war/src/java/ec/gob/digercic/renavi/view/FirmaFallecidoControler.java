/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.FallecidoFacadeLocal;
import ec.gob.digercic.renavi.entities.Fallecido;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ws.cedula.Cedula;
import ec.gob.digercic.renavi.ws.firma.Firmado_Service;
import ec.gob.digercic.renavi.ws.qrbc.Barras;
import ec.gob.digercic.renavi.ws.qrbc.GeneratorCode;
import ec.gob.digercic.renavi.ws.qrbc.QR;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import ec.gob.digercic.saureg.ejb.TblSauregUsuarioFacadeLocal;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
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
@ManagedBean(name = "firmaFallecidoControler")
@ViewScoped
public class FirmaFallecidoControler implements Serializable {

    private List<Fallecido> listFallecidoPorFirmar;
    private Date fechaActual;
    private Date fechaPreFirma;
    private Date fechaInicio;
    private Date fechaFin;
    @EJB
    private FallecidoFacadeLocal ejbFallecidoFacade;
    @EJB
    private TblSauregUsuarioFacadeLocal ejbUsuarioFacade;
    @EJB
    private ec.gob.digercic.renavi.ejb.VariableRenaviFacadeLocal ejbFacadeVariable;
    private List<Fallecido> listSelectFallecido;
    private TblSauregUsuario userLgn;
    private JasperPrint jasperPrint;
    private Short registrosVencidos;
    private Short registrosPorVencer;
    List<Fallecido> listFallecidos;
    byte[] pdf1;

    public FirmaFallecidoControler() {
    }

    public void actualizaTablaPorFirmar(ActionEvent event) {
        buscarFallecidosNoFirmados();
        RequestContext.getCurrentInstance().update("form:checkboxDT");
        RequestContext.getCurrentInstance().update("form:porVencer");
    }

    @PostConstruct
    public void iniciar() throws ParseException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            //Obtengo la fecha actual del sistema
            fechaActual = new Date();
            fechaFin = fechaActual;
            //Obtengo el tiempo preFirma
            Long tp = new Long(ejbFacadeVariable.find(3).getVarValor());
            fechaPreFirma = new Date(fechaActual.getTime() - tp);
            fechaInicio = fechaPreFirma;
            //Busco los nacidos vivos no firmados
            buscarFallecidosNoFirmados();
            //Cuento los registros vencidos a la fecha de hoy
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //formateo la fecha prefirma para que sea a las 23:59:59
            fechaPreFirma = new Date(sdf.parse(sdf.format(fechaPreFirma)).getTime() + 86399000L);
            StringBuilder sqlVencidos = new StringBuilder("select count(*), n.fk_usu_saureg from nacido_vivo_renavi n, madre_renavi m ");
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
            try {
                itemsObjectFDatos = ejbFallecidoFacade.executeNativeQueryListResult(sqlVencidos.toString());
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

    public void buscarFallecidosNoFirmados() {
        try {
            if (userLgn.getAgenciaInUser().getCodMsp() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fi = sdf.format(fechaInicio);
                fechaInicio = sdf.parse(fi);
                String ff = sdf.format(fechaFin);
                fechaFin = sdf.parse(ff);
                fechaFin = new Date(fechaFin.getTime() + 86399000L);
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                ParametroConsulta param = new ParametroConsulta("fechaInicio", fechaInicio);
                //ParametroConsulta param = new ParametroConsulta("fechaInicio", fi);
                parametros.add(param);
                param = new ParametroConsulta("fechaFin", fechaFin);
                //param = new ParametroConsulta("fechaFin", ff);
                parametros.add(param);
                ParametroConsulta paramInstitucion = new ParametroConsulta("idInstitucion", userLgn.getAgenciaInUser().getCodMsp());
                parametros.add(paramInstitucion);
                ParametroConsulta paramEstadoF = new ParametroConsulta("statusF", JsfUtil.ESTADO_REG_ACTIVO);
                parametros.add(paramEstadoF);
                ParametroConsulta paramEstadoFirma = new ParametroConsulta("estFirma", JsfUtil.STAT_FIR_SIN);
                parametros.add(paramEstadoFirma);
                //Parametro para estado de registro = 5 (DIGERCIC)
                List<String> listEstados = new ArrayList<>();
                listEstados.add("4");
                listEstados.add("8");
                ParametroConsulta paramEstadoRegistro = new ParametroConsulta("estRegistro", listEstados);
                parametros.add(paramEstadoRegistro);
                ParametroConsulta usuario = new ParametroConsulta("usuario", userLgn.getNomUsuario());
                parametros.add(usuario);
                listFallecidoPorFirmar = ejbFallecidoFacade.consultarTablaResultList("Fallecido.findInFechaCreacion", parametros);
                if (listFallecidoPorFirmar.isEmpty()) {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("messages");
                    registrosPorVencer = 0;
                }
                StringBuilder sqlPorVencer = new StringBuilder("select count(*), f.fk_usu_saureg from fallecido f ");
                sqlPorVencer.append("where to_date(to_char(f.fecha_creacion_fal, 'dd/MM/yyyy'),'dd/MM/yyyy') = to_date('");
                sqlPorVencer.append(sdf.format(fechaPreFirma));
                sqlPorVencer.append("','dd/MM/yyyy') ");
                sqlPorVencer.append("and f.status_fal = 'A' ");
                sqlPorVencer.append("and f.fk_usu_firma_saureg = '");
                sqlPorVencer.append(userLgn.getNomUsuario());
                sqlPorVencer.append("' and f.fk_agencia_firma_saureg = ");
                sqlPorVencer.append(userLgn.getAgenciaInUser().getCodMsp());
                sqlPorVencer.append(" and f.fk_id_est_reg_fal =");
                sqlPorVencer.append(JsfUtil.STAT_DAT_DOCTOR);
                sqlPorVencer.append(" and f.fk_id_estado_firma =");
                sqlPorVencer.append(JsfUtil.STAT_FIR_SIN);
                sqlPorVencer.append(" and f.pdf_sin_firma is not null ");
                sqlPorVencer.append("group by f.fk_usu_saureg");
                List<Object[]> itemsObjectFDatos = new ArrayList<Object[]>();
                try {
                    itemsObjectFDatos = ejbFallecidoFacade.executeNativeQueryListResult(sqlPorVencer.toString());
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
     * @return the listFallecidoPorFirmar
     */
    public List<Fallecido> getListFallecidoPorFirmar() {
        //buscarNacidoVivosDefault() ;
        return listFallecidoPorFirmar;
    }

    /**
     * @param listFallecidoPorFirmar the listFallecidoPorFirmar to set
     */
    public void setListNacidoVivoPorFirmar(List<Fallecido> listFallecidoPorFirmar) {
        this.listFallecidoPorFirmar = listFallecidoPorFirmar;
    }

    public List<Fallecido> getListSelectFallecido() {
        return listSelectFallecido;
    }

    public void setListSelectFallecido(List<Fallecido> listSelectFallecido) {
        this.listSelectFallecido = listSelectFallecido;
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
        StringBuilder temp = new StringBuilder();
        if (this.getListSelectFallecido().size() > 0) {
            for (int k = 0; k < listSelectFallecido.size(); k++) {
                temp.append(listSelectFallecido.get(k).getIdFal());
                if (k < listSelectFallecido.size() - 1) {
                    temp.append(",");
                }
            }
            int contarCedula = 0;
            Date fechaFirma = new Date();
            for (Fallecido item : listSelectFallecido) {
                try{
                    item.setFechaFirmaFal(fechaFirma);
                    //ejbFallecidoFacade.edit(item);
                    Barras bc = generarBarras(item.getCedulaFal());
                    FacesContext contex = FacesContext.getCurrentInstance();
                    String ruta = "REGISTRO CIVIL DEL ECUADOR. Verifica Formulario de Registro de fallecido(http://servicios1.registrocivil.gob.ec" + contex.getExternalContext().getRequestContextPath() + "/consultaCiudadanaFal.jsf?nccftndjh=" + item.getCedulaFal() + ")";
                    QR qr = generarQR(ruta);
                    item.setPdfSinFirma(pdfSinFirmaConFechaFirma(item, userLgn, fechaFirma, bc.getImagenBarra(), qr.getImagenQR()));
                    item.setPdfCertificado(firmaPdf(pdfCertificadoInhumacion(item, userLgn, fechaFirma, bc.getImagenBarra(), qr.getImagenQR()), "1"));
                    ejbFallecidoFacade.edit(item);
                    contarCedula ++ ;
                }catch(Exception e){
                    e.printStackTrace();
                }    
            }
            /* Fin recuperación Nacidos Vivos*/
            if (contarCedula == listSelectFallecido.size()) {
                RequestContext.getCurrentInstance().execute("PF('firmaDialog').show();");
                RequestContext.getCurrentInstance().execute("AppletFirmaDigital.setIds('" + temp.toString() + "');");
                RequestContext.getCurrentInstance().execute("AppletFirmaDigital.setProceso('fallecimiento');");
                RequestContext.getCurrentInstance().execute("AppletFirmaDigital.setCedula('" + userLgn.getNomUsuario() + "');");
                RequestContext.getCurrentInstance().execute("AppletFirmaDigital.setManeraParaFirma('" + ejbUsuarioFacade.getManeraFirma(userLgn.getNomUsuario(), userLgn.getAgenciaInUser().getIdAgencia().toString()) + "');");
            } else {
                JsfUtil.displayMessage("El proceso de firma no se puede realizar debido a errores.", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("messages");
            }
        } else {
            JsfUtil.displayMessage("No se ha seleccionado ningún registro para realizar el proceso de firma.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("messages");

        }
    }

    public void verPDFSinFirma() {
        try {
            Long idNac = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idNacViv"));
            Fallecido objeto = ejbFallecidoFacade.find(idNac);
            byte[] pdfData = objeto.getPdfSinFirma();
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
            FacesContext.getCurrentInstance().responseComplete();
            JsfUtil.displayMessage("No se puede visualizar el PDF del registro.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form:messages");
        }
    }

    private Cedula numeroCedula(java.lang.String provincia) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.cedula.GeneraCedula_Service service_cedula = new ec.gob.digercic.renavi.ws.cedula.GeneraCedula_Service();
        ec.gob.digercic.renavi.ws.cedula.GeneraCedula port = service_cedula.getGeneraCedulaPort();
        return port.numeroCedula(provincia);
    }

    public byte[] pdfSinFirmaConFechaFirma(Fallecido item, TblSauregUsuario usuarioFirma, Date fechaFirma, byte[] bc, byte[] qr) throws JRException, IOException, SQLException {
        try {
            List<Fallecido> fallecidoList = new ArrayList<Fallecido>();
            fallecidoList.add(item);
            Map<String, Object> parametros = new HashMap<String, Object>();
            //parametros.put("fechaFirma", fallecidoList.get(0).getFechaFirmaFal());
            parametros.put("institucion", userLgn.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("usuarioLogeado", userLgn.getNomUsuario().toString());
            parametros.put("provincia_rsdnc", fallecidoList.get(0).getProvinciaDsRsdncFal());
            parametros.put("canton_rsdnc", fallecidoList.get(0).getCantonDsRsdncFal());
            parametros.put("parroquia_rsdnc", fallecidoList.get(0).getParroquiaDsRsdncFal());
            parametros.put("localidad_rsdnc", fallecidoList.get(0).getLocalidadRsdncFal());
            parametros.put("direccion_rsdnc", fallecidoList.get(0).getCiudadRsdncFal());
            parametros.put("nombre_establecimiento", userLgn.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("lugar_ocurrio_fallecimiento", userLgn.getAgenciaInUser().getIdInstituc().getNomInstituc());
            parametros.put("provincia_fllcmnt", fallecidoList.get(0).getProvinciaDsFllcmntFal());
            parametros.put("canton_fllcmnt", fallecidoList.get(0).getCantonDsFllcmntFal());
            parametros.put("parroquia_fllcmnt", fallecidoList.get(0).getParroquiaDsFllcmntFal());
            parametros.put("localidad_fllcmnt", fallecidoList.get(0).getLocalidadFllcmntFal());
            parametros.put("direccion_fllcmnt", fallecidoList.get(0).getCiudadFllcmntFal());
            parametros.put("telefono_fllcmnt", "");
            parametros.put("codMSP", fallecidoList.get(0).getFkAgenciaSaureg());
            parametros.put("codigoBarras", bc);
            parametros.put("codigoQR", qr);
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/fallecido/formulario_users.jasper");
            // String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69.jasper");
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(fallecidoList);
            jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] pdfCertificadoInhumacion(Fallecido fallecido, TblSauregUsuario usuarioFirma, Date fechaFirma, byte[] bc, byte[] qr) throws JRException, IOException, SQLException {
        try {
            List<Fallecido> fallecidoList = new ArrayList<Fallecido>();
            fallecidoList.add(fallecido);
            Map<String, Object> parametros = new HashMap<String, Object>();
            //parametros.put("fechaFirma", fallecidoList.get(0).getFechaFirmaFal());
            parametros.put("institucion", userLgn.getAgenciaInUser().getNomAgencia().toString());
            //parametros.put("usuarioLogeado", userLgn.getNomUsuario().toString());
            parametros.put("usuarioLogeado", userLgn.getApellido() + " " + userLgn.getNombre());
            parametros.put("codigoBarras", bc);
            parametros.put("codigoQR", qr);
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/fallecido/certificado_inhumacion.jasper");
            // String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69.jasper");
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(fallecidoList);
            jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Barras generarBarras(java.lang.String msg) {
        ec.gob.digercic.renavi.ws.qrbc.GeneratorCode service = new GeneratorCode();
        ec.gob.digercic.renavi.ws.qrbc.GeneratorCodeSoap port = service.getGeneratorCodeSoap();
        return port.generarBarras(msg);
    }

    private QR generarQR(java.lang.String msg) {
        ec.gob.digercic.renavi.ws.qrbc.GeneratorCode service = new GeneratorCode();
        ec.gob.digercic.renavi.ws.qrbc.GeneratorCodeSoap port = service.getGeneratorCodeSoap();
        return port.generarQR(msg);
    }

    private byte[] firmaPdf(byte[] sinFirma, java.lang.String nomSistema) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.firma.Firmado_Service service = new Firmado_Service();
        ec.gob.digercic.renavi.ws.firma.Firmado port = service.getFirmadoPort();
        return port.firmaPdf(sinFirma, nomSistema);
    }
    
    
}
