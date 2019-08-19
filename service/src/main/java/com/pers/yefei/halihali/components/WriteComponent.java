package com.pers.yefei.halihali.components;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: yefei
 * @date: 2019/1/20 19:48
 */
@Component
public class WriteComponent {


    /**
     *
     * @param fileName
     * @return  final fileName
     * @throws IOException
     */
    public String initDistFile(String fileName) throws IOException {

        if (StringUtils.isEmpty(fileName)){
            return null;
        }


        String fileDir = String.format("%s/tmp/yefei/halihali", System.getProperties().getProperty("user.home"));

        File distDir = new File(fileDir);
        if(!distDir.exists()) {
            distDir.mkdirs();
        }

        int index = 0;
        String finalFileName = genFileName(fileName, index);;


        //缓存文件路径
        String cacheFileDirPath = getCacheFileDir(finalFileName);
        File cacheFileDir = new File(cacheFileDirPath);
        if(!cacheFileDir.exists()) {
            cacheFileDir.mkdirs();
        }

        File distFile = new File(getDistFilePath(finalFileName));
        if(distFile.exists()){
            //清空文件
            FileWriter fw = new FileWriter(distFile);
            fw.write("");
            fw.close();
        }

        return finalFileName;
    }

    public String getDistFilePath(String fileName){
        return String.format("%s/tmp/yefei/halihali/%s.ts", System.getProperties().getProperty("user.home"), fileName);
    }

    public String getCacheFileDir(String fileName){
        return String.format("%s/tmp/yefei/halihali/%s.cache", System.getProperties().getProperty("user.home"), fileName);
    }

    public String getCacheFilePath(String fileName, int index){
        return String.format("%s/tmp/yefei/halihali/%s.cache/%s.ts", System.getProperties().getProperty("user.home"), fileName, fillZero(index, 4));
    }


    private String genFileName( String fileName, int index){
        if (index == 0)
            return fileName;
        else
            return String.format("%s(%d)", fileName, index);
    }

    private String fillZero(int val, int digitNumber){
        String result = "";
        for (int i=1; i < digitNumber; i++){
            if (val < Math.pow((10), (i))){
                result += "0";
            }
        }
        result += val;

        return result;
    }

}
