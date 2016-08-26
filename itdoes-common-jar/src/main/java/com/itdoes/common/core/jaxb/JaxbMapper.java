package com.itdoes.common.core.jaxb;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class JaxbMapper {
	private static final ConcurrentMap<Class<?>, JAXBContext> CONTEXTS = new ConcurrentHashMap<Class<?>, JAXBContext>();

	public static String toXml(Object root) {
		return toXml(root, null);
	}

	public static String toXml(Object root, String encoding) {
		return toXml(root, Reflections.getUserClass(root), encoding);
	}

	public static String toXml(Object root, Class<?> clazz, String encoding) {
		try {
			final StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static String toXml(Collection<?> root, String rootName, Class<?> clazz) {
		return toXml(root, rootName, clazz, null);
	}

	public static String toXml(Collection<?> root, String rootName, Class<?> clazz, String encoding) {
		try {
			final CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;
			final JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(
					new QName(rootName), CollectionWrapper.class, wrapper);
			final StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(wrapperElement, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml, Class<T> clazz) {
		try {
			final StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller(clazz).unmarshal(reader);
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Marshaller createMarshaller(Class<?> clazz, String encoding) {
		try {
			final JAXBContext context = getJaxbContext(clazz);
			final Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			if (StringUtils.isNotBlank(encoding)) {
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			}
			return marshaller;
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Unmarshaller createUnmarshaller(Class<?> clazz) {
		try {
			final JAXBContext context = getJaxbContext(clazz);
			return context.createUnmarshaller();
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	protected static JAXBContext getJaxbContext(Class<?> clazz) {
		Validate.notNull(clazz, "Class must not be null");

		JAXBContext context = CONTEXTS.get(clazz);
		if (context == null) {
			try {
				context = JAXBContext.newInstance(clazz, CollectionWrapper.class);
				CONTEXTS.putIfAbsent(clazz, context);
			} catch (JAXBException e) {
				throw Exceptions.unchecked(e);
			}
		}
		return context;
	}

	public static class CollectionWrapper {
		@XmlAnyElement
		protected Collection<?> collection;
	}

	private JaxbMapper() {
	}
}
