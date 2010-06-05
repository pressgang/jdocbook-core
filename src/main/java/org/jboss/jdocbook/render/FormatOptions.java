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

import java.io.Serializable;

/**
 * Describes certain user specified options in regard to a particular
 *
 * @author Steve Ebersole
 */
public interface FormatOptions extends Serializable {
	/**
	 * Retrieve the format name.  Should match with a standard DocBook format.
	 *
	 * @return The format name.
	 */
	public String getName();

	/**
	 * Retrieve the name to use in naming the resulting rendered output file.
	 *
	 * @return The file name to use.
	 */
	public String getTargetFinalName();

	/**
	 * Retrieve the resource name of a custom <tt>XSLT</tt> stylesheet to use.
	 *
	 * @return The custom <tt>XSLT</tt> stylesheet to use.
	 */
	public String getStylesheetResource();
}
