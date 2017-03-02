/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.ejb.impl;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.utilitario.generico.impl.SauregAbstractFacade;
import ec.gob.digercic.saureg.ejb.TblSauregAgenciaFacadeLocal;
import ec.gob.digercic.saureg.entities.TblSauregAgencia;
import ec.gob.digercic.util.JsfUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author daniel.porras
 */
@Stateless
public class TblSauregAgenciaFacade extends SauregAbstractFacade<TblSauregAgencia> implements TblSauregAgenciaFacadeLocal {
    
    @Override
    public List<TblSauregAgencia> getAgenciasPorInstitucion(String idInstitucion){
        List<TblSauregAgencia> listAgencia = new ArrayList<TblSauregAgencia>();
        try {
            /*List<ParametroConsulta> params = new ArrayList<ParametroConsulta>();
            params.add(new ParametroConsulta("codInstituc", Long.parseLong(idInstitucion)));
            params.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO.toString()));*/
            StringBuilder query = new StringBuilder("select * from tbl_saureg_agencia a ");
            query.append("where a.id_instituc = ");
            query.append(idInstitucion);
            query.append(" and a.status = '");
            query.append(JsfUtil.ESTADO_REG_ACTIVO);
            query.append("'");
            listAgencia = this.executeNativeQueryListResultGenerico(query.toString(), TblSauregAgencia.class);
            return listAgencia;
        } catch (EntidadException e) {
            e.printStackTrace();
            return null;
        }
    }
}
