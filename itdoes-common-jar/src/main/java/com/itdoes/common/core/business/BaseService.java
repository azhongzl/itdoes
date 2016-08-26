package com.itdoes.common.core.business;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jalen Zhong
 */
@Transactional(readOnly = true)
public abstract class BaseService {
}
