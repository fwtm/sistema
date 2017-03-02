/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.utilitario.generico;

import ec.gob.digercic.renavi.excepciones.EntidadException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Daniel Porras
 */
public interface AbstractFacadeLocal<T> {

    EntityManager getEntityManager();

    void detach(T entity) throws EntidadException;
	
	void create(T entity) throws EntidadException;

    void edit(T entity) throws EntidadException;

    void remove(T entity) throws EntidadException;

    T find(Object id) throws EntidadException;

    List<T> findAll() throws EntidadException;

    List<T> findRange(int[] range);

    int count();

    void sincronizar() throws EntidadException;
    
    Query generarConsultaDinamica(String query, List<ParametroConsulta> parametros);
    
    Object findByNamedQuerySingleResult(String query) throws EntidadException ;
    
    Object consultarTablaSingleResult(String query, List<ParametroConsulta> parametros) throws EntidadException;
    
    Object consultarTablaSingleResult(String query, String nombre, Object objeto) throws EntidadException;
    
    List<T> findByNamedQueryResultList(String query) throws EntidadException;
    
    List<T> consultarTablaResultList(String query, List<ParametroConsulta> parametros) throws EntidadException;
    
    List<T> consultarTablaResultList(String query, String nombre, Object objeto) throws EntidadException;
    
    int executeQueryBoleanResult(String query, List<ParametroConsulta> parametros) throws EntidadException;
    
    int executeQueryBoleanResult(String query, String nombre, Object objeto) throws EntidadException;
    
    boolean executeNativeQueryBoleanResult(String query) throws EntidadException;
    
    List<T> executeQueryListResult(String query) throws EntidadException;
    
    List<Object[]> executeNativeQueryListResult(String query) throws EntidadException;
    
    List<T> executeNativeQueryListResultGenerico(java.lang.String sqlString, java.lang.Class resultClass) throws EntidadException;
    
    int executeNativeQuery(String query) throws EntidadException;
}
