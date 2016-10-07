package com.itdoes.common.business.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Files;
import com.itdoes.common.core.util.Ids;
import com.itdoes.common.core.util.Reflections;
import com.itdoes.common.core.web.MultipartFiles;

/**
 * @author Jalen Zhong
 */
@Service
public class EntityUploadService extends BaseService {
	public static final String UPLOAD_ROOT_PATH = "/upload/";
	public static final String UPLOAD_TEMP_ROOT_PATH = "/upload/_temp/";

	public <T, ID extends Serializable> String postUploadPre(EntityPair<T, ID> pair, T entity, String realRootPath,
			List<MultipartFile> uploadFileList) {
		String tempUploadDir = null;
		if (needUpload(pair, uploadFileList)) {
			tempUploadDir = getTempUploadDir(pair, realRootPath, Ids.uuid());

			final StringBuilder sb = new StringBuilder();
			for (MultipartFile uploadFile : uploadFileList) {
				if (sb.length() != 0) {
					sb.append(',');
				}
				sb.append(uploadFile.getOriginalFilename());
				uploadFile(tempUploadDir, uploadFile);
			}
			Reflections.setFieldValue(entity, pair.getUploadField().getName(), sb.toString());
		}
		return tempUploadDir;
	}

	public <T, ID extends Serializable> void postUploadPost(EntityPair<T, ID> pair, T entity, String realRootPath,
			List<MultipartFile> uploadFileList, ID id, String tempUploadDir) {
		if (needUpload(pair, uploadFileList)) {
			final File srcDir = new File(tempUploadDir);
			final File destDir = new File(getUploadDir(pair, realRootPath, id.toString()));
			Files.moveDirectory(srcDir, destDir);
		}
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> void putUpload(EntityPair<T, ID> pair, T entity, T oldEntity,
			String realRootPath, List<MultipartFile> uploadFileList) {
		if (needUpload(pair, uploadFileList)) {
			final ID id = (ID) Reflections.getFieldValue(entity, pair.getIdField().getName());
			final String uploadDir = getUploadDir(pair, realRootPath, id.toString());

			final StringBuilder sb = new StringBuilder();
			final String uploadFilesString = (String) Reflections.getFieldValue(entity,
					pair.getUploadField().getName());
			if (StringUtils.isNotBlank(uploadFilesString)) {
				sb.append(uploadFilesString.trim());
			}
			for (MultipartFile uploadFile : uploadFileList) {
				if (sb.length() != 0) {
					sb.append(',');
				}
				sb.append(uploadFile.getOriginalFilename());
				uploadFile(uploadDir, uploadFile);
			}
			Reflections.setFieldValue(entity, pair.getUploadField().getName(), sb.toString());
		}
	}

	private static boolean isPermitted(String permission) {
		final Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(permission);
	}

	private static <T, ID extends Serializable> boolean needUpload(EntityPair<T, ID> pair,
			List<MultipartFile> uploadFileList) {
		if (pair.getUploadField() == null
				|| !isPermitted(Permissions.getEntityOneEntityOneFieldWritePermission(
						pair.getEntityClass().getSimpleName(), pair.getUploadField().getName()))
				|| Collections3.isEmpty(uploadFileList)) {
			return false;
		}

		// Spring will add an empty file automatically if no file provided
		if (uploadFileList.size() == 1) {
			return !uploadFileList.get(0).isEmpty();
		}

		return true;
	}

	private static void uploadFile(String realPath, MultipartFile uploadFile) {
		MultipartFiles.save(realPath, uploadFile);
	}

	private static <T, ID extends Serializable> String getUploadDir(EntityPair<T, ID> pair, String realRootPath,
			String uuid) {
		return realRootPath + UPLOAD_ROOT_PATH + pair.getEntityClass().getSimpleName() + "/" + uuid;
	}

	private static <T, ID extends Serializable> String getTempUploadDir(EntityPair<T, ID> pair, String realRootPath,
			String uuid) {
		return realRootPath + UPLOAD_TEMP_ROOT_PATH + pair.getEntityClass().getSimpleName() + "/" + uuid;
	}
}
