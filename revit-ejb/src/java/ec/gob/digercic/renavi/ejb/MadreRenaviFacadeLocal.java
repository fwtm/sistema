/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.ejb;

import ec.gob.digercic.renavi.entities.MadreRenavi;
import ec.gob.digercic.renavi.utilitario.generico.AbstractFacadeLocal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author daniel.porras
 */
@Local
public interface MadreRenaviFacadeLocal extends AbstractFacadeLocal<MadreRenavi> {
    
}
