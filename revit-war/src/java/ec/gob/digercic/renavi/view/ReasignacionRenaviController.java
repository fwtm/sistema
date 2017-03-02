/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.LogsReasignacion;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ws.TblSauregUsuario;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
 * @author henry.aguilar
 */
@ManagedBean(name = "reasignacionRenaviController")
@ViewScoped
public class ReasignacionRenaviController implements Serializable {
    
    private ec.gob.digercic.renavi.ws.TblSauregUsuario usuSesion; //JJHF CAMBIO LOG IN
    private List<ec.gob.digercic.renavi.ws.TblSauregUsuario> listaProfesional;
    private String userProfCedula;
    private String userReasigCedula;
    private ec.gob.digercic.renavi.ws.TblSauregUsuario tblSauregUsuario;//JJHF CAMBIO LOG IN
    private List<NacidoVivoRenavi> listRegProf;
    private List<NacidoVivoRenavi> listRegProfFinal;
    private List<NacidoVivoRenavi> listSelectReg;
    private String usuEstablecimiento;
    private JasperPrint jasperPrint;
    private Integer bandera = 1;
    private String nombreU;
    private String apellidoU;
    private String registroR;
    private String asistencia;

    private List<ec.gob.digercic.renavi.ws.TblSauregUsuario> listProfAsig =
            new ArrayList<ec.gob.digercic.renavi.ws.TblSauregUsuario>();
    private List<ec.gob.digercic.renavi.ws.TblSauregRol> listaRol = new ArrayList<ec.gob.digercic.renavi.ws.TblSauregRol>();
    @EJB
    private ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal ejbNacVivFacade;
    @EJB
    private ec.gob.digercic.renavi.ejb.NacionalidadRenaviFacadeLocal ejbFacadeNacionalidad;
    @EJB
    private ec.gob.digercic.renavi.ejb.LogsReasignacionFacadeLocal ejbFacadeLogReasig;

