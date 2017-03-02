package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.ejb.CatalogoProfesionalRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.CatalogoProfesionalRenavi;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import java.io.IOException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "madreRenaviDController")
@ViewScoped
public class MadreRenaviDController implements Serializable {

    @EJB
    private ec.gob.digercic.renavi.ejb.MadreRenaviFacadeLocal ejbFacade;
    @EJB
    private ec.gob.digercic.renavi.ejb.NacionalidadRenaviFacadeLocal ejbFacadeNacionalidad;
    @EJB
    private ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal ejbFacadeNacidoVivoRenavi;
    @EJB
    private ec.gob.digercic.saureg.ejb.TblSauregUsuarioFacadeLocal ejbFacadeUsuario;
    private String cedula;
    private JasperPrint jasperPrint;
    /**/
    @EJB
    private CatalogoProfesionalRenaviFacadeLocal ejbFacadeCatalogoProfe;
    private List<CatalogoProfesionalRenavi> lstProfesion = new ArrayList<CatalogoProfesionalRenavi>();
    private List<ec.gob.digercic.renavi.ws.TblSauregRol> listaRol = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregRol>();
    private ec.gob.digercic.renavi.ws.TblSauregUsuario userLgn; //JJHF CAMBIO LOGIN
    private ec.gob.digercic.renavi.ws.TblSauregUsuario tblSauregUsuario; //JJHF CAMBIO LOGIN
    private Boolean flagRol = false;
    private String asistencia;

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public MadreRenaviDController() {
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
        return "/pages/contenedor/EditCont";
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**/
    @PostConstruct
    public void iniciar() {
        try {
            //Obtengo el usuario del sistema
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            userLgn = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");//JJHF CAMBIO LOGIN
            tblSauregUsuario = userLgn;//JJHF CAMBIO SAUREG
            //****************************************
            lstProfesion = ejbFacadeCatalogoProfe.findAll();
            listaRol = this.rolesUsuarios(userLgn.getNomUsuario(), String.valueOf(userLgn.getAgenciaInUser().getIdAgencia()));
            /**/
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**/

    public void eliminar() {
        try {
            //MadreRenavi madre = 
            String q = "update madre_renavi m set m.cedul_mad = '"
                    + cedula.substring(0, 8) + "AB'"
                    + " where m.cedul_mad = '";
            q = q + cedula + "'";
            System.out.println(" ---> " + q);
            int e = ejbFacade.executeNativeQuery(q);
            if (e > 0) {
                JsfUtil.displayMessage("Actualizado", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            } else {
                JsfUtil.displayMessage("No se actualiza", JsfUtil.INFO_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }

        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
        }
    }

    public void generaPDF() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            TblSauregUsuario userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
            NacidoVivoRenavi nv = (NacidoVivoRenavi) ejbFacadeNacidoVivoRenavi.consultarTablaSingleResult("NacidoVivoRenavi.findByCedulNacViv", "cedulNacViv", cedula);
            byte[] pdf;
            try {
                pdf = this.pdfSinFirma(nv, userLgn);
            } catch (Exception epdf) {
                pdf = null;
            }
            if (pdf != null) {
                //Seteo el pdf generado y edito el registro con el pdf generado
                try {
                    nv.setPdfSinFirmaNacViv(pdf);
                    ejbFacadeNacidoVivoRenavi.edit(nv);
                    JsfUtil.displayMessage("Se generó el pdf para el registro solicitado.", JsfUtil.ERROR_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                } catch (EntidadException ee2) {
                    ee2.printStackTrace();
                    JsfUtil.displayMessage(ee2.getMessage(), JsfUtil.ERROR_MESSAGE);
                    RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
                }
            } else {
                JsfUtil.displayMessage("No se pudo generar el pdf para el registro solicitado.", JsfUtil.ERROR_MESSAGE);
                RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
            }
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form_cont:mssgsBusqueda");
        }
    }

    public byte[] pdfSinFirma(NacidoVivoRenavi nv, TblSauregUsuario usuarioFirma) throws JRException, IOException, SQLException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            TblSauregUsuario userLgn = (TblSauregUsuario) httpSession.getAttribute("usuarioSesion");
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

    /*Cambio para identificar los roles de los usuarios  FWTM  25-02-2016*/
    private List<ec.gob.digercic.renavi.ws.TblSauregRol> rolesUsuarios(java.lang.String userI, String agencia) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        List<ec.gob.digercic.renavi.ws.TblSauregRol> lstUserRol;
        lstUserRol = port.getRolesByUsuarioByAgenciaBySistema(userI, agencia, "1");
        return lstUserRol;
    }
    /*25-02-2016*/

}
