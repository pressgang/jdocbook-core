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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;

import org.jboss.jdocbook.JDocBookComponentRegistry;
import org.jboss.jdocbook.ValueInjection;
import org.jboss.jdocbook.util.FileUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An {@link EntityResolver} used to resolve XInclude files specifically to add
 * {@linkplain org.jboss.jdocbook.Configuration#getValueInjections() injected values} as DOCTYPE entities.
 * Essentially we are wrapping the underlying stream and adding the DOCTYPE info.
 *
 * @see FileUtils#createInputSource
 *
 * @author Steve Ebersole
 */
public class XIncludeEntityResolver implements EntityResolver {
	private final JDocBookComponentRegistry componentRegistry;

	public XIncludeEntityResolver(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	protected LinkedHashSet<ValueInjection> getValueInjections() {
		return componentRegistry.getConfiguration().getValueInjections();
	}

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		// in my experience an XInclude is presented here with a null publicId and a file:// url systemId
		// I have never seen this documented anywhere as what is to expected however.
		if ( publicId != null || systemId == null ) {
			return null;
		}
		if ( ! systemId.startsWith( "file:" ) && ! systemId.endsWith( ".xml" ) ) {
			return null;
		}

		final File file = new File( new URL( systemId ).getFile() );
		if ( ! file.exists() ) {
			return null;
		}

		InputSource source = FileUtils.createInputSource( file, getValueInjections() );
		source.setSystemId( systemId );
		return source;
	}
}
