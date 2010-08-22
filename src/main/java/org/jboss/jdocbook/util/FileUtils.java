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
package org.jboss.jdocbook.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import net.socialchange.doctype.Doctype;
import net.socialchange.doctype.DoctypeChangerStream;
import net.socialchange.doctype.DoctypeGenerator;
import net.socialchange.doctype.DoctypeImpl;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.jboss.jdocbook.JDocBookProcessException;
import org.jboss.jdocbook.ValueInjection;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Various {@link java.io.File file} and {@link java.io.File directory} related utilities.
 *
 * @author Steve Ebersole
 */
public class FileUtils extends org.codehaus.plexus.util.FileUtils {
	/**
	 * Create a SAXSource from a given <tt>file</tt>.
	 * <p/>
	 * NOTE: the result <b>is</b> {@link java.io.BufferedInputStream buffered}.
	 *
	 * @param file The file from which to generate a SAXSource
	 * @param resolver An entity resolver to apply to the file reader.
	 * @param valueInjections The values to be injected
	 *
	 * @return An appropriate SAXSource
	 */
	public static SAXSource createSAXSource(
			File file,
			EntityResolver resolver,
			final LinkedHashSet<ValueInjection> valueInjections) {
		try {
			final InputSource source = createInputSource( file, valueInjections );

        	SAXParserFactory factory = new SAXParserFactoryImpl();
        	factory.setXIncludeAware( true );

			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setEntityResolver( resolver );
			reader.setFeature( Constants.DTD_LOADING_FEATURE, true );
			reader.setFeature( Constants.DTD_VALIDATION_FEATURE, false );

			return new SAXSource( reader, source );
		}
		catch ( ParserConfigurationException e ) {
			throw new JDocBookProcessException( "unable to build SAX Parser/Factory [" + e.getMessage() + "]", e );
		}
		catch ( SAXException e ) {
			throw new JDocBookProcessException( "unable to build SAX Parser/Factory [" + e.getMessage() + "]", e );
		}

	}

	public static InputSource createInputSource(File file, final LinkedHashSet<ValueInjection> valueInjections) {
		try {
			InputSource source = new InputSource( createInputStream( file, valueInjections ) );
			source.setSystemId( file.toURI().toURL().toString() );
			return source;
		}
		catch ( MalformedURLException e ) {
			throw new JDocBookProcessException( "unexpected problem converting file to URL", e );
		}
	}

	public static InputStream createInputStream(File file, final LinkedHashSet<ValueInjection> valueInjections) {
		final boolean injectionsDefined = valueInjections != null && ! valueInjections.isEmpty();
		try {
			InputStream inputStream = new BufferedInputStream( new FileInputStream( file ) );
			if ( injectionsDefined ) {
				DoctypeChangerStream changerStream = new DoctypeChangerStream( inputStream );
				changerStream.setGenerator(
						new DoctypeGenerator() {
							public Doctype generate(final Doctype doctype) {
								final String root = doctype == null ? null : doctype.getRootElement();
								final String pubId = doctype == null ? null : doctype.getPublicId();
								final String sysId = doctype == null ? null : doctype.getSystemId();

								StringBuilder internalSubset = buildInjectedEntitySubset( valueInjections );
								if ( doctype != null && doctype.getInternalSubset() != null ) {
									internalSubset.append( doctype.getInternalSubset() ).append( '\n' );
								}
								return new DoctypeImpl( root, pubId, sysId, internalSubset.toString() );
							}
						}
				);
				inputStream = changerStream;
			}
			return inputStream;
		}
		catch ( FileNotFoundException e ) {
			throw new JDocBookProcessException( "unable to locate source file", e );
		}
	}

	/**
	 * Determine the 'relativity' of a file in relation to the given basedir.  'relativity', is the relative path
	 * from the basedir to the file's parent (directory).
	 * <p/>
	 * For example, a file <tt>/home/steve/hibernate/tmp/Test.java</tt> would have a relativity of <tt>hibernate/tmp</tt>
	 * relative to <tt>/home/steve</tt> as the basedir.
	 *
	 * @param file The file for which to determine relativity.
	 * @param basedir The directory from which to base the relativity.
	 * @return The relativity.
	 * @throws RuntimeException Indicates that the given file was not found to be relative to basedir.
	 */
	public static String determineRelativity(File file, File basedir) {
		String basedirPath = resolveFullPathName( basedir );
		String directory = resolveFullPathName( file.getParentFile() );
		if ( basedirPath.equals( directory ) ) {
			return null;
		}
		int baseStart = directory.indexOf( basedirPath );
		if ( baseStart < 0 ) {
			throw new RuntimeException( "Included file did not seem to be relative to basedir!" );
		}
		String relativity = directory.substring( basedirPath.length() + 1 );
		while ( relativity.startsWith( "/" ) ) {
			relativity = relativity.substring( 1 );
		}
		return relativity;
	}

	/**
	 * Determine if the file is a XML file from file extension.
	 *
	 * @param file The file to check
	 *
	 * @return True if the given file is determined to be an xml file.
	 */
	public static boolean isXMLFile(File file) {
		return file != null && file.exists() && file.getName().endsWith( ".xml" );
	}

	/**
	 * Here for consistent handling of full path names.
	 *
	 * @param path The path
	 * @return The full path name.
	 */
	public static String resolveFullPathName(File path) {
		return path.getAbsolutePath();
	}

	public static StringBuilder buildInjectedEntitySubset(Set<ValueInjection> valueInjections) {
		StringBuilder buffer = new StringBuilder();
		for ( ValueInjection injection : valueInjections ) {
			buffer.append( "<!ENTITY " )
					.append( injection.getName() )
					.append( " \"" )
					.append( injection.getValue() )
					.append( "\">\n" );
		}
		return buffer;
	}
}
