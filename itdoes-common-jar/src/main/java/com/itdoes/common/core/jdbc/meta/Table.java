package com.itdoes.common.core.jdbc.meta;

import java.util.List;

import com.google.common.collect.Lists;
import com.itdoes.common.core.BaseBean;

/**
 * @author Jalen Zhong
 */
public class Table extends BaseBean {
	private static final long serialVersionUID = 5745999103027546966L;

	private final String name;
	private final List<Column> columnList = Lists.newArrayList();

	public Table(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Column> getColumnList() {
		return columnList;
	}
}
