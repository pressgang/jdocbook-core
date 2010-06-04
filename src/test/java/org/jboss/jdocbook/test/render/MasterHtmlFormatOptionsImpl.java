package org.jboss.jdocbook.test.render;

import org.jboss.jdocbook.render.FormatOptions;
import org.jboss.jdocbook.util.StandardDocBookFormatMetadata;

public class MasterHtmlFormatOptionsImpl implements FormatOptions {

	@Override
	public String getName() {
		return StandardDocBookFormatMetadata.HTML.getName();
	}

	@Override
	public String getStylesheetResource() {
		return "classpath:/xslt/org/hibernate/jdocbook/xslt/xhtml.xsl";
	}

	@Override
	public String getTargetFinalName() {
		return "index.html";
	}

}
