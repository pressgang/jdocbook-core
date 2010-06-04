package org.jboss.jdocbook.test;

import java.util.LinkedHashSet;
import java.util.Properties;

import org.jboss.jdocbook.Configuration;
import org.jboss.jdocbook.Profiling;
import org.jboss.jdocbook.ValueInjection;

public class BaseConfiguration implements Configuration {

	@Override
	public LinkedHashSet<String> getCatalogs() {

		return null;
	}

	@Override
	public char getLocaleSeparator() {

		return '-';
	}

	@Override
	public Profiling getProfiling() {

		return null;
	}

	@Override
	public Properties getTransformerParameters() {

		return null;
	}

	@Override
	public LinkedHashSet<ValueInjection> getValueInjections() {

		return null;
	}

	@Override
	public boolean isAutoDetectFontsEnabled() {

		return false;
	}

	@Override
	public boolean isUseFopFontCacheEnabled() {

		return false;
	}

	@Override
	public boolean isUseRelativeImageUris() {

		return false;
	}

	@Override
	public String getDocBookVersion() {
		return "1.72.0";
	}

}
