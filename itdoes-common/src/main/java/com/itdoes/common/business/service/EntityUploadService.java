package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
	public static final char UPLOAD_FILENAME_SEPARATOR = ',';

	public <T, ID extends Serializable> String postUploadPre(EntityPair<T, ID> pair, T entity, String realRootPath,
			List<MultipartFile> uploadFileList) {
		String uploadTempDir = null;
		if (isUploadable(pair, uploadFileList)) {
			uploadTempDir = getUploadTempDir(pair, realRootPath, Ids.uuid());

			final StringBuilder sb = new StringBuilder();
			for (MultipartFile uploadFile : uploadFileList) {
				if (sb.length() != 0) {
					sb.append(UPLOAD_FILENAME_SEPARATOR);
				}
				sb.append(uploadFile.getOriginalFilename());
				uploadFile(uploadTempDir, uploadFile);
			}
			Reflections.setFieldValue(entity, pair.getUploadField().getName(), sb.toString());
		}
		return uploadTempDir;
	}

	public <T, ID extends Serializable> void postUploadPost(EntityPair<T, ID> pair, T entity, String realRootPath,
			List<MultipartFile> uploadFileList, ID id, String uploadTempDir) {
		if (isUploadable(pair, uploadFileList)) {
			Files.moveDirectory(uploadTempDir, getUploadDir(pair, realRootPath, id.toString()));
		}
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> void putUpload(EntityPair<T, ID> pair, T entity, T oldEntity,
			String realRootPath, List<MultipartFile> uploadFileList) {
		if (isUploadable(pair, uploadFileList)) {
			final ID id = (ID) Reflections.getFieldValue(entity, pair.getIdField().getName());
			final String uploadDir = getUploadDir(pair, realRootPath, id.toString());

			final Set<String> uploadFilenameSet = toUploadFilenameSet(pair, entity);
			for (MultipartFile uploadFile : uploadFileList) {
				uploadFile(uploadDir, uploadFile);
				uploadFilenameSet.add(uploadFile.getOriginalFilename());
			}

			final Set<String> uploadOldFilenameSet = toUploadFilenameSet(pair, oldEntity);
			deleteUploadOrphanFiles(uploadDir, uploadFilenameSet, uploadOldFilenameSet);

			final String uploadFilenamesString = StringUtils.join(uploadFilenameSet, UPLOAD_FILENAME_SEPARATOR);
			Reflections.setFieldValue(entity, pair.getUploadField().getName(), uploadFilenamesString);
		}
	}

	public <T, ID extends Serializable> void deleteUpload(EntityPair<T, ID> pair, ID id, String realRootPath) {
		if (pair.getUploadField() == null) {
			return;
		}

		final String uploadDir = getUploadDir(pair, realRootPath, id.toString());
		Files.deleteDirectory(uploadDir);
	}

	private static boolean isPermitted(String permission) {
		final Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(permission);
	}

	private static <T, ID extends Serializable> boolean isUploadable(EntityPair<T, ID> pair,
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

	private static <T, ID extends Serializable> String getUploadTempDir(EntityPair<T, ID> pair, String realRootPath,
			String uuid) {
		return realRootPath + UPLOAD_TEMP_ROOT_PATH + pair.getEntityClass().getSimpleName() + "/" + uuid;
	}

	private static void deleteUploadOrphanFiles(String uploadDir, Set<String> uploadFilenameSet,
			Set<String> uploadOldFilenameSet) {
		final List<String> toBeDeletedList = Collections3.subtract(uploadOldFilenameSet, uploadFilenameSet);
		if (Collections3.isEmpty(toBeDeletedList)) {
			return;
		}

		for (String toBeDeleted : toBeDeletedList) {
			Files.deleteFile(uploadDir, toBeDeleted, true);
		}
	}

	private static <T, ID extends Serializable> Set<String> toUploadFilenameSet(EntityPair<T, ID> pair, T entity) {
		return Collections3.asLinkedHashSet(
				StringUtils.split((String) Reflections.getFieldValue(entity, pair.getUploadField().getName()),
						UPLOAD_FILENAME_SEPARATOR));
	}
}
