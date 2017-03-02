/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.util;

//import ec.gob.registrocivil.renavi.modelo.Rol;
//import ec.gob.registrocivil.renavi.modelo.Usuario;
import ec.gob.digercic.saureg.entities.TblSauregCompRol;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author santiago.tapia
 */
public class AuthorizationListener implements PhaseListener {

    String msg;

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        FacesContext context = FacesContext.getCurrentInstance();
        String currentPage = facesContext.getViewRoot().getViewId();
        NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
        boolean isLoginPage = facesContext.getViewRoot().getViewId().lastIndexOf("login") > -1 ? true : false;
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        Object currentUser = session.getAttribute("username");

        if(!isLoginPage && (currentUser == null) && (currentPage.equals("/pages/usuarioRenavi/OlvidoContrasenia.xhtml"))){
            System.out.println("Estado: Entro al reseteo de su contraseña.");
        }else if(!isLoginPage && (currentUser == null) && (currentPage.equals("/consultaCiudadana.xhtml"))){
            System.out.println("Estado: Entro a la consulta ciudadana.");
        }else if (!isLoginPage && (currentUser == null /*|| currentUser == ""*/)) {
            nh.handleNavigation(facesContext, null, "/login?faces-redirect=true");
            System.out.println("Estado: NO está logueado y pretende acceder a recursos privados saltando el login.");

        } else if (isLoginPage && currentUser != null) {
            nh.handleNavigation(facesContext, null, "/index?faces-redirect=true");
            System.out.println("Estado: Logueado pero quiere regresar al login.");

        }

//        try {
//            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//            HttpSession httpSession = request.getSession(false);
//            ec.gob.digercic.renavi.ws.TblSauregUsuario usuarioLoggin = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
//            List<ec.gob.digercic.renavi.ws.TblSauregCompRol> rolesUsuario = usuarioLoggin.getTblSauregCompRolList();
//            boolean Condicion = false;            
//            //Digitador
//            if(currentPage.contains("/pages/contenedor/") 
//                    || currentPage.equals("/pages/madreRenavi/Find.xhtml")){
//                for(ec.gob.digercic.renavi.ws.TblSauregCompRol rolDig : rolesUsuario){
//                    if(rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("1"))){
//                        Condicion = true;
//                        break;
//                    }
//                }
//                if(Condicion == false){
//                    nh.handleNavigation(facesContext, null, "/index?faces-redirect=true");
//                }
//            }//Firmador token y certificado
//            else if(currentPage.contains("/pages/firmaElectronica/")){
//                for(ec.gob.digercic.renavi.ws.TblSauregCompRol rolDig : rolesUsuario){
//                    if(rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("14"))
//                            || rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("16"))){
//                        Condicion = true;
//                        break;
//                    }
//                }
//                if(Condicion == false){
//                    nh.handleNavigation(facesContext, null, "/index?faces-redirect=true");
//                }
//            }
//            //Anulaciones supervisor y digitador
//            else if(currentPage.startsWith("/pages/anulacionRenavi/")){
//                for(ec.gob.digercic.renavi.ws.TblSauregCompRol rolDig : rolesUsuario){
//                    if(rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("1"))
//                           //|| rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("86"))){ //produccion
//                           || rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("46"))){ //pruebas
//                            Condicion = true;
//                        break;
//                    }
//                }
//                if(Condicion == false){
//                    nh.handleNavigation(facesContext, null, "/index?faces-redirect=true");
//                }
//            }
//            //Reporte reg civil, intitucion, agencia, medico
//            else if(currentPage.startsWith("/pages/reporteRenavi/")){
//                for(ec.gob.digercic.renavi.ws.TblSauregCompRol rolDig : rolesUsuario){
//                    if(rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("20"))
//                            || rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("21"))
//                            || rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("22"))
//                            || rolDig.getTblSauregRol().getIdRol().equals(Long.parseLong("25"))){
//                        Condicion = true;
//                        break;
//                    }
//                }
//                if(Condicion == false){
//                    nh.handleNavigation(facesContext, null, "/index?faces-redirect=true");
//                }
//            }//todos los casos extras
//            else{
//                if(currentPage.startsWith("/pages/alfabetismoRenavi/")
//                        || currentPage.startsWith("/pages/estadoCivilRenavi/")
//                        || currentPage.startsWith("/pages/estadoFirmaRenavi/")
//                        || currentPage.startsWith("/pages/estadoRegistroRenavi/")
//                        || currentPage.startsWith("/pages/identificacionEtnicaRenavi/")
//                        || currentPage.equals("/pages/madreRenavi/CreateM.xhtml")
//                        || currentPage.equals("/pages/madreRenavi/EditM.xhtml")
//                        || currentPage.startsWith("/pages/nacidoVivoRenavi/")
//                        || currentPage.startsWith("/pages/nacionalidadRenavi/")
//                        || currentPage.startsWith("/pages/nivelInstruccionRenavi/")
//                        || currentPage.startsWith("/pages/padreRenavi/")
//                        || currentPage.startsWith("/pages/paisRenavi/")
//                        || currentPage.startsWith("/pages/productoEmbarazoRenavi/")
//                        || currentPage.startsWith("/pages/sexoRenavi/")
//                        || currentPage.startsWith("/pages/tipoAreaRenavi/")
//                        || currentPage.startsWith("/pages/tipoAsistenciaRenavi/")
//                        || currentPage.startsWith("/pages/tipoInscripcionRenavi/")
//                        || currentPage.startsWith("/pages/tipoPartoRenavi/")
//                        || currentPage.equals("/pages/usuarioRenavi/CreateD.xhtml")
//                        || currentPage.equals("/pages/usuarioRenavi/EditD.xhtml")){
//                    nh.handleNavigation(facesContext, null, "/index?faces-redirect=true");
//                }
//            }
//        } catch (NullPointerException e) {
//            //e.printStackTrace();
//            System.out.println("Exception of Listener");
//        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
