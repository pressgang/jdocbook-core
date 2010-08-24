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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

import org.jboss.jdocbook.JDocBookProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/**
 * Allows chaining a series of {@link EntityResolver resolvers} together.
 * <p/>
 * "Precedence" of the resolvers is determined by the order in which they are {@linkplain #addEntityResolver added},
 * following a FILO strategy.
 *
 * @author Steve Ebersole
 */
public class EntityResolverChain implements EntityResolver2 {
	private static final Logger log = LoggerFactory.getLogger( EntityResolverChain.class );

	private LinkedList<EntityResolver> entityResolvers = new LinkedList<EntityResolver>();

	public EntityResolverChain(EntityResolver rootResolver) {
		entityResolvers.addLast( rootResolver );
	}

	public void addEntityResolver(EntityResolver entityResolver) {
		entityResolvers.addFirst( entityResolver );
	}

	/**
	 * {@inheritDoc}
	 */
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		log.trace( "EntityResolver#resolveEntity: publicId=[" + publicId + "]; systemId=[" + systemId + "]" );
		for ( EntityResolver entityResolver : entityResolvers ) {
			InputSource source = entityResolver.resolveEntity( publicId, systemId );
			if ( source != null ) {
				return source;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
		log.trace( "EntityResolver2#getExternalSubset: name=[{}]; baseURI=[{}]", name, baseURI );
		for ( EntityResolver entityResolver : entityResolvers ) {
			if ( entityResolver instanceof EntityResolver2 ) {
				InputSource source = ( (EntityResolver2) entityResolver ).getExternalSubset( name, baseURI );
				if ( source != null ) {
					return source;
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
			throws SAXException, IOException {
		log.trace(
				"EntityResolver2#resolveEntity: name=[" + name + "]; publicId=["
						+ publicId + "]; baseURI=[" + baseURI + "]; systemId=[" + systemId + "]"
		);
		String expandedSystemId = null;
		for ( EntityResolver entityResolver : entityResolvers ) {
			if ( entityResolver instanceof EntityResolver2 ) {
				InputSource source = ( (EntityResolver2) entityResolver ).resolveEntity( name, publicId, baseURI, systemId );
				if ( source != null ) {
					return source;
				}
			}
			else {
				if ( expandedSystemId == null ) {
					expandedSystemId = expandSystemId( baseURI, systemId );
				}
				InputSource source = entityResolver.resolveEntity( publicId, expandedSystemId );
				if ( source != null ) {
					return source;
				}
			}
		}
		return null;
	}

	private String expandSystemId(String baseURI, String systemId) {
		if ( systemId == null || systemId.length() == 0 ) {
			return null;
		}

		// see if systemId is absolute already
		try {
			final URI systemIdURI = new URI( systemId );
			if ( systemIdURI.isAbsolute() ) {
				return systemId;
			}
		}
		catch ( URISyntaxException ignore ) {
		}

		return resolveRelativeURI( resolveURI( baseURI ), systemId ).toString();
	}

	private URI resolveURI(String uriString) {
		try {
			if ( uriString == null || uriString.length() == 0 ) {
				return new URI( "file", "", "", null, null );
			}
			else {
				try {
					return new URI( uriString );
				}
				catch ( URISyntaxException e ) {
					// base may also be relative
					return new URI("file", "", "", null, null);
				}
			}
		}
		catch ( URISyntaxException e ) {
			throw new JDocBookProcessException( "Unable to resolve uriString as java.net.URI [" + uriString + "]", e );
		}
	}

	private URI resolveRelativeURI(URI base, String resource) {
		if ( resource == null ) {
			throw new IllegalArgumentException( "resource cannot be null" );
		}
		if ( resource.startsWith( "/" ) ) {
			resource = resource.substring( 1, resource.length() );
		}

		// fairly naive impl...
		final String basePath = base.toString();
		int chopPoint = basePath.lastIndexOf( '/' );
		final String path;
		if ( chopPoint < 0 ) {
			path = basePath + '/' + resource;
		}
		else {
			path = basePath.substring( 0, chopPoint ) + '/' + resource;
		}

		try {
			return new URI( path );
		}
		catch ( URISyntaxException e ) {
			throw new JDocBookProcessException(
					"Unable to resolve relative URI [base=" + base.toString() + "; resource=" + resource + "]" 
			);
		}
	}
}
