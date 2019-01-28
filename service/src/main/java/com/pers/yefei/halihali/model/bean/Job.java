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
    private String urlPrefix;
    private String fileName;
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

}
