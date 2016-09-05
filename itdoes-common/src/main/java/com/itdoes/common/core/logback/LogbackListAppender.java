package com.itdoes.common.core.logback;

import java.util.List;

import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * @author Jalen Zhong
 */
public class LogbackListAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
	private final List<ILoggingEvent> logs = Lists.newArrayList();

	private final Level level;

	public LogbackListAppender() {
		this(null);
	}

	public LogbackListAppender(Level level) {
		this.level = level;
		start();
	}

	@Override
	protected void append(ILoggingEvent e) {
		if (level == null || e.getLevel().isGreaterOrEqual(level)) {
			logs.add(e);
		}
	}

	public ILoggingEvent getFirst() {
		if (logs.isEmpty()) {
			return null;
		}
		return logs.get(0);
	}

	public String getFirstMessage() {
		if (logs.isEmpty()) {
			return null;
		}
		return getFirst().getFormattedMessage();
	}

	public ILoggingEvent getLast() {
		if (logs.isEmpty()) {
			return null;
		}
		return logs.get(logs.size() - 1);
	}

	public String getLastMessage() {
		if (logs.isEmpty()) {
			return null;
		}
		return getLast().getFormattedMessage();
	}

	public List<ILoggingEvent> getAll() {
		return logs;
	}

	public int getSize() {
		return logs.size();
	}

	public boolean isEmpty() {
		return logs.isEmpty();
	}

	public void clear() {
		logs.clear();
	}

	public void addToLogger(String loggerName) {
		final Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
		logger.addAppender(this);
	}

	public void addToLogger(Class<?> loggerClass) {
		final Logger logger = (Logger) LoggerFactory.getLogger(loggerClass);
		logger.addAppender(this);
	}

	public void addToRootLogger() {
		addToLogger(Logger.ROOT_LOGGER_NAME);
	}

	public void removeFromLogger(String loggerName) {
		final Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
		logger.detachAppender(this);
	}

	public void removeFromLogger(Class<?> loggerClass) {
		final Logger logger = (Logger) LoggerFactory.getLogger(loggerClass);
		logger.detachAppender(this);
	}

	public void removeFromRootLogger() {
		removeFromLogger(Logger.ROOT_LOGGER_NAME);
	}
}
