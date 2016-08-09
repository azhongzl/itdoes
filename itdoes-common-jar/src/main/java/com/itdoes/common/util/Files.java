package com.itdoes.common.util;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;

/**
 * @author Jalen Zhong
 */
public class Files {
	private static final List<File> EMPTY_FILES = Lists.newArrayList();

	public static List<File> listFiles(File dir, FileFilter filter) {
		Validate.notNull(dir, "Dir is null");
		Validate.notNull(filter, "Filter is null");
		Validate.isTrue(dir.exists(), "Dir does not exist");
		Validate.isTrue(dir.isDirectory(), "Dir is not a directory");

		final File[] files = dir.listFiles(filter);
		if (files == null || files.length == 0) {
			return EMPTY_FILES;
		}

		return Lists.newArrayList(files);
	}

	public static List<File> listFiles(File dir) {
		return listFiles(dir, AllFileFilter.getInstance());
	}

	public static List<File> listFiles(String dir, FileFilter filter) {
		return listFiles(new File(dir), filter);
	}

	public static List<File> listFiles(String dir) {
		return listFiles(new File(dir));
	}

	public static class AllFileFilter implements FileFilter {
		private static final AllFileFilter INSTANCE = new AllFileFilter();

		public static AllFileFilter getInstance() {
			return INSTANCE;
		}

		private AllFileFilter() {
		}

		@Override
		public boolean accept(File pathname) {
			return true;
		}
	}

	public static class PrefixFileFilter implements FileFilter {
		private final String prefix;

		public PrefixFileFilter(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().startsWith(prefix);
		}
	}

	public static class PostfixFileFilter implements FileFilter {
		private final String postfix;

		public PostfixFileFilter(String postfix) {
			this.postfix = postfix;
		}

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(postfix);
		}
	}

	private Files() {
	}
}
