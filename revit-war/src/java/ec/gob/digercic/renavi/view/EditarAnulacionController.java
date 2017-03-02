/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

/**/
import ec.gob.digercic.renavi.ejb.CatalogoProfesionalRenaviFacadeLocal;
/**/
import ec.gob.digercic.renavi.ejb.AnulacionRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.EstadoRegistroRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.LogAccesosRenaviFacadeLocal;
import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.AlfabetismoRenavi;
import ec.gob.digercic.renavi.entities.Anulacion;
import ec.gob.digercic.renavi.entities.CatalogoProfesionalRenavi;

import ec.gob.digercic.renavi.entities.EstadoRegistroRenavi;
import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.entities.NivelInstruccionRenavi;
import ec.gob.digercic.renavi.entities.PaisRenavi;
import ec.gob.digercic.renavi.entities.SexoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.CapturaIP;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.entities.TblSauregUbicacion;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import java.io.IOException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
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
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
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
@ManagedBean(name = "editarAnulacionController")
@ViewScoped
public class EditarAnulacionController implements Serializable {

    private NacidoVivoRenavi nacidoAEditar;
    @EJB
    private AnulacionRenaviFacadeLocal ejbAnulacion;
    @EJB
    private NacidoVivoRenaviFacadeLocal ejbNacidoVivo;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregUbicacionFacadeLocal ejbFacadeUbicacion;
    @EJB
    private ec.gob.digercic.renavi.ejb.TipoAreaRenaviFacadeLocal ejbFacadetipoArea;
    @EJB
    private ec.gob.digercic.renavi.ejb.NivelInstruccionRenaviFacadeLocal ejbFacadeNivelInstruccion;
    @EJB
    private EstadoRegistroRenaviFacadeLocal ejbEstRegistro;
    @EJB
    private LogAccesosRenaviFacadeLocal ejbLogs;
    @EJB
    private ec.gob.digercic.renavi.ejb.PaisRenaviFacadeLocal ejbFacadePais;
    /**/
    @EJB
    private CatalogoProfesionalRenaviFacadeLocal ejbFacadeCatalogoProfe;
    /**/

    private Anulacion anulacion;
    private List<TblSauregUbicacion> provincias;
    private String provincia;
    private List<TblSauregUbicacion> cantones;
    private String canton;
    private List<TblSauregUbicacion> parroquias;
    private String parroquia;
    private Short numParto;
    private Short numHijosvivos;
    private Short numHijosVivMuertos;
    private Short numHijosNacierMuertos;
    private Short hijosVivosSistema;
    private List<NivelInstruccionRenavi> nivelesInstruccion;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn;
    private JasperPrint jasperPrint;
    private Date fechaActual = new Date();
    private Date fechaInferior;
    private Date fechaSuperior;
    //para validación de fecha de nacimiento y sexo.
    private Date fechaAControlar;
    private SexoRenavi sexoAControlar;
    private List<PaisRenavi> listPais;
    /**/
    private Boolean flagRol = false;
    private String asistencia;
    /**/
    /**/
    private List<CatalogoProfesionalRenavi> lstProfesion = new ArrayList<CatalogoProfesionalRenavi>();
    private List<ec.gob.digercic.renavi.ws.TblSauregRol> listaRol = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregRol>();

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public Date getFechaAControlar() {
        return fechaAControlar;
    }

    public void setFechaAControlar(Date fechaAControlar) {
        this.fechaAControlar = fechaAControlar;
    }

    public EditarAnulacionController() {
    }

