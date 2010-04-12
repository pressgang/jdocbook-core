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
package org.jboss.jdocbook.render;

import org.jboss.jdocbook.xslt.FormatPlan;

/**
 * Used to create a <tt>XSL-FO</tt> file.  If you ever run into problems the PDF generation via FOP, the FOP
 * folks will inevitably ask for the <tt>XSL-FO</tt> file.  jDocBook itself does not generate the <tt>XSL-FO</tt>
 * file, instead generating the PDF in a single pass.  This tasks, however, allows you to execute the first
 * stage pf that process and create the <tt>XSL-FO</tt> file.
 * <p/>
 * To reiterate, this is not a task you'd use in normal processing.
 *
 * @author Steve Ebersole
 */
public interface XslFoGenerator {
	/**
	 * Generate the <tt>XSL-FO</tt> file.
	 *
	 * @param source The source document.
	 * @param formatOptions The format options for FOP-based formatting plan (mainly needed for stylesheet references).
	 */
	public void generateXslFo(RenderingSource source, FormatOptions formatOptions);
}
