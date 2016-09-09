package com.itdoes.common.core.spring.propertyeditors;

import java.time.format.DateTimeFormatter;

/**
 * @author Jalen Zhong
 */
public class CustomLocalDateEditor extends AbstractCustomJavaTimeEditor {
	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

	public CustomLocalDateEditor(boolean allowEmpty, int exactDateLength) {
		super(DEFAULT_FORMATTER, allowEmpty, exactDateLength);
	}

	public CustomLocalDateEditor(boolean allowEmpty) {
		super(DEFAULT_FORMATTER, allowEmpty);
	}

	public CustomLocalDateEditor(int exactDateLength) {
		super(DEFAULT_FORMATTER, exactDateLength);
	}

	public CustomLocalDateEditor() {
		super(DEFAULT_FORMATTER);
	}

	public CustomLocalDateEditor(DateTimeFormatter formatter, boolean allowEmpty, int exactDateLength) {
		super(formatter, allowEmpty, exactDateLength);
	}

	public CustomLocalDateEditor(DateTimeFormatter formatter, boolean allowEmpty) {
		super(formatter, allowEmpty);
	}

	public CustomLocalDateEditor(DateTimeFormatter formatter, int exactDateLength) {
		super(formatter, exactDateLength);
	}

	public CustomLocalDateEditor(DateTimeFormatter formatter) {
		super(formatter);
	}
}
