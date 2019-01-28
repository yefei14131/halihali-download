package com.pers.yefei.halihali.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author: yefei
 * @date: 2018/11/9 22:44
 */
@Slf4j
public class FileUtils {

    public static void downFile(String netUrl, File destFile) throws IOException {

        if(destFile.exists()){

//            FileWriter fileWriter =new FileWriter(destFile);
//            fileWriter.write("");
//            fileWriter.flush();
//            fileWriter.close();

        }else{
            mkDir(destFile.getParentFile());
            destFile.createNewFile();
        }

        int byteread = 0;

        URL url = new URL(netUrl);
        FileOutputStream fs = null;
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            fs = new FileOutputStream(destFile);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }finally {
            if (fs != null){
                fs.close();
            }
        }

    }


    public static void mkDir(File dir) {
        if (dir.exists()){
            return;
        }

        if (dir.getParentFile().exists()) {
            dir.mkdir();
        } else {
            mkDir(dir.getParentFile());
            dir.mkdir();
        }
    }
}