    @PostConstruct
    public void init() throws EntidadException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            setUserLgn((ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion"));
            ec.gob.digercic.renavi.entities.NacidoVivoRenavi nacido = ejbNacidoVivo.find(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idCedul")));
            String aux = nacido.getCedulNacViv();
            aux = aux.substring(0, 10);
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            ParametroConsulta param = new ParametroConsulta("cedulNacViv", aux);
            parametros.add(param);

            NacidoVivoRenavi objeto = (NacidoVivoRenavi) ejbNacidoVivo.consultarTablaSingleResult("NacidoVivoRenavi.findByCedulNacViv", parametros);
            nacidoAEditar = objeto;
            fechaInferior = restaDias(nacidoAEditar.getFechaNacimNacViv(), 5);
            fechaSuperior = sumaDias(nacidoAEditar.getFechaNacimNacViv(), 5);

            List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            ParametroConsulta par = new ParametroConsulta("id", Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idAnul")));
            params.add(par);
            anulacion = (Anulacion) getEjbAnulacion().consultarTablaSingleResult("Anulacion.findById", params);
            /**/
            lstProfesion = ejbFacadeCatalogoProfe.findAll();
            listaRol = this.rolesUsuarios(userLgn.getNomUsuario(), String.valueOf(userLgn.getAgenciaInUser().getIdAgencia()));
            /**/
            for (int i = 0; i < lstProfesion.size(); i++) {
                for (int j = 0; j < listaRol.size(); j++) {
                    if (listaRol.get(j).getDescripcion().toString().equals(lstProfesion.get(i).getNombreCatProf().toString())) {
                        flagRol = true;
                        if (lstProfesion.get(i).getNombreCatProf().toString().equals("DIGITADOR")) {
                            setAsistencia("MEDICO");
                        } else {
                            setAsistencia(lstProfesion.get(i).getNombreCatProf().toString());
                        };
                        break;
                    }
                    if (flagRol) {
                        break;
                    }
                }
            }
            /**/

            if (nacidoAEditar.getFkIdSabeLeerMad() != null) {
                obtenerSabeLeer(nacidoAEditar.getFkIdSabeLeerMad());
            }

            if (nacidoAEditar.getResidnProvdsMad() != null && nacidoAEditar.getResidnProvidMad() != null) {
                provincia = nacidoAEditar.getResidnProvidMad() + ".- " + nacidoAEditar.getResidnProvdsMad();
                cantones = new ArrayList<TblSauregUbicacion>();
                try {
                    List<ParametroConsulta> params1 = new ArrayList<ParametroConsulta>();
                    System.out.println("-----> " + nacidoAEditar.getResidnProvidMad());
                    params1.add(new ParametroConsulta("codExterno1", nacidoAEditar.getResidnProvidMad()));
                    params1.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                    cantones = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodExterno1", params1);
                    canton = nacidoAEditar.getResidnCantidMad() + ".- " + nacidoAEditar.getResidnCantdsMad();
                } catch (EntidadException ec) {
                    ec.printStackTrace();
                }
            }
            if (nacidoAEditar.getResidnCantdsMad() != null && nacidoAEditar.getResidnCantidMad() != null) {
                parroquias = new ArrayList<TblSauregUbicacion>();
                try {
                    List<ParametroConsulta> params2 = new ArrayList<ParametroConsulta>();
                    params2.add(new ParametroConsulta("codExterno1", nacidoAEditar.getResidnCantidMad()));
                    params2.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                    parroquias = ejbFacadeUbicacion.consultarTablaResultList("TblSauregUbicacion.findByIdGrupoCodExterno1", params2);
                    parroquia = nacidoAEditar.getResidnParridMad() + ".- " + nacidoAEditar.getResidnParrdsMad();
                } catch (EntidadException ec) {
                    ec.printStackTrace();
                }
            }
            try {
                listPais = ejbFacadePais.findAll();
            } catch (EntidadException ee) {
                ee.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorConsulta"), JsfUtil.INFO_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
        }

    }

    /**
     * Suma un número de dias definido a una fecha Date
     *
     * @param fecha
     * @param dias
     * @return
     */
    public static Date sumaDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DATE, dias);
        return cal.getTime();
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

    /**
     * Resta un número de dias definido a una fecha Date
     *
     * @param fecha
     * @param dias
     * @return
     */
    public static Date restaDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DATE, -dias);
        return cal.getTime();
    }

