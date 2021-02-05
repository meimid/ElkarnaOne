package ihmcredit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.meimid.core.model.Users;
import com.meimid.core.service.IUserService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
@RequestMapping("/welcome")
//@CrossOrigin
public class WelcomeController {

	@Autowired
	private MessageSource	     messageSource;
	@Autowired
	private IUserService userService;
	
	

	
	@RequestMapping(value = "/token",consumes = { "application/json" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserLight getAccountJeson(@RequestBody final UserLight userL,
	        final HttpServletRequest request,final HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		
UtilsConvert.crossVlidation(request,response);
		
		
		if(userL!=null&&StringUtils.isNoneEmpty(userL.getUserLogin())&&StringUtils.isNoneEmpty(userL.getPassword())){
			Users user=userService.getUserByLogin(userL.getUserLogin().toUpperCase());
			
				
				
			
			if(user!=null &&  user.getPassword().equals(userL.getPassword()) && user.isEnabled() ){

				if( user.isTempUser()) {
					//long currentTime = System.currentTimeMillis();
					
					Date dt=user.getDateCreation();
					LocalDate oneMonthsAgo = LocalDate.now().plusMonths(-1); 
					if( user.getExpiryDate() != null)
					{
						 dt=user.getExpiryDate();
						 oneMonthsAgo = LocalDate.now().plusDays(1); 
					}
					//LocalDate currentDate = LocalDate.now();
										
					LocalDate date2  = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if(date2.isBefore(oneMonthsAgo))
					{
						
						userL.setCode("ko");
						userL.setExpire(1);	
						return  userL;
					}
			        
				}
				else
				{
				if( user.getExpiryDate() != null) {
					//long currentTime = System.currentTimeMillis();
					Date dt=user.getExpiryDate();					
					//LocalDate currentDate = LocalDate.now();
					LocalDate oneMonthsAgo = LocalDate.now().plusDays(2); 					
					LocalDate date2  = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if(date2.isBefore(oneMonthsAgo))
					{
						
						userL.setCode("ko");
						userL.setExpire(1);	
						return  userL;
					}
			        
				}
				}
				int len=user.getName().length();
				if(len>11)
				len=11;
				userL.setName(user.getName().substring(0,len));
				UserLight userll=UtilsConvert.UpdateUserLight(userL);
				 response.addCookie(new Cookie("JWTSESSIONID", userL.getUserLogin()));
				 DiffterFilter.mapAI.put(userll.getUserLogin(),userll.getValue());
				return userll;// UtilsConvert.UpdateUserLight(userL);
				 
			
				
				
			}
					
			
		}
		userL.setCode("ko");
		return  userL;
		
		}
	

}
