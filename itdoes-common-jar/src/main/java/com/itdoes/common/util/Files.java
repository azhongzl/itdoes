package com.itdoes.common.util;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class Files {
	public static Collection<File> listFiles(String dir, boolean recursive) {
		return listFiles(new File(dir), recursive);
	}

	public static Collection<File> listFiles(File dir, boolean recursive) {
		return listFiles(dir, TrueFileFilter.INSTANCE, recursive);
	}

	public static Collection<File> listFiles(String dir, IOFileFilter fileFilter, boolean recursive) {
		return listFiles(new File(dir), fileFilter, recursive);
	}

	public static Collection<File> listFiles(File dir, IOFileFilter fileFilter, boolean recursive) {
		return listFiles(dir, fileFilter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
	}

	public static Collection<File> listFiles(File dir, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		Validate.notNull(dir, "Dir is null");
		Validate.notNull(fileFilter, "File Filter is null");
		Validate.notNull(fileFilter, "Dir Filter is null");
		Validate.isTrue(dir.exists(), "Dir " + dir + " does not exist");
		Validate.isTrue(dir.isDirectory(), "Dir " + dir + " is not a directory");

		return FileUtils.listFiles(dir, fileFilter, dirFilter);
	}

	public static Collection<File> listFilesAndDirs(File dir, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		Validate.notNull(dir, "Dir is null");
		Validate.notNull(fileFilter, "File Filter is null");
		Validate.notNull(fileFilter, "Dir Filter is null");
		Validate.isTrue(dir.exists(), "Dir " + dir + " does not exist");
		Validate.isTrue(dir.isDirectory(), "Dir " + dir + " is not a directory");

		return FileUtils.listFilesAndDirs(dir, fileFilter, dirFilter);
	}

	public static void move(String srcDir, String destDir) {
		final Collection<File> srcFiles = listFiles(srcDir, true);
		if (Collections3.isEmpty(srcFiles)) {
			return;
		}

		for (File srcFile : srcFiles) {

		}
	}

	public static void copy(String srcDir, String destDir) {

	}

	private Files() {
	}
}
