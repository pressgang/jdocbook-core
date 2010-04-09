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

import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * An entity resolver for resolving DocBook resources (DTD, etc) locally rather than hitting the network.
 *
 * @author Steve Ebersole
 */
public class LocalDocBookEntityResolver implements EntityResolver {
	public static final String SYSTEM_ID_PREFIX = "http://www.oasis-open.org/docbook/xml/";
	public static final int SYSTEM_ID_PREFIX_LEN = SYSTEM_ID_PREFIX.length();

	public InputSource resolveEntity(String publicId, String systemId) {
		if ( systemId == null ) {
			return null;
		}

		if ( systemId.startsWith( SYSTEM_ID_PREFIX ) ) {
			final int versionEndTokenPosition = systemId.indexOf( '/', SYSTEM_ID_PREFIX_LEN );
			final String version = systemId.substring( SYSTEM_ID_PREFIX_LEN, versionEndTokenPosition );
			final String name = systemId.substring( versionEndTokenPosition + 1 );
			final String resourceName = "docbook/xml/" + version + '/' + name;
			InputStream stream = getClass().getClassLoader().getResourceAsStream( resourceName );
			if ( stream == null ) {
				stream = getClass().getClassLoader().getResourceAsStream( '/' + resourceName );
			}

			if ( stream != null ) {
				InputSource source = new InputSource( stream );
				source.setPublicId( publicId );
				source.setSystemId( systemId );
				return source;
			}
		}

		return null;
	}
}
