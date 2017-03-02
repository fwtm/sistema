/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.ejb.impl;

import ec.gob.digercic.renavi.ejb.*;
import ec.gob.digercic.renavi.entities.CatalogoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.impl.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author daniel.porras
 */
@Stateless
public class CatalogoRenaviFacade extends AbstractFacade<CatalogoRenavi> implements CatalogoRenaviFacadeLocal {

    @Override
    public List<CatalogoRenavi> getCatalogo(String descripcion) {
        StringBuilder query = new StringBuilder().
                append("SELECT * FROM CATALOGO_RENAVI WHERE DESCRIPCION_CAT_RENAVI = '").
                append(descripcion).
                append("' AND STATUS_CAT_RENAVI = 'A' ORDER BY NOMBRE_CAT_RENAVI");
        List<CatalogoRenavi> lista = null;
        try {
            lista = executeNativeQueryListResultGenerico(query.toString(), CatalogoRenavi.class);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        return lista;

    }

    @Override
    public List<CatalogoRenavi> getCatalogo(String descripcion, String condicion) {
        StringBuilder query = new StringBuilder().
                append("SELECT * FROM CATALOGO_RENAVI WHERE DESCRIPCION_CAT_RENAVI = '").
                append(descripcion).
                append("' AND CONDICION_CAT_RENAVI IN (").
                append(condicion).
                append(") AND STATUS_CAT_RENAVI = 'A' ORDER BY NOMBRE_CAT_RENAVI");
        List<CatalogoRenavi> lista = null;
        try {
            lista = executeNativeQueryListResultGenerico(query.toString(), CatalogoRenavi.class);
        } catch (EntidadException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
