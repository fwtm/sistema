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
import ec.gob.digercic.saureg.ejb.TblSauregCompRolFacadeLocal;
import ec.gob.digercic.saureg.ejb.TblSauregConfigFacadeLocal;
import ec.gob.digercic.saureg.ejb.TblSauregRolSistFacadeLocal;
import ec.gob.digercic.saureg.ejb.TblSauregUsuarioFacadeLocal;
import ec.gob.digercic.saureg.entities.TblSauregAgencia;
import ec.gob.digercic.saureg.entities.TblSauregCompRol;
import ec.gob.digercic.saureg.entities.TblSauregRolSist;
import ec.gob.digercic.saureg.entities.TblSauregSegAcceso;
import ec.gob.digercic.saureg.entities.TblSauregUsuAgencia;
import ec.gob.digercic.saureg.entities.TblSauregUsuario;
import ec.gob.digercic.util.GestorMenu;
import ec.gob.digercic.util.JsfUtil;
import ec.gob.digercic.util.SHAHashing;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author daniel.porras
 */
@Stateless
public class TblSauregUsuarioFacade extends SauregAbstractFacade<TblSauregUsuario> implements TblSauregUsuarioFacadeLocal {

    @EJB
    private TblSauregRolSistFacadeLocal ejbRolSistemaFacade;
    @EJB
    private TblSauregAgenciaFacadeLocal ejbAgenciaFacade;
    @EJB
    private TblSauregCompRolFacadeLocal ejbCompRolFacade;
    @EJB
    private TblSauregConfigFacadeLocal ejbConfigFacade;
    
