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
package org.jboss.jdocbook.render;

import java.io.File;
import java.util.Locale;

/**
 * Describes a source to be rendered
 *
 * @author Steve Ebersole
 */
public interface RenderingSource {
	/**
	 * Get the language that this source represents.
	 *
	 * @return The source language.
	 */
	public Locale getLanguage();

	/**
	 * Retrieve the source document {@link File} reference.
	 *
	 * @return The source document {@link File}
	 */
	public File resolveSourceDocument();

	/**
	 * Retrieve publishing base directory for this language.  Each format will write into a sub-directory
	 * of this named directory.
	 *
	 * @return The publishing base {@link File directory}
	 */
	public File resolvePublishingBaseDirectory();

	/**
	 * Retrieve the directory into which <tt>XSL-FO</tt> files for this language should get written.
	 *
	 * @return The <tt>XSL-FO</tt> {@link File directory}
	 */
	public File getXslFoDirectory();
}