    /*Método que cambia el formato de la fecha a dd/mm/YYYY*/
    public String formatoFecha(Date fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaConFormato = formatter.format(fecha);
        return fechaConFormato;
    }

    /*el presente método recupera las diversas opciones para la alternativa si sabe leer, dependiendo de la edad de la madre
     */
    public void obtenerSabeLeer(AlfabetismoRenavi item) {
        //Reseteo el nivel de instrucción
        nivelesInstruccion = null;
        nivelesInstruccion = new ArrayList<NivelInstruccionRenavi>();
        //Obtengo el valor seleccionado
        if (item != null) {
            if (item.getIdAlfb() == 2) {
                try {
                    NivelInstruccionRenavi instruccion = ejbFacadeNivelInstruccion.find(0);
                    nivelesInstruccion.add(instruccion);
                    nacidoAEditar.setFkIdNivelInstr(instruccion);
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            } else {
                try {
                    if (nacidoAEditar.getEdadMad() >= 10 && nacidoAEditar.getEdadMad() <= 11) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad10And11");
                    } else if (nacidoAEditar.getEdadMad() >= 12 && nacidoAEditar.getEdadMad() <= 14) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad12And14");
                    } else if (nacidoAEditar.getEdadMad() >= 15 && nacidoAEditar.getEdadMad() <= 16) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad15And16");
                    } else if (nacidoAEditar.getEdadMad() >= 17 && nacidoAEditar.getEdadMad() <= 22) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad17And22");
                    } else if (nacidoAEditar.getEdadMad() >= 23 && nacidoAEditar.getEdadMad() <= 49) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    } else if (nacidoAEditar.getEdadMad() >= 50) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    }
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    public void cambiarSabeLeer(ValueChangeEvent event) {
        //Reseteo el nivel de instraucción
        nivelesInstruccion = null;
        nivelesInstruccion = new ArrayList<NivelInstruccionRenavi>();
        nacidoAEditar.setFkIdNivelInstr(null);
        //Obtengo el valor seleccionado
        AlfabetismoRenavi item = (AlfabetismoRenavi) event.getNewValue();
        if (item != null) {
            if (item.getIdAlfb() == 2) {
                try {
                    NivelInstruccionRenavi instruccion = ejbFacadeNivelInstruccion.find(0);
                    nivelesInstruccion.add(instruccion);
                    nacidoAEditar.setFkIdNivelInstr(instruccion);
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            } else {
                try {
                    if (nacidoAEditar.getEdadMad() >= 10 && nacidoAEditar.getEdadMad() <= 11) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad10And11");
                    } else if (nacidoAEditar.getEdadMad() >= 12 && nacidoAEditar.getEdadMad() <= 14) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad12And14");
                    } else if (nacidoAEditar.getEdadMad() >= 15 && nacidoAEditar.getEdadMad() <= 16) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad15And16");
                    } else if (nacidoAEditar.getEdadMad() >= 17 && nacidoAEditar.getEdadMad() <= 22) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad17And22");
                    } else if (nacidoAEditar.getEdadMad() >= 23 && nacidoAEditar.getEdadMad() <= 49) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    } else if (nacidoAEditar.getEdadMad() >= 50) {
                        nivelesInstruccion = ejbFacadeNivelInstruccion.findByNamedQueryResultList("NivelInstruccionRenavi.findByEdad23And49");
                    }
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    public void cambiarProvincia(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Reseteo los combos de cantones y parroquias
            cantones = null;
            parroquias = null;
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            nacidoAEditar.setResidnProvidMad(value.substring(0, value.indexOf(".- ")));
            nacidoAEditar.setResidnProvdsMad(value.substring(value.indexOf(".-") + 3, value.length()));
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

    public void cambiarCanton(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Reseteo los combos de parroquias
            parroquias = null;
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            nacidoAEditar.setResidnCantidMad(value.substring(0, value.indexOf(".- ")));
            nacidoAEditar.setResidnCantdsMad(value.substring(value.indexOf(".-") + 3, value.length()));
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

    public void cambiarParroquia(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            //Asigno el valor seleccionado en combo
            String value = event.getNewValue().toString();
            nacidoAEditar.setResidnParridMad(value.substring(0, value.indexOf(".- ")));
            nacidoAEditar.setResidnParrdsMad(value.substring(value.indexOf(".-") + 3, value.length()));
            //Setear campoInecAreaMad
            nacidoAEditar.setCampoInecAreaMad(value.substring(0, value.indexOf(".- ")));
            //Seteo urbana o rural
            try {
                if (Integer.valueOf(value.substring(4, value.indexOf(".- "))) < 51) {
                    //Urbana
                    nacidoAEditar.setFkIdTipoAreaMad(ejbFacadetipoArea.find(Integer.valueOf("1")));
                } else {
                    //Rural
                    nacidoAEditar.setFkIdTipoAreaMad(ejbFacadetipoArea.find(Integer.valueOf("2")));
                }
            } catch (EntidadException ee) {
                ee.printStackTrace();
            }
            System.gc();
        }
    }

    public String cancelar() {
        return "BandejaEdicion";
    }

    public String editar() throws JRException, IOException, SQLException, EntidadException {
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
            log.setAccion("REVIT. ANULACION DE REGISTRO. MÉDICO EDITA REGISTRO");//accion realizada, con el siguiente en formato:                                                             //<sistema><accion_general><accion_especifica>
            CapturaIP ip = new CapturaIP();
            log.setIp(ip.obieneDireccionIP());
            getEjbLogs().create(log);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        System.out.println("Entra a editar");
        try {
            NacidoVivoRenavi nacVivoRecuperado = new NacidoVivoRenavi();
            nacVivoRecuperado = nacidoAEditar;
            if (nacVivoRecuperado.getFkIdProEmb().getIdProEmb() > 1) {
                List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
                parametros.add(new ParametroConsulta("numParto", nacVivoRecuperado.getNumPartoSistema()));
                parametros.add(new ParametroConsulta("idMad", nacVivoRecuperado.getFkCedulMad()));
                List<NacidoVivoRenavi> productosMultiples = ejbNacidoVivo.consultarTablaResultList("NacidoVivoRenavi.findProductosPartoMultiple", parametros);
                System.out.println("obtengo partos multiples");
                System.out.println(productosMultiples);
                for (NacidoVivoRenavi item : productosMultiples) {
                    if (item.getCedulNacViv().equals(nacVivoRecuperado.getCedulNacViv())) {
                        EstadoRegistroRenavi estReg;
                        estReg = ejbEstRegistro.find(8);
                        if (sexoAControlar != null) {
                            nacidoAEditar.setFkIdSexoNv(sexoAControlar);
                        }
                        if (fechaAControlar != null) {
                            nacidoAEditar.setFechaNacimNacViv(fechaAControlar);
                        }
                        nacidoAEditar.setFkIdEstReg(estReg);
                        nacidoAEditar.setFechaCreacionNacViv(new Date());
                        nacidoAEditar.setFechaActualizacionNacViv(new Date());
                        System.out.println("antes edit");
                        getEjbNacidoVivo().edit(nacidoAEditar);
                        System.out.println("primera edicion ok");
                        try {
                            byte[] pdfAnuladoModificado = pdfSinFirma(nacidoAEditar, userLgn);
                            nacidoAEditar.setPdfSinFirmaNacViv(pdfAnuladoModificado);
                            getEjbNacidoVivo().edit(nacidoAEditar);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("genera pdf");
                        List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                        params.add(new ParametroConsulta("cedNacViv", nacidoAEditar.getCedulNacViv()));  //este cambio hice
                        Anulacion anul = (Anulacion) getEjbAnulacion().consultarTablaSingleResult("Anulacion.findByCedNacViv", params);
                        System.out.println("anulacion  a editar:" + anul.getId());
                        anul.setEstAnulNacViv("4");
                        System.out.println("completado");
                        anul.setFechaFinalizacion(new Date());
                        getEjbAnulacion().edit(anul);
                    } else {
                        if (!(item.getCedulNacViv().contains("-1"))) {
                            EstadoRegistroRenavi estReg;
                            estReg = ejbEstRegistro.find(8);
                            item.setFkIdEstReg(estReg);
                            item.setFechaCreacionNacViv(new Date());
                            item.setFechaActualizacionNacViv(new Date());
                            System.out.println("antes edit");
                            getEjbNacidoVivo().edit(item);
                            System.out.println("primera edicion ok");
                            try {
                                byte[] pdfAnulado = pdfSinFirma(item, userLgn);
                                item.setPdfSinFirmaNacViv(pdfAnulado);
                                getEjbNacidoVivo().edit(item);
                            } catch (EntidadException e) {
                                e.printStackTrace();
                            }
                        }//fin if    
                    } //fin else
                }// fin for
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NacidoVivoRenaviActualizado"), JsfUtil.INFO_MESSAGE);
                return "BandejaEdicion";
            } else {
                try {
                    EstadoRegistroRenavi estReg;
                    estReg = ejbEstRegistro.find(8);
                    if (sexoAControlar != null) {
                        nacidoAEditar.setFkIdSexoNv(sexoAControlar);
                    }
                    if (fechaAControlar != null) {
                        nacidoAEditar.setFechaNacimNacViv(fechaAControlar);
                    }
                    nacidoAEditar.setFkIdEstReg(estReg);
                    nacidoAEditar.setFechaCreacionNacViv(new Date());
                    nacidoAEditar.setFechaActualizacionNacViv(new Date());
                    System.out.println("antes edit");
                    getEjbNacidoVivo().edit(nacidoAEditar);

                    System.out.println("primera edicion ok");
                    try {
                        byte[] pdfAnuladoModificado = pdfSinFirma(nacidoAEditar, userLgn);
                        nacidoAEditar.setPdfSinFirmaNacViv(pdfAnuladoModificado);
                        getEjbNacidoVivo().edit(nacidoAEditar);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("genera pdf");
                    List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
                    params.add(new ParametroConsulta("cedNacViv", nacidoAEditar.getCedulNacViv()));  //este cambio hice
                    Anulacion anul = (Anulacion) getEjbAnulacion().consultarTablaSingleResult("Anulacion.findByCedNacViv", params);
                    System.out.println("anulacion  a editar:" + anul.getId());
                    anul.setEstAnulNacViv("4");
                    System.out.println("completado");
                    anul.setFechaFinalizacion(new Date());
                    getEjbAnulacion().edit(anul);
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NacidoVivoRenaviActualizado"), JsfUtil.INFO_MESSAGE);
                    return "BandejaEdicion";
                } catch (EntidadException ee) {
                    ee.printStackTrace();
                    JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                    return "/index";
                }
            }
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "/index";
        }
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
     * @return the nacidoAEditar
     */
    public NacidoVivoRenavi getNacidoAEditar() {
        return nacidoAEditar;
    }

    /**
     * @param nacidoAEditar the nacidoAEditar to set
     */
    public void setNacidoAEditar(NacidoVivoRenavi nacidoAEditar) {
        this.nacidoAEditar = nacidoAEditar;
    }

    /**
     * @return the provincias
     */
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

    /**
     * @param provincias the provincias to set
     */
    public void setProvincias(List<TblSauregUbicacion> provincias) {
        this.provincias = provincias;
    }

    /**
     * @return the provincia
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * @param provincia the provincia to set
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * @return the cantones
     */
    public List<TblSauregUbicacion> getCantones() {
        return cantones;
    }

    /**
     * @param cantones the cantones to set
     */
    public void setCantones(List<TblSauregUbicacion> cantones) {
        this.cantones = cantones;
    }

    /**
     * @return the canton
     */
    public String getCanton() {
        return canton;
    }

    /**
     * @param canton the canton to set
     */
    public void setCanton(String canton) {
        this.canton = canton;
    }

    /**
     * @return the parroquias
     */
    public List<TblSauregUbicacion> getParroquias() {
        return parroquias;
    }

    /**
     * @param parroquias the parroquias to set
     */
    public void setParroquias(List<TblSauregUbicacion> parroquias) {
        this.parroquias = parroquias;
    }

    /**
     * @return the parroquia
     */
    public String getParroquia() {
        return parroquia;
    }

    /**
     * @param parroquia the parroquia to set
     */
    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    /**
     * @return the numParto
     */
    public Short getNumParto() {
        return numParto;
    }

    /**
     * @param numParto the numParto to set
     */
    public void setNumParto(Short numParto) {
        this.numParto = numParto;
    }

    /**
     * @return the numHijosvivos
     */
    public Short getNumHijosvivos() {
        return numHijosvivos;
    }

    /**
     * @param numHijosvivos the numHijosvivos to set
     */
    public void setNumHijosvivos(Short numHijosvivos) {
        this.numHijosvivos = numHijosvivos;
    }

    /**
     * @return the numHijosVivMuertos
     */
    public Short getNumHijosVivMuertos() {
        return numHijosVivMuertos;
    }

    /**
     * @param numHijosVivMuertos the numHijosVivMuertos to set
     */
    public void setNumHijosVivMuertos(Short numHijosVivMuertos) {
        this.numHijosVivMuertos = numHijosVivMuertos;
    }

    /**
     * @return the numHijosNacierMuertos
     */
    public Short getNumHijosNacierMuertos() {
        return numHijosNacierMuertos;
    }

    /**
     * @param numHijosNacierMuertos the numHijosNacierMuertos to set
     */
    public void setNumHijosNacierMuertos(Short numHijosNacierMuertos) {
        this.numHijosNacierMuertos = numHijosNacierMuertos;
    }

    /**
     * @return the hijosVivosSistema
     */
    public Short getHijosVivosSistema() {
        return hijosVivosSistema;
    }

    /**
     * @param hijosVivosSistema the hijosVivosSistema to set
     */
    public void setHijosVivosSistema(Short hijosVivosSistema) {
        this.hijosVivosSistema = hijosVivosSistema;
    }

    /**
     * @return the anulacion
     */
    public Anulacion getAnulacion() {
        return anulacion;
    }

    /**
     * @param anulacion the anulacion to set
     */
    public void setAnulacion(Anulacion anulacion) {
        this.anulacion = anulacion;
    }

    /**
     * @return the nivelesInstruccion
     */
    public List<NivelInstruccionRenavi> getNivelesInstruccion() {
        if (nivelesInstruccion == null) {
            nivelesInstruccion = new ArrayList<NivelInstruccionRenavi>();
        }
        return nivelesInstruccion;
    }

    /**
     * @param nivelesInstruccion the nivelesInstruccion to set
     */
    public void setNivelesInstruccion(List<NivelInstruccionRenavi> nivelesInstruccion) {
        this.nivelesInstruccion = nivelesInstruccion;
    }

    /**
     * @return the ejbAnulacion
     */
    public AnulacionRenaviFacadeLocal getEjbAnulacion() {
        return ejbAnulacion;
    }

    /**
     * @param ejbAnulacion the ejbAnulacion to set
     */
    public void setEjbAnulacion(AnulacionRenaviFacadeLocal ejbAnulacion) {
        this.ejbAnulacion = ejbAnulacion;
    }

    public List<PaisRenavi> getListPais() {
        if (listPais == null) {
            listPais = new ArrayList<PaisRenavi>();
        }
        return listPais;
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
                nacidoAEditar.setProviRcdscNacViv(null);
                nacidoAEditar.setProviRcidNacViv(null);
                provincia = null;
                nacidoAEditar.setCantnRcdscNacViv(null);
                nacidoAEditar.setCantnRcidNacViv(null);
                canton = null;
                nacidoAEditar.setPaarqRcdscNacViv(null);
                nacidoAEditar.setParrqRcidNacViv(null);
                parroquia = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] pdfSinFirma_old(Long id, TblSauregUsuario usuarioFirma) throws JRException, IOException, SQLException {
        try {
            //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

            //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.24.16.38:1521:INTDB", "revit", "revit");
            //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.91.16.45:1521:intdb", "revit", "revit2015$");
            //  Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.24.16.38:1521:INTDB", "REVIT", "revit");
            //  Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.91.16.45:1521:intdb", "revit", "revit2015$");
            Connection conn = JsfUtil.getConecction();//DFJ
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("id", id);
            parametros.put("institucion", getUserLgn().getAgenciaInUser().getNomAgencia().toString());
            parametros.put("usuarioLogeado", getUserLgn().getNomUsuario().toString());
            parametros.put("lugar", getUserLgn().getAgenciaInUser().getIdInstituc().getNomInstituc().toString());
            parametros.put("nombre", getUserLgn().getAgenciaInUser().getNomAgencia().toString());
            parametros.put("provincia", getUserLgn().getAgenciaInUser().getIdProvincia().getDerscripcion().toString());
            parametros.put("canton", getUserLgn().getAgenciaInUser().getIdCanton().getDerscripcion().toString());
            parametros.put("parroquia", getUserLgn().getAgenciaInUser().getIdParroquia().getDerscripcion().toString());
            parametros.put("direccion", getUserLgn().getAgenciaInUser().getDireccion().toString());
            parametros.put("asistidoPor", getAsistencia());
            parametros.put("telefono", getUserLgn().getAgenciaInUser().getTelefono() == null ? "" : getUserLgn().getAgenciaInUser().getTelefono().toString());
            parametros.put("nombreFirma", usuarioFirma.getNombre() + " " + usuarioFirma.getApellido());
            parametros.put("telefonoFirma", usuarioFirma.getAgenciaInUser().getTelefonoUsuario() == null ? "" : usuarioFirma.getAgenciaInUser().getTelefonoUsuario().toString());
            parametros.put("cedula", usuarioFirma.getNomUsuario().toString());
            parametros.put("ciud_loc", usuarioFirma.getAgenciaInUser().getLocalidad() == null ? "" : usuarioFirma.getAgenciaInUser().getLocalidad().toString());
            parametros.put("codMSP", getUserLgn().getAgenciaInUser().getCodMsp());
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users.jasper");
            // String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69.jasper");
            setJasperPrint(JasperFillManager.fillReport(reportPath, parametros, conn));
            byte[] pdf = JasperExportManager.exportReportToPdf(getJasperPrint());
            return pdf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users.jasper");
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(items);
            jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
            byte[] pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdf1;
        } catch (Exception e) {
            return null;
        }
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

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    /**
     * @param jasperPrint the jasperPrint to set
     */
    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    /**
     * @return the fechaActual
     */
    public Date getFechaActual() {
        return fechaActual;
    }

    /**
     * @param fechaActual the fechaActual to set
     */
    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Date getFechaInferior() {
        return fechaInferior;
    }

    /**
     * @param fechaInferior the fechaActual to set
     */
    public void setFechaInferior(Date fechaInferior) {
        this.fechaInferior = fechaInferior;
    }

    public Date getFechaSuperior() {
        return fechaSuperior;
    }

    public void setFechaSuperior(Date fechaSuperior) {
        this.fechaSuperior = fechaSuperior;
    }

    /**
     * @return the sexo
     */
    public SexoRenavi getSexoAControlar() {
        return sexoAControlar;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexoAControlar(SexoRenavi sexoAControlar) {
        this.sexoAControlar = sexoAControlar;
    }

    /**
     * @return the ejbLogs
     */
    public LogAccesosRenaviFacadeLocal getEjbLogs() {
        return ejbLogs;
    }

    /**
     * @param ejbLogs the ejbLogs to set
     */
    public void setEjbLogs(LogAccesosRenaviFacadeLocal ejbLogs) {
        this.ejbLogs = ejbLogs;
    }
}
