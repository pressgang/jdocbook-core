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
package org.jboss.jdocbook.xslt;

import java.net.URL;
import javax.xml.transform.Transformer;

import org.apache.xml.resolver.tools.CatalogResolver;

/**
 * Builder of {@link Transformer} instances.
 *
 * @author Steve Ebersole
 */
public interface TransformerBuilder {

	/**
	 * Get the {@link CatalogResolver} in effect for this builder.
	 *
	 * @return The catalog resolver
	 */
	public CatalogResolver getCatalogResolver();

	/**
	 * Build a transformer from the <tt>XSLT</tt> referenced by the given URL.
	 *
	 * @param xslt A URL referencing an <tt>XSLT</tt>
	 *
	 * @return The transformer.
	 */
	public Transformer buildStandardTransformer(URL xslt);

	/**
	 * Build a transformer from the <tt>XSLT</tt> referenced by the given resource name.
	 *
	 * @param xsltResource The resource name referencing an <tt>XSLT</tt>
	 *
	 * @return The transformer.
	 */
	public Transformer buildStandardTransformer(String xsltResource);

	/**
	 * Build a transformer from the format plan and <tt>XSLT</tt> referenced by the given resource name.
	 *
	 * @param formatPlan The format plan
	 * @param customStylesheet A URL referencing an <tt>XSLT</tt>
	 *
	 * @return The transformer.
	 *
	 * @throws XSLTException Indicates an error building the transformer
	 */
	public Transformer buildTransformer(FormatPlan formatPlan, URL customStylesheet) throws XSLTException;
}
