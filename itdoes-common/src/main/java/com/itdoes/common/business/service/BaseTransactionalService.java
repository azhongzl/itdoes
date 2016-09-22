package com.itdoes.common.business.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jalen Zhong
 */
@Transactional(readOnly = true)
public abstract class BaseTransactionalService extends BaseService {
}
