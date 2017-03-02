package ec.gob.digercic.renavi.view;

/**/
import ec.gob.digercic.renavi.ejb.CatalogoProfesionalRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.CatalogoProfesionalRenavi;
/**/
import ec.gob.digercic.renavi.ejb.CatalogoRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogEliminacionFacadeLocal;
import ec.gob.digercic.renavi.entities.MadreRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.MadreRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.AlfabetismoRenavi;
import ec.gob.digercic.renavi.entities.CatalogoRenavi;
import ec.gob.digercic.renavi.entities.EstadoCivilRenavi;
import ec.gob.digercic.renavi.entities.EstadoRegistroRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.LogsEliminacion;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.entities.NacionalidadRenavi;
import ec.gob.digercic.renavi.entities.NivelInstruccionRenavi;
import ec.gob.digercic.renavi.entities.PaisRenavi;
import ec.gob.digercic.renavi.entities.TipoPartoRenavi;
import ec.gob.digercic.renavi.entities.VariableRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.UtilitarioCaracteres;
import ec.gob.digercic.renavi.view.util.UtilitarioNombre;
import ec.gob.digercic.renavi.view.validator.CedulaValidator;
import ec.gob.digercic.renavi.ws.TblSauregUsuario;
import ec.gob.digercic.renavi.ws.cedula.Cedula;
import ec.gob.digercic.renavi.ws.datmadre.Ciudadanos;
import ec.gob.digercic.renavi.ws.datmadre.Persona;
import ec.gob.digercic.saureg.entities.TblSauregUbicacion;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "madreRenaviCDLController")
@ViewScoped
public class MadreRenaviCDLController implements Serializable {

    private MadreRenavi current;
    private List<MadreRenavi> items;
    private StreamedContent imagem;
    private NacidoVivoRenavi hijoAux;
    private Integer tabIndex = 0;
    /**/
    private Boolean flagRol = false;
    private String asistencia;
    private String ruta;

    /**/
    @EJB
    private ec.gob.digercic.renavi.ejb.MadreRenaviFacadeLocal ejbFacade;
    @EJB
    private ec.gob.digercic.renavi.ejb.EstadoRegistroRenaviFacadeLocal ejbFacadeEstadoRegistro;
    @EJB
    private ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal ejbFacadeNacidoVivoRenavi;
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
    private ec.gob.digercic.renavi.ejb.TipoIdentRenaviFacadeLocal ejbFacadeTipIdent;
    @EJB
    private ec.gob.digercic.renavi.ejb.TipoPartoRenaviFacadeLocal ejbFacadeTipoPar;
    @EJB
    private LogAccesosRenaviFacadeLocal ejbLogs;
    @EJB
    private ec.gob.digercic.renavi.ejb.MadreRenaviFacadeLocal ejbFacadeMadre;
    @EJB
    private LogEliminacionFacadeLocal ejbLogsEli;
    @EJB
    private CatalogoRenaviFacadeLocal ejbCatRenavi;
    /**/
    @EJB
    private CatalogoProfesionalRenaviFacadeLocal ejbFacadeCatalogoProfe;
    /**/

    private List<TblSauregUbicacion> provincias;
    private String provincia;
    private List<TblSauregUbicacion> cantones;
    private String canton;
    private List<TblSauregUbicacion> parroquias;
    private List<TipoPartoRenavi> tipparto;
    private String parroquia;
    private String buscarPor = "Cédula";
    private String cedulaBusq;
    private String apellidoUnoBusq;
    private String apellidoDosBusq;
    private String nombreUnoBusq;
    private String nombreDosBusq;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    //Fecha para el registro de auditoria y para validación de calendario
    //en los que se deba ingresar maxdate la fecha actual
    private Date fechaActual;
    private Date fechaP;
    private Date fechaF;
    private Date fechaPrenacimiento;
    private int errtp = 0;
    //Para la madre
    private List<NivelInstruccionRenavi> nivelesInstruccion;
    private List<Persona> personas;
    private JasperPrint jasperPrint;
    private boolean flagInconsistencia = false;
    private List<NacidoVivoRenavi> hijosAux;
    private List<NacidoVivoRenavi> hijosAuxDelete;
    private Short numParto;
    private Short numHijosvivos;
    private Short numHijosVivMuertos;
    private Short numHijosNacierMuertos;
    private Short hijosVivosSistema;
    private int porEliminarNV;
    private int creadosNV;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn;//JJHF CAMBIO LOGIN
    private List<ec.gob.digercic.renavi.ws.TblSauregUsuario> tblSauregUsuarioList;//JJHF CAMBIO LOGIN
    private List<TipoPartoRenavi> itemp;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario tblSauregUsuario; //JJHF CAMBIO LOGIN
    private String nomUsuarioSaureg;
    private List<PaisRenavi> listPais;
    private List<PaisRenavi> listPaisDatMadre;
    private String cedula;
    private String ced;
    private String flag;
    private LogsEliminacion logLgn;
    private List<Object[]> listObj;
    private String observacionA;
    private String chkInscrito = "0";
    private Date fechaInicio;
    private Date fechaHoy;
    private List<CatalogoRenavi> listcat = new ArrayList<CatalogoRenavi>();
    private String malformaciones = "ÚNICA";
    private String criterioBusqueda = null;
    private String edadMadre;

    private List<CatalogoProfesionalRenavi> lstProfesion = new ArrayList<CatalogoProfesionalRenavi>();
    private List<ec.gob.digercic.renavi.ws.TblSauregRol> listaRol = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregRol>();

    //INICIO DFJ
    private boolean tieneMalformacion = false;
    private List malformacionesSeleccionadas;
    private int numMalformacionReal = 0;
    //FIN DFJ
    private List<CatalogoRenavi> listTipoSangre;//DFJ
    private String lugarNacimiento;
    
