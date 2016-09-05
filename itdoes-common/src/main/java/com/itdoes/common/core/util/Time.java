package com.itdoes.common.core.util;

import java.util.Date;

/**
 * @author Jalen Zhong
 */
public interface Time {
	Time NOW = new NowTime();

	Date getDate();

	long getTime();

	class NowTime implements Time {
		@Override
		public Date getDate() {
			return new Date();
		}

		@Override
		public long getTime() {
			return System.currentTimeMillis();
		}
	}

	class MockTime implements Time {
		private long time;

		public MockTime() {
			this(0);
		}

		public MockTime(Date date) {
			this(date.getTime());
		}

		public MockTime(long time) {
			this.time = time;
		}

		@Override
		public Date getDate() {
			return new Date(time);
		}

		@Override
		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

		public void increase(int millis) {
			time += millis;
		}

		public void decrease(int millis) {
			time -= millis;
		}
	}
}
