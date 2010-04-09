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

import org.jboss.jdocbook.profile.Profiler;
import org.jboss.jdocbook.profile.ProfilerImpl;
import org.jboss.jdocbook.translate.Translator;
import org.jboss.jdocbook.translate.TranslatorImpl;
import org.jboss.jdocbook.xslt.TransformerBuilder;
import org.jboss.jdocbook.xslt.TransformerBuilderImpl;
import org.xml.sax.EntityResolver;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class JDocBookComponentFactory {
	private final Environment environment;
	private final Configuration configuration;

	private final TransformerBuilderImpl transformerBuilder;

	private final TranslatorImpl translator;
	private final ProfilerImpl profiler;

	public JDocBookComponentFactory(Environment environment, Configuration configuration) {
		this.environment = environment;
		this.configuration = configuration;

		this.transformerBuilder = new TransformerBuilderImpl( this );

		this.translator = new TranslatorImpl( this );
		this.profiler = new ProfilerImpl( this );
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public TransformerBuilder getTransformerBuilder() {
		return transformerBuilder;
	}

	public Translator getTranslator() {
		return translator;
	}

	public Profiler getProfiler() {
		return profiler;
	}
}
