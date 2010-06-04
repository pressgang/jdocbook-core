package org.jboss.jdocbook.test.render;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;

import org.jboss.jdocbook.Configuration;
import org.jboss.jdocbook.Environment;
import org.jboss.jdocbook.Profiling;
import org.jboss.jdocbook.ValueInjection;
import org.jboss.jdocbook.test.AbstractJDocBookTest;
import org.jboss.jdocbook.test.Constant;
import org.jboss.jdocbook.translate.TranslationSource;

/**
 * current problems:
 * <p>
 * have to unzip *.jdocbook-style manually and copy the content to staging
 * directory
 * 
 */
public class RenderTest extends AbstractJDocBookTest {
//
//	public void testRenderHtmlMasterLanguage() {
//		getJDocBookComponentRegistry().getRenderer().render(
//				new RenderingSourceImpl( MASTER_LANGUAGE, MASTER_DOC_FILE ),
//				new MasterHtmlFormatOptionsImpl() );
//	}
//
//	public void testRenderHtmlSingleMasterLanguage() {
//		getJDocBookComponentRegistry().getRenderer().render(
//				new RenderingSourceImpl( MASTER_LANGUAGE, MASTER_DOC_FILE ),
//				new MasterHtmlSingleFormatOptionsImpl() );
//	}
//
//	public void testRenderPdfMasterLanguage() {
//		getJDocBookComponentRegistry().getRenderer().render(
//				new RenderingSourceImpl( MASTER_LANGUAGE, MASTER_DOC_FILE ),
//				new MasterPdfFormatOptionsImpl() );
//	}

	public void testRenderPdfTranslatedLanguage() {
		TranslationSource translationSource = new TranslationSourceImpl( Locale.SIMPLIFIED_CHINESE,
				new File( BASE_SOURCE_DIR, "zh-CN" ), new File( WORK_DIR, "zh-CN" ) );
		getJDocBookComponentRegistry().getTranslator().translate( translationSource );
		System.out.println("--------------- translation done, start rendering -----------------");
		getJDocBookComponentRegistry().getRenderer().render(
				new RenderingSourceImpl( Locale.SIMPLIFIED_CHINESE, new File( translationSource
						.resolveTranslatedXmlDirectory(), MASTER_DOC_FILE_NAME ) ),
				new MasterPdfFormatOptionsImpl() );
	}

	private class TranslationSourceImpl implements TranslationSource {
		private Locale lang;
		private File resolvePoDirectory;
		private File resolveTranslatedXmlDirectory;

		public TranslationSourceImpl( final Locale lang, final File resolvePoDirectory,
				final File resolveTranslatedXmlDirectory ) {
			this.lang = lang;
			this.resolvePoDirectory = resolvePoDirectory;
			this.resolveTranslatedXmlDirectory = resolveTranslatedXmlDirectory;
		}

		@Override
		public Locale getLanguage() {
			return lang;
		}

		@Override
		public File resolvePoDirectory() {
			return resolvePoDirectory;
		}

		@Override
		public File resolveTranslatedXmlDirectory() {
			return resolveTranslatedXmlDirectory;
		}
	}
}
