/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.ejb.impl;


import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.impl.AbstractFacade;
import ec.gob.digercic.util.JsfUtil;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author daniel.porras
 */
@Stateless
public class NacidoVivoRenaviFacade extends AbstractFacade<NacidoVivoRenavi> implements NacidoVivoRenaviFacadeLocal {

    //INICIO DFJ
    @Override
    public void agregarMalformaciones(NacidoVivoRenavi nacidovivo, List<String> listaMalformaciones) {

        try {
            //Elimina malformaciones del nacido vivo
            eliminarMalformaciones(nacidovivo);
            if (listaMalformaciones != null && listaMalformaciones.isEmpty() == false) {
                for (String id_cat_renavi : listaMalformaciones) {
                    String sql = "INSERT INTO  MALFOR_NACIDO_VIVO_RENAVI (id_nac_viv,id_cat_renavi) Values (" + nacidovivo.getIdNacViv() + "," + id_cat_renavi + ")";
                    executeNativeQuery(sql);
                }
            }
        } catch (EntidadException e) {
        }
    }

    @Override
    public void eliminarMalformaciones(NacidoVivoRenavi nacidovivo) {
        try {
            //Elimina malformaciones del nacido vivo
            String sql = "DELETE FROM MALFOR_NACIDO_VIVO_RENAVI WHERE id_nac_viv=" + nacidovivo.getIdNacViv();
            executeNativeQuery(sql);
        } catch (EntidadException e) {
        }
    }

    @Override
    public List getMalformaciones(NacidoVivoRenavi nacidovivo) {
        List lista = null;
        try {
            lista = executeNativeQueryListResult("SELECT id_cat_renavi FROM MALFOR_NACIDO_VIVO_RENAVI where id_nac_viv=" + nacidovivo.getIdNacViv());
        } catch (EntidadException e) {
        }
        return lista;
    }
    //FIN DFJ

    @Override
    public void eliminarAnulados(NacidoVivoRenavi nacidovivo) {
        try {
            //Elimina malformaciones del nacido vivo
            String sql = "DELETE FROM anulacion WHERE fk_ced_nac_viv=" + nacidovivo.getIdNacViv();
            executeNativeQuery(sql);
        } catch (EntidadException e) {
        }
    }
}
