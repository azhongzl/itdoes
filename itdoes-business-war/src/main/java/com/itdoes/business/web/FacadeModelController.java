package com.itdoes.business.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.business.service.FacadeService;
import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.Businesses.EntityPair;
import com.itdoes.common.business.Result;
import com.itdoes.common.util.Exceptions;
import com.itdoes.common.web.MediaTypes;

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
@RestController
@RequestMapping(value = "/facade", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadeModelController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@RequestParam(value = "ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity) {
		facadeService.save(ec, entity);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@RequestParam(value = "ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity) {
		facadeService.save(ec, entity);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@ModelAttribute
	public void getEntity(@RequestParam(value = "ec") String ec, HttpServletRequest request, Model model) {
		final EntityPair pair = facadeService.getEntityPair(ec);
		final String id = request.getParameter(pair.idField.getName());
		if (StringUtils.isBlank(id)) {
			try {
				final Object entity = pair.entityClass.newInstance();
				model.addAttribute("entity", entity);
			} catch (InstantiationException | IllegalAccessException e) {
				throw Exceptions.unchecked(e);
			}
		} else {
			model.addAttribute("entity", facadeService.get(ec, id));
		}
	}
}
