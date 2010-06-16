package org.jboss.jdocbook.test.render;

import org.jboss.jdocbook.render.FormatOptions;
import org.jboss.jdocbook.util.StandardDocBookFormatMetadata;

public class MasterPdfFormatOptionsImpl implements FormatOptions {

	public String getName() {
		return StandardDocBookFormatMetadata.PDF.getName();
	}

	public String getStylesheetResource() {
		return "classpath:/xslt/org/hibernate/jdocbook/xslt/pdf.xsl";
	}

	public String getTargetFinalName() {
		return "hibernate_reference.pdf";
	}

}
