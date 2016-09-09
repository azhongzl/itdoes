package com.itdoes.common.core.spring.propertyeditors;

import java.time.format.DateTimeFormatter;

/**
 * @author Jalen Zhong
 */
public class CustomLocalDateTimeEditor extends AbstractCustomJavaTimeEditor {
	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	public CustomLocalDateTimeEditor(boolean allowEmpty, int exactDateLength) {
		super(DEFAULT_FORMATTER, allowEmpty, exactDateLength);
	}

	public CustomLocalDateTimeEditor(boolean allowEmpty) {
		super(DEFAULT_FORMATTER, allowEmpty);
	}

	public CustomLocalDateTimeEditor(int exactDateLength) {
		super(DEFAULT_FORMATTER, exactDateLength);
	}

	public CustomLocalDateTimeEditor() {
		super(DEFAULT_FORMATTER);
	}

	public CustomLocalDateTimeEditor(DateTimeFormatter formatter, boolean allowEmpty, int exactDateLength) {
		super(formatter, allowEmpty, exactDateLength);
	}

	public CustomLocalDateTimeEditor(DateTimeFormatter formatter, boolean allowEmpty) {
		super(formatter, allowEmpty);
	}

	public CustomLocalDateTimeEditor(DateTimeFormatter formatter, int exactDateLength) {
		super(formatter, exactDateLength);
	}

	public CustomLocalDateTimeEditor(DateTimeFormatter formatter) {
		super(formatter);
	}
}
