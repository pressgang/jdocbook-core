/*
 * jDocBook, processing of DocBook sources
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.jboss.jdocbook.xslt;

import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

/**
 * Responsible for resolving relative references from jar base urls.
 *
 * @author Steve Ebersole
 */
public class RelativeJarUriResolver implements URIResolver {
	/**
	 * {@inheritDoc}
	 */
	public Source resolve(String href, String base) throws TransformerException {
		// href need to be relative
		if ( href.indexOf( "://" ) > 0 || href.startsWith( "/" ) ) {
			return null;
		}

		// base would need to start with jar:
		if ( !base.startsWith( "jar:" ) ) {
			return null;
		}

		// I have had a few different experiences attempting to load relative jar urls in different
		// environments.  Sometimes this seems to require the full protocol to be "jar:file://".  Other
		// times "jar:file://" causes resolution problems and it needs to be just "file://"/
		//
		// This is certainly not my bag and no clue which is right (if either are).  So here we just try a
		// few variations

		try {
			// try the "jar:file://" form
			final String fullHref = base.substring( 0, base.lastIndexOf( '/' ) + 1 ) + href;
			URL url = new URL( fullHref );
			return new StreamSource( url.openStream(), url.toExternalForm() );
		}
		catch ( Throwable ignore ) {
		}

		// try the "file://" form
		try {
			final String fullHref = base.substring( 4, base.lastIndexOf( '/' ) + 1 ) + href;
			URL url = new URL( fullHref );
			return new StreamSource( url.openStream(), url.toExternalForm() );
		}
		catch ( Throwable ignore ) {
		}

		return null;
	}
}
