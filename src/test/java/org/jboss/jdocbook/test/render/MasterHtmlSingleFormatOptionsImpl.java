package org.jboss.jdocbook.test.render;

import org.jboss.jdocbook.render.FormatOptions;
import org.jboss.jdocbook.util.StandardDocBookFormatMetadata;

public class MasterHtmlSingleFormatOptionsImpl implements FormatOptions {

	@Override
	public String getName() {
		return StandardDocBookFormatMetadata.HTML_SINGLE.getName();
	}

	@Override
	public String getStylesheetResource() {
		return "classpath:/xslt/org/hibernate/jdocbook/xslt/xhtml-single.xsl";
	}

	@Override
	public String getTargetFinalName() {
		return "index.html";
	}

}
