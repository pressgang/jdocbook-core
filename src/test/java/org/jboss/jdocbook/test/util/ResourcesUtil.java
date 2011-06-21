/*
 * jDocBook, processing of DocBook sources
 *
 * Copyright (c) 2011, Red Hat Inc. or third-party contributors as
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

package org.jboss.jdocbook.test.util;

import java.io.File;

/**
 * @author: Strong Liu
 */
public class ResourcesUtil {
	public static File RESOURCE_DIR = new File( "src/test/resources" );
	public static File SAMPLE_FILE = new File( RESOURCE_DIR, "sample.xml" );

	/**
	 * @param path relative to test/resources
	 */
	public static File getFile(String path) {
		return new File( RESOURCE_DIR, path );
	}

	private static File testDir;

	public static File getTestDir() {
		if ( testDir == null || !testDir.exists() ) {
			testDir = new File( "/tmp/jdocbook-core-test" );
		}
		return testDir;
	}

	public static void main(String[] args) {
		System.out.println( new File( "src/test/resources" ).exists() );
		System.out.println( getFile( "sample.xml" ).exists() );
	}
}
