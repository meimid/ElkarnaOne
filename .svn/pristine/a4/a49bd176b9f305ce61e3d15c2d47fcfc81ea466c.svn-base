package ihmcredit;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.meimid.core.model.AccountsMovement;

public class UtilsConvert {

	public static AccountDetaillLight convert(final AccountsMovement accMvt) {

		final AccountDetaillLight l_AccountsMovementLight = new AccountDetaillLight();
		l_AccountsMovementLight.setDateOperation(accMvt.getDateOperation());
		l_AccountsMovementLight.setLibelle(accMvt.getLibelle());
		l_AccountsMovementLight.setMontantCredit(convert(accMvt
		        .getMontantCredit()));
		l_AccountsMovementLight.setMontantDebit(convert(accMvt
		        .getMontantDebit()));

		l_AccountsMovementLight.setAccountLibelle(accMvt.getAccount()
		        .getLibelle());
		l_AccountsMovementLight.setNumCompte(accMvt.getAccount()
		        .getNumAccount());
		return l_AccountsMovementLight;
	}

	public static List<AccountDetaillLight> convert(
	        final List<AccountsMovement> accMvtList) {
		final List<AccountDetaillLight> l_AccountsMovementLightList = new ArrayList<AccountDetaillLight>();
		if (!CollectionUtils.isEmpty(accMvtList)) {
			for (final AccountsMovement l : accMvtList) {
				l_AccountsMovementLightList.add(convert(l));
			}

		}

		return l_AccountsMovementLightList;

	}

	

	public static String convert(final Long id) {
		if (id == null) {
			return "";
		}
		return id.toString();
	}

	public static String convert(final Integer id) {
		if (id == null) {
			return "";
		}
		return id.toString();
	}

	public static String convert(final Double id) {
		if (id == null) {
			return "";
		}
		return id.toString();
	}

	
	
	public static String base64Encode(String token) {
	    byte[] encodedBytes = Base64.encode(token.getBytes());
	    return new String(encodedBytes, Charset.forName("UTF-8"));
	}


	
	
	
	public static String base64Encode(UserLight token) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper obj=new ObjectMapper();
		return base64Encode(obj.writeValueAsString(token));
		
	}

	
	public static boolean valideToken(UserLight token) throws JsonProcessingException {
		
		long currentTime = System.currentTimeMillis();
		if(currentTime-token.getStamp()>1800000)
			return false;
		return true;
		
		
	}

	public static UserLight base64Decode(String token) throws JsonParseException, JsonMappingException, IOException {
		
	    byte[] decodedBytes = Base64.decode(token.getBytes());
	    ObjectMapper obj=new ObjectMapper();
	    String str=new String(decodedBytes, Charset.forName("UTF-8"));
	    return obj.readValue(str ,UserLight.class);
	}
	
	public static UserLight UpdateUserLight(UserLight userL) throws JsonGenerationException, JsonMappingException, IOException{
		
		userL.setCode("ok");
		userL.setValue("");
		userL.setStamp(System.currentTimeMillis());
		userL.setValue(UtilsConvert.base64Encode(userL));
		 
		return  userL;	
		
	}
	
public  static void crossVlidation(  final HttpServletRequest request,final HttpServletResponse response) throws JsonProcessingException {
	
	
    //  UserLight userL=new UserLight();
		 response.addHeader("Access-Control-Allow-Origin", "*");
       response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
         response.addHeader("Access-Control-Allow-Credentials", "true");
         if(StringUtils.isNotEmpty( request.getHeader("Access-Control-Request-Headers")))
         response.addHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
         else
        	 response.addHeader("Access-Control-Allow-Headers", "*"); 
	
}
}
