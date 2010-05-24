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
package org.jboss.jdocbook.profile;

import java.io.File;
import java.util.Locale;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.jboss.jdocbook.Configuration;
import org.jboss.jdocbook.JDocBookComponentRegistry;
import org.jboss.jdocbook.util.Constants;
import org.jboss.jdocbook.util.FileUtils;
import org.jboss.jdocbook.util.TranslationUtils;
import org.jboss.jdocbook.xslt.EntityResolverChain;
import org.jboss.jdocbook.xslt.LocalDocBookEntityResolver;
import org.jboss.jdocbook.xslt.TransformerBuilder;
import org.jboss.jdocbook.xslt.XIncludeEntityResolver;
import org.jboss.jdocbook.xslt.XSLTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link Profiler} contract
 *
 * @author Steve Ebersole
 */
public class ProfilerImpl implements Profiler {
	private static final Logger log = LoggerFactory.getLogger( ProfilerImpl.class );

	private final JDocBookComponentRegistry componentRegistry;

	public ProfilerImpl(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	private EntityResolverChain entityResolver;

	private EntityResolverChain getEntityResolver() {
		if ( entityResolver == null ) {
			entityResolver = new EntityResolverChain( componentRegistry.getTransformerBuilder().getCatalogResolver() );
			entityResolver.addEntityResolver( new LocalDocBookEntityResolver() );
			entityResolver.addEntityResolver( new XIncludeEntityResolver( componentRegistry ) );
		}
		return entityResolver;
	}

	private Configuration configuration() {
		return componentRegistry.getConfiguration();
	}

	private TransformerBuilder transformerBuilder() {
		return componentRegistry.getTransformerBuilder();
	}

	/**
	 * {@inheritDoc}
	 */
	public void profile(ProfilingSource profilingSource) {
		try {
			final File targetFile = profilingSource.resolveProfiledDocumentFile();
			log.info( "applying DocBook profiling [" + targetFile.getAbsolutePath() + "]" );

			if ( ! targetFile.getParentFile().exists() ) {
				boolean created = targetFile.getParentFile().mkdirs();
				if ( !created ) {
					log.info( "Unable to create parent directory " + targetFile.getAbsolutePath() );
				}
			}

			final String languageString = render( profilingSource.getLanguage() );

			Transformer xslt = transformerBuilder().buildStandardTransformer( Constants.MAIN_PROFILE_XSL_RESOURCE );
			xslt.setParameter( "l10n.gentext.language", languageString );

			// figure out the attribute upon which to profile
			final String profilingAttributeName = configuration().getProfiling().getAttributeName();
			if ( profilingAttributeName == null || "lang".equals( profilingAttributeName ) ) {
				xslt.setParameter( "profile.attribute", "lang" );
				xslt.setParameter( "profile.lang", languageString );
			}
			else {
				xslt.setParameter( "profile.attribute", profilingAttributeName );
				xslt.setParameter( "profile.value", configuration().getProfiling().getAttributeValue() );
			}

			xslt.transform( buildSource( profilingSource.resolveDocumentFile() ), buildResult( targetFile ) );
		}
		catch ( TransformerException e ) {
			throw new XSLTException( "error performing translation [" + e.getLocationAsString() + "] : " + e.getMessage(), e );
		}
	}

	private Source buildSource(File sourceFile) throws XSLTException {
		return FileUtils.createSAXSource( sourceFile, getEntityResolver(), configuration().getValueInjections() );
	}

	protected Result buildResult(File targetFile) throws XSLTException {
		return new StreamResult( targetFile );
	}

	private String render(Locale language) {
		return TranslationUtils.render( language, configuration().getLocaleSeparator() );
	}
}
