package org.jboss.jdocbook.test.render;

import java.io.File;
import java.util.Locale;

import org.jboss.jdocbook.render.RenderingSource;
import org.jboss.jdocbook.test.Constant;

public class RenderingSourceImpl implements RenderingSource, Constant {
	private Locale lang;
	private File resolveSourceDoctument;
	public RenderingSourceImpl( final Locale lang, final File resolveSourceDoctument ) {
		this.lang = lang;
		this.resolveSourceDoctument=resolveSourceDoctument;
	}

	@Override
	public Locale getLanguage() {
		return lang;
	}

	@Override
	public File getXslFoDirectory() {
		return null;
	}

	@Override
	public File resolvePublishingBaseDirectory() {
		return new File( PUBLISH_DIR, getLanguage().toString() );
	}

	@Override
	public File resolveSourceDocument() {
		return resolveSourceDoctument;
	}

}
