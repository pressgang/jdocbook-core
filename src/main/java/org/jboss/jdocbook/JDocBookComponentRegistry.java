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

import java.util.Locale;

import org.jboss.jdocbook.profile.Profiler;
import org.jboss.jdocbook.profile.ProfilerImpl;
import org.jboss.jdocbook.render.Renderer;
import org.jboss.jdocbook.render.RendererImpl;
import org.jboss.jdocbook.render.XslFoGenerator;
import org.jboss.jdocbook.render.XslFoGeneratorImpl;
import org.jboss.jdocbook.translate.PoSynchronizer;
import org.jboss.jdocbook.translate.PoSynchronizerImpl;
import org.jboss.jdocbook.translate.PotSynchronizer;
import org.jboss.jdocbook.translate.PotSynchronizerImpl;
import org.jboss.jdocbook.translate.Translator;
import org.jboss.jdocbook.translate.TranslatorImpl;
import org.jboss.jdocbook.util.TranslationUtils;
import org.jboss.jdocbook.xslt.TransformerBuilder;
import org.jboss.jdocbook.xslt.TransformerBuilderImpl;

/**
 * Registry for the various jDocBook components.
 *
 * @author Steve Ebersole
 */
public class JDocBookComponentRegistry {
	private final Environment environment;
	private final Configuration configuration;

	private final TransformerBuilderImpl transformerBuilder;

	private final TranslatorImpl translator;
	private final ProfilerImpl profiler;
	private final RendererImpl renderer;
	private final XslFoGeneratorImpl xslFoGenerator;
	private final PotSynchronizerImpl potSynchronizer;
	private final PoSynchronizerImpl poSynchronizer;

	public JDocBookComponentRegistry(Environment environment, Configuration configuration) {
		this.environment = environment;
		this.configuration = configuration;

		this.transformerBuilder = new TransformerBuilderImpl( this );

		this.translator = new TranslatorImpl( this );
		this.profiler = new ProfilerImpl( this );
		this.renderer = new RendererImpl( this );
		this.xslFoGenerator = new XslFoGeneratorImpl( this );
		this.potSynchronizer = new PotSynchronizerImpl( this );
		this.poSynchronizer = new PoSynchronizerImpl( this );
	}

	/**
	 * Retrieve the info for the environment in which jDocBook is being executed.
	 *
	 * @return The execution environment.
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * Retrieve the user configuration.
	 *
	 * @return The user configuration.
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Retrieve the builder for <tt>XSLT</tt> {@link javax.xml.transform.Transformer} instances.
	 *
	 * @return The transformer builder
	 */
	public TransformerBuilder getTransformerBuilder() {
		return transformerBuilder;
	}

	/**
	 * Retrieve the reference to the translator service.
	 *
	 * @return The translator service
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public Translator getTranslator() {
		return translator;
	}

	/**
	 * Retrieve the reference to the profiler service.
	 *
	 * @return The profiler service
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public Profiler getProfiler() {
		return profiler;
	}

	/**
	 * Retrieve the reference to the renderer service.
	 *
	 * @return The renderer service
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public Renderer getRenderer() {
		return renderer;
	}

	@SuppressWarnings({ "UnusedDeclaration" })
	public XslFoGenerator getXslFoGenerator() {
		return xslFoGenerator;
	}

	@SuppressWarnings({ "UnusedDeclaration" })
	public PotSynchronizer getPotSynchronizer() {
		return potSynchronizer;
	}

	@SuppressWarnings({ "UnusedDeclaration" })
	public PoSynchronizer getPoSynchronizer() {
		return poSynchronizer;
	}

	public String toLanguageString(Locale language) {
		return TranslationUtils.render( language, configuration.getLocaleSeparator() );
	}
}
