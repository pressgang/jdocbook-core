package org.jboss.jdocbook.test.render;

import org.jboss.jdocbook.render.FormatOptions;
import org.jboss.jdocbook.util.StandardDocBookFormatMetadata;

public class MasterHtmlSingleFormatOptionsImpl implements FormatOptions {

	public String getName() {
		return StandardDocBookFormatMetadata.HTML_SINGLE.getName();
	}

	public String getStylesheetResource() {
		return "classpath:/xslt/org/hibernate/jdocbook/xslt/xhtml-single.xsl";
	}

	public String getTargetFinalName() {
		return "index.html";
	}

}
