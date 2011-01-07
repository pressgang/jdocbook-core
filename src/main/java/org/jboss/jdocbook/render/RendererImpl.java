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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.jboss.jdocbook.JDocBookComponentRegistry;
import org.jboss.jdocbook.render.fop.ResultImpl;
import org.jboss.jdocbook.util.ConsoleRedirectionHandler;
import org.jboss.jdocbook.util.FileUtils;
import org.jboss.jdocbook.util.StandardDocBookFormatMetadata;
import org.jboss.jdocbook.util.TranslationUtils;
import org.jboss.jdocbook.xslt.EntityResolverChain;
import org.jboss.jdocbook.xslt.FormatPlan;
import org.jboss.jdocbook.xslt.LocalDocBookEntityResolver;
import org.jboss.jdocbook.xslt.XIncludeEntityResolver;
import org.jboss.jdocbook.xslt.XSLTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link Renderer} contract
 *
 * @author Steve Ebersole
 */
public class RendererImpl implements Renderer {
	private static final Logger log = LoggerFactory.getLogger( RendererImpl.class );

	private final JDocBookComponentRegistry componentRegistry;
	private final EntityResolverChain entityResolver;

	public RendererImpl(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
		entityResolver = new EntityResolverChain( componentRegistry.getTransformerBuilder().getCatalogResolver() );
		entityResolver.addEntityResolver( new LocalDocBookEntityResolver() );
		entityResolver.addEntityResolver( new XIncludeEntityResolver( componentRegistry ) );
	}

	/**
	 * {@inheritDoc}
	 */
	public void render(RenderingSource source, FormatOptions formatOptions) {
		final String languageStr = stringify( source.getLanguage() );
		final FormatPlan formatPlan = FormatPlanBuilder.buildFormatPlan( formatOptions );
		log.info( "Starting rendering {}/{}", languageStr, formatPlan.getName() );

		File sourceFile = source.resolveSourceDocument();
		if ( !sourceFile.exists() ) {
			log.warn( "Source document {} did not exist; skipping", sourceFile.getAbsolutePath() );
			return;
		}

		final File publishingDirectory = source.resolvePublishingBaseDirectory();
		if ( ! publishingDirectory.exists() ) {
			boolean created = publishingDirectory.mkdirs();
			if ( !created ) {
				log.warn( "Unable to create publishing directory {}", publishingDirectory.getAbsolutePath() );
			}
		}

		final File targetDirectory = new File( publishingDirectory, formatPlan.getName() );
		if ( ! targetDirectory.exists() ) {
			FileUtils.mkdir( targetDirectory.getAbsolutePath() );
		}

		final File stagingDirectory = componentRegistry.getEnvironment().getStagingDirectory();
		if ( formatPlan.requiresImageCopying() ) {
			if ( stagingDirectory.exists() ) {
				File imageBase = new File( stagingDirectory, "images" );
				if ( imageBase.exists() ) {
					try {
						FileUtils.copyDirectoryStructure( imageBase, targetDirectory );
					}
					catch ( IOException e ) {
						throw new RenderingException( "unable to copy images", e );
					}
				}
				File cssBase = new File( stagingDirectory, "css" );
				if ( cssBase.exists() ) {
					try {
						FileUtils.copyDirectoryStructure( cssBase, targetDirectory );
					}
					catch ( IOException e ) {
						throw new RenderingException( "unable to copy css", e );
					}
				}
			}
		}

		final File targetFile = new File( targetDirectory, deduceTargetFileName( sourceFile, formatPlan ) );
		if ( targetFile.exists() ) {
			boolean deleted = targetFile.delete();
			if ( !deleted ) {
				log.warn( "Unable to delete existing target file {}", targetFile.getAbsolutePath() );
			}
		}
		if ( !targetFile.exists() ) {
			try {
				boolean created = targetFile.createNewFile();
				if ( !created ) {
					log.warn( "Unable to create target file {}", targetFile.getAbsolutePath() );
				}
			}
			catch ( IOException e ) {
				throw new RenderingException( "unable to create output file [" + targetFile.getAbsolutePath() + "]", e );
			}
		}

		Transformer transformer = buildTransformer( targetFile, formatPlan, stagingDirectory );
		transformer.setParameter( "l10n.gentext.language", languageStr );

		ConsoleRedirectionHandler console = new ConsoleRedirectionHandler( determineConsoleRedirectFile( source, formatPlan ) );
		console.start();

		try {
			Source transformationSource = buildSource( sourceFile );
			Result transformationResult = buildResult( targetFile, formatPlan );

			try {
				transformer.transform( transformationSource, transformationResult );
			}
			catch ( TransformerException e ) {
				throw new XSLTException( "error rendering [" + e.getMessageAndLocation() + "] on " + sourceFile.getName(), e );
			}
			finally {
				releaseResult( transformationResult, formatPlan );
			}
		}
		finally {
			console.stop();
		}
	}

