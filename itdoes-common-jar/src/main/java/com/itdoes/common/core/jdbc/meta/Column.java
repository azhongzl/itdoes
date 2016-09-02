package com.itdoes.common.core.jdbc.meta;

import com.itdoes.common.core.BaseBean;

/**
 * @author Jalen Zhong
 */
public class Column extends BaseBean {
	private static final long serialVersionUID = -1255240887631207414L;

	public static class ColumnType extends BaseBean {
		private static final long serialVersionUID = -534686835301174976L;

		private final int type;
		private final String typeName;

		public ColumnType(int type, String typeName) {
			this.type = type;
			this.typeName = typeName;
		}

		public int getType() {
			return type;
		}

		public String getTypeName() {
			return typeName;
		}
	}

	private final String name;
	private final ColumnType type;
	private final boolean nullable;
	private boolean pk;

	public Column(String name, ColumnType type, boolean nullable) {
		this.name = name;
		this.type = type;
		this.nullable = nullable;
	}

	public String getName() {
		return name;
	}

	public ColumnType getType() {
		return type;
	}

	public boolean isNullable() {
		return nullable;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}
}