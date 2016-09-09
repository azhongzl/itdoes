package com.itdoes.common.core.spring.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

import org.springframework.util.StringUtils;

/**
 * @author Jalen Zhong
 */
public abstract class AbstractCustomJavaTimeEditor extends PropertyEditorSupport {
	private final DateTimeFormatter formatter;

	private final boolean allowEmpty;

	private final int exactDateLength;

	/**
	 * <p>
	 * The "allowEmpty" parameter states if an empty String should be allowed for parsing, i.e. get interpreted as null
	 * value. Otherwise, an IllegalArgumentException gets thrown in that case.
	 * <p>
	 * The "exactDateLength" parameter states that IllegalArgumentException gets thrown if the String does not exactly
	 * match the length specified. This is useful because SimpleDateFormat does not enforce strict parsing of the year
	 * part, not even with {@code setLenient(false)}. Without an "exactDateLength" specified, the "01/01/05" would get
	 * parsed to "01/01/0005". However, even with an "exactDateLength" specified, prepended zeros in the day or month
	 * part may still allow for a shorter year part, so consider this as just one more assertion that gets you closer to
	 * the intended date format.
	 */
	public AbstractCustomJavaTimeEditor(DateTimeFormatter formatter, boolean allowEmpty, int exactDateLength) {
		this.formatter = formatter;
		this.allowEmpty = allowEmpty;
		this.exactDateLength = exactDateLength;
	}

	public AbstractCustomJavaTimeEditor(DateTimeFormatter formatter, boolean allowEmpty) {
		this(formatter, allowEmpty, -1);
	}

	public AbstractCustomJavaTimeEditor(DateTimeFormatter formatter, int exactDateLength) {
		this(formatter, true, -1);
	}

	public AbstractCustomJavaTimeEditor(DateTimeFormatter formatter) {
		this(formatter, true);
	}

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
			throw new IllegalArgumentException(
					"Could not parse date: it is not exactly " + this.exactDateLength + " characters long");
		} else {
			try {
				setValue(this.formatter.parse(text));
			} catch (DateTimeParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		final TemporalAccessor value = (TemporalAccessor) getValue();
		return value != null ? this.formatter.format(value) : "";
	}
}
