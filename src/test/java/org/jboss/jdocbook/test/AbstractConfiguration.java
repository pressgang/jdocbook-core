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

package org.jboss.jdocbook.test;

import java.util.LinkedHashSet;
import java.util.Map;

import org.jboss.jdocbook.Configuration;
import org.jboss.jdocbook.Profiling;
import org.jboss.jdocbook.ValueInjection;

/**
 * @author: Strong Liu
 */
public class AbstractConfiguration implements Configuration{
	public Map<String, String> getTransformerParameters() {
		return null;
	}

	public boolean isUseRelativeImageUris() {
		return false;
	}

	public char getLocaleSeparator() {
		return '-';
	}

	public boolean isAutoDetectFontsEnabled() {
		return false;
	}

	public boolean isUseFopFontCacheEnabled() {
		return false;
	}

	public LinkedHashSet<ValueInjection> getValueInjections() {
		return null;
	}

	public LinkedHashSet<String> getCatalogs() {
		return null;
	}

	public Profiling getProfiling() {
		return null;
	}

	public String getDocBookVersion() {
		return null;
	}
}
