package com.itdoes.common.core.spring;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public class Springs {
	public static String getBeanName(String classSimpleName) {
		Validate.notBlank(classSimpleName, "Class simple name is blank");

		return StringUtils.uncapitalize(classSimpleName);
	}

	public static String getBeanName(Class<?> clazz) {
		Validate.notNull(clazz, "Class is null");

		return getBeanName(clazz.getSimpleName());
	}

	/**
	 * Spring will add an empty file automatically if no file provided.
	 */
	public static boolean isEmpty(List<MultipartFile> fileList) {
		if (Collections3.isEmpty(fileList)) {
			return true;
		}

		if (fileList.size() == 1) {
			return fileList.get(0).isEmpty();
		}

		return false;
	}

	private Springs() {
	}
}
