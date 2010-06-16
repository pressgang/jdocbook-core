package org.jboss.jdocbook.test;

import java.util.LinkedHashSet;
import java.util.Properties;

import org.jboss.jdocbook.Configuration;
import org.jboss.jdocbook.Profiling;
import org.jboss.jdocbook.ValueInjection;

public class BaseConfiguration implements Configuration {

	public LinkedHashSet<String> getCatalogs() {
		return null;
	}

	public char getLocaleSeparator() {
		return '-';
	}

	public Profiling getProfiling() {
		return null;
	}

	public Properties getTransformerParameters() {
		return null;
	}

	public LinkedHashSet<ValueInjection> getValueInjections() {
		return null;
	}

	public boolean isAutoDetectFontsEnabled() {
		return false;
	}

	public boolean isUseFopFontCacheEnabled() {
		return false;
	}

	public boolean isUseRelativeImageUris() {
		return false;
	}

	public String getDocBookVersion() {
		return "1.72.0";
	}

}
