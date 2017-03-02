/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.AnulacionRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.Error1RenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.EstadoFirmaRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.EstadoRegistroRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.Anulacion;

import ec.gob.digercic.renavi.entities.Error1;
import ec.gob.digercic.renavi.entities.EstadoFirmaRenavi;
import ec.gob.digercic.renavi.entities.EstadoRegistroRenavi;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author santiago.tapia
 */
@ManagedBean(name = "bandejaAnulacionController")
@ViewScoped
public class BandejaAnulacionController implements Serializable {

    private String cedulaStringHijo = null, cedulaUserAnul = null;
    private String queryConsulta = "";
    private Date fechaNacIni = null; //fecha inicio Nacimiento Nacido Vivo
    private Date fechaNacFin = null; //fecha inicio Nacimiento Nacido Vivo
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
    private List<Anulacion> resultado;
    private Anulacion registroSeleccionado;
    private List<Error1> erroresAnulacion = new ArrayList<Error1>();
    private List<Error1> selectedErroresAnulacion = new ArrayList<Error1>();
    private Anulacion current = new Anulacion();
    @EJB
    private Error1RenaviFacadeLocal ejbErrorAnulacion;
    @EJB
    private AnulacionRenaviFacadeLocal ejbAnulacion;
    @EJB
    private NacidoVivoRenaviFacadeLocal ejbNacidoVivo;
    @EJB
    private EstadoRegistroRenaviFacadeLocal ejbEstRegistro;
    @EJB
    private EstadoFirmaRenaviFacadeLocal ejbEstFirma;
    private Anulacion selectionCurrent;

    public BandejaAnulacionController() {
    }

    public String cambiaFormatoFecha(Date date) {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fechaConFormato = formateador.format(date);
        return fechaConFormato;
    }

    public String action() {
        return "EditarRegistro";
    }