	private String stringify(Locale locale) {
		return TranslationUtils.render( locale, componentRegistry.getConfiguration().getLocaleSeparator() );
	}

	protected Transformer buildTransformer(
			File targetFile,
			FormatPlan formatPlan,
			File stagingDirectory) throws RenderingException, XSLTException {
		final URL transformationStylesheet =  componentRegistry.getEnvironment()
				.getResourceDelegate()
				.requireResource( formatPlan.getStylesheetResource() );
		Transformer transformer = componentRegistry.getTransformerBuilder().buildTransformer( formatPlan, transformationStylesheet );
		if ( formatPlan.requiresSettingImagePath() ) {
			try {
				String imgSrcPath = new File( stagingDirectory, "images" ).toURI().toURL().toString();
				if ( !imgSrcPath.endsWith( "/" ) ) {
					imgSrcPath += '/';
				}
				log.trace( "setting 'img.src.path' xslt parameter {}", imgSrcPath );
				transformer.setParameter( "img.src.path", imgSrcPath );
			}
			catch ( MalformedURLException e ) {
				throw new XSLTException( "unable to prepare 'img.src.path' xslt parameter", e );
			}
		}

		transformer.setParameter( "keep.relative.image.uris", componentRegistry.getConfiguration().isUseRelativeImageUris() ? "1" : "0" );
		transformer.setParameter( "base.dir", targetFile.getParent() + File.separator );
		transformer.setParameter( "manifest.in.base.dir", "1" );

		if ( formatPlan.doesChunking() ) {
			String rootFilename = targetFile.getName();
			rootFilename = rootFilename.substring( 0, rootFilename.lastIndexOf( '.' ) );
			transformer.setParameter( "root.filename", rootFilename );
		}
		return transformer;
	}

	private File determineConsoleRedirectFile(RenderingSource source, FormatPlan formatPlan) {
		String fileName = "console-"
				+ stringify( source.getLanguage() ) + "-"
				+ formatPlan.getName()
				+ ".log";
		return new File( new File( componentRegistry.getEnvironment().getWorkDirectory(), "log" ), fileName );
	}

	private String deduceTargetFileName(File source, FormatPlan formatPlan) {
		return formatPlan.getTargetNamingStrategy().determineTargetFileName( source );
	}

	protected Source buildSource(File sourceFile) throws RenderingException {
		return FileUtils.createSAXSource(
			sourceFile,
			entityResolver,
			componentRegistry.getConfiguration().getValueInjections()
		);
	}

	protected Result buildResult(File targetFile, FormatPlan formatPlan) throws RenderingException, XSLTException {
		if ( StandardDocBookFormatMetadata.PDF.getName().equals( formatPlan.getName() ) ) {
			return new ResultImpl( targetFile, componentRegistry );
		}
		else {
			return new StreamResult( targetFile );
		}
	}

	protected void releaseResult(Result transformationResult, FormatPlan formatPlan) {
		if ( StandardDocBookFormatMetadata.PDF.getName().equals( formatPlan.getName() ) ) {
			( (ResultImpl) transformationResult ).release();
		}
		else {
			// nothing to do...
		}
	}
}
