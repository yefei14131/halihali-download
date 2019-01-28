package com.pers.yefei.halihali.exception;


import lombok.Data;

@Data
public abstract class ServerBaseException extends RuntimeException {

	protected int code;

	protected String message;

	public ServerBaseException(int code, String message){
		this.code = code;
		this.message = message;
	}

	public ServerBaseException (int code, String message, Exception ex){
		super(ex);
		this.code = code;
		this.message = message;
	}


	@Override
	public String toString(){
		return String.format("ServerBaseException - %s [code = %s, message = %s]", this.getClass().getName(),  this.code, this.getMessage(), this.getMessage());

	}
}
