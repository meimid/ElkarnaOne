package ihmcredit;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meimid.core.dto.AccountDto;
import com.meimid.core.dto.AccountInfo;
import com.meimid.core.dto.AccountLight;
import com.meimid.core.dto.AccountsMovementLight;
import com.meimid.core.model.Account;
import com.meimid.core.model.Personne;
import com.meimid.core.model.UserRoles;
import com.meimid.core.model.Users;

@RestController
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequestMapping("/account")
@CrossOrigin
public class AccountController extends AbstractClassBase {
	
	
	//private static final String URLELKANA = "http://www.elkarna.com:8080/ElkarnaNet/";
	
	private static final String URLELKANA = "http://localhost:8080/ElkarnaNet/";
	
	 
	@RequestMapping(value = "/listAllAccounts", produces = MediaType.APPLICATION_JSON_VALUE)
	 @ResponseBody
	public List<AccountLight> getAllAccountJeson(final HttpServletRequest request ) throws JsonProcessingException {
		Users userLoged= (Users) request.getAttribute("userLoged");
		
		List<AccountLight> list = accountService.getAllAccountLight(userLoged.getUserLogin());

		
		return list;
	}
	
	
	@CrossOrigin
		@RequestMapping(value = "/listAllAccountsLigth", produces = MediaType.APPLICATION_JSON_VALUE)
		 @ResponseBody
		public List<AccountDto> getAllAccountLight(final HttpServletRequest request) throws JsonProcessingException {


		Users userLoged= (Users) request.getAttribute("userLoged");
			
			return accountService.getAllAccounDto(userLoged.getUserLogin());
		}
	
	
	@RequestMapping(value = "/saveAccount", consumes = "application/json",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Person saveAccount(@RequestBody final Person pers,final HttpServletRequest request)
	        throws UnsupportedEncodingException, JsonProcessingException

	{
	//	UtilsConvert.crossVlidation(request,response);
		
		if (pers == null || StringUtils.isEmpty(pers.getLibelle()) || StringUtils.isEmpty(pers.getLibelle().trim())  ) {
			final String error = messageSource.getMessage(
			        "label.name.required", null,
			        LocaleContextHolder.getLocale());
			pers.setMessage(error);
			return pers;

		}
		if (StringUtils.isEmpty(pers.getType())||StringUtils.isEmpty(pers.getType().trim()) ||!getTypeCompteCodeList().contains(pers.getType())  ) {
			final String error = messageSource.getMessage(
			        "type.account.required", null,
			        LocaleContextHolder.getLocale());
			pers.setMessage(error);
			return pers;

		}
		
		Users userLoged= (Users) request.getAttribute("userLoged");
		
		pers.setMessage("");
		pers.setLibelle(pers.getLibelle().trim().replaceAll("\\s+", " "));

		// verify if name does not exist
		String l_numCompte = pers.getNumCompte();
		final String l_numPersonne = pers.getNumPersonne();

		if (StringUtils.isEmpty(l_numPersonne)) {

			final List<Account> lListAccount = accountService
			        .getAccountByExacteName(pers.getLibelle(),userLoged.getUserLogin());
			if (!org.springframework.util.CollectionUtils.isEmpty(lListAccount)) {
				final String error = messageSource.getMessage(
				        "label.exist.account.with.samename", null,
				        LocaleContextHolder.getLocale());
				pers.setMessage(error);
				return pers;
			}
			if (!StringUtils.isEmpty(l_numCompte)) {
				l_numCompte = userLoged.getCode()+pers.getNumCompte().trim()
				        .replaceAll("\\s+", " ");
				final Account lc = accountService.getAccountByNum(l_numCompte);
				if (lc != null) {
					final String error = messageSource.getMessage(
					        "label.existe.compte.meme.numero", null,
					        LocaleContextHolder.getLocale());
					pers.setMessage(error);
					return pers;

				}
			}
		}

		final Personne newPers = new Personne();
		newPers.setNom(pers.getLibelle());
		newPers.setAdresse(pers.getAdresse());
		newPers.setNumTelFixe(pers.getTel());
		newPers.setNumTelCell(pers.getTel());
		newPers.setEmail(pers.getEmail());
		newPers.setUser(userLoged);
		final Account account = new Account();
		account.setNumAccount(l_numCompte);
		
		new String(pers.getLibelle().getBytes("8859_1"), "UTF8");
		account.setLibelle(pers.getLibelle());
		// account.setLibelle(libInter
		account.setUser(userLoged);
		account.setTypeCompte(pers.getType());
		
		account.setPerson(newPers);
		
		if (StringUtils.isEmpty(l_numPersonne)) {
			accountService.saveAccount(account);
		} else {

			accountService.updateAccount(account);
		}
		pers.setNumCompte(account.getNumAccount());
		pers.setNumPersonne(newPers.getNumPersonne().toString());
	
		return pers;

	}
	
	
	@RequestMapping(value = "/updatePersonne", consumes = "application/json",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Person updatePersonneAccount(@RequestBody final Person pers,final HttpServletRequest request)
	        throws UnsupportedEncodingException, JsonProcessingException

	{
	//	UtilsConvert.crossVlidation(request,response);
		
		if (pers == null || StringUtils.isEmpty(pers.getLibelle()) || StringUtils.isEmpty(pers.getLibelle().trim())) {
			final String error = messageSource.getMessage(
			        "label.name.required", null,
			        LocaleContextHolder.getLocale());
			pers.setMessage(error);
			return pers;

		}
		if (StringUtils.isEmpty(pers.getNumCompte())||StringUtils.isEmpty(pers.getNumCompte().trim()) ) {
			final String error = messageSource.getMessage(
			        "lable.compte.not.exit", null,
			        LocaleContextHolder.getLocale());
			pers.setMessage(error);
			return pers;

		}
		
		Users userLoged= (Users) request.getAttribute("userLoged");
		pers.setMessage("");
		pers.setLibelle(pers.getLibelle().trim().replaceAll("\\s+", " "));
		// verify if name does not exist
		String l_numCompte = pers.getNumCompte();
    
				final Account lc = accountService.getAccountByNum(l_numCompte);
				if (lc == null ) {
					final String error = messageSource.getMessage(
					        "lable.compte.not.exit", null,
					        LocaleContextHolder.getLocale());
					pers.setMessage(error);
					return pers;

				}
				
				
			
		

		Personne newPers =personneService.findPersonnedByNum(lc.getPerson().getNumPersonne()) ;
		//newPers.setUser(userLoged);
		
		if(!pers.getLibelle().equals(lc.getLibelle()))
		{
			final List<Account> lListAccount = accountService
			        .getAccountByExacteName(pers.getLibelle(),userLoged.getUserLogin());
			if (!org.springframework.util.CollectionUtils.isEmpty(lListAccount)) {
				final String error = messageSource.getMessage(
				        "label.exist.account.with.samename", null,
				        LocaleContextHolder.getLocale());
				pers.setMessage(error);
				return pers;
			}
			
			lc.setLibelle(pers.getLibelle());	
			accountService.updateAccountLibelle(lc.getNumAccount(), lc.getLibelle());
		}
		
			try {
			 personneService.udatePersonneInfo(lc.getPerson().getNumPersonne(), pers.getLibelle(), pers.getTel(),pers.getEmail(),pers.getAdresse());
			}
			catch(Exception e ){
				System.out.println("Exception Updating Personne " + e.getMessage());
				pers.setMessage(messageSource.getMessage(
				        "label.operation.not.saved", null,
				        LocaleContextHolder.getLocale()));

				e.printStackTrace();
			}
		
		
		
		return pers;

	}
	
	
	
	
	
	
	
	
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
			System.out.println("elapsedTimeMillis verif " + elapsedTimeMillis
			        + " sn");
			if (account != null) {
				start = System.currentTimeMillis();
				final List<AccountsMovementLight> l_list = accountService
				        .getAccountsMovementLightByNameAccount(l_numCompte,
				                l_fromDate, l_toDate);
				elapsedTimeMillis = System.currentTimeMillis() - start;
				//System.out.println(" elapsedTimeMillis account "				        + elapsedTimeMillis + " sn");
				final MvtBean mvt = new MvtBean();
				mvt.setListMvt(l_list);
				start = System.currentTimeMillis();
				mvt.setSolde(accountService.getAccountBalance(account
				        .getNumAccount()));
				//elapsedTimeMillis = System.currentTimeMillis() - start;
				//System.out.println("elapsedTimeMillis sold "        + elapsedTimeMillis + " sn");
				// Get elapsed time in seconds
				return mvt;

			} 

		}
		return null;
	}
	
