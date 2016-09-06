package com.itdoes.common.core;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jalen Zhong
 */
public interface Constants {
	String UTF8 = "UTF-8";
	Charset UTF8_CHARSET = Charset.forName(UTF8);

	List<String> JAVA_KEYWORDS = Arrays.asList("abstract", "assert", "boolean", "break", "byte", "case", "catch",
			"char", "class", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally",
			"float", "for", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
			"package", "private", "protected", "public", "return", "strictfp", "short", "static", "super", "switch",
			"synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while");

	String LINE_SEPARATOR = System.getProperty("line.separator");
}
