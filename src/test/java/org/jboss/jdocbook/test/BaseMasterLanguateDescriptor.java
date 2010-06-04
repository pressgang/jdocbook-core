package org.jboss.jdocbook.test;

import java.io.File;
import java.util.Locale;
import java.util.Set;

import org.jboss.jdocbook.MasterLanguageDescriptor;
import org.jboss.jdocbook.util.XIncludeHelper;

public class BaseMasterLanguateDescriptor implements MasterLanguageDescriptor, Constant {

	@Override
	public File getBaseSourceDirectory() {
		return MASTER_SOURCE_DIR;
	}

	@Override
	public Set<File> getDocumentFiles() {
		return XIncludeHelper.locateInclusions( getRootDocumentFile() );
	}

	@Override
	public Locale getLanguage() {
		return MASTER_LANGUAGE;
	}

	@Override
	public File getPotDirectory() {
		return POT_DIR;
	}

	@Override
	public File getRootDocumentFile() {
		return MASTER_DOC_FILE;
	}

}