	@RequestMapping(value = "/solde", consumes = "application/json",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	InfoBean soldeAccount(Compte operation) {
		
		final String numAccount =operation.getNumCompte();// 
		//String str=	request.getParameter("numAccount");
		//String str=	request.getParameter("numCompte");

		final InfoBean inf = new InfoBean();
		final Account ac = accountService.getAccountByNum(numAccount);
		if (ac != null) {
			ac.setLibSolde(messageSource.getMessage("label.rest.mvt", null,
			        LocaleContextHolder.getLocale()));

			//accountsMovementService.soldeCompte(ac);
			accountService.soldeCompte(ac);
			inf.setCode("0");
		}
		return inf;

	}
	
	
	@RequestMapping(value = "/soldeCompte",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	InfoBean soldeCompte( @RequestParam(required = true) final String  numCompte,final HttpServletRequest request) throws JsonProcessingException {
		final InfoBean inf = new InfoBean();
		final Account ac = accountService.getAccountByNum(numCompte);
		if (ac != null) {
			ac.setLibSolde(messageSource.getMessage("label.rest.mvt", null,
			        LocaleContextHolder.getLocale()));
			Users userLoged= (Users) request.getAttribute("userLoged");
			//accountsMovementService.soldeCompte(ac);
			accountService.soldeCompte(ac);
			inf.setCode("0");
		}
		return inf;
		
		
	}
	
	@RequestMapping(value = "/listAllAccountDebit",  produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	List<AccountInfo> getAllAccountDebit(final ModelMap model,
	        final HttpServletRequest request) throws JsonProcessingException {
		List<AccountInfo> l_list;
		final List<String> l_types = new ArrayList();
		l_types.add("CCLIENT");
		l_types.add("CFOURNISSEUR");
		//l_types.add(AccountCodeEnums.CDEPOT.getCode());
		final String l_debit = request.getParameter("credit");
		long l_some = 0;
		Users userLoged= (Users) request.getAttribute("userLoged");
		if ("d".equals(l_debit)) {
			l_list = accountService.getAllAccountsNegatif(l_types,userLoged.getUserLogin());
			for (final AccountInfo l_acount : l_list) {
				l_some += l_acount.getBalance();
			}
			

			
		} else {

			l_list = accountService.getAllAccountsPositif(l_types,userLoged.getUserLogin());
			for (final AccountInfo l_acount : l_list) {
				l_some += l_acount.getBalance();
			}
			

			
		}
		return l_list;
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
		Users userLoged= (Users) request.getAttribute("userLoged");
		if(!userP.getUserLogin().equals(userLoged.getUserLogin())){
			final String msg =    "label.user.mot.allowed.to.change.password";
			userP.setMessage(msg);
			return userP;			
		}

			final Users user = userService.getUserByLogin(userP.getUserLogin());
			if(user.getPassword().equals(userP.getCurrentPassword())){
				user.setPassword(userP.getNewPassword());
				userService.updateUser(user);
				
			}
			else {
				userP.setMessage("old.paaword.invalide");
				return userP;
				
			}
			
			
		userP.setMessage("");
		
		return userP;

	}
	
	
	
	
	
	
	
	@RequestMapping(value = "/listAllUser",  produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	 List<Users> getAllUser( final HttpServletRequest request) {
		return  userService.getAllUser();
		
	}
	
	
	@RequestMapping(value = "/accountDetailHisto",
	        consumes = "application/json",
	        produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public MvtBean getAccountDetailHisto(final ModelMap model,

	@RequestBody final OperationBean operation) throws JsonProcessingException {
		final String l_numCompte = operation.getNumCompte();

		final Date l_fromDate = operation.getFrom();
		final Date l_toDate = operation.getTo();

		if (!StringUtils.isEmpty(l_numCompte)) {
			System.currentTimeMillis();
			final AccountDto account = accountService
			        .getAccountLightByNumCompte(l_numCompte);

			if (account != null) {
				System.currentTimeMillis();
				final List<AccountsMovementLight> l_list = accountService
				        .getAccountsMovementLightHistoByNameAccount(
				                l_numCompte, l_fromDate, l_toDate);
				final MvtBean mvt = new MvtBean();
				mvt.setListMvt(l_list);
				mvt.setSolde(accountService.getAccountBalance(account
				        .getNumAccount()));
				mvt.setSolde(0l);
				// elapsedTimeMillis = System.currentTimeMillis() - start;
				// System.out.println("elapsedTimeMillis sold "
				// + elapsedTimeMillis + " sn");
				// Get elapsed time in seconds
				return mvt;

			} else {
				model.put("error", messageSource.getMessage(
				        "lable.compte.not.found", null,
				        LocaleContextHolder.getLocale()));
			}

		}
		return null;
	}

	@RequestMapping(value = "/createOnlineAccount",
	        consumes = "application/json",
	        produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public String createOnlieAccount(@RequestBody final OperationBean operation,final HttpServletRequest request) throws JsonProcessingException {
		final String l_numCompte = operation.getNumCompte();
		final AccountDto account = accountService
		        .getAccountLightByNumCompte(l_numCompte);
		Users userLoged= (Users) request.getAttribute("userLoged");
		if (account != null) {
			HttpURLConnection con =null;
			
			 UserLight usel=getUser(userLoged);
				  if(usel!=null && !StringUtils.isEmpty(usel.getValue())){
					  UserLogin newUser=getNewUser();
					newUser.setUserLogin(account.getNumAccount());
					newUser.setName(account.getLibelle());
					newUser.setCode(userLoged.getUserLogin());
					
					try{
				    con =getHttpURLConnection( URLELKANA+"online/savemodifyUserP");
				    con.setRequestProperty("Authorization", usel.getValue());
				    ObjectMapper mapper = new ObjectMapper();
				    DataOutputStream out = new DataOutputStream(con.getOutputStream());
					mapper.writeValue(out,newUser);
					out.flush();
					out.close();
					int status = con.getResponseCode();
					if(status==200)
					{
						BufferedReader in = new BufferedReader(
								  new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
								
						StringBuffer content = new StringBuffer();
						String inputLine;
								while ((inputLine = in.readLine()) != null) {
									
								    content.append(inputLine);
								}
								in.close();
								
								con.disconnect();
						return content.toString();
					}
					
					
				  }
					
					
					catch(Exception ex){
						con.disconnect();
					}
					finally {
						con.disconnect();
					}
					
				
					
				}
				  
				  
				  
				  
				  
			
		}
		
		return "KO";
	}
	
	
	HttpURLConnection getHttpURLConnection(String urlstr) throws IOException{
		
		HttpURLConnection con =null;
		
		
		
		//	URL url = new URL("http://www.elkarna.com:8080/ElkarnaOne/welcome/token");
			URL url= new URL(urlstr);
			
				con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");			
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			return con;

	}
	UserLight getUser(Users userLoged){
		HttpURLConnection con =null;
		UserLight usel=null;
		try{
		//	
			
			
			con =getHttpURLConnection(URLELKANA+"welcome/token");
			con.setRequestMethod("POST");				
			Map<String, String> parameters = new HashMap();
			parameters.put("userLogin", "ONLINE");
			parameters.put("password", "ONLINE");
			ObjectMapper mapper = new ObjectMapper();
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			mapper.writeValue(out, parameters);
			out.flush();
			out.close();
			int status = con.getResponseCode();
			if(status==200){
			
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {						
					    content.append(inputLine);
					}
					in.close();
			
					usel=mapper.readValue(content.toString(), UserLight.class);
					con.disconnect();
					
					return usel;
			
			}
			}
			catch (Exception e) {
				con.disconnect();
			}
			finally{
				con.disconnect();
				}
		return usel;
			}
	
	
	
	UserLogin getNewUser(){
		UserLogin newUser=new UserLogin();
		//elk
		newUser.setPassword("bfe03036f7ec78b9ae3aefa7e8716f30");
		newUser.setPrimaryRole("ROLE_USER");
		
		return newUser;
	}
	
	
	
	
	@RequestMapping(value = "/loadPersonne", produces = MediaType.APPLICATION_JSON_VALUE)
	 @ResponseBody
	public Person getPersonne(final HttpServletRequest request ) throws JsonProcessingException {
	   
		Person loadPersnone=new Person();
		String numCompte=request.getParameter("numCompte");
		if(!StringUtils.isEmpty(numCompte)) {
		 Account ac=accountService.getAccountByNum(numCompte);
		 if(ac!=null) {
			 
			 loadPersnone.setLibelle(ac.getLibelle());
			 loadPersnone.setAdresse(ac.getPerson().getAdresse());
			 loadPersnone.setTel(ac.getPerson().getNumTelCell());
			 loadPersnone.setEmail(ac.getPerson().getEmail());
			 loadPersnone.setAdresse(ac.getPerson().getAdresse());
			 loadPersnone.setNumCompte(numCompte);
			 
			 
			 
			 }
		 }
		 
				return loadPersnone;
	}
	
	
	
	
	@RequestMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile( HttpServletRequest request) {
        // Load file as Resource
	 String numAccount=request.getParameter("numAccount");
	 loadData(numAccount);
	 //String fileName=fileStorageProperties.getUploadDir()+File.separator+"book_new.csv";
	 //String fileName=System.getProperty("user.home")+File.separator+"book_new.csv";
	 String fileName=System.getenv("tempDire")+File.separator+numAccount+".csv";
	
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
          //  logger.info("Could not determine file type.");
            System.out.println("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	 void loadData(String numAccount) {
			// String fileName=fileStorageProperties.getUploadDir()+File.separator+"book_new.csv";
			 //String fileName=System.getProperty("user.home")+File.separator+"book_new.csv";
			 String fileName=System.getenv("tempDire")+File.separator+numAccount+".csv";
			 File myFile=new File(fileName);
			 CSVPrinter printer = null;
			try {
				 if(myFile.exists()){
					 myFile.delete();
					}
				 myFile.createNewFile();
				PrintWriter outln = new PrintWriter(myFile, "UTF-8");
				outln.print('\ufeff');
				Account account=accountService.getAccountByNum(numAccount);
				 if (account != null) {
					 final List<AccountsMovementLight> l_list = accountService
						        .getAccountsMovementLightByNameAccount(numAccount,
						                null, null);
						 printer = CSVFormat.DEFAULT.withDelimiter(';').print(outln);		
							 printer.printRecord("","","","الحساب "+account.getLibelle() +" "+ numAccount);
						 Long soldI=accountService.getAccountBalance(account
							        .getNumAccount());
								 if(soldI>0) {
									 printer.printRecord("","",""," له   "+soldI+ " " );
								 }else {
									 printer.printRecord("","","","عليه   "+soldI+ " " );
								 }
								 printer.printRecord("","",""," " );
								 printer.printRecord("","",""," " );
								 printer.printRecord( "عليه"," له"," التفاصيل" ," التاريخ");
						 //System.out.println(" elapsedTimeMillis account "				        + elapsedTimeMillis + " sn");
						 for(AccountsMovementLight ac:l_list ) {
							 printer.printRecord(ac.getMontantDebit(),ac.getMontantCredit(),ac.getRemarque(), ac.getDatAS());
						 }
					} 
				
				 outln.flush();
				 outln.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	 }
	 
	 
	 
	 @RequestMapping("/downloadPDFFile")
	    public ResponseEntity<Resource> downloadPDFFile( HttpServletRequest request) {
	        // Load file as Resource
		 String numAccount=request.getParameter("numAccount");
		 loadPDFdData(numAccount);
		 //String fileName=fileStorageProperties.getUploadDir()+File.separator+"book_new.csv";
		 //String fileName=System.getProperty("user.home")+File.separator+"book_new.csv";
		 String fileName=System.getenv("tempDire")+File.separator+numAccount+".pdf";
		
	        Resource resource = fileStorageService.loadFileAsResource(fileName);

	        // Try to determine file's content type
	        String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	        } catch (IOException ex) {
	          //  logger.info("Could not determine file type.");
	            System.out.println("Could not determine file type.");
	        }

	        // Fallback to the default content type if type could not be determined
	        if(contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
	    }
	 
	 
	 
	 void loadPDFdData(String numAccount) {
		 
		 
			
			// String fileName=fileStorageProperties.getUploadDir()+File.separator+"book_new.csv";
			// String fileName=System.getProperty("user.home")+File.separator+"book_new.pdf";
			 String fileName=System.getenv("tempDire")+File.separator+numAccount+".pdf";
			// String fileName=System.getenv("tempDire")+File.separator+numAccount+".csv";
			 
			 File myFile=new File(fileName);
			
			
			 try {
		            Document document = new Document();
		            PdfWriter lpr=  PdfWriter.getInstance(document, new FileOutputStream(myFile));
		            lpr.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		            document.open();
		            //addMetaData(document);
		            //addTitlePage(document);
		            //addContent(document);
		        	//writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		            getEtatDuCompte(document,numAccount);
		            
	                if(document!=null)
	                {
		              document.close();
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			 
			 
			 
			 
		
		    
		 }
	
	
	  void getEtatDuCompte( final Document doc, String numCompte) throws DocumentException,
      IOException {
	Account account=accountService.getAccountByNum(numCompte);
	
	if(account!=null) {
	final PdfPTable table = new PdfPTable(4);
	table.setWidthPercentage(100.0f);
	table.setWidths(new float[] { 3.0f, 2.0f, 2.0f, 2.0f });
	table.setSpacingBefore(10);
	final BaseFont bf = BaseFont.createFont("trado.ttf",
	        BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

	Font font = new Font(bf, 15, 0);
	font.setColor(BaseColor.BLACK);
//	writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	final PdfPTable tbl = new PdfPTable(1);
	String label= messageSource.getMessage("ETATCOMPT", null,
	        LocaleContextHolder.getLocale());
	   Paragraph preface = new Paragraph(label);
	  //preface.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	   PdfPCell cell = getPdfPCellNoBorder(preface);
	
	   
	   PdfPCell cell2 =getPdfPCellNoBorder(preface);
		//cell2.addElement(new Paragraph(label, font));
		//cell2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		tbl.addCell(cell2);
		cell2 = new PdfPCell();
		cell2.addElement(new Paragraph(label+ " "+account.getLibelle()
		        + "   " + account.getNumAccount(), font));
		cell2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		cell2.setPaddingBottom(8);
		cell2.setBorder(Rectangle.NO_BORDER);
		tbl.addCell(cell2);
		cell2 = new PdfPCell();
		 label= messageSource.getMessage("label.credit", null,
			        LocaleContextHolder.getLocale());
	
		 
		 Long soldI=accountService.getAccountBalance(account
			        .getNumAccount());
				 if(soldI>0) {
					 label+="  "+soldI+ " " ;
						font.setColor(BaseColor.BLUE);
				 }else {
					 
					 label= messageSource.getMessage("label.debit", null,
						        LocaleContextHolder.getLocale());
						font.setColor(BaseColor.RED);
					 label+="  "+soldI+ " " ;
				 }
		 
		    cell2.addElement(new Paragraph(label,font));
			cell2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell2.setPaddingBottom(8);
			cell2.setBorder(Rectangle.NO_BORDER);
			tbl.addCell(cell2);
		doc.add(tbl);
		
	   font = new Font(bf, 15, 0);
		font.setColor(BaseColor.WHITE);
				cell = new PdfPCell();
	cell.setBackgroundColor(BaseColor.BLUE);
	cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	// if (l_not_personelAccount) {
	cell.setPadding(4);
	// } else {
	// cell.setPadding(3);
	// // write table header
	// }
	// if (l_not_personelAccount) {
	ConstantPDFTrans lConstantPDFTrans=getPDFConstatEtatCompte();
	cell.setPhrase(new Phrase(lConstantPDFTrans.getMtDebit(), font));
	// cell.setPhrase(new Phrase("Published Date", font));
	table.addCell(cell);

	// cell.setPhrase(new Phrase("ISBN", font));
	cell.setPhrase(new Phrase(lConstantPDFTrans.getMtcredit(), font));
	table.addCell(cell);

	// cell.setPhrase(new Phrase("Author", font));
	cell.setPhrase(new Phrase(lConstantPDFTrans.getMtdetail(), font));
	table.addCell(cell);

	cell.setPhrase(new Phrase(lConstantPDFTrans.getDate(), font));
	table.addCell(cell);

	font = new Font(bf, 12, 0);
	font.setColor(BaseColor.BLACK);
//writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	//cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	cell.setBackgroundColor(BaseColor.WHITE);
	Double sommeIN=0.0;
	Double sommeOut=0.0;
	
	String sommeINStr="";
	String sommeOutStr="";

	 if (account != null) {
		 final List<AccountsMovementLight> l_list = accountService
			        .getAccountsMovementLightByNameAccount(numCompte,
			                null, null);
		 
		 
		 for (final AccountsMovementLight aBook : l_list) {

				if (aBook.getMontantDebit() != null) {
					
					//table.addCell(aBook.getMontantDebit().toString());
					cell.setPhrase(new Phrase(aBook.getMontantDebit().toString(), font));
					table.addCell(cell);
					sommeOut+=aBook.getMontantDebit();
					
				} else {
					table.addCell("");
				}
				if (aBook.getMontantCredit() != null) {
				//	table.addCell(aBook.getMontantCredit().toString());
					
					cell.setPhrase(new Phrase(aBook.getMontantCredit().toString(), font));
					table.addCell(cell);
					sommeIN+=aBook.getMontantCredit();
				} else {
					table.addCell("");
				}
				 PdfPCell pdfCell = new PdfPCell(new Phrase(
				        aBook.getRemarque(), font));
				pdfCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				pdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				// table.addCell(new Phrase(aBook.getLibelle(), font));
				//table.addCell(pdfCell);
				
				cell.setPhrase(new Phrase(aBook.getRemarque(), font));
				table.addCell(cell);
				
				
			
				// table.addCell(new Phrase(aBook.getLibelle(), font));
				//table.addCell(pdfCell);
				//table.addCell();
				cell.setPhrase(new Phrase(aBook.getDatAS(), font));
				table.addCell(cell);

				// table.addCell(String.valueOf(aBook.getPrice()));
			}
		 
		 
		 label= messageSource.getMessage("lable.some", null,
			        LocaleContextHolder.getLocale());
		 if(sommeIN>0)
		 {
			sommeINStr=sommeIN.toString(); 
		 }
		 
		 if(sommeOut>0)
		 {
			sommeOutStr=sommeOut.toString(); 
		 }
		  
			cell.setPhrase(new Phrase(sommeOutStr, font));
			table.addCell(cell);
			cell.setPhrase(new Phrase(sommeINStr, font));
			table.addCell(cell);
			cell.setPhrase(new Phrase(label, font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("", font));
			table.addCell(cell);		 
		 
		 
		 
		 }
	
	


	

	doc.add(table);
	}
}



ConstantPDFTrans getPDFConstatEtatCompte() {
	final ConstantPDFTrans lConstantPDFTrans = new ConstantPDFTrans();
	final String date = messageSource.getMessage("label.date", null,
	        LocaleContextHolder.getLocale());
	final String detail = messageSource.getMessage("label.detail", null,
	        LocaleContextHolder.getLocale());
	final String debit = messageSource.getMessage("label.debit", null,
	        LocaleContextHolder.getLocale());
	final String credit = messageSource.getMessage("label.credit", null,
	        LocaleContextHolder.getLocale());
	final String parg = messageSource.getMessage("label.compte.etat", null,
	        LocaleContextHolder.getLocale());
	final String numcopte = messageSource.getMessage("label.numcompte",
	        null, LocaleContextHolder.getLocale());
	final String label = messageSource.getMessage("label.name", null,
	        LocaleContextHolder.getLocale());

	lConstantPDFTrans.setDate(date);

	lConstantPDFTrans.setMtcredit(credit);
	lConstantPDFTrans.setMtdetail(detail);
	lConstantPDFTrans.setMtDebit(debit);
	lConstantPDFTrans.setParagraph(parg);
	lConstantPDFTrans.setNumCpmteLable(numcopte);
	lConstantPDFTrans.setCompteLable(label);
	return lConstantPDFTrans;

}
	

private PdfPCell getPdfPCellNoBorder(Paragraph paragraph) {
    PdfPCell cell = new PdfPCell();
    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
    cell.setPaddingBottom(8);
    cell.setBorder(Rectangle.NO_BORDER);
    cell.addElement(paragraph);
    return cell;
}

}
