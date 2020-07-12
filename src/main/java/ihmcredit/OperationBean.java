package ihmcredit;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OperationBean implements Serializable {
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
	private String	          montantCredit;
	private String	          montantDebit;

	private String	          password;
	private String	          userName;
	private boolean	          active	         = false;
	private String	          accountLabel;

	private Long	          montant;

	private Date	          dateOperation;

	private String	          libelle;

	private String	          remarque;

	private String	          deleteLibelle;

	private Integer	          numTransc;

	private Date	          from;
	private Date	          to;

	private String	          status	         = "";
	private String	          message	         = "";

	private String	          errotTile	         = "";

	private Long	          price;

	private Integer	          qnt;

	private String	          refProduct;

	private Long	          priceV;

	private String	          modify;

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

	public String getMontantCredit() {
		return montantCredit;
	}

	public void setMontantCredit(final String montantCredit) {
		this.montantCredit = montantCredit;
	}

	public String getMontantDebit() {
		return montantDebit;
	}

	public void setMontantDebit(final String montantDebit) {
		this.montantDebit = montantDebit;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public Date getDateOperation() {
		return dateOperation;
	}

	public void setDateOperation(final Date dateOperation) {
		this.dateOperation = dateOperation;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(final String libelle) {
		this.libelle = libelle;
	}

	public Long getMontant() {
		return montant;
	}

	public void setMontant(final Long montant) {
		this.montant = montant;
	}

	public String getRemarque() {
		return remarque;
	}

	public void setRemarque(final String remarque) {
		this.remarque = remarque;
	}

	public String getDeleteLibelle() {
		return deleteLibelle;
	}

	public void setDeleteLibelle(final String deleteLibelle) {
		this.deleteLibelle = deleteLibelle;
	}

	public Integer getNumTransc() {
		return numTransc;
	}

	public void setNumTransc(final Integer numTransc) {
		this.numTransc = numTransc;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(final Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(final Date to) {
		this.to = to;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
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

	public String getErrotTile() {
		return errotTile;
	}

	public void setErrotTile(final String errotTile) {
		this.errotTile = errotTile;
	}

	public String getAccountLabel() {
		return accountLabel;
	}

	public void setAccountLabel(final String accountLabel) {
		this.accountLabel = accountLabel;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(final Long price) {
		this.price = price;
	}

	public Integer getQnt() {
		return qnt;
	}

	public void setQnt(final Integer qnt) {
		this.qnt = qnt;
	}

	public String getRefProduct() {
		return refProduct;
	}

	public void setRefProduct(final String refProduct) {
		this.refProduct = refProduct;
	}

	public Long getPriceV() {
		return priceV;
	}

	public void setPriceV(final Long priceV) {
		this.priceV = priceV;
	}

	public String getModify() {
		return modify;
	}

	public void setModify(final String modify) {
		this.modify = modify;
	}

}
