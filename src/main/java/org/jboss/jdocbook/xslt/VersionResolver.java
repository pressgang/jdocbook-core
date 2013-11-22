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

import org.jboss.jdocbook.Environment;
import org.jboss.jdocbook.JDocBookComponentRegistry;

/**
 * Maps docbook-version based URIs to local classpath lookups.  These URIs are in the form
 * 'http://docbook.sourceforge.net/release/xsl/{version}'
 *
 * @author Steve Ebersole
 */
public class VersionResolver implements URIResolver {
	public static final String BASE_HREF = "http://docbook.sourceforge.net/release/xsl/";
	public static final int BASE_HREF_LEN = BASE_HREF.length();

	private final JDocBookComponentRegistry componentRegistry;
	private final VersionMatcher versionMatcher;

	/**
	 * Constructs a VersionResolver instance using the given <tt>version</tt>.
	 *
	 * @param componentRegistry The execution environment
	 */
	public VersionResolver(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
		if ( Environment.DocBookXsltResolutionStrategy.INCLUSIVE == componentRegistry.getEnvironment().getDocBookXsltResolutionStrategy() ) {
			versionMatcher = new VersionMatcher() {
				@Override
				public boolean matched(String version) {
					return true;
				}

				@Override
				public String toString() {
					return "inclusive";
				}
			};
		}
		else {
			final String versionToMatch = componentRegistry.getConfiguration().getDocBookVersion() == null
					? "current"
					: componentRegistry.getConfiguration().getDocBookVersion();
			versionMatcher = new VersionMatcher() {
				@Override
				public boolean matched(String version) {
					return versionToMatch.equals( version );
				}

				@Override
				public String toString() {
					return "[versionToMatch=" + versionToMatch + "]";
				}
			};
		}
	}

	@Override
	public Source resolve(String href, String base) throws TransformerException {
		if ( href.startsWith( BASE_HREF ) ) {
			final int versionEndTokenPosition = href.indexOf( '/', BASE_HREF_LEN );
			final String version = href.substring( BASE_HREF_LEN, versionEndTokenPosition );
			if ( versionMatcher.matched( version ) ) {
				final String name = href.substring( versionEndTokenPosition + 1 );
				return resolveLocally( name );
			}
		}

		if ( base.startsWith( BASE_HREF ) ) {
			final int versionEndTokenPosition = base.indexOf( '/', BASE_HREF_LEN );
			final String version = base.substring( BASE_HREF_LEN, versionEndTokenPosition );
			if ( versionMatcher.matched( version ) ) {
				final String remainingBase = base.substring( versionEndTokenPosition + 1 );
				return resolveLocally( remainingBase + '/' + href );
			}
		}

		return null;
	}

	private Source resolveLocally(String resourceName) {
		try {
			URL resourceURL = componentRegistry.getEnvironment().getResourceDelegate().requireResource( resourceName );
			return new StreamSource( resourceURL.openStream(), resourceURL.toExternalForm() );
		}
		catch ( IllegalArgumentException e ) {
			return null;
		}
		catch ( IOException e ) {
			return null;
		}
	}

	@Override
	public String toString() {
		return super.toString() + " [versionMatcher=" + versionMatcher + "]";
	}

	private static interface VersionMatcher {
		public boolean matched(String version);
	}
}
