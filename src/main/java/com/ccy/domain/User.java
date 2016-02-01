package com.ccy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.ccy.domain.validate.ConfimPassowrd;

/**
 * Entity implementation class for Entity: User
 * @author Aashish Amrute
 */
@Entity
@ConfimPassowrd
@Configurable
public class User implements Serializable {
	
	@Id
	@Pattern(regexp="[a-zA-Z0-9]+", message="Only alphanumeric allowed")
	@Column(unique=true)
	private String username;
	
	@NotEmpty
	@Length(min=8,max=16)
	private String password;
	
	//confirm password field
	private transient String confirmPassowrd;
	
	//user unable or disable
	private boolean enabled = true;
	
	@Email
	@NotEmpty
	@Column(unique=true)
	private String email;
	
	/**
     */
    @Size(min = 3, max = 30)
    private String firstName;

    /**
     */
    @NotNull
    @Size(min = 3, max = 30)
    private String lastName;

    /**
     */
    @NotNull
    @Size(min = 1, max = 50)
    private String address;

    @Pattern(regexp="[0-9]+", message="Invalid Zip Code!!")
    private String zip;
    
    /**
     */
    @NotEmpty
    @Size(max = 30)
    private String city;
    
    /**
     */
    @NotEmpty
    private String country;

    /**
     */
    @Pattern(regexp="[0-9]+",  message="Invalid Mobile Number !!")
    private String mobile;


    /**
     */
    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date birthDay;
	
	private String role = "CUSTOMER";
	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}   
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}   
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}   
	public String getConfirmPassowrd() {
		return confirmPassowrd;
	}
	public void setConfirmPassowrd(String confirmPassowrd) {
		this.confirmPassowrd = confirmPassowrd;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}   
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	@PersistenceContext
    transient EntityManager entityManager;
	
	public static final EntityManager entityManager() {
        EntityManager em = new User().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
	
	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
   
	public static User findUser(String id) {
        if (id == null) return null;
        return entityManager().find(User.class, id);
    }
	
}
