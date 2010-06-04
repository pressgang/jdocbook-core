package org.jboss.jdocbook.test;

import java.io.File;
import java.util.Locale;

public interface Constant {
	File BUILD_DIR = new File( "build" );
	File WORK_DIR = new File( BUILD_DIR, "work" );
	
	File PUBLISH_DIR = new File( BUILD_DIR, "publish" );
	File BASE_SOURCE_DIR = new File( "src/test/resources/docbook" );
	File STAGING_DIR = new File( BASE_SOURCE_DIR, "staging" );
	File POT_DIR = new File( BASE_SOURCE_DIR, "pot" );
	String MASTER_DOC_FILE_NAME = "HIBERNATE_-_Relational_Persistence_for_Idiomatic_Java.xml";
	Locale MASTER_LANGUAGE = Locale.US;
	File MASTER_DOC_FILE = new File( new File( BASE_SOURCE_DIR, "en-US" ), MASTER_DOC_FILE_NAME );
	File MASTER_SOURCE_DIR = new File( BASE_SOURCE_DIR, "en-US" );
}