    /**/
    private ec.gob.digercic.renavi.ws.TblSauregUsuario agenciaMSP;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario test;
    private boolean flagAnulacion = false;
    /**/

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }
    
    public TblSauregUsuario getAgenciaMSP() {
        return agenciaMSP;
    }

    public void setAgenciaMSP(TblSauregUsuario agenciaMSP) {
        this.agenciaMSP = agenciaMSP;
    }
    
    public MadreRenaviCDLController() {
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public List<CatalogoRenavi> getListTipoSangre() {
        return listTipoSangre;
    }

    public void setListTipoSangre(List<CatalogoRenavi> listTipoSangre) {
        this.listTipoSangre = listTipoSangre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaHoy() {
        return fechaHoy;
    }

    public MadreRenavi getSelected() {
        if (current == null) {
            current = new MadreRenavi();
        }
        return current;
    }

    private MadreRenaviFacadeLocal getFacade() {
        return ejbFacade;
    }

    public List<PaisRenavi> getListPais() {
        if (listPais == null) {
            listPais = new ArrayList<PaisRenavi>();
        }
        return listPais;
    }

    public List<PaisRenavi> getListPaisDatMadre() {
        if (listPaisDatMadre == null) {
            listPaisDatMadre = new ArrayList<PaisRenavi>();
        }
        return listPaisDatMadre;
    }

    public void cambiarPaisDatMadre(ValueChangeEvent event) {
        PaisRenavi item = (PaisRenavi) event.getNewValue();
        if (item != null) {
            hijoAux.setCampoInecCodPaisMad(item.getCodigPais());
        } else {
            hijoAux.setCampoInecCodPaisMad(null);
        }
    }

    public List<CatalogoRenavi> getListcat() {
        return listcat;
    }

    public void setListcat(List<CatalogoRenavi> listcat) {
        this.listcat = listcat;
    }

    public List<MadreRenavi> getItems() {
        try {
            items = getFacade().findAll();
            return items;
        } catch (EntidadException ee) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            return null;
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

    public List<TipoPartoRenavi> getItemp() {
        this.validarTipoParto();
        return itemp;
    }

    public void setItemp(List<TipoPartoRenavi> itemp) {
        this.itemp = itemp;
    }

    public String getMalformaciones() {
        return malformaciones;
    }

    public void setMalformaciones(String malformaciones) {
        this.malformaciones = malformaciones;
    }

    public boolean isTieneMalformacion() {
        return tieneMalformacion;
    }

    public void setTieneMalformacion(boolean tieneMalformacion) {
        this.tieneMalformacion = tieneMalformacion;
    }

    public List<String> getMalformacionesSeleccionadas() {
        return malformacionesSeleccionadas;
    }

    public void setMalformacionesSeleccionadas(List<String> malformacionesSeleccionadas) {
        this.malformacionesSeleccionadas = malformacionesSeleccionadas;
    }
    
    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }

    /**
     * Se ejecuta cuando selecciona malformaciones de la Lista de Seleccion
     *
     * @param evt
     */
    public void seleccionarMalformacion(AjaxBehaviorEvent evt) {

        if (malformacionesSeleccionadas != null && malformacionesSeleccionadas.isEmpty() == false) {
            //Solo permite seleccionar una opcion  
            if (malformaciones.equals("ÚNICA")) {
                if (malformacionesSeleccionadas.size() > 1) {
                    Object str_aux_ult = malformacionesSeleccionadas.get(0);
                    malformacionesSeleccionadas.clear();
                    malformacionesSeleccionadas.add(str_aux_ult);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:fkIdMalformacionesC");
                }
            }
            numMalformacionReal = malformacionesSeleccionadas.size();
        } else {
            numMalformacionReal = 0;
        }
    }

    public void cambiarTipoMalformacion(AjaxBehaviorEvent evt) {
        malformacionesSeleccionadas = null;
        numMalformacionReal = 0;
    }

    /**
     * Se ejecuta cuando se cambia la opcion de tiene malformaciones
     *
     * @param evt
     */
    public void cambiarTieneMalformacion(AjaxBehaviorEvent evt) {
        malformacionesSeleccionadas = null;
        malformaciones = null;
        malformaciones = "ÚNICA";
        numMalformacionReal = 0;
        //Carga catalogo de malformaciones dependiendo del sexo 
        if (tieneMalformacion) {
            listcat = ejbCatRenavi.getCatalogo("MALFORMACIONES", "'0','" + hijoAux.getFkIdSexoNv().getIdSexo() + "'");
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

    public Short getNumParto() {
        return numParto;
    }

    public Short getNumHijosvivos() {
        return numHijosvivos;
    }

    public Short getNumHijosVivMuertos() {
        return numHijosVivMuertos;
    }

    public Short getNumHijosNacierMuertos() {
        return numHijosNacierMuertos;
    }

    public ec.gob.digercic.renavi.ws.TblSauregUsuario getUserLgn() {//JJHF CAMBIO LOGIN
        return userLgn;//JJHF CAMBIO LOGIN
    }

    public void setUserLgn(ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn) {//JJHF CAMBIO LOGIN
        this.userLgn = userLgn;//JJHF CAMBIO LOGIN
    }

    public List<ec.gob.digercic.renavi.ws.TblSauregUsuario> getTblSauregUsuarioList() {
        return tblSauregUsuarioList;
    }

    public void cambiarTblSauregUsuarioList(ValueChangeEvent event) {//JJHF CAMBIO LOGIN
        for (ec.gob.digercic.renavi.ws.TblSauregUsuario item : tblSauregUsuarioList) {//JJHF CAMBIO LOGIN
            if (item.getNomUsuario().equals(event.getNewValue())) {//JJHF CAMBIO LOGIN
                tblSauregUsuario = item;//JJHF CAMBIO LOGIN
                tblSauregUsuario.setAgenciaInUser(userLgn.getAgenciaInUser());//JJHF CAMBIO LOGIN
                break;//JJHF CAMBIO LOGIN
            }
        }
    }
    /*Cambio para identificar los roles de los usuarios  FWTM  25-02-2016*/

    private List<ec.gob.digercic.renavi.ws.TblSauregRol> rolesUsuarios(java.lang.String userI, String agencia) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        List<ec.gob.digercic.renavi.ws.TblSauregRol> lstUserRol;
        lstUserRol = port.getRolesByUsuarioByAgenciaBySistema(userI, agencia, "1");
        return lstUserRol;
    }
    /*25-02-2016*/

    public ec.gob.digercic.renavi.ws.TblSauregUsuario getTblSauregUsuario() {
        return tblSauregUsuario;
    }

    public void setTblSauregUsuario(ec.gob.digercic.renavi.ws.TblSauregUsuario tblSauregUsuario) {
        this.tblSauregUsuario = tblSauregUsuario;
    }

    public String getNomUsuarioSaureg() {
        return nomUsuarioSaureg;
    }

    public void setNomUsuarioSaureg(String nomUsuarioSaureg) {
        this.nomUsuarioSaureg = nomUsuarioSaureg;
    }

    public String prepareEdit() {
        return "Edit";
    }

    public List<TipoPartoRenavi> getTipparto() {
        return tipparto;
    }

    public void setTipparto(List<TipoPartoRenavi> tipparto) {
        this.tipparto = tipparto;
    }
    
    public String getEdadMadre() {
        return edadMadre;
    }
    
    public void setEdadMadre (String edadMadre) {
        this.edadMadre = edadMadre;
    }

    /**
     * Comparador para ordenar la lista de hijos de acuerdo al número de
     * producto del embrazo
     */
    private static Comparator<NacidoVivoRenavi> COMPARATOR = new Comparator<NacidoVivoRenavi>() {
        // This is where the sorting happens.
        @Override
        public int compare(NacidoVivoRenavi n1, NacidoVivoRenavi n2) {
            return n1.getNumeroProductoNacViv().compareTo(n2.getNumeroProductoNacViv());
        }
    };

    /**
     * Método para guardar la información en el tab de la madre
     */
    public void saveM() {
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
            log.setAccion("REVIT. MADRES REVIT. GUARDA MADRES");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);

            LogsEliminacion logEli = new LogsEliminacion();

        } catch (EntidadException e) {
            e.printStackTrace();
        }
        try {
            hijoAux.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_MADRE));
            if (hijosAux == null) {
                hijoAux.setFkIdEstFir(ejbFacadeEstadoFirma.find(JsfUtil.STAT_FIR_SIN));
                hijoAux.setFkAgenciaSaureg(Long.valueOf(userLgn.getAgenciaInUser().getCodMsp()));
                hijoAux.setFkUsuSaureg(userLgn.getNomUsuario());
                //Creo la madre si no existe en la base de datos del revit
                if (current.getIdMad() == null) {
                    //Asigno el estado del registro
                    current.setStatus(JsfUtil.ESTADO_REG_ACTIVO);
                    current.setFkIdTipoIdent(ejbFacadeTipIdent.find(JsfUtil.STAT_IDENT));
                    getFacade().create(current);
                }
                hijoAux.setFkCedulMad(current);
                //Asigno el estado del registro
                hijoAux.setStatus(JsfUtil.ESTADO_REG_ACTIVO);
                //Guardo los registros
                hijosAux = new ArrayList<NacidoVivoRenavi>();
                try {
                    for (int i = 0; i < hijoAux.getHijosNaciervivSistema(); i++) {
                        NacidoVivoRenavi temp = new NacidoVivoRenavi();
                        temp = (NacidoVivoRenavi) BeanUtils.cloneBean(hijoAux);
                        //Asigno el que producto del embraso es
                        temp.setNumeroProductoNacViv((short) (i + 1));

                        ejbFacadeNacidoVivoRenavi.create(temp);
                        hijosAux.add(temp);
                    }
                    //Ordeno por numero de producto la lista hijosAux
                    Collections.sort(hijosAux, COMPARATOR);
                    //Seteo la variable de hijosVivosSistema
                    hijosVivosSistema = hijoAux.getHijosNaciervivSistema();
                    hijoAux = null;
                    //Seteo el tabindex
                    this.setTabIndex(1);
                    RequestContext.getCurrentInstance().update("form_cont:contTab");
                    //Creo el Log
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("MadreRenaviCreado"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                } catch (Exception euno) {
                    euno.printStackTrace();
                    JsfUtil.displayMessage("No se pudo guardar la información de la madre. Contactese con el administrador del sistema.", JsfUtil.ERROR_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                    limpiar();
                }
            } else {
                //Cuando los hijos vivos son iguales
                if (hijoAux.getHijosNaciervivSistema() == hijosVivosSistema) {
                    hijosAux = actualizarDatosNacidos(hijosAux, hijoAux);
                    if (hijosAux != null) {
                        hijoAux = null;
                        //Seteo el tabindex
                        this.setTabIndex(1);
                        RequestContext.getCurrentInstance().update("form_cont:contTab");
                        //Creo el Log
                        JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("MadreRenaviCreado"), JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                    } else {
                        JsfUtil.displayMessage("No se pudo guardar la información de la madre. Contáctese con el administrador del sistema.", JsfUtil.ERROR_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                        limpiar();
                    }

                } else if (hijoAux.getHijosNaciervivSistema() < hijosVivosSistema) {
                    hijosAux = eliminarDatosNacidos(hijosAux, hijoAux);
                    if (hijosAux != null) {
                        if (hijosAux.size() == ((int) hijoAux.getHijosNaciervivSistema())) {
                            hijosAux = actualizarDatosNacidos(hijosAux, hijoAux);
                            if (hijosAux != null) {
                                hijoAux = null;
                                //Seteo el tabindex
                                this.setTabIndex(1);
                                RequestContext.getCurrentInstance().update("form_cont:contTab");
                                JsfUtil.displayMessage("Se han eliminado " + (hijosVivosSistema - hijosAux.size()) + " regstro(s) de nacido vivo. " + ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("MadreRenaviCreado"), JsfUtil.INFO_MESSAGE);
                                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                                //Seteo la variable de hijosVivosSistema
                                hijosVivosSistema = hijosAux.get(0).getHijosNaciervivSistema();
                            } else {
                                JsfUtil.displayMessage("No se pudo guardar la información de la madre. Contáctese con el administrador del sistema.", JsfUtil.ERROR_MESSAGE);
                                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                                limpiar();
                            }

                        } else {
                            porEliminarNV = (hijosAux.size() - hijoAux.getHijosNaciervivSistema());
                            JsfUtil.displayMessage("Debe eliminar " + porEliminarNV + " registro(s) de nacido vivo, debido a que se notifica que solo hay " + hijoAux.getHijosNaciervivSistema() + " hijo(s) nacido(s) vivo(s) en este parto.", JsfUtil.INFO_MESSAGE);
                            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsHijosDelete");
                            RequestContext.getCurrentInstance().execute("PF('eliminarNVDialog').show();");
                        }
                    } else {
                        JsfUtil.displayMessage("Se ha producido un error al actualizar la información. Por favor vuelva a intentarlo.", JsfUtil.WARN_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                    }
                } else {
                    int porCrear = (hijoAux.getHijosNaciervivSistema() - hijosVivosSistema);
                    hijosAux = crearDatosNacidos(hijosAux, hijoAux, porCrear);
                    if (hijosAux != null) {
                        hijoAux = null;
                        //Seteo el tabindex
                        this.setTabIndex(1);
                        RequestContext.getCurrentInstance().update("form_cont:contTab");
                        //Actualizo la tabla de hijos en el tab de nacido vivo
                        RequestContext.getCurrentInstance().update(":form_cont:contTab:pdtTblHijos");
                        //Creo el Log
                        JsfUtil.displayMessage("Se han creado " + creadosNV + " regstro(s) de nacido vivo " + ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("MadreRenaviCreado"), JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                        //Actualizo el nuevo producto del embrazo
                        hijosVivosSistema = hijosAux.get(0).getHijosNaciervivSistema();
                    } else {
                        JsfUtil.displayMessage("No se pudo guardar la información de la madre. Contáctese con el administrador del sistema.", JsfUtil.ERROR_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                        limpiar();
                    }
                }
            }
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        } catch (Exception e) {
            e.printStackTrace();
            limpiar();
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se pudo guardar la información de la madre. Contáctese con el administrador del sistema.", JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
        }
        this.validarNac();
    }

    /**
     * Método para cambiar el pais de residencia habitual de la madre. Cuando es
     * ecuador, hay que elegir: provincia, cantón y parroquia. Si es diferente
     * de Ecuador, solo se ingresa dirección y localidad.
     *
     * @param event
     */
    public void cambiarPaisResidnMad(ValueChangeEvent event) {
        try {
            PaisRenavi pais = (PaisRenavi) event.getNewValue();
            if (!pais.getIdPais().equals(JsfUtil.COD_PAIS_EC)) {
                hijoAux.setProviRcdscNacViv(null);
                hijoAux.setProviRcidNacViv(null);
                provincia = null;
                hijoAux.setCantnRcdscNacViv(null);
                hijoAux.setCantnRcidNacViv(null);
                canton = null;
                hijoAux.setPaarqRcdscNacViv(null);
                hijoAux.setParrqRcidNacViv(null);
                parroquia = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que actualiza registros de nacido vivo cuando se regresa al tab de
     * la madre
     *
     * @param ninios
     * @param ninio
     * @return
     */
    public List<NacidoVivoRenavi> actualizarDatosNacidos(List<NacidoVivoRenavi> ninios,
            NacidoVivoRenavi ninio) {
        try {
            List<NacidoVivoRenavi> hijosAuxTemp = new ArrayList<NacidoVivoRenavi>();
            int numerProducto = 1;
            for (NacidoVivoRenavi item : ninios) {
                NacidoVivoRenavi temp = new NacidoVivoRenavi();
                temp = (NacidoVivoRenavi) BeanUtils.cloneBean(ninio);
                temp.setIdNacViv(item.getIdNacViv());
                //Reseteo los nombres, apellidos y otros datos
                temp.setNombrNacViv(item.getNombrNacViv());
                //temp.setApellNacViv(item.getApellNacViv());
                temp.setFkIdSexoNv(item.getFkIdSexoNv());
                temp.setTallaNacViv(item.getTallaNacViv());
                temp.setPesoNacViv(item.getPesoNacViv());
                temp.setFechaNacimNacViv(item.getFechaNacimNacViv());
                temp.setFkIdTipPar(item.getFkIdTipPar());
                temp.setApgar1NacViv(item.getApgar1NacViv());
                temp.setApgar2NacViv(item.getApgar2NacViv());
                temp.setNumeroProductoNacViv((short) (numerProducto));
                temp.setMalformacionesConge(item.getMalformacionesConge());//DFJ
                temp.setObsrvNacViv(item.getObsrvNacViv());//DFJ
                temp.setTipoSangreNacViv(item.getTipoSangreNacViv());//DFJ    
                temp.setLugNacEspecifique(item.getLugNacEspecifique());//DFJ
                ejbFacadeNacidoVivoRenavi.edit(temp);
                //Aniado el objeto con los datos actuializados
                hijosAuxTemp.add(temp);
                numerProducto++;
            }
            //Ordeno por numero de producto la lista hijosAux
            Collections.sort(hijosAuxTemp, COMPARATOR);
            return hijosAuxTemp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método que elimina registros de nacido vivo cuando se regresa al tab de
     * la madre y el número de hijos es menor que el anterior elejido. (aquí
     * elimina automaticamente los registro con el criterio de datos completos)
     *
     * @param ninios
     * @param ninio
     * @return
     */
    public List<NacidoVivoRenavi> eliminarDatosNacidos(List<NacidoVivoRenavi> ninios,
            NacidoVivoRenavi ninio) {
        try {
            int resta = hijosVivosSistema - ninio.getHijosNaciervivSistema();
            int restaRcrrIncrmnt = 0;
            List<NacidoVivoRenavi> eliminados = new ArrayList<NacidoVivoRenavi>();
            for (NacidoVivoRenavi item : ninios) {
                if (restaRcrrIncrmnt == resta) {
                    break;
                } else {
                    int dcsnv = datosCompletosSaveNV(item);
                    try {
                        if (dcsnv == 2 || dcsnv == 0) {
                            ejbFacadeNacidoVivoRenavi.remove(item);
                            //aniado a la lista de eliminados
                            eliminados.add(item);
                            restaRcrrIncrmnt++;
                        }
                    } catch (Exception euno) {
                        euno.printStackTrace();
                    }
                }
            }
            for (NacidoVivoRenavi itemDelete : eliminados) {
                ninios.remove(itemDelete);
            }
            return ninios;
        } catch (Exception edos) {
            edos.printStackTrace();
            return null;
        }
    }

    /**
     * Método para crear registros de nacido vivo cuando se regresa al tab de la
     * madre y el número de hijos es mayor que el anterior elejido.
     *
     * @param ninios
     * @param ninio
     * @param porCrear
     * @return
     */
    public List<NacidoVivoRenavi> crearDatosNacidos(List<NacidoVivoRenavi> ninios,
            NacidoVivoRenavi ninio, int porCrear) {
        try {
            int numerProducto = 1;
            List<NacidoVivoRenavi> hijosAuxTemp = new ArrayList<NacidoVivoRenavi>();
            for (NacidoVivoRenavi item : ninios) {
                NacidoVivoRenavi tempr = new NacidoVivoRenavi();
                tempr = (NacidoVivoRenavi) BeanUtils.cloneBean(ninio);
                tempr.setIdNacViv(item.getIdNacViv());
                //Reseteo los nombres, apellidos y otros datos
                tempr.setNombrNacViv(item.getNombrNacViv());
                tempr.setApellNacViv(item.getApellNacViv());
                tempr.setFkIdSexoNv(item.getFkIdSexoNv());
                tempr.setTallaNacViv(item.getTallaNacViv());
                tempr.setPesoNacViv(item.getPesoNacViv());
                tempr.setFechaNacimNacViv(item.getFechaNacimNacViv());
                tempr.setFkIdTipPar(item.getFkIdTipPar());
                tempr.setApgar1NacViv(item.getApgar1NacViv());
                tempr.setApgar2NacViv(item.getApgar2NacViv());
                //Actualizo  el objeto
                tempr.setNumeroProductoNacViv((short) numerProducto);
                ejbFacadeNacidoVivoRenavi.edit(tempr);
                //Aniado el objeto con los datos actuializados
                hijosAuxTemp.add(tempr);
                numerProducto++;
            }
            //Creo los registros que faltan
            creadosNV = 0;
            for (int pc = 0; pc < porCrear; pc++) {
                NacidoVivoRenavi tempcc = new NacidoVivoRenavi();
                tempcc = (NacidoVivoRenavi) BeanUtils.cloneBean(ninio);
                //Reseteo los nombres, apellidos y otros datos
                tempcc.setNumeroProductoNacViv((short) numerProducto);
                tempcc.setNombrNacViv(null);
                //tempcc.setApellNacViv(null);
                tempcc.setFkIdSexoNv(null);
                tempcc.setTallaNacViv(null);
                tempcc.setPesoNacViv(null);
                tempcc.setFechaNacimNacViv(null);
                tempcc.setFkIdTipPar(null);
                tempcc.setApgar1NacViv(null);
                tempcc.setApgar2NacViv(null);

                //INICIO DFJ
                //Pone el estado del primer hijo 
                if (ninios.isEmpty() == false) {
                    NacidoVivoRenavi ncaux = ninios.get(0);
                    String c = ncaux.getFkIdEstReg().toString();
                    if (c.equals("DATOS INCOMPLETOS") || c.equals("DATOS INCOMPLETOS DE LA MADRE") || c.equals("DATOS INCOMPLETOS NACIDO VIVO")) {
                        tempcc.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_NACVIVO));
                    } else {
                        tempcc.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_DOCTOR));
                        if (c.equals("REGISTRO INCOMPLETO ANULACION") || c.equals("REGISTRO ANULACION COMPLETO")) {
                            tempcc.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_ANULADO_FE));
                        }
                    }
                }
                //FIN DFJ

                //Creo el objeto
                ejbFacadeNacidoVivoRenavi.create(tempcc);
                //Aniado el objeto con los datos actuializados
                creadosNV++;
                hijosAuxTemp.add(tempcc);
                numerProducto++;
            }
            Collections.sort(hijosAuxTemp, COMPARATOR);
            return hijosAuxTemp;
        } catch (Exception edos) {
            edos.printStackTrace();
            return null;
        }
    }

    /**
     * Método que elimina registros de nacido vivo cuando se regresa al tab de
     * la madre y el número de hijos es menor que el anterior elejido. (aquí
     * presenta una pantalla para que elija el que desea eliminar)
     */
    public void deleteHijosAuxDelete() {
        try {
            if (!hijosAuxDelete.isEmpty()) {
                //Si elegio el numero correcto de registros para eliminar
                int ret = 0;
                if (porEliminarNV == hijosAuxDelete.size()) {
                    List<NacidoVivoRenavi> tempRemove = new ArrayList<NacidoVivoRenavi>();
                    for (NacidoVivoRenavi item : hijosAuxDelete) {
                        try {
                            //INICIO DFJ                               
                            //Elimina anulados
                            ejbFacadeNacidoVivoRenavi.eliminarAnulados(item);
                            //Elimina las malformaciones
                            ejbFacadeNacidoVivoRenavi.eliminarMalformaciones(item);
                            ejbFacadeNacidoVivoRenavi.remove(item);
                            //FIN DFJ
                            //Elimino de la lista hijosAux
                            hijosAux.remove(item);
                            //Aniado a la lista de eliminados
                            tempRemove.add(item);
                            ret++;
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                    //elimino la lista de hijosDelete con ayuda de la lista de eliminados
                    for (NacidoVivoRenavi itemR : tempRemove) {
                        hijosAuxDelete.remove(itemR);
                    }
                    List<NacidoVivoRenavi> hijosAuxTemp = new ArrayList<NacidoVivoRenavi>();
                    int numProducto = 1;
                    for (NacidoVivoRenavi item : hijosAux) {
                        NacidoVivoRenavi tempr = new NacidoVivoRenavi();
                        tempr = (NacidoVivoRenavi) BeanUtils.cloneBean(hijoAux);
                        tempr.setIdNacViv(item.getIdNacViv());
                        //Reseteo los nombres, apellidos y otros datos
                        tempr.setNombrNacViv(item.getNombrNacViv());
                        tempr.setApellNacViv(item.getApellNacViv());
                        tempr.setFkIdSexoNv(item.getFkIdSexoNv());
                        tempr.setTallaNacViv(item.getTallaNacViv());
                        tempr.setPesoNacViv(item.getPesoNacViv());
                        tempr.setFechaNacimNacViv(item.getFechaNacimNacViv());
                        tempr.setApgar1NacViv(item.getApgar1NacViv());
                        tempr.setApgar2NacViv(item.getApgar2NacViv());
                        //Actualizo  el objeto
                        tempr.setNumeroProductoNacViv((short) (numProducto));
                        ejbFacadeNacidoVivoRenavi.edit(tempr);
                        //Aniado el objeto con los datos actuializados
                        hijosAuxTemp.add(tempr);
                        numProducto++;
                    }
                    //Remuevo el hijo con los datos desactualizados
                    hijosAux = null;
                    hijosAux = hijosAuxTemp;
                    //Ordeno por número de producto la lista hijosAux
                    Collections.sort(hijosAux, COMPARATOR);
                    RequestContext.getCurrentInstance().update(":form_cont:contTab:pdtTblHijosDelete");
                    if (hijosAuxDelete.isEmpty()) {
                        hijosVivosSistema = hijosAux.get(0).getHijosNaciervivSistema();
                        RequestContext.getCurrentInstance().update(":form_cont:contTab:pdtTblHijos");
                        RequestContext.getCurrentInstance().execute("PF('eliminarNVDialog').hide();");
                        //Seteo el tabindex
                        this.setTabIndex(1);
                        RequestContext.getCurrentInstance().update("form_cont:contTab");
                        //Muestro el mensaje de datos de madre guardados
                        JsfUtil.displayMessage("Se han eliminado " + ret + " registro(s) de nacido vivo. " + ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("MadreRenaviCreado"), JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                        hijoAux = null;
                        hijosAuxDelete = null;
                    } else {
                        int faltan = porEliminarNV - ret;
                        JsfUtil.displayMessage("Falta(n) por eliminar " + faltan + " registro(s). Por favor intentarlo nuevamente.", JsfUtil.WARN_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsHijosDelete");
                    }
                } else {
                    JsfUtil.displayMessage("Unicamente se deben eliminar " + porEliminarNV + " registro(s). Por favor intentarlo nuevamente.", JsfUtil.WARN_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsHijosDelete");
                }
            } else {
                JsfUtil.displayMessage("No ha seleccionado ningún registro. Por favor intentarlo nuevamente.", JsfUtil.WARN_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsHijosDelete");
            }
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        } catch (Exception e) {
            RequestContext.getCurrentInstance().execute("PF('eliminarNVDialog').hide();");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se han borrado los registros seleccionados.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
        }
    }

    public int datosCompletosSaveNV(NacidoVivoRenavi nv) {
        if (nv.getNombrNacViv() != null && nv.getApellNacViv() != null
                && nv.getFechaNacimNacViv() != null
                && nv.getSemanGstcnNacViv() != null
                && nv.getFkIdSexoNv() != null
                && nv.getPesoNacViv() != null
                && nv.getTallaNacViv() != null
                && nv.getApgar1NacViv() != null
                && nv.getApgar2NacViv() != null) {
            return 1;
        } else if ((nv.getFechaNacimNacViv() != null
                && nv.getSemanGstcnNacViv() != null)) {
            return 2;
        }
        return 0;
    }

    public void completarDatosNV() {
        try {
            malformaciones = null; //DFJ 
            malformacionesSeleccionadas = null; //DFJ
            Short numeroProducto = new Short(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numeroProducto"));
            for (NacidoVivoRenavi item : hijosAux) {
                if (item.getNumeroProductoNacViv().equals(numeroProducto)) {
                    hijoAux = (NacidoVivoRenavi) BeanUtils.cloneBean(item);
                    malformaciones = hijoAux.getMalformacionesConge();  //DFJ
                    malformacionesSeleccionadas = ejbFacadeNacidoVivoRenavi.getMalformaciones(hijoAux);//DFJ
                    numMalformacionReal = malformacionesSeleccionadas.size();//DFJ
                    break;
                }
            }
            //DFJ  
            tieneMalformacion = malformacionesSeleccionadas == null ? false : !malformacionesSeleccionadas.isEmpty();
            if (tieneMalformacion) {
                listcat = ejbCatRenavi.getCatalogo("MALFORMACIONES", "'0','" + hijoAux.getFkIdSexoNv().getIdSexo() + "'");
            }
            //FIN DFJ 
            RequestContext.getCurrentInstance().execute("PF('hijoDialog').show();");
            RequestContext.getCurrentInstance().update("form_cont:contTab:idHijoDialog");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        } catch (Exception ee) {
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorConsulta"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaNV");
        }
    }

    /**
     * Guarda los objetos y pasa al tab de hijos
     *
     * @return
     */
    public void updateHijo() {
        try {
            if (hijoAux.getFkIdEstReg().getIdEstReg().equals(JsfUtil.STAT_DAT_NACVIVO)) {
                hijoAux.setMalformacionesConge(malformaciones);
                hijoAux.setLugNacEspecifique(lugarNacimiento); //dfj
                ejbFacadeNacidoVivoRenavi.edit(hijoAux);
            } else {
                hijoAux.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_NACVIVO));
                hijoAux.setMalformacionesConge(malformaciones);
                hijoAux.setLugNacEspecifique(lugarNacimiento); //dfj
                ejbFacadeNacidoVivoRenavi.edit(hijoAux);
            }
            ejbFacadeNacidoVivoRenavi.agregarMalformaciones(hijoAux, malformacionesSeleccionadas);//DFJ

            //actualizo la lista de hijosAux
            for (NacidoVivoRenavi rem : hijosAux) {
                if (rem.getIdNacViv().equals(hijoAux.getIdNacViv())) {
                    hijosAux.remove(rem);
                    break;
                }
            }
            hijosAux.add(hijoAux);
            Collections.sort(hijosAux, COMPARATOR);
            RequestContext.getCurrentInstance().update("form_cont:contTab:pdtTblHijos");
            hijoAux = null;
            RequestContext.getCurrentInstance().execute("PF('hijoDialog').hide()");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        } catch (Exception ee) {
            /*JsfUtil.removeSessionBackingBean("madreRenaviCDLController");
             this.setTabIndex(0);
             RequestContext.getCurrentInstance().update("form_cont:contTab");*/
            ee.printStackTrace();
            hijoAux = null;
            RequestContext.getCurrentInstance().execute("PF('hijoDialog').hide()");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaNV");
        }
    }

    public void cancelUpdateHijo() {
        hijoAux = null;
        RequestContext.getCurrentInstance().reset("form_cont:contTab:idHijoDialog");//DFJ
    }

    //JJHF CAMBIO LOGIN
    private List<ec.gob.digercic.renavi.ws.TblSauregUsuario> UsuariosByAgenciaAndPagina(java.lang.String idcodmsp) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario usuario = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
        List<ec.gob.digercic.renavi.ws.TblSauregUsuario> ListUsuarios;
        ListUsuarios = port.getUsuariosByAgenciaMSP(idcodmsp);
        return ListUsuarios;

    }

    //JJHF CAMBIO LOGIN
    public void saveNV() {
        int err = this.validarFechaN();
        this.validarParto();
        if (err == 0 && errtp == 0) {
            try {
                int contador = 0;
                for (Integer i = 0; i < hijosAux.size(); i++) {
                    if (datosCompletosSaveNV(hijosAux.get(i)) == 1) {
                        contador++;
                    }
                }
                if (contador == hijosAux.size()) {
                    tblSauregUsuarioList = this.UsuariosByAgenciaAndPagina(userLgn.getAgenciaInUser().getCodMsp());//JJHF CAMBIO LOGIN
                    for (ec.gob.digercic.renavi.ws.TblSauregUsuario item : tblSauregUsuarioList) {//JJHF CAMBIO LOGIN
                        if (item.getNomUsuario().equals(userLgn.getNomUsuario())) {
                            try {
                                tblSauregUsuario = (ec.gob.digercic.renavi.ws.TblSauregUsuario) BeanUtils.cloneBean(userLgn);//JJHF CAMBIO LOGIN
                                nomUsuarioSaureg = tblSauregUsuario.getNomUsuario();
                            } catch (Exception eu) {
                                eu.printStackTrace();
                            } finally {
                                break;
                            }
                        }
                    }
                    hijoAux = (NacidoVivoRenavi) BeanUtils.cloneBean(hijosAux.get(0));
                    this.setTabIndex(2);
                    RequestContext.getCurrentInstance().update("form_cont:contTab");
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NacidosVivosRenaviCreados"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaNV");
                } else {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NacidoVivoRenaviFaltanDatos"), JsfUtil.WARN_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaNV");
                }
                RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            } catch (Exception e) {
                e.printStackTrace();
                RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
                JsfUtil.displayMessage("No se puede continuar con el proceso de registro de datos.", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaNV");
            }
        } else {
            if (err == 1) {
                JsfUtil.displayMessage("Fecha de Nacimiento Incorrecta, Solo puede existir un día de diferencia en la fechas de nacimiento de los nacidos vivos.", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaNV");
            }
            if (errtp == 1) {
                JsfUtil.displayMessage("Tipo de Parto Incorrecto, si seleccionó parto por cesárea, todos los demas regitros deben ser del mismo tipo.", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaNV");
            }
        }

        for (NacidoVivoRenavi nv : hijosAux) {
            if (!nv.getNombrNacViv().contains("-" + nv.getNumeroProductoNacViv().toString())) {
                StringBuilder nombrenv = new StringBuilder();
                nombrenv.append(nv.getNombrNacViv());
                nombrenv.append("-");
                nombrenv.append(nv.getNumeroProductoNacViv().toString());
                nv.setNombrNacViv(nombrenv.toString());
                try {
                    ejbFacadeNacidoVivoRenavi.edit(nv);
                } catch (EntidadException ex) {
                    Logger.getLogger(MadreRenaviCDLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public void atrasNV() {
        try {
            hijoAux = (NacidoVivoRenavi) BeanUtils.cloneBean(hijosAux.get(0));
            hijoAux.setIdNacViv(null);
            current = hijoAux.getFkCedulMad();
            this.setTabIndex(0);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        } catch (Exception e) {
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se puede regresar a la pestaña de la madre.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaNV");
        }
    }

    /**
     * Finaliza la inscripcion del nacido vivo
     *
     * @return
     */
    public void saveD() {
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
            log.setAccion("REVIT. MADRES REVIT. GUARDA USUARIO");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        //para control si la madre es indocumentada o documentada
        boolean identif = false;
        if (current.getCedulMad() != null) {
            identif = true;
        }
        EstadoRegistroRenavi estado = new EstadoRegistroRenavi();
        try {
            estado = ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_DOCTOR);
        } catch (EntidadException ee) {
            estado = null;
        }
        if (estado != null) {
            StringBuilder msjError = new StringBuilder();
            StringBuilder msj = new StringBuilder();
            for (Integer i = 0; i < hijosAux.size(); i++) {
                try {
                    hijosAux.get(i).setFkIdEstReg(estado);
                    //Seteo la observación de la persona que atendió el parto.
                    hijosAux.get(i).setObsrvAtprtNacViv(hijoAux.getObsrvAtprtNacViv());
                    //Seteo quien debe firmar el pdf
                    hijosAux.get(i).setFkUsuFirmaSaureg(tblSauregUsuario.getNomUsuario());
                    hijosAux.get(i).setFkAgenciaFirmaSaureg(Long.valueOf(tblSauregUsuario.getAgenciaInUser().getCodMsp()));

                    ejbFacadeNacidoVivoRenavi.edit(hijosAux.get(i));
                    byte[] pdf;
                    try {
                        pdf = this.pdfSinFirma(hijosAux.get(i), tblSauregUsuario);
                    } catch (Exception epdf) {
                        pdf = null;
                    }
                    if (pdf != null) {
                        //Seteo el pdf generado y edito el registro con el pdf generado
                        try {
                            hijosAux.get(i).setPdfSinFirmaNacViv(pdf);
                            ejbFacadeNacidoVivoRenavi.edit(hijosAux.get(i));
                            if (msj.toString().equals("")) {
                                msj.append("Los registros con código: " + hijosAux.get(i).getIdNacViv() + ", ");
                            } else {
                                msj.append(hijosAux.get(i).getIdNacViv() + ", ");
                            }
                        } catch (EntidadException ee2) {
                            if (msjError.toString().equals("")) {
                                msjError.append("Los registros con código: " + hijosAux.get(i).getIdNacViv() + ", ");
                            } else {
                                msjError.append(hijosAux.get(i).getIdNacViv() + ", ");
                            }
                        }
                    } else {
                        if (msjError.toString().equals("")) {
                            msjError.append("Los registros con código: " + hijosAux.get(i).getIdNacViv() + ", ");
                        } else {
                            msjError.append(hijosAux.get(i).getIdNacViv() + ", ");
                        }
                    }
                } catch (Exception ee1) {
                    ee1.printStackTrace();
                    msjError.append("Los registros con código: " + hijosAux.get(i).getIdNacViv() + ", ");
                }
            }
            if (!msjError.toString().equals("")) {
                msjError.append("no han sido guardados.");
                JsfUtil.displayMessage(msjError.toString(), JsfUtil.ERROR_MESSAGE);
            }
            if (!msj.toString().equals("")) {
                msj.append("han sido guardados.");
                JsfUtil.displayMessage(msj.toString(), JsfUtil.INFO_MESSAGE);
            }
            //Para el envio a la creación de nuevos registros, tanto para madres documentadas como indocumentadas
            if (!identif) {
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
                RequestContext.getCurrentInstance().execute("PF('cdIndocumentada').show()");
            } else {
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
                limpiar();
                this.setTabIndex(0);
                RequestContext.getCurrentInstance().update("form_cont:contTab");
            }
        } else {
            JsfUtil.displayMessage("No se pudo recuperar información referente al estado del registro. No se ha finalizado el proceso.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
            limpiar();
            this.setTabIndex(0);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
        }
    }

    public void atrasD() {
        try {
            this.setTabIndex(1);
            RequestContext.getCurrentInstance().update("form_cont:contTab");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        } catch (Exception e) {
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage("No se puede regresar a la pestaña del nacido vivo.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
        }
    }

    public String destroy() {
        try {
            Integer id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            current = getFacade().find(id);
            getFacade().remove(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("MadreRenaviEliminado"), JsfUtil.INFO_MESSAGE);
            return "List";
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "List";
        }
    }

    @FacesConverter(forClass = MadreRenavi.class)
    public static class MadreRenaviControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try {
                if (value == null || value.length() == 0) {
                    return null;
                }
                MadreRenaviCDLController controller = (MadreRenaviCDLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "madreRenaviCDLController");
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

        String getStringKey(Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof MadreRenavi) {
                MadreRenavi o = (MadreRenavi) object;
                return getStringKey(o.getIdMad());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + MadreRenavi.class.getName());
            }
        }

    }

    /**
     * Medotodo que consume WS y devuelve la información de la madre dado:
     *
     * @param cedula
     * @param usuario
     * @param contrasenia
     * @return
     */
    private Persona busquedaPorCedula(java.lang.String cedula, java.lang.String usuario, java.lang.String contrasenia) {
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
        ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta_Service service = new ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta_Service();
        ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta port = service.getWSRegistroCivilConsultaPort();
        return port.busquedaPorNombre(apellido1, apellido2, nombre1, nombre2, edadInicio, edadFinal, genero, usuario, contrasenia);
    }

    public void asignacionCurrent(String cdl) {
        Persona perso = new Persona();
        try {
            perso = this.busquedaPorCedula(cdl, JsfUtil.USER_ACCWS_MADRE, JsfUtil.PASS_ACCWS_MADRE);
            if (perso.getCodigoError().equals("000") || (Integer.parseInt(perso.getCodigoError()) >= 200)) {
                //Verifico el genero, estado de ciudadania y la edad
                StringBuilder obsMadre = new StringBuilder();
                StringBuilder msjInconsistencias = new StringBuilder();
                if (perso.getCondicionCedulado() != null
                        && perso.getGenero() != null
                        && perso.getFechaNacimiento() != null
                        && perso.getCedula() != null
                        && perso.getNombre() != null) {
                    //Condición de cedulado;
                    hijoAux.setCondicCedMad(perso.getCondicionCedulado().trim());
                    obsMadre.append("CONDICIÓN DE CIUDADANÍA: ");
                    obsMadre.append(perso.getCondicionCedulado().trim());
                    if (perso.getFechaDefuncion() != null) {
                        hijoAux.setFecFallecMad(perso.getFechaDefuncion().trim());
                        obsMadre.append("; FECHA DE DEFUNCIÓN: ");
                        obsMadre.append(perso.getFechaDefuncion());
                        obsMadre.append("; FECHA DE INSCRIPCIÓN DE FALLECIMIENTO: ");
                        obsMadre.append(perso.getFechaInscripcionFallecimiento().trim());
                    }
                    if (perso.getCodigoCondicionCedulado().contains("7")) {
                        msjInconsistencias.append("El registro encontrado aparece con estado de ciudadanía 'Fallecido'. ");
                        flagInconsistencia = true;
                    }
                    /*Cambio realizadoe para validar la condicion de la usuario... Mantis 1447    FWTM*/
                    if (perso.getCodigoCondicionCedulado().contains("8") || 
                            perso.getCodigoCondicionCedulado().contains("9")) {
                        msjInconsistencias.append("Condición de cedulado no es aceptada para realizar el registro. ");
                        flagInconsistencia = true;
                    }
                    /**/
                    //Género
                    hijoAux.setSexoMad(perso.getGenero().trim());
                    obsMadre.append("; GÉNERO: ");
                    obsMadre.append(perso.getGenero().trim());
                    if (perso.getGenero().equals("HOMBRE")) {
                        msjInconsistencias.append("Se han encontrado inconsistencias en el sexo de la madre. ");
                        flagInconsistencia = true;
                    }
                    //edad
                    try {
                        current.setFechaNacimMad(formatoFecha.parse(perso.getFechaNacimiento().trim()));
                        current.setAnioNacimMad(Short.valueOf(perso.getFechaNacimiento().trim().substring(6, 10)));
                        current.setMesNacimMad(Short.valueOf(perso.getFechaNacimiento().trim().substring(3, 5)));
                        current.setDiaNacimMad(Short.valueOf(perso.getFechaNacimiento().trim().substring(0, 2)));
                        //Edad de la madre
                        hijoAux.setEdadMad(this.calcularEdad(formatoFecha.parse(perso.getFechaNacimiento())));
                        if (!(hijoAux.getEdadMad() <= 56 && hijoAux.getEdadMad() >= 8)) {
                            msjInconsistencias.append("La edad de la madre está fuera del rango establecido. ");
                            flagInconsistencia = true;
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(MadreRenaviCDLController.class.getName()).log(Level.SEVERE, null, ex);
                        current.setFechaNacimMad(null);
                    }
                    //Datos de la madre
                    current.setNombrMad(perso.getNombre().trim());
                    current.setCedulMad(perso.getCedula().trim());
                    //Si la madre tiene registros por completar el registro de datos
                    int regNoCompletos = 0;
                    List<Object[]> itemsObjectFDatos = new ArrayList<Object[]>();
                    StringBuilder sqlFDatos = new StringBuilder("select COUNT(n.id_nac_viv), n.fk_cedul_mad ");
                    sqlFDatos.append("from nacido_vivo_renavi n, madre_renavi m ");
                    sqlFDatos.append("where n.fk_cedul_mad = m.id_mad ");
                    sqlFDatos.append("AND n.fk_id_est_reg < ");
                    sqlFDatos.append(JsfUtil.STAT_DAT_DOCTOR);
                    sqlFDatos.append(" AND m.cedul_mad = '");
                    sqlFDatos.append(current.getCedulMad());
                    sqlFDatos.append("' GROUP BY n.fk_cedul_mad");
                    try {
                        itemsObjectFDatos = ejbFacade.executeNativeQueryListResult(sqlFDatos.toString());
                        if (!itemsObjectFDatos.isEmpty()) {
                            for (Object[] objTemFDatos : itemsObjectFDatos) {
                                regNoCompletos = ((BigDecimal) objTemFDatos[0]).intValue();
                            }
                            if (regNoCompletos > 0) {
                                msjInconsistencias.append("La madre posee ");
                                msjInconsistencias.append(regNoCompletos);
                                msjInconsistencias.append(" registro(s) de nacido(s) vivo(s) sin completar la información. ");
                                flagInconsistencia = true;
                            }
                        } else {
                            //Si la madre no tubo un hijo recientemente.
                            Date fechaUltNacimiento = new Date();
                            long cedulaMadre = 0L;
                            List<Object[]> itemsObject = new ArrayList<Object[]>();
                            StringBuilder sql = new StringBuilder("select n.fecha_nacim_nac_viv, n.fk_cedul_mad, ");
                            sql.append("n.numer_parto_nac_viv,n.hijos_vivsa_mad,n.hijos_nvmrt_mad, ");
                            sql.append("n.hijos_nmrts_mad, n.num_parto_sistema ");
                            sql.append("from nacido_vivo_renavi n, madre_renavi m ");
                            sql.append("where n.fk_cedul_mad = m.id_mad AND m.cedul_mad = '");
                            sql.append(current.getCedulMad());
                            sql.append("' AND n.fecha_nacim_nac_viv = ( ");
                            sql.append("select MAX(n1.fecha_nacim_nac_viv) ");
                            sql.append("from nacido_vivo_renavi n1, madre_renavi m1 ");
                            sql.append("where n1.fk_cedul_mad = m1.id_mad AND m1.cedul_mad = '");
                            sql.append(current.getCedulMad());
                            //DFJ
                            sql.append("' ) ");
                            //Solo registros firmados
                            /*Se aumenta 3 estados para la validacion de que la madre no puede tener partos seguido
                              los estados son estado_firma = 1, estado_registro = 6, 5 y 7
                              FWTM ---  16-06-2016
                            */
                            sql.append("and n.fk_id_est_fir in (1, 2) and n.fk_id_est_reg in (4, 5, 6, 7, 8)");
                            //FIN DFJ
                            try {
                                itemsObject = ejbFacade.executeNativeQueryListResult(sql.toString());
                                if (!itemsObject.isEmpty()) {
                                    for (Object[] objTem : itemsObject) {
                                        fechaUltNacimiento = (Date) objTem[0];
                                        cedulaMadre = ((BigDecimal) objTem[1]).longValue();
                                        if (objTem[2] != null) {
                                            numParto = ((BigDecimal) objTem[2]).shortValue();
                                            hijoAux.setNumerPartoNacViv((short) (numParto + 1));
                                        } else {
                                            numParto = 0;
                                        }
                                        if (objTem[3] != null) {
                                            numHijosvivos = ((BigDecimal) objTem[3]).shortValue();
                                        } else {
                                            numHijosvivos = 0;
                                        }
                                        if (objTem[4] != null) {
                                            numHijosVivMuertos = ((BigDecimal) objTem[4]).shortValue();
                                            hijoAux.setHijosNvmrtMad(numHijosVivMuertos);
                                        } else {
                                            numHijosVivMuertos = 0;
                                        }
                                        if (objTem[5] != null) {
                                            numHijosNacierMuertos = ((BigDecimal) objTem[5]).shortValue();
                                            hijoAux.setHijosNmrtsMad(numHijosNacierMuertos);
                                        } else {
                                            numHijosNacierMuertos = 0;
                                        }
                                        if (objTem[6] != null) {
                                            hijoAux.setNumPartoSistema((short) (((BigDecimal) objTem[6]).shortValue() + 1));
                                        } else {
                                            hijoAux.setNumPartoSistema((short) 1);
                                        }
                                    }
                                    current.setIdMad(cedulaMadre);
                                    Long fchUltNcmnt = fechaUltNacimiento.getTime();
                                    Long fchActl = fechaActual.getTime();
                                    try {
                                        VariableRenavi tiempoParto = ejbFacadeVariable.find(1);
                                        Long tempTiempoParto = new Long(tiempoParto.getVarValor());
                                        Long resta = fchActl - fchUltNcmnt;
                                        if (resta < tempTiempoParto) {
                                            msjInconsistencias.append("El periodo de tiempo entre el parto anterior y el actual no está dentro del rango permitido. ");
                                            flagInconsistencia = true;
                                        }
                                    } catch (Exception eev) {
                                        eev.printStackTrace();
                                        msjInconsistencias.append("Existió un error al consultar el tiempo mínimo entre partos. ");
                                        flagInconsistencia = true;
                                    }
                                } else {
                                    try {
                                        MadreRenavi mad = (MadreRenavi) ejbFacadeMadre.consultarTablaSingleResult("MadreRenavi.findByCedulMad", "cedulMad", current.getCedulMad());
                                        current.setIdMad(mad.getIdMad());
                                    } catch (EntidadException eem) {
                                        current.setIdMad(null);
                                    }
                                    numParto = 0;
                                    hijoAux.setNumerPartoNacViv((short) (numParto + 1));
                                    numHijosvivos = 0;
                                    numHijosVivMuertos = 0;
                                    hijoAux.setHijosNvmrtMad(numHijosVivMuertos);
                                    numHijosNacierMuertos = 0;
                                    hijoAux.setHijosNmrtsMad(numHijosNacierMuertos);
                                    hijoAux.setNumPartoSistema((short) 1);
                                }
                            } catch (Exception eenq) {
                                eenq.printStackTrace();
                                msjInconsistencias.append("Existió un error al consultar información de la madre. ");
                                flagInconsistencia = true;
                            }
                        }
                    } catch (Exception efd) {
                        efd.printStackTrace();
                        msjInconsistencias.append("Existió un error al consultar información de la madre. ");
                        flagInconsistencia = true;
                    }
                    //Si no hay inconsistencias
                    if (!flagInconsistencia) {
                        //Aisgnación del pais de residencia de la madre
                        hijoAux.setFkIdPaisResidnMad(ejbFacadePais.find(JsfUtil.COD_PAIS_EC));
                        //Asigno los apellidos de la madre al recien nacido
                        List<String> nmbrsMdr = UtilitarioNombre.separacion(current.getNombrMad());
                        if (!nmbrsMdr.isEmpty()) {
                            hijoAux.setApellNacViv(nmbrsMdr.get(0));
                        }
                        //Seteo la fecha de creación y de actualización del nacido vivo
                        hijoAux.setFechaCreacionNacViv(fechaActual);
                        hijoAux.setFechaActualizacionNacViv(fechaActual);
                        if (perso.getNombrePadre() != null || !perso.getNombrePadre().equals("")) {
                            current.setNomPadreMad(perso.getNombrePadre().trim());
                        }
                        if (perso.getNombreMadre() != null || !perso.getNombreMadre().equals("")) {
                            current.setNomMadreMad(perso.getNombreMadre().trim());
                        }
                        //Para la fotografía
                        if (perso.getCodigoError().equals("000")) {
                            try {
                                InputStream input = new ByteArrayInputStream(perso.getFotografia());
                                this.setImagem(new DefaultStreamedContent(input, "image/jpeg"));
                                hijoAux.setFotoMad(perso.getFotografia());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //Obtengo la nacionalidad de la madre
                        try {
                            if (!perso.getNacionalidad().equals("")) {
                                List<ParametroConsulta> paramsPais = new ArrayList<ParametroConsulta>();
                                if (perso.getNacionalidad().toUpperCase().trim().contains("ECUATORIANA")) {
                                    hijoAux.setFkIdNacMad(ejbFacadeNacionalidad.find(1));
                                    paramsPais.add(new ParametroConsulta("idPais", 81));
                                    listPaisDatMadre = ejbFacadePais.consultarTablaResultList("PaisRenavi.findByIdPais", paramsPais);
                                    hijoAux.setFkIdPaisMad(listPaisDatMadre.get(0));
                                    hijoAux.setCampoInecCodPaisMad(listPaisDatMadre.get(0).getCodigPais());
                                } else {
                                    NacionalidadRenavi nac = ejbFacadeNacionalidad.find(2);
                                    nac.setDescNac(perso.getNacionalidad());
                                    hijoAux.setFkIdNacMad(nac);
                                    paramsPais.add(new ParametroConsulta("idPais", 81));
                                    listPaisDatMadre = ejbFacadePais.consultarTablaResultList("PaisRenavi.findByCodigPaisMe", paramsPais);
                                    for (PaisRenavi p : listPaisDatMadre) {
                                        //System.out.println("--> " + );
                                        if (UtilitarioCaracteres.stripAccents(p.getDescPais())
                                                .contains(UtilitarioCaracteres.stripAccents(perso.getLugarNacimiento()))) {
                                            hijoAux.setFkIdPaisMad(p);
                                            hijoAux.setCampoInecCodPaisMad(p.getCodigPais());
                                            break;
                                        } else if (UtilitarioCaracteres.stripAccents(perso.getLugarNacimiento())
                                                .contains(UtilitarioCaracteres.stripAccents(p.getDescPais()))) {
                                            hijoAux.setFkIdPaisMad(p);
                                            hijoAux.setCampoInecCodPaisMad(p.getCodigPais());
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (EntidadException ee) {
                            ee.printStackTrace();
                            hijoAux.setFkIdNacMad(null);
                            hijoAux.setFkIdPaisMad(null);
                            hijoAux.setCampoInecCodPaisMad(null);
                        }
                        //Obtengo el estado civil de la madre
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
                                } else if (perso.getEstadoCivil().toUpperCase().contains("EN UNION DE HECHO")) {
                                    estadoCivil = ejbFacadeEstadoCivil.find(7);
                                }
                                hijoAux.setFkIdEstCivMad(estadoCivil);
                                obsMadre.append("ESTADO CIVIL: ");
                                obsMadre.append(perso.getEstadoCivil());
                            }
                        } catch (EntidadException ee) {
                            hijoAux.setFkIdEstCivMad(null);
                        }
                        if (perso.getConyuge() != null) {
                            hijoAux.setConyugeMad(perso.getConyuge().trim());
                            obsMadre.append(" ;CÓNYUGE: ");
                            obsMadre.append(perso.getConyuge().trim());
                        }
                        //Seteo la observación de la madre
                        hijoAux.setObsrvEstadoMad(obsMadre.toString());
                    } else {
                        msjInconsistencias.append("No se puede continuar con el proceso de registro de datos del nacido vivo.");
                        JsfUtil.displayMessage(msjInconsistencias.toString(), JsfUtil.ERROR_MESSAGE);
                        RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaErrorM");
                    }
                } else {
                    JsfUtil.displayMessage("Los datos de la madre buscada no están completos.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                }
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistroPorCedulaNoEncontrado"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
            }
        } catch (Exception e) {
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

    public void BuscarMadre() {
        this.BusquedaWS(cedulaBusq);
    }

    /**
     * Método que busca las madres traidas del WS cuando la consulta se la
     * realiza por appellidos y nombres
     *
     */
    public void buscarMadreAppellidosNombres() {
        Ciudadanos ciudadanos = new Ciudadanos();
        try {
            ciudadanos = this.busquedaPorNombre(apellidoUnoBusq, apellidoDosBusq, nombreUnoBusq, nombreDosBusq, null, null, "2", JsfUtil.USER_ACCWSNOMBRES_MADRE,
                    JsfUtil.PASS_ACCWSNOMBRES_MADRE);
            personas = ciudadanos.getPersona();
            if (personas.isEmpty()) {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistrosPorNombresNoEncontrados"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().execute("PF('indocumenDialog').show();");
            } else {
                RequestContext.getCurrentInstance().execute("PF('statusDialog').hide();");
                RequestContext.getCurrentInstance().execute("PF('personasDialog').show();");
                RequestContext.getCurrentInstance().update("form_cont:contTab:pdtTblItemsPersonas");
            }
        } catch (Exception e) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("WebServiceErrorConsulta"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
        }
    }

    public StreamedContent getImagem() {
        return imagem;
    }

    public void setImagem(StreamedContent imagem) {
        this.imagem = imagem;
    }

    public void limpiar() {
        this.current = null;
        this.setImagem(null);
        this.hijoAux = null;
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
        hijosAux = null;
        hijosAuxDelete = null;
        flagInconsistencia = false;
        numParto = null;
        numHijosvivos = null;
        numHijosVivMuertos = null;
        numHijosNacierMuertos = null;
        hijosVivosSistema = null;
        porEliminarNV = 0;
        cedulaBusq = null;
        chkInscrito = null;
        System.gc();
    }

    /**
     * Método para limpiar la consulta cuando el usuario ha seleccionado buscar
     * por cédula.
     */
    public void limpiarBuscarPorCedula() {
        this.hijoAux = new NacidoVivoRenavi();
        this.current = new MadreRenavi();
        personas = new ArrayList<Persona>();
        this.setImagem(null);
        cedulaBusq = null;
        nivelesInstruccion = null;
        flagInconsistencia = false;
        provincia = null;
        cantones = null;
        canton = null;
        parroquias = null;
        parroquia = null;
        numParto = null;
        numHijosvivos = null;
        numHijosVivMuertos = null;
        numHijosNacierMuertos = null;
        hijosVivosSistema = null;
        porEliminarNV = 0;
        if (listObj != null) {    
           listObj.clear();
        }
        edadMadre = null;
        setCriterioBusqueda("idMadre");
        observacionA = null;
        chkInscrito = null;
        System.gc();
    }

    /**
     * Método para limpiar la consulta cuando el usuario ha seleccionado buscar
     * por appellidos y nombres.
     */
    public void limpiarBuscarPorApellNom() {
        this.hijoAux = new NacidoVivoRenavi();
        this.current = new MadreRenavi();
        this.setImagem(null);
        personas = new ArrayList<Persona>();
        cedulaBusq = null;
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
        numParto = null;
        numHijosvivos = null;
        numHijosVivMuertos = null;
        numHijosNacierMuertos = null;
        hijosVivosSistema = null;
        porEliminarNV = 0;
        System.gc();
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public NacidoVivoRenavi getHijoAux() {
        if (hijoAux == null) {
            hijoAux = new NacidoVivoRenavi();
        }
        return hijoAux;
    }

    public void setHijoAux(NacidoVivoRenavi hijoAux) {
        this.hijoAux = hijoAux;
    }

    public List<NacidoVivoRenavi> getHijosAux() {
        return hijosAux;
    }

    public List<NacidoVivoRenavi> getHijosAuxDelete() {
        return hijosAuxDelete;
    }

    public void setHijosAuxDelete(List<NacidoVivoRenavi> hijosAuxDelete) {
        this.hijosAuxDelete = hijosAuxDelete;
    }

    private Cedula numeroCedula(java.lang.String provincia) {
        ec.gob.digercic.renavi.ws.cedula.GeneraCedula_Service service_cedula = new ec.gob.digercic.renavi.ws.cedula.GeneraCedula_Service();
        ec.gob.digercic.renavi.ws.cedula.GeneraCedula port = service_cedula.getGeneraCedulaPort();
        return port.numeroCedula(provincia);
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
            hijoAux.setResidnProvidMad(value.substring(0, value.indexOf(".- ")));
            hijoAux.setResidnProvdsMad(value.substring(value.indexOf(".-") + 3, value.length()));
            //Asigno los cantones correspondientes a la provincia seleccionada
            cantones = new ArrayList<TblSauregUbicacion>();
            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            params.add(new ParametroConsulta("codDpa", value.substring(0, value.indexOf(".- "))));
            params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
            try {
                cantones = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodDpa", params);
            } catch (EntidadException ee) {
            }
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
            hijoAux.setResidnCantidMad(value.substring(0, value.indexOf(".- ")));
            hijoAux.setResidnCantdsMad(value.substring(value.indexOf(".-") + 3, value.length()));
            //Asigno las parroquias correspondientes a la provincia seleccionada
            parroquias = new ArrayList<TblSauregUbicacion>();
            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            params.add(new ParametroConsulta("codDpa", value.substring(0, value.indexOf(".- "))));
            params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
            try {
                parroquias = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodDpa", params);
            } catch (EntidadException ee) {
            }
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
            hijoAux.setResidnParridMad(value.substring(0, value.indexOf(".- ")));
            hijoAux.setResidnParrdsMad(value.substring(value.indexOf(".-") + 3, value.length()));
            //Setear campoInecAreaMad
            hijoAux.setCampoInecAreaMad(value.substring(0, value.indexOf(".- ")));
            //Seteo urbana o rural
            try {
                if (Integer.valueOf(value.substring(4, value.indexOf(".- "))) < 51) {
                    //Urbana
                    hijoAux.setFkIdTipoAreaMad(ejbFacadetipoArea.find(Integer.valueOf("1")));
                } else {
                    //Rural
                    hijoAux.setFkIdTipoAreaMad(ejbFacadetipoArea.find(Integer.valueOf("2")));
                }
            } catch (EntidadException ee) {
            }
        }
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

    public String getObservacionA() {
        return observacionA;
    }

    public void setObservacionA(String observacionA) {
        this.observacionA = observacionA;
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

    public Date getFechaActual() {
        this.validarNac();
        return fechaActual;
    }

    public Date getFechaPrenacimiento() {
        return fechaPrenacimiento;
    }

    public Date getFechaP() {
        this.validarNac();
        return fechaP;
    }

    public void setFechaP(Date fechaP) {
        this.fechaP = fechaP;
    }

    public Date getFechaF() {
        return fechaF;
    }

    public void setFechaF(Date fechaF) {
        this.fechaF = fechaF;
    }

    public List<Object[]> getListObj() {
        return listObj;
    }

    public void setListObj(List<Object[]> listObj) {
        this.listObj = listObj;
    }

    public String getChkInscrito() {
        return chkInscrito;
    }

    public void setChkInscrito(String chkInscrito) {
        this.chkInscrito = chkInscrito;
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
        Calendar calFechaActual = Calendar.getInstance();
        int año = calFechaActual.get(Calendar.YEAR) - fechaNacim.get(Calendar.YEAR);
        int mes = calFechaActual.get(Calendar.MONTH) - fechaNacim.get(Calendar.MONTH);
        int dia = calFechaActual.get(Calendar.DATE) - fechaNacim.get(Calendar.DATE);
        if (mes < 0 || (mes == 0 && dia < 0)) {
            año--;
        }
        short edad = (short) año;
        return edad;
    }

    public List<NivelInstruccionRenavi> getNivelesInstruccion() {
        if (nivelesInstruccion == null) {
            nivelesInstruccion = new ArrayList<NivelInstruccionRenavi>();
        }
        return nivelesInstruccion;
    }

    public void setNivelesInstruccion(List<NivelInstruccionRenavi> nivelesInstruccion) {
        this.nivelesInstruccion = nivelesInstruccion;
    }

    public List<Persona> getPersonas() {
        if (personas == null) {
            personas = new ArrayList<Persona>();
        }
        return personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
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
            current = new MadreRenavi();
            hijoAux = new NacidoVivoRenavi();
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
        hijoAux.setFkIdNivelInstr(null);
        //Obtengo el valor seleccionado
        AlfabetismoRenavi item = (AlfabetismoRenavi) event.getNewValue();
        if (item != null) {
            if (item.getIdAlfb() == 2) {
                try {
                    NivelInstruccionRenavi instruccion = ejbFacadeNivelInstruccion.find(0);
                    nivelesInstruccion.add(instruccion);
                    hijoAux.setFkIdNivelInstr(instruccion);
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            } else {
                try {
                    //if (hijoAux.getEdadMad() >= 10 && hijoAux.getEdadMad() <= 11) {
                    if (hijoAux.getEdadMad() >= 8 && hijoAux.getEdadMad() <= 11) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad10And11");
                    } else if (hijoAux.getEdadMad() >= 12 && hijoAux.getEdadMad() <= 14) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad12And14");
                    } else if (hijoAux.getEdadMad() >= 15 && hijoAux.getEdadMad() <= 16) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad15And16");
                    } else if (hijoAux.getEdadMad() >= 17 && hijoAux.getEdadMad() <= 22) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad17And22");
                    } else if (hijoAux.getEdadMad() >= 23 && hijoAux.getEdadMad() <= 49) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    } else if (hijoAux.getEdadMad() >= 50) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    }
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            }
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
            limpiar();
        } else {
            limpiarBuscarPorApellNom();
        }
        RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
    }

    /* Provincia canton y parroquia para ma madre---------------------------------------------------------------------------------*/
    public byte[] pdfSinFirma(NacidoVivoRenavi nv, ec.gob.digercic.renavi.ws.TblSauregUsuario usuarioFirma) throws JRException, IOException, SQLException {//JJHF CAMBIO LOG IN
        try {
            List<NacidoVivoRenavi> items = new ArrayList<NacidoVivoRenavi>();
            if (nv.getFkIdNacMad().getIdNac() == 2) {
                nv.setFkIdNacMad(ejbFacadeNacionalidad.find(2));
            }
            items.add(nv);
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("institucion", userLgn.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("usuarioLogeado", userLgn.getNomUsuario().toString());
            parametros.put("lugar", userLgn.getAgenciaInUser().getIdInstituc().getNomInstituc().toString());
            parametros.put("nombre", userLgn.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("provincia", userLgn.getAgenciaInUser().getIdProvincia().getDerscripcion().toString());
            parametros.put("canton", userLgn.getAgenciaInUser().getIdCanton().getDerscripcion().toString());
            parametros.put("parroquia", userLgn.getAgenciaInUser().getIdParroquia().getDerscripcion().toString());
            parametros.put("direccion", userLgn.getAgenciaInUser().getDireccion().toString());
            parametros.put("asistidoPor", getAsistencia());
            parametros.put("telefono", userLgn.getAgenciaInUser().getTelefono().toString());
            parametros.put("nombreFirma", usuarioFirma.getNombre() + " " + usuarioFirma.getApellido());
            parametros.put("telefonoFirma", usuarioFirma.getAgenciaInUser().getTelefonoUsuario() == null ? "" : usuarioFirma.getAgenciaInUser().getTelefonoUsuario().toString());
            parametros.put("cedula", usuarioFirma.getNomUsuario().toString());
            parametros.put("ciud_loc", usuarioFirma.getAgenciaInUser().getLocalidad() == null ? "" : usuarioFirma.getAgenciaInUser().getLocalidad().toString());
            parametros.put("codMSP", userLgn.getAgenciaInUser().getCodMsp());
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users.jasper");
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(items);
            jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
            byte[] pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdf1;
        } catch (Exception e) {
            return null;
        }
    }

    @PostConstruct
    public void iniciar() throws ParseException {
        try {
            setCriterioBusqueda("idMadre");
            ruta = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            fechaActual = new Date();
            //Obtengo el tiempo prenacimiento
            Long tp = new Long(ejbFacadeVariable.find(2).getVarValor());
            fechaActual = new Date();
            fechaPrenacimiento = new Date(fechaActual.getTime() - tp);
            /*CAMBIOS PARA EL CALENDARIO DE RECIEN NACIDO EDICION   FWTM  26-01-2016*/
            Calendar c1 = GregorianCalendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMMM/yyyy hh:mm:ss");
            c1.add(Calendar.DATE, -4);
            fechaInicio = sdf.parse(sdf.format(c1.getTime()));
            fechaHoy = new Date();
            try {
                String query;
                query = "SELECT * FROM CATALOGO_RENAVI WHERE DESCRIPCION_CAT_RENAVI='MALFORMACIONES' ORDER BY ID_CAT_RENAVI DESC";
                listcat = ejbCatRenavi.executeNativeQueryListResultGenerico(query, CatalogoRenavi.class);

            } catch (Exception e) {
                System.out.println("ERROR CONSULTANDO MALFORMACIONES" + e);
            }

            /**/
            //Obtengo los datos del usuario Logeado para el tab de hijo
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");//JJHF CAMBIO LOGIN
            tblSauregUsuario = userLgn;//JJHF CAMBIO SAUREG
            //Obtengo la lista de paises
            listPais = ejbFacadePais.findAll();
            /**/
            lstProfesion = ejbFacadeCatalogoProfe.findAll();
            listaRol = this.rolesUsuarios(userLgn.getNomUsuario(), String.valueOf(userLgn.getAgenciaInUser().getIdAgencia()));
            /**/
            listTipoSangre = ejbCatRenavi.getCatalogo("TIPO DE SANGRE"); //DFJ -- Recupera el catálogo de tipo de Sangre

            for (int i = 0; i < lstProfesion.size(); i++) {
                for (int j = 0; j < listaRol.size(); j++) {
                    if (listaRol.get(j).getDescripcion().toString().equals(lstProfesion.get(i).getNombreCatProf().toString())) {
                        if (lstProfesion.get(i).getNombreCatProf().equalsIgnoreCase("DIGITADOR")) {
                            setAsistencia("OTRA");
                        } else {
                            setAsistencia(lstProfesion.get(i).getNombreCatProf().toString());
                            flagRol = true;
                            break;
                        }
                    }
                }
                if (flagRol) {
                    break;
                }
            }
            /**/

            //DFJ
            //Homologar nombre de la institucion donde se registra el nacimiento            
            List<CatalogoRenavi> listInstitucionREVIT = ejbCatRenavi.getCatalogo("LUGAR DE NACIMIENTO");
            lugarNacimiento = "OTRO";
            if (listInstitucionREVIT != null) {
                for (CatalogoRenavi catalogoRenavi : listInstitucionREVIT) {
                        if (catalogoRenavi.getNombreCatRenavi().contains(userLgn.getAgenciaInUser().getIdInstituc().getNomInstituc())) {
                            lugarNacimiento = catalogoRenavi.getNombreCatRenavi();
                            break;
                        }
                    }
                }
            //FIN DFJ
        } catch (EntidadException ee) {
            JsfUtil.displayMessage("Ha ocurrido un problema al iniciar el proceso de registro de nacido vivo.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
        }
    }

    public String eliminar() {
        cedula = cedulaBusq;
        if (criterioBusqueda.equals("idMadre")) {
            try {
                List<MadreRenavi> lstMadre = new ArrayList<MadreRenavi>();
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("cedulMad", cedula));
                parametros.add(new ParametroConsulta("status", "A"));
                lstMadre = ejbFacadeMadre.consultarTablaResultList("MadreRenavi.findByCedulMadAndStstus", parametros);
                StringBuilder updateCedula = new StringBuilder("update madre_renavi m set m.cedul_mad = '");
                updateCedula.append(cedula.substring(0, 8) + "AB'");
                updateCedula.append(", status = 'I'");
                updateCedula.append(" where m.cedul_mad = '");
                updateCedula.append(cedula);
                updateCedula.append("' and m.id_mad = ");
                updateCedula.append(lstMadre.get(0).getIdMad());
                int e = ejbFacade.executeNativeQuery(updateCedula.toString());
                if (e > 0) {
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
                        log.setAccion("REVIT. ADMINISTRACION. ELIMINACION MADRE RENAVI");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
                        CapturaIP ip = new CapturaIP();
                        log.setIp(ip.obieneDireccionIP());
                        ejbLogs.create(log);
                        //logs en la tabla logs_eliminacion
                        LogsEliminacion logEli = new LogsEliminacion();
                        logEli.setFechaEli(new Date());
                        logEli.setUsuarioEli(userLgn.getNomUsuario());
                        logEli.setAgenIdEli(userLgn.getAgenciaInUser().getCodMsp());
                        logEli.setNomUsuEli(userLgn.getNombre());
                        logEli.setApeUsuEli(userLgn.getApellido());
                        logEli.setInstIdEli(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());
                        CapturaIP ip2 = new CapturaIP();
                        logEli.setIpEli(ip2.obieneDireccionIP());
                        ejbLogsEli.create(logEli);
                        //cambiar este query multiparto sin considerar                  
                        StringBuilder valor = new StringBuilder();
                        valor.append("update logs_eliminacion set id_mad_eli = (select id_mad from madre_renavi where id_mad = ");
                        valor.append(lstMadre.get(0).getIdMad());
                        valor.append("), cedul_mad_eli = '");
                        valor.append(cedula);
                        valor.append("', nombr_mad_eli = (select nombr_mad from madre_renavi where id_mad = ");
                        valor.append(lstMadre.get(0).getIdMad());
                        valor.append("), pasap_mad_eli = (select pasap_mad from madre_renavi where id_mad = ");
                        valor.append(lstMadre.get(0).getIdMad());
                        valor.append("), estado_registro_eli = (select status from madre_renavi where id_mad = ");
                        valor.append(lstMadre.get(0).getIdMad());
                        valor.append(") where id_log_eli = ");
                        valor.append(logEli.getIdEli());

                        System.out.println(" QUERY UPDATE:   " + valor.toString());
                        int i = ejbFacade.executeNativeQuery(valor.toString());
                        List<NacidoVivoRenavi> nacidoEli = new ArrayList<NacidoVivoRenavi>();
//                        List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                        parametros.clear();
                        parametros.add(new ParametroConsulta("cedulaMad", (cedula.substring(0, 8) + "AB")));
                        nacidoEli = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByCedulMadElim", parametros);
                        //update para el estado del recien nacido vivo de la madre eliminada a estado 9
                        for (NacidoVivoRenavi item : nacidoEli) {
                            StringBuilder valup = new StringBuilder("update nacido_vivo_renavi set fk_id_est_reg = 9 where fk_cedul_mad = (select id_mad from madre_renavi where cedul_mad = '");
                            valup.append(item.getFkCedulMad().getCedulMad().substring(0, 8));
                            valup.append("AB'");
                            valup.append(" and id_mad = ");
                            valup.append(lstMadre.get(0).getIdMad());
                            valup.append(")");
                            int u = ejbFacade.executeNativeQuery(valup.toString());
                        }
                            JsfUtil.displayMessage("Registro del Nacido Vivo Eliminado.", JsfUtil.INFO_MESSAGE);
                            RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    } catch (EntidadException o) {
                        o.printStackTrace();
                    }

                } else {
                    JsfUtil.displayMessage("Error, no se eliminó el registro, revise que el número de la cédula de la madre, sea correcto.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                }
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
            }
        } else {
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
                log.setAccion("REVIT. ADMINISTRACION. ELIMINACION MADRE RENAVI");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
                CapturaIP ip = new CapturaIP();
                log.setIp(ip.obieneDireccionIP());
                ejbLogs.create(log);
                //logs en la tabla logs_eliminacion
                LogsEliminacion logEli = new LogsEliminacion();
                logEli.setFechaEli(new Date());
                logEli.setUsuarioEli(userLgn.getNomUsuario());
                logEli.setAgenIdEli(userLgn.getAgenciaInUser().getCodMsp());
                logEli.setNomUsuEli(userLgn.getNombre());
                logEli.setApeUsuEli(userLgn.getApellido());
                logEli.setInstIdEli(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());
                CapturaIP ip2 = new CapturaIP();
                logEli.setIpEli(ip2.obieneDireccionIP());
                ejbLogsEli.create(logEli);
                //cambiar este query multiparto sin considerar                  
                StringBuilder valor = new StringBuilder();
                valor.append("update logs_eliminacion set id_mad_eli = (select id_mad from madre_renavi where id_mad = ");
                valor.append(cedula);
                valor.append("), cedul_mad_eli = ");
                valor.append(cedula);
                valor.append(", nombr_mad_eli = (select nombr_mad from madre_renavi where id_mad = ");
                valor.append(cedula);
                valor.append("), pasap_mad_eli = (select pasap_mad from madre_renavi where id_mad = ");
                valor.append(cedula);
                valor.append("), estado_registro_eli = (select status from madre_renavi where id_mad = ");
                valor.append(cedula);
                valor.append(") where id_log_eli = ");
                valor.append(logEli.getIdEli());

                System.out.println(" QUERY UPDATE:   " + valor.toString());
                int i = ejbFacade.executeNativeQuery(valor.toString());
                List<NacidoVivoRenavi> nacidoEli = new ArrayList<NacidoVivoRenavi>();
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("cedulaMad", Long.parseLong(cedula)));
                nacidoEli = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByIdMadElim", parametros);
                //update para el estado del recien nacido vivo de la madre eliminada a estado 9
                for (NacidoVivoRenavi item : nacidoEli) {
                    StringBuilder valup = new StringBuilder("update nacido_vivo_renavi set fk_id_est_reg = 9 where fk_cedul_mad = (select id_mad from madre_renavi where id_mad = ");
                    valup.append(item.getFkCedulMad().getIdMad());
                    valup.append(")");
                    int u = ejbFacade.executeNativeQuery(valup.toString());
                }
                    JsfUtil.displayMessage("Registro del Nacido Vivo Eliminado.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
            }
        }
        this.limpiarBuscarPorCedula();
        return "/pages/madreRenavi/Delete";
    }

    public String anular() {
        cedula = cedulaBusq;
        if (criterioBusqueda.equals("idMadre")) {
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
                log.setAccion(observacionA);//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
                CapturaIP ip = new CapturaIP();
                log.setIp(ip.obieneDireccionIP());
                ejbLogs.create(log);
                //logs en la tabla logs_eliminacion
                LogsEliminacion logEli = new LogsEliminacion();
                logEli.setFechaEli(new Date());
                logEli.setUsuarioEli(userLgn.getNomUsuario());
                logEli.setAgenIdEli(userLgn.getAgenciaInUser().getCodMsp());
                logEli.setNomUsuEli(userLgn.getNombre());
                logEli.setApeUsuEli(userLgn.getApellido());
                logEli.setInstIdEli(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());
                CapturaIP ip2 = new CapturaIP();
                logEli.setIpEli(ip2.obieneDireccionIP());
                ejbLogsEli.create(logEli);
                //cambiar este query multiparto sin considerar                  
                StringBuilder valor = new StringBuilder();
                valor.append("update logs_eliminacion set id_mad_eli=(select id_mad from madre_renavi where cedul_mad = '");
                valor.append(cedula);
                valor.append("'), cedul_mad_eli = '");
                valor.append(cedula);
                valor.append("', nombr_mad_eli = (select nombr_mad from madre_renavi where cedul_mad = '");
                valor.append(cedula);
                valor.append("'");
                valor.append(" ), pasap_mad_eli = (select pasap_mad from madre_renavi where cedul_mad = '");
                valor.append(cedula);
                valor.append("'");
                valor.append("), estado_registro_eli = (select status from madre_renavi where cedul_mad = '");
                valor.append(cedula);
                valor.append("'");
                valor.append(") where id_log_eli =");
                valor.append(logEli.getIdEli());
                int i = ejbFacade.executeNativeQuery(valor.toString());
                List<NacidoVivoRenavi> nacido = new ArrayList<NacidoVivoRenavi>();
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("cedulaMad", cedula));
                nacido = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByCedulMadAnul", parametros);
                for (NacidoVivoRenavi item : nacido) {
                    //update para el estado del recien nacido vivo de la madre eliminada a estado 9
                    StringBuilder valup = new StringBuilder();
                    valup.append("update nacido_vivo_renavi set fk_id_est_reg = 11 where fk_cedul_mad = (select id_mad from madre_renavi where cedul_mad = '");
                    valup.append(item.getFkCedulMad().getCedulMad());
                    valup.append("')");
                    int u = ejbFacade.executeNativeQuery(valup.toString());
                }
                JsfUtil.displayMessage("Registro del Nacido Vivo Anulado.", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("mssgsBusqueda");
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
            }
        } else {
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
                log.setAccion(observacionA);//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
                CapturaIP ip = new CapturaIP();
                log.setIp(ip.obieneDireccionIP());
                ejbLogs.create(log);
                //logs en la tabla logs_eliminacion
                LogsEliminacion logEli = new LogsEliminacion();
                logEli.setFechaEli(new Date());
                logEli.setUsuarioEli(userLgn.getNomUsuario());
                logEli.setAgenIdEli(userLgn.getAgenciaInUser().getCodMsp());
                logEli.setNomUsuEli(userLgn.getNombre());
                logEli.setApeUsuEli(userLgn.getApellido());
                logEli.setInstIdEli(userLgn.getAgenciaInUser().getIdInstituc().getIdInstituc().toString());
                CapturaIP ip2 = new CapturaIP();
                logEli.setIpEli(ip2.obieneDireccionIP());
                ejbLogsEli.create(logEli);
                //cambiar este query multiparto sin considerar                  
                StringBuilder valor = new StringBuilder();
                valor.append("update logs_eliminacion set id_mad_eli = ");
                valor.append(cedula);
                valor.append(", nombr_mad_eli = (select nombr_mad from madre_renavi where id_mad = ");
                valor.append(cedula);
                valor.append("), pasap_mad_eli = (select pasap_mad from madre_renavi where id_mad = ");
                valor.append(cedula);
                valor.append("), estado_registro_eli = (select status from madre_renavi where id_mad = ");
                valor.append(cedula);
                valor.append(") where id_log_eli = ");
                valor.append(logEli.getIdEli());
                int i = ejbFacade.executeNativeQuery(valor.toString());
                List<NacidoVivoRenavi> nacido = new ArrayList<NacidoVivoRenavi>();
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("cedulaMad", Long.parseLong(cedula)));
                nacido = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByIdMadAnul", parametros);
                for (NacidoVivoRenavi item : nacido) {
                    //update para el estado del recien nacido vivo de la madre eliminada a estado 9
                    StringBuilder valup = new StringBuilder();
                    valup.append("update nacido_vivo_renavi set fk_id_est_reg = 11 where fk_cedul_mad = ");
                    valup.append(item.getFkCedulMad().getIdMad());
                    int u = ejbFacade.executeNativeQuery(valup.toString());
                }
                JsfUtil.displayMessage("Registro del Nacido Vivo Anulado.", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("mssgsBusqueda");
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
            }
        }
        this.limpiarBuscarPorCedula();
        return "/pages/madreRenavi/Anular";
    }

    public void buscarME() {
        List<MadreRenavi> listMad = new ArrayList<MadreRenavi>();
        ced = cedulaBusq;
        try {
            StringBuilder valor = new StringBuilder("select m.id_mad from madre_renavi m, nacido_vivo_renavi n where m.cedul_mad = '");
            valor.append(cedulaBusq);
            valor.append("'");
            valor.append(" and n.fk_id_est_fir = 1 and m.id_mad = n.fk_cedul_mad and");
            valor.append(" ( n.fk_id_est_reg = 1 or n.fk_id_est_reg = 2 or n.fk_id_est_reg = 3 or n.fk_id_est_reg = 4)");
            listMad = ejbFacade.executeNativeQueryListResultGenerico(valor.toString(), MadreRenavi.class);

            if (listMad.size() > 0) {
                JsfUtil.displayMessage("Registro de Nacido Vivo encontrado en el sistema.", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                this.BuscarMadre();
            } else {
                JsfUtil.displayMessage("No se encuentra un Registro de Nacido Vivo en el sistema con la información solicitada, o el registro de Nacido Vivo tiene el estado incorrecto para ser eliminado.", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                cedulaBusq = null;
            }
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
        }

    }

    public void buscarMA() {
        List<MadreRenavi> listMad = new ArrayList<MadreRenavi>();
        
        if (ruta.equals("/pages/madreRenavi/Delete.xhtml")) {
            try {
                StringBuilder valor = new StringBuilder("select m.id_mad from madre_renavi m, nacido_vivo_renavi n where m.cedul_mad = '");
                valor.append(cedulaBusq);
                valor.append("'");
                valor.append(" and m.id_mad = n.fk_cedul_mad");
                valor.append(" and n.fk_id_est_fir = 1");
                valor.append(" and n.fk_id_est_reg not in (7, 8, 9)");
                System.out.println(" ---> " + valor.toString());
                listMad = ejbFacade.executeNativeQueryListResultGenerico(valor.toString(), MadreRenavi.class);

                if (listMad.size() > 0) {
                    JsfUtil.displayMessage("Registro de Nacido Vivo encontrado en el sistema.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    flagAnulacion = true;
                    this.BuscarMadre();
                } else {
                    JsfUtil.displayMessage("No se encuentra un Registro de Nacido Vivo en el sistema con la información solicitada, o el registro de Nacido Vivo tiene el estado incorrecto para ser anulado.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    cedulaBusq = null;
                }
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
            }
        } else {
            try {
                StringBuilder valor = new StringBuilder("select m.id_mad from madre_renavi m, nacido_vivo_renavi n where m.cedul_mad = '");
                valor.append(cedulaBusq);
                valor.append("'");
                valor.append(" and m.id_mad = n.fk_cedul_mad and n.fk_id_est_reg in (4, 8)");
                valor.append(" and n.fk_id_est_fir = 2");
                System.out.println(" ---> " + valor.toString());
                listMad = ejbFacade.executeNativeQueryListResultGenerico(valor.toString(), MadreRenavi.class);

                if (listMad.size() > 0) {
                    JsfUtil.displayMessage("Registro de Nacido Vivo encontrado en el sistema.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    flagAnulacion = true;
                    this.BuscarMadre();
                } else {
                    JsfUtil.displayMessage("No se encuentra un Registro de Nacido Vivo en el sistema con la información solicitada, o el registro de Nacido Vivo tiene el estado incorrecto para ser anulado.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    cedulaBusq = null;
                }
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
            }
        }
    }
    
    /**/
    public void buscarMaCodigo() {
        List<MadreRenavi> listMad = new ArrayList<MadreRenavi>();
        if (ruta.equals("/pages/madreRenavi/Delete.xhtml")) {
            try {
                StringBuilder valor = new StringBuilder("select m.id_mad from madre_renavi m, nacido_vivo_renavi n where m.id_mad = ");
                valor.append(cedulaBusq);
                valor.append(" and m.id_mad = n.fk_cedul_mad");
                valor.append(" and n.fk_id_est_fir = 1");
                valor.append(" and n.fk_id_est_reg not in (7, 8, 9)");
                System.out.println(" ---> " + valor.toString());
                listMad = ejbFacade.executeNativeQueryListResultGenerico(valor.toString(), MadreRenavi.class);

                if (listMad.size() > 0) {
                    JsfUtil.displayMessage("Registro de Nacido Vivo encontrado en el sistema.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    flagAnulacion = true;
                    this.busquedaMadreIndo();
                } else {
                    JsfUtil.displayMessage("No se encuentra un Registro de Nacido Vivo en el sistema con la información solicitada, o el registro de Nacido Vivo tiene el estado incorrecto para ser anulado.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    cedulaBusq = null;
                }
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
            }
        } else {
            try {
                StringBuilder valor = new StringBuilder("select m.id_mad from madre_renavi m, nacido_vivo_renavi n where m.id_mad = ");
                valor.append(cedulaBusq);
                valor.append(" and m.id_mad = n.fk_cedul_mad and n.fk_id_est_reg in (4, 8)");
                valor.append(" and n.fk_id_est_fir = 2");
                System.out.println(" ---> " + valor.toString());
                listMad = ejbFacade.executeNativeQueryListResultGenerico(valor.toString(), MadreRenavi.class);

                if (listMad.size() > 0) {
                    JsfUtil.displayMessage("Registro de Nacido Vivo encontrado en el sistema.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    flagAnulacion = true;
                    this.busquedaMadreIndo();
                } else {
                    JsfUtil.displayMessage("No se encuentra un Registro de Nacido Vivo en el sistema con la información solicitada, o el registro de Nacido Vivo tiene el estado incorrecto para ser anulado.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    cedulaBusq = null;
                }
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
            }
        }
    }
    /**/

    public void buscarNV() {
        listObj = null;
        if (ruta.equals("/pages/madreRenavi/Delete.xhtml")) {
            StringBuilder query = new StringBuilder("select n.cedul_nac_viv, n.nombr_nac_viv, n.apell_nac_viv, n.fecha_nacim_nac_viv, n.fk_id_est_fir, n.fk_id_est_reg, n.fk_usu_saureg, n.fk_agencia_saureg");
            query.append(" from nacido_vivo_renavi n, madre_renavi mr");
            query.append(" where n.fk_cedul_mad = mr.id_mad and mr.cedul_mad = '");
            query.append(cedulaBusq);
            query.append("'");
            query.append(" and n.fk_id_est_fir = 1");
            query.append(" and n.fk_id_est_reg not in (7, 8, 9)");
            System.out.println("consulta " + query.toString());
            try {
                listObj = ejbLogs.executeNativeQueryListResult(query.toString());
                for (int i = 0; i < listObj.size(); i++) {
                    if(listObj.get(i)[6] == null || listObj.get(i)[7] == null) {
                        break;
                    } else {
                        agenciaMSP = this.getUsuarioByUserAndAgenciaMSP(listObj.get(i)[6].toString(), listObj.get(i)[7].toString());
                        break;
                    }
                }

                if (listObj.isEmpty() && agenciaMSP == null) {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    cedulaBusq = null;
                }
                RequestContext.getCurrentInstance().update("tblResultados");
            } catch (EntidadException e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder query = new StringBuilder("select n.cedul_nac_viv, n.nombr_nac_viv, n.apell_nac_viv, n.fecha_nacim_nac_viv, n.fk_id_est_fir, n.fk_id_est_reg, n.fk_usu_saureg, n.fk_agencia_saureg");
            query.append(" from nacido_vivo_renavi n, madre_renavi mr");
            query.append(" where n.fk_cedul_mad = mr.id_mad and mr.cedul_mad = '");
            query.append(cedulaBusq);
            query.append("'");
            if(flagAnulacion) {
            query.append(" and n.fk_id_est_reg in (4, 8)");
            }
            System.out.println("consulta " + query.toString());
            try {
                listObj = ejbLogs.executeNativeQueryListResult(query.toString());
                for (int i = 0; i < listObj.size(); i++) {
                    if(listObj.get(i)[6] == null || listObj.get(i)[7] == null) {
                        break;
                    } else {
                        agenciaMSP = this.getUsuarioByUserAndAgenciaMSP(listObj.get(i)[6].toString(), listObj.get(i)[7].toString());
                        break;
                    }
                }

                if (listObj.isEmpty() && agenciaMSP == null) {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    cedulaBusq = null;
                }
                RequestContext.getCurrentInstance().update("tblResultados");
            } catch (EntidadException e) {
                e.printStackTrace();
            }
        }
    }

    public void BusquedaWS(String cdl) {
        this.buscarNV();
        Persona perso = new Persona();
        try {
            perso = this.busquedaPorCedula(cdl, JsfUtil.USER_ACCWS_MADRE, JsfUtil.PASS_ACCWS_MADRE);
            if (perso.getCodigoError().equals("000") || (Integer.parseInt(perso.getCodigoError()) >= 200)) {
                //Verifico el genero, estado de ciudadania y la edad
                StringBuilder obsMadre = new StringBuilder();
                StringBuilder msjInconsistencias = new StringBuilder();
                if (perso.getCondicionCedulado() != null
                        && perso.getGenero() != null
                        && perso.getFechaNacimiento() != null
                        && perso.getCedula() != null
                        && perso.getNombre() != null) {
                    //Condición de cedulado;
                    hijoAux.setCondicCedMad(perso.getCondicionCedulado().trim());
                    obsMadre.append("CONDICIÓN DE CIUDADANÍA: ");
                    obsMadre.append(perso.getCondicionCedulado().trim());
                    if (perso.getFechaDefuncion() != null) {
                        hijoAux.setFecFallecMad(perso.getFechaDefuncion().trim());
                        obsMadre.append("; FECHA DE DEFUNCIÓN: ");
                        obsMadre.append(perso.getFechaDefuncion());
                        obsMadre.append("; FECHA DE INSCRIPCIÓN DE FALLECIMIENTO: ");
                        obsMadre.append(perso.getFechaInscripcionFallecimiento().trim());
                    }

                    //Género
                    hijoAux.setSexoMad(perso.getGenero().trim());
                    obsMadre.append("; GÉNERO: ");
                    obsMadre.append(perso.getGenero().trim());

                    //edad
                    try {
                        current.setFechaNacimMad(formatoFecha.parse(perso.getFechaNacimiento().trim()));
                        current.setAnioNacimMad(Short.valueOf(perso.getFechaNacimiento().trim().substring(6, 10)));
                        current.setMesNacimMad(Short.valueOf(perso.getFechaNacimiento().trim().substring(3, 5)));
                        current.setDiaNacimMad(Short.valueOf(perso.getFechaNacimiento().trim().substring(0, 2)));
                        //Edad de la madre
                        hijoAux.setEdadMad(this.calcularEdad(formatoFecha.parse(perso.getFechaNacimiento())));

                    } catch (ParseException ex) {
                        Logger.getLogger(MadreRenaviCDLController.class.getName()).log(Level.SEVERE, null, ex);
                        current.setFechaNacimMad(null);
                    }
                    //Datos de la madre
                    current.setNombrMad(perso.getNombre().trim());
                    current.setCedulMad(perso.getCedula().trim());
                    //Si la madre tiene registros por completar el registro de datos
                    int regNoCompletos = 0;
                    List<Object[]> itemsObjectFDatos = new ArrayList<Object[]>();
                    StringBuilder sqlFDatos = new StringBuilder("select COUNT(n.id_nac_viv), n.fk_cedul_mad ");
                    sqlFDatos.append("from nacido_vivo_renavi n, madre_renavi m ");
                    sqlFDatos.append("where n.fk_cedul_mad = m.id_mad ");
                    sqlFDatos.append("AND n.fk_id_est_reg < ");
                    sqlFDatos.append(JsfUtil.STAT_DAT_DOCTOR);
                    sqlFDatos.append(" AND m.cedul_mad = '");
                    sqlFDatos.append(current.getCedulMad());
                    sqlFDatos.append("' GROUP BY n.fk_cedul_mad");
                    try {
                        itemsObjectFDatos = ejbFacade.executeNativeQueryListResult(sqlFDatos.toString());
                        if (!itemsObjectFDatos.isEmpty()) {
                            for (Object[] objTemFDatos : itemsObjectFDatos) {
                                regNoCompletos = ((BigDecimal) objTemFDatos[0]).intValue();
                            }

                        } else {
                            //Si la madre no tubo un hijo recientemente.
                            Date fechaUltNacimiento = new Date();
                            long cedulaMadre = 0L;
                            List<Object[]> itemsObject = new ArrayList<Object[]>();
                            StringBuilder sql = new StringBuilder("select n.fecha_nacim_nac_viv, n.fk_cedul_mad, ");
                            sql.append("n.numer_parto_nac_viv,n.hijos_vivsa_mad,n.hijos_nvmrt_mad, ");
                            sql.append("n.hijos_nmrts_mad, n.num_parto_sistema ");
                            sql.append("from nacido_vivo_renavi n, madre_renavi m ");
                            sql.append("where n.fk_cedul_mad = m.id_mad AND m.cedul_mad = '");
                            sql.append(current.getCedulMad());
                            sql.append("' AND n.fecha_nacim_nac_viv = (");
                            sql.append("select MAX(n1.fecha_nacim_nac_viv) ");
                            sql.append("from nacido_vivo_renavi n1, madre_renavi m1 ");
                            sql.append("where n1.fk_cedul_mad = m1.id_mad AND m1.cedul_mad = '");
                            sql.append(current.getCedulMad());
                            sql.append("')");
                            //Solo registros firmados
                            /*Se aumenta 3 estados para la validacion de que la madre no puede tener partos seguido
                              los estados son estado_firma = 1, estado_registro = 6, 5 y 7
                              FWTM ---  16-06-2016
                            */
                            sql.append("and n.fk_id_est_fir in (1, 2) and n.fk_id_est_reg in (4, 5, 6, 7, 8)");
                            try {
                                itemsObject = ejbFacade.executeNativeQueryListResult(sql.toString());
                                if (!itemsObject.isEmpty()) {
                                    for (Object[] objTem : itemsObject) {
                                        fechaUltNacimiento = (Date) objTem[0];
                                        cedulaMadre = ((BigDecimal) objTem[1]).longValue();
                                        if (objTem[2] != null) {
                                            numParto = ((BigDecimal) objTem[2]).shortValue();
                                            hijoAux.setNumerPartoNacViv((short) (numParto + 1));
                                        } else {
                                            numParto = 0;
                                        }
                                        if (objTem[3] != null) {
                                            numHijosvivos = ((BigDecimal) objTem[3]).shortValue();
                                        } else {
                                            numHijosvivos = 0;
                                        }
                                        if (objTem[4] != null) {
                                            numHijosVivMuertos = ((BigDecimal) objTem[4]).shortValue();
                                            hijoAux.setHijosNvmrtMad(numHijosVivMuertos);
                                        } else {
                                            numHijosVivMuertos = 0;
                                        }
                                        if (objTem[5] != null) {
                                            numHijosNacierMuertos = ((BigDecimal) objTem[5]).shortValue();
                                            hijoAux.setHijosNmrtsMad(numHijosNacierMuertos);
                                        } else {
                                            numHijosNacierMuertos = 0;
                                        }
                                        if (objTem[6] != null) {
                                            hijoAux.setNumPartoSistema((short) (((BigDecimal) objTem[6]).shortValue() + 1));
                                        } else {
                                            hijoAux.setNumPartoSistema((short) 1);
                                        }
                                    }
                                    current.setIdMad(cedulaMadre);
                                    Long fchUltNcmnt = fechaUltNacimiento.getTime();
                                    Long fchActl = fechaActual.getTime();

                                } else {
                                    try {
                                        MadreRenavi mad = (MadreRenavi) ejbFacadeMadre.consultarTablaSingleResult("MadreRenavi.findByCedulMad", "cedulMad", current.getCedulMad());
                                        current.setIdMad(mad.getIdMad());
                                    } catch (EntidadException eem) {
                                        current.setIdMad(null);
                                    }
                                    numParto = 0;
                                    hijoAux.setNumerPartoNacViv((short) (numParto + 1));
                                    numHijosvivos = 0;
                                    numHijosVivMuertos = 0;
                                    hijoAux.setHijosNvmrtMad(numHijosVivMuertos);
                                    numHijosNacierMuertos = 0;
                                    hijoAux.setHijosNmrtsMad(numHijosNacierMuertos);
                                    hijoAux.setNumPartoSistema((short) 1);
                                }
                            } catch (Exception eenq) {
                                eenq.printStackTrace();
                                msjInconsistencias.append("Existió un error al consultar información de la madre. ");
                                flagInconsistencia = true;
                            }
                        }
                    } catch (Exception efd) {
                        efd.printStackTrace();
                        msjInconsistencias.append("Existió un error al consultar información de la madre. ");
                        flagInconsistencia = true;
                    }
                    //Si no hay inconsistencias
                    if (!flagInconsistencia) {
                        //Aisgnación del pais de residencia de la madre
                        hijoAux.setFkIdPaisResidnMad(ejbFacadePais.find(JsfUtil.COD_PAIS_EC));
                        //Asigno los apellidos de la madre al recien nacido
                        List<String> nmbrsMdr = UtilitarioNombre.separacion(current.getNombrMad());
                        if (!nmbrsMdr.isEmpty()) {
                            hijoAux.setApellNacViv(nmbrsMdr.get(0));
                        }
                        //Seteo la fecha de creación y de actualización del nacido vivo
                        hijoAux.setFechaCreacionNacViv(fechaActual);
                        hijoAux.setFechaActualizacionNacViv(fechaActual);
                        if (perso.getNombrePadre() != null || !perso.getNombrePadre().equals("")) {
                            current.setNomPadreMad(perso.getNombrePadre().trim());
                        }
                        if (perso.getNombreMadre() != null || !perso.getNombreMadre().equals("")) {
                            current.setNomMadreMad(perso.getNombreMadre().trim());
                        }
                        //Para la fotografía
                        if (perso.getCodigoError().equals("000")) {
                            try {
                                InputStream input = new ByteArrayInputStream(perso.getFotografia());
                                this.setImagem(new DefaultStreamedContent(input, "image/jpeg"));
                                hijoAux.setFotoMad(perso.getFotografia());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //Obtengo la nacionalidad de la madre
                        try {
                            if (!perso.getNacionalidad().equals("")) {
                                List<ParametroConsulta> paramsPais = new ArrayList<ParametroConsulta>();
                                if (perso.getNacionalidad().toUpperCase().trim().contains("ECUATORIANA")) {
                                    hijoAux.setFkIdNacMad(ejbFacadeNacionalidad.find(1));
                                    paramsPais.add(new ParametroConsulta("idPais", 81));
                                    listPaisDatMadre = ejbFacadePais.consultarTablaResultList("PaisRenavi.findByIdPais", paramsPais);
                                    hijoAux.setFkIdPaisMad(listPaisDatMadre.get(0));
                                    hijoAux.setCampoInecCodPaisMad(listPaisDatMadre.get(0).getCodigPais());
                                } else {
                                    NacionalidadRenavi nac = ejbFacadeNacionalidad.find(2);
                                    nac.setDescNac(perso.getNacionalidad());
                                    hijoAux.setFkIdNacMad(nac);
                                    paramsPais.add(new ParametroConsulta("idPais", 81));
                                    listPaisDatMadre = ejbFacadePais.consultarTablaResultList("PaisRenavi.findByCodigPaisMe", paramsPais);
                                    for (PaisRenavi p : listPaisDatMadre) {
                                        //System.out.println("--> " + );
                                        if (UtilitarioCaracteres.stripAccents(p.getDescPais())
                                                .contains(UtilitarioCaracteres.stripAccents(perso.getLugarNacimiento()))) {
                                            hijoAux.setFkIdPaisMad(p);
                                            hijoAux.setCampoInecCodPaisMad(p.getCodigPais());
                                            break;
                                        } else if (UtilitarioCaracteres.stripAccents(perso.getLugarNacimiento())
                                                .contains(UtilitarioCaracteres.stripAccents(p.getDescPais()))) {
                                            hijoAux.setFkIdPaisMad(p);
                                            hijoAux.setCampoInecCodPaisMad(p.getCodigPais());
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (EntidadException ee) {
                            ee.printStackTrace();
                            hijoAux.setFkIdNacMad(null);
                            hijoAux.setFkIdPaisMad(null);
                            hijoAux.setCampoInecCodPaisMad(null);
                        }
                        //Obtengo el estado civil de la madre
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
                                } else if (perso.getEstadoCivil().toUpperCase().contains("EN UNION DE HECHO")) {
                                    estadoCivil = ejbFacadeEstadoCivil.find(7);
                                }
                                hijoAux.setFkIdEstCivMad(estadoCivil);
                                obsMadre.append("ESTADO CIVIL: ");
                                obsMadre.append(perso.getEstadoCivil());
                            }
                        } catch (EntidadException ee) {
                            hijoAux.setFkIdEstCivMad(null);
                        }
                        if (perso.getConyuge() != null) {
                            hijoAux.setConyugeMad(perso.getConyuge().trim());
                            obsMadre.append(" ;CÓNYUGE: ");
                            obsMadre.append(perso.getConyuge().trim());
                        }
                        //Seteo la observación de la madre
                        hijoAux.setObsrvEstadoMad(obsMadre.toString());
                    } else {
                        msjInconsistencias.append("No se puede continuar con el proceso de registro de datos del nacido vivo.");
                        JsfUtil.displayMessage(msjInconsistencias.toString(), JsfUtil.ERROR_MESSAGE);
                        RequestContext.getCurrentInstance().update("contTab:mssgsBusquedaErrorM");
                    }
                } else {
                    JsfUtil.displayMessage("Los datos de la madre buscada no están completos.", JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusquedaM");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusquedaM");
                }
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistroPorCedulaNoEncontrado"), JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusquedaM");
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusquedaM");
            }
        } catch (Exception e) {
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("WebServiceErrorConsulta"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusquedaM");
            RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusquedaM");

        }
    }

    public void validarCelular() {
        String celular;

        celular = current.getCelularMad();

        if (celular.equals("99") || celular.length() > 9) {
            saveM();
        } else {
            JsfUtil.displayMessage("El número de celular está incorrecto.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
        }
    }

    public void validarNac() {
        int flag = 0;

        if (hijosAux.get(0).getFkIdEstReg().getIdEstReg().toString().equals("6") || hijosAux.get(0).getFkIdEstReg().getIdEstReg().toString().equals("7") || hijosAux.get(0).getFkIdEstReg().getIdEstReg().toString().equals("8")) {
            try {
                //int variable = Integer.parseInt(fechaPrenacimiento);
                Long tp = new Long(ejbFacadeVariable.find(2).getVarValor());
                fechaP = new Date(hijosAux.get(0).getFechaNacimNacViv().getTime() + tp);
                fechaF = new Date(hijosAux.get(0).getFechaNacimNacViv().getTime() - tp);
                fechaPrenacimiento = fechaF;
                ////INICIO DFJ ---- 0001176
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                params.add(new ParametroConsulta("cedulNacViv", hijosAux.get(0).getCodigoRcNacViv() != null ? hijosAux.get(0).getCodigoRcNacViv() + "-1" : hijosAux.get(0).getCedulNacViv() != null ? hijosAux.get(0).getCedulNacViv() + "-1" : null));
                NacidoVivoRenavi hijoAnulado = null;
                try {
                    hijoAnulado = (NacidoVivoRenavi) ejbFacadeNacidoVivoRenavi.consultarTablaSingleResult("NacidoVivoRenavi.findByCedulNacViv", params);
                } catch (EntidadException e) {
                }
                if (hijoAnulado != null) {
                    fechaPrenacimiento = hijoAnulado.getFechaNacimNacViv();
                    try {
                        fechaP = new Date(fechaPrenacimiento.getTime() + tp);
                        fechaF = new Date(fechaPrenacimiento.getTime() - tp);
                        fechaPrenacimiento = fechaF;
                    } catch (Exception ex) {
                        Logger.getLogger(MadreRenaviEVController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //// FIN 0001176
            } catch (EntidadException ex) {
                Logger.getLogger(MadreRenaviEVController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            fechaActual = new Date();
            fechaP = new Date();
        }

    }

    public int validarFechaN() {
        int flag = 0;
        if (hijosAux.size() > 1) {

            Date fechaNV;
            fechaNV = hijosAux.get(0).getFechaNacimNacViv();
            for (NacidoVivoRenavi item : hijosAux) {
                Date fc;
                fc = this.zeroFechaHora(item.getFechaNacimNacViv());
                if (!fc.equals(fechaNV)) {
                    Date fechaNV1 = this.sumarRestarHorasFecha(fechaNV, 24);
                    if (!fc.equals(fechaNV1)) {

                        flag = 1;
                        return flag;

                    } else {
                        flag = 0;
                    }
                } else {
                    flag = 0;
                }

            }

        }
        return flag;
    }

    public Date sumarRestarHorasFecha(Date fecha, int horas) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha); // Configuramos la fecha que se recibe

        calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0

        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas

    }

    public Date zeroFechaHora(Date fecha) {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }

    public void validarTipoParto() {
        itemp = new ArrayList<>();

        try {

            //Si no es mèdico solo puede seleccionar Parto Normal
            if (asistencia != null && asistencia.equals("MEDICO")) {
                itemp = ejbFacadeTipoPar.findAll();
            } else {
                itemp = ejbFacadeTipoPar.consultarTablaResultList("TipoPartoRenavi.findByIdTipPar", "idTipPar", new BigDecimal("1"));
            }

        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);

        }

    }

    public void validarParto() {
        if (hijosAux != null) {
            if (hijosAux.size() >= 2) {
                if (hijosAux.get(0).getFkIdTipPar().getIdTipPar() == 2) {
                    for (NacidoVivoRenavi item : hijosAux) {
                        if (item.getNumeroProductoNacViv() >= 2 && item.getFkIdTipPar().getIdTipPar() == 1) {
                            errtp = 1;
                        } else {
                            errtp = 0;
                        }
                    }
                }
            }
        }
    }

    public int getNumMalformacionReal() {
        return numMalformacionReal;
    }

    public void setNumMalformacionReal(int numMalformacionReal) {
        this.numMalformacionReal = numMalformacionReal;
    }

    private TblSauregUsuario getUsuarioByUserAndAgenciaMSP(java.lang.String nomUsuario, java.lang.String codMsp) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        return port.getUsuarioByUserAndAgenciaMSP(nomUsuario, codMsp);
    }
    
    public void seleccionBusqueda () {
        if (criterioBusqueda.equals("idMadre")) {
                CedulaValidator validar = new CedulaValidator();
                if (validar.validarCedula(cedulaBusq).equals("000")) {
                    this.buscarMA();
                } else {
                     if (validar.validarCedula(cedulaBusq).equals("001")) {
                            JsfUtil.displayMessage("Cédula no Válida", JsfUtil.ERROR_MESSAGE);
                            RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                            RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                        } else {
                            JsfUtil.displayMessage("Cédula Incompleta", JsfUtil.ERROR_MESSAGE);
                            RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                            RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                        }
                }
         } else {
            this.buscarMaCodigo();
        }
    }
    
    public void busquedaMadreIndo () {
        this.buscarNvIndo();
        current = new MadreRenavi();
        try {
            current = ejbFacadeMadre.find(Long.parseLong(cedulaBusq));
            setEdadMadre(String.valueOf(this.calcularEdad(current.getFechaNacimMad())));
            if (current == null) { 
                JsfUtil.displayMessage("Madre Indocumentada no Existe", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
            }
        } catch (Exception e) {
            JsfUtil.displayMessage("Madre Indocumentada no Válida", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
            RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
        }
    }
    
    public void buscarNvIndo() {
        listObj = null;
        if (ruta.equals("/pages/madreRenavi/Delete.xhtml")) {
            StringBuilder query = new StringBuilder("select n.cedul_nac_viv, n.nombr_nac_viv, n.apell_nac_viv, n.fecha_nacim_nac_viv, n.fk_id_est_fir, n.fk_id_est_reg, n.fk_usu_saureg, n.fk_agencia_saureg");
            query.append(" from nacido_vivo_renavi n, madre_renavi mr");
            query.append(" where n.fk_cedul_mad = mr.id_mad and mr.id_mad = ");
            query.append(cedulaBusq);
            query.append(" and n.fk_id_est_fir = 1");
            query.append(" and n.fk_id_est_reg not in (7, 8, 9)");
            System.out.println("consulta " + query.toString());
            try {
                listObj = ejbLogs.executeNativeQueryListResult(query.toString());
                for (int i = 0; i < listObj.size(); i++) {
                    if(listObj.get(i)[6] == null || listObj.get(i)[7] == null) {
                        break;
                    } else {
                        agenciaMSP = this.getUsuarioByUserAndAgenciaMSP(listObj.get(i)[6].toString(), listObj.get(i)[7].toString());
                        break;
                    }
                }

                if (listObj.isEmpty() && agenciaMSP == null) {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    cedulaBusq = null;
                }
                RequestContext.getCurrentInstance().update("tblResultados");
            } catch (EntidadException e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder query = new StringBuilder("select n.cedul_nac_viv, n.nombr_nac_viv, n.apell_nac_viv, n.fecha_nacim_nac_viv, n.fk_id_est_fir, n.fk_id_est_reg, n.fk_usu_saureg, n.fk_agencia_saureg");
            query.append(" from nacido_vivo_renavi n, madre_renavi mr");
            query.append(" where n.fk_cedul_mad = mr.id_mad and mr.id_mad = ");
            query.append(cedulaBusq);
            if(flagAnulacion) {
            query.append(" and n.fk_id_est_reg in (4, 8)");
            }
            System.out.println("consulta " + query.toString());
            try {
                listObj = ejbLogs.executeNativeQueryListResult(query.toString());
                for (int i = 0; i < listObj.size(); i++) {
                    if(listObj.get(i)[6] == null || listObj.get(i)[7] == null) {
                        break;
                    } else {
                        agenciaMSP = this.getUsuarioByUserAndAgenciaMSP(listObj.get(i)[6].toString(), listObj.get(i)[7].toString());
                        break;
                    }
                }

                if (listObj.isEmpty() && agenciaMSP == null) {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ItemsIsEmpty"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_anular_M:mssgsBusqueda");
                    RequestContext.getCurrentInstance().update("form_eliminacion:mssgsBusqueda");
                    cedulaBusq = null;
                }
                RequestContext.getCurrentInstance().update("tblResultados");
            } catch (EntidadException e) {
                e.printStackTrace();
            }
        }
    }

}
