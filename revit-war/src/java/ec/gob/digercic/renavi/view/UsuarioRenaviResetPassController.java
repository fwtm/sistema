package ec.gob.digercic.renavi.view;

import botdetect.CodeStyle;
import botdetect.HelpLinkMode;
import botdetect.web.jsf.JsfCaptcha;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.EnvioMail;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.entities.TblSauregConfig;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "usuarioRenaviResetPassController")
@ViewScoped
public class UsuarioRenaviResetPassController implements Serializable {

    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregUsuarioFacadeLocal ejbFacadeUsuario;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregConfigFacadeLocal ejbConfigFacade;
    private String username;
    private String email;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userReset;
    //---
    private String captchaCode;
    private JsfCaptcha captcha;
    private boolean correctLabelVisible, incorrectLabelVisible;

    public UsuarioRenaviResetPassController() {
        captcha = new JsfCaptcha();
        captcha.setCodeLength(4);
        //BlackOverlap
        //captcha.setImageStyles("ancientMosaic, overlap2");
        captcha.setImageStyles("BlackOverlap");
        captcha.setCodeStyle(CodeStyle.ALPHANUMERIC);
        captcha.setHelpLinkEnabled(false);
        captcha.setHelpLinkMode(HelpLinkMode.IMAGE);
        captcha.setOmitStylesheet(true);
        // to set single image style you can use enum value:
        // captcha.setImageStyle(ImageStyle.Darts);
        captcha.setSoundEnabled(false);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ec.gob.digercic.renavi.ws.TblSauregUsuario getUserReset() {
        return userReset;
    }

    /**
     * Método para buscar el usuario al que se debe resetear la contraseña.
     *
     * @return
     */
    public void buscarUserResetPass() {
        try {
            //boolean isHuman = captcha.validate(captchaCode);
            //System.out.println("---> isHuman " + isHuman);
            //if(isHuman){
            //userReset = ejbFacadeUsuario.getUsuarioByUsernameAndMail(username, email);
            
            
            
            userReset=this.UsuarioByUsernameAndMail(username, email);
            if (userReset != null) {
                RequestContext.getCurrentInstance().execute("PF('resetPass').show()");
                RequestContext.getCurrentInstance().update("emailUser");
            } else {
                JsfUtil.displayMessage("No existe resultado para la búsqueda realizada.", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("frm_lvdCntrsn:messages");
            }
            //}else{
            //JsfUtil.displayMessage("El texto ingresado no corresponde a la imagen.", JsfUtil.ERROR_MESSAGE);
            //RequestContext.getCurrentInstance().update("frm_lvdCntrsn:messages");
            //}

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.displayMessage("Existen inconvenientes al realizar la búsqueda del usuario. Contáctese con el administrador del sistema.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("frm_lvdCntrsn:messages");
        }
    }

    //JJHF CAMBIO LOGIN
    private ec.gob.digercic.renavi.ws.TblSauregUsuario reseteoClave(java.lang.String nombre, java.lang.String usuario, java.lang.String idsistema, java.lang.String email) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario newuser = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
        newuser = port.enviarMailResetPass(nombre, usuario, idsistema, email);
        return newuser;

    }
 //JJHF CAMBIO LOGIN

    //JJHF CAMBIO LOGIN
    private ec.gob.digercic.renavi.ws.TblSauregUsuario UsuarioByUsernameAndMail(java.lang.String user, java.lang.String mail) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario newuser = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
        newuser = port.getUsuarioByUsernameAndMail(user, email);
        return newuser;

    }
    //JJHF CAMBIO LOGIN

    /**
     * Método para resetear la contraseña de un usuario. la contraseña se genera
     * de forma aleatoria entre numeros y letras.
     *
     * @return
     */
    public void resetearContrasenia() {
        try {
            
            ec.gob.digercic.renavi.ws.TblSauregUsuario userfornewpass = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
            ec.gob.digercic.renavi.ws.TblSauregUsuario busquedauser = new ec.gob.digercic.renavi.ws.TblSauregUsuario();
            
            busquedauser=this.UsuarioByUsernameAndMail(username, email);
            
            if(busquedauser.equals(null)){                
                JsfUtil.displayMessage("No se pudo realizar el proceso de reseteo de clave.", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("frm_lvdCntrsn:messages");     
                
            }else{
                userfornewpass=this.reseteoClave(busquedauser.getNombre(), username, "1", email);
                
                if(userfornewpass.equals(null)){
                 JsfUtil.displayMessage("No se pudo realizar el proceso de reseteo de clave.", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("frm_lvdCntrsn:messages");     
                }else{
                    JsfUtil.displayMessage("Se realizó con exito el proceso de reseteo de clave.", JsfUtil.WARN_MESSAGE);
                RequestContext.getCurrentInstance().update("frm_lvdCntrsn:messages");   
                }
            }
            
         

          //  this.reseteoClave(email, mailUser, username, email);
            //JJHF CAMBIO LOG IN
//            String password = Integer.toString((int) (Math.random() * Integer.MAX_VALUE), 24);
//            userReset.setContrasenia(ec.gob.digercic.util.SHAHashing.encripta(password));
//            userReset.setStatus("S");
//            ejbFacadeUsuario.edit(userReset);
//            List<ParametroConsulta> configParams = new ArrayList<ParametroConsulta>();
//            configParams.add(new ParametroConsulta("codConfig", "MAIL.SMTP.HOST"));
//            configParams.add(new ParametroConsulta("codInstituc", 1L));
//            String mailHost = ((TblSauregConfig) ejbConfigFacade.consultarTablaSingleResult("TblSauregConfig.findByCodConfig", configParams)).getValorTexto();
//
//            configParams.clear();
//            configParams.add(new ParametroConsulta("codConfig", "MAIL.SMTP.PORT"));
//            configParams.add(new ParametroConsulta("codInstituc", 1L));
//            String mailPort = ((TblSauregConfig) ejbConfigFacade.consultarTablaSingleResult("TblSauregConfig.findByCodConfig", configParams)).getValorTexto();
//
//            configParams.clear();
//            configParams.add(new ParametroConsulta("codConfig", "MAIL.USER"));
//            configParams.add(new ParametroConsulta("codInstituc", 1L));
//            String mailUser = ((TblSauregConfig) ejbConfigFacade.consultarTablaSingleResult("TblSauregConfig.findByCodConfig", configParams)).getValorTexto();
//
//            configParams.clear();
//            configParams.add(new ParametroConsulta("codConfig", "MAIL.PASSWORD"));
//            configParams.add(new ParametroConsulta("codInstituc", 1L));
//            String mailPassword = ((TblSauregConfig) ejbConfigFacade.consultarTablaSingleResult("TblSauregConfig.findByCodConfig", configParams)).getValorTexto();
//
//            configParams.clear();
//            configParams.add(new ParametroConsulta("codConfig", "MAIL.FROM"));
//            configParams.add(new ParametroConsulta("codInstituc", 1L));
//            String mailFrom = ((TblSauregConfig) ejbConfigFacade.consultarTablaSingleResult("TblSauregConfig.findByCodConfig", configParams)).getValorTexto();
//            
//            boolean rc = EnvioMail.enviarEmail((userReset.getApellido() + " " + userReset.getNombre()), 
//                    userReset.getNomUsuario(), email, password, mailHost, mailPort, mailUser, 
//                    mailPassword, mailFrom);
//            if(rc){
//                RequestContext.getCurrentInstance().execute("PF('resetPassConfirm').show()");
//            }else{
//                JsfUtil.displayMessage("No se pudo realizar el proceso de reseteo de contraseña.", JsfUtil.ERROR_MESSAGE);
//                RequestContext.getCurrentInstance().update("frm_lvdCntrsn:messages");
//            }
            //JJHF CAMBIO LOG IN
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.displayMessage("Existio inconvenientes al resetear su clave. Contáctese con el administrador del sistema.", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("frm_lvdCntrsn:messages");
        }
    }

    //-----------------------------
    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public JsfCaptcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(JsfCaptcha captcha) {
        this.captcha = captcha;
    }

    public boolean isCorrectLabelVisible() {
        return correctLabelVisible;
    }

    public boolean isIncorrectLabelVisible() {
        return incorrectLabelVisible;
    }

    public void validate() {
        // validate the Captcha to check we're not dealing with a bot
        boolean isHuman = captcha.validate(captchaCode);
        if (isHuman) {
            correctLabelVisible = true;
            incorrectLabelVisible = false;
            System.out.println("----> es correcta");
        } else {
            correctLabelVisible = false;
            incorrectLabelVisible = true;
            System.out.println("----> es incorrecta");
        }
        this.captchaCode = "";
    }
}
