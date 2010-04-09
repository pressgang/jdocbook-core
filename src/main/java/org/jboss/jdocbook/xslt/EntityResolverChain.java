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
import java.util.LinkedList;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Allows chaining a series of {@link javax.xml.transform.URIResolver resolvers} together.
 * <p/>
 * "Precedence" of the resolvers is determined by the order in which they are {@linkplain #addEntityResolver added},
 * following a FILO strategy.
 *
 * @author Steve Ebersole
 */
public class EntityResolverChain implements EntityResolver {
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
		for ( EntityResolver entityResolver : entityResolvers ) {
			InputSource source = entityResolver.resolveEntity( publicId, systemId );
			if ( source != null ) {
				return source;
			}
		}
		return null;
	}
}
