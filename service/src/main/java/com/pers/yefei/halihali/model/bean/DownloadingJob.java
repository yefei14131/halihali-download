package com.pers.yefei.halihali.model.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author: yefei
 * @date: 2019/1/20 17:10
 */
@Data
public class DownloadingJob {

    private Date startTime;

    private int index;

    private String jobHashCode;

    public DownloadingJob(String jobHashCode, int index){
        this.jobHashCode = jobHashCode;
        this.index = index;
        this.startTime = new Date();
    }
}
