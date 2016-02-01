/**
 * 
 */
package com.ccy.domain.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aashish Amrute
 *
 */
public class GenericDAO {

	private final static Logger LOG = LoggerFactory.getLogger(GenericDAO.class);

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new GenericDAO().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static List<?> findByFieldName(Object o, String[] fieldNames) {

		if (fieldNames.length == 0) {
			return Collections.EMPTY_LIST;
		}

		String query = "Select o.username from " + o.getClass().getSimpleName() + " o ";
		String where = null; 

		for (String field : fieldNames) {
			
			if (where == null){
				where = "where";
			}else {
				where = "and";
			}
			
			try {
				where += " o." + field + " = '"
						+ (String) o.getClass()
								.getDeclaredMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1))
								.invoke(o) + "' ";
			} catch (IllegalAccessException e) {
				LOG.error("Exception in findByFieldName ", e);
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				LOG.error("Exception in findByFieldName ", e);
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				LOG.error("Exception in findByFieldName ", e);
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				LOG.error("Invalid Field: " + field, e);
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				LOG.error("Securtiy Exception in findByFieldName ", e);
				throw new RuntimeException(e);
			}
		}
		
		//return Collections.EMPTY_LIST;
		return entityManager().createQuery(query + where).getResultList();
		
	}
}
