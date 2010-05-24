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
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.jboss.jdocbook.JDocBookComponentRegistry;

/**
 * Maps docbook-version based URIs to local classpath lookups.  These URIs are in the form
 * 'http://docbook.sourceforge.net/release/xsl/{version}'
 *
 * @author Steve Ebersole
 */
public class VersionResolver implements URIResolver {
	public static final String BASE_HREF = "http://docbook.sourceforge.net/release/xsl/";

	private JDocBookComponentRegistry componentRegistry;

	/**
	 * Constructs a VersionResolver instance using the given <tt>version</tt>.
	 *
	 * @param componentRegistry The execution environment
	 */
	public VersionResolver(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	/**
	 * {@inheritDoc}
	 */
	public Source resolve(String href, String base) throws TransformerException {
		final UrlParts urlParts;
		if ( href.startsWith( BASE_HREF ) ) {
			urlParts = new UrlParts( href );
		}
		else if ( base.startsWith( BASE_HREF ) ) {
			urlParts = new UrlParts(  base + "/" + href );
		}
		else {
			return null;
		}

		if ( isMatch( urlParts.version ) ) {
			return resolve( urlParts.resource );
		}

		return null;
	}

	private boolean isMatch(String version) {
		if ( componentRegistry.getConfiguration().isMatchAllVersionsForResourceResolution() ) {
			return true;
		}
		else {
			final String[] matches = componentRegistry.getConfiguration().getVersionsForResourceResolution();
			if ( matches == null ) {
				return false;
			}
			for ( String match : matches ) {
				if ( match.equals( version ) ) {
					return true;
				}
			}
			return false;
		}
	}


	private Source resolve(String resource) {
		try {
			URL resourceURL = componentRegistry.getEnvironment().getResourceDelegate().requireResource( resource );
			return new StreamSource( resourceURL.openStream(), resourceURL.toExternalForm() );
		}
		catch ( IllegalArgumentException e ) {
			return null;
		}
		catch ( IOException e ) {
			return null;
		}
	}

	private static class UrlParts {
		private String version;
		private String resource;

		private UrlParts(String urlString) {
			final String partsWeCareAbout = urlString.substring( BASE_HREF.length() );
			final int versionDelimiterPosition = partsWeCareAbout.indexOf( '/' );
			this.version = partsWeCareAbout.substring( 0, versionDelimiterPosition );
			this.resource = partsWeCareAbout.substring( versionDelimiterPosition + 1, partsWeCareAbout.length() );
		}

		@Override
		public String toString() {
			return "UrlParts{" +
					"version='" + version + '\'' +
					", resource='" + resource + '\'' +
					'}';
		}
	}
}
