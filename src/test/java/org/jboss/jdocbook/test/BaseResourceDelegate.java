package org.jboss.jdocbook.test;

import java.net.URL;

import org.jboss.jdocbook.ResourceDelegate;
import org.jboss.jdocbook.util.ResourceDelegateSupport;

public class BaseResourceDelegate extends ResourceDelegateSupport {

	@Override
	protected ClassLoader getResourceClassLoader() {
		return getClass().getClassLoader();
	}
}