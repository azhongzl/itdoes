package com.itdoes.common.business.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itdoes.common.core.util.Exceptions;

/**
 * @author Jalen Zhong
 */
@Service
public class SearchService extends BaseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

	@PersistenceContext
	private EntityManager em;

	@PostConstruct
	public void myInit() {
		createIndex();
	}

	public void createIndex() {
		LOGGER.info("Search engine index creating...");
		try {
			getFullTextEntityManager().createIndexer().startAndWait();
		} catch (InterruptedException e) {
			throw Exceptions.unchecked(e);
		}
		LOGGER.info("Search engine index created");
	}

	public Page<?> search(String searchString, Class<?> entityClass, String[] fields, Pageable pageable) {
		return search(searchString, entityClass, new DefaultQueryFactory(fields), pageable);
	}

	public Page<?> search(String searchString, Class<?> entityClass, QueryFactory queryFactory, Pageable pageable) {
		final FullTextEntityManager ftem = getFullTextEntityManager();

		final Query q = createQuery(searchString, ftem, entityClass, queryFactory);
		final FullTextQuery ftq = ftem.createFullTextQuery(q, entityClass);
		return createPage(ftq, pageable);
	}

	public Page<?> search(String searchString, Class<?>[] entityClasses, QueryFactory[] queryFactories,
			Pageable pageable) {
		final FullTextEntityManager ftem = getFullTextEntityManager();

		final BooleanQuery.Builder bqb = new BooleanQuery.Builder();
		for (int i = 0; i < entityClasses.length; i++) {
			final Query q = createQuery(searchString, ftem, entityClasses[i], queryFactories[i]);
			bqb.add(q, Occur.SHOULD);
		}
		final FullTextQuery ftq = ftem.createFullTextQuery(bqb.build(), entityClasses);
		return createPage(ftq, pageable);
	}

	private FullTextEntityManager getFullTextEntityManager() {
		return Search.getFullTextEntityManager(em);
	}

	public static interface QueryFactory {
		Query createQuery(String searchString, QueryBuilder queryBuilder);
	}

	private static class DefaultQueryFactory implements QueryFactory {
		private final String[] fields;

		public DefaultQueryFactory(String[] fields) {
			this.fields = fields;
		}

		@Override
		public Query createQuery(String searchString, QueryBuilder queryBuilder) {
			return queryBuilder.keyword().wildcard().onFields(fields).matching(searchString).createQuery();
		}
	}

	private Query createQuery(String searchString, FullTextEntityManager ftem, Class<?> entityClass,
			QueryFactory queryFactory) {
		final QueryBuilder queryBuilder = ftem.getSearchFactory().buildQueryBuilder().forEntity(entityClass).get();
		final Query query = queryFactory.createQuery(searchString, queryBuilder);
		return query;
	}

	private Page<?> createPage(FullTextQuery ftq, Pageable pageable) {
		ftq.setFirstResult(pageable.getOffset());
		ftq.setMaxResults(pageable.getPageSize());
		return createPage(ftq.getResultList(), pageable, ftq.getResultSize());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Page<?> createPage(List<?> resultList, Pageable pageable, long total) {
		return new PageImpl(resultList, pageable, total);
	}
}
