package com.itdoes.common.business.web;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.cglib.CglibMapper;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = EntityBaseController.ENTITY_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class EntityPutController extends EntityBaseController {
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{ec}/" + EntityMainController.ENTITY_COMMAND_PUT + "/{id}", method = RequestMethod.POST)
	public <T, ID extends Serializable> Result put(@PathVariable("ec") String ec,
			@Valid @ModelAttribute("entity") T entity, ServletRequest request) {
		final EntityPair<T, ID> pair = getPair(ec);
		final T oldEntity = (T) request.getAttribute("oldEntity");
		subjectService.put(pair, entity, oldEntity);
		return HttpResults.success();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{ec}/" + EntityMainController.ENTITY_COMMAND_PUT_UPLOAD
			+ "/{id}", method = RequestMethod.POST)
	public <T, ID extends Serializable> Result put(@PathVariable("ec") String ec,
			@Valid @ModelAttribute("entity") T entity,
			@RequestParam(BaseController.UPLOAD_FILE_PARAM) List<MultipartFile> uploadFileList,
			ServletRequest request) {
		final EntityPair<T, ID> pair = getPair(ec);
		final T oldEntity = (T) request.getAttribute("oldEntity");
		subjectService.putUpload(pair, entity, oldEntity, realRootPath, uploadFileList, isUploadDeleteOrphanFiles());
		return HttpResults.success();
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute
	public <T, ID extends Serializable> void getEntity(@PathVariable("ec") String ec, @PathVariable("id") String id,
			Model model, ServletRequest request) {
		final EntityPair<T, ID> pair = getPair(ec);

		final T entity = subjectService.get(pair, convertId(pair, id));
		model.addAttribute("entity", entity);

		if (pair.needCopyOldEntity()) {
			final T oldEntity = (T) CglibMapper.copy(entity);
			request.setAttribute("oldEntity", oldEntity);
		}
	}
}
