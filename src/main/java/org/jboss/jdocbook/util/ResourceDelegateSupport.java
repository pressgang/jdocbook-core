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
package org.jboss.jdocbook.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.jdocbook.ResourceDelegate;

/**
 * Support for building {@link ResourceDelegate} implementations.
 *
 * @author Steve Ebersole
 */
public abstract class ResourceDelegateSupport implements ResourceDelegate {
	protected abstract ClassLoader getResourceClassLoader();

	/**
	 * {@inheritDoc}
	 */
	public URL requireResource(String name) {
		URL resource = locateResource( name );
		if ( resource == null ) {
			throw new IllegalArgumentException( "could not locate resource [" + name + "]" );
		}
		return resource;
	}

	/**
	 * {@inheritDoc}
	 */
	public URL locateResource(String name) {
		if ( name.startsWith( "classpath:" ) ) {
			return locateClassPathResource( name.substring( 10 ) );
		}
		else if ( name.startsWith( "file:" ) ) {
			try {
				return new URL( name );
			}
			catch ( MalformedURLException e ) {
				throw new IllegalArgumentException( "malformed explicit file url [" + name + "]");
			}
		}
		else {
			// assume a classpath resource (backwards compatibility)
			return locateClassPathResource( name );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public URL locateClassPathResource(String name) {
		while ( name.startsWith( "/" ) ) {
			name = name.substring( 1 );
		}

		URL result = getResourceClassLoader().getResource( name );
		if ( result == null ) {
			result = getResourceClassLoader().getResource( "/" + name );
		}

		return result;
	}
}
