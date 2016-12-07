package com.itdoes.common.business.web;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

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
	public static final String PAGE_NO = "page_no";
	public static final String PAGE_SIZE = "page_size";

	public static final String UPLOAD_FILE_PARAM = "uploadFile";

	@Autowired
	protected ServletContext context;
}
