package com.ccy.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

/**
 * Entity implementation class for Entity: CcyHist
 *
 */
@Entity
@NamedQuery(name="CcyRate.findByUserName",query="Select h from CcyRate h where h.username.username = :username order by h.rateDate desc")
@Configurable
public class CcyRate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private User username;
	private String ccyFrom;
	private String ccyTo;
	
	@Temporal(TemporalType.DATE)
	@Past
	@DateTimeFormat(style = "M-")
	private Date rateDate;
	
	@Temporal(TIMESTAMP)
	private Date creDate = new Date();
	
	private Integer rate;
	
	public CcyRate() {
		super();
	}  
	
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	} 
	
	public User getUsername() {
		return username;
	}
	
	public void setUsername(User username) {
		this.username = username;
	}
	
	public void setUsername(String username) {
		if (username != null)
		this.username = User.findUser(username);
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
	public String getCcyFrom() {
		return this.ccyFrom;
	}

	public void setCcyFrom(String ccyFrom) {
		if (ccyFrom != null && ccyFrom.length() > 3){
			ccyFrom = ccyFrom.substring(0,3);
		}
		this.ccyFrom = ccyFrom;
	}   
	public String getCcyTo() {
		return this.ccyTo;
	}

	public void setCcyTo(String ccyTo) {
		if (ccyTo != null && ccyTo.length() > 3){
			ccyTo = ccyTo.substring(0,3);
		}
		this.ccyTo = ccyTo;
	}   
	
	public Date getRateDate() {
		return rateDate;
	}

	public void setRateDate(Date rateDate) {
		this.rateDate = rateDate;
	}

	public Date getCreDate() {
		return creDate;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	@PersistenceContext
    transient EntityManager entityManager;
	
	public static final EntityManager entityManager() {
        EntityManager em = new CcyRate().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
	
	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
   
	public static CcyRate findUser(Long id) {
        if (id == null) return null;
        return entityManager().find(CcyRate.class, id);
    }
	
	/**
	 * Find CcyHist by username.
	 * @param username - username
	 * @param maxResult - max no of record return, if null then return all result
	 * @return List of ccyHist
	 */
	public static List<CcyRate> findCcyHistByUserName(String username, Integer maxResult) {
		
		Query q = entityManager().createNamedQuery("CcyRate.findByUserName");
		q.setParameter("username", username);
		if (maxResult != null) {
			q.setMaxResults(maxResult);
		}
		
		return ((List<CcyRate>) q.getResultList());
		
	} 
   
}
