package ihmcredit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;







import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.meimid.core.service.IUserService;
import org.apache.log4j.*;

@Component
@CrossOrigin
public class DiffterFilter  extends GenericFilterBean {
	static final String ORIGIN = "Origin" ;
	//static final String ORIGIN = "*" ;
	@Autowired
	IUserService	          userService;
	private static Logger logger = Logger.getLogger(DiffterFilter.class);
	

	static Map<String,String> mapAI=new HashMap<String, String>();

	
	//@Override
		public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
			
			HttpServletRequest request=	(HttpServletRequest) req;
			HttpServletResponse response=(HttpServletResponse) res;
			
			
		 //response.addHeader("Access-Control-Allow-Origin", "*");
			 /* String origin = request.getHeader(ORIGIN);
			  response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Headers",
	       request.getHeader("Access-Control-Request-Headers"));*/
	        if ("OPTIONS".equals(request.getMethod())) {
	        	if ("*".equals(request.getHeader(ORIGIN))||"http://localhost:4200".equals(request.getHeader(ORIGIN))) {
		      }
	        	//response.addHeader("Access-Control-Allow-Origin", "*");
	        	UtilsConvert.crossVlidation(request,response);
	            response.getWriter().print("OK");
	            response.getWriter().flush();
	            return;
	        }
	        
	        Cookie cookies=WebUtils.getCookie(request, "JWTSESSIONID");
	        
			
			 
				String userJson=request.getHeader("Authorization");
			
				if(cookies!=null && cookies.getValue()!=null){
					userJson=mapAI.get(cookies.getValue());
					logger.info("Token is gettig JWTSESSIONID  ");
				}
				
		if(StringUtils.isNoneEmpty(userJson)){
			
				
//				if(session!=null && session.getAttribute("token")!=null){
//				
//				
//				//ObjectMapper mapper = new ObjectMapper();
//				response.addHeader("Access-Control-Allow-Origin", "*");
//				User userFormSession=(User) session.getAttribute("token");
//				SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userFormSession));
//				request.setAttribute("userLoged",userFormSession );

				//JSON from String to Objectt
			try{
				
				
				UserLight obj = UtilsConvert.base64Decode(userJson);
				if(UtilsConvert.valideToken(obj)){
					UtilsConvert.UpdateUserLight(obj);	
					
					SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userService.getUserByLogin(obj.getUserLogin().toUpperCase())));
					response.addHeader("Access-Control-Allow-Origin", "*");
					response.addHeader("currentUser", obj.getValue());
					response.addHeader("Access-Control-Expose-Headers","currentUser");
				    request.setAttribute("userLoged",userService.getUserByLogin((obj.getUserLogin().toUpperCase()))) ;
				    mapAI.put(obj.getUserLogin(),obj.getValue());
					
				}
				}
			catch(JsonParseException e){
				logger.error("Error parsing token "+userJson);
				SecurityContextHolder.getContext().setAuthentication(null);
				
			}
       catch(Exception e){
    	  
    	   logger.error("Error  token "+userJson);
				SecurityContextHolder.getContext().setAuthentication(null);
				response.addHeader("currentUser","not valide Tokend");
			}
		}
				
			else {
				logger.error("Token is EMpty ");
				SecurityContextHolder.getContext().setAuthentication(null);
				
			}
		
			
			chain.doFilter(req, res); // toujours continuer
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		public IUserService getUserService() {
			return userService;
		}
		public void setUserService(IUserService userService) {
			this.userService = userService;
		}

		public ResponseEntity handle() {
	        return new ResponseEntity(HttpStatus.OK);
	    }
}
