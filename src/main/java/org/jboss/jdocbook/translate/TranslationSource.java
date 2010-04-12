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
package org.jboss.jdocbook.translate;

import java.io.File;
import java.util.Locale;

/**
 * Describes a source of translations
 *
 * @author Steve Ebersole
 */
public interface TranslationSource {
	/**
	 * Retrieve the translation language.
	 *
	 * @return The translation language.
	 */
	public Locale getLanguage();

	/**
	 * Retrieve the directory containing PO files for this translation.
	 *
	 * @return This translation's PO file directory.
	 */
	public File resolvePoDirectory();

	/**
	 * Retrieve the directory to which translated XML files should go (created by applying the PO files on top of the
	 * master XML).
	 *
	 * @return This translation's XML directory.
	 */
	public File resolveTranslatedXmlDirectory();
}
