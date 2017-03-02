/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.ejb;

import ec.gob.digercic.renavi.utilitario.generico.AbstractFacadeLocal;
import ec.gob.digercic.saureg.entities.TblSauregAgencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author daniel.porras
 */
@Local
public interface TblSauregAgenciaFacadeLocal extends AbstractFacadeLocal<TblSauregAgencia> {
    
    public List<TblSauregAgencia> getAgenciasPorInstitucion(String idInstitucion);
}
