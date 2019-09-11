package com.pers.yefei.halihali.model.bean;


import com.pers.yefei.halihali.utils.MD5Utils;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author: yefei
 * @date: 2019/1/20 14:33
 */
@Data
public class Job {
    private String demoUrl;
    private int suffixByte = 3; //url后面数字的位数,影响后面0补全的位数
    private String urlPrefix;
    private String fileName;
    private String suffixName;
    private int maxIndex;
    private LinkedList<Integer> indexlist = new LinkedList<>();
    private LinkedList<Integer> finishedIndexlist = new LinkedList<>();
    private LinkedHashMap<Integer, DownloadingJob> downloadingJobs = new LinkedHashMap<>();

    public String getJobHashCode(){
        try {
            return String.format("%s_%s", MD5Utils.md5Encode(urlPrefix), fileName);
        }catch (Exception e){
            return null;
        }
    }

    public String getUrl(int index){

        return String.format("%s%s%s", urlPrefix, fillZero(index, suffixByte), suffixName);
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