    @Override
    public TblSauregUsuario getUsuario(String user, String contrasenia, String idSistema, Long estado){
        try{
            TblSauregUsuario usuario = new TblSauregUsuario();
            List<ParametroConsulta> parametrosLogin = new ArrayList<ParametroConsulta>();
            parametrosLogin.add(new ParametroConsulta("usernameUsu", user));
            parametrosLogin.add(new ParametroConsulta("passwordUsu", SHAHashing.encripta(contrasenia)));
            parametrosLogin.add(new ParametroConsulta("estado",estado ));
            usuario = (TblSauregUsuario) this.consultarTablaSingleResult("TblSauregUsuario.findByUsernameAndPasswordUsu", parametrosLogin);
            if (usuario.getIdUsuario() != null) {
                List<TblSauregRolSist> rolSistItems = new ArrayList<TblSauregRolSist>();
                List<ParametroConsulta> paramsRolSist = new ArrayList<ParametroConsulta>();
                paramsRolSist.add(new ParametroConsulta("idSistema", Long.parseLong(idSistema)));
                paramsRolSist.add(new ParametroConsulta("status", JsfUtil.ESTADO_REG_ACTIVO));
                rolSistItems = ejbRolSistemaFacade.consultarTablaResultList("TblSauregRolSist.findByRolesActivosAndSistema", paramsRolSist);
                //interseccion entre roles de sistema y los roles de usuario(verificar si se puede comentar)
                List<TblSauregSegAcceso> segAccItems = new ArrayList<TblSauregSegAcceso>();
                for(TblSauregSegAcceso itemSA : usuario.getTblSauregSegAccesoList()){
                    for(TblSauregRolSist itemRS : rolSistItems){
                        if(itemRS.getTblSauregRol().getIdRol().equals(itemSA.getIdRol().getIdRol()) 
                                && itemSA.getStatus().equals("A")){
                            segAccItems.add(itemSA);
                            break;
                        }
                    }
                }
                //Asigno la nueva lista de roles activos de acuerdo al sistema 
                usuario.setTblSauregSegAccesoList(segAccItems);
                usuario.setListTblSauregAgencia(this.getAgencias(usuario));
                usuario.setTblSauregCompRolList(this.getTblSauRegCompRol(usuario,idSistema));
                if(usuario.getTblSauregCompRolList() == null){
                    return null;
                }else{
                    return usuario;
                }
            } else {
                return null;
            }
        }catch(EntidadException ee){
            ee.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<TblSauregUsuario> getUsuariosByAgenciaAndPagina(String idAgencia, String idPagina) {
        try{
            StringBuilder query = new StringBuilder();
            query.append("select u.* from tbl_saureg_usuario u where u.nom_usuario in (");
            query.append("select sa.nom_usuario from tbl_saureg_seg_acceso sa where sa.id_rol in (");
            query.append("select distinct(cr.id_rol) from tbl_saureg_comp_rol cr where (cr.id_permiso = ");
            query.append(JsfUtil.COD_PERMISO_FIRMA_TOK);
            query.append(" or cr.id_permiso = ");
            query.append(JsfUtil.COD_PERMISO_FIRMA_CER);
            query.append(" ) and cr.id_estruc_sist =");
            query.append(idPagina);
            query.append(" ) and sa.status = '");
            query.append(JsfUtil.ESTADO_REG_ACTIVO);
            query.append("') and u.nom_usuario in (");
            query.append("select ua.nom_usuario from tbl_saureg_usu_agencia ua where ua.id_agencia = ");
            query.append(idAgencia);
            query.append(" and ua.status = '");
            query.append(JsfUtil.ESTADO_REG_ACTIVO);
            query.append("') and u.id_estados = 1 order by u.apellido, u.nombre asc");
            List<TblSauregUsuario> usuariosReturn = new ArrayList<TblSauregUsuario>();
            usuariosReturn = this.executeNativeQueryListResultGenerico(query.toString(), TblSauregUsuario.class);
            if(usuariosReturn.isEmpty()){
                return null;
            }
            else{
                return usuariosReturn;
            }
        }catch(EntidadException ee){
            ee.printStackTrace();
            return null;
        }
    }

    @Override
    public TblSauregUsuario getUsuarioByUserAndCodAgenciaMSP(String nomUsuario, String codMSP) {
        try{
            StringBuilder query = new StringBuilder();
            query.append("select u.* from tbl_saureg_usuario u, tbl_saureg_usu_agencia ua, tbl_saureg_agencia a ");
            query.append("where u.nom_usuario = ua.nom_usuario and a.id_agencia = ua.id_agencia and ua.status = '");
            query.append(JsfUtil.ESTADO_REG_ACTIVO);
            query.append("' and u.nom_usuario = '");
            query.append(nomUsuario);
            query.append("' and a.cod_msp ='");
            query.append(codMSP);
            query.append("'");
            List<TblSauregUsuario> usuariosReturn = new ArrayList<TblSauregUsuario>();
            usuariosReturn = this.executeNativeQueryListResultGenerico(query.toString(), TblSauregUsuario.class);
            if(usuariosReturn.isEmpty()){
                return null;
            }
            else{
                return usuariosReturn.get(0);
            }
        }catch(EntidadException ee){
            return null;
        }
    }
    
    /**
    * Método que recupera todas las agencias a la que pertenece el usuario.
    * @param usuario
    * @return 
    */
    public List<TblSauregAgencia> getAgencias(TblSauregUsuario usuario) {
        List<TblSauregAgencia> listAgencia = new ArrayList<TblSauregAgencia>();
        try {
            if (usuario != null) {
                StringBuilder query = new StringBuilder();
                query.append("select * from tbl_saureg_agencia a, tbl_saureg_institucion t ");
                query.append("where a.id_instituc = t.id_instituc and ");
                query.append("a.id_agencia in (");
                for(int i = 0; i < usuario.getTblSauregUsuAgenciaList().size(); i++){
                    query.append(usuario.getTblSauregUsuAgenciaList().get(i).getTblSauregUsuAgenciaPK().getIdAgencia());
                    if(i == usuario.getTblSauregUsuAgenciaList().size() - 1){
                      break;
                    }else{
                        query.append(", ");
                    }
                }
                query.append(") and a.status = '");
                query.append(JsfUtil.ESTADO_REG_ACTIVO);
                query.append("' order by t.nom_corto, a.nom_agencia asc");
                listAgencia = ejbAgenciaFacade.executeNativeQueryListResultGenerico(query.toString(), TblSauregAgencia.class);
                //Añado el email, telefono, telef extencion del usuario para cada una de las agencias
                for(TblSauregUsuAgencia item : usuario.getTblSauregUsuAgenciaList()){
                    for(int j = 0; j < listAgencia.size(); j++){
                        if(listAgencia.get(j).getIdAgencia()
                                .equals(item.getTblSauregUsuAgenciaPK().getIdAgencia())){
                           listAgencia.get(j).seteMailUsuario(item.getEMail());
                           listAgencia.get(j).setTelefonoUsuario(item.getTelefono());
                           listAgencia.get(j).setTelfExtensionUsuario(item.getTelfExtension());
                           listAgencia.get(j).setCelularUsuario(item.getCelular());
                           break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAgencia;
    }
    
    /**
    * Método que recupera el menú para el usuario
    * en el sistema en el que se encuentra.
    * @param user
    * @param sistema
    * @return 
    */
    public List<TblSauregCompRol> getTblSauRegCompRol(TblSauregUsuario user, String sistema) {
        try {
            List<TblSauregCompRol>  rolMenu = new ArrayList<TblSauregCompRol>();
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            List<String> idsRoles = new ArrayList<String>();
            for(TblSauregSegAcceso item : user.getTblSauregSegAccesoList()){
                idsRoles.add(item.getIdRol().getIdRol().toString());
            }
            if(idsRoles.isEmpty()){
                return null;
            }else{
                ParametroConsulta roles = new ParametroConsulta("idRoles", idsRoles);
                parametros.add(roles);
                ParametroConsulta idSistema = new ParametroConsulta("idSistema", Long.parseLong(sistema));
                parametros.add(idSistema); 
                rolMenu = ejbCompRolFacade.consultarTablaResultList("TblSauregCompRol.findByUserSistema", parametros);
                //Quito los duplicados
                rolMenu = GestorMenu.quitarDuplicados(rolMenu);
                return rolMenu;
            }    
        } catch (EntidadException ex) {
            return null;
        }

    }

    @Override
    public TblSauregUsuario getUsuarioByUsernameAndMail(String usuario, String email) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("select u.* from tbl_saureg_usuario u where u.nom_usuario in (");
            query.append("select ua.nom_usuario from tbl_saureg_usu_agencia ua where ua.nom_usuario = '");
            query.append(usuario);
            query.append("' and ua.e_mail = '");
            query.append(email);
            query.append("' and ua.status = '");
            query.append(JsfUtil.ESTADO_REG_ACTIVO);
            query.append("') and u.id_estados = 1");
            List<TblSauregUsuario> userLogin = new ArrayList<TblSauregUsuario>();
            userLogin =  this.executeNativeQueryListResultGenerico(query.toString(), TblSauregUsuario.class);
            if(!userLogin.isEmpty()){
                return userLogin.get(0);
            }
            return null;
        } catch (EntidadException e) {
            return null;
        }
    }
    
    @Override
    public String cambiarContrasenia(TblSauregUsuario usuario, String contrasenia, String status){
        try{
            usuario.setContrasenia(SHAHashing.encripta(contrasenia));
            usuario.setStatus(status);
            this.edit(usuario);
            return "001";
        }catch(EntidadException ee){
            ee.printStackTrace();
            return "004";
        }
    }
    
    @Override
    public String getManeraFirma(String nomUsuario, String codAgencia){
        List<Object[]> temp = new ArrayList<Object[]>();
        try{
            String query =      /*"select unique  sp.cod_certificado, su.nom_usuario"  
                                +" from tbl_saureg_usuario su, tbl_saureg_seg_acceso sa,"
                                +" tbl_saureg_rol sr,tbl_saureg_comp_rol cr , tbl_saureg_permiso sp"
                                +" where su.nom_usuario ="+ nomUsuario +""
                                +" and su.nom_usuario =  sa.nom_usuario"
                                +" and  sa.id_rol = sr.id_rol and sr.id_rol =  cr.id_rol"
                                +" and cr.id_permiso = sp.id_permiso and sp.cod_certificado is not null"
                                +" and sp.certificado is not null and sa.cod_agencia = " + codAgencia;*/
                                "select unique b.id_rol, a.nom_usuario"
                                +" from tbl_saureg_seg_acceso a, tbl_saureg_rol b"
                                +" where a.nom_usuario = '"+ nomUsuario +"' "
                                +" and b.id_rol = a.id_rol"
                                +" and (a.id_rol = 14 or a.id_rol = 16)"
                                +" and a.cod_agencia = "+ codAgencia +" "
                                +" and b.status = 'A' "
                                +" and a.status = 'A' "; 
            temp = this.executeNativeQueryListResult(query);
            if(temp.isEmpty()){
                return null;
            }
            else{
                return temp.get(0)[0].toString();
            }
        }catch(EntidadException ee){
            return null;
        }
    }
    
    @Override
    public List<TblSauregUsuario> getUsuariosByAgenciaMSP(String codMSP){
        try {
            //Usuarios de la agencia
            List<TblSauregUsuario> usuarios = new ArrayList<TblSauregUsuario>();
            StringBuilder query = new StringBuilder("SELECT * FROM TBL_SAUREG_USUARIO u WHERE u.id_estados = 1 ");
            query.append("AND u.NOM_USUARIO IN(SELECT ua.NOM_USUARIO FROM TBL_SAUREG_USU_AGENCIA ua WHERE ua.ID_AGENCIA = (select a1.id_agencia from tbl_saureg_agencia a1 where a1.cod_msp ='");
            query.append(codMSP);
            query.append("') ");
            query.append("AND ua.STATUS='A') AND u.NOM_USUARIO IN(SELECT sa.NOM_USUARIO FROM TBL_SAUREG_SEG_ACCESO sa WHERE sa.ID_ROL IN (1,14,16) AND sa.STATUS='A')");
            usuarios = this.executeNativeQueryListResultGenerico(query.toString(), TblSauregUsuario.class );
            if(usuarios.isEmpty()){
                return null;
            }
            else{
                return usuarios;
            }
        } catch (EntidadException e) {
            return null;
        }
    }
}
