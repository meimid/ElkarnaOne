package ihmcredit;

import java.io.Serializable;
import java.util.Date;

public class OperationDto implements Serializable {

	private String	numCompte;
	private Date	from;
	private Date	to;
	private String	libelle;
	private String	nomClient;

	public OperationDto() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the numCompte
	 */
	public String getNumCompte() {
		return numCompte;
	}

	/**
	 * @param numCompte
	 *            the numCompte to set
	 */
	public void setNumCompte(final String numCompte) {
		this.numCompte = numCompte;
	}

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(final Date from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final Date to) {
		this.to = to;
	}

	/**
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle
	 *            the libelle to set
	 */
	public void setLibelle(final String libelle) {
		this.libelle = libelle;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}
	
	

}
