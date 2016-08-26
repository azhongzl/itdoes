package com.itdoes.common.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author Jalen Zhong
 */
public class Collections3Test {
	@Test
	public void propToString() {
		final TestBean bean1 = new TestBean();
		bean1.setId(1);
		final TestBean bean2 = new TestBean();
		bean2.setId(2);

		final List<TestBean> list = Lists.newArrayList(bean1, bean2);
		assertThat(Collections3.propToString(list, "id", ",")).isEqualTo("1,2");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void propToList() {
		final TestBean bean1 = new TestBean();
		bean1.setId(1);
		final TestBean bean2 = new TestBean();
		bean2.setId(2);

		final List<TestBean> list = Lists.newArrayList(bean1, bean2);
		final List<Integer> result = Collections3.propToList(list, "id");
		assertThat(result).containsOnly(1, 2);
	}

	@Test
	public void prePost() {
		final List<String> list = Lists.newArrayList("aa", "bb");
		final String result = Collections3.prePost(list, "<li>", "</li>");
		assertThat(result).isEqualTo("<li>aa</li><li>bb</li>");
	}

	@Test
	public void getFirstLast() {
		List<String> list = Lists.newArrayList("a", "b", "c");
		assertThat(Collections3.isEmpty(list)).isFalse();
		assertThat(Collections3.getFirst(list)).isEqualTo(list.get(0));
		assertThat(Collections3.getLast(list)).isEqualTo(list.get(list.size() - 1));
	}

	@Test
	public void listsOperation() {
		List<String> listA = Lists.newArrayList("a", "b", "c");
		List<String> listB = Lists.newArrayList("a", "b");

		List<String> result = Collections3.union(listA, listB);
		assertThat(result).containsSequence("a", "b", "c", "a", "b");

		result = Collections3.subtract(listA, listB);
		assertThat(result).containsOnly("c");

		result = Collections3.intersection(listA, listB);
		assertThat(result).containsOnly("a", "b");
	}

	public static class TestBean {
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
}
