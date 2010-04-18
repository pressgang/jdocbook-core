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

import java.io.File;

import org.jboss.jdocbook.util.FileUtils;
import static org.jboss.jdocbook.util.StandardDocBookFormatMetadata.getFormatMetadata;
import static org.jboss.jdocbook.util.StandardDocBookFormatMetadata.FormatMetadata;
import org.jboss.jdocbook.util.TargetNamingStrategy;
import org.jboss.jdocbook.xslt.FormatPlan;

/**
 * Helper to build a format plan.
 *
 * @author Steve Ebersole
 */
public class FormatPlanBuilder {
	/**
	 * Build a {@link FormatPlan} given a set of user {@link FormatOptions}.  Basically we merge the
	 * {@link FormatOptions} with the corresponding {@link FormatMetadata}.
	 *
	 * @param formatOptions The user format options
	 *
	 * @return The format plan.
	 */
	public static FormatPlan buildFormatPlan(FormatOptions formatOptions) {
		FormatMetadata formatMetadata = getFormatMetadata( formatOptions.getName() );
		if ( formatMetadata == null ) {
			throw new RenderingException( "Unknown format name " + formatOptions.getName() );
		}

		return new FormatPlanImpl( formatMetadata, formatOptions );
	}

	private static class FormatPlanImpl implements FormatPlan {
		private final FormatMetadata formatMetadata;
		private final FormatOptions formatOptions;
		private final TargetNamingStrategyImpl targetNamingStrategy = new TargetNamingStrategyImpl();

		private FormatPlanImpl(FormatMetadata formatMetadata, FormatOptions formatOptions) {
			this.formatMetadata = formatMetadata;
			this.formatOptions = formatOptions;
		}

		public String getName() {
			return formatMetadata.getName();
		}

		public String getStylesheetResource() {
			return formatOptions.getStylesheetResource() != null
					? formatOptions.getStylesheetResource()
					: formatMetadata.getStylesheetResource();
		}

		public String getCorrespondingDocBookStylesheetResource() {
			return formatMetadata.getStylesheetResource();
		}

		public TargetNamingStrategy getTargetNamingStrategy() {
			return targetNamingStrategy;
		}

		public boolean requiresSettingImagePath() {
			return formatMetadata.requiresImagePathSetting();
		}

		public boolean requiresImageCopying() {
			return formatMetadata.requiresImageCopying();
		}

		public boolean doesChunking() {
			return formatMetadata.doesChunking();
		}

		private class TargetNamingStrategyImpl implements TargetNamingStrategy {
			public String determineTargetFileName(File source) {
				return formatOptions.getTargetFinalName() != null
						? formatOptions.getTargetFinalName()
						: FileUtils.basename( source.getAbsolutePath() ) + formatMetadata.getFileExtension();
			}
		}
	}
}
