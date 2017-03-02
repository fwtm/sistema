/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.ejb.impl;

import ec.gob.digercic.renavi.ejb.NacionalidadRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.NacionalidadRenavi;
import ec.gob.digercic.renavi.utilitario.generico.impl.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author daniel.porras
 */
@Stateless
public class NacionalidadRenaviFacade extends AbstractFacade<NacionalidadRenavi> implements NacionalidadRenaviFacadeLocal {
    
}
