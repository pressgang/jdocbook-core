package org.jboss.jdocbook.test;

import java.io.File;
import java.util.Locale;
import java.util.Set;

import org.jboss.jdocbook.MasterLanguageDescriptor;
import org.jboss.jdocbook.util.XIncludeHelper;

public class BaseMasterLanguageDescriptor implements MasterLanguageDescriptor, Constant {

	public File getBaseSourceDirectory() {
		return MASTER_SOURCE_DIR;
	}

	public Set<File> getDocumentFiles() {
		return XIncludeHelper.locateInclusions( getRootDocumentFile() );
	}

	public Locale getLanguage() {
		return MASTER_LANGUAGE;
	}

	public File getPotDirectory() {
		return POT_DIR;
	}

	public File getRootDocumentFile() {
		return MASTER_DOC_FILE;
	}

}
