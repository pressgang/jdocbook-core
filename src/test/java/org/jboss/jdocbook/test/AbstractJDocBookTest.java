package org.jboss.jdocbook.test;

import junit.framework.TestCase;

import org.jboss.jdocbook.Configuration;
import org.jboss.jdocbook.Environment;
import org.jboss.jdocbook.JDocBookComponentRegistry;

public abstract class AbstractJDocBookTest extends TestCase implements Constant {
	private JDocBookComponentRegistry registry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		if ( WORK_DIR != null && WORK_DIR.exists() ) {
			System.out.println( "-------------- delete work directory --------------" );
			boolean isDeleted = WORK_DIR.delete();
			System.out.println( isDeleted ? "-------- delete work directory done ---------"
					: "-------- delete work directory failed ---------" );
		}

	}

	protected JDocBookComponentRegistry getJDocBookComponentRegistry() {
		if ( registry == null ) {
			registry = new JDocBookComponentRegistry( getEnvironment(), getConfiguration() );
		}
		return registry;
	}

	protected Environment getEnvironment() {
		return new BaseEnvironment();
	}

	protected Configuration getConfiguration() {
		return new BaseConfiguration();
	}

}
