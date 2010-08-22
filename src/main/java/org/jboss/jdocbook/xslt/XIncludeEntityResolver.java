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
import java.io.StringReader;
import java.net.URL;
import java.util.LinkedHashSet;

import org.jboss.jdocbook.JDocBookComponentRegistry;
import org.jboss.jdocbook.ValueInjection;
import org.jboss.jdocbook.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/**
 * An {@link EntityResolver} used to resolve XInclude files specifically to add
 * {@linkplain org.jboss.jdocbook.Configuration#getValueInjections() injected values} as DOCTYPE entities.
 * Essentially we are wrapping the underlying stream and adding the DOCTYPE info.
 *
 * @see FileUtils#createInputSource
 *
 * @author Steve Ebersole
 */
public class XIncludeEntityResolver implements EntityResolver2 {
	private static final Logger log = LoggerFactory.getLogger( XIncludeEntityResolver.class );

	private final JDocBookComponentRegistry componentRegistry;

	public XIncludeEntityResolver(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	protected LinkedHashSet<ValueInjection> getValueInjections() {
		return componentRegistry.getConfiguration().getValueInjections();
	}

	public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
		// IMPL NOTE: this is the form called when the document contains no external subset (or no DOCTYPE).
		log.trace( "generating external subset; name=[{}]; baseURI=[{}]", name, baseURI );
		LinkedHashSet<ValueInjection> injections = getValueInjections();
		if ( injections == null || injections.isEmpty() ) {
			log.trace( "No value injections defined; skipping" );
			return null;
		}
		StringBuilder subset = FileUtils.buildInjectedEntitySubset( injections );
		return new InputSource( new StringReader( subset.toString() ) );
	}

	public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
			throws SAXException, IOException {
		// IMPL NOTE: this is the form called when the document contains a DOCTYPE.
		//		IMPORTANT : this form is actually called many times.  The scenario in which we are interested is where
		//				systemId = "*.ent" which indicates attempt to resolve external entities; we need to prepend
		//				any value injections
		log.trace( "resolving entity; name=[" + name + "]; publicId=[" + publicId + "]; baseURI=[" + baseURI + "]; systemId=[" + systemId + "]" );
		LinkedHashSet<ValueInjection> injections = getValueInjections();
		if ( injections == null || injections.isEmpty() ) {
			log.trace( "No value injections defined; skipping" );
			return null;
		}
		if ( publicId == null && baseURI.startsWith( "file:" ) && systemId.trim().endsWith( ".ent" ) ) {
			File baseFile = new File( new URL( baseURI ).getFile() );
			if ( baseFile.exists() ) {
				InputSource inputSource = createInputSource( new File( baseFile.getParentFile(), systemId ), injections );
				inputSource.setSystemId( baseFile.getAbsolutePath() );
				return inputSource;
			}
		}
		return null;
	}

	private InputSource createInputSource(File entFile, LinkedHashSet<ValueInjection> injections) throws IOException {
		// need to read all the contents into memory here and prepend our injections
		StringBuilder buffer = FileUtils.buildInjectedEntitySubset( injections );
		if ( entFile.exists() ) {
			final String contents = FileUtils.fileRead( entFile );
			buffer.append( contents );
		}
		else {
			log.warn( "referenced ENT file not found: " + entFile.getAbsolutePath() );
		}
		return new InputSource( new StringReader( buffer.toString() ) );
	}

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		return resolveEntity( null, publicId, null, systemId );
	}
}
