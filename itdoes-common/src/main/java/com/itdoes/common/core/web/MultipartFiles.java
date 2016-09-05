package com.itdoes.common.core.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.core.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class MultipartFiles {
	public static void save(ServletContext context, String relativePath, MultipartFile file) {
		Validate.notNull(file, "MultipartFile is null");

		save(context, relativePath, file.getOriginalFilename(), file);
	}

	public static void save(ServletContext context, String relativePath, String filename, MultipartFile file) {
		Validate.notNull(context, "ServletContext is null");
		Validate.notNull(relativePath, "RelativePath is null");

		save(context.getRealPath(relativePath), filename, file);
	}

	public static void save(String realPath, MultipartFile file) {
		Validate.notNull(file, "MultipartFile is null");

		save(realPath, file.getOriginalFilename(), file);
	}

	public static void save(String realPath, String filename, MultipartFile file) {
		Validate.notBlank(realPath, "RealPath is blank");
		Validate.notBlank(filename, "Filename is blank");
		Validate.notNull(file, "MultipartFile is null");

		try {
			FileUtils.writeByteArrayToFile(new File(realPath, filename), file.getBytes());
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static void save(ServletContext context, String relativePath, List<MultipartFile> files) {
		Validate.notNull(context, "ServletContext is null");
		Validate.notNull(relativePath, "RelativePath is null");

		save(context.getRealPath(relativePath), files);
	}

	public static void save(String realPath, List<MultipartFile> files) {
		Validate.notNull(files, "List<MultipartFile> is null");

		for (MultipartFile file : files) {
			save(realPath, file);
		}
	}

	private MultipartFiles() {
	}
}
