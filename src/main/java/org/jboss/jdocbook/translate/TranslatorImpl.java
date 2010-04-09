/*
 * jDocBook, processing of DocBook sources
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.jboss.jdocbook.translate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.jboss.jdocbook.JDocBookProcessException;
import org.jboss.jdocbook.MasterLanguageDescriptor;
import org.jboss.jdocbook.util.FileUtils;
import org.jboss.jdocbook.util.TranslationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class TranslatorImpl implements Translator {
	private static final Logger log = LoggerFactory.getLogger( TranslatorImpl.class );

	public void translate(
			MasterLanguageDescriptor masterDescriptor,
			File basePoDirectory,
			File targetDirectory) throws JDocBookProcessException {
		for ( File file : masterDescriptor.getDocumentFiles() ) {
			// determine the proper relative work directory for the translated XML
			String relativity = FileUtils.determineRelativity( file, masterDescriptor.getBaseSourceDirectory() );
			File relativeWorkDir = (relativity == null)
					? targetDirectory
					: new File( targetDirectory, relativity );
			File translatedFile = new File( relativeWorkDir, file.getName() );

			// if the file to translate is not an XML file, simply copy it
			if ( FileUtils.isXMLFile( file ) ) {
				String poFileName = TranslationUtils.determinePoFileName( file );
				File relativeTranslationDir = (relativity == null)
						? basePoDirectory
						: new File( basePoDirectory, relativity );
				File poFile = new File( relativeTranslationDir, poFileName );
				if ( !poFile.exists() ) {
					throw new JDocBookProcessException(
							"Unable to locate PO file for [" + file + "] in [" + basePoDirectory + "]"
					);
				}
				generateTranslatedXML( file, poFile, translatedFile );
			}
			else {
				try {
					FileUtils.copyFileToDirectoryIfModified( file, translatedFile.getParentFile() );
				}
				catch ( IOException e) {
					throw new JDocBookProcessException(
							"unable to copy file [ " + file + " ] to directory [ "
									+ translatedFile.getParentFile() + " ]"
					);
				}
			}
		}
	}

	private void generateTranslatedXML(File masterFile, File poFile, File translatedFile) {
		if ( !masterFile.exists() ) {
			log.trace( "skipping translation; source file did not exist : {}", masterFile );
			return;
		}
		if ( !poFile.exists() ) {
			log.trace( "skipping translation; PO file did not exist : {}", poFile );
			return;
		}

		if ( translatedFile.exists()
				&& translatedFile.lastModified() >= masterFile.lastModified()
				&& translatedFile.lastModified() >= poFile.lastModified() ) {
			log.trace( "skipping translation; up-to-date : {0}", translatedFile );
			return;
		}

		if ( ! translatedFile.getParentFile().exists() ) {
			boolean created = translatedFile.getParentFile().mkdirs();
			if ( ! created ) {
				log.info( "Unable to create directories for translation" );
			}
		}

		CommandLine commandLine = CommandLine.parse( "po2xml" );
		commandLine.addArgument( FileUtils.resolveFullPathName( masterFile ) );
		commandLine.addArgument( FileUtils.resolveFullPathName( poFile ) );

		try {
			final FileOutputStream xmlStream = new FileOutputStream( translatedFile );
			DefaultExecutor executor = new DefaultExecutor();
			try {
				PumpStreamHandler streamDirector = new PumpStreamHandler( xmlStream, System.err );
				executor.setStreamHandler( streamDirector );
				executor.execute( commandLine );
			}
			catch ( IOException ioe ) {
				throw new JDocBookProcessException( "unable to execute po2xml : " + ioe.getMessage() );
			}
			finally {
				try {
					xmlStream.flush();
					xmlStream.close();
				}
				catch ( IOException ignore ) {
					// intentionally empty...
				}
			}
		}
		catch ( IOException e  ) {
			throw new JDocBookProcessException( "unable to open output stream for translated XML file [" + translatedFile + "]" );
		}
	}
}
