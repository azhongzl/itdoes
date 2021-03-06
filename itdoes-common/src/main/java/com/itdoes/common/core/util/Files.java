package com.itdoes.common.core.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class Files {
	private static final char UNIX_PATH_SEPARATOR = '/';
	private static final char WINDOWS_PATH_SEPARATOR = '\\';

	public static String toUnixPath(String path) {
		Validate.notNull(path, "Path is null");

		return path.replace(WINDOWS_PATH_SEPARATOR, UNIX_PATH_SEPARATOR);
	}

	public static String toWindowsPath(String path) {
		Validate.notNull(path, "Path is null");

		return path.replace(UNIX_PATH_SEPARATOR, WINDOWS_PATH_SEPARATOR);
	}

	public static String getFileExtension(String filename, boolean includeDot) {
		Validate.notBlank(filename, "Filename is blank");

		final int index = filename.lastIndexOf(".");
		if (index == -1) {
			return "";
		}

		if (includeDot) {
			return filename.substring(index);
		} else {
			return filename.substring(index + 1);
		}
	}

	public static Collection<File> listFiles(String dir) {
		return listFiles(new File(dir));
	}

	public static Collection<File> listFiles(File dir) {
		return listFiles(dir, true);
	}

	public static Collection<File> listFiles(String dir, boolean recursive) {
		return listFiles(new File(dir), recursive);
	}

	public static Collection<File> listFiles(File dir, boolean recursive) {
		return listFiles(dir, TrueFileFilter.INSTANCE, recursive);
	}

	public static Collection<File> listFiles(String dir, IOFileFilter fileFilter) {
		return listFiles(new File(dir), fileFilter);
	}

	public static Collection<File> listFiles(File dir, IOFileFilter fileFilter) {
		return listFiles(dir, fileFilter, true);
	}

	public static Collection<File> listFiles(String dir, IOFileFilter fileFilter, boolean recursive) {
		return listFiles(new File(dir), fileFilter, recursive);
	}

	public static Collection<File> listFiles(File dir, IOFileFilter fileFilter, boolean recursive) {
		return listFiles(dir, fileFilter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
	}

	public static Collection<File> listFiles(String dir, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		return listFiles(new File(dir), fileFilter, dirFilter);
	}

	public static Collection<File> listFiles(File dir, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		Validate.notNull(fileFilter, "File Filter is null");
		Validate.notNull(fileFilter, "Dir Filter is null");

		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return Collections.emptyList();
		}

		return FileUtils.listFiles(dir, fileFilter, dirFilter);
	}

	public static Collection<File> listFilesAndDirs(File dir, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		Validate.notNull(fileFilter, "File Filter is null");
		Validate.notNull(fileFilter, "Dir Filter is null");

		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return Collections.emptyList();
		}

		return FileUtils.listFilesAndDirs(dir, fileFilter, dirFilter);
	}

	public static void moveDirectory(String srcDir, String destDir) {
		moveDirectory(new File(srcDir), new File(destDir));
	}

	public static void moveDirectory(File srcDir, File destDir) {
		try {
			FileUtils.moveDirectory(srcDir, destDir);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static void moveDirectoryToDirectory(String srcDir, String destDir, boolean createDestDir) {
		moveDirectoryToDirectory(new File(srcDir), new File(destDir), createDestDir);
	}

	public static void moveDirectoryToDirectory(File srcDir, File destDir, boolean createDestDir) {
		try {
			FileUtils.moveDirectoryToDirectory(srcDir, destDir, createDestDir);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static void deleteDirectory(String dir) {
		deleteDirectory(new File(dir));
	}

	public static void deleteDirectory(File dir) {
		try {
			FileUtils.deleteDirectory(dir);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static void deleteFile(String dir, String file, boolean quiet) {
		deleteFile(new File(dir, file), quiet);
	}

	public static void deleteFile(String file, boolean quiet) {
		deleteFile(new File(file), quiet);
	}

	public static void deleteFile(File file, boolean quiet) {
		if (quiet) {
			FileUtils.deleteQuietly(file);
		} else {
			try {
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				throw Exceptions.unchecked(e);
			}
		}
	}

	public static void copyFileFromDirToDir(String srcDir, String destDir) {
		copyFileFromDirToDir(new File(srcDir), new File(destDir));
	}

	public static void copyFileFromDirToDir(File srcDir, File destDir) {
		copyFileFromDirToDir(srcDir, TrueFileFilter.INSTANCE, destDir);
	}

	public static void copyFileFromDirToDir(String srcDir, IOFileFilter fileFilter, String destDir) {
		copyFileFromDirToDir(new File(srcDir), fileFilter, new File(destDir));
	}

	public static void copyFileFromDirToDir(File srcDir, IOFileFilter fileFilter, File destDir) {
		copyFileFromDirToDir(srcDir, fileFilter, true, destDir);
	}

	public static void copyFileFromDirToDir(String srcDir, IOFileFilter fileFilter, boolean recursive, String destDir) {
		copyFileFromDirToDir(new File(srcDir), fileFilter, recursive, new File(destDir));
	}

	public static void copyFileFromDirToDir(File srcDir, IOFileFilter fileFilter, boolean recursive, File destDir) {
		copyFileFromDirToDir(srcDir, fileFilter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE,
				destDir);
	}

	public static void copyFileFromDirToDir(String srcDir, IOFileFilter fileFilter, IOFileFilter dirFilter,
			String destDir) {
		copyFileFromDirToDir(new File(srcDir), fileFilter, dirFilter, new File(destDir));
	}

	public static void copyFileFromDirToDir(File srcDir, IOFileFilter fileFilter, IOFileFilter dirFilter,
			File destDir) {
		actFileFromDirToDir(srcDir, fileFilter, dirFilter, destDir, FileAction.COPY_FILE_ACTION);
	}

	public static void moveFileFromDirToDir(String srcDir, String destDir) {
		moveFileFromDirToDir(new File(srcDir), new File(destDir));
	}

	public static void moveFileFromDirToDir(File srcDir, File destDir) {
		moveFileFromDirToDir(srcDir, TrueFileFilter.INSTANCE, destDir);
	}

	public static void moveFileFromDirToDir(String srcDir, IOFileFilter fileFilter, String destDir) {
		moveFileFromDirToDir(new File(srcDir), fileFilter, new File(destDir));
	}

	public static void moveFileFromDirToDir(File srcDir, IOFileFilter fileFilter, File destDir) {
		moveFileFromDirToDir(srcDir, fileFilter, true, destDir);
	}

	public static void moveFileFromDirToDir(String srcDir, IOFileFilter fileFilter, boolean recursive, String destDir) {
		moveFileFromDirToDir(new File(srcDir), fileFilter, recursive, new File(destDir));
	}

	public static void moveFileFromDirToDir(File srcDir, IOFileFilter fileFilter, boolean recursive, File destDir) {
		moveFileFromDirToDir(srcDir, fileFilter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE,
				destDir);
	}

	public static void moveFileFromDirToDir(String srcDir, IOFileFilter fileFilter, IOFileFilter dirFilter,
			String destDir) {
		moveFileFromDirToDir(new File(srcDir), fileFilter, dirFilter, new File(destDir));
	}

	public static void moveFileFromDirToDir(File srcDir, IOFileFilter fileFilter, IOFileFilter dirFilter,
			File destDir) {
		actFileFromDirToDir(srcDir, fileFilter, dirFilter, destDir, FileAction.MOVE_FILE_ACTION);
	}

	private static void actFileFromDirToDir(File srcDir, IOFileFilter fileFilter, IOFileFilter dirFilter, File destDir,
			FileAction action) {
		final Collection<File> srcFiles = listFiles(srcDir, fileFilter, dirFilter);
		if (Collections3.isEmpty(srcFiles)) {
			return;
		}

		try {
			final String srcDirCanonicalPath = srcDir.getCanonicalPath();
			for (File srcFile : srcFiles) {
				final String srcFileCanonicalPath = srcFile.getCanonicalPath();
				final String relativePath = srcFileCanonicalPath.substring(srcDirCanonicalPath.length());
				final File destFile = new File(destDir, relativePath);

				action.act(srcFile, destFile);
			}
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private static interface FileAction {
		FileAction COPY_FILE_ACTION = new FileAction() {
			@Override
			public void act(File srcFile, File destFile) throws IOException {
				FileUtils.copyFile(srcFile, destFile);
			}
		};

		FileAction MOVE_FILE_ACTION = new FileAction() {
			@Override
			public void act(File srcFile, File destFile) throws IOException {
				FileUtils.moveFile(srcFile, destFile);
			}
		};

		void act(File srcFile, File destFile) throws IOException;
	}

	private Files() {
	}
}
