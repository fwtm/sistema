package ec.gob.digercic.renavi.view;

/**/
import ec.gob.digercic.renavi.ejb.CatalogoProfesionalRenaviFacadeLocal;
/**/
import ec.gob.digercic.renavi.ejb.CatalogoRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.MadreRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.MadreRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.AlfabetismoRenavi;
import ec.gob.digercic.renavi.entities.CatalogoProfesionalRenavi;
import ec.gob.digercic.renavi.entities.CatalogoRenavi;
import ec.gob.digercic.renavi.entities.EstadoRegistroRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.entities.NivelInstruccionRenavi;
import ec.gob.digercic.renavi.entities.PaisRenavi;
import ec.gob.digercic.renavi.entities.TipoPartoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.UtilitarioNombre;
import ec.gob.digercic.renavi.ws.cedula.Cedula;
import ec.gob.digercic.renavi.ws.datmadre.Persona;
import ec.gob.digercic.saureg.entities.TblSauregUbicacion;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
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
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "madreRenaviIndocEVController")
@ViewScoped
public class MadreRenaviIndocEVController implements Serializable {

    private MadreRenavi current;
    private List<MadreRenavi> items;
    private NacidoVivoRenavi hijoAux;
    private Integer tabIndex = 0;

    private List<PaisRenavi> listPais;
    private List<PaisRenavi> listPaisDatosMad;
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
    private LogAccesosRenaviFacadeLocal ejbLogs;
    @EJB
    private ec.gob.digercic.renavi.ejb.TipoPartoRenaviFacadeLocal ejbFacadeTipoPar;
    @EJB
    private CatalogoRenaviFacadeLocal ejbCatRenavi;
    /**/
    @EJB
    private CatalogoProfesionalRenaviFacadeLocal ejbFacadeCatalogoProfe;

    private List<TblSauregUbicacion> provincias;
    private String provincia;
    private List<TblSauregUbicacion> cantones;
    private String canton;
    private List<TblSauregUbicacion> parroquias;
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
    private Date fechaActual = new Date();
    private Date fechaP;
    private Date fechaF;
    private Date fechaAnterior;
    private Date fechaInicio;
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
    private ec.gob.digercic.renavi.ws.TblSauregUsuario tblSauregUsuario;//JJHF CAMBIO LOGIN
    private String nomUsuarioSaureg;
    private String nombreDeLaMadre;

    private String nombreMad;
    private String apellidoMad;

    private String flagc;
    private Date fechanacmax;
    private int errtp = 0;
    private List<TipoPartoRenavi> itemp;
    private List<CatalogoRenavi> listcat = new ArrayList<CatalogoRenavi>();
    private String malformaciones = "ÚNICA";
    private Date fechaPrenacimiento;
    /**/
    private List<CatalogoProfesionalRenavi> lstProfesion = new ArrayList<CatalogoProfesionalRenavi>();
    private List<ec.gob.digercic.renavi.ws.TblSauregRol> listaRol = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregRol>();
    /**/
    private Boolean flagRol = false;
    private String asistencia;

