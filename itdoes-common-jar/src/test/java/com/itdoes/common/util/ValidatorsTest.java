package com.itdoes.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.itdoes.common.test.spring.SpringTestCase;

/**
 * @author Jalen Zhong
 */
@ContextConfiguration(locations = { "/applicationContext-validator.xml" })
public class ValidatorsTest extends SpringTestCase {
	private static final String EMAIL_KEY = "email";
	private static final String EMAIL_MESSAGE = "not a well-formed email address";
	private static final String NAME_KEY = "name";
	private static final String NAME_MESSAGE = "may not be empty";

	@Autowired
	private Validator validator;

	@BeforeClass
	public static void beforeClass() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void validateWithException() {
		Customer customer = new Customer();
		customer.setEmail("aaa");

		try {
			Validators.validateWithException(validator, customer);
			failBecauseExceptionWasNotThrown(ConstraintViolationException.class);
		} catch (ConstraintViolationException e) {
			Map<String, String> mapResult = Validators.propertyMessages(e);
			assertThat(mapResult).containsOnly(entry(EMAIL_KEY, EMAIL_MESSAGE), entry(NAME_KEY, NAME_MESSAGE));
		}
	}

	@Test
	public void propertyMessages() {
		Customer customer = new Customer();
		customer.setEmail("aaa");

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertThat(violations).hasSize(2);

		List<String> listResult = Validators.messages(violations);
		assertThat(listResult).containsOnly(EMAIL_MESSAGE, NAME_MESSAGE);

		Map<String, String> mapResult = Validators.propertyMessages(violations);
		assertThat(mapResult).containsOnly(entry(EMAIL_KEY, EMAIL_MESSAGE), entry(NAME_KEY, NAME_MESSAGE));

		listResult = Validators.propertyMessagesAsList(violations);
		assertThat(listResult).containsOnly(EMAIL_KEY + " " + EMAIL_MESSAGE, NAME_KEY + " " + NAME_MESSAGE);
	}

	private static class Customer {
		private String name;
		private String email;

		@NotBlank
		public String getName() {
			return name;
		}

		@SuppressWarnings("unused")
		public void setName(String name) {
			this.name = name;
		}

		@Email
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}
}
