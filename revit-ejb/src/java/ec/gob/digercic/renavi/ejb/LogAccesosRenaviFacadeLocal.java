/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.ejb;

import ec.gob.digercic.renavi.entities.LogsAcceso;
import ec.gob.digercic.renavi.utilitario.generico.AbstractFacadeLocal;
import javax.ejb.Local;

/**
 *
 * @author santiago.tapia
 */
@Local
public interface LogAccesosRenaviFacadeLocal extends AbstractFacadeLocal<LogsAcceso> {
    
}
