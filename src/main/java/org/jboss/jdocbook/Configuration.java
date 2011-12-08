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
package org.jboss.jdocbook;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Describes general user configuration data.
 *
 * @author Steve Ebersole
 */
public interface Configuration {
	/**
	 * Defines any ad-hoc <tt>XSLT</tt> parameters to be passed to the built transformer.
	 *
	 * @return The <tt>XSLT</tt> parameters.
	 */
	public Map<String, String> getTransformerParameters();

	/**
	 * Should relative uris be used to reference images?
	 *
	 * @return True if relative uris should be used; false otherwise.
	 */
	public boolean isUseRelativeImageUris();

	/**
	 * Retrieve the separator used in locale strings.
	 *
	 * @return The local separator
	 */
	public char getLocaleSeparator();

	/**
	 * Should we instruct <a href="http://xmlgraphics.apache.org/fop/">FOP</a> to auto-detect fonts via
	 * system-specific means?
	 *
	 * @return True if FOP should be instructed to auto-detect fonts; false otherwise.
	 */
	public boolean isAutoDetectFontsEnabled();

	/**
	 * Should we use the <a href="http://xmlgraphics.apache.org/fop/">FOP</a> font cache?
	 *
	 * @return True to enable font caching; false otherwise.
	 */
	public boolean isUseFopFontCacheEnabled();

	/**
	 * Retrieve any DTD entity values to be injected into XML documents.
	 *
	 * @return Any DTD entity injection values.
	 */
	public LinkedHashSet<ValueInjection> getValueInjections();

	/**
	 * Retrieve any additional catalog (uri/entity resolution) paths.
	 *
	 * @return Additional catalog paths.
	 */
	public LinkedHashSet<String> getCatalogs();

	/**
	 * Retrieve the profiling configuration.
	 *
	 * @return The profiling configuration.
	 */
	public Profiling getProfiling();

	/**
	 * Retrieve the DocBook version string to look for in locally resolving DocBook resources.  Used only in case
	 * Environment#getDocBookXsltResolutionStrategy() returns Environment.DocBookXsltResolutionStrategy#NAMED
	 *
	 * @return The DocBook version to look from in URL strings for local resolution.
	 */
	public String getDocBookVersion();
}
