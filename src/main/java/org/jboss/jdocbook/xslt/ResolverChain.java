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

import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

/**
 * Allows chaining a series of {@link javax.xml.transform.URIResolver resolvers} together.
 * <p/>
 * "Precedence" of the resolvers is determined by the order in which
 * they are {@link #addResolver added}.
 *
 * @author Steve Ebersole
 */
public class ResolverChain implements URIResolver {
	private final List<URIResolver> resolvers = new ArrayList<URIResolver>();

	public ResolverChain() {
	}

	public ResolverChain(URIResolver resolver) {
		this();
		addResolver( resolver );
	}

	/**
	 * Adds a resolver to the chain.
	 *
	 * @param resolver The resolver to add.
	 */
	public void addResolver(URIResolver resolver) {
		resolvers.add( resolver );
	}

	/**
	 * Here we iterate over all the chained resolvers and delegate to them
	 * until we find one which can handle the resolve request (if any).
	 *
	 * {@inheritDoc}
	 */
	public Source resolve(String href, String base) throws TransformerException {
		Source result = null;
		for ( URIResolver resolver : resolvers ) {
			result = resolver.resolve( href, base );
			if ( result != null ) {
				break;
			}
		}
		return result;
	}
}
