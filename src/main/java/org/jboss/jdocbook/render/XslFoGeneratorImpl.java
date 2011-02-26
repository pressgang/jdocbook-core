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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jboss.jdocbook.JDocBookComponentRegistry;
import org.jboss.jdocbook.util.FileUtils;
import org.jboss.jdocbook.util.StandardDocBookFormatMetadata;
import org.jboss.jdocbook.util.TranslationUtils;
import org.jboss.jdocbook.xslt.FormatPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link XslFoGenerator} contract.
 *
 * @author Steve Ebersole
 */
public class XslFoGeneratorImpl implements XslFoGenerator {
	private static final Logger log = LoggerFactory.getLogger( XslFoGeneratorImpl.class );

	private final JDocBookComponentRegistry componentRegistry;

	public XslFoGeneratorImpl(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	public void generateXslFo(RenderingSource source, FormatOptions formatOptions) {
		final FormatPlan formatPlan = FormatPlanBuilder.buildFormatPlan( formatOptions );
		final Transformer transformer = buildXslFoTransformer( formatPlan );

		final String sourceFileBaseName = FileUtils.basename( source.resolveSourceDocument().getAbsolutePath() );
		final File fo = new File( source.getXslFoDirectory(), sourceFileBaseName + "fo" );

		String lang = TranslationUtils.render( source.getLanguage(), componentRegistry.getConfiguration().getLocaleSeparator() );
		transformer.setParameter( "l10n.gentext.language", lang );

		boolean createFile;
		if ( ! fo.getParentFile().exists() ) {
			boolean created = fo.getParentFile().mkdirs();
			if ( ! created ) {
				throw new RenderingException( "Unable to create FO file directory" );
			}
			createFile = true;
		}
		else {
			createFile = ! fo.exists();
		}
		if ( createFile ) {
			try {
				boolean created = fo.createNewFile();
				if ( ! created ) {
					log.info( "File system indicated problem creating FO file {}", fo );
				}
			}
			catch ( IOException e ) {
				throw new RenderingException( "Unable to create FO file " + fo.toString() );
			}
		}

		try {
			final OutputStream out = new FileOutputStream( fo );
			try {
				File sourceFile = source.resolveSourceDocument();
				Source sourceStream = new StreamSource( sourceFile );
				Result resultStream = new StreamResult( out );

				try {
					transformer.transform( sourceStream, resultStream );
				}
				catch ( TransformerException e ) {
					throw new RenderingException( "Unable to apply FO transformation", e );
				}
			}
			finally {
				try {
					out.close();
				}
				catch ( IOException e ) {
					log.info( "Unable to close output stream {}", fo );
				}
			}
		}
		catch ( FileNotFoundException e ) {
			throw new RenderingException( "Unable to open output stream to FO file", e );
		}

	}

	private Transformer buildXslFoTransformer(FormatPlan formatPlan) {
		final URL transformationStylesheet;
		if ( formatPlan.getStylesheetResource() == null ) {
			transformationStylesheet = componentRegistry.getEnvironment().getResourceDelegate()
					.requireResource( StandardDocBookFormatMetadata.PDF.getStylesheetResource() );
		}
		else {
			transformationStylesheet = componentRegistry.getEnvironment().getResourceDelegate()
					.requireResource( formatPlan.getStylesheetResource() );
		}

		return componentRegistry.getTransformerBuilder().buildTransformer( formatPlan, transformationStylesheet );
	}
}
