package com.itdoes.common.business.service;

import java.util.ArrayList;
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

	public List<?> search(String searchString, SearchEntity searchEntity, int firstResult, int maxResults) {
		final Query q = createQuery(searchString, searchEntity);
		final FullTextQuery ftq = ftem.createFullTextQuery(q, searchEntity.getEntityClass());
		ftq.setFirstResult(firstResult);
		ftq.setMaxResults(maxResults);
		final List<?> resultList = ftq.getResultList();
		return resultList;
	}

	public List<?> search(String searchString, List<SearchEntity> searchEntities, int firstResult, int maxResults) {
		final List<Class<?>> entityClasses = new ArrayList<>(searchEntities.size());
		final BooleanQuery.Builder bqb = new BooleanQuery.Builder();
		for (SearchEntity searchEntity : searchEntities) {
			final Query q = createQuery(searchString, searchEntity);
			bqb.add(q, Occur.SHOULD);
		}
		final FullTextQuery ftq = ftem.createFullTextQuery(bqb.build(),
				entityClasses.toArray(new Class[searchEntities.size()]));
		ftq.setFirstResult(firstResult);
		ftq.setMaxResults(maxResults);
		final List<?> resultList = ftq.getResultList();
		return resultList;
	}

	private Query createQuery(String searchString, SearchEntity searchEntity) {
		final QueryBuilder queryBuilder = ftem.getSearchFactory().buildQueryBuilder()
				.forEntity(searchEntity.getEntityClass()).get();
		final Query query = queryBuilder.keyword().onFields(searchEntity.getFields()).matching(searchString)
				.createQuery();
		return query;
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
