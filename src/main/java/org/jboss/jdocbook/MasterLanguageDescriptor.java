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
import java.util.Locale;
import java.util.Set;

/**
 * Descriptor of the master language.
 *
 * @author Steve Ebersole
 */
public interface MasterLanguageDescriptor {
	/**
	 * Retrieve the master language.
	 *
	 * @return The master language.
	 */
	public Locale getLanguage();

	/**
	 * Retrive the GNU gettext <tt>POT</tt> directory.
	 *
	 * @return The <tt>POT</tt> directory.
	 */
	public File getPotDirectory();

	/**
	 * Retrieve the base directory for the master language sources.
	 *
	 * @return The base source directory.
	 */
	public File getBaseSourceDirectory();

	/**
	 * Retrieve the file reference for the root source document.
	 *
	 * @return The root document file.
	 */
	public File getRootDocumentFile();

	/**
	 * Retrieve the full set of source files, including <tt>XInclude</tt> files.
	 *
	 * @return The complete document file set.
	 */
	public Set<File> getDocumentFiles();
}
