package org.jboss.jdocbook.test.render;

import org.jboss.jdocbook.render.FormatOptions;
import org.jboss.jdocbook.util.StandardDocBookFormatMetadata;

public class MasterHtmlFormatOptionsImpl implements FormatOptions {

	public String getName() {
		return StandardDocBookFormatMetadata.HTML.getName();
	}

	public String getStylesheetResource() {
		return "classpath:/xslt/org/hibernate/jdocbook/xslt/xhtml.xsl";
	}

	public String getTargetFinalName() {
		return "index.html";
	}

}
