package ihmcredit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.Headers;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meimid.core.dto.AccountDto;
import com.meimid.core.dto.AccountsMovementLight;
import com.meimid.core.model.Account;
import com.meimid.core.model.GlobalConf;
import com.meimid.core.model.UserPassRest;
import com.meimid.core.model.UserRoles;
import com.meimid.core.model.Users;
import com.meimid.core.service.IGlobalConfService;
import com.meimid.core.service.IUserResetPassService;
import com.meimid.core.service.IUserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.result.Output;
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
	@Autowired
    private JavaMailSender emailSender;
	@Autowired
	IUserResetPassService userPassService;
	
	 private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
	            Font.BOLD);
	    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
	            Font.NORMAL, BaseColor.RED);
	    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
	            Font.BOLD);
	    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
	            Font.BOLD);
	    public static final String FONT = "NotoNaskhArabic-Regular.ttf";
	    public static final String ARABIC = "\u0627\u0644\u0633\u0639\u0631";
	
	@RequestMapping(value = "/registreNewUser",
	        headers = { "Content-type=application/json" },
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Users saveUser(@RequestBody final Users userP) throws JsonProcessingException {
		if (userP == null || StringUtils.isEmpty(userP.getUserLogin()) || StringUtils.isEmpty(userP.getUserLogin().trim())) {
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
		        
		        SimpleMailMessage message = new SimpleMailMessage();
			    message.setFrom("donotreplay@elkarna.com");
			    message.setTo(userP.getEmail());
		        message.setSubject("  مرحبا بك في برنامج الكرن ");
		        message.setText( "  مرحبا بك في برنامج الكرن. بإمكانك تجربة البرنامج لمدة  شهر مجانا  ");
		       // message.setSubject("  Test sub");
		        //message.setText( "this test ");
		        try {
		        emailSender.send(message);
		        }
		        catch(Exception e) {
		        	
		        	System.out.println("------------------- ");
		         	System.out.println("Erreur sending email "+e.getMessage());
		        	
		        }
		        
		        
		        
		        	
		        } 
		        nextCOdeStr=confG.getValue()+nextCOdeStr;
		        userP.setCode(nextCOdeStr);
		       
		}
		
		userP.setEnabled(1);
		userP.setTempUser(1);
		//LocalDate oneMonthsAgo = LocalDate.now().plusMonths(-1); 		
		userP.setExpiryDate(DateUtils.addMonths(new Date(), 1));
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
	
	
	@RequestMapping(value = "/sendeNewPass",
	        headers = { "Content-type=application/json" },
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Boolean sendEmailr(@RequestBody final Users userP) throws JsonProcessingException {
		Users user=null;
		if(!StringUtils.isEmpty(userP.getEmail())) {
			user= userService.findUserByEmail(userP.getEmail());
			 
		}else {
			
			if(!StringUtils.isEmpty(userP.getUserLogin())) {
				user=userService.getUserByLogin(userP.getUserLogin().toUpperCase());	
			}
		}
		
		if(user!=null) {
			 
			 UUID uuid = UUID.randomUUID();
			 UserPassRest up=new UserPassRest();
			 up.setUserLogin(user.getUserLogin());
			 up.setToken(uuid.toString());
			 userPassService.saveUser(up);
		//	 MimeMessage message = emailSender.createMimeMessage();		 
		 SimpleMailMessage message = new SimpleMailMessage();
		       message.setFrom("donotreplay@elkarna.com");
		       message.setTo(user.getEmail());
	        message.setSubject("  تغيير كلمة السر");
	        message.setText( "  عند ما تضغط علي الرابط التالي يتم تغيير كلة السر إلي   12     http://elkarna.com/Elkarna/signup/changePassWord?userLogin="+user.getUserLogin()+"&&token="+uuid.toString());
	       // message.setSubject("  Test sub");
	        //message.setText( "this test ");
	        try {
	        emailSender.send(message);
	        }
	        catch(Exception e) {
	        	
	        	System.out.println("------------------- ");
	         	System.out.println("Erreur sending email "+e.getMessage());
	        	System.out.println("------------------- ");
	        	return false;
	        }
	        return true;
		 }
		
	return false;
		
		
	}
	
	
	
	@RequestMapping(value = "/changePassWord")
	public 
	ModelAndView  changePassed(final HttpServletRequest request,final HttpServletResponse response,ModelMap model) throws JsonProcessingException {
	String token=request.getParameter("token");
	String userLogin=request.getParameter("userLogin");
	String message= messageSource.getMessage("lable.email.wechange.password.wrong",
	        null, LocaleContextHolder.getLocale());
	
	
		if(!StringUtils.isEmpty(token)) {
			UserPassRest user=	 userPassService.findUserByToken(token);
			 if(user!=null && user.getUserLogin().equals(userLogin) ) {
				 userPassService.delete(userLogin);
				 Users userNew=	 userService.getUserByLogin(userLogin);
				 userNew.setPassword("c20ad4d76fe97759aa27a0c99bff6710");
				 userService.saveUser(userNew);
				 message= messageSource.getMessage("lable.email.wechange.password",
					        null, LocaleContextHolder.getLocale());
//				 String str= "  12  لقد تم تغيير كلمة السرإلي العدد   " ;
//				 
//				 response.setCharacterEncoding("utf-8");     
//				  //  String str="şŞğĞİıçÇöÖüÜ";
//				return messageSource.getMessage("lable.email.wechange.password",
//					        null, LocaleContextHolder.getLocale());
				 
				
				
			 }
		}
		
		model.addAttribute("message", message);
		return new ModelAndView("passmodify");
			
		
	}
	
	
	@RequestMapping(value = "/Test")
	public   ModelAndView Test( HttpServletRequest request,final HttpServletResponse response,ModelMap model ) throws IOException {

		
		String rawString = " لقد تم تغيير كلمة السرإلي العدد";
		//response.setCharacterEncoding("utf-8");  
		
	String str=rawString;//	new String(rawString.getBytes("8859_1"), "UTF8")+'\ufeff';
		String message= messageSource.getMessage("lable.email.wechange.password.wrong",
		        null, LocaleContextHolder.getLocale());
		model.addAttribute("message", message);
		return new ModelAndView("passmodify");
//		String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);
	
				//	 String str= new String("  123  لقد تم تغيير كلمة السرإلي العدد   " ,"UTF-8");
					 
	//				response.setCharacterEncoding("utf-8");   
					// return str;
					// return utf8EncodedString;
					//return messageSource.getMessage("lable.email.wechange.password",						        null, LocaleContextHolder.getLocale());
			
			

		    }
	
	
	@RequestMapping("/downloadFileNotused")
	    public ResponseEntity<Resource> downloadFile( HttpServletRequest request) {
	        // Load file as Resource
		 String numAccount=request.getParameter("numAccount");
		 loadData(numAccount);
		 //String fileName=fileStorageProperties.getUploadDir()+File.separator+"book_new.csv";
		 //String fileName=System.getProperty("user.home")+File.separator+"book_new.csv";
		 String fileName=System.getenv("tempDire")+File.separator+numAccount+".csv";
		 System.out.println("file is 2 "+fileName);
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
	
	
	
	@RequestMapping("/downloadFile")
    public ResponseEntity<Resource> downloadPDFFile( HttpServletRequest request) {
        // Load file as Resource
	 String numAccount=request.getParameter("numAccount");
	 loadPDFdData(numAccount);
	 //String fileName=fileStorageProperties.getUploadDir()+File.separator+"book_new.csv";
	 String fileName=System.getProperty("user.home")+File.separator+"book_new.pdf";
	 
	 //String fileName=System.getenv("tempDire")+File.separator+numAccount+".csv";
	 System.out.println("file is 2 "+fileName);
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
		
		
		 
	
	    // ...
	//	 FileWriter out = new FileWriter("book_new.csv");
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
					 
			      	//	 final MvtBean mvt = new MvtBean();
					//mvt.setListMvt(l_list);
					//mvt.setSolde(accountService.getAccountBalance(account					        .getNumAccount()));
					//elapsedTimeMillis = System.currentTimeMillis() - start;
					//System.out.println("elapsedTimeMillis sold "        + elapsedTimeMillis + " sn");
					// Get elapsed time in seconds
					//return mvt;

				} 
			
			
//			 List<AccountDto> lst= accountService.getAllAccounDto("TEST");
//			 for(AccountDto ac:lst) {
//				 printer.printRecord(ac.getNumAccount(), ac.getLibelle());
//			 }
			
			 outln.flush();
			 outln.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	 }
	
	
	
	
	 
	 void loadPDFdData(String numAccount) {
		 
		 
			
		// String fileName=fileStorageProperties.getUploadDir()+File.separator+"book_new.csv";
		 String fileName=System.getProperty("user.home")+File.separator+"book_new.pdf";
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
	 
	 
	 private static void addMetaData(Document document) {
	        document.addTitle("My first PDF");
	        document.addSubject("Using iText");
	        document.addKeywords("Java, PDF, iText");
	        document.addAuthor("Lars Vogel");
	        document.addCreator("Lars Vogel");
	    }
	


	    private static void addTitlePage(Document document)
	            throws DocumentException, IOException {
	        Paragraph preface = new Paragraph();
	        // We add one empty line
	    	final BaseFont bf = BaseFont.createFont("trado.ttf",
			        BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	    	final Font font = new Font(bf, 10, 0);
			font.setColor(BaseColor.BLACK);
	        addEmptyLine(preface, 1);
	        Font f = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        // Lets write a big header
	        preface.add(new Paragraph(" التاريخ",font ));
	      
	        addEmptyLine(preface, 1);
	        // Will create: Report generated by: _name, _date
	        preface.add(new Paragraph(
	                "التاريخ: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	                font ));
	        addEmptyLine(preface, 3);
	        preface.add(new Paragraph(
	                "التاريخ ",
	                f ));

	        addEmptyLine(preface, 8);

	        preface.add(new Paragraph(
	                "التاريخ",
	                font));

	        document.add(preface);
	        // Start a new page
	        document.newPage();
	    }
	    
	    private static void addContent(Document document) throws DocumentException {
	        Anchor anchor = new Anchor("التاريخ");
	        anchor.setName("التاريخ");
	        Font f = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        // Second parameter is the number of the chapter
	        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

	        Paragraph subPara = new Paragraph();
	        Section subCatPart = catPart.addSection(subPara);
	        //subCatPart.add(new Paragraph("Hello"));


	        //subCatPart.add(paragraph);

	        // add a table
	        createTable(subCatPart);

	        // now add all this to the document
	        document.add(catPart);

	        // Next section
	    
	        // Second parameter is the number of the chapter
	        catPart = new Chapter(new Paragraph(anchor), 1);

	        subPara = new Paragraph("التاريخ", f);
	     //   subCatPart = catPart.addSection(subPara);
	       // subCatPart.add(new Paragraph("This is a very important message"));

	        // now add all this to the document
	        document.add(catPart);

	    }
	    
	    
	    private static void createTable(Section subCatPart)
	            throws BadElementException {
	        PdfPTable table = new PdfPTable(3);

	        // t.setBorderColor(BaseColor.GRAY);
	        // t.setPadding(4);
	        // t.setSpacing(4);
	        // t.setBorderWidth(1);
	        Font f = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        Phrase phrase = new Phrase();
	        Chunk chunk = new Chunk(" التاريخ");
	        phrase.add(chunk);
	        phrase.add(new Chunk(ARABIC, f));
	      //  PdfPCell cell = new PdfPCell(phrase);
	        
	        PdfPCell c1 = new PdfPCell(new Phrase(phrase));
	       // c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        c1.setUseDescender(true);
	        c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	        table.addCell(c1);
	        phrase = new Phrase();
	        chunk = new Chunk(" التفاصيل");
	        phrase.add(chunk);
	        phrase.add(new Chunk(ARABIC, f));

	        c1 = new PdfPCell(phrase);
	        //c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	        table.addCell(c1);
	        
	        phrase = new Phrase();
	        chunk = new Chunk(" له");
	        phrase.add(chunk);
	        phrase.add(new Chunk(ARABIC, f));
	        c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

	        c1 = new PdfPCell(phrase);
	        //c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	      //  c1.setTextAlignment(TextAlignmen.RIGHT);
	        c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	        table.addCell(c1);
	        table.setHeaderRows(1);

	        table.addCell("1.4");
	        table.addCell(" التفاصيل");
	        table.addCell("1.2");
	        table.addCell("2.1");
	        table.addCell("2.2");
	        table.addCell("2.3");

	        subCatPart.add(table);

	    }
	    
	    private static void addEmptyLine(Paragraph paragraph, int number) {
	        for (int i = 0; i < number; i++) {
	            paragraph.add(new Paragraph(" "));
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
				 cell.setPhrase(new Phrase(sommeINStr, font));
					table.addCell(cell);
					cell.setPhrase(new Phrase(sommeOutStr, font));
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
