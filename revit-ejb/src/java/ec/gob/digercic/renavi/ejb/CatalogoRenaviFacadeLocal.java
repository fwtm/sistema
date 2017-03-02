/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.ejb;

import ec.gob.digercic.renavi.entities.CatalogoRenavi;
import ec.gob.digercic.renavi.utilitario.generico.AbstractFacadeLocal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author daniel.porras
 */
@Local
public interface CatalogoRenaviFacadeLocal extends AbstractFacadeLocal<CatalogoRenavi> {
    /**
     * DFJ Retorna una lista de catalogo de tipo CatalogoRenavi
     *
     * @param descripcion
     * @return
     */
    public List<CatalogoRenavi> getCatalogo(String descripcion);

    /**
     * DFJ Retorna una lista de catalogo de tipo CatalogoRenavi a partir de una
     * condición
     *
     * @param descripcion
     * @param condicion
     * @return
     */
    public List<CatalogoRenavi> getCatalogo(String descripcion, String condicion);

}
