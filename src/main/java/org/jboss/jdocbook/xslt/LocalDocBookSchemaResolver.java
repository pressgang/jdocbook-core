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

import java.io.IOException;
import java.io.InputStream;

import org.jboss.jdocbook.DocBookSchemaResolutionStrategy;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An entity resolver for resolving DocBook schemas (DTD, XSD, RNG, etc) locally rather than hitting the network.
 * <p/>
 * IMPL NOTE : the systemId pattern here for DocBook schemas
 * {@code http://docbook.org/xml/{version}/{type}/docbook.{type}}
 * <p/>
 * IMPL NOTE : The publicId matching follows the DocBook recommended publicId: {@code http://docbook.org/ns/docbook}.
 * Via org.jboss.jdocbook.Environment#getDocBookSchemaResolutionStrategy() users can indicate which schema should get
 * used when that publicId is matched.  By default, we map that for the RelaxNG schema as per DocBook recommendations.
 *
 * @author Steve Ebersole
 */
public class LocalDocBookSchemaResolver extends EntityResolverChain {
	public static final String BASE_PUBLIC_ID = "http://docbook.org/ns/docbook";

	public static final String SYSTEM_ID_PREFIX = "http://docbook.org/xml/";
	public static final int SYSTEM_ID_PREFIX_LEN = SYSTEM_ID_PREFIX.length();

	/**
	 * Internal contract for "local DocBook schema" resolvers.  In general the implementations are systemId based,
	 * however the additional contract methods here allow their easy reuse from the publicId based impl.
	 */
	private static interface InternalEntityResolver extends EntityResolver {
		public InputStream resolveStream(String resource);
		public String getMainDocBookSchemaName();
	}

	private static final InternalEntityResolver DTD_RESOLVER = new DocBookDtdResolver();
	private static final InternalEntityResolver XSD_RESOLVER = new DocBookXsdResolver();
	private static final InternalEntityResolver RNG_RESOLVER = new DocBookRngResolver();
	private static final InternalEntityResolver RNC_RESOLVER = new DocBookRncResolver();
	private static final InternalEntityResolver SCH_RESOLVER = new DocBookSchResolver();

	private static final LegacyDocBookDtdResolver LEGACY_DTD_RESOLVER = new LegacyDocBookDtdResolver();

	public LocalDocBookSchemaResolver(DocBookSchemaResolutionStrategy schemaResolutionStrategy) {
		super( new BasePublicIdSchemaResolver( schemaResolutionStrategy ) );
		addEntityResolver( DTD_RESOLVER );
		addEntityResolver( XSD_RESOLVER );
		addEntityResolver( RNG_RESOLVER );
		addEntityResolver( RNC_RESOLVER );
		addEntityResolver( SCH_RESOLVER );
		addEntityResolver( LEGACY_DTD_RESOLVER );
	}

	/**
	 * A resolver specifically for DocBook recommended publicId matching
	 */
	public static class BasePublicIdSchemaResolver implements EntityResolver {
		private final InternalEntityResolver indicatedResolver;

		public BasePublicIdSchemaResolver(DocBookSchemaResolutionStrategy schemaResolutionStrategy) {
			switch ( schemaResolutionStrategy ) {
				case DTD: {
					indicatedResolver = DTD_RESOLVER;
					break;
				}
				case XSD: {
					indicatedResolver = XSD_RESOLVER;
					break;
				}
				case RNC: {
					indicatedResolver = RNC_RESOLVER;
					break;
				}
				case SCH: {
					indicatedResolver = SCH_RESOLVER;
					break;
				}
				default: {
					indicatedResolver = RNG_RESOLVER;
				}
			}
		}

		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if ( publicId == null ) {
				return null;
			}

			if ( publicId.equals( BASE_PUBLIC_ID ) ) {
				return wrap(
						indicatedResolver.resolveStream( indicatedResolver.getMainDocBookSchemaName() ),
						publicId,
						systemId
				);
			}

			return null;
		}
	}

	/**
	 * Utility method to wrap an {@link InputStream} as a {@link InputSource}
	 *
	 * @param stream The stream to wrap
	 * @param publicId The publicId to be applied to the built source
	 * @param systemId The systemId to be applied to the built source
	 *
	 * @return The built source
	 */
	private static InputSource wrap(InputStream stream, String publicId, String systemId) {
		if ( stream != null ) {
			InputSource source = new InputSource( stream );
			source.setPublicId( publicId );
			source.setSystemId( systemId );
			return source;
		}
		return null;
	}

	/**
	 * Template implementation of the {@link InternalEntityResolver} contract.  The actual impls are all systemId based
	 * resolvers, however this contract allows easy reuse of their
	 */
	public static abstract class AbstractInternalResolver implements InternalEntityResolver {
		public abstract String getType();

		public InputSource resolveEntity(String publicId, String systemId) {
			if ( systemId == null ) {
				return null;
			}

			if ( systemId.startsWith( SYSTEM_ID_PREFIX ) ) {
				final int versionEndTokenPosition = systemId.indexOf( '/', SYSTEM_ID_PREFIX_LEN );
				final String name = systemId.substring( versionEndTokenPosition + 1 );

				final InputStream stream = resolveStream( name );

				if ( stream != null ) {
					InputSource source = new InputSource( stream );
					source.setPublicId( publicId );
					source.setSystemId( systemId );
					return source;
				}
			}

			return null;
		}

		public InputStream resolveStream(String resource) {
			final String resourceName = "docbook/" + getType() + "/" + resource;
			InputStream stream = getClass().getClassLoader().getResourceAsStream( resourceName );
			if ( stream == null ) {
				stream = getClass().getClassLoader().getResourceAsStream( '/' + resourceName );
			}

			return stream;
		}

		public String getMainDocBookSchemaName() {
			return "docbook." + getType();
		}
	}

	/**
	 * The DTD resolver
	 */
	public static class DocBookDtdResolver extends AbstractInternalResolver {
		@Override
		public String getType() {
			return "dtd";
		}
	}

	/**
	 * The XSD (XML Schema) resolver
	 */
	public static class DocBookXsdResolver extends AbstractInternalResolver {
		@Override
		public String getType() {
			return "xsd";
		}
	}

	/**
	 * The RNG (RelaxNG) resolver
	 */
	public static class DocBookRngResolver extends AbstractInternalResolver {
		@Override
		public String getType() {
			return "rng";
		}
	}

	/**
	 * The RNC (RelaxNG compact) resolver
	 */
	public static class DocBookRncResolver extends AbstractInternalResolver {
		@Override
		public String getType() {
			return "rnc";
		}
	}

	/**
	 * The SCH (Schematron) resolver
	 */
	public static class DocBookSchResolver extends AbstractInternalResolver {
		@Override
		public String getType() {
			return "sch";
		}
	}

	/**
	 * The legacy DTD resolver.
	 */
	public static class LegacyDocBookDtdResolver extends DocBookDtdResolver {
		public static final String SYSTEM_ID_PREFIX = "http://www.oasis-open.org/docbook/xml/";
		public static final int SYSTEM_ID_PREFIX_LEN = SYSTEM_ID_PREFIX.length();

		@Override
		public InputSource resolveEntity(String publicId, String systemId) {
			if ( systemId == null ) {
				return null;
			}

			if ( systemId.startsWith( SYSTEM_ID_PREFIX ) ) {
				final int versionEndTokenPosition = systemId.indexOf( '/', SYSTEM_ID_PREFIX_LEN );
				final String name = systemId.substring( versionEndTokenPosition + 1 );

				final InputStream stream = resolveStream( name );

				if ( stream != null ) {
					InputSource source = new InputSource( stream );
					source.setPublicId( publicId );
					source.setSystemId( systemId );
					return source;
				}
			}

			return null;
		}
	}
}
