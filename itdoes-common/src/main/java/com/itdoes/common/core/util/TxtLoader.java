package com.itdoes.common.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.google.common.collect.Lists;
import com.itdoes.common.core.Constants;

/**
 * @author Jalen Zhong
 */
public class TxtLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(TxtLoader.class);

	private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

	private static List<String> loadLineList(Charset encoding, String... paths) {
		final List<String> lines = Lists.newArrayList();

		for (String path : paths) {
			LOGGER.debug("Loading txt from file: {}", path);
			InputStream is = null;
			try {
				final Resource resource = RESOURCE_LOADER.getResource(path);
				is = resource.getInputStream();

				final List<String> linesTbd = IOUtils.readLines(is, encoding);
				if (Collections3.isEmpty(linesTbd)) {
					continue;
				}

				for (String line : linesTbd) {
					if (StringUtils.isBlank(line)) {
						continue;
					}

					final String formattedLine = StringUtils.trim(line);
					if (!lines.contains(formattedLine)) {
						lines.add(formattedLine);
					}
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("Error in loadng txt from file: " + path, e);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}

		return lines;
	}

	private final List<String> lineList;

	public TxtLoader(Charset encoding, String... paths) {
		this.lineList = loadLineList(encoding, paths);
	}

	public TxtLoader(String... paths) {
		this(Constants.UTF8_CHARSET, paths);
	}

	public List<String> getLineList() {
		return lineList;
	}
}
