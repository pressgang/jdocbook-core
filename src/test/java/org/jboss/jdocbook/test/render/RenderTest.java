/*
 * jDocBook, processing of DocBook sources
 *
 * Copyright (c) 2011, Red Hat Inc. or third-party contributors as
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
package org.jboss.jdocbook.test.render;

import java.io.File;
import java.util.Locale;

import org.jboss.jdocbook.JDocBookComponentRegistry;
import org.jboss.jdocbook.render.FormatOptions;
import org.jboss.jdocbook.render.RenderingSource;
import org.junit.Test;

/**
 * @author Strong Liu
 */
public class RenderTest {

//	@Test
	public void testDocBookRender() {
		JDocBookComponentRegistry registry = new JDocBookComponentRegistry( new RenderEnvironment(), new RenderConfiguration() );
		registry.getRenderer().render( new TestRenderingSource(), new TestFormatOptions() );
		//check output
	}

	class TestRenderingSource implements RenderingSource {
		public Locale getLanguage() {
			return new Locale( "en", "US" );
		}

		public File resolveSourceDocument() {
			return null;
		}

		public File resolvePublishingBaseDirectory() {
			return null;
		}

		public File getXslFoDirectory() {
			return null;
		}
	}

	class TestFormatOptions implements FormatOptions {
		public String getName() {
			return null;
		}

		public String getTargetFinalName() {
			return null;
		}

		public String getStylesheetResource() {
			return null;
		}
	}

}
