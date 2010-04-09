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

import org.jboss.jdocbook.JDocBookProcessException;
import org.jboss.jdocbook.MasterLanguageDescriptor;

/**
 * Performs the work of applying a language's PO files to generate its set of translated DocBook XML.
 * <p/>
 * TODO : most of this information is static.  the only thing that really changes is the translation language
 * which effects the PO directory and target directory via relativity since the language is part of those
 * directory paths.  In other words, this contract really could be as simply as {@code translate(String language)}
 * if the master descriptor, PO base directory and translation work base directory were all injected
 * ahead of time.  Lets see how this can evolve as we add the of 2 stages...
 *
 * @author Steve Ebersole
 */
public interface Translator {
	/**
	 * Performs a translation.
	 *
 	 * @param master Information about the master language
	 * @param basePoDirectory The base translation PO directory
	 * @param targetDirectory The base translation work directory
	 *
	 * @throws JDocBookProcessException Indicates a problem performing the translation
	 */
	public void translate(
			MasterLanguageDescriptor master,
			File basePoDirectory,
			File targetDirectory) throws JDocBookProcessException;
}
