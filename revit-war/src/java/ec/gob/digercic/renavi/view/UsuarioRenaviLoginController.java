package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.view.util.SHAHashing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
//import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jfree.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "usuarioRenaviLoginController")
@SessionScoped
public class UsuarioRenaviLoginController implements Serializable {

    private String username;
    private String password;
    private String nuevoPassword;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLogin;
    private Long idAgencia;
    /*Cambio para Desplegar leyenda en la pantalla inicio*/
    private String identidadAgencias;
    private List<ec.gob.digercic.renavi.ws.TblSauregCompRol> roles;
    private List<ec.gob.digercic.renavi.ws.TblSauregEstrucSist> estructura;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario usu;
    private List<ec.gob.digercic.renavi.ws.TblSauregCompRol> ListEstructura;
    
    public UsuarioRenaviLoginController() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNuevoPassword() {
        return nuevoPassword;
    }

    public void setNuevoPassword(String nuevoPassword) {
        this.nuevoPassword = nuevoPassword;
    }

    public ec.gob.digercic.renavi.ws.TblSauregUsuario getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(ec.gob.digercic.renavi.ws.TblSauregUsuario userLogin) {
        this.userLogin = userLogin;
    }

    public Long getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(Long idAgencia) {
        this.idAgencia = idAgencia;
    }
    
    public ec.gob.digercic.renavi.ws.TblSauregUsuario getUsu() {
        return usu;
    }

    public void setUsu(ec.gob.digercic.renavi.ws.TblSauregUsuario usu) {
        this.usu = usu;
    }

    public List<ec.gob.digercic.renavi.ws.TblSauregCompRol> getListEstructura() {
        return ListEstructura;
    }

    public void setListEstructura(List<ec.gob.digercic.renavi.ws.TblSauregCompRol> ListEstructura) {
        this.ListEstructura = ListEstructura;
    }

    public List<ec.gob.digercic.renavi.ws.TblSauregCompRol> getRoles() {
        return roles;
    }

    public void setRoles(List<ec.gob.digercic.renavi.ws.TblSauregCompRol> roles) {
        this.roles = roles;
    }

    public List<ec.gob.digercic.renavi.ws.TblSauregEstrucSist> getEstructura() {
        return estructura;
    }

    public void setEstructura(List<ec.gob.digercic.renavi.ws.TblSauregEstrucSist> estructura) {
        this.estructura = estructura;
    }

    /*Cambio para Desplegar leyenda en la pantalla inicio*/
    public Boolean mensajeDispaly() {
        String ruta = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        if (ruta.equals("/index.xhtml")) {
            return true;
        } else {
            return false;
        }
    }

//JJHF CAMBIO LOG IN

    private List<ec.gob.digercic.renavi.ws.TblSauregCompRol> cambioEstructura(java.lang.String username, java.lang.String idAgencia, java.lang.String idSistema) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        List<ec.gob.digercic.renavi.ws.TblSauregCompRol> compRol = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregCompRol>();
        compRol = port.getCompRolUsuario(username, idAgencia, idSistema);
        return compRol;

    }
