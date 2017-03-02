package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.AlfabetismoRenavi;
import ec.gob.digercic.renavi.entities.EstadoCivilRenavi;
import ec.gob.digercic.renavi.entities.EstadoRegistroFallecido;
import ec.gob.digercic.renavi.entities.Fallecido;
import ec.gob.digercic.renavi.entities.NivelInstruccionRenavi;
import ec.gob.digercic.renavi.entities.PaisRenavi;
import ec.gob.digercic.renavi.entities.RespuestaMuerteenestablecim;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ws.datmadre.Ciudadanos;
import ec.gob.digercic.renavi.ws.datmadre.Persona;
import ec.gob.digercic.saureg.entities.TblSauregUbicacion;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "fallecidoCDUVController")
@ViewScoped
public class FallecidoCDUVController implements Serializable {

    private Fallecido current;
    @EJB
    private ec.gob.digercic.renavi.ejb.FallecidoFacadeLocal ejbFacade;
    @EJB
    private ec.gob.digercic.renavi.ejb.EstadoRegistroFallecidoFacadeLocal ejbFacadeEstadoRegistro;
    @EJB
    private ec.gob.digercic.renavi.ejb.EstadoFirmaRenaviFacadeLocal ejbFacadeEstadoFirma;
    @EJB
    private ec.gob.digercic.renavi.ejb.NacionalidadRenaviFacadeLocal ejbFacadeNacionalidad;
    @EJB
    private ec.gob.digercic.renavi.ejb.PaisRenaviFacadeLocal ejbFacadePais;
    @EJB
    private ec.gob.digercic.renavi.ejb.EstadoCivilRenaviFacadeLocal ejbFacadeEstadoCivil;
    @EJB
    private ec.gob.digercic.renavi.ejb.NivelInstruccionRenaviFacadeLocal ejbFacadeNivelInstruccion;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregUbicacionFacadeLocal ejbFacadeUbicacion;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregUsuarioFacadeLocal ejbFacadeUsuario;
    @EJB
    private ec.gob.digercic.renavi.ejb.VariableRenaviFacadeLocal ejbFacadeVariable;
    @EJB
    private ec.gob.digercic.renavi.ejb.TipoAreaRenaviFacadeLocal ejbFacadetipoArea;
    @EJB
    private ec.gob.digercic.renavi.ejb.TipoRegistroFallecimientoFacadeLocal ejbFacadetipoRegistroFal;
    @EJB
    private ec.gob.digercic.renavi.ejb.RespuestaCetirficacionMedicaFacadeLocal ejbFacadeRespCertifMedica;
    @EJB
    private ec.gob.digercic.renavi.ejb.TipoEdadFacadeLocal ejbFacadeTipoEdad;
    private String tituloPantalla;
    //Para el ciudadano
    private List<NivelInstruccionRenavi> nivelesInstruccion;
    private List<Persona> personas;
    private Date fechaActual = new Date();
    private StreamedContent imagem;
    private String buscarPor = "Cédula";
    private String cedulaBusq;
    private String apellidoUnoBusq;
    private String apellidoDosBusq;
    private String nombreUnoBusq;
    private String nombreDosBusq;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private boolean flagInconsistencia = false;
    private List<TblSauregUbicacion> provincias;
    private String provincia;
    private List<TblSauregUbicacion> cantones;
    private String canton;
    private List<TblSauregUbicacion> parroquias;
    private String parroquia;
    private Integer tabIndex = 0;
    private TblSauregUsuario userLgn;
    private List<TblSauregUsuario> tblSauregUsuarioList;
    private TblSauregUsuario tblSauregUsuario;
    private String nomUsuarioSaureg;
    private List<TblSauregUbicacion> provinciasFllcmnt;
    private String provinciaFllcmnt;
    private List<TblSauregUbicacion> cantonesFllcmnt;
    private String cantonFllcmnt;
    private List<TblSauregUbicacion> parroquiasFllcmnt;
    private String parroquiaFllcmnt;
    private JasperPrint jasperPrint;
    //Para las unidades de horas, minutos dias, semanas, meses o años
    private String unidadTiempoCausaA;
    private String unidadTiempoCausaB;
    private String unidadTiempoCausaC;
    private String unidadTiempoCausaD;
    private String unidadTiempoCausaOtros;

    public FallecidoCDUVController() {
    }

