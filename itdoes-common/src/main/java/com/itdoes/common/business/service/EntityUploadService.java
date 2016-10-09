package com.itdoes.common.business.service;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.core.spring.Springs;
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
	public static final char UPLOAD_FILENAME_SEPARATOR = ',';

	private static final String UPLOAD_TEMP_RELATIVE_PATH = "_temp";

	public <T, ID extends Serializable> String postUploadPre(EntityPair<T, ID> pair, T entity,
			String uploadRealRootPath, List<MultipartFile> uploadFileList) {
		String uploadTempDir = null;
		if (isPostUploadable(pair, uploadFileList)) {
			uploadTempDir = getUploadTempDir(pair, uploadRealRootPath, Ids.uuid());
			final Set<String> uploadFilenameSet = new LinkedHashSet<String>(uploadFileList.size());

			for (MultipartFile uploadFile : uploadFileList) {
				uploadFile(uploadTempDir, uploadFile);
				uploadFilenameSet.add(uploadFile.getOriginalFilename());
			}

			Reflections.setFieldValue(entity, pair.getUploadField().getName(),
					StringUtils.join(uploadFilenameSet, UPLOAD_FILENAME_SEPARATOR));
		}
		return uploadTempDir;
	}

	public <T, ID extends Serializable> void postUploadPost(EntityPair<T, ID> pair, T entity, String uploadRealRootPath,
			List<MultipartFile> uploadFileList, ID id, String uploadTempDir) {
		if (isPostUploadable(pair, uploadFileList)) {
			Files.moveDirectory(uploadTempDir, getUploadDir(pair, uploadRealRootPath, id.toString()));
		}
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> void putUpload(EntityPair<T, ID> pair, T entity, String uploadRealRootPath,
			List<MultipartFile> uploadFileList, boolean uploadDeleteOrphanFiles) {
		if (isPutUploadable(pair)) {
			final ID id = (ID) Reflections.getFieldValue(entity, pair.getIdField().getName());
			final String uploadDir = getUploadDir(pair, uploadRealRootPath, id.toString());
			final Set<String> uploadFilenameSet = toUploadFilenameSet(pair, entity);

			if (!isUploadFileEmpty(uploadFileList)) {
				for (MultipartFile uploadFile : uploadFileList) {
					uploadFile(uploadDir, uploadFile);
					uploadFilenameSet.add(uploadFile.getOriginalFilename());
				}
			}

			if (uploadDeleteOrphanFiles) {
				deleteUploadOrphanFiles(uploadDir, uploadFilenameSet);
			}

			Reflections.setFieldValue(entity, pair.getUploadField().getName(),
					StringUtils.join(uploadFilenameSet, UPLOAD_FILENAME_SEPARATOR));
		}
	}

	public <T, ID extends Serializable> void deleteUpload(EntityPair<T, ID> pair, ID id, String uploadRealRootPath,
			boolean uploadDeleteOrphanFiles) {
		if (!hasUploadField(pair) || !uploadDeleteOrphanFiles) {
			return;
		}

		final String uploadDir = getUploadDir(pair, uploadRealRootPath, id.toString());
		Files.deleteDirectory(uploadDir);
	}

	private static <T, ID extends Serializable> boolean isPostUploadable(EntityPair<T, ID> pair,
			List<MultipartFile> uploadFileList) {
		return hasUploadField(pair) && isUploadPermitted(pair) && !isUploadFileEmpty(uploadFileList);
	}

	private static <T, ID extends Serializable> boolean isPutUploadable(EntityPair<T, ID> pair) {
		return hasUploadField(pair) && isUploadPermitted(pair);
	}

	private static <T, ID extends Serializable> boolean hasUploadField(EntityPair<T, ID> pair) {
		return pair.getUploadField() != null;
	}

	private static <T, ID extends Serializable> boolean isUploadPermitted(EntityPair<T, ID> pair) {
		final Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(Perms.getEntityOneEntityOneFieldWritePerm(pair.getEntityClass().getSimpleName(),
				pair.getUploadField().getName()));
	}

	private static boolean isUploadFileEmpty(List<MultipartFile> uploadFileList) {
		return Springs.isEmpty(uploadFileList);
	}

	private static void uploadFile(String realPath, MultipartFile uploadFile) {
		MultipartFiles.save(realPath, uploadFile);
	}

	private static <T, ID extends Serializable> String getUploadDir(EntityPair<T, ID> pair, String uploadRealRootPath,
			String uuid) {
		return uploadRealRootPath + "/" + pair.getEntityClass().getSimpleName() + "/" + uuid;
	}

	private static <T, ID extends Serializable> String getUploadTempDir(EntityPair<T, ID> pair,
			String uploadRealRootPath, String uuid) {
		return uploadRealRootPath + "/" + UPLOAD_TEMP_RELATIVE_PATH + "/" + pair.getEntityClass().getSimpleName() + "/"
				+ uuid;
	}

	private static void deleteUploadOrphanFiles(String uploadDir, Set<String> uploadFilenameSet) {
		final Collection<File> toBeDeletedFileList = Files.listFiles(uploadDir, new IOFileFilter() {
			@Override
			public boolean accept(File file) {
				return accept(file.getParentFile(), file.getName());
			}

			@Override
			public boolean accept(File dir, String name) {
				return !uploadFilenameSet.contains(name);
			}
		});

		if (Collections3.isEmpty(toBeDeletedFileList)) {
			return;
		}

		for (File toBeDeletedFile : toBeDeletedFileList) {
			Files.deleteFile(toBeDeletedFile, true);
		}
	}

	private static <T, ID extends Serializable> Set<String> toUploadFilenameSet(EntityPair<T, ID> pair, T entity) {
		return Collections3.asLinkedHashSet(
				StringUtils.split((String) Reflections.getFieldValue(entity, pair.getUploadField().getName()),
						UPLOAD_FILENAME_SEPARATOR));
	}
}
