package org.jboss.jdocbook.test.render;

import org.jboss.jdocbook.render.FormatOptions;
import org.jboss.jdocbook.util.StandardDocBookFormatMetadata;

public class MasterPdfFormatOptionsImpl implements FormatOptions {

	@Override
	public String getName() {
		return StandardDocBookFormatMetadata.PDF.getName();
	}

	@Override
	public String getStylesheetResource() {
		return "classpath:/xslt/org/hibernate/jdocbook/xslt/pdf.xsl";
	}

	@Override
	public String getTargetFinalName() {
		return "hibernate_reference.pdf";
	}

}