    public void leerObservacionRechazo() {
        try {
            Long id = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnul"));
            selectionCurrent = ejbAnulacion.find(id);
            RequestContext.getCurrentInstance().update("motRec");
            RequestContext.getCurrentInstance().execute("PF('dialogMotRech').show();");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void marcarComoLeido() {
        try {
            selectionCurrent.setObservacionRechazoLeido("1");
            ejbAnulacion.edit(selectionCurrent);
            selectionCurrent = null;
            String query = "select * from anulacion a "
                    + " where a.user_medico_anul_nac_viv = " + userLgn.getNomUsuario();
            resultado.clear();
            resultado = ejbAnulacion.executeNativeQueryListResultGenerico(query, Anulacion.class);
        } catch (EntidadException ee) {
            ee.printStackTrace();
            selectionCurrent = null;
        }
    }

    @PostConstruct
    public void init() throws EntidadException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        setUserLgn((ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion"));
        resultado = null;
        String query = "";
        setQueryConsulta("");
        queryConsulta += "select * from anulacion an ,nacido_vivo_renavi na, madre_renavi ma"
                      + " where an.user_medico_anul_nac_viv = " + userLgn.getNomUsuario();
        queryConsulta += " and id_agencia_anul= " + userLgn.getAgenciaInUser().getCodMsp();
        queryConsulta += " and an.fk_ced_nac_viv = na.id_nac_viv and na.fk_cedul_mad = ma.id_mad";

        System.out.println("Bandeja de Consulta Query --> " + queryConsulta);
        queryConsulta += query;
        try {
            resultado = ejbAnulacion.executeNativeQueryListResultGenerico(getQueryConsulta(), Anulacion.class);
            RequestContext.getCurrentInstance().update("tblResultados");
        } catch (EntidadException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the fechaNacIni
     */
    public Date getFechaNacIni() {
        return fechaNacIni;
    }

    /**
     * @param fechaNacIni the fechaNacIni to set
     */
    public void setFechaNacIni(Date fechaNacIni) {
        this.fechaNacIni = fechaNacIni;
    }

    /**
     * @return the fechaNacFin
     */
    public Date getFechaNacFin() {
        return fechaNacFin;
    }

    /**
     * @param fechaNacFin the fechaNacFin to set
     */
    public void setFechaNacFin(Date fechaNacFin) {
        this.fechaNacFin = fechaNacFin;
    }

    /**
     * @return the cedulaString
     */
    public String getCedulaStringHijo() {
        return cedulaStringHijo;
    }

    /**
     * @param cedulaString the cedulaString to set
     */
    public void setCedulaStringHijo(String cedulaStringHijo) {
        this.cedulaStringHijo = cedulaStringHijo;
    }

    /**
     * @return the resultado
     */
    public List<Anulacion> getResultado() {
        return resultado;
    }

    /**
     * @param resultado the resultado to set
     */
    public void setResultado(List<Anulacion> resultado) {
        this.resultado = resultado;
    }

    /**
     * @return the queryConsulta
     */
    public String getQueryConsulta() {
        return queryConsulta;
    }

    /**
     * @param queryConsulta the queryConsulta to set
     */
    public void setQueryConsulta(String queryConsulta) {
        this.queryConsulta = queryConsulta;
    }

    /**
     * @return the registroSeleccionado
     */
    public Anulacion getRegistroSeleccionado() {

        return registroSeleccionado;
    }

    /**
     * @param registroSeleccionado the registroSeleccionado to set
     */
    public void setRegistroSeleccionado(Anulacion registroSeleccionado) {
        this.registroSeleccionado = registroSeleccionado;
    }

    /**
     * @return the userLgn
     */
    public ec.gob.digercic.renavi.ws.TblSauregUsuario getUserLgn() {
        return userLgn;
    }

    public void setUserLgn(ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn) {
        this.userLgn = userLgn;
    }

   

    /**
     * @return the erroresAnulacion
     */
    public List<Error1> getErroresAnulacion() throws EntidadException {
        erroresAnulacion = ejbErrorAnulacion.findAll();
        return erroresAnulacion;
    }

    /**
     * @return the selectedErroresAnulacion
     */
    public List<Error1> getSelectedErroresAnulacion() {
        return selectedErroresAnulacion;
    }

    /**
     * @return the ejbErrorAnulacion
     */
    public Error1RenaviFacadeLocal getEjbErrorAnulacion() {
        return ejbErrorAnulacion;
    }

    /**
     * @param selectedErroresAnulacion the selectedErroresAnulacion to set
     */
    public void setSelectedErroresAnulacion(List<Error1> selectedErroresAnulacion) {
        this.selectedErroresAnulacion = selectedErroresAnulacion;
    }

    /**
     * @return the ejbAnulacion
     */
    public AnulacionRenaviFacadeLocal getEjbAnulacion() {
        return ejbAnulacion;
    }

    /**
     * @return the current
     */
    public Anulacion getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(Anulacion current) {
        this.current = current;
    }

    public Anulacion getSelectionCurrent() {
        return selectionCurrent;
    }

    /**
     * @return the cedulaUserAnul
     */
    public String getCedulaUserAnul() {
        return cedulaUserAnul;
    }

    /**
     * @param cedulaUserAnul the cedulaUserAnul to set
     */
    public void setCedulaUserAnul(String cedulaUserAnul) {
        this.cedulaUserAnul = cedulaUserAnul;
    }

    /**
     * @return the ejbNacidoVivo
     */
    public NacidoVivoRenaviFacadeLocal getEjbNacidoVivo() {
        return ejbNacidoVivo;
    }

    /**
     * @param ejbNacidoVivo the ejbNacidoVivo to set
     */
    public void setEjbNacidoVivo(NacidoVivoRenaviFacadeLocal ejbNacidoVivo) {
        this.ejbNacidoVivo = ejbNacidoVivo;
    }

    /**
     * @return the ejbEstRegistro
     */
    public EstadoRegistroRenaviFacadeLocal getEjbEstRegistro() {
        return ejbEstRegistro;
    }

    /**
     * @param ejbEstRegistro the ejbEstRegistro to set
     */
    public void setEjbEstRegistro(EstadoRegistroRenaviFacadeLocal ejbEstRegistro) {
        this.ejbEstRegistro = ejbEstRegistro;
    }

    public void actualizaTablaAnulaciones(ActionEvent event) throws SQLException {
        buscar();
        RequestContext.getCurrentInstance().update(":form:tblResultados");
    }

    public void aprobar() {
        current = registroSeleccionado;
        try {
            NacidoVivoRenavi nacVivoRecuperado = new NacidoVivoRenavi();
            nacVivoRecuperado = ejbNacidoVivo.find(current.getFkCedNacViv().getIdNacViv());
            String cedula = nacVivoRecuperado.getCedulNacViv();
            NacidoVivoRenavi nacVivCreado = nacVivoRecuperado;
            //System.out.println("Nacido Vivo recuperado pa proceso de anulacion: "+nacVivoRecuperado.getNombrNacViv() +" " + nacVivoRecuperado.getApellNacViv());
            current.setEstAnulNacViv("2"); //de momento 1:por verificar, 2: aprobado, 3: rechazado 
            current.setObsRech("Solicitud de Anulación Aprobada");
            EstadoRegistroRenavi estReg;
            estReg = ejbEstRegistro.find(6);   // 6 = estado ANULACION.
            nacVivoRecuperado.setFkIdEstReg(estReg);
            nacVivoRecuperado.setCedulNacViv(cedula + "-1");
            ejbNacidoVivo.edit(nacVivoRecuperado);
            //System.out.println("Editó el anterior al estado a anulado. id:"+nacVivoRecuperado.getIdNacViv()  +" Cédula :"+nacVivoRecuperado.getCedulNacViv()+" Estado Registro: "+nacVivoRecuperado.getFkIdEstReg());
            Boolean madrePartos = false, nacViv = false;
            for (Error1 err : current.getErr()) {
                if (err.getId() == 1 || err.getId() == 2) {
                    madrePartos = true;
                }
            }
            if (madrePartos || nacViv) {
                estReg = ejbEstRegistro.find(2);
            } else {
                System.out.println("Predomina Nacido Vivo");
                estReg = ejbEstRegistro.find(3);
            }
            EstadoFirmaRenavi estFirm = ejbEstFirma.find(1);
            nacVivCreado.setFkIdEstReg(estReg);
            nacVivCreado.setFkIdEstFir(estFirm);
            nacVivCreado.setIdNacViv(null);
            //le elimina el pdf firmado y el sin firmar
            nacVivCreado.setPdfConFirmaNacViv(null);
            nacVivCreado.setPdfSinFirmaNacViv(null);
            nacVivCreado.setCedulNacViv(cedula);
            ejbNacidoVivo.detach(nacVivCreado);
            ejbNacidoVivo.create(nacVivCreado);
            getEjbAnulacion().edit(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("AnulacionRenaviAprobado"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('nvDialog').hide();");
            setCurrent(new Anulacion());
        } catch (EntidadException ee) {
            ee.getCause();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
        }
    }

    public void rechazar() {
        current = registroSeleccionado;
        try {
            current.setEstAnulNacViv("3"); //de momento 1:por verificar, 2: aprobado, 3: rechazado 
            current.setObsRech(registroSeleccionado.getObsRech());
            getEjbAnulacion().edit(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("AnulacionRenaviRechazado"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('nvDialog').hide();");
            setCurrent(new Anulacion());
        } catch (Exception ee) {
            ee.getCause();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
        }
    }

    public void buscar() throws SQLException {
        resultado = null;
        String query = "";
        setQueryConsulta("");
        queryConsulta += "select * from anulacion an "
                      + " where an.user_medico_anul_nac_viv = " + userLgn.getNomUsuario();
        queryConsulta += "  and id_agencia_anul=" + userLgn.getAgenciaInUser().getCodMsp() ;
        queryConsulta += query;
        
        System.out.println("QUERY ----> "+queryConsulta);
        try {
            resultado = ejbAnulacion.executeNativeQueryListResultGenerico(getQueryConsulta(), Anulacion.class);
            RequestContext.getCurrentInstance().update("tblResultados");
        } catch (EntidadException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the ejbEstFirma
     */
    public EstadoFirmaRenaviFacadeLocal getEjbEstFirma() {
        return ejbEstFirma;
    }

    /**
     * @param ejbEstFirma the ejbEstFirma to set
     */
    public void setEjbEstFirma(EstadoFirmaRenaviFacadeLocal ejbEstFirma) {
        this.ejbEstFirma = ejbEstFirma;
    }

}
