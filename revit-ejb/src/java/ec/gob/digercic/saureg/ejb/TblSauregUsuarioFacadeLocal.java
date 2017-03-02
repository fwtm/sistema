/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.ejb;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.AbstractFacadeLocal;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import java.util.List;
import javax.ejb.Local;
import javax.jws.WebParam;

/**
 *
 * @author daniel.porras
 */
@Local
public interface TblSauregUsuarioFacadeLocal extends AbstractFacadeLocal<TblSauregUsuario> {

    public TblSauregUsuario getUsuario(String usuario, String contrasenia, String idSistema, Long estado);
    
    public List<TblSauregUsuario> getUsuariosByAgenciaAndPagina(String idAgencia, String idPagina);
    
    public TblSauregUsuario getUsuarioByUserAndCodAgenciaMSP(String nomUsuario, String codMSP);
    
    public TblSauregUsuario getUsuarioByUsernameAndMail(String nomUsuario, String email);
    
    public String cambiarContrasenia(TblSauregUsuario usuario, String contrasenia, String status);
    
    public String getManeraFirma(String nomUsuario, String codAgencia);
    
    public List<TblSauregUsuario> getUsuariosByAgenciaMSP(String codMSP);
}