    //INICIO DFJ
    private boolean tieneMalformacion = false;
    private List malformacionesSeleccionadas;
    private int numMalformacionReal = 0;
    private String lugarNacimiento;
    //FIN DFJ
    private List<CatalogoRenavi> listTipoSangre;//DFJ

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }
    /**/

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public MadreRenaviIndocEVController() {
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

    public List<MadreRenavi> getItems() {
        try {
            items = getFacade().findAll();
            return items;
        } catch (EntidadException ee) {
            ee.printStackTrace();
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

    public SelectItem[] getItemsAvailableSelectOne() {
        try {
            return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
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

    public Date getFechaAnterior() {
        return fechaAnterior;
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
    }//JJHF CAMBIO LOGIN

    public void setUserLgn(ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn) {//JJHF CAMBIO LOGIN
        this.userLgn = userLgn;//JJHF CAMBIO LOGIN
    }//JJHF CAMBIO LOGIN

    public Date getFechaPrenacimiento() {
        return fechaPrenacimiento;
    }

    public void setFechaPrenacimiento(Date fechaPrenacimiento) {
        this.fechaPrenacimiento = fechaPrenacimiento;
    }

    public List<ec.gob.digercic.renavi.ws.TblSauregUsuario> getTblSauregUsuarioList() {//JJHF CAMBIO LOGIN
        return tblSauregUsuarioList;//JJHF CAMBIO LOGIN
    }//JJHF CAMBIO LOGIN

    public Date getFechaP() {
        this.validarNac();
        return fechaP;
    }

    public void setFechaP(Date fechaP) {
        this.fechaP = fechaP;
    }

    public List<TipoPartoRenavi> getItemp() {
        this.validarTipoParto();
        return itemp;
    }

    public void setItemp(List<TipoPartoRenavi> itemp) {
        this.itemp = itemp;
    }

    public Date getFechaF() {
        return fechaF;
    }

    public void setFechaF(Date fechaF) {
        this.fechaF = fechaF;
    }

    public Date getFechanacmax() {

        fechanacmax = this.sumarRestarDiasFecha(hijosAux.get(0).getFechaCreacionNacViv(), 1);
        return fechanacmax;
    }

    public List<CatalogoRenavi> getListcat() {
        return listcat;
    }

    public void setListcat(List<CatalogoRenavi> listcat) {
        this.listcat = listcat;
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
//            if (malformacionesSeleccionadas.size() == 1) {
//                malformaciones = "ÚNICA";
//            } else {
//                malformaciones = "MULTIPLE";
//            }
        } else {
//            malformaciones = null;
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
        numMalformacionReal = 0;
        malformaciones = "ÚNICA";
        //Carga catalogo de malformaciones dependiendo del sexo 
        if (tieneMalformacion) {
            listcat = ejbCatRenavi.getCatalogo("MALFORMACIONES", "'0','" + hijoAux.getFkIdSexoNv().getIdSexo() + "'");
        }
    }

    public void setFechanacmax(Date fechanacmax) {
        this.fechanacmax = fechanacmax;
    }

    public void cambiarTblSauregUsuarioList(ValueChangeEvent event) {
        for (ec.gob.digercic.renavi.ws.TblSauregUsuario item : tblSauregUsuarioList) {//JJHF CAMBIO LOGIN
            if (item.getNomUsuario().equals(event.getNewValue())) {//JJHF CAMBIO LOGIN
                tblSauregUsuario = item;//JJHF CAMBIO LOGIN
                tblSauregUsuario.setAgenciaInUser(userLgn.getAgenciaInUser());//JJHF CAMBIO LOGIN
                break;//JJHF CAMBIO LOGIN
            }
        }
    }

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

    public String getNombreDeLaMadre() {
        return nombreDeLaMadre;
    }

    public void setNombreDeLaMadre(String nombreDeLaMadre) {
        this.nombreDeLaMadre = nombreDeLaMadre;
    }

    public List<PaisRenavi> getListPais() {

        if (listPais == null) {
            listPais = new ArrayList<PaisRenavi>();
        }
        return listPais;
    }

    public List<PaisRenavi> getListPaisDatosMad() {
        if (listPaisDatosMad == null) {
            listPaisDatosMad = new ArrayList<PaisRenavi>();
        }
        return listPaisDatosMad;
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

    @PostConstruct
    public void iniciar() {
        try {
            //Obtengo el usuario del sistema
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            tblSauregUsuario = userLgn;
            //****************************************
            String idMad = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id_Mad");
            //****************************************
            //parametros de busqueda de la madre, criterio por  cedula, institución, hijos activos
            //madres activas, instituciones activas, sin firmar.
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            Short numPartoSistema = new Short(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numPartoSistema"));
            parametros.add(new ParametroConsulta("numPartoSistema", numPartoSistema));
            String cedulMad = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cedulMad");
            //parametros.add(new ParametroConsulta("cedulMad", cedulMad));
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
                    if (userLgn.getUserInstitInUser().getNomCorto() != null) {
                        if (catalogoRenavi.getNombreCatRenavi().contains(userLgn.getUserInstitInUser().getNomInstituc())) {
                            lugarNacimiento = catalogoRenavi.getNombreCatRenavi();
                            break;
                        }
                    }
                }
            }
            //FIN DFJ

            parametros.add(new ParametroConsulta("fkAgenciaSaureg", Long.valueOf(userLgn.getAgenciaInUser().getCodMsp())));
            parametros.add(new ParametroConsulta("fkUsuSaureg", userLgn.getNomUsuario()));
            parametros.add(new ParametroConsulta("statusNV", JsfUtil.ESTADO_REG_ACTIVO));
            parametros.add(new ParametroConsulta("statusM", JsfUtil.ESTADO_REG_ACTIVO));
            parametros.add(new ParametroConsulta("estFirma", JsfUtil.STAT_FIR_SIN));
            //Parametro para estado de registro = 5 (DIGERCIC)
            parametros.add(new ParametroConsulta("estRegistro", 5));
            hijosAux = new ArrayList<NacidoVivoRenavi>();
            //hijosAux = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByNumPartoSistemaCedulMadAndInstitucion", parametros);
            //****************************************************
            if (!cedulMad.equals("null")) {
                parametros.add(new ParametroConsulta("cedulMad", cedulMad));
                hijosAux = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByNumPartoSistemaCedulMadAndInstitucion", parametros);
            } else {
                parametros.add(new ParametroConsulta("idMad", Integer.parseInt(idMad)));
                hijosAux = ejbFacadeNacidoVivoRenavi.consultarTablaResultList("NacidoVivoRenavi.findByNumPartoSistemaCedulMadNoIdentAndInstitucion", parametros);
            }
            //****************************************************
            Collections.sort(hijosAux, COMPARATOR);
            hijoAux = (NacidoVivoRenavi) BeanUtils.cloneBean(hijosAux.get(0));
            hijoAux.setIdNacViv(null);
            //Obtengo el tiempo prenacimiento
            Long tp = new Long(ejbFacadeVariable.find(2).getVarValor());
            fechaActual = new Date();
            fechaPrenacimiento = new Date(fechaActual.getTime() - tp);
            /*CAMBIOS PARA EL CALENDARIO DE RECIEN NACIDO EDICION   FWTM  26-01-2016*/
            Calendar c1 = GregorianCalendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMMM/yyyy hh:mm:ss");
            c1.add(Calendar.DATE, -4);
            fechaInicio = sdf.parse(sdf.format(c1.getTime()));
            /**/
            //El usuario que tiene permisos de firma
            try {
                if (hijoAux.getFkUsuFirmaSaureg() != null && hijoAux.getFkAgenciaFirmaSaureg() != null) {
                    tblSauregUsuario = this.UsuarioByUserAndCodAgenciaMSP(hijoAux.getFkUsuFirmaSaureg(), hijoAux.getFkAgenciaFirmaSaureg().toString());//JJHF CAMBIO LOG IN
                    //tblSauregUsuario.setAgenciaInUser(userLgn.getAgenciaInUser());//JJHF CAMBIO LOGIN
                    nomUsuarioSaureg = hijoAux.getFkUsuFirmaSaureg();
                }
            } catch (Exception euf) {
                tblSauregUsuario = null;
                nomUsuarioSaureg = null;
                euf.printStackTrace();
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("SelectEntidadExceptionEdit"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
            }
            hijosVivosSistema = hijoAux.getHijosNaciervivSistema();
            //Obtengo la madre de los recien nacidos
            current = hijoAux.getFkCedulMad();
            this.setNombreDeLaMadre(current.getNombrMad());
            //inicio dfj
            //Si edita una madre indocumentada que no se a guardado por separdo nombres y apellidos
            if (current.getApellidosMadIndoc() == null) {
                List<String> nmbrsMdr = UtilitarioNombre.separacion(current.getNombrMad());
                try {
                    this.setApellidoMad(nmbrsMdr.get(0));
                    this.setNombreMad(nmbrsMdr.get(1));
                } catch (Exception e) {
                }
            } else {
                this.setNombreMad(current.getNombresMadIndoc());
                this.setApellidoMad(current.getApellidosMadIndoc());
            }
            //fin DFJ
            //obtengo los labels de ayuda para la información historica de partos
            Date fechaUltNacimiento = new Date();
            List<Object[]> itemsObject = new ArrayList<Object[]>();
            StringBuilder sql = new StringBuilder("select distinct(n.fecha_creacion_nac_viv), n.numer_parto_nac_viv,");
            sql.append("n.hijos_vivsa_mad,n.hijos_nvmrt_mad,n.hijos_nmrts_mad ");
            sql.append("from nacido_vivo_renavi n, madre_renavi m ");
            sql.append("where n.fk_cedul_mad = m.id_mad AND m.cedul_mad = '");
            sql.append(current.getCedulMad());
            sql.append("'");
            try {
                itemsObject = ejbFacade.executeNativeQueryListResult(sql.toString());
                if (itemsObject.size() > 1) {
                    numParto = ((BigDecimal) itemsObject.get(itemsObject.size() - 1)[1]).shortValue();
                    numHijosvivos = ((BigDecimal) itemsObject.get(itemsObject.size() - 1)[2]).shortValue();
                    numHijosVivMuertos = ((BigDecimal) itemsObject.get(itemsObject.size() - 1)[3]).shortValue();
                    numHijosNacierMuertos = ((BigDecimal) itemsObject.get(itemsObject.size() - 1)[4]).shortValue();
                } else {
                    numParto = 0;
                    numHijosvivos = 0;
                    numHijosVivMuertos = 0;
                    numHijosNacierMuertos = 0;
                }
            } catch (Exception edhp) {
                edhp.printStackTrace();
            }
            if (hijoAux.getResidnProvdsMad() != null && hijoAux.getResidnProvidMad() != null) {
                provincia = hijoAux.getResidnProvidMad() + ".- " + hijoAux.getResidnProvdsMad();
                cantones = new ArrayList<TblSauregUbicacion>();
                try {
                    List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                    params.add(new ParametroConsulta("codExterno1", hijoAux.getResidnProvidMad()));
                    params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                    cantones = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodExterno1", params);
                    canton = hijoAux.getResidnCantidMad() + ".- " + hijoAux.getResidnCantdsMad();
                } catch (EntidadException ec) {
                    ec.printStackTrace();
                }
            }
            if (hijoAux.getResidnCantdsMad() != null && hijoAux.getResidnCantidMad() != null) {
                parroquias = new ArrayList<TblSauregUbicacion>();
                try {
                    List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                    params.add(new ParametroConsulta("codExterno1", hijoAux.getResidnCantidMad()));
                    params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                    parroquias = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodExterno1", params);
                    parroquia = hijoAux.getResidnParridMad() + ".- " + hijoAux.getResidnParrdsMad();
                } catch (EntidadException ec) {
                    ec.printStackTrace();
                }
            }
            if (hijoAux.getFkIdSabeLeerMad() != null) {
                this.obtenerSabeLeer(hijoAux.getFkIdSabeLeerMad());
            }
            //Obtengo la lista de paises
            listPais = ejbFacadePais.findAll();
            listPaisDatosMad = ejbFacadePais.findAll();
        } catch (Exception e) {
            System.gc();
            hijoAux = new NacidoVivoRenavi();
            current = new MadreRenavi();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("SelectEntidadExceptionEdit"), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
        }
    }
//JJHF CAMBIO LOGIN

    private ec.gob.digercic.renavi.ws.TblSauregUsuario UsuarioByUserAndCodAgenciaMSP(java.lang.String user, java.lang.String codAgencia) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario usuario = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
        ec.gob.digercic.renavi.ws.TblSauregUsuario Usuarios;
        Usuarios = port.getUsuarioByUserAndAgenciaMSP(user, codAgencia);
        return Usuarios;

    }

    //JJHF CAMBIO LOGIN
    public Date sumarRestarDiasFecha(Date fecha, int dias) {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0

        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

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

    /**
     * ***************************
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
            log.setAccion("REVIT. REGISTRO DE MADRES. GUARDADO DE MADRES");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            ejbLogs.create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        try {

            //            validacion si es parto multiple y cambio de estado
// CAMBIO JUAN JOSE HERNANDEZ
//           
            String c = hijoAux.getFkIdEstReg().toString();
            System.out.println("C " + c);
            if (c.equals("DATOS INCOMPLETOS") || c.equals("DATOS INCOMPLETOS DE LA MADRE") || c.equals("DATOS INCOMPLETOS NACIDO VIVO")) {
                hijoAux.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_MADRE));
            } else {
                hijoAux.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_DOCTOR));
                if (c.equals("REGISTRO INCOMPLETO ANULACION") || c.equals("REGISTRO ANULACION COMPLETO")) {
                    hijoAux.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_ANULADO_FE));
                }

            }

// CAMBIO JUAN JOSE HERNANDEZ            
//            validacion si es parto multiple y cambio de estado
            //Cuando los hijos vivos son iguales
            MadreRenavi madre = new MadreRenavi();
            madre = hijoAux.getFkCedulMad();
            //inicio DFJ
            madre.setNombresMadIndoc(this.getNombreMad());
            madre.setApellidosMadIndoc(this.getApellidoMad());
            madre.setNombrMad(this.getApellidoMad() + " " + this.getNombreMad());
            //fin DFJ
            ejbFacade.edit(madre);

            for (int i = 0; i < hijosAux.size(); i++) {
                hijosAux.get(i).setApellNacViv(getApellidoMad());
            }

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
                    JsfUtil.displayMessage("Se han creado " + creadosNV + " registro(s) de nacido vivo " + ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("MadreRenaviCreado"), JsfUtil.INFO_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                    //Actualizo el nuevo producto del embrazo
                    hijosVivosSistema = hijosAux.get(0).getHijosNaciervivSistema();
                } else {
                    JsfUtil.displayMessage("No se pudo guardar la información de la madre. Contáctese con el administrador del sistema.", JsfUtil.ERROR_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaM");
                    limpiar();
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
        for (NacidoVivoRenavi nv : hijosAux) {

            byte[] pdf;
            try {
                pdf = this.pdfSinFirma(nv, tblSauregUsuario);
            } catch (Exception epdf) {
                pdf = null;
            }
            if (pdf != null) {
                //Seteo el pdf generado y edito el registro con el pdf generado
                try {
                    nv.setPdfSinFirmaNacViv(pdf);
                    nv.setFechaActualizacionNacViv(new Date());
                    ejbFacadeNacidoVivoRenavi.edit(nv);

                } catch (EntidadException ee2) {
                    ee2.printStackTrace();
                }
            }
        }

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
                temp.setApellNacViv(item.getApellNacViv());
                temp.setFkIdSexoNv(item.getFkIdSexoNv());
                temp.setTallaNacViv(item.getTallaNacViv());
                temp.setPesoNacViv(item.getPesoNacViv());
                temp.setFechaNacimNacViv(item.getFechaNacimNacViv());
                temp.setFkIdTipPar(item.getFkIdTipPar());
                temp.setApgar1NacViv(item.getApgar1NacViv());
                temp.setApgar2NacViv(item.getApgar2NacViv());
                temp.setNumeroHistoriaNacViv(item.getNumeroHistoriaNacViv());
                temp.setNumeroProductoNacViv((short) (numerProducto));
                temp.setFechaActualizacionNacViv(new Date());
                temp.setCodigoRcNacViv(item.getCodigoRcNacViv());
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
                tempr.setFechaActualizacionNacViv(new Date());
                tempr.setCodigoRcNacViv(item.getCodigoRcNacViv());
                tempr.setNumeroHistoriaNacViv(item.getNumeroHistoriaNacViv());
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
                tempcc.setNumeroHistoriaNacViv(null);
                tempcc.setCodigoRcNacViv(null);
                tempcc.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_MADRE));
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
                            //FIN DFJ
                            ejbFacadeNacidoVivoRenavi.remove(item);
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
                        tempr.setFechaActualizacionNacViv(new Date());
                        tempr.setCodigoRcNacViv(item.getCodigoRcNacViv());
                        tempr.setNumeroHistoriaNacViv(item.getNumeroHistoriaNacViv());
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
        String idProvincia;
        idProvincia = userLgn.getAgenciaInUser().getIdProvincia().getCodExterno1();
        hijoAux.setNumeroHistoriaNacViv(idProvincia.concat(userLgn.getAgenciaInUser().getCodMsp()).concat(hijoAux.getIdNacViv().toString()));
        try {

            //            validacion si es parto multiple y cambio de estado
// CAMBIO JUAN JOSE HERNANDEZ
            String c = hijoAux.getFkIdEstReg().toString();
            if (c.equals("DATOS INCOMPLETOS") || c.equals("DATOS INCOMPLETOS DE LA MADRE") || c.equals("DATOS INCOMPLETOS NACIDO VIVO")) {
                hijoAux.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_NACVIVO));
            } else {
                hijoAux.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_DOCTOR));
                if (c.equals("REGISTRO INCOMPLETO ANULACION") || c.equals("REGISTRO ANULACION COMPLETO")) {
                    hijoAux.setFkIdEstReg(ejbFacadeEstadoRegistro.find(JsfUtil.STAT_ANULADO_FE));
                }

            }

// CAMBIO JUAN JOSE HERNANDEZ            
//            validacion si es parto multiple y cambio de estado 
            hijoAux.setFechaActualizacionNacViv(new Date());
            //hijoAux.setFkAgenciaSaureg(userLgn.getAgenciaInUser().getIdAgencia());
            //hijoAux.setFkUsuSaureg(userLgn.getNomUsuario());
            hijoAux.setMalformacionesConge(malformaciones);//DFJ  
            hijoAux.setLugNacEspecifique(lugarNacimiento); //dfj
            ejbFacadeNacidoVivoRenavi.edit(hijoAux);
            ejbFacadeNacidoVivoRenavi.agregarMalformaciones(hijoAux, malformacionesSeleccionadas);

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
            hijoAux = null;
            RequestContext.getCurrentInstance().execute("PF('hijoDialog').hide()");
            RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorActualizacion"), JsfUtil.ERROR_MESSAGE);
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
        if (err != 1 && errtp != 1) {

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
                log.setAccion("REVIT. REGISTRO DE MADRES. GUARDADO DE NACIDOS VIVOS");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
                CapturaIP ip = new CapturaIP();
                log.setIp(ip.obieneDireccionIP());
                ejbLogs.create(log);
            } catch (EntidadException e) {
                e.printStackTrace();
            }
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
                                nomUsuarioSaureg = userLgn.getNomUsuario();
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
            for (NacidoVivoRenavi nv : hijosAux) {
                if (nv.getNombrNacViv().contains("-" + nv.getNumeroProductoNacViv().toString())) {
                    byte[] pdf;
                    try {
                        pdf = this.pdfSinFirma(nv, tblSauregUsuario);
                    } catch (Exception epdf) {
                        pdf = null;
                    }
                    if (pdf != null) {
                        //Seteo el pdf generado y edito el registro con el pdf generado
                        try {
                            nv.setPdfSinFirmaNacViv(pdf);
                            nv.setFechaActualizacionNacViv(new Date());
                            ejbFacadeNacidoVivoRenavi.edit(nv);

                        } catch (EntidadException ee2) {
                            ee2.printStackTrace();
                        }
                    }
                } else {

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
                    byte[] pdf;
                    try {
                        pdf = this.pdfSinFirma(nv, tblSauregUsuario);
                    } catch (Exception epdf) {
                        pdf = null;
                    }
                    if (pdf != null) {
                        //Seteo el pdf generado y edito el registro con el pdf generado
                        try {
                            nv.setPdfSinFirmaNacViv(pdf);
                            nv.setFechaActualizacionNacViv(new Date());
                            ejbFacadeNacidoVivoRenavi.edit(nv);

                        } catch (EntidadException ee2) {
                            ee2.printStackTrace();
                        }
                    }
                }
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
    }

    public void atrasNV() {
        try {
            hijoAux = (NacidoVivoRenavi) BeanUtils.cloneBean(hijosAux.get(0));
            hijoAux.setIdNacViv(null);
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
        }
        EstadoRegistroRenavi estado = new EstadoRegistroRenavi();
        try {

            //            validacion si es parto multiple y cambio de estado
// CAMBIO JUAN JOSE HERNANDEZ
            String c = hijoAux.getFkIdEstReg().toString();

            if (c.equals("DATOS INCOMPLETOS") || c.equals("DATOS INCOMPLETOS DE LA MADRE") || c.equals("DATOS INCOMPLETOS NACIDO VIVO")) {

                estado = ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_DOCTOR);
            } else {
                estado = ejbFacadeEstadoRegistro.find(JsfUtil.STAT_DAT_DOCTOR);
                if (c.equals("REGISTRO INCOMPLETO ANULACION") || c.equals("REGISTRO ANULACION COMPLETO")) {
                    estado = ejbFacadeEstadoRegistro.find(JsfUtil.STAT_ANULADO_FE);
                }

            }

// CAMBIO JUAN JOSE HERNANDEZ            
//            validacion si es parto multiple y cambio de estado
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
                    hijosAux.get(i).setFechaActualizacionNacViv(new Date());
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
                            hijosAux.get(i).setFechaActualizacionNacViv(new Date());
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
                } catch (EntidadException ee1) {
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
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
        } else {
            JsfUtil.displayMessage("No se pudo recuperar información referente al estado del registro. No se ha finalizado el proceso.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:contTab:mssgsBusquedaD");
        }
        RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("Find.jsf");
        } catch (Exception e) {
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
                MadreRenaviIndocEVController controller = (MadreRenaviIndocEVController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "madreRenaviCDLController");
                return controller.ejbFacade.find(getKey(value));
            } catch (EntidadException ee) {
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

    public void limpiar() {
        this.current = null;
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
        System.gc();
    }

    /**
     * Método para limpiar la consulta cuando el usuario ha seleccionado buscar
     * por cédula.
     */
    public void limpiarBuscarPorCedula() {
        this.hijoAux = new NacidoVivoRenavi();
        this.current = new MadreRenavi();

        cedulaBusq = null;
        nivelesInstruccion = null;
        flagInconsistencia = false;
        provincia = null;
        cantones = null;
        canton = null;
        parroquias = null;
        parroquia = null;
        //
        numParto = null;
        numHijosvivos = null;
        numHijosVivMuertos = null;
        numHijosNacierMuertos = null;
        hijosVivosSistema = null;
        porEliminarNV = 0;
        System.gc();
    }

    /**
     * Método para limpiar la consulta cuando el usuario ha seleccionado buscar
     * por appellidos y nombres.
     */
    public void limpiarBuscarPorApellNom() {
        this.hijoAux = new NacidoVivoRenavi();
        this.current = new MadreRenavi();
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
//        if (hijosAux == null) {
//            hijosAux = new ArrayList<NacidoVivoRenavi>();
//        }
        return hijosAux;
    }

    public List<NacidoVivoRenavi> getHijosAuxDelete() {
        return hijosAuxDelete;
    }

    public void setHijosAuxDelete(List<NacidoVivoRenavi> hijosAuxDelete) {
        this.hijosAuxDelete = hijosAuxDelete;
    }

    private Cedula numeroCedula(java.lang.String provincia) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
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
                ee.printStackTrace();
            }
            System.gc();
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
        return fechaActual;
    }

//    public Date getFechaPrenacimiento() {
//        return fechaPrenacimiento;
//    }
    public String getFlagc() {
        return flagc;
    }

    public void setFlagc(String flagc) {
        this.flagc = flagc;
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
            limpiarBuscarPorCedula();
        } else {
            limpiarBuscarPorApellNom();
        }
        RequestContext.getCurrentInstance().execute("PF('statusDialog').hide()");
    }

    /* Provincia canton y parroquia para ma madre---------------------------------------------------------------------------------*/
    public byte[] pdfSinFirma(NacidoVivoRenavi nv, ec.gob.digercic.renavi.ws.TblSauregUsuario usuarioFirma) throws JRException, IOException, SQLException {
        try {
            List<NacidoVivoRenavi> items = new ArrayList<NacidoVivoRenavi>();
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
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users1.jasper");
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(items);
            jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
            byte[] pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdf1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error -->" + e.getMessage());
            return null;
        }
    }

    /**
     * Método para llenar el combo de paises al seleccionar la nacionalidad de
     * la madre. Esto es cuando la madre es identificada.
     *
     * @param event
     */
    public void cambiarPais(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            try {
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                if (event.getNewValue().toString().equals("ECUATORIANA")) {
                    params.add(new ParametroConsulta("idPais", 81));
                    listPaisDatosMad = ejbFacadePais.consultarTablaResultList("PaisRenavi.findByIdPais", params);
                    hijoAux.setFkIdPaisMad(listPaisDatosMad.get(0));
                } else {
                    params.add(new ParametroConsulta("idPais", 81));
                    listPaisDatosMad = ejbFacadePais.consultarTablaResultList("PaisRenavi.findByCodigPaisMe", params);
                    hijoAux.setFkIdPaisMad(null);
                }
            } catch (EntidadException e) {
                e.printStackTrace();
            }
        }
    }

    public void seleccionFechaNacMadre(SelectEvent event) {
        Calendar fechaNacimiento = Calendar.getInstance();
        fechaNacimiento.setTime(current.getFechaNacimMad());
        StringBuilder msjInconsistencias = new StringBuilder();
        Calendar fechaActual = Calendar.getInstance();
        int anioMad = fechaNacimiento.get(Calendar.YEAR);
        int mesMad = fechaNacimiento.get(Calendar.MONTH);
        int diaMad = fechaNacimiento.get(Calendar.DATE);
        int año = fechaActual.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH) - fechaNacimiento.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE) - fechaNacimiento.get(Calendar.DATE);
        if (mes < 0 || (mes == 0 && dia < 0)) {
            año--;
        }
        short edad = (short) año;
        hijoAux.setEdadMad(edad);
        if (!(hijoAux.getEdadMad() <= 56 && hijoAux.getEdadMad() >= 8)) {
            msjInconsistencias.append("La edad de la madre está fuera del rango establecido. ");
            RequestContext.getCurrentInstance().execute("alert('Edad no permitida')");
            flagInconsistencia = true;
        } else {
            flagInconsistencia = false;
        }
        if (!flagInconsistencia) {
            hijoAux.setSexoMad(JsfUtil.SEXO_MADRE);
            hijoAux.setFechaCreacionNacViv(this.fechaActual);
            hijoAux.setFechaActualizacionNacViv(this.fechaActual);
            short anioM = (short) anioMad;
            short mesM = (short) mesMad;
            short diaM = (short) diaMad;
            current.setAnioNacimMad(anioM);
            current.setMesNacimMad(mesM);
            current.setDiaNacimMad(diaM);
            hijoAux.setNumPartoSistema(JsfUtil.STAT_NUM_PART_SIST);
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
                //Busca si tiene anulado para bloquear edicion de fechas 
                List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                params.add(new ParametroConsulta("codigoRcNacViv", hijosAux.get(0).getCodigoRcNacViv() != null ? hijosAux.get(0).getCodigoRcNacViv() + "-1" : hijosAux.get(0).getCedulNacViv() != null ? hijosAux.get(0).getCedulNacViv() + "-1" : null));
                NacidoVivoRenavi hijoAnulado = null;
                try {
                    hijoAnulado = (NacidoVivoRenavi) ejbFacadeNacidoVivoRenavi.consultarTablaSingleResult("NacidoVivoRenavi.findByCodigoRcNacViv", params);
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
            } catch (EntidadException e) {
                Logger.getLogger(MadreRenaviEVController.class.getName()).log(Level.SEVERE, null, e);
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
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
        }
    }

    public void validarParto() {

        if (hijosAux != null) {

            if (hijosAux.size() >= 2) {
                if (hijosAux.get(0).getFkIdTipPar().getIdTipPar().toString().equals("2")) {

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

    public String getNombreMad() {
        return nombreMad;
    }

    public void setNombreMad(String nombreMad) {
        this.nombreMad = nombreMad;
    }

    public String getApellidoMad() {
        return apellidoMad;
    }

    public void setApellidoMad(String apellidoMad) {
        this.apellidoMad = apellidoMad;
    }

}
