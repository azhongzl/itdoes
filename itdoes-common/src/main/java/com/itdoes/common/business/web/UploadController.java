package com.itdoes.common.business.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.Permissions;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Strings;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;
import com.itdoes.common.core.web.MultipartFiles;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = UploadController.URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class UploadController extends BaseController {
	public static final String URL_PREFIX = "/" + Permissions.UPLOAD_PERMISSON_PREFIX;
	private static final String URL_TO_FIND = URL_PREFIX + "/";

	@RequestMapping(value = "/**", method = RequestMethod.POST)
	public Result upload(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {
		if (Collections3.isEmpty(files)) {
			return HttpResults.fail(HttpStatus.BAD_REQUEST.value(), "No upload file");
		}

		final String path = Strings.substring(request.getRequestURI(), URL_TO_FIND);
		MultipartFiles.save(context, path, files);
		return HttpResults.success(files.size());
	}
}