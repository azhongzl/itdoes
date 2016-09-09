package com.itdoes.common.core.spring.propertyeditors;

import java.time.format.DateTimeFormatter;

/**
 * @author Jalen Zhong
 */
public class CustomLocalTimeEditor extends AbstractCustomJavaTimeEditor {
	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

	public CustomLocalTimeEditor(boolean allowEmpty, int exactDateLength) {
		super(DEFAULT_FORMATTER, allowEmpty, exactDateLength);
	}

	public CustomLocalTimeEditor(boolean allowEmpty) {
		super(DEFAULT_FORMATTER, allowEmpty);
	}

	public CustomLocalTimeEditor(int exactDateLength) {
		super(DEFAULT_FORMATTER, exactDateLength);
	}

	public CustomLocalTimeEditor() {
		super(DEFAULT_FORMATTER);
	}

	public CustomLocalTimeEditor(DateTimeFormatter formatter, boolean allowEmpty, int exactDateLength) {
		super(formatter, allowEmpty, exactDateLength);
	}

	public CustomLocalTimeEditor(DateTimeFormatter formatter, boolean allowEmpty) {
		super(formatter, allowEmpty);
	}

	public CustomLocalTimeEditor(DateTimeFormatter formatter, int exactDateLength) {
		super(formatter, exactDateLength);
	}

	public CustomLocalTimeEditor(DateTimeFormatter formatter) {
		super(formatter);
	}
}
