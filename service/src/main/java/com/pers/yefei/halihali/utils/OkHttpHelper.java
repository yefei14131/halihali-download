package com.pers.yefei.halihali.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class OkHttpHelper {

    final static int timeout = 60;
    private static OkHttpClient okHttpClient;

    @PostConstruct
    void init(){
        ConnectionPool connectionPool = new ConnectionPool(10, 10, TimeUnit.MINUTES);
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectionPool(connectionPool)
                .build();
    }


    public byte[] getResponseByte(String url) throws IOException {

        Request request = new Request.Builder().url(url)
                .build();
        Call call = okHttpClient.newCall(request);

        Response response = call.execute();
        byte[] bytes = response.body().bytes();
        response.close();
        return bytes;

    }


    public int getResponseCode(String url) {
        Request request = new Request.Builder().url(url)
                .build();
        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            int code = response.code();
            response.close();

            return code;
        } catch (IOException e) {
            log.error("curl error", e);
           return 0;
        }
    }

    public int connectionCount(){
        return okHttpClient.connectionPool().connectionCount();
    }

}
