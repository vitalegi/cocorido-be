package it.vitalegi.cocorido.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StorageUtil {

	@Value("${data.folder}")
	private String dataFolder;

	public <E> E getValue(String fileName, TypeReference<E> typeReference) {
		return getValue(fileName, typeReference, null);
	}

	public synchronized <E> E getValue(String fileName, TypeReference<E> typeReference, E defaultValue) {
		log.info("Start getValue {}", fileName);
		File file = getFile(fileName);
		if (!file.exists()) {
			log.error("File {} doesn't exist.", file.getAbsolutePath());
			return defaultValue;
		}
		try (InputStream is = new FileInputStream(file)) {
			E value = JacksonUtil.getJsonMapper().<E>readValue(is, typeReference);
			log.info("End getValue {}", fileName);
			return value;
		} catch (IOException e) {
			log.error("Error getValue {}: {}", fileName, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public synchronized <E> void setValue(String fileName, E value) {
		log.info("Start setValue {}", fileName);
		Paths.get(dataFolder).toFile().mkdirs();
		File file = getFile(fileName);
		try (OutputStream os = new FileOutputStream(file)) {
			JacksonUtil.getJsonMapper().writeValue(os, value);
			log.info("End setValue {}", fileName);
		} catch (Exception e) {
			log.error("Error setValue {}: {}", fileName, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private File getFile(String fileName) {
		return Paths.get(dataFolder, fileName + ".json").toFile();
	}
}
