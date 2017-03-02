/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.utilitario.generico.impl;

import ec.gob.digercic.renavi.utilitario.generico.AbstractFacadeLocal;
import ec.gob.digercic.renavi.utilitario.generico.ParametroConsulta;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author Daniel Porras
 */
public abstract class AbstractFacade<T> implements AbstractFacadeLocal<T> {

    @PersistenceContext(unitName = "revit-ejbPU")
    protected EntityManager em;
    protected Class<T> entityClass;

    public AbstractFacade(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    public AbstractFacade() {
        Class<T> tipoNuevo = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.entityClass = tipoNuevo;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void detach(T entity) throws EntidadException {
        try {
            getEntityManager().detach(entity);
            sincronizar();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntidadException(entity.getClass().getSimpleName() + ": error al hacer detach.",
                    e.getCause());
        }
    }
	
    @Override
    public void create(T entity) throws EntidadException {
        try {
            System.out.println("AbstractFacade.create(T entity) throws EntidadException");
            getEntityManager().persist(entity);
            sincronizar();
        }catch(EntidadException ee){
            throw new EntidadException(ee.getMessage(), ee.getCause());
        }catch (Exception e) {
            e.printStackTrace();
            throw new EntidadException(entity.getClass().getSimpleName() + ": error al guardar.",
                    e.getCause());
        }
    }

    @Override
    public void edit(T entity) throws EntidadException {
        try {
            getEntityManager().merge(entity);
            sincronizar();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntidadException(entity.getClass().getSimpleName() + ": error al editar.",
                    e.getCause());
        }
    }

    @Override
    public void remove(T entity) throws EntidadException {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
            sincronizar();
        } catch (Exception e) {
            throw new EntidadException(entity.getClass().getSimpleName() + ": error al eliminar.",
                    e.getCause());
        }
    }

    @Override
    public T find(Object id) throws EntidadException {
        try {
            return getEntityManager().find(entityClass, id);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            throw new EntidadException("Error al consultar "
                    + "la entidad, verifique los parámetros de consulta.", iae.getCause());
        } catch (NoResultException nre) {
            throw new EntidadException("No hay datos para la búsqueda realizada.", nre.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntidadException("Error al consultar la entidad.", e.getCause());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() throws EntidadException {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            return getEntityManager().createQuery(cq).getResultList();
        } catch (NoResultException nre) {
            throw new EntidadException("Error, no hay datos para la búsqueda realizada.", nre.getCause());
        } catch (Exception e) {
            throw new EntidadException("Error al consultar la entidad.", e.getCause());
        }
    }

    @Override
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void sincronizar() throws EntidadException, PersistenceException {
        try {
            em.flush();
        } catch (Exception e) {
            System.out.println(e.getClass() + " ----------------------------------");
            throw new EntidadException(
                    "Error en la base de datos. Es posible que el registro este duplicado o siendo utilizado en otro proceso", e.getCause());
        }
    }

    /**
     * Construye una consulta dinÃ¡mica basandose el nombre del named query y
     * los parÃ¡metros que se le pasa como parÃ¡mero.
     *
     * @param query
     * @param parametros
     * @return
     */
    @Override
    public Query generarConsultaDinamica(String query, List<ParametroConsulta> parametros) {
        try {
            Query consulta = getEntityManager().createNamedQuery(query);
            for (ParametroConsulta parametro : parametros) {
                consulta.setParameter(parametro.getNombreParametro(), parametro.getValorParametro());
            }
            return consulta;
        } catch (IllegalArgumentException iae) {
            System.out.println("Error en los parámetros de entrada al generar la consulta dinámica "
                    + iae.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error al generar la consulta dinámica " + e.getMessage());
            return null;
        }
    }

    /**
     * Realiza una consulta a la base de datos indicandole cual es el named
     * query
     *
     * @param query
     * @return T un objeto
     */
    @Override
    public Object findByNamedQuerySingleResult(String query) throws EntidadException {
        try {
            Query consulta = getEntityManager().createNamedQuery(query);
            return consulta.getSingleResult();
        } catch (IllegalArgumentException iae) {
            throw new EntidadException("Error, no existe la consulta.", iae.getCause());
        } catch (NoResultException nre) {
            throw new EntidadException("No hay datos para la búsqueda realizada.", nre.getCause());
        } catch (NonUniqueResultException nure) {
            throw new EntidadException("Hay más de un resultado para la consulta realizada.", nure.getCause());
        } catch (Exception e) {
            throw new EntidadException("Error al consultar la entidad", e.getCause());
        }
    }

    /**
     * Genera una consulta dinÃ¡mica a la base de datos, devolviendo un único
     * resultado
     *
     * @param query
     * @param parametros
     * @return
     */
    @Override
    public Object consultarTablaSingleResult(String query, List<ParametroConsulta> parametros) throws EntidadException {
        try {
            return this.generarConsultaDinamica(query, parametros).getSingleResult();
        } catch (IllegalArgumentException iae) {
            throw new EntidadException("Error al consultar la tabla, verifique los parámetros ingresados.",
                    iae.getCause());
        } catch (NoResultException nre) {
            throw new EntidadException("No hay datos para la búsqueda realizada.", nre.getCause());
        } catch (NonUniqueResultException nure) {
            throw new EntidadException("Hay más de un resultado para la consulta realizada.", nure.getCause());
        } catch (Exception e) {
            throw new EntidadException("Error al consultar la tabla.", e.getCause());
        }

    }

    /**
     *
     * @param query
     * @param nombre
     * @param objeto
     * @return
     * @throws EntidadException
     */
    @Override
    public Object consultarTablaSingleResult(String query, String nombre, Object objeto) throws EntidadException {
        try {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            parametros.add(new ParametroConsulta(nombre, objeto));
            return this.generarConsultaDinamica(query, parametros).getSingleResult();
        } catch (IllegalArgumentException iae) {
            throw new EntidadException("Error al consultar la tabla, verifique los parámetros ingresados.",
                    iae.getCause());
        } catch (NoResultException nre) {
            throw new EntidadException("No hay datos para la búsqueda realizada.", nre.getCause());
        } catch (NonUniqueResultException nure) {
            throw new EntidadException("Hay más de un resultado para la consulta realizada.", nure.getCause());
        } catch (Exception e) {
            //System.out.println(e.getStackTrace());
            throw new EntidadException("Error al consultar la tabla", e.getCause());
        }
    }

    /**
     * Realiza una consulta a la base de datos indicandole cual es el named
     * query
     *
     * @param query
     * @return List<T> lista de objetos
     */
    @Override
    public List<T> findByNamedQueryResultList(String query) throws EntidadException {
        try {
            Query consulta = getEntityManager().createNamedQuery(query);
            return consulta.getResultList();
        } catch (IllegalArgumentException iae) {
            throw new EntidadException("Error, no existe la consultarealizada.", iae.getCause());
        } catch (Exception e) {
            throw new EntidadException("Error al consultar la entidad", e.getCause());
        }
    }

    /**
     * Genera una consulta dinÃ¡mica a la base de datos
     *
     * @param query
     * @param parametros
     * @return
     */
    @Override
    public List<T> consultarTablaResultList(String query, List<ParametroConsulta> parametros) throws EntidadException {
        try {
            return this.generarConsultaDinamica(query, parametros).getResultList();
        } catch (IllegalArgumentException iae) {
            throw new EntidadException("Error al consultar la tabla, verifique los parámetros ingresados.",
                    iae.getCause());
        } catch (Exception e) {
            throw new EntidadException("Error al consultar la tabla.", e.getCause());
        }

    }

    @Override
    public List<T> consultarTablaResultList(String query, String nombre, Object objeto) throws EntidadException {
        try {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            parametros.add(new ParametroConsulta(nombre, objeto));
            return this.generarConsultaDinamica(query, parametros).getResultList();
        } catch (IllegalArgumentException iae) {
            throw new EntidadException("Error al consultar la tabla, verifique los parámetros ingresados.",
                    iae.getCause());
        } catch (Exception e) {
            throw new EntidadException("Error al consultar la tabla.", e.getCause());
        }
    }

    /**
     * Polimorfismo Método para ejecutar un query (JPA) el cual me devuelva un
     * resultado boleano Este metodo se utiliza en actualizaciones y
     * eliminaciones
     *
     * @param query
     * @param parametros
     * @return
     * @throws EntidadException
     */
    @Override
    public int executeQueryBoleanResult(String query, List<ParametroConsulta> parametros) throws EntidadException {
        try {
            return this.generarConsultaDinamica(query, parametros).executeUpdate();
        } catch (Exception e) {
            throw new EntidadException("Error al ejecutar la actualización.",
                    e.getCause());
        }
    }

    /**
     * Polimorfismo Método para ejecutar un query (JPA) el cual me devuelva un
     * resultado boleano Este metodo se utiliza en actualizaciones y
     * eliminaciones
     *
     * @param query
     * @param nombre
     * @param objeto
     * @return
     * @throws EntidadException
     */
    @Override
    public int executeQueryBoleanResult(String query, String nombre, Object objeto) throws EntidadException {
        try {
            List<ParametroConsulta> parametros = new ArrayList<ParametroConsulta>();
            parametros.add(new ParametroConsulta(nombre, objeto));
            return this.generarConsultaDinamica(query, parametros).executeUpdate();
        } catch (Exception e) {
            throw new EntidadException("Error al ejecutar la consulta.",
                    e.getCause());
        }
    }

    /**
     * Método para ejecutar un query nativo el cual me devuelba un resultado
     * boleano Este metodo se utiliza en actualizaciones e inserciones
     *
     * @param query
     * @return
     * @throws EntidadException
     */
    @Override
    public boolean executeNativeQueryBoleanResult(String query) throws EntidadException {
        try {
            Query consulta = em.createNativeQuery(query);
            return (Boolean) consulta.getSingleResult();
        } catch (Exception e) {
            throw new EntidadException("Error al ejecutar la consulta.",
                    e.getCause());
        }
    }

    /**
     * Método para ejecutar un query previamente armado el cual devuelba una
     * lista de objetos
     *
     * @param query
     * @return
     * @throws EntidadException
     */
    @Override
    public List<T> executeQueryListResult(String query) throws EntidadException {
        try {
            Query consulta = em.createQuery(query);
            return consulta.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntidadException("Error al ejecutar la consulta.",
                    e.getCause());
        }
    }

    /**
     * Método para ejecutar un query nativo previamente armado el cual devuelba
     * una lista de objetos
     *
     * @param query
     * @return
     * @throws EntidadException
     */
    @Override
    public List<Object[]> executeNativeQueryListResult(String query) throws EntidadException {
        try {
            Query consulta = em.createNativeQuery(query);
            //Query consulta = em.createNamedQuery(query, ConAsiTimbrada.class);
            return (List<Object[]>) consulta.getResultList();
        } catch (Exception e) {
            throw new EntidadException("Error al ejecutar la consulta.",
                    e.getCause());
        }
    }
    
    /**
     * Método para ejecutar un query nativo generado en tiempo de ejecución.
     * Devuelve un list de <T>. 
     *
     * @param query
     * @return
     * @throws EntidadException
     */
    @Override
    public List<T> executeNativeQueryListResultGenerico(java.lang.String sqlString, java.lang.Class resultClass) throws EntidadException {
        try {
            List<T> resultado;
            Query consulta = em.createNativeQuery(sqlString, resultClass);
            resultado = consulta.getResultList();
            return resultado; 
        } catch (Exception e) {
            throw new EntidadException("Error al ejecutar la consulta.",
                    e.getCause());
        }
    }
    
    
    /**
     * Polimorfismo Método para ejecutar un query (JPA) el cual me devuelva un
     * resultado boleano Este metodo se utiliza en actualizaciones y
     * eliminaciones
     *
     * @param query
     * @param parametros
     * @return
     * @throws EntidadException
     */
    @Override
    public int executeNativeQuery(String query) throws EntidadException {
        try {
            return em.createNativeQuery(query).executeUpdate();
        } catch (Exception e) {
            throw new EntidadException("Error al ejecutar la consulta.",
                    e.getCause());
        }
    }
}
