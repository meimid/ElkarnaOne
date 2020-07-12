package ihmcredit;

import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meimid.core.dto.AccountDto;
import com.meimid.core.dto.AccountsMovementLight;

@RestController
@PreAuthorize("hasAuthority('ROLE_ONLINE')")
@RequestMapping("/online")
@CrossOrigin
	 
public class OnlineController  extends AbstractClassBase {
	
	@RequestMapping(value = "/accountDetail", consumes = "application/json",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public MvtBean getAccountDetail(@RequestBody final OperationBean operation) throws JsonProcessingException {
		final String l_numCompte = operation.getNumCompte();

		final Date l_fromDate = operation.getFrom();
		final Date l_toDate = operation.getTo();

		if (!StringUtils.isEmpty(l_numCompte)) {
			long start = System.currentTimeMillis();
			final AccountDto account = accountService
			        .getAccountLightByNumCompte(l_numCompte);
			long elapsedTimeMillis = System.currentTimeMillis() - start;
				if (account != null) {
				start = System.currentTimeMillis();
				final List<AccountsMovementLight> l_list = accountService
				        .getAccountsMovementLightByNameAccount(l_numCompte,
				                l_fromDate, l_toDate);
				elapsedTimeMillis = System.currentTimeMillis() - start;
				final MvtBean mvt = new MvtBean();
				mvt.setListMvt(l_list);
				start = System.currentTimeMillis();
				mvt.setSolde(accountService.getAccountBalance(account
				        .getNumAccount()));
				elapsedTimeMillis = System.currentTimeMillis() - start;
				System.out.println("elapsedTimeMillis sold "
				        + elapsedTimeMillis + " sn");
				// Get elapsed time in seconds
				return mvt;

			} 

		}
		return null;
	}

}
