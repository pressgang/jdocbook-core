package org.jboss.jdocbook.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jboss.jdocbook.Environment;
import org.jboss.jdocbook.MasterLanguageDescriptor;
import org.jboss.jdocbook.ResourceDelegate;

public class BaseEnvironment implements Environment, Constant {

	@Override
	public List<File> getFontDirectories() {
		List<File> list=new ArrayList<File>();
		list.add(new File( STAGING_DIR,"fonts" ));
		return list;
	}

	@Override
	public MasterLanguageDescriptor getMasterLanguageDescriptor() {
		return new BaseMasterLanguateDescriptor();
	}

	@Override
	public ResourceDelegate getResourceDelegate() {
		return new BaseResourceDelegate();
	}

	@Override
	public File getStagingDirectory() {
		return STAGING_DIR;
	}

	@Override
	public File getWorkDirectory() {
		return WORK_DIR;
	}

}
