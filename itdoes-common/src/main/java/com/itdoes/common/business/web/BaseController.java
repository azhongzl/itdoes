package com.itdoes.common.business.web;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.google.common.collect.Lists;
import com.itdoes.common.core.jpa.SearchFilter;
import com.itdoes.common.core.jpa.SearchFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;

/**
 * <pre>
 * Two ways for Date<->String convert:
 * 1) Add in Entity field
 * &#64;DateTimeFormat(pattern = "yyyy-MM-dd") 
 *          
 * 2) Add in Controller
 * &#64;InitBinder
 * public void initBinder(WebDataBinder binder) {
 *  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 *  dateFormat.setLenient(false);
 *  binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
 * }
 * </pre>
 * 
 * @author Jalen Zhong
 * 
 */
public abstract class BaseController {
	private static final int DEFAULT_MAX_PAGE_SIZE = 100;

	private static final String FILTER_PREFIX = "ff_";
	private static final char FILTER_SEPARATOR = '_';
	private static final char SORT_SEPARATOR = '_';

	@Autowired
	protected ServletContext context;

	private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;

	public void setMaxPageSize(int maxPageSize) {
		if (maxPageSize < 1) {
			return;
		}

		this.maxPageSize = maxPageSize;
	}

	protected <T> Specification<T> buildSpecification(Class<T> clazz, ServletRequest request) {
		return Specifications.build(clazz, buildFilters(request));
	}

	private List<SearchFilter> buildFilters(ServletRequest request) {
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
					Validate.isTrue(names.length == 2,
							"Filter name should be in format <Field>[%sOperator], but now it is %s", FILTER_SEPARATOR,
							unprefixParamName);

					field = names[0];
					operator = Operator.valueOf(names[1]);
				}

				final SearchFilter filter = new SearchFilter(field, operator, value);
				filters.add(filter);
			}
		}
		return filters;
	}

	protected PageRequest buildPageRequest(int pageNo, int pageSize, String pageSort) {
		if (pageNo < 1) {
			pageNo = 1;
		}

		if (pageSize < 1 || pageSize > maxPageSize) {
			pageSize = maxPageSize;
		}

		Sort sort = null;
		if (StringUtils.isNotBlank(pageSort)) {
			final String[] sortParams = StringUtils.split(pageSort, SORT_SEPARATOR);
			Validate.isTrue(sortParams.length == 2,
					"Page.sort shout be in format <Field>%sA or <Field>%sD, now it is %s", SORT_SEPARATOR,
					SORT_SEPARATOR, pageSort);

			final String field = sortParams[0];
			final String directionString = sortParams[1];
			final Direction direction;
			if ("a".equalsIgnoreCase(directionString)) {
				direction = Direction.ASC;
			} else {
				direction = Direction.DESC;
			}
			sort = new Sort(direction, field);
		}

		return new PageRequest(pageNo - 1, pageSize, sort);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String value) {
				if (StringUtils.isBlank(value)) {
					setValue(null);
				} else {
					setValue(new Date(Long.valueOf(value)));
				}
			}

			@Override
			public String getAsText() {
				final Date date = (Date) getValue();
				return date != null ? String.valueOf(date.getTime()) : "";
			}
		});
	}
}
