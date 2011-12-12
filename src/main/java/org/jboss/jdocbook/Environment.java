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

import java.io.File;
import java.util.List;

/**
 * Defines information about the execution environment.
 *
 * @author Steve Ebersole
 */
public interface Environment {
	/**
	 * Retrieve the resource lookup delegate for this environment.
	 *
	 * @return The resource lookup delegate
	 */
	public ResourceDelegate getResourceDelegate();

	/**
	 * Get the master language descriptor.
	 * <p/>
	 * Note: it was decided to place it here since this is essentially static information.
	 *
	 * @return The master language descriptor.
	 */
	public MasterLanguageDescriptor getMasterLanguageDescriptor();

	/**
	 * Retrieve the work directory
	 *
	 * @return THe work directory
	 */
	public File getWorkDirectory();

	/**
	 * Retrieve the staging directory
	 *
	 * @return The staging directory
	 */
	public File getStagingDirectory();

	/**
	 * Retrieve any directories explicitly containing fonts.
	 * 
	 * @return Font directories.
	 */
	public List<File> getFontDirectories();

	/**
	 * Indicates how to handle resolution of URIs naming DocBook XSLT in terms of version.
	 *
	 * @see org.jboss.jdocbook.xslt.VersionResolver
	 */
	public static enum DocBookXsltResolutionStrategy {
		/**
		 * Any version should be considered a match
		 */
		INCLUSIVE,
		/**
		 * Only named versions (named via {@link Configuration#getDocBookVersion()} should be matched
		 */
		NAMED
	}

	/**
	 * Retrieve the strategy this environment would like used for resolving DocBook XSLT URIs
	 *
	 * @return The environment preferred strategy
	 */
	public DocBookXsltResolutionStrategy getDocBookXsltResolutionStrategy();

	public DocBookSchemaResolutionStrategy getDocBookSchemaResolutionStrategy();
}
