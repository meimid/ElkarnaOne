package ihmcredit;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

public class MyContextLoaderListerner extends ContextLoaderListener {

	@Override
	public void contextInitialized(final ServletContextEvent evt) {
		final ClassLoader contextLoader = Thread.currentThread()
		        .getContextClassLoader();
		Thread.currentThread().setContextClassLoader(
		        getClass().getClassLoader());
		super.contextInitialized(evt);
		Thread.currentThread().setContextClassLoader(contextLoader);
	}

	@Override
	public void contextDestroyed(final ServletContextEvent evt) {

		final ClassLoader contextLoader = Thread.currentThread()
		        .getContextClassLoader();
		Thread.currentThread().setContextClassLoader(
		        getClass().getClassLoader());
		super.contextDestroyed(evt);
		evt.getServletContext().removeAttribute(
		        WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		Thread.currentThread().setContextClassLoader(contextLoader);
	}

}