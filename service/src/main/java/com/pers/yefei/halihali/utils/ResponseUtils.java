package com.pers.yefei.halihali.utils;

import com.alibaba.fastjson.JSONObject;
import com.pers.yefei.halihali.exception.ResponseCodeEnum;
import com.pers.yefei.halihali.exception.ServerBaseException;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class ResponseUtils {
	private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
	

	
	private static JSONObject responseJSON(int code , String message ){
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("message",message);
		
		return obj;
	}

	private static JSONObject responseJSON(int code ,String message, Object msgData ){
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("message",message);
		obj.put("data",msgData);

		return obj;
	}

    private static void writeData(HttpServletResponse response, JSONObject data){
        try{
            String jsonString = data.toString();
			response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType(CONTENT_TYPE_JSON);
            byte[] resonseBuf = jsonString.getBytes("UTF-8");
            response.setContentLength(resonseBuf.length);
            response.getOutputStream().write(resonseBuf);

            if (log.isDebugEnabled()) log.debug(jsonString);
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }
	
	public static void writeResponseSuccess(HttpServletResponse response) {

        JSONObject objJson = responseJSON(ResponseCodeEnum.SUSCCESS.getCode(), ResponseCodeEnum.SUSCCESS.getReason());
        writeData(response, objJson);

	}

	public static void writeResponseSuccess(HttpServletResponse response, String message) {
        JSONObject objJson = responseJSON(ResponseCodeEnum.SUSCCESS.getCode(), message);
        writeData(response, objJson);
	}


	public static void writeResponseSuccess(HttpServletResponse response, Object msgData) {
        JSONObject objJson = responseJSON(ResponseCodeEnum.SUSCCESS.getCode(), ResponseCodeEnum.SUSCCESS.getReason(), msgData);
        writeData(response, objJson);
	}


	public static void writeResponseFailure(HttpServletResponse response, ServerBaseException e) {
        JSONObject objJson = responseJSON(ResponseCodeEnum.FAIL.getCode(), ResponseCodeEnum.FAIL.getReason());
        writeData(response, objJson);
	}

	
	public static void writeResponseFailure(HttpServletResponse response, String errMsg) {
        JSONObject objJson = responseJSON(ResponseCodeEnum.FAIL.getCode(), errMsg);
        writeData(response, objJson);
	}


	public static boolean sendImg(HttpServletResponse response, BufferedImage buffImg, String mimeType, String fileName, String extName){
		OutputStream out= null;
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("attachment;filename=%s.%s", fileName, extName));
		try {
			out= response.getOutputStream();
			ImageIO.write(buffImg, extName, out);
//			BufferedInputStream buffIn= new BufferedInputStream(in);
//			BufferedOutputStream buffOut= new BufferedOutputStream(out);
//			byte buff[]= new byte[4096];//申请缓冲区
//			int size= buffIn.read(buff);//初始化读取缓冲
//			while (size!= -1) {
//				buffOut.write(buff, 0, size);//输出缓冲数据
//				size= buffIn.read(buff);//继续读取数据
//			}
//			buffIn.close();//关闭输入流
//			buffOut.flush();//清空输出流
//			buffOut.close();//关闭输出流
			return true;
		} catch (IOException e) {
			log.error("直接推送图片错误", e);
			return false;
		}
	}

}
