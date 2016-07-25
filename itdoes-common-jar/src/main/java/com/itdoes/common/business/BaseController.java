package com.itdoes.common.business;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.itdoes.common.jackson.JsonMapper;
import com.itdoes.common.jackson.JsonMapperBuilder;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.jpa.SearchFilter.Operator;

/**
 * @author Jalen Zhong
 */
public abstract class BaseController {
	public static final JsonMapper JSON_MAPPER = JsonMapperBuilder.newBuilder().nonEmpty().build();

	protected static final String DEFAULT_PAGE_SIZE = "100";

	private static final String FILTER_PREFIX = "ff_";
	private static final char FILTER_SEPARATOR = '_';
	private static final char SORT_SEPARATOR = '_';

	protected static String toJson(Result result) {
		return JSON_MAPPER.toJson(result);
	}

	protected static List<SearchFilter> buildFilters(ServletRequest request) {
		final List<SearchFilter> filters = Lists.newArrayList();

		final Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();
			if (paramName.startsWith(FILTER_PREFIX)) {
				final String value = request.getParameter(paramName);
				if (StringUtils.isBlank(value)) {
					continue;
				}

				final String unprefixParamName = paramName.substring(FILTER_PREFIX.length());

				final String field;
				final Operator operator;
				if (unprefixParamName.indexOf(FILTER_SEPARATOR) == -1) {
					field = unprefixParamName;
					operator = Operator.EQ;
				} else {
					final String[] names = StringUtils.split(unprefixParamName, FILTER_SEPARATOR);
					field = names[0];
					operator = Operator.valueOf(names[1]);
				}

				final SearchFilter filter = new SearchFilter(field, operator, value);
				filters.add(filter);
			}
		}
		return filters;
	}

	protected static PageRequest buildPageRequest(int pageNo, int pageSize, String pageSort) {
		Sort sort = null;
		if (pageSort != null) {
			final String[] sortParams = StringUtils.split(pageSort, SORT_SEPARATOR);
			Validate.isTrue(sortParams.length == 2, "Page.sort shout be in format \"<COLUMN>" + SORT_SEPARATOR
					+ "A\" or \"<COLUMN>" + SORT_SEPARATOR + "D\", now it is \"" + pageSort + "\"");

			final String column = sortParams[0];
			final String directionString = sortParams[1];
			final Direction direction;
			if ("a".equalsIgnoreCase(directionString)) {
				direction = Direction.ASC;
			} else {
				direction = Direction.DESC;
			}
			sort = new Sort(direction, column);
		}

		return new PageRequest(pageNo - 1, pageSize, sort);
	}
}
