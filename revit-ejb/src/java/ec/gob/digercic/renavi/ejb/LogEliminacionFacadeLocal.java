/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*cambio de Juan Hernandez*/
package ec.gob.digercic.renavi.ejb;


import ec.gob.digercic.renavi.entities.LogsEliminacion;
import ec.gob.digercic.renavi.utilitario.generico.AbstractFacadeLocal;
import javax.ejb.Local;

/**
 *
 * @author santiago.tapia
 */
@Local
public interface LogEliminacionFacadeLocal extends AbstractFacadeLocal<LogsEliminacion> {
    
}