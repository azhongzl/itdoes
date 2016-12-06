package com.itdoes.common.extension.codegenerator.entity.config;

import com.itdoes.common.core.jdbc.meta.Column;

/**
 * @author Jalen Zhong
 */
public interface NotNullConfig {
	String getFieldNotNull(String tableName, Column column);
}
