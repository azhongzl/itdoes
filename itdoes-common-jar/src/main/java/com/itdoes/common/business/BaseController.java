package com.itdoes.common.business;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.google.common.collect.Lists;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.jpa.SearchFilter.Operator;

/**
 * @author Jalen Zhong
 * 
 *         <pre>
 *         Two ways for Date<->String convert:
 *         1) Add in Entity field
 *         &#64;DateTimeFormat(pattern = "yyyy-MM-dd") 
 *          
 *         2) Add in Controller
 *         &#64;InitBinder
 *         public void initBinder(WebDataBinder binder) {
 *         	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 *         	dateFormat.setLenient(false);
 *         	binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
 *         }
 *         </pre>
 */
public abstract class BaseController {
	protected static final String DEFAULT_PAGE_SIZE = "100";

	private static final String FILTER_PREFIX = "ff_";
	private static final char FILTER_SEPARATOR = '_';
	private static final char SORT_SEPARATOR = '_';

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
