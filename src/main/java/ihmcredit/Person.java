package ihmcredit;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Person implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	@Size(min = 2, max = 130)
	private String	          firstName;
	private Integer	          age;
	private String	          adresse;
	@NotNull
	private String	          type;
	private String	          numCompte;

	private String	          libelle;

	private String	          tel;

	private String	          status;

	private String	          message;

	private String	          numPersonne;
	private String	          email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(final Integer age) {
		this.age = age;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(final String adresse) {
		this.adresse = adresse;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getNumCompte() {
		return numCompte;
	}

	public void setNumCompte(final String numCompte) {
		this.numCompte = numCompte;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(final String libelle) {
		this.libelle = libelle;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(final String tel) {
		this.tel = tel;
	}

	public Object convert(final Object arg0, final Class arg1, final Object arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getNumPersonne() {
		return numPersonne;
	}

	public void setNumPersonne(final String numPersonne) {
		this.numPersonne = numPersonne;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}
