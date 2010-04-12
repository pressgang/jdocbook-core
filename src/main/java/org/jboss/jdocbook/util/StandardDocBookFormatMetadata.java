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

	public static final FormatMetadata PDF = new BasicFormatMetadata(
			"pdf",  "/fo/docbook.xsl", "pdf", true, false, false
	);

	public static final FormatMetadata XHTML = new BasicFormatMetadata(
			"xhtml", "/xhtml/docbook.xsl", "xhtml", false, true, false
	);

	public static final FormatMetadata ECLIPSE = new HtmlBasedFormatMetadata( "eclipse", "/eclipse/eclipse.xsl" );

	public static final FormatMetadata HTML = new HtmlBasedFormatMetadata( "html", "/html/chunk.xsl" );

	public static final FormatMetadata HTML_SINGLE = new HtmlBasedFormatMetadata( "html_single", "/html/docbook.xsl", false );

	public static final FormatMetadata HTMLHELP = new HtmlBasedFormatMetadata( "htmlhelp", "/htmlhelp/htmlhelp.xsl" );

	public static final FormatMetadata JAVAHELP = new HtmlBasedFormatMetadata( "javahelp", "/javahelp/javahelp.xsl" );

	public static final FormatMetadata MAN = new HtmlBasedFormatMetadata( "man", "/manpages/docbook.xsl", false );

	public static final FormatMetadata WEBSITE = new HtmlBasedFormatMetadata( "website", "/website/website.xsl", false );

	private static class BasicFormatMetadata implements FormatMetadata {
		private final String name;
		private final String stylesheetResource;
		private final String fileExtension;
		private final boolean requiresImagePathSetting;
		private final boolean requiresImageCopying;
		private final boolean doesChunking;

		private BasicFormatMetadata(
				String name,
				String stylesheetResource,
				String fileExtension,
				boolean requiresImagePathSetting,
				boolean requiresImageCopying,
				boolean doesChunking) {
			this.name = name;
			this.stylesheetResource = stylesheetResource;
			this.fileExtension = fileExtension;
			this.requiresImagePathSetting = requiresImagePathSetting;
			this.requiresImageCopying = requiresImageCopying;
			this.doesChunking = doesChunking;
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
	}

	private static class HtmlBasedFormatMetadata extends BasicFormatMetadata {
		private HtmlBasedFormatMetadata(String name, String stylesheetResource) {
			this( name, stylesheetResource, true );
		}

		private HtmlBasedFormatMetadata(String name, String stylesheetResource, boolean doesChunking) {
			this( name, stylesheetResource, "html", doesChunking );
		}

		private HtmlBasedFormatMetadata(
				String name,
				String stylesheetResource,
				String fileExtension,
				boolean doesChunking) {
			super( name, stylesheetResource, fileExtension, false, true, doesChunking );
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
