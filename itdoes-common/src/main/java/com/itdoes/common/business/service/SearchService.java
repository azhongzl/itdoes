package com.itdoes.common.business.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Jalen Zhong
 */
@Service
public class SearchService extends BaseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

	@PersistenceContext
	private EntityManager em;

	private FullTextEntityManager ftem;

	@PostConstruct
	public void myInit() {
		ftem = Search.getFullTextEntityManager(em);
	}

	public void createIndex() throws Exception {
		LOGGER.info("Search engine index creating...");
		ftem.createIndexer().startAndWait();
		LOGGER.info("Search engine index created");
	}

	public List<?> search(Class<?> entityClass, String[] fields, String searchString) {
		final QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(entityClass).get();
		final Query q = qb.keyword().onFields(fields).matching(searchString).createQuery();
		final FullTextQuery ftq = ftem.createFullTextQuery(q, entityClass);
		final List<?> resultList = ftq.getResultList();
		return resultList;
	}
}
