package com.itdoes.common.business.service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Service;

/**
 * @author Jalen Zhong
 */
@Service
public class SearchService extends BaseService {
	@PersistenceContext
	private EntityManager em;

	private FullTextEntityManager ftem;

	@PostConstruct
	public void myInit() {
		ftem = Search.getFullTextEntityManager(em);
	}

	public void createIndex() throws Exception {
		// LOG.info("Updating Index");
		ftem.createIndexer().startAndWait();
	}
}
