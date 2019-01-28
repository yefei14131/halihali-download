package com.pers.yefei.halihali.exception;


/**
 *
 */
public class FileNoFoundException extends ServerBaseException {

	private static final long serialVersionUID = 1L;


	public FileNoFoundException() {
		super(ResponseCodeEnum.FILE_NOFUND.getCode(), ResponseCodeEnum.FILE_NOFUND.getReason());
	}

	public FileNoFoundException(String message) {
		super(ResponseCodeEnum.FILE_NOFUND.getCode(), ResponseCodeEnum.FILE_NOFUND.getReason() + ":" + message);
	}



	public FileNoFoundException(String message, Exception e) {
		super(ResponseCodeEnum.FILE_NOFUND.getCode(), ResponseCodeEnum.FILE_NOFUND.getReason() + ":" + message, e);
	}


	public FileNoFoundException(Exception e) {
		super(ResponseCodeEnum.FILE_NOFUND.getCode(), ResponseCodeEnum.FILE_NOFUND.getReason(), e);
	}

}
