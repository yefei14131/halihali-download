package com.pers.yefei.halihali.exception;


public enum ResponseCodeEnum {
	
	SUSCCESS(0,"success")
	, FAIL(1001,"系统繁忙，请稍后重试！")
	, WELCOME(2001, "欢迎使用MockServer")
	, FILE_NOFUND(20011, "文件不存在！")
	, FindMaxIndexError(20021, "获取最大索引失败！")
	;

	
	 private final int code;
	 private final String reason;
	 
	 ResponseCodeEnum(int code, String reason) {
	    this.code = code;
	    this.reason= reason;
	 }

	 public int getCode(){
		 return this.code;
	 }
	 public String getReason(){
		 return this.reason;
	 }
}
