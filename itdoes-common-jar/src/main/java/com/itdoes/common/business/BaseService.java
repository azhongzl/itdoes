package com.itdoes.common.business;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jalen Zhong
 */
@Transactional(readOnly = true)
public abstract class BaseService {
}
