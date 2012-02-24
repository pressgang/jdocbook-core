package org.jboss.jdocbook.render.fop;

import java.io.PrintStream;

import org.apache.fop.events.Event;
import org.apache.fop.events.EventFormatter;
import org.apache.fop.events.EventListener;
import org.jboss.jdocbook.JDocBookProcessException;
import org.jboss.jdocbook.util.ConsoleRedirectionHandler;

/**
 * @author Steve Ebersole
 */
public class EventListenerBridge implements EventListener {
	@Override
	public void processEvent(Event event) {
		locateRedirectionStream().println( EventFormatter.format( event ) );
	}

	private PrintStream locateRedirectionStream() {
		final ConsoleRedirectionHandler redirectionHandler = ConsoleRedirectionHandler.getCurrentRedirectionHandler();
		if ( redirectionHandler == null ) {
			throw new JDocBookProcessException( "Could not locate current console redirection handler" );
		}
		return redirectionHandler.getRedirectionStream();
	}
}
