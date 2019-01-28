package com.pers.yefei.halihali.exception;


/**
 *
 */
public class FindMaxIndexErrorException extends ServerBaseException {

	private static final long serialVersionUID = 1L;


	public FindMaxIndexErrorException() {
		super(ResponseCodeEnum.FindMaxIndexError.getCode(), ResponseCodeEnum.FindMaxIndexError.getReason());
	}

	public FindMaxIndexErrorException(String message) {
		super(ResponseCodeEnum.FindMaxIndexError.getCode(), ResponseCodeEnum.FindMaxIndexError.getReason() + ":" + message);
	}



	public FindMaxIndexErrorException(String message, Exception e) {
		super(ResponseCodeEnum.FindMaxIndexError.getCode(), ResponseCodeEnum.FindMaxIndexError.getReason() + ":" + message, e);
	}


	public FindMaxIndexErrorException(Exception e) {
		super(ResponseCodeEnum.FindMaxIndexError.getCode(), ResponseCodeEnum.FindMaxIndexError.getReason(), e);
	}

}
