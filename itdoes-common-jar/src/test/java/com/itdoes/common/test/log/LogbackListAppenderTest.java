package com.itdoes.common.test.log;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackListAppenderTest {
	@Test
	public void log() {
		String testString1 = "Hello";
		String testString2 = "World";
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(LogbackListAppenderTest.class);
		Logger logger = LoggerFactory.getLogger(LogbackListAppenderTest.class);

		assertThat(appender.getFirst()).isNull();
		assertThat(appender.getLast()).isNull();
		assertThat(appender.getFirstMessage()).isNull();
		assertThat(appender.getLastMessage()).isNull();
		assertThat(appender.getAll()).hasSize(0);
		assertThat(appender.getSize()).isZero();
		assertThat(appender.isEmpty()).isTrue();

		logger.info(testString1);
		logger.info(testString2);
		assertThat(appender.getFirst().getFormattedMessage()).isEqualTo(testString1);
		assertThat(appender.getLast().getFormattedMessage()).isEqualTo(testString2);
		assertThat(appender.getFirstMessage()).isEqualTo(testString1);
		assertThat(appender.getLastMessage()).isEqualTo(testString2);
		assertThat(appender.getAll()).hasSize(2);
		assertThat(appender.getAll().get(1).getFormattedMessage()).isEqualTo(testString2);
		assertThat(appender.getSize()).isEqualTo(2);
		assertThat(appender.isEmpty()).isFalse();

		appender.clear();
		assertThat(appender.getFirst()).isNull();
		assertThat(appender.getLast()).isNull();
		assertThat(appender.getFirstMessage()).isNull();
		assertThat(appender.getLastMessage()).isNull();
		assertThat(appender.getAll()).hasSize(0);
		assertThat(appender.getSize()).isZero();
		assertThat(appender.isEmpty()).isTrue();
	}

	@Test
	public void addAndRemoveAppender() {
		String testString = "Hello";
		Logger logger = LoggerFactory.getLogger(LogbackListAppenderTest.class);
		LogbackListAppender appender = new LogbackListAppender();

		appender.addToLogger(LogbackListAppenderTest.class);
		logger.info(testString);
		assertThat(appender.getFirst()).isNotNull();

		appender.clear();
		appender.removeFromLogger(LogbackListAppenderTest.class);
		logger.info(testString);
		assertThat(appender.getFirst()).isNull();

		appender.clear();
		appender.addToLogger(LogbackListAppenderTest.class.getName());
		logger.info(testString);
		assertThat(appender.getFirst()).isNotNull();

		appender.clear();
		appender.removeFromLogger(LogbackListAppenderTest.class.getName());
		logger.info(testString);
		assertThat(appender.getFirst()).isNull();
	}
}
