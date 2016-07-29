package com.itdoes.business.web;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.Businesses;
import com.itdoes.common.business.Businesses.EntityPair;
import com.itdoes.common.business.Result;
import com.itdoes.common.dozer.BeanMapper;
import com.itdoes.common.util.Reflections;
import com.itdoes.common.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = FacadeBaseController.FACADE_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadePutController extends FacadeBaseController {
	@RequestMapping(value = "/{ec}/" + FacadeMainController.FACADE_URL_PUT + "/{id}", method = RequestMethod.POST)
	public String put(@PathVariable("ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity,
			ServletRequest request) {
		final EntityPair pair = facadeService.getEntityPair(ec);
		if (hasSecureColumns(pair)) {
			handleSecureColumns(pair, entity, (BaseEntity) request.getAttribute("oldEntity"));
		}

		facadeService.put(ec, entity);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@ModelAttribute
	public void getEntity(@PathVariable("ec") String ec, @PathVariable("id") String id, Model model,
			ServletRequest request) {
		final BaseEntity entity = facadeService.get(ec, id);
		model.addAttribute("entity", entity);

		final EntityPair pair = facadeService.getEntityPair(ec);
		if (hasSecureColumns(pair)) {
			final BaseEntity oldEntity = (BaseEntity) BeanMapper.map(entity, pair.entityClass);
			request.setAttribute("oldEntity", oldEntity);
		}
	}

	protected void handleSecureColumn(EntityPair pair, BaseEntity entity, BaseEntity oldEntity, Subject subject,
			String tableName, String secureFieldName) {
		if (!subject.isPermitted(Businesses.getWritePermission(tableName, secureFieldName))) {
			Reflections.invokeSet(entity, secureFieldName, Reflections.invokeGet(oldEntity, secureFieldName));
		}
	}
}
