package it.vitalegi.cocorido.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Giorgio Vitale
 */
public class JacksonUtil {

	private static void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static InputStream getInputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static InputStream getInputStream(String path) {
		return JacksonUtil.class.getResourceAsStream(path);
	}

	public static ObjectMapper getJsonMapper() {
		ObjectMapper mapper = new ObjectMapper(new JsonFactory());
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}

	public static OutputStream getOutputStream(File file) {
		try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static <E> E getValue(ObjectMapper mapper, Class<E> clazz, InputStream is) {
		try {
			return mapper.<E>readValue(is, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(is);
		}
	}

	public static <E> E getValue(ObjectMapper mapper, TypeReference<E> typeReference, InputStream is) {
		try {
			return mapper.<E>readValue(is, typeReference);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(is);
		}
	}

	public static <E> E getValueYaml(Class<E> clazz, InputStream is) {
		try {
			return getValue(getYamlMapper(), clazz, is);
		} finally {
			close(is);
		}
	}

	public static <E> E getValueYaml(TypeReference<E> typeReference, InputStream is) {
		try {
			return getYamlMapper().<E>readValue(is, typeReference);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(is);
		}
	}

	public static ObjectMapper getYamlMapper() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}

	public static <E> void setValueYaml(OutputStream os, E value) {
		try {
			getYamlMapper().writeValue(os, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(os);
		}
	}
}