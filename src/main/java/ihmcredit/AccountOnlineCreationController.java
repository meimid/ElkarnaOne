package ihmcredit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meimid.core.model.GlobalConf;
import com.meimid.core.model.UserRoles;
import com.meimid.core.model.Users;
import com.meimid.core.service.IGlobalConfService;
import com.meimid.core.service.IUserService;
import org.apache.commons.validator.routines.EmailValidator;
@RestController
@CrossOrigin
@RequestMapping("/signup")
public class AccountOnlineCreationController  extends AbstractClassBase {
	
	@Autowired
	IUserService	          userService;
	@Autowired
	IGlobalConfService	          globalService;
	@Autowired
	MessageSource	          messageSource;
	
	@RequestMapping(value = "/registreNewUser",
	        headers = { "Content-type=application/json" },
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Users saveUser(@RequestBody final Users userP) throws JsonProcessingException {
		if (userP == null || StringUtils.isEmpty(userP.getUserLogin())) {
			final String msg = messageSource.getMessage("label.name.required",
			        null, LocaleContextHolder.getLocale());
			userP.setMessage(msg);
			return userP;

		}
		if (userP == null || StringUtils.isEmpty(userP.getEmail())) {
			final String msg = messageSource.getMessage("lable.email.require",
			        null, LocaleContextHolder.getLocale());
			userP.setMessage(msg);
			return userP;

		}
	if(!EmailValidator.getInstance().isValid(userP.getEmail()))
	{
		final String msg = messageSource.getMessage("lable.email.not.valide",
		        null, LocaleContextHolder.getLocale());
		userP.setMessage(msg);
		return userP;
		
	}
	
	if (  userService.findUserByEmail(userP.getEmail())!=null) {
		final String msg = messageSource.getMessage("lable.email.existe",
		        null, LocaleContextHolder.getLocale());
		userP.setMessage(msg);
		return userP;

	}
	
	if (  userService.getUserByLogin(userP.getUserLogin().toUpperCase())!=null) {
		final String msg = messageSource.getMessage("lable.login.exist",
		        null, LocaleContextHolder.getLocale());
		userP.setMessage(msg);
		return userP;

	}

	
	

		if (StringUtils.isEmpty(userP.getUserLogin())
		        || StringUtils.isEmpty(userP.getPassword())) {
			String msg = "";
			if (StringUtils.isEmpty(userP.getUserLogin())) {
				msg = messageSource.getMessage("label.name.required", null,
				        LocaleContextHolder.getLocale());
			}
			if (StringUtils.isEmpty(userP.getPassword())) {
				msg = messageSource.getMessage("lable.password.necessaire",
				        null, LocaleContextHolder.getLocale());

			}
			
			userP.setMessage(msg);
			return userP;

		}
		
		if (userP.getUserLogin().indexOf(" ") >= 0) {
			final String msg = messageSource.getMessage(
			        "label.user.login.not.valide", null,
			        LocaleContextHolder.getLocale());
			userP.setMessage(msg);
			return userP;

		}

		if (userP.isCreated()) {
			final Users user = userService.getUserByLogin(userP.getUserLogin());
			if (user != null) {
				final String msg = messageSource.getMessage("lable.exit.user",
				        null, LocaleContextHolder.getLocale());
				userP.setMessage(msg);
				return userP;

			}
		} else{
			
			 GlobalConf confG=globalService.getCurrentCode("CD");
		        Integer nextCOde=0;
		        String nextCOdeStr;
		        if(confG==null){
		        	confG=new GlobalConf();
		        	confG.setCode(("CD"));
		        	confG.setValue("1");
		        	nextCOde=1;
		        	
		        	
		        } 
		        
		        nextCOdeStr=confG.getValue();
		        nextCOde=Integer.parseInt(nextCOdeStr)+1;
		        if(nextCOde<=10){
		        	nextCOdeStr="0"+nextCOdeStr;	
		        }
		        confG.setValue(String.valueOf(nextCOde));		        
		        globalService.save(confG);
		        
		        confG=globalService.getCurrentCode("L");
		        if(confG==null){
		        	confG=new GlobalConf();
		        	confG.setCode("L");
		        	confG.setValue("A");
		        globalService.save(confG);     	
		        	
		        } 
		        nextCOdeStr=confG.getValue()+nextCOdeStr;
		        userP.setCode(nextCOdeStr);
		       
		}
		
		userP.setEnabled(1);
		userP.setTempUser(1);
		final UserRoles userRoles = new UserRoles();
		userRoles.setUser(userP);
		userRoles.setRole("ROLE_USER");
		userP.getRoles().add(userRoles);
		userP.setUserLogin(userP.getUserLogin().toUpperCase());
		userService.updateUser(userP);
		userP.setCreated(true);
		userP.setMessage("");
		
		return userP;

	}
	
	
	
	

}