    @PostConstruct
    public void init() throws EntidadException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        usuSesion = (ec.gob.digercic.renavi.ws.TblSauregUsuario) httpSession.getAttribute("usuarioSesion");//JJHF CAMBIO LOG IN
    }

    public ec.gob.digercic.renavi.ws.TblSauregUsuario getUsuSesion() {//JJHF CAMBIO LOG IN
        return usuSesion;//JJHF CAMBIO LOG IN
    }

    public void setUsuSesion(ec.gob.digercic.renavi.ws.TblSauregUsuario usuSesion) {//JJHF CAMBIO LOG IN
        this.usuSesion = usuSesion;//JJHF CAMBIO LOG IN
    }

    public String getUserProfCedula() {
        return userProfCedula;
    }

    public void setUserProfCedula(String userProfCedula) {
        this.userProfCedula = userProfCedula;
    }

    public String getUserReasigCedula() {
        return userReasigCedula;
    }

    public void setUserReasigCedula(String userReasigCedula) {
        this.userReasigCedula = userReasigCedula;
    }

    public List<NacidoVivoRenavi> getListRegProf() {
        return listRegProf;
    }

    public void setListRegProf(List<NacidoVivoRenavi> listRegProf) {
        this.listRegProf = listRegProf;
    }

    public List<NacidoVivoRenavi> getListSelectReg() {
        return listSelectReg;
    }

    public void setListSelectReg(List<NacidoVivoRenavi> listSelectReg) {

        this.listSelectReg = listSelectReg;

    }

    public String getUsuEstablecimiento() {
        return usuEstablecimiento;
    }

    public void setUsuEstablecimiento(String usuEstablecimiento) {
        this.usuEstablecimiento = usuEstablecimiento;
    }

    public Integer getBandera() {
        return bandera;
    }

    public void setBandera(Integer bandera) {
        this.bandera = bandera;
    }

    public String getNombreU() {
        return nombreU;
    }

    public void setNombreU(String nombreU) {
        this.nombreU = nombreU;
    }

    public String getApellidoU() {
        return apellidoU;
    }

    public void setApellidoU(String apellidoU) {
        this.apellidoU = apellidoU;
    }

    public String getRegistroR() {
        return registroR;
    }

    public void setRegistroR(String registroR) {
        this.registroR = registroR;
    }

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    //JJHF CAMBIO LOGIN
    private ec.gob.digercic.renavi.ws.TblSauregUsuario UsuarioByUserAndAgenciaMSP(java.lang.String user, java.lang.String codMsp) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        ec.gob.digercic.renavi.ws.TblSauregUsuario Usuario;
        Usuario = port.getUsuarioByUserAndAgenciaMSP(user, codMsp);
        return Usuario;

    }

    //JJHF CAMBIO LOGIN
    private List<ec.gob.digercic.renavi.ws.TblSauregUsuario> UsuarioBycodMspandRol(java.lang.String rol, java.lang.String codMsp) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        List<ec.gob.digercic.renavi.ws.TblSauregUsuario> Usuario;
        Usuario = port.getUsuariosPorSistemaPorAgenciaMspPorRol(codMsp, "1", rol);
        return Usuario;

    }

    //JJHF CAMBIO LOGIN
    public void cambiarTblSauregUsuarioList(ValueChangeEvent event) {
        String cedu = (String) event.getNewValue();
        this.setUsuEstablecimiento(usuSesion.getAgenciaInUser().getNomAgencia());
        tblSauregUsuario = this.UsuarioByUserAndAgenciaMSP(cedu, usuSesion.getAgenciaInUser().getCodMsp());//JJHF CAMBIO LOGIN
        listRegProf = new ArrayList<>();//DFJ
    }

    public ec.gob.digercic.renavi.ws.TblSauregUsuario getTblSauregUsuario() {
        return tblSauregUsuario;
    }

    public void setTblSauregUsuario(ec.gob.digercic.renavi.ws.TblSauregUsuario tblSauregUsuario) {
        this.tblSauregUsuario = tblSauregUsuario;
    }

    //JJHF CAMBIO LOGIN
    private List<ec.gob.digercic.renavi.ws.TblSauregUsuario> UsuariosByAgenciaMSP(java.lang.String codMsp) {// If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        List<ec.gob.digercic.renavi.ws.TblSauregUsuario> Usuarios;
        Usuarios = port.getUsuariosByAgenciaMSPAll(codMsp);
        return Usuarios;
    }

    //JJHF CAMBIO LOGIN
    public List<ec.gob.digercic.renavi.ws.TblSauregUsuario> getListProfAsig() {
        List<ec.gob.digercic.renavi.ws.TblSauregUsuario> lstMed = 
                new ArrayList<ec.gob.digercic.renavi.ws.TblSauregUsuario>();
        
        List<ec.gob.digercic.renavi.ws.TblSauregUsuario> lstSuper = 
                new ArrayList<ec.gob.digercic.renavi.ws.TblSauregUsuario>();
        
        try {
            lstSuper = this.UsuarioBycodMspandRol(" '" + JsfUtil.ROL_JEFE_SERVICIO.toString() + "', '" + JsfUtil.ROL_GERENTE_UNIDAD.toString() + "' ", usuSesion.getAgenciaInUser().getCodMsp());
            lstMed = this.UsuarioBycodMspandRol(" '" + JsfUtil.ROL_MEDICO.toString() + "', '" + JsfUtil.ROL_OBSTETRIZ.toString() +"' ", usuSesion.getAgenciaInUser().getCodMsp());
            
            if (lstSuper.size() > lstMed.size()) {
                for (ec.gob.digercic.renavi.ws.TblSauregUsuario item : lstSuper) {
                    for (int i = 0; i < lstMed.size(); i++) {
                        if(lstMed.get(i).getNomUsuario().equals(item.getNomUsuario())) {
                            listProfAsig.add(item);
                        }
                    }
                }
            } if (lstSuper.size() < lstMed.size()) {
                for (ec.gob.digercic.renavi.ws.TblSauregUsuario item : lstMed) {
                    for (int i = 0; i < lstSuper.size(); i++) {
                        if (lstSuper.get(i).getNomUsuario().equals(item.getNomUsuario())) {
                            listProfAsig.add(item);
                        }
                    }
                }
            } else {
                for (ec.gob.digercic.renavi.ws.TblSauregUsuario item : lstMed) {
                    for (int i = 0; i < lstSuper.size(); i++) {
                        if(item.getNomUsuario().equals(lstSuper.get(i).getNomUsuario())) {
                            listProfAsig.add(item);
                        }
                    }
                }
            }
            //DFJ ELIMINA USUARIOS INACTIVOS 
            for (Iterator<TblSauregUsuario> it = listProfAsig.iterator(); it.hasNext();) {
                TblSauregUsuario tbl = it.next();
                if (tbl.getStatus() != null && tbl.getStatus().equalsIgnoreCase("I")) {
                    it.remove();
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        if (listProfAsig.isEmpty() || listProfAsig == null) {
            JsfUtil.displayMessage("NO EXISTE PROFESIONALES PARA ASIGNAR", JsfUtil.ERROR_MESSAGE);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        return listProfAsig;
        
    }

    public void setListProfAsig(List<ec.gob.digercic.renavi.ws.TblSauregUsuario> listProfAsig) {
        this.listProfAsig = listProfAsig;
    }

    public void cargarListaRegProf() {
        List<ParametroConsulta> parametros = new ArrayList<>();
        parametros.add(new ParametroConsulta("fkUsuSaureg", userProfCedula));
        parametros.add(new ParametroConsulta("fkIdEstFir", JsfUtil.STAT_FIR_SIN));
        parametros.add(new ParametroConsulta("fkAgenciaSaureg", new Long(usuSesion.getAgenciaInUser().getCodMsp())));
        listRegProf = new ArrayList<NacidoVivoRenavi>();
        listRegProfFinal = new ArrayList<NacidoVivoRenavi>();
        try {
            listRegProf = ejbNacVivFacade.consultarTablaResultList("NacidoVivoRenavi.findByUsuSaureg", parametros);
        } catch (EntidadException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        if (!this.userProfCedula.equals(this.userReasigCedula)) {
            this.cargarListaRegProf();
        } else {
            this.setBandera(1);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ErrorAsignacion"), JsfUtil.FATAL_MESSAGE);
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public List<ec.gob.digercic.renavi.ws.TblSauregUsuario> getListaProfesional() {
        listaProfesional = this.UsuariosByAgenciaMSP(usuSesion.getAgenciaInUser().getCodMsp());

        //DFJ 0001281        
        //Filtrar solo los profesionales que tienen registros pendientes de firmar
        try {
            if (listaProfesional != null && listaProfesional.isEmpty() == false) {
                List<Object[]> lisPendientes = ejbNacVivFacade.executeNativeQueryListResult("select DISTINCT  FK_USU_SAUREG,FK_AGENCIA_SAUREG from nacido_vivo_renavi where fk_id_est_fir=1 and fk_id_est_reg in (2,3,4,7,8) "
                        + "AND FK_AGENCIA_SAUREG =" + usuSesion.getAgenciaInUser().getCodMsp());
                List<TblSauregUsuario> lisElimina = new ArrayList<>();
                for (TblSauregUsuario tblSauregUsuario1 : listaProfesional) {
                    boolean encontro = false;
                    for (Object[] objUsuario : lisPendientes) {
                        if (objUsuario[0] != null) {
                            if (tblSauregUsuario1.getNomUsuario().toString().equals(String.valueOf(objUsuario[0]))) {
                                encontro = true;
                                break;
                            }
                        }
                    }
                    if (!encontro) {
                        lisElimina.add(tblSauregUsuario1);
                    }
                }
                //Elimina los que no tengan registros pendientes
                for (TblSauregUsuario tblSauregUsuario1 : lisElimina) {
                    listaProfesional.remove(tblSauregUsuario1);
                }
            }
        } catch (EntidadException e) {
        }
        //FIN DFJ
        RequestContext.getCurrentInstance().update("form:nomProf");
        RequestContext.getCurrentInstance().update("form:contTab:cargoProf");
        return listaProfesional;
    }

    public void setListaProfesional(List<ec.gob.digercic.renavi.ws.TblSauregUsuario> listaProfesional) {
        this.listaProfesional = listaProfesional;
    }

    public void reasignar() {
        this.buscarNacidoVivos();
        Date horaReasignacion = new Date();
        for (NacidoVivoRenavi item : listSelectReg) {
            this.setBandera(0);
            int estReg = item.getFkIdEstReg().getIdEstReg();
            LogsReasignacion logReasig = new LogsReasignacion();
            logReasig.setIdNacViv(item.getIdNacViv());
            logReasig.setFechaAsigReg(horaReasignacion);
            logReasig.setFkAgenciaSaureg(new Long(usuSesion.getAgenciaInUser().getCodMsp()));
            logReasig.setUsuarioEjecAsig(usuSesion.getNomUsuario());
            logReasig.setUsuarioRegAnt(userProfCedula);
            logReasig.setUsuarioRegAsig(userReasigCedula);
            try {
                ejbFacadeLogReasig.create(logReasig);
            } catch (EntidadException ex) {
                ex.printStackTrace();
                Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, ex);
            }

            switch (estReg) {
                case 1:
                    item.setFkUsuSaureg(userReasigCedula);
                    item.setFkUsuFirmaSaureg(userReasigCedula);

                    try {
                        ejbNacVivFacade.edit(item);
                        JsfUtil.displayMessage("Registro asignado correctamente, favor revisar en la bandeja de edición.", JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("growl");
                    } catch (EntidadException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 2:
                    item.setFkUsuSaureg(userReasigCedula);
                    item.setFkUsuFirmaSaureg(userReasigCedula);

                    try {
                        ejbNacVivFacade.edit(item);
                        JsfUtil.displayMessage("Registro asignado correctamente, favor revisar en la bandeja de edición..", JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("growl");
                    } catch (EntidadException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 3:
                    item.setFkUsuSaureg(userReasigCedula);
                    item.setFkUsuFirmaSaureg(userReasigCedula);
                    try {
                        ejbNacVivFacade.edit(item);
                        JsfUtil.displayMessage("Registro asignado correctamente, favor revisar en la bandeja de edición..", JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("growl");
                    } catch (EntidadException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 4:
                    item.setFkUsuSaureg(userReasigCedula);
                    item.setFkUsuFirmaSaureg(userReasigCedula);
                    byte[] pdf;
                    try {
                        pdf = this.pdfSinFirma(item);
                    } catch (Exception epdf) {
                        pdf = null;
                    }
                    if (pdf != null) {
                        item.setPdfSinFirmaNacViv(pdf);
                    }
                    try {
                        ejbNacVivFacade.edit(item);
                        JsfUtil.displayMessage("Registro asignado correctamente, favor revisar en la bandeja de firmar o extemporáneos.", JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("growl");
                    } catch (EntidadException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 7:
                    item.setFkUsuSaureg(userReasigCedula);
                    item.setFkUsuFirmaSaureg(userReasigCedula);
                    try {
                        ejbNacVivFacade.edit(item);
                        JsfUtil.displayMessage("Registro asignado correctamente, favor revisar en la bandeja de edición.", JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("growl");
                    } catch (EntidadException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 8:
                    item.setFkUsuSaureg(userReasigCedula);
                    item.setFkUsuFirmaSaureg(userReasigCedula);
                    byte[] pdf1;
                    try {
                        pdf1 = this.pdfSinFirma(item);
                    } catch (Exception epdf) {
                        pdf1 = null;
                    }
                    if (pdf1 != null) {
                        item.setPdfSinFirmaNacViv(pdf1);
                    }
                    try {
                        ejbNacVivFacade.edit(item);
                        JsfUtil.displayMessage("Registro asignado correctamente, favor revisar en la bandeja de firmar o extemporáneos.", JsfUtil.INFO_MESSAGE);
                        RequestContext.getCurrentInstance().update("growl");
                    } catch (EntidadException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                default:
                    break;
            }
        }
        this.limpiar();
    }

    public byte[] pdfSinFirma(NacidoVivoRenavi nv) throws JRException, IOException, SQLException {
        try {
                tblSauregUsuario = this.UsuarioByUserAndAgenciaMSP(userReasigCedula, usuSesion.getAgenciaInUser().getCodMsp());
                listaRol = this.rolesUsuarios(userReasigCedula, String.valueOf(usuSesion.getAgenciaInUser().getIdAgencia()));
                for (int i = 0; i < listaRol.size(); i++) {
                    if (listaRol.get(i).getDescripcion().equals("MEDICO")
                            || listaRol.get(i).getDescripcion().equals("OBSTETRIZ")) {
                        setAsistencia(listaRol.get(i).getDescripcion());
                    }
                }
                if (tblSauregUsuario == null || listaRol == null) {
                  Logger.getLogger(ReasignacionRenaviController.class.getName()).log(Level.SEVERE, null, "NO SIRVE");
                  JsfUtil.displayMessage("Error al crear archivo.", JsfUtil.ERROR_MESSAGE);
                  RequestContext.getCurrentInstance().update("growl");
            }

            List<NacidoVivoRenavi> items = new ArrayList<NacidoVivoRenavi>();
            if (nv.getFkIdNacMad().getIdNac() == 2) {
                nv.setFkIdNacMad(ejbFacadeNacionalidad.find(2));
            }
            
            items.add(nv);
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("institucion", usuSesion.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("usuarioLogeado", userReasigCedula);
            parametros.put("lugar", usuSesion.getAgenciaInUser().getIdInstituc().getNomInstituc().toString());
            parametros.put("nombre", usuSesion.getAgenciaInUser().getNomAgencia().toString());
            parametros.put("provincia", usuSesion.getAgenciaInUser().getIdProvincia().getDerscripcion().toString());
            parametros.put("canton", usuSesion.getAgenciaInUser().getIdCanton().getDerscripcion().toString());
            parametros.put("parroquia", usuSesion.getAgenciaInUser().getIdParroquia().getDerscripcion().toString());
            parametros.put("direccion", usuSesion.getAgenciaInUser().getDireccion().toString());
            parametros.put("asistidoPor", getAsistencia());
            parametros.put("telefono", usuSesion.getAgenciaInUser().getTelefono().toString());
            parametros.put("nombreFirma", tblSauregUsuario.getNombre().toString() + " " +tblSauregUsuario.getApellido().toString());
            parametros.put("telefonoFirma", tblSauregUsuario.getAgenciaInUser().getTelefonoUsuario() == null ? " " : tblSauregUsuario.getAgenciaInUser().getTelefonoUsuario().toString());
            parametros.put("cedula", tblSauregUsuario.getNomUsuario().toString());
            parametros.put("ciud_loc", tblSauregUsuario.getAgenciaInUser().getIdCanton().getDerscripcion() == null ? " " : tblSauregUsuario.getAgenciaInUser().getIdCanton().getDerscripcion().toString());
            parametros.put("codMSP", usuSesion.getAgenciaInUser().getCodMsp().toString());
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/nacidoVivo/report69_Users.jasper");
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(items);
            jasperPrint = JasperFillManager.fillReport(reportPath, parametros, beanCollectionDataSource);
            byte[] pdf1 = JasperExportManager.exportReportToPdf(jasperPrint);
            return pdf1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reCargarLista(ActionEvent event) {
        if (this.getBandera() == 0) {
            listRegProf.clear();
            this.cargarListaRegProf();
            RequestContext.getCurrentInstance().update("form:checkboxDT");
            RequestContext.getCurrentInstance().update("growl");
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("RegistroActualizado"), JsfUtil.INFO_MESSAGE);
        }
    }

    public void buscarNacidoVivos() {
        Set<Long> listamama = new HashSet<Long>();
        Long idm;
        for (NacidoVivoRenavi bnacido : listSelectReg) {
            idm = bnacido.getFkCedulMad().getIdMad();
            listamama.add(idm);
        }
        listSelectReg.clear();
        List<NacidoVivoRenavi> listahermanos = new ArrayList<NacidoVivoRenavi>();
        List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
        for (Long idmadre : listamama) {
            parametros.add(new ParametroConsulta("idMad", idmadre));
            parametros.add(new ParametroConsulta("statusNV", JsfUtil.ESTADO_REG_ACTIVO));
            parametros.add(new ParametroConsulta("statusM", JsfUtil.ESTADO_REG_ACTIVO));
            parametros.add(new ParametroConsulta("estFirma", JsfUtil.STAT_FIR_SIN));
            try {
                listahermanos = ejbNacVivFacade.consultarTablaResultList("NacidoVivoRenavi.findByIdMad", parametros);
            } catch (Exception e) {
                System.out.println("ERROR" + e.toString());
            }
            listSelectReg.addAll(listahermanos);
        }
    }

    public void limpiar() {
        listRegProf.clear();
        listRegProfFinal.clear();
        listSelectReg.clear();
        listaProfesional.clear();
        listProfAsig.clear();
    }
    
    private List<ec.gob.digercic.renavi.ws.TblSauregRol> rolesUsuarios(java.lang.String userI, String agencia) {
        ec.gob.digercic.renavi.ws.Usuarios_Service service = new ec.gob.digercic.renavi.ws.Usuarios_Service();
        ec.gob.digercic.renavi.ws.Usuarios port = service.getUsuariosPort();
        List<ec.gob.digercic.renavi.ws.TblSauregRol> lstUserRol;
        lstUserRol = port.getRolesByUsuarioByAgenciaBySistema(userI, agencia, "1");
        return lstUserRol;
    }
}
