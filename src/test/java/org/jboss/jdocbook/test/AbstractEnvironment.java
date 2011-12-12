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

import java.io.File;
import java.util.List;

import org.jboss.jdocbook.DocBookSchemaResolutionStrategy;
import org.jboss.jdocbook.Environment;
import org.jboss.jdocbook.ResourceDelegate;
import org.jboss.jdocbook.test.util.ResourcesUtil;

/**
 * @author Strong Liu
 */
abstract public class AbstractEnvironment implements Environment {
	public ResourceDelegate getResourceDelegate() {
		return new ResourceDelegateImpl();
	}

	public File getWorkDirectory() {
		return new File( ResourcesUtil.getTestDir(),"work");
	}

	public File getStagingDirectory() {
		return new File( ResourcesUtil.getTestDir(),"staging");
	}

	public List<File> getFontDirectories() {
		return null;
	}

	public DocBookXsltResolutionStrategy getDocBookXsltResolutionStrategy() {
		return DocBookXsltResolutionStrategy.INCLUSIVE;
	}

	public DocBookSchemaResolutionStrategy getDocBookSchemaResolutionStrategy() {
		return DocBookSchemaResolutionStrategy.RNG;
	}
}