    @PostConstruct
    public void iniciar() {
        try {
            //Obtengo el usuario del sistema
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            String operacion = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("operacion");
            if (operacion != null) {
                Long id = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
                current = ejbFacade.find(id);
                tituloPantalla = "Edición de Registro de Defunción";
                try {
                    if (current.getFkUsuFirmaSaureg() != null && current.getFkAgenciaFirmaSaureg() != null) {
                        tblSauregUsuario = ejbFacadeUsuario.getUsuarioByUserAndCodAgenciaMSP(current.getFkUsuFirmaSaureg(), current.getFkAgenciaFirmaSaureg().toString());
                        tblSauregUsuario.setAgenciaInUser(userLgn.getAgenciaInUser());
                        nomUsuarioSaureg = current.getFkUsuFirmaSaureg();
                    }
                } catch (Exception euf) {
                    tblSauregUsuario = null;
                    nomUsuarioSaureg = null;
                    /*euf.printStackTrace();
                     JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("SelectEntidadExceptionEdit"), JsfUtil.ERROR_MESSAGE);
                     RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");*/
                }
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                //Provincia, canton, parroqua de residencia
                if (current.getProvinciaDsRsdncFal() != null && current.getProvinciaIdRsdncFal() != null) {
                    provincia = current.getProvinciaIdRsdncFal() + ".- " + current.getProvinciaDsRsdncFal();
                    cantones = new ArrayList<TblSauregUbicacion>();
                    try {
                        params.add(new ParametroConsulta("codExterno1", current.getProvinciaIdRsdncFal()));
                        params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                        cantones = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodExterno1", params);
                        canton = current.getCantonIdRsdncFal() + ".- " + current.getCantonDsRsdncFal();
                    } catch (EntidadException ec) {
                        ec.printStackTrace();
                    }
                }
                if (current.getCantonIdRsdncFal() != null && current.getCantonDsRsdncFal() != null) {
                    parroquias = new ArrayList<TblSauregUbicacion>();
                    try {
                        params.clear();
                        params.add(new ParametroConsulta("codExterno1", current.getCantonIdRsdncFal()));
                        params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                        parroquias = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodExterno1", params);
                        parroquia = current.getParroquiaIdRsdncFal() + ".- " + current.getParroquiaDsRsdncFal();
                    } catch (EntidadException ec) {
                        ec.printStackTrace();
                    }
                }
                //Provincia, canton, parroquia donde ocurrio el fallecimiento
                if (current.getProvinciaDsFllcmntFal() != null && current.getProvinciaIdFllcmntFal() != null) {
                    provinciaFllcmnt = current.getProvinciaIdFllcmntFal() + ".- " + current.getProvinciaDsFllcmntFal();
                    cantonesFllcmnt = new ArrayList<TblSauregUbicacion>();
                    try {
                        params.clear();
                        params.add(new ParametroConsulta("codExterno1", current.getProvinciaIdFllcmntFal()));
                        params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                        cantonesFllcmnt = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodExterno1", params);
                        cantonFllcmnt = current.getCantonIdFllcmntFal() + ".- " + current.getCantonDsFllcmntFal();
                    } catch (EntidadException ec) {
                        ec.printStackTrace();
                    }
                }
                if (current.getCantonIdFllcmntFal() != null && current.getCantonDsFllcmntFal() != null) {
                    parroquiasFllcmnt = new ArrayList<TblSauregUbicacion>();
                    try {
                        params.clear();
                        params.add(new ParametroConsulta("codExterno1", current.getCantonIdFllcmntFal()));
                        params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                        parroquiasFllcmnt = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodExterno1", params);
                        parroquiaFllcmnt = current.getParroquiaIdFllcmntFal() + ".- " + current.getParroquiaDsFllcmntFal();
                    } catch (EntidadException ec) {
                        ec.printStackTrace();
                    }
                }
                //obtengo el nivel de instrución             
                if (current.getFkIdAlfabetismo() != null) {
                    this.obtenerSabeLeer(current.getFkIdAlfabetismo());
                }
                if (current.getFotografiaFal() != null) {
                    try {
                        InputStream input = new ByteArrayInputStream(current.getFotografiaFal());
                        this.setImagem(new DefaultStreamedContent(input, "image/jpeg"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //Obtengo las unidades de tiempo entre la muerte
                //Asigno las unidades a los tiempos de muerte
                if (current.getTiempoMuerteAFal() != null) {
                    unidadTiempoCausaA = current.getTiempoMuerteAFal().substring(4, current.getTiempoMuerteAFal().length());
                    current.setTiempoMuerteAFal(current.getTiempoMuerteAFal().substring(0, 3));
                }
                if (current.getTiempoMuerteBFal() != null) {
                    unidadTiempoCausaB = current.getTiempoMuerteBFal().substring(4, current.getTiempoMuerteBFal().length());
                    current.setTiempoMuerteBFal(current.getTiempoMuerteBFal().substring(0, 3));
                }
                if (current.getTiempoMuerteCFal() != null) {
                    unidadTiempoCausaC = current.getTiempoMuerteCFal().substring(4, current.getTiempoMuerteCFal().length());
                    current.setTiempoMuerteCFal(current.getTiempoMuerteCFal().substring(0, 3));
                }
                if (current.getTiempoMuerteDFal() != null) {
                    unidadTiempoCausaD = current.getTiempoMuerteDFal().substring(4, current.getTiempoMuerteDFal().length());
                    current.setTiempoMuerteDFal(current.getTiempoMuerteDFal().substring(0, 3));
                }
                if (current.getTiempoMuerteOtrsMotivFal() != null) {
                    unidadTiempoCausaOtros = current.getTiempoMuerteOtrsMotivFal().substring(4, current.getTiempoMuerteOtrsMotivFal().length());
                    current.setTiempoMuerteOtrsMotivFal(current.getTiempoMuerteOtrsMotivFal().substring(0, 3));
                }
                System.gc();
            } else {
                tituloPantalla = "Creación de Registro de Defunción";
            }
        } catch (EntidadException ee) {
            tituloPantalla = "Error en la pantalla";
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCargaIniciar"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        }
    }

    public String create() {
        try {
            ejbFacade.create(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("CreacionRegistro"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return prepareCreate();
        } catch (EntidadException ee) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoCreacionRegistro"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return "";
        }
    }

    public String update() {
        try {
            ejbFacade.edit(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("EdicionRegistro"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return prepareList();
        } catch (EntidadException ee) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoEdicionRegistro"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
            return null;
        }
    }

    private void destroy() {
        try {
            ejbFacade.remove(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("EliminacionRegistro"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        } catch (EntidadException ee) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/edu/uce/eperson/view/resources/Mensajes").getString("NoEliminacionRegistro"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("hfrm:mssgsGrwl");
        }
    }

    public String prepareList() {
        return "List";
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        return "Create";
    }

    public Fallecido getSelected() {
        if (current == null) {
            current = new Fallecido();
        }
        return current;
    }

    public String getTituloPantalla() {
        return tituloPantalla;
    }

    public List<NivelInstruccionRenavi> getNivelesInstruccion() {
        if (nivelesInstruccion == null) {
            nivelesInstruccion = new ArrayList<NivelInstruccionRenavi>();
        }
        return nivelesInstruccion;
    }

    public List<Persona> getPersonas() {
        if (personas == null) {
            personas = new ArrayList<Persona>();
        }
        return personas;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public StreamedContent getImagem() {
        return imagem;
    }

    public void setImagem(StreamedContent imagem) {
        this.imagem = imagem;
    }

    public String getBuscarPor() {
        return buscarPor;
    }

    public void setBuscarPor(String buscarPor) {
        this.buscarPor = buscarPor;
    }

    public String getCedulaBusq() {
        return cedulaBusq;
    }

    public void setCedulaBusq(String cedulaBusq) {
        this.cedulaBusq = cedulaBusq;
    }

    public String getApellidoUnoBusq() {
        return apellidoUnoBusq;
    }

    public void setApellidoUnoBusq(String apellidoUnoBusq) {
        this.apellidoUnoBusq = apellidoUnoBusq;
    }

    public String getApellidoDosBusq() {
        return apellidoDosBusq;
    }

    public void setApellidoDosBusq(String apellidoDosBusq) {
        this.apellidoDosBusq = apellidoDosBusq;
    }

    public String getNombreUnoBusq() {
        return nombreUnoBusq;
    }

    public void setNombreUnoBusq(String nombreUnoBusq) {
        this.nombreUnoBusq = nombreUnoBusq;
    }

    public String getNombreDosBusq() {
        return nombreDosBusq;
    }

    public void setNombreDosBusq(String nombreDosBusq) {
        this.nombreDosBusq = nombreDosBusq;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getProvinciaFllcmnt() {
        return provinciaFllcmnt;
    }

    public void setProvinciaFllcmnt(String provinciaFllcmnt) {
        this.provinciaFllcmnt = provinciaFllcmnt;
    }

    public String getCantonFllcmnt() {
        return cantonFllcmnt;
    }

    public void setCantonFllcmnt(String cantonFllcmnt) {
        this.cantonFllcmnt = cantonFllcmnt;
    }

    public String getParroquiaFllcmnt() {
        return parroquiaFllcmnt;
    }

    public void setParroquiaFllcmnt(String parroquiaFllcmnt) {
        this.parroquiaFllcmnt = parroquiaFllcmnt;
    }

    public String getUnidadTiempoCausaA() {
        return unidadTiempoCausaA;
    }

    public void setUnidadTiempoCausaA(String unidadTiempoCausaA) {
        this.unidadTiempoCausaA = unidadTiempoCausaA;
    }

    public String getUnidadTiempoCausaB() {
        return unidadTiempoCausaB;
    }

    public void setUnidadTiempoCausaB(String unidadTiempoCausaB) {
        this.unidadTiempoCausaB = unidadTiempoCausaB;
    }

    public String getUnidadTiempoCausaC() {
        return unidadTiempoCausaC;
    }

    public void setUnidadTiempoCausaC(String unidadTiempoCausaC) {
        this.unidadTiempoCausaC = unidadTiempoCausaC;
    }

    public String getUnidadTiempoCausaD() {
        return unidadTiempoCausaD;
    }

    public void setUnidadTiempoCausaD(String unidadTiempoCausaD) {
        this.unidadTiempoCausaD = unidadTiempoCausaD;
    }

    public String getUnidadTiempoCausaOtros() {
        return unidadTiempoCausaOtros;
    }

    public void setUnidadTiempoCausaOtros(String unidadTiempoCausaOtros) {
        this.unidadTiempoCausaOtros = unidadTiempoCausaOtros;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public List<TblSauregUsuario> getTblSauregUsuarioList() {
        if (tblSauregUsuarioList == null) {
            tblSauregUsuarioList = new ArrayList<TblSauregUsuario>();
        }
        return tblSauregUsuarioList;
    }

    public void setTblSauregUsuarioList(List<TblSauregUsuario> tblSauregUsuarioList) {
        this.tblSauregUsuarioList = tblSauregUsuarioList;
    }

    public TblSauregUsuario getTblSauregUsuario() {
        if (tblSauregUsuario == null) {
            tblSauregUsuario = new TblSauregUsuario();
        }
        return tblSauregUsuario;
    }

    public void setTblSauregUsuario(TblSauregUsuario tblSauregUsuario) {
        this.tblSauregUsuario = tblSauregUsuario;
    }

    public String getNomUsuarioSaureg() {
        return nomUsuarioSaureg;
    }

    public void setNomUsuarioSaureg(String nomUsuarioSaureg) {
        this.nomUsuarioSaureg = nomUsuarioSaureg;
    }

    /**
     * ************************************
     */
    /**
     * Medotodo que consume WS y devuelve la información de la madre dado:
     *
     * @param cedula
     * @param usuario
     * @param contrasenia
     * @return
     */
    private Persona busquedaPorCedula(java.lang.String cedula, java.lang.String usuario, java.lang.String contrasenia) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta_Service service = new ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta_Service();
        ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta port = service.getWSRegistroCivilConsultaPort();
        return port.busquedaPorCedula(cedula, usuario, contrasenia);
    }

    /**
     * Medotodo que consume WS y devuelve la información de la madre dado:
     *
     * @param apellido1
     * @param apellido2
     * @param nombre1
     * @param nombre2
     * @param edadInicio
     * @param edadFinal
     * @param genero
     * @param usuario
     * @param contrasenia
     * @return
     */
    private Ciudadanos busquedaPorNombre(java.lang.String apellido1, java.lang.String apellido2, java.lang.String nombre1, java.lang.String nombre2, java.lang.String edadInicio, java.lang.String edadFinal, java.lang.String genero, java.lang.String usuario, java.lang.String contrasenia) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta_Service service = new ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta_Service();
        ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta port = service.getWSRegistroCivilConsultaPort();
        return port.busquedaPorNombre(apellido1, apellido2, nombre1, nombre2, edadInicio, edadFinal, genero, usuario, contrasenia);
    }

    /**
     * Método para consultar la edad de una persona
     *
     * @param fechaNacimiento Fecha de nacimiento de la persona.
     * @return Edad en años de la persona.
     */
    public short calcularEdad(Date fechaNacimiento) {
        Calendar fechaNacim = Calendar.getInstance();
        fechaNacim.setTime(fechaNacimiento);
        Calendar fechaActual = Calendar.getInstance();
        int año = fechaActual.get(Calendar.YEAR) - fechaNacim.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH) - fechaNacim.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE) - fechaNacim.get(Calendar.DATE);
        if (mes < 0 || (mes == 0 && dia < 0)) {
            año--;
        }
        short edad = (short) año;
        return edad;
    }

    public void asignacionCurrent(String cdl) {
        Persona perso = new Persona();
        try {
            perso = this.busquedaPorCedula(cdl, JsfUtil.USER_ACCWS_MADRE, JsfUtil.PASS_ACCWS_MADRE);
            if (perso.getCodigoError().equals("000") || (Integer.parseInt(perso.getCodigoError()) >= 200)) {
                //verifico si el ciudadano no ha sido ya guardado en la base de datos
                List<Fallecido> yaFallecido = ejbFacade.consultarTablaResultList("Fallecido.findByCedulaFal", "cedulaFal", perso.getCedula());
                if (yaFallecido.isEmpty()) {
                    //Verifico el genero, estado de ciudadania y la edad
                    StringBuilder obsMadre = new StringBuilder();
                    StringBuilder msjInconsistencias = new StringBuilder();
                    if (perso.getCondicionCedulado() != null
                            && perso.getFechaNacimiento() != null
                            && perso.getCedula() != null
                            && perso.getNombre() != null) {
                        //Condición de cedulado;
                        current.setCondicionCeduladoFal(perso.getCondicionCedulado().trim());
                        obsMadre.append("CONDICIÓN DE CIUDADANÍA: ");
                        obsMadre.append(perso.getCondicionCedulado().trim());
                        if (perso.getCondicionCedulado().contains("FALLECIDO")) {
                            msjInconsistencias.append("El registro encontrado aparece con estado de ciudadanía 'Fallecido'. ");
                            flagInconsistencia = true;
                        }
                        //edad
                        try {
                            current.setFechaNacimientoFal(formatoFecha.parse(perso.getFechaNacimiento().trim()));
                            current.setEdadFal(this.calcularEdad(formatoFecha.parse(perso.getFechaNacimiento())));
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                            current.setFechaNacimientoFal(null);
                        }
                        //Datos del fallecido
                        current.setNombreFal(perso.getNombre().trim());
                        current.setCedulaFal(perso.getCedula().trim());
                        //Si la el ciudadano no está registrado ya como difunto
                        //Género
                        if(perso.getGenero() != null){
                            current.setSexoFal(perso.getGenero().trim());
                            obsMadre.append("; GÉNERO: ");
                            obsMadre.append(perso.getGenero().trim());
                        } 
                        //Si no hay inconsistencias
                        if (!flagInconsistencia) {
                            //Seteo la fecha de creación y de actualización del nacido vivo
                            current.setFechaCreacionFal(fechaActual);
                            //current.setFechaActualizacion(fechaActual);
                            if (perso.getNombrePadre() != null || !perso.getNombrePadre().equals("")) {
                                current.setNombrePadreFal(perso.getNombrePadre().trim());
                            }
                            if (perso.getNombreMadre() != null || !perso.getNombreMadre().equals("")) {
                                current.setNombreMadreFal(perso.getNombreMadre().trim());
                            }
                            //Para la fotografía
                            if (perso.getCodigoError().equals("000")) {
                                try {
                                    InputStream input = new ByteArrayInputStream(perso.getFotografia());
                                    this.setImagem(new DefaultStreamedContent(input, "image/jpeg"));
                                    current.setFotografiaFal(perso.getFotografia());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            //Obtengo la nacionalidad del ciudadano
                            try {
                                if (!perso.getNacionalidad().equals("")) {
                                    if (perso.getNacionalidad().toUpperCase().trim().contains("ECUATORIANA")) {
                                        current.setFkIdNacionalidad(ejbFacadeNacionalidad.find(1));
                                        PaisRenavi pais = ejbFacadePais.find(81);
                                        current.setFkIdPais(pais);
                                        current.setCodigoPaisFal(pais.getCodigPais());
                                    }
                                }
                            } catch (EntidadException ee) {
                                ee.printStackTrace();
                                current.setFkIdNacionalidad(null);
                                current.setFkIdPais(null);
                                current.setCodigoPaisFal(null);
                            }
                            //Obtengo el estado civil del fallecido
                            try {
                                if (!perso.getEstadoCivil().equals("")) {
                                    EstadoCivilRenavi estadoCivil = new EstadoCivilRenavi();
                                    if (perso.getEstadoCivil().toUpperCase().contains("SOLTER")) {
                                        estadoCivil = ejbFacadeEstadoCivil.find(2);
                                    } else if (perso.getEstadoCivil().toUpperCase().contains("CASAD")) {
                                        estadoCivil = ejbFacadeEstadoCivil.find(3);
                                    } else if (perso.getEstadoCivil().toUpperCase().contains("DIVORCIAD")) {
                                        estadoCivil = ejbFacadeEstadoCivil.find(4);
                                    } else if (perso.getEstadoCivil().toUpperCase().contains("VIUD")) {
                                        estadoCivil = ejbFacadeEstadoCivil.find(6);
                                    }
                                    current.setFkIdEstadoCivilFal(estadoCivil);
                                    obsMadre.append("ESTADO CIVIL: ");
                                    obsMadre.append(perso.getEstadoCivil());
                                }
                            } catch (EntidadException ee) {
                                ee.printStackTrace();
                                current.setFkIdEstadoCivilFal(null);
                            }
                            if (perso.getConyuge() != null) {
                                current.setConyugeFal(perso.getConyuge().trim());
                                obsMadre.append(" ;CÓNYUGE: ");
                                obsMadre.append(perso.getConyuge().trim());
                            }
                            //Seteo la observación de la madre
                            //hijoAux.setObsrvEstadoMad(obsMadre.toString());
                        } else {
                            msjInconsistencias.append("No se puede continuar con el proceso de registro de datos del fallecido.");
                            JsfUtil.displayMessage(msjInconsistencias.toString(), JsfUtil.ERROR_MESSAGE);
                            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaErrorM");
                        }
                    } else {
                        JsfUtil.displayMessage("Los datos del ciudadano buscado no están completos.", JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                    }
                } else {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("CiudadanoYaInscrito"), JsfUtil.WARN_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaF");
                    limpiar();
                }
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistroPorCedulaNoEncontrado"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
            }
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
            System.gc();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("WebServiceErrorConsulta"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
        }
    }

    /**
     * Metodo setea informacion de la madre traida en el WS cuando la consulta
     * se la realiza por numero de cédula
     *
     */
    public void buscarMadreCedula() {
        this.asignacionCurrent(cedulaBusq);
    }

    /**
     * Método que busca las madres traidas del WS cuando la consulta se la
     * realiza por appellidos y nombres
     *
     */
    public void buscarMadreAppellidosNombres() {
        Ciudadanos ciudadanos = new Ciudadanos();
        try {
            ciudadanos = this.busquedaPorNombre(apellidoUnoBusq, apellidoDosBusq, nombreUnoBusq, nombreDosBusq, null, null, null, JsfUtil.USER_ACCWSNOMBRES_MADRE,
                    JsfUtil.PASS_ACCWSNOMBRES_MADRE);
            personas = ciudadanos.getPersona();
            if (personas.isEmpty()) {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistrosPorNombresNoEncontrados"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
            } else {
                RequestContext.getCurrentInstance().execute("PF('statusDialog').hide();");
                RequestContext.getCurrentInstance().execute("PF('personasDialog').show();");
                RequestContext.getCurrentInstance().update("form_cont:contTab:pdtTblItemsPersonas");
            }
            System.gc();
        } catch (Exception e) {
            System.gc();
            e.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("WebServiceErrorConsulta"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
        }
    }

    /**
     * Método cuando el usuario realiza la selección del modo de búsqueda por
     * cédula y por apellidos y nombres. Esto esta en la página de la madre.
     *
     * @param event
     */
    public void cambiarBuscarPor(AjaxBehaviorEvent event) {
        String seleccion = (String) ((UIOutput) event.getSource()).getValue();
        if (seleccion.equals("Cédula")) {
            limpiarBuscarPorCedula();
        } else {
            limpiarBuscarPorApellNom();
        }
        RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
    }

    public void limpiar() {
        this.current = null;
        this.setImagem(null);
        personas = null;
        //Reseteo el valor de los combos de provincia, cantón y parroquia
        provincias = null;
        cantones = null;
        parroquias = null;
        provincia = null;
        canton = null;
        parroquia = null;
        cedulaBusq = null;
        apellidoUnoBusq = null;
        apellidoDosBusq = null;
        nombreUnoBusq = null;
        nombreDosBusq = null;
        nivelesInstruccion = null;
        /*hijosAux = null;
         hijosAuxDelete = null;*/
        flagInconsistencia = false;
        /*numParto = null;
         numHijosvivos = null;
         numHijosVivMuertos = null;
         numHijosNacierMuertos = null;
         hijosVivosSistema = null;
         porEliminarNV = 0;*/
        System.gc();
    }

    /**
     * Método para limpiar la consulta cuando el usuario ha seleccionado buscar
     * por cédula.
     */
    public void limpiarBuscarPorCedula() {
        this.current = new Fallecido();
        this.setImagem(null);
        cedulaBusq = null;
        nivelesInstruccion = null;
        flagInconsistencia = false;
        provincia = null;
        cantones = null;
        canton = null;
        parroquias = null;
        parroquia = null;
        //
        /*numParto = null;
         numHijosvivos = null;
         numHijosVivMuertos = null;
         numHijosNacierMuertos = null;
         hijosVivosSistema = null;
         porEliminarNV = 0;*/
        System.gc();
    }

    /**
     * Método para limpiar la consulta cuando el usuario ha seleccionado buscar
     * por appellidos y nombres.
     */
    public void limpiarBuscarPorApellNom() {
        this.current = new Fallecido();
        this.setImagem(null);
        personas = new ArrayList<Persona>();
        apellidoUnoBusq = null;
        apellidoDosBusq = null;
        nombreUnoBusq = null;
        nombreDosBusq = null;
        nivelesInstruccion = null;
        flagInconsistencia = false;
        provincia = null;
        cantones = null;
        canton = null;
        parroquias = null;
        parroquia = null;
        //
        /*numParto = null;
         numHijosvivos = null;
         numHijosVivMuertos = null;
         numHijosNacierMuertos = null;
         hijosVivosSistema = null;
         porEliminarNV = 0;*/
        System.gc();
    }

    public List<TblSauregUbicacion> getProvincias() {
        try {
            if (provincias == null) {
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                params.add(new ParametroConsulta("idGrupo", Long.parseLong("1")));
                params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                provincias = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupo", params);
            }
            return provincias;
        } catch (EntidadException ee) {
            ee.printStackTrace();
            return null;
        }

    }

    public void cambiarProvincia(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Reseteo los combos de cantones y parroquias
            cantones = null;
            parroquias = null;
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            current.setProvinciaIdRsdncFal(value.substring(0, value.indexOf(".- ")));
            current.setProvinciaDsRsdncFal(value.substring(value.indexOf(".-") + 3, value.length()));
            //Asigno los cantones correspondientes a la provincia seleccionada
            cantones = new ArrayList<TblSauregUbicacion>();
            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            params.add(new ParametroConsulta("codDpa", value.substring(0, value.indexOf(".- "))));
            params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
            try {
                cantones = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodDpa", params);
            } catch (EntidadException ee) {
                ee.printStackTrace();
            }
            System.gc();
        }
    }

    public List<TblSauregUbicacion> getCantones() {
        if (cantones == null) {
            cantones = new ArrayList<TblSauregUbicacion>();
        }
        return cantones;
    }

    public void cambiarCanton(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Reseteo los combos de parroquias
            parroquias = null;
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            current.setCantonIdRsdncFal(value.substring(0, value.indexOf(".- ")));
            current.setCantonDsRsdncFal(value.substring(value.indexOf(".-") + 3, value.length()));
            //Asigno las parroquias correspondientes a la provincia seleccionada
            parroquias = new ArrayList<TblSauregUbicacion>();
            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            params.add(new ParametroConsulta("codDpa", value.substring(0, value.indexOf(".- "))));
            params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
            try {
                parroquias = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodDpa", params);
            } catch (EntidadException ee) {
                ee.printStackTrace();
            }
            System.gc();
        }
    }

    public List<TblSauregUbicacion> getParroquias() {
        if (parroquias == null) {
            parroquias = new ArrayList<TblSauregUbicacion>();
        }
        return parroquias;
    }

    public void cambiarParroquia(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            current.setParroquiaIdRsdncFal(value.substring(0, value.indexOf(".- ")));
            current.setParroquiaDsRsdncFal(value.substring(value.indexOf(".-") + 3, value.length()));
            System.gc();
        }
    }

    public List<TblSauregUbicacion> getProvinciasFllcmnt() {
        try {
            if (provinciasFllcmnt == null) {
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                params.add(new ParametroConsulta("idGrupo", Long.parseLong("1")));
                params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                provinciasFllcmnt = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupo", params);
            }
            return provinciasFllcmnt;
        } catch (EntidadException ee) {
            ee.printStackTrace();
            return null;
        }

    }

    public void cambiarProvinciaFllcmnt(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Reseteo los combos de cantones y parroquias
            cantonesFllcmnt = null;
            parroquiasFllcmnt = null;
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            current.setProvinciaIdFllcmntFal(value.substring(0, value.indexOf(".- ")));
            current.setProvinciaDsFllcmntFal(value.substring(value.indexOf(".-") + 3, value.length()));
            //Asigno los cantones correspondientes a la provincia seleccionada
            cantonesFllcmnt = new ArrayList<TblSauregUbicacion>();
            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            params.add(new ParametroConsulta("codDpa", value.substring(0, value.indexOf(".- "))));
            params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
            try {
                cantonesFllcmnt = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodDpa", params);
            } catch (EntidadException ee) {
                ee.printStackTrace();
            }
            System.gc();
        }
    }

    public List<TblSauregUbicacion> getCantonesFllcmnt() {
        if (cantonesFllcmnt == null) {
            cantonesFllcmnt = new ArrayList<TblSauregUbicacion>();
        }
        return cantonesFllcmnt;
    }

    public void cambiarCantonFllcmnt(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Reseteo los combos de parroquias
            parroquiasFllcmnt = null;
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            current.setCantonIdFllcmntFal(value.substring(0, value.indexOf(".- ")));
            current.setCantonDsFllcmntFal(value.substring(value.indexOf(".-") + 3, value.length()));
            //Asigno las parroquias correspondientes a la provincia seleccionada
            parroquiasFllcmnt = new ArrayList<TblSauregUbicacion>();
            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            params.add(new ParametroConsulta("codDpa", value.substring(0, value.indexOf(".- "))));
            params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
            try {
                parroquiasFllcmnt = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodDpa", params);
            } catch (EntidadException ee) {
                ee.printStackTrace();
            }
            System.gc();
        }
    }

    public List<TblSauregUbicacion> getParroquiasFllcmnt() {
        if (parroquiasFllcmnt == null) {
            parroquiasFllcmnt = new ArrayList<TblSauregUbicacion>();
        }
        return parroquiasFllcmnt;
    }

    public void cambiarParroquiaFllcmnt(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            current.setParroquiaIdFllcmntFal(value.substring(0, value.indexOf(".- ")));
            current.setParroquiaDsFllcmntFal(value.substring(value.indexOf(".-") + 3, value.length()));
            System.gc();
        }
    }

    /**
     * Método para seleccionar la madre de la lista de personas consultadas por
     * apellidos y nombres
     *
     * @param event
     */
    public void seleccionarPersonaDialog(ActionEvent event) {
        String cedula = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedulaDialog");
        if (cedula != null) {
            //Reinicio la madre, hijo, provincias, cantones, parroquias, intrucciones
            current = new Fallecido();
            nivelesInstruccion = null;
            this.asignacionCurrent(cedula);
        }
    }

    /**
     * Método para cambiar el valor de la variable sabe leer? esto esta en la
     * págiana de la madre
     *
     * @param event
     */
    public void cambiarSabeLeer(ValueChangeEvent event) {
        //Reseteo el nivel de instraucción
        nivelesInstruccion = null;
        nivelesInstruccion = new ArrayList<NivelInstruccionRenavi>();
        current.setFkIdNivelInstruccion(null);
        //Obtengo el valor seleccionado
        AlfabetismoRenavi item = (AlfabetismoRenavi) event.getNewValue();
        if (item != null) {
            if (item.getIdAlfb() == 2) {
                try {
                    NivelInstruccionRenavi instruccion = ejbFacadeNivelInstruccion.find(0);
                    nivelesInstruccion.add(instruccion);
                    current.setFkIdNivelInstruccion(instruccion);
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            } else {
                try {
                    if (current.getEdadFal() >= 10 && current.getEdadFal() <= 11) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad10And11");
                    } else if (current.getEdadFal() >= 12 && current.getEdadFal() <= 14) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad12And14");
                    } else if (current.getEdadFal() >= 15 && current.getEdadFal() <= 16) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad15And16");
                    } else if (current.getEdadFal() >= 17 && current.getEdadFal() <= 22) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad17And22");
                    } else if (current.getEdadFal() >= 23 && current.getEdadFal() <= 49) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    } else if (current.getEdadFal() >= 50) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    }
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    public void saveF() {
        try {
            //Seteo el tipo de registro/Identificado o no identificado
            current.setFkIdTipRegFal(ejbFacadetipoRegistroFal.find(JsfUtil.STAT_IDENT));
            //Seteo el estado de la firma y el estado de registro del fallecido
            current.setFkIdEstadoFirma(ejbFacadeEstadoFirma.find(JsfUtil.STAT_FIR_SIN));
            current.setFkIdEstRegFal(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_FALLECIDO));
            //Seteo la fecha de creacion, de actualización y de fallecimiento
            current.setFechaCreacionFal(fechaActual);
            current.setFechaActualizacionFal(fechaActual);
            current.setFechaFallecimientoFal(fechaActual);
            //Seteo el status del registro, esto es para la eliminacion lógica
            current.setStatusFal("A");
            //Seteo el usuario y la agencia en ña que registra el fallecimiento del ciudadano
            current.setFkAgenciaSaureg(userLgn.getAgenciaInUser().getCodMsp());
            current.setFkUsuSaureg(userLgn.getNomUsuario());
            //ejbFacade.edit(current);
            if(current.getIdFal() == null){
                ejbFacade.create(current);
            }else{
                ejbFacade.edit(current);
            } 
            //Seteo el tabindex
            this.setTabIndex(1);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
            //Creo el Log
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("FallecidoCreado"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaF");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            //}catch(EntidadException ee){
        } catch (EntidadException ee) {
            ee.printStackTrace();
            limpiar();
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se pudo guardar la información del fallecido. Contáctese con el administrador del sistema.", JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaF");
        }
    }

    public void editF() {
        try {
            //Seteo el estado de la firma del fallecido
            current.setFkIdEstRegFal(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_FALLECIDO));
            //ejbFacade.edit(current);
            ejbFacade.edit(current);
            //Seteo el tabindex
            this.setTabIndex(1);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
            //Creo el Log
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("FallecidoCreado"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaF");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            //}catch(EntidadException ee){
        } catch (EntidadException ee) {
            ee.printStackTrace();
            limpiar();
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se pudo guardar la información del fallecido. Contáctese con el administrador del sistema.", JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaF");
        }
    }

    public void saveDF() {
        try {
            tblSauregUsuarioList = ejbFacadeUsuario.getUsuariosByAgenciaAndPagina(userLgn.getAgenciaInUser().getIdAgencia().toString(), JsfUtil.PAG_FIRMARNV);
            for (TblSauregUsuario item : tblSauregUsuarioList) {
                if (item.getNomUsuario().equals(userLgn.getNomUsuario())) {
                    try {
                        tblSauregUsuario = (TblSauregUsuario) BeanUtils.cloneBean(userLgn);
                        nomUsuarioSaureg = tblSauregUsuario.getNomUsuario();
                        //Seteo el usuario y la agencia en la que firmara electronicamente el fallecimiento del ciudadano
                        current.setFkAgenciaFirmaSaureg(tblSauregUsuario.getAgenciaInUser().getCodMsp());
                        current.setFkUsuFirmaSaureg(tblSauregUsuario.getNomUsuario());
                    } catch (Exception eu) {
                        eu.printStackTrace();
                    } finally {
                        break;
                    }
                }
            }
            //Seteo el estado del registro
            current.setFkIdEstRegFal(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_DATOS_FALLECIDO));
            if (current.getEdadFal() >= 1) {
                current.setFkIdTipEda(ejbFacadeTipoEdad.find(4));
            }
            //Asigno las unidades a los tiempos de muerte
            current.setTiempoMuerteAFal(current.getTiempoMuerteAFal() + " " + unidadTiempoCausaA);
            current.setTiempoMuerteBFal(current.getTiempoMuerteBFal() + " " + unidadTiempoCausaB);
            current.setTiempoMuerteCFal(current.getTiempoMuerteCFal() + " " + unidadTiempoCausaC);
            current.setTiempoMuerteDFal(current.getTiempoMuerteDFal() + " " + unidadTiempoCausaD);
            current.setTiempoMuerteOtrsMotivFal(current.getTiempoMuerteOtrsMotivFal() + " " + unidadTiempoCausaOtros);
            //Edito el registro
            ejbFacade.edit(current);
            //Seteo el tabindex
            this.setTabIndex(2);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
            //Creo el Log
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("FallecidoCreado"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            //}catch(EntidadException ee){
        } catch (EntidadException ee) {
            ee.printStackTrace();
            limpiar();
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se pudo guardar la información del fallecido. Contáctese con el administrador del sistema.", JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaDF");
        }
    }
    
    public void atrasF(){
         try {
            this.setTabIndex(0);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        } catch (Exception e) {
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se puede regresar a la pestaña anterior.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaDF");
        }
    }

    public void saveD() {
        EstadoRegistroFallecido estado = new EstadoRegistroFallecido();
        try {
            estado = ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_DOCTOR_FAL);
        } catch (Exception e) {
            estado = null;
        }
        if (estado != null) {
            StringBuilder msjError = new StringBuilder();
            StringBuilder msj = new StringBuilder();
            try {
                //Seteo el estado
                current.setFkIdEstRegFal(estado);
                //Seteo si es certificado por medico
                current.setFkIdResCerMed(ejbFacadeRespCertifMedica.find(JsfUtil.CERTIFICACION_MEDICA_SI));
                //Seteo el usuario y la agencia en la que firmara electronicamente el fallecimiento del ciudadano
                current.setFkAgenciaFirmaSaureg(tblSauregUsuario.getAgenciaInUser().getCodMsp());
                current.setFkUsuFirmaSaureg(tblSauregUsuario.getNomUsuario());
                ejbFacade.edit(current);
                //Genero el PDF
                byte[] pdfSinFirma;
                try {
                    pdfSinFirma = this.pdfSinFirma(current, tblSauregUsuario);
                } catch (Exception epdf) {
                    epdf.printStackTrace();
                    pdfSinFirma = null;
                }
                if (pdfSinFirma != null) {
                    try {
                        current.setPdfSinFirma(pdfSinFirma);
                        ejbFacade.edit(current);
                        msj.append("El registro con número único de trámite: ");
                        msj.append(current.getIdFal().toString());
                    } catch (EntidadException ee2) {

                    }
                } else {
                    msjError.append("El registro con número único de trámite: ");
                    msj.append(current.getIdFal().toString());
                }
                //msj.append("El registro con número único de trámite: ");
                //msj.append(current.getIdFal());
            } catch (EntidadException ee1) {
                ee1.printStackTrace();
                msjError.append("El registro con número único de trámite: ");
                msjError.append(current.getIdFal());
            }
            if (!msjError.toString().equals("")) {
                msjError.append(" no ha sido guardado.");
                JsfUtil.displayMessage(msjError.toString(), JsfUtil.ERROR_MESSAGE);
            }
            if (!msj.toString().equals("")) {
                msj.append(" ha sido guardado.");
                JsfUtil.displayMessage(msj.toString(), JsfUtil.INFO_MESSAGE);
            }
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
            limpiar();
            this.setTabIndex(0);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
        } else {
            JsfUtil.displayMessage("No se pudo recuperar información referente al estado del registro. No se ha finalizado el proceso.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
            limpiar();
            this.setTabIndex(0);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
        }
        RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
    }
    
    public void atrasD(){
         try {
            this.setTabIndex(1);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        } catch (Exception e) {
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se puede regresar a la pestaña anterior.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaDF");
        }
    }

    public void cambiarTblSauregUsuarioList(ValueChangeEvent event) {
        for (TblSauregUsuario item : tblSauregUsuarioList) {
            if (item.getNomUsuario().equals(event.getNewValue())) {
                tblSauregUsuario = item;
                tblSauregUsuario.setAgenciaInUser(userLgn.getAgenciaInUser());
                break;
            }
        }
    }

    public void cambiarMuerteEnEstablecimiento(ValueChangeEvent event) {
        try {
            //provinciasFllcmnt.clear();
            provinciaFllcmnt = null;
            cantonesFllcmnt.clear();
            cantonFllcmnt = null;
            parroquiasFllcmnt.clear();
            parroquiaFllcmnt = null;
            current.setCiudadFllcmntFal(null);
            current.setLocalidadFllcmntFal(null);
            RespuestaMuerteenestablecim item = (RespuestaMuerteenestablecim) event.getNewValue();
            if (item.getIdResMueEnest() == 1) {
                //Provincia
                current.setProvinciaIdFllcmntFal(tblSauregUsuario.getAgenciaInUser().getIdProvincia().getCodDpa());
                current.setProvinciaDsFllcmntFal(tblSauregUsuario.getAgenciaInUser().getIdProvincia().getDerscripcion());
                provinciaFllcmnt = current.getProvinciaIdFllcmntFal() + ".- " + current.getProvinciaDsFllcmntFal();
                //busco los cantones de la provincia
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                params.add(new ParametroConsulta("codDpa", tblSauregUsuario.getAgenciaInUser().getIdProvincia().getCodDpa()));
                params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                try {
                    cantonesFllcmnt = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodDpa", params);
                } catch (EntidadException ec) {
                }
                //canton
                current.setCantonIdFllcmntFal(tblSauregUsuario.getAgenciaInUser().getIdCanton().getCodDpa());
                current.setCantonDsFllcmntFal(tblSauregUsuario.getAgenciaInUser().getIdCanton().getDerscripcion());
                cantonFllcmnt = current.getCantonIdFllcmntFal() + ".- " + current.getCantonDsFllcmntFal();
                //busco las parroquias del canton
                params.clear();
                params.add(new ParametroConsulta("codDpa", tblSauregUsuario.getAgenciaInUser().getIdCanton().getCodDpa()));
                params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                try {
                    parroquiasFllcmnt = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodDpa", params);
                } catch (EntidadException ec) {
                }
                //parroquia
                current.setParroquiaIdFllcmntFal(tblSauregUsuario.getAgenciaInUser().getIdParroquia().getCodDpa());
                current.setParroquiaDsFllcmntFal(tblSauregUsuario.getAgenciaInUser().getIdParroquia().getDerscripcion());
                parroquiaFllcmnt = current.getParroquiaIdFllcmntFal() + ".- " + current.getParroquiaDsFllcmntFal();
                //localidad
                current.setLocalidadFllcmntFal(tblSauregUsuario.getAgenciaInUser().getLocalidad());
                //Direccion
                current.setCiudadFllcmntFal(tblSauregUsuario.getAgenciaInUser().getDireccion());
            }
        } catch (Exception e) {

        }
    }

    public byte[] pdfSinFirma(Fallecido item, TblSauregUsuario usuarioFirma) throws JRException, IOException, SQLException {
        try {

            List<Fallecido> fallecidoList = new ArrayList<Fallecido>();
            fallecidoList.add(item);
            Map<String, Object> parametros = new HashMap<String, Object>();
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

    /*Para calcular la edad del fallecido en base a su fecha de defuncion*/
    public void seleccionFechaFallecimiento(SelectEvent event) {
        try {
            /*System.out.println(madre.getFechaNacimMad().toString() + " ********************************");
             DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
             Date fecha = formatter.parse(madre.getFechaNacimMad().toString());
             madre.setFechaNacimMad(fecha);
             System.out.println("fecha:" + madre.getFechaNacimMad());
             //Calculo de la edad.*/
            Calendar fechaNacimiento = Calendar.getInstance();
            fechaNacimiento.setTime(current.getFechaNacimientoFal());
            Calendar fechaFallecimiento = Calendar.getInstance();
            fechaFallecimiento.setTime(current.getFechaFallecimientoFal());
            int año = fechaFallecimiento.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
            int mes = fechaFallecimiento.get(Calendar.MONTH) - fechaNacimiento.get(Calendar.MONTH);
            int dia = fechaFallecimiento.get(Calendar.DATE) - fechaNacimiento.get(Calendar.DATE);
            if (mes < 0 || (mes == 0 && dia < 0)) {
                año--;
            }
            short edad = (short) año;
            current.setEdadFal(edad);
            /*sabeLeer = null;
             nivelInstr = null;
             getMadre().setFkIdNivelInstr(null);*/
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.displayMessage("No se pudo calcular la edad del fallecido", JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaDF");
        }
    }

    /**
     * Método para cambiar el valor de la variable sabe leer? esto esta en la
     * págiana de la madre
     *
     * @param event
     */
    public void obtenerSabeLeer(AlfabetismoRenavi item) {
        //Reseteo el nivel de instraucción
        nivelesInstruccion = null;
        nivelesInstruccion = new ArrayList<NivelInstruccionRenavi>();
        //Obtengo el valor seleccionado
        if (item != null) {
            if (item.getIdAlfb() == 2) {
                try {
                    NivelInstruccionRenavi instruccion = ejbFacadeNivelInstruccion.find(0);
                    nivelesInstruccion.add(instruccion);
                    current.setFkIdNivelInstruccion(instruccion);
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            } else {
                try {
                    if (current.getEdadFal() >= 10 && current.getEdadFal() <= 11) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad10And11");
                    } else if (current.getEdadFal() >= 12 && current.getEdadFal() <= 14) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad12And14");
                    } else if (current.getEdadFal() >= 15 && current.getEdadFal() <= 16) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad15And16");
                    } else if (current.getEdadFal() >= 17 && current.getEdadFal() <= 22) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad17And22");
                    } else if (current.getEdadFal() >= 23 && current.getEdadFal() <= 49) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    } else if (current.getEdadFal() >= 50) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    }
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

}
