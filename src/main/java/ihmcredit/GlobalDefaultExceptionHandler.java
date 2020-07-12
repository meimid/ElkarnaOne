package ihmcredit;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
	public static final String	DEFAULT_ERROR_VIEW	= "error";
	static Logger	           log	               = Logger.getLogger(GlobalDefaultExceptionHandler.class
	                                                       .getName());

	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(final HttpServletRequest req,
	        final Exception e) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.
		log.error(e.getMessage());
		log.error(e.fillInStackTrace());
		/*
		 * if (AnnotationUtils.findAnnotation(e.getClass(),
		 * ResponseStatus.class) != null) { throw e; }
		 */

		// Otherwise setup and send the user to a default error-view.
		final ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}
}