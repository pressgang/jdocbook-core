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

import org.jboss.jdocbook.JDocBookComponentRegistry;

/**
 * Resolves classpath references from the given classloader using classpath:/
 * as the protocol scheme
 *
 * @author Pete Muir
 * @author Steve Ebersole
 */
public class ClasspathResolver implements URIResolver {
	public static final String SCHEME = "classpath:";

	private final JDocBookComponentRegistry componentRegistry;

	public ClasspathResolver(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	public Source resolve(String href, String base) throws TransformerException {
		// href would need to start with classpath:
		if ( !href.startsWith( SCHEME ) ) {
			return null;
		}

		try {
			URL url = componentRegistry.getEnvironment().getResourceDelegate().locateResource( href );
			if ( url != null ) {
				return new StreamSource( url.openStream(), url.toExternalForm() );
			}
		}
		catch ( Throwable ignore ) {
		}

		throw new TransformerException( "Unable to resolve requested classpath URL [" + href + "]" );
	}

}

