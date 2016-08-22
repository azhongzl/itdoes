package com.itdoes.business.web;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.Result;
import com.itdoes.common.web.Webs;

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
	public Result upload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file)
			throws IOException {
		if (!file.isEmpty()) {
			final byte[] bytes = file.getBytes();
			final String originalFilename = file.getOriginalFilename();
			FileUtils.writeByteArrayToFile(new File(Webs.getRealPath(context, "/"), originalFilename), bytes);
			return Result.success(new MultipartFile[] { file });
		}

		throw new IllegalArgumentException("file not uploaded");
	}
}
