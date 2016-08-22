package com.itdoes.business.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.Result;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping("/upload")
public class UploadController {
	@RequestMapping(method = RequestMethod.GET)
	public String uploadForm() {
		return "upload";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result upload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file)
			throws IOException {
		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			return Result.success(new Integer[] { bytes.length });
		}

		throw new IllegalArgumentException("file not uploaded");
	}
}
