package com.itdoes.business.web;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.business.service.FacadeService;
import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.Result;
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
	public void getEntity(@RequestParam(value = "ec") String ec,
			@RequestParam(value = "id", required = false) String id, Model model) {
		if (id != null) {
			model.addAttribute("entity", facadeService.get(ec, id));
		} else {
			model.addAttribute("entity", facadeService.newInstance(ec));
		}
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
