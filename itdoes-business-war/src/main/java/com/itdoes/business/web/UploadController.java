package com.itdoes.business.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.Result;
import com.itdoes.common.util.Collections3;
import com.itdoes.common.web.MultipartFiles;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {
	@RequestMapping(method = RequestMethod.GET)
	public String uploadForm() {
		return "upload";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result upload(@RequestParam("path") String path, @RequestParam("file") List<MultipartFile> files) {
		if (!Collections3.isEmpty(files)) {
			MultipartFiles.save(context, path, files);
			return Result.success(files.toArray(new MultipartFile[files.size()]));
		}

		throw new IllegalArgumentException("file not uploaded");
	}
}
