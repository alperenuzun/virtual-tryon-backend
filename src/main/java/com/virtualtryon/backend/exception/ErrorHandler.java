package com.virtualtryon.backend.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
public class ErrorHandler implements ErrorController {
	
	@Autowired
	private ErrorAttributes errorAttributes;
	
	@RequestMapping("/error")
    ApiError handleError(WebRequest webRequest) {
		Map<String, Object> attributes = errorAttributes.getErrorAttributes(webRequest, true);
		
		String allMessage = (String) attributes.get("message");
		int startPos = allMessage.lastIndexOf("messageTemplate='");
		int endPos = allMessage.length() - 4;
		String message = (startPos != -1) ? allMessage.substring(startPos+17, endPos) : allMessage;

		String url = (String) attributes.get("path");
		int status = (Integer) attributes.get("status");
		return new ApiError(status, message, url);
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
