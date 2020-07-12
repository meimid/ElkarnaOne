package ihmcredit;

import java.util.Date;

import com.meimid.core.dto.TransfereReq;

public class OperationDiverBean {
	

	private String type;
	
	private String remarque;

	private Long montantDebit;

	private Long montantCredit;

	private final Date dateCreation = new Date();

	private Date dateOperation = new Date();

	private Boolean valide;

	private Long montant;

	private String message;

	private String status;
	
      private String numCompteFrom="";
      private String numCompteTo="";
	
	private Long numTransc;
	
	TransfereReq transfereReq;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemarque() {
		return remarque;
	}

	public void setRemarque(String remarque) {
		this.remarque = remarque;
	}

	public Long getMontantDebit() {
		return montantDebit;
	}

	public void setMontantDebit(Long montantDebit) {
		this.montantDebit = montantDebit;
	}

	public Long getMontantCredit() {
		return montantCredit;
	}

	public void setMontantCredit(Long montantCredit) {
		this.montantCredit = montantCredit;
	}

	public Date getDateOperation() {
		return dateOperation;
	}

	public void setDateOperation(Date dateOperation) {
		this.dateOperation = dateOperation;
	}

	public Boolean getValide() {
		return valide;
	}

	public void setValide(Boolean valide) {
		this.valide = valide;
	}

	public Long getMontant() {
		return montant;
	}

	public void setMontant(Long montant) {
		this.montant = montant;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNumCompteFrom() {
		return numCompteFrom;
	}

	public void setNumCompteFrom(String numCompteFrom) {
		this.numCompteFrom = numCompteFrom;
	}

	public String getNumCompteTo() {
		return numCompteTo;
	}

	public void setNumCompteTo(String numCompteTo) {
		this.numCompteTo = numCompteTo;
	}

	public Long getNumTrans() {
		return numTransc;
	}

	public void setNumTrans(Long numTransc) {
		this.numTransc = numTransc;
	}

	public TransfereReq getTransfereReq() {
		return transfereReq;
	}

	public void setTransfereReq(TransfereReq transfereReq) {
		this.transfereReq = transfereReq;
	}

	public Date getDateCreation() {
		return dateCreation;
	}
	
	

}
