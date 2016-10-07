package com.itdoes.common.business.service;

import java.util.ArrayList;
import java.util.List;

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

	public void createIndex() {
		LOGGER.info("Search engine index creating...");
		try {
			getFullTextEntityManager().createIndexer().startAndWait();
		} catch (InterruptedException e) {
			throw Exceptions.unchecked(e);
		}
		LOGGER.info("Search engine index created");
	}

	public Page<?> search(String searchString, SearchEntity searchEntity, Pageable pageable) {
		final FullTextEntityManager ftem = getFullTextEntityManager();

		final Query q = createQuery(ftem, searchString, searchEntity);
		final FullTextQuery ftq = ftem.createFullTextQuery(q, searchEntity.getEntityClass());
		ftq.setFirstResult(pageable.getOffset());
		ftq.setMaxResults(pageable.getPageSize());
		return createPage(ftq.getResultList(), pageable, ftq.getResultSize());
	}

	public Page<?> search(String searchString, List<SearchEntity> searchEntities, Pageable pageable) {
		final FullTextEntityManager ftem = getFullTextEntityManager();

		final List<Class<?>> entityClasses = new ArrayList<>(searchEntities.size());
		final BooleanQuery.Builder bqb = new BooleanQuery.Builder();
		for (SearchEntity searchEntity : searchEntities) {
			final Query q = createQuery(ftem, searchString, searchEntity);
			bqb.add(q, Occur.SHOULD);
		}
		final FullTextQuery ftq = ftem.createFullTextQuery(bqb.build(),
				entityClasses.toArray(new Class[searchEntities.size()]));
		ftq.setFirstResult(pageable.getOffset());
		ftq.setMaxResults(pageable.getPageSize());
		return createPage(ftq.getResultList(), pageable, ftq.getResultSize());
	}

	private FullTextEntityManager getFullTextEntityManager() {
		return Search.getFullTextEntityManager(em);
	}

	private Query createQuery(FullTextEntityManager ftem, String searchString, SearchEntity searchEntity) {
		final QueryBuilder queryBuilder = ftem.getSearchFactory().buildQueryBuilder()
				.forEntity(searchEntity.getEntityClass()).get();
		final Query query = queryBuilder.keyword().onFields(searchEntity.getFields()).matching(searchString)
				.createQuery();
		return query;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Page<?> createPage(List<?> resultList, Pageable pageable, long total) {
		return new PageImpl(resultList, pageable, total);
	}

	public static class SearchEntity {
		private final Class<?> entityClass;
		private final String[] fields;

		public SearchEntity(Class<?> entityClass, String[] fields) {
			this.entityClass = entityClass;
			this.fields = fields;
		}

		public Class<?> getEntityClass() {
			return entityClass;
		}

		public String[] getFields() {
			return fields;
		}
	}
}
