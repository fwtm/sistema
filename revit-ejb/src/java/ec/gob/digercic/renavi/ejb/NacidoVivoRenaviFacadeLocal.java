/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.ejb;

import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.utilitario.generico.AbstractFacadeLocal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author daniel.porras
 */
@Local
public interface NacidoVivoRenaviFacadeLocal extends AbstractFacadeLocal<NacidoVivoRenavi> {

    public void agregarMalformaciones(NacidoVivoRenavi nacidovivo, List<String> listaMalformaciones);

    public void eliminarMalformaciones(NacidoVivoRenavi nacidovivo);

    public List getMalformaciones(NacidoVivoRenavi nacidovivo);

    public void eliminarAnulados(NacidoVivoRenavi nacidovivo);

}
