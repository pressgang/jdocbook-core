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
package org.jboss.jdocbook.util;

/**
 * Various string constants.
 *
 * @author Steve Ebersole
 */
public class Constants {
	/**
	 * The DocBook-supplied XSL for profiling in (what it terms) the two-pass approach.
	 */
	public static final String MAIN_PROFILE_XSL_RESOURCE = "profiling/profile.xsl";

	/**
	 * The name of the XML DTD validation feature.
	 */
	public static final String DTD_VALIDATION_FEATURE = "http://xml.org/sax/features/validation";

	/**
	 * The name of the XML DTD loading feature.
	 */
	public static final String DTD_LOADING_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
}
