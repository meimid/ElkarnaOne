package ihmcredit;

import java.io.Serializable;
import java.util.List;

import com.meimid.core.dto.AccountsMovementLight;


public class MvtBean implements Serializable {
	/**
	 * 
	 */
	private static final long	        serialVersionUID	= 1L;
	private List<AccountsMovementLight>	listMvt;
	private Long	                    solde;

	/**
	 * @return the listMvt
	 */
	public List<AccountsMovementLight> getListMvt() {
		return listMvt;
	}

	/**
	 * @param listMvt
	 *            the listMvt to set
	 */
	public void setListMvt(final List<AccountsMovementLight> listMvt) {
		this.listMvt = listMvt;
	}

	/**
	 * @return the solde
	 */
	public Long getSolde() {
		return solde;
	}

	/**
	 * @param solde
	 *            the solde to set
	 */
	public void setSolde(final Long solde) {
		this.solde = solde;
	}

}
