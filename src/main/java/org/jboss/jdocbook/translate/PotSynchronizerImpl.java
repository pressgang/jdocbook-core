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

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.jboss.jdocbook.JDocBookComponentRegistry;
import org.jboss.jdocbook.JDocBookProcessException;
import org.jboss.jdocbook.util.FileUtils;
import org.jboss.jdocbook.util.TranslationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link PotSynchronizer} contract.
 *
 * @author Steve Ebersole
 */
public class PotSynchronizerImpl implements PotSynchronizer {
	private static final Logger log = LoggerFactory.getLogger( PotSynchronizerImpl.class );

	private final JDocBookComponentRegistry componentRegistry;

	public PotSynchronizerImpl(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	@Override
	public void synchronizePot() throws JDocBookProcessException {
		final File basedir = componentRegistry.getEnvironment().getMasterLanguageDescriptor().getBaseSourceDirectory();
		final File potDirectory = componentRegistry.getEnvironment().getMasterLanguageDescriptor().getPotDirectory();
		for ( File sourceFile : componentRegistry.getEnvironment().getMasterLanguageDescriptor().getDocumentFiles() ) {
			String relativity = FileUtils.determineRelativity( sourceFile, basedir );
			File relativeTranslationDir = (relativity == null)
					? potDirectory
					: new File( potDirectory, relativity );

			if ( FileUtils.isXMLFile( sourceFile ) ) {
				String poFileName = TranslationUtils.determinePotFileName( sourceFile );
				File potFile = new File( relativeTranslationDir, poFileName );
				updatePortableObjectTemplate( sourceFile, potFile );
			}
		}
	}

	private void updatePortableObjectTemplate(File masterFile, File potFile) {
		if ( !masterFile.exists() ) {
			log.trace( "skipping POT update; source file did not exist : {0}", masterFile );
			return;
		}

		if ( potFile.exists() && potFile.lastModified() >= masterFile.lastModified() ) {
			log.trace( "skipping POT update; up-to-date : {0}", potFile );
			return;
		}

		if ( !potFile.getParentFile().exists() ) {
			boolean created = potFile.getParentFile().mkdirs();
			if ( !created ) {
				log.info( "Unable to generate POT directory {}" + FileUtils.resolveFullPathName( potFile.getParentFile() ) );
			}
		}
		executeXml2pot( masterFile, potFile );
	}

	private void executeXml2pot(File masterFile, File potFile) {
		CommandLine commandLine = CommandLine.parse( "xml2pot" );
		commandLine.addArgument( FileUtils.resolveFullPathName( masterFile ) );

		DefaultExecutor executor = new DefaultExecutor();

		try {
			final FileOutputStream xmlStream = new FileOutputStream( potFile );
			PumpStreamHandler streamDirector = new PumpStreamHandler( xmlStream, System.err );
			executor.setStreamHandler( streamDirector );
			try {
				log.trace( "updating POT file {0}", potFile );
				executor.execute( commandLine );
			}
			catch (IOException ioe) {
				throw new JDocBookProcessException( "Unable to execute xml2pot command", ioe );
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
			throw new JDocBookProcessException( "unable to open output stream for POT file [" + potFile + "]" );
		}
	}
}
