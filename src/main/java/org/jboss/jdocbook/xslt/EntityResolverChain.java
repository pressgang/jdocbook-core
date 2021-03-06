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

	@Override
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

	@Override
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

	@Override
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
				// JAXP parsers automatically handle "expanding" the system id for you when using EntityResolver.
				// However since we are bridging between EntityResolver2 and EntityResolver ourselves we need to
				// handle this
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

	private String expandSystemId(String base, String systemId) {
		if ( systemId == null || systemId.length() == 0 ) {
			return null;
		}

		try {
			final URI systemIdURI = new URI( systemId );
			if ( systemIdURI.isAbsolute() ) {
				return systemId;
			}

			return resolveBase( base ).resolve( systemId ).normalize().toString();
		}
		catch ( URISyntaxException e ) {
			throw new JDocBookProcessException( "Could not parse systemId [" + systemId + "] as URI", e );
		}
	}

	private URI resolveBase(String base) {
		// IMPL NOTE : `new URI(String)` does not account for null uri strings, so we check for that here
		try {
			if ( base == null || base.length() == 0 ) {
				return new URI( "file", "", "", null, null );
			}
			else {
				try {
					return new URI( base );
				}
				catch ( URISyntaxException e ) {
					// base may also be relative
					return new URI("file", "", "", null, null);
				}
			}
		}
		catch ( URISyntaxException e ) {
			throw new JDocBookProcessException( "Unable to parse base [" + base + "] as URI", e );
		}
	}
}
