package org.jboss.jdocbook.test;

import org.jboss.jdocbook.util.ResourceDelegateSupport;

public class BaseResourceDelegate extends ResourceDelegateSupport {

	@Override
	protected ClassLoader getResourceClassLoader() {
		return getClass().getClassLoader();
	}
}