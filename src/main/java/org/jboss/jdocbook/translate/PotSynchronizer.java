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
 * Contract for synchronizing (creating/updating) PortableObjectTemplate (POT) file(s) from the master
 * language document.  XIncludes are followed and processed as well.
 *
 * @author Steve Ebersole
 */
public interface PotSynchronizer {
	/**
	 * Perform the synchronization on the the POT files pertaining to the (master language) DocBook source.
	 *
	 * @throws JDocBookProcessException unable to synchronize POT files
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public void synchronizePot() throws JDocBookProcessException;
}