//JJHF CAMBIO LOG IN

    public void cambiarAgencia(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            for (ec.gob.digercic.renavi.ws.TblSauregAgencia item : usu.getListTblSauregAgencia()) {//JJHF CAMBIO LOGIN
                if (item.getIdAgencia().equals((Long) event.getNewValue())) {
                    usu.setAgenciaInUser(item);//JJHF CAMBIO LOGIN
                    idAgencia = item.getIdAgencia();
                    //usu.setTblSauregCompRolList(this.cambioEstructura(username, idAgencia.toString(), "1"));//JJHF CAMBIO LOGIN
                    ListEstructura = this.cambioEstructura(username, idAgencia.toString(), "1");//JJHF CAMBIO LOGIN
                    //INICIO DFJ
                    //Ordenar la Lista del menu por el campo OrdenEstruct
                    Collections.sort(ListEstructura, new Comparator() {
                        public int compare(Object objOrigen, Object objOrden) {

                            return ((ec.gob.digercic.renavi.ws.TblSauregCompRol) objOrigen).getTblSauregEstrucSist().getOrdenEstruct()
                                    .compareTo(((ec.gob.digercic.renavi.ws.TblSauregCompRol) objOrden).getTblSauregEstrucSist().getOrdenEstruct());
                        }
                    });
                    //FIN DFJ
                    RequestContext.getCurrentInstance().update("frm_agencia_select");
                    RequestContext.getCurrentInstance().update("temprincipalMenu");
                    try {
                        FacesContext contex = FacesContext.getCurrentInstance();
                        contex.getExternalContext().redirect(contex.getExternalContext().getRequestContextPath() + "/index.jsf");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
//JJHF CAMBIO LOG IN

    private ec.gob.digercic.renavi.ws.TblSauregUsuario busquedaUsuario(java.lang.String username, java.lang.String password) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario usuario = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
        usuario = port.getUsuario(username, password, "1");
        return usuario;

    }
//JJHF CAMBIO LOG IN

    /**
     * Método para realizar la autenticación del usuario en el sistema
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public void login() {
        try {

            usu = this.busquedaUsuario(username, password);

            //1 = Sistema revit
            //userLogin = ejbFacadeUsuario.getUsuario(username, password, "1", 1L);
            if (usu != null) {
                if (usu.getStatus().equals("N")) {

                    List<ec.gob.digercic.renavi.ws.TblSauregAgencia> agencia = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregAgencia>();

                    agencia = usu.getListTblSauregAgencia();
                    ListEstructura = usu.getTblSauregCompRolList();

                    //INICIO DFJ
                    //Ordenar la Lista del menu por el campo OrdenEstruct
                    Collections.sort(ListEstructura, new Comparator() {
                        public int compare(Object objOrigen, Object objOrden) {

                            return ((ec.gob.digercic.renavi.ws.TblSauregCompRol) objOrigen).getTblSauregEstrucSist().getOrdenEstruct()
                                    .compareTo(((ec.gob.digercic.renavi.ws.TblSauregCompRol) objOrden).getTblSauregEstrucSist().getOrdenEstruct());
                        }
                    });
                    //FIN DFJ

                    idAgencia = usu.getAgenciaInUser().getIdAgencia();
                    roles = usu.getTblSauregCompRolList();
                    estructura = usu.getEstructSistList();

                    //AÑADO EL OBJETO HALLADO EN LA SESION
                    FacesContext context = FacesContext.getCurrentInstance();
                    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                    HttpSession httpSession = request.getSession(false);
                    httpSession.setAttribute("usuarioSesion", usu);

                    httpSession.setAttribute("username", username);
                    identidadAgencias = username;
                    try {
                        password = null;
                        FacesContext contex = FacesContext.getCurrentInstance();
                        contex.getExternalContext().redirect("index.jsf");
                    } catch (IOException ioe) {
                        password = null;
                        try {
                            FacesContext contex = FacesContext.getCurrentInstance();
                            contex.getExternalContext().redirect("loginError.jsf");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    RequestContext.getCurrentInstance().execute("PF('resetPass').show()");
                    RequestContext.getCurrentInstance().update("loginform:cntndrCmbPss");
                }
            } else {
                try {
                    password = null;
                    usu = null;
                    FacesContext contex = FacesContext.getCurrentInstance();
                    contex.getExternalContext().redirect("loginError.jsf");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ews) {
            try {
                password = null;
                usu = null;
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("loginError.jsf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para salir del sistema
     *
     * @return
     * @throws IOException
     */
    public String logOut() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        userLogin = null;
        return "/login?faces-redirect=true";
    }

    /**
     * Método para el cambio de contraseña del usuario
     *
     * @return
     */
    //JJHF CAMBIO LOG IN
    private String cambiarContraseña(java.lang.String username, java.lang.String password, java.lang.String newpassword) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        String cambioCon;
        cambioCon = port.cambiarContrasenia(username, password, newpassword);
        return cambioCon;

    }
//JJHF CAMBIO LOG IN    

    public void cambiarContraseniaUserLogin() {
        try {
            if (!password.equals(nuevoPassword)) {
                if (usu.getContrasenia().equals(SHAHashing.encripta(password))) {//JJHF CAMBIO LOG IN
                    if (!nuevoPassword.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\@#$%<>=¿?!¡.;:-_&/()^`´|*~]).{6,15})")) {
                        JsfUtil.displayMessage("La contraseña no cumple con los requisistos mínimos de seguridad.", JsfUtil.ERROR_MESSAGE);
                        RequestContext.getCurrentInstance().update("frm:msg");
                    } else {
                        String cambioCon = this.cambiarContraseña(usu.getNomUsuario(), password, nuevoPassword);//JJHF CAMBIO LOGIN
                        //  String cambioCon = ejbFacadeUsuario.cambiarContrasenia(userLogin, nuevoPassword, "N");//JJHF CAMBIO LOGIN
                        if (cambioCon.equals("ACTUALIZACION_CORRECTA")) {//JJHF CAMBIO LOGIN
                            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                            usu = null;
                            //username = null;
                            password = null;
                            nuevoPassword = null;
                            RequestContext.getCurrentInstance().execute("PF('cntrsnCmbd').show()");
                        } else {
                            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("UsuarioNoCambioContrasenia"), JsfUtil.ERROR_MESSAGE);
                            RequestContext.getCurrentInstance().update("frm:msg");
                        }
                    }
                } else {
                    JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ContraseniaActualNoCoincidenM"), JsfUtil.ERROR_MESSAGE);
                    RequestContext.getCurrentInstance().update("frm:msg");
                }
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ContraseniasCoincidenM"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("frm:msg");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage("No se puede realizar el proceso de cambio de contraseña.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("frm:msg");
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("../../login.jsf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para el cambio de contraseña del usuario, en el primer ingreso
     *
     * @return
     */
    public void cambiarContraseniaPrmrVz() {
        try {
            if (!usu.getContrasenia().equals(SHAHashing.encripta(nuevoPassword))) {
                if (!nuevoPassword.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\@#$%<>=¿?!¡.;:-_&/()^`´|*~]).{6,15})")) {
                    JsfUtil.displayMessage("La contraseña no cumple con los requisistos mínimos de seguridad.", JsfUtil.ERROR_MESSAGE);
                    RequestContext.getCurrentInstance().update("loginform:msg");
                } else {
                    String cambioCon = this.cambiarContraseña(usu.getNomUsuario(), password, nuevoPassword);//JJHF CAMBIO LOGIN
                    //String cambioCon = ejbFacadeUsuario.cambiarContrasenia(userLogin, nuevoPassword, "N");
                    if (cambioCon.equals("ACTUALIZACION_CORRECTA")) {//JJHF CAMBIO LOGIN
                        usu = null;
                        username = null;
                        password = null;
                        nuevoPassword = null;
                        RequestContext.getCurrentInstance().execute("PF('cntrsnCmbd').show()");
                    } else {
                        JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("UsuarioNoCambioContrasenia"), JsfUtil.ERROR_MESSAGE);
                        RequestContext.getCurrentInstance().update("loginform:msg");
                    }
                }
            } else {
                JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ContraseniasCoincidenM"), JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("loginform:msg");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage("No se puede realizar el proceso de cambio de contraseña.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("loginform:msgGeneral");
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("login.jsf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cambiarContraseniaPrmrVzCls() {
        nuevoPassword = null;
        RequestContext.getCurrentInstance().execute("PF('resetPass').hide()");
    }

}
