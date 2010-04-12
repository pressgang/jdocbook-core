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
package org.jboss.jdocbook.xslt;

import org.jboss.jdocbook.util.TargetNamingStrategy;

/**
 * Describes a particular DocBook formatting plan to apply in rendering.
 *
 * @author Steve Ebersole
 */
public interface FormatPlan {
	/**
	 * The name of the format plan.  Informational.
	 *
	 * @return The format plan name.
	 */
	public String getName();

	/**
	 * The <tt>XSLT</tt> stylesheet that defines the rendering for this format plan, as a resource.
	 *
	 * @return The resource name of the <tt>XSLT</tt> stylesheet.
	 */
	public String getStylesheetResource();

	/**
	 * Identify the "base" DocBook <tt>XSLT</tt> stylesheet used to provide this format plan.
	 *
	 * @return The resource name of the "base" DocBook <tt>XSLT</tt> stylesheet
	 */
	public String getCorrespondingDocBookStylesheetResource();

	/**
	 * Retrieve the target naming strategy for this format plan.
	 *
	 * @return This plan's target naming strategy.
	 */
	public TargetNamingStrategy getTargetNamingStrategy();

	/**
	 * Does this plan require setting of the <tt>img.src.path</tt> DocBook <tt>XSLT</tt> parameter?
	 *
	 * @return True if this plan needs <tt>img.src.path</tt> set; false otherwise.
	 */
	public boolean requiresSettingImagePath();

	/**
	 * Does this plan require copying of the images to the publish directory?
	 *
	 * @return True if this plan needs images copied; false otherwise.
	 */
	public boolean requiresImageCopying();

	/**
	 * Does this plan perform output <a href="http://www.sagehill.net/docbookxsl/Chunking.html">chunking</a>?
	 *
	 * @return True if this plan does chunking; false otherwise.
	 */
	public boolean doesChunking();
}
