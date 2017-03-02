 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ws.datmadre.Persona;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author daniel.porras
 */
@ManagedBean(name = "fotografiaController")
@RequestScoped
public class FotografiaController {
    
    private StreamedContent fotografia;
    @ManagedProperty("#{param.id}")
    private BigDecimal id;
    @ManagedProperty("#{param.cedula}")
    private String cedula;
    @ManagedProperty("#{param.view}")
    private String view;
    @ManagedProperty("#{param.estadoCivil}")
    private String foto;
    
    @EJB
    private ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal ejbFacadeNacidoVivoRenavi;

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            fotografia = new DefaultStreamedContent();
        } else {
            // So, browser is requesting the fotografia. Return a real StreamedContent with the fotografia bytes.
            if(view.equals("Create")){
                //System.out.println(estadoCivil + " *************");
                //if(estadoCivil != null && !estadoCivil.equals("")){
                    try{
                        Persona perso = this.busquedaPorCedula(cedula, JsfUtil.USER_ACCWS_MADRE, JsfUtil.PASS_ACCWS_MADRE);
                        if(perso.getCodigoError().equals("000")){
                            fotografia = new DefaultStreamedContent(new ByteArrayInputStream(perso.getFotografia()));
                        }
                    }catch(Exception e){
                        //e.printStackTrace();
                    }
                //}
            }else{
              try{
                    fotografia = new DefaultStreamedContent(new ByteArrayInputStream(ejbFacadeNacidoVivoRenavi.find(id).getFotoMad()));
                }catch(EntidadException ee){
                    //ee.printStackTrace();
                }  
            }  
        }
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public StreamedContent getFotografia() {
        return fotografia;
    }
    
    private Persona busquedaPorCedula(java.lang.String cedula, java.lang.String usuario, java.lang.String contrasenia) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta_Service service = new ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta_Service();
        ec.gob.digercic.renavi.ws.datmadre.WSRegistroCivilConsulta port = service.getWSRegistroCivilConsultaPort();
        return port.busquedaPorCedula(cedula, usuario, contrasenia);
    }
}
