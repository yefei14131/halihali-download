package com.pers.yefei.halihali.utils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class OkHttpHelper {

    final static int timeout = 60;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(timeout, TimeUnit.SECONDS).build();

    public byte[] getResponseByte(String url) throws IOException {

        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);

        Response response = call.execute();
        byte[] bytes = response.body().bytes();
        response.close();
        return bytes;

    }


    public int getResponseCode(String url) {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            int code = response.code();
            response.close();

            return code;
        } catch (IOException e) {
           return 0;
        }
    }



}
