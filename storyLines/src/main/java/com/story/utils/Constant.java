package com.story.utils;

public class Constant {

	public static Integer ONE = 1;
	public static Integer ZERO = 0;
	
	public static final String OPEN_ID = "openid";
	public static final String ROLE = "role";

	// Controller Result
	public static final String RESULT_STATUS = "status";
	public static final String RESULT_STATUS_SUCCESS = "success";
	public static final String RESULT_STATUS_FAILURE = "failure";
	public static final String RESULT_MESSAGE = "message";
	public static final String RESULT_DATA = "data";
	public static final String RESULT_STORY = "story";
	public static final String RESULT_PHASE = "phase";

	// Punctuation
	public static final String PUNCTUATION_COMMA = ",";

	// Wechat
	public static final String APPID = "wxee45d2d0a12bf77a";// test account: "
															// wxfa215961decf944c";
	public static final String SECRET = "2ff4924bfc4581cfb5c7a2de13cdb6b6";// test
																			// account:
																			// "
																			// 21028fef3a271a280c2712240bb0e3ba";

	public static final String AUTH_URL = "https://api.weixin.qq.com/sns/jscode2session?"
			+ "appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

	// User Role
	public static final String USER_ROLE_ADMIN = "ADMIN";
	public static final String USER_ROLE_USER = "USER";

}
