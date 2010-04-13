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

import org.jboss.jdocbook.JDocBookProcessException;

/**
 * Contract for synchronizing (creating/updating) PortableObject (PO) file(s) for a given translation.  Each
 * {@linkplain PotSynchronizer POT} file will result in a corresponding PO file for the given translation.
 *
 * @author Steve Ebersole
 */
public interface PoSynchronizer {
	/**
	 * Synchronize the PO files for the given translation.
	 *
	 * @param source The descriptor for the translation whose PO files are to be synchronized
	 *
	 * @throws JDocBookProcessException unable to synchronize POT files
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public void synchronizePo(TranslationSource source);
}
