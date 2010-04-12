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

import java.net.URL;

/**
 * Delegate for performing resource look ups.
 *
 * @author Steve Ebersole
 */
public interface ResourceDelegate {
	/**
	 * Locate the requested resource, throwing an exception if it could not be found.  The name is expected
	 * to have a scheme prefix.  Both 'classpath:' and 'file:' schemes are allowed here.
	 *
	 * @param name The resource name.
	 * @return The resource's URL.
	 * @throws IllegalArgumentException If the resource could not be found.
	 */
	public URL requireResource(String name);

	/**
	 * Locate the requested resource.  The name is expected to have a scheme prefix.  Both 'classpath:' and
	 * 'file:' schemes are allowed here.
	 *
	 * @param name The resource name.
	 * @return The resource's URL.
	 */
	public URL locateResource(String name);

	/**
	 * Look up a resource exclusively via classpath lookup.  The name should not have any scheme prefix, just the
	 * resource name.
	 *
	 * @param name The resource name.
	 * @return The resource's URL.
	 */
	public URL locateClassPathResource(String name);
}
