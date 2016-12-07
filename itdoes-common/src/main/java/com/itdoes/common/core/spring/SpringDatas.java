package com.itdoes.common.core.spring;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * @author Jalen Zhong
 */
public class SpringDatas {
	public static PageRequest newPageRequest(int pageNo, int pageSize, int maxPageSize, Sort sort) {
		if (pageNo < 1) {
			pageNo = 1;
		}

		if (pageSize < 1 || pageSize > maxPageSize) {
			pageSize = maxPageSize;
		}

		return new PageRequest(pageNo - 1, pageSize, sort);
	}

	public static Sort newSort(String field, boolean asc) {
		return new Sort(asc ? Direction.ASC : Direction.DESC, field);
	}

	private SpringDatas() {
	}
}
