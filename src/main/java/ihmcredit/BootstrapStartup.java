package ihmcredit;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.meimid.core.model.Account;
import com.meimid.core.model.UserRoles;
import com.meimid.core.model.Users;
import com.meimid.core.service.IAccountService;
import com.meimid.core.service.ITypeCompteService;
import com.meimid.core.service.ITypeOperationService;
import com.meimid.core.service.IUserService;

@Component
public class BootstrapStartup {

	// @Autowired
	IUserService	      userService;

	IAccountService	      accountService;

	MessageSource	      messageSource;


	public void initialize() {
		final List<Users> users = userService.getUserByRole("ROLE_ADMIN");

		if (CollectionUtils.isEmpty(users)) {
			final Users user = new Users();
			user.setUserLogin("ADMIN");
			user.setName("meimid");
			//user.setPassword("604250983c36b66a1faaf9bd01b30227");
			user.setPassword("admin");
			user.setCode("mei");
			user.setEnabled(1);
			final UserRoles userRoles = new UserRoles();
			userRoles.setUser(user);
			userRoles.setRole("ROLE_ADMIN");
			user.getRoles().add(userRoles);
			userService.createUser(user);
			

		}
		//updatePassword();
		Users user = userService.getUserByLogin("ONLINE");

		if (user==null) {
			user = new Users();
			user.setUserLogin("ONLINE");
			user.setName("ONLINE");
			user.setPassword("ONLINE");
			user.setCode("one");
			user.setEnabled(1);
			final UserRoles userRoles = new UserRoles();
			userRoles.setUser(user);
			userRoles.setRole("ROLE_ONLINE");
			user.getRoles().add(userRoles);
			userService.createUser(user);
			

		}
		
		
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the accountService
	 */
	public IAccountService getAccountService() {
		return accountService;
	}

	/**
	 * @param accountService
	 *            the accountService to set
	 */
	public void setAccountService(final IAccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource
	 *            the messageSource to set
	 */
	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	
@Transactional
	void updatePassword(){
		final Users user = userService.getUserByLogin("ADMIN");
		if (user != null) {
			user.setPassword("admin");
			userService.updateUser(user);
			

		}
		
		
	}

	

}
