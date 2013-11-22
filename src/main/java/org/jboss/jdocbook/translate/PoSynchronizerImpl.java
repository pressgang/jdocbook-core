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
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jboss.jdocbook.JDocBookComponentRegistry;
import org.jboss.jdocbook.JDocBookProcessException;
import org.jboss.jdocbook.util.FileUtils;
import org.jboss.jdocbook.util.TranslationUtils;
import org.jboss.jdocbook.util.VCSDirectoryExclusionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link PoSynchronizer} contract.
 *
 * @author Steve Ebersole
 */
public class PoSynchronizerImpl implements PoSynchronizer {
	private static final Logger log = LoggerFactory.getLogger( PoSynchronizerImpl.class );

	private final JDocBookComponentRegistry componentRegistry;

	public PoSynchronizerImpl(JDocBookComponentRegistry componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	@Override
	public void synchronizePo(TranslationSource source) {
		synchronizePo(
				componentRegistry.getEnvironment().getMasterLanguageDescriptor().getPotDirectory(),
				source.resolvePoDirectory(),
				source.getLanguage()
		);
	}

	private void synchronizePo(File potDirectory, File poDirectory, Locale translationLocale)
			throws JDocBookProcessException {
		if ( !potDirectory.exists() ) {
			log.info( "skipping PO updates; POT directory did not exist : {0}", potDirectory );
			return;
		}

		final File[] files = potDirectory.listFiles( new VCSDirectoryExclusionFilter() );
		if ( files != null ) {
			for ( File file : files ) {
				if ( file.isDirectory() ) {
					// recurse into the directory by calling back into ourselves with the sub-dir
					synchronizePo(
							new File( potDirectory, file.getName() ),
							new File( poDirectory, file.getName() ),
							translationLocale
					);
				}
				else {
					if ( TranslationUtils.isPotFile( file ) ) {
						File translation = new File( poDirectory, TranslationUtils.determinePoFileName( file ) );
						updateTranslation( file, translation, translationLocale );
					}
				}
			}
		}
	}

	private void updateTranslation(File template, File translation, Locale translationLocale) {
		if ( !template.exists() ) {
			log.trace( "skipping PO updates; POT file did not exist : {0}", template );
			return;
		}

		if ( translation.lastModified() >= template.lastModified() ) {
			log.trace( "skipping PO updates; up-to-date : {0}", translation );
			return;
		}

		final String translationLocaleString = componentRegistry.toLanguageString( translationLocale );

		CommandLine commandLine;
		if ( translation.exists() ) {
			commandLine = CommandLine.parse( "msgmerge" );
			commandLine.addArgument( "--quiet" );
			commandLine.addArgument( "--update" );
			commandLine.addArgument( "--backup=none" );
			commandLine.addArgument( FileUtils.resolveFullPathName( translation ) );
			commandLine.addArgument( FileUtils.resolveFullPathName( template ) );
		}
		else {
			if ( ! translation.getParentFile().exists() ) {
				boolean created = translation.getParentFile().mkdirs();
				if ( ! created ) {
					log.info( "Unable to create PO directory {}", translation.getParentFile().getAbsolutePath() );
				}
			}
			commandLine = CommandLine.parse( "msginit" );
			commandLine.addArgument( "--no-translator" );
			commandLine.addArgument( "--locale=" + translationLocaleString );
			commandLine.addArgument( "-i" );
			commandLine.addArgument( FileUtils.resolveFullPathName( template ) );
			commandLine.addArgument( "-o" );
			commandLine.addArgument( FileUtils.resolveFullPathName( translation ) );
		}

		log.info( "po-synch -> " + commandLine.toString() );

		DefaultExecutor executor = new DefaultExecutor();
		try {
			executor.execute( commandLine );
		}
		catch ( IOException e ) {
			throw new JDocBookProcessException( "Error synchronizing PO file [" + template.getName() + "] for " + translationLocaleString, e );
		}
	}
}
