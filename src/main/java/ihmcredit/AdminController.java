package ihmcredit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meimid.core.model.GlobalConf;
import com.meimid.core.model.HistoriqueUserExpireDate;
import com.meimid.core.model.UserRoles;
import com.meimid.core.model.Users;
import com.meimid.core.service.IGlobalConfService;
import com.meimid.core.service.IHistoriqueUserEpireDateService;
import com.meimid.core.service.IUserService;


@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin")
@CrossOrigin
public class AdminController extends AbstractClassBase {
	
	@Autowired
	IUserService	          userService;
	@Autowired
	IGlobalConfService	          globalService;
	@Autowired
	MessageSource	          messageSource;
	@Autowired
	IHistoriqueUserEpireDateService	          userExpireService;
	

	
	



	@RequestMapping(value = "/savemodifyUserP",
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

		if (StringUtils.isEmpty(userP.getUserLogin())
		        || StringUtils.isEmpty(userP.getPassword())
		        || StringUtils.isEmpty(userP.getPrimaryRole())) {
			String msg = "";
			if (StringUtils.isEmpty(userP.getUserLogin())) {
				msg = messageSource.getMessage("label.name.required", null,
				        LocaleContextHolder.getLocale());
			}
			if (StringUtils.isEmpty(userP.getPassword())) {
				msg = messageSource.getMessage("lable.password.necessaire",
				        null, LocaleContextHolder.getLocale());

			}
			if (StringUtils.isEmpty(userP.getPrimaryRole())) {
				msg = messageSource.getMessage("label.role.required", null,
				        LocaleContextHolder.getLocale());

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
		final UserRoles userRoles = new UserRoles();
		userRoles.setUser(userP);
		userRoles.setRole(userP.getPrimaryRole());
		userP.getRoles().add(userRoles);
		userP.setUserLogin(userP.getUserLogin().toUpperCase());
		userService.updateUser(userP);
		userP.setCreated(true);
		userP.setMessage("");
		
		return userP;

	}
	
	@RequestMapping(value = "/getUser",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Users getUser(HttpServletRequest request) throws JsonProcessingException
	{
	
		
	 final Users user = userService.getUserByLogin(request.getParameter("userLogin"));
	return  user;
	}
	
	
	
	@RequestMapping(value = "/AcctiveDesactiveUser",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Users desactiveUser(HttpServletRequest request) throws JsonProcessingException
	{
	
		
	 final Users user = userService.getUserByLogin(request.getParameter("userLogin"));
	 
	 if(user!=null){
		 Users userLoged= (Users) request.getAttribute("userLoged");
		 if(!user.getUserLogin().equals(userLoged.getUserLogin())){
			if(user.isEnabled()){
				 userService.udateUserStatus(user.getUserLogin(),2);
			} else {
				 userService.udateUserStatus(user.getUserLogin(),1);
			}
		 }
		
	 }
	return  user;
	}
	
	
	@RequestMapping(value = "/modifyPass",
	        headers = { "Content-type=application/json" },
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Users modifyUserPassword(@RequestBody final Users userP,final HttpServletRequest request) throws JsonProcessingException {
		if (userP == null || StringUtils.isEmpty(userP.getUserLogin())) {
			final String msg = "label.name.required";
			userP.setMessage(msg);
			return userP;

		}

		if (StringUtils.isEmpty(userP.getUserLogin())
		        || StringUtils.isEmpty(userP.getPassword())
		        || StringUtils.isEmpty(userP.getPrimaryRole())) {
			String msg = "";
			if (StringUtils.isEmpty(userP.getUserLogin())) {
				msg = "label.name.required";
			}
			if (StringUtils.isEmpty(userP.getPassword())) {
				msg = "lable.password.necessaire";

			}
			if (StringUtils.isEmpty(userP.getPrimaryRole())) {
				msg = "label.role.required";

			}

			userP.setMessage(msg);
			return userP;

		}
		if (userP.getUserLogin().indexOf(" ") >= 0) {
			final String msg =    "label.user.login.not.valide";
			userP.setMessage(msg);
			return userP;

		}
		

			final Users user = userService.getUserByLogin(userP.getUserLogin());
			user.setPassword(userP.getNewPassword());
			userService.updateUser(user);
				
			
			
			
		userP.setMessage("");
		
		return userP;

	}
	
	
	@RequestMapping(value = "/updateUserFromTemp",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Users updateTempUser(HttpServletRequest request) throws JsonProcessingException
	{
	
		
	 final Users user = userService.getUserByLogin(request.getParameter("userLogin"));
	 
	 if(user!=null){
		 Users userLoged= (Users) request.getAttribute("userLoged");
		 if(!user.getUserLogin().equals(userLoged.getUserLogin())){
			
			 user.setTempUser(0);
			 userService.saveUser(user);
		 }
		
	 }
	return  user;
	}
	
	@RequestMapping(value = "/forceTempUser",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Users forceTempUser(HttpServletRequest request) throws JsonProcessingException
	{
	
		
	 final Users user = userService.getUserByLogin(request.getParameter("userLogin"));
	 
	 if(user!=null){
		 Users userLoged= (Users) request.getAttribute("userLoged");
		 if(!user.getUserLogin().equals(userLoged.getUserLogin())){
			
			 user.setTempUser(1);
			 userService.saveUser(user);
		 }
		
	 }
	return  user;
	}
	
	@RequestMapping(value = "/stopUser",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Users stopUser(HttpServletRequest request) throws JsonProcessingException
	{
	
		
	 final Users user = userService.getUserByLogin(request.getParameter("userLogin"));
	 
	 if(user!=null){
		 Users userLoged= (Users) request.getAttribute("userLoged");
		 if(!user.getUserLogin().equals(userLoged.getUserLogin())){
			
			 user.setEnabled(0); 
			 userService.saveUser(user);
		 }
		
	 }
	return  user;
	}
	
	
	@RequestMapping(value = "/activeUser",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Users active(HttpServletRequest request) throws JsonProcessingException
	{
	
		
	 final Users user = userService.getUserByLogin(request.getParameter("userLogin"));
	 
	 if(user!=null){
		 Users userLoged= (Users) request.getAttribute("userLoged");
		 if(!user.getUserLogin().equals(userLoged.getUserLogin())){
			
			 user.setEnabled(1); 
			 userService.saveUser(user); 
		 }
		
	 }
	return  user;
	}
	
	
	
	@RequestMapping(value = "/updateUserExpireDate",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Users updateTempUserExpireDate(HttpServletRequest request) throws JsonProcessingException
	{
	
		final Users user = userService.getUserByLogin(request.getParameter("userLogin"));
		String sDate1=request.getParameter("expireDate");  
	    
	 if(user!=null && sDate1!=null){
		 
		 try {
		 Users userLoged= (Users) request.getAttribute("userLoged");
		 if(!user.getUserLogin().equals(userLoged.getUserLogin())){
			 
			 Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1); 
			
			 user.setTempUser(0);
			 int k=userService.updateUserUserExpireDate(user.getUserLogin(), date1);
			 if(k>0) {
				 HistoriqueUserExpireDate ht=new HistoriqueUserExpireDate();
				 ht.setUserLogin(user.getUserLogin());
				 ht.setExpiryDate(date1);
				 userExpireService.saveHistoriqueEpireDate(ht);
				 
			 }
			 }
			 
		 
		 }catch (Exception e) {
			// TODO: handle exception
		}
	 }
	return  user;
	}
	
	
	
	@RequestMapping(value = "/listHistoExpireDate",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<HistoriqueUserExpireDate> getHistoriqueUserExpireDate(HttpServletRequest request) throws JsonProcessingException
	{
	 final Users user = userService.getUserByLogin(request.getParameter("userLogin"));
	 if(user!=null ){
				return  userExpireService.getUserByLogin(user.getUserLogin());
		 }
	 		return  new ArrayList<HistoriqueUserExpireDate>();
	}
	
	
	@RequestMapping(value = "/listUserExpireWithInDate",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Users> getUserExpireWithinDate(HttpServletRequest request) throws JsonProcessingException
	{
		
		 String sDate1=request.getParameter("expireDate"); 
	
	 if(sDate1!=null ){
		 Date date1;
		try {
			date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
			return  userService.getAllUserExpireByDate(date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				
		 }
	 		return  new ArrayList<Users>();
	}
	
	
	
	@RequestMapping(value = "/updateUserStatus",
	        headers = { "Content-type=application/json" },
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody UserLight updateUserStatus(@RequestBody  UserLight userP)throws JsonProcessingException {
		
		System.out.println("calling updateUserStatus P"+userP.getUserLogin());
		System.out.println("calling updateUserStatus enabled"+userP.getEnabled());
		System.out.println("calling updateUserStatus P"+userP.getTempUser());
		System.out.println("calling updateUserStatus P"+userP.getExpiryDate());
		if (userP == null || StringUtils.isEmpty(userP.getUserLogin())) {
			final String msg = messageSource.getMessage("label.name.required",
			        null, LocaleContextHolder.getLocale());
			userP.setMessage(msg);
		}
		
		
		final Users user = userService.getUserByLogin(userP.getUserLogin());
		if (user != null) {
			
		//	user.setEnabled(userP.getEnabled());
			if(userP.getTempUser())
			user.setTempUser(1);
			else
			user.setTempUser(0);
			Date oldDate=user.getExpiryDate();
					
			user.setExpiryDate(userP.getExpiryDate());
			//user.setAExpiryDate(userP.getExpiryDate());
			userService.updateUser(user);
			if(oldDate!= null && oldDate.equals(userP.getExpiryDate()))
				saveUserUpdateDate(user,oldDate);
			userP.setMessage("");
		}
		
		
		return userP;
		
	}
	
	void saveUserUpdateDate(Users userP,Date oldDate) {
		 HistoriqueUserExpireDate ht=new HistoriqueUserExpireDate();
		 ht.setUserLogin(userP.getUserLogin());
		 ht.setExpiryDate(oldDate);
		 userExpireService.saveHistoriqueEpireDate(ht);
		
	}
	
	
	
	@RequestMapping(value = "/listUserByLogin",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Users> getUserByLogin(HttpServletRequest request) throws JsonProcessingException
	{
		
		 String userLogin=request.getParameter("userLogin"); 
	
	 if(!StringUtils.isEmpty(userLogin) ){
		 
			return  userService.getAllUserStrartBy(userLogin.toUpperCase());
		
				
		 }
	 		return  new ArrayList<Users>();
	}
	
	
	
	
	
}
