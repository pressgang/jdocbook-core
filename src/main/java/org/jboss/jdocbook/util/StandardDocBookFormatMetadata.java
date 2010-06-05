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

/**
 * Information about each of the standard DocBook formats.
 *
 * @author Steve Ebersole
 */
public class StandardDocBookFormatMetadata {
	/**
	 * Information about a standard DocBook format.
	 */
	public static interface FormatMetadata {
		/**
		 * The name of the format.
		 *
		 * @return The format name.
		 */
		public String getName();

		/**
		 * The resource name of the <tt>XSLT</tt> stylesheet for this format.
		 *
		 * @return The resource name of the <tt>XSLT</tt> stylesheet.
		 */
		public String getStylesheetResource();

		/**
		 * Retrieve the file extension used by default for this format's output file(s).
		 *
		 * @return This format's default file extension
		 */
		public String getFileExtension();

		/**
		 * Does this format require setting of the <tt>img.src.path</tt> DocBook <tt>XSLT</tt> parameter?
		 *
		 * @return True if this format needs <tt>img.src.path</tt> set; false otherwise.
		 */
		public boolean requiresImagePathSetting();

		/**
		 * Does this format require copying of the images to the publish directory?
		 *
		 * @return True if this format needs images copied; false otherwise.
		 */
		public boolean requiresImageCopying();

		/**
		 * Does this format perform output <a href="http://www.sagehill.net/docbookxsl/Chunking.html">chunking</a>?
		 *
		 * @return True if this format does chunking; false otherwise.
		 */
		public boolean doesChunking();
	}

	public static final FormatMetadata PDF = new BasicFormatMetadata( "pdf" )
			.setStylesheetResource( "/fo/docbook.xsl" )
			.setFileExtension( "pdf" )
			.setRequiresImagePathSetting( true )
			.setRequiresImageCopying( false )
			.setDoesChunking( false );

	public static final FormatMetadata XHTML = new BasicFormatMetadata( "xhtml" )
			.setStylesheetResource( "/xhtml/docbook.xsl" )
			.setFileExtension( "xhtml" )
			.setRequiresImagePathSetting( false )
			.setRequiresImageCopying( true )
			.setDoesChunking( false );

	public static final FormatMetadata ECLIPSE = new HtmlBasedFormatMetadata( "eclipse" )
			.setStylesheetResource( "/eclipse/eclipse.xsl" );

	public static final FormatMetadata HTML = new HtmlBasedFormatMetadata( "html" )
			.setStylesheetResource( "/html/chunk.xsl" );

	public static final FormatMetadata HTML_SINGLE = new HtmlBasedFormatMetadata( "html_single" )
			.setStylesheetResource( "/html/docbook.xsl" )
			.setDoesChunking( false );

	public static final FormatMetadata HTMLHELP = new HtmlBasedFormatMetadata( "htmlhelp" )
			.setStylesheetResource( "/htmlhelp/htmlhelp.xsl" );

	public static final FormatMetadata JAVAHELP = new HtmlBasedFormatMetadata( "javahelp" )
			.setStylesheetResource( "/javahelp/javahelp.xsl" );

	public static final FormatMetadata MAN = new HtmlBasedFormatMetadata( "man" )
			.setStylesheetResource( "/manpages/docbook.xsl" )
			.setDoesChunking( false );

	public static final FormatMetadata WEBSITE = new HtmlBasedFormatMetadata( "website" )
			.setStylesheetResource( "/website/website.xsl" )
			.setDoesChunking( false );


	private static class BasicFormatMetadata implements FormatMetadata {
		private final String name;
		private String stylesheetResource;
		private String fileExtension;
		private boolean requiresImagePathSetting;
		private boolean requiresImageCopying;
		private boolean doesChunking;

		private BasicFormatMetadata(String name) {
			this.name = name;
// I have seen this cause problems when the XSLT is looking at 'img.src.path' for callouts
// and I do not think there is really a danger in setting it
			this.requiresImagePathSetting = true;
		}

		public String getName() {
			return name;
		}

		public String getStylesheetResource() {
			return stylesheetResource;
		}

		public String getFileExtension() {
			return fileExtension;
		}

		public boolean requiresImagePathSetting() {
			return requiresImagePathSetting;
		}

		public boolean requiresImageCopying() {
			return requiresImageCopying;
		}

		public boolean doesChunking() {
			return doesChunking;
		}

		BasicFormatMetadata setStylesheetResource(String stylesheetResource) {
			this.stylesheetResource = stylesheetResource;
			return this;
		}

		BasicFormatMetadata setFileExtension(String fileExtension) {
			this.fileExtension = fileExtension;
			return this;
		}

		BasicFormatMetadata setRequiresImagePathSetting(boolean requiresImagePathSetting) {
			this.requiresImagePathSetting = requiresImagePathSetting;
			return this;
		}

		BasicFormatMetadata setRequiresImageCopying(boolean requiresImageCopying) {
			this.requiresImageCopying = requiresImageCopying;
			return this;
		}

		BasicFormatMetadata setDoesChunking(boolean doesChunking) {
			this.doesChunking = doesChunking;
			return this;
		}
	}

	private static class HtmlBasedFormatMetadata extends BasicFormatMetadata {
		private HtmlBasedFormatMetadata(String name) {
			super( name );
			setDoesChunking( true );
			setFileExtension( "html" );
// I have seen this cause problems when the XSLT is looking at 'img.src.path' for callouts
// and I do not think there is really a danger in setting it
//			setRequiresImagePathSetting( false );
			setRequiresImageCopying( true );
		}
	}

	public static FormatMetadata getFormatMetadata(String name) {
		if ( ECLIPSE.getName().equals( name ) ) {
			return ECLIPSE;
		}
		else if ( HTML.getName().equals( name ) ) {
			return HTML;
		}
		else if ( HTML_SINGLE.getName().equals( name ) ) {
			return HTML_SINGLE;
		}
		else if ( HTMLHELP.getName().equals( name ) ) {
			return HTMLHELP;
		}
		else if ( JAVAHELP.getName().equals( name ) ) {
			return JAVAHELP;
		}
		else if ( MAN.getName().equals( name ) ) {
			return MAN;
		}
		else if ( PDF.getName().equals( name ) ) {
			return PDF;
		}
		else if ( WEBSITE.getName().equals( name ) ) {
			return WEBSITE;
		}
		else if ( XHTML.getName().equals( name ) ) {
			return XHTML;
		}
		else {
			return null;
		}
	}
}
