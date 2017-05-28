package com.story.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpResult {

	private String status;

	private String message;

	private Object data;

	public HttpResult(String status, String messge) {
		this.status = status;
		this.message = messge;
	}

	public HttpResult(String status, String messge, Object data) {
		this.status = status;
		this.message = messge;
		this.data = data;
	}

	public Map<String, Object> build() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Constant.RESULT_STATUS, this.status);
		result.put(Constant.RESULT_MESSAGE, this.message);
		if (this.data != null) {
			result.put(Constant.RESULT_DATA, this.data);
		}
		return result;
	}
	
}
