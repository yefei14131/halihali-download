package com.pers.yefei.halihali.services;

import com.pers.yefei.halihali.components.DownloadedComponent;
import com.pers.yefei.halihali.components.JobComponent;
import com.pers.yefei.halihali.components.WriteComponent;
import com.pers.yefei.halihali.exception.FindMaxIndexErrorException;
import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.tasks.CheckDownloadTask;
import com.pers.yefei.halihali.tasks.DownloadTask;
import com.pers.yefei.halihali.tasks.FlushTask;
import com.pers.yefei.halihali.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: yefei
 * @date: 2019/1/20 19:32
 */
@Service
@Slf4j
public class DownloadService {

    private final static int initMaxIndex = 3000;
    private final static int downloadPoolNum = 30;

    @Autowired
    private JobComponent jobComponent;


    @Autowired
    private WriteComponent writeComponent;

    @Autowired
    private DownloadedComponent downloadedComponent;


    public void init(){

//        jobComponent.add(job);
    }


    public void addJob(Job job) throws IOException {
        //检查写入文件名是否重复，重复则修复

        String fileName = writeComponent.initDistFile(job.getFileName());
        job.setFileName(fileName);


        findMaxIndex(job);
        for (int i=0; i <= job.getMaxIndex(); i++)
            job.getIndexlist().add(i);

        jobComponent.add(job);
    }

    public void startJob(Job job){

        ThreadPoolExecutor executor = new ThreadPoolExecutor(downloadPoolNum, downloadPoolNum * 2, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(downloadPoolNum));

        for(int i=0; i < downloadPoolNum; i++){

            List<Integer> indexList = new ArrayList<>();
            for( int j=0; j<= job.getMaxIndex() / downloadPoolNum; j++){
                if(j * downloadPoolNum + i > job.getMaxIndex()){
                    break;
                }
                indexList.add(j * downloadPoolNum + i);
            }

            DownloadTask downloadTask = new DownloadTask(job, indexList, downloadedComponent, writeComponent);
            executor.execute(downloadTask);
        }

        // 缓存写文件
        new Thread(new FlushTask(job, downloadedComponent, writeComponent, jobComponent)).start();

//        executor.shutdown();
    }

    public void flushTofile(){
        jobComponent.getJobs().forEach(job -> {
            new Thread(new FlushTask(job, downloadedComponent, writeComponent, jobComponent)).start();
        });
    }

    public void checkDownloadingJob(){
        jobComponent.getJobs().forEach(job -> {
            new Thread(new CheckDownloadTask(job, downloadedComponent, writeComponent)).start();
        });
    }


    private void findMaxIndex(Job job){
        log.debug("正在查找最大index：{}", job.getFileName());

        String urlPrefix = job.getUrlPrefix();

        int maxIndex = findMaxIndex(urlPrefix, 0, initMaxIndex);
        if(maxIndex == 0 ){
            throw new FindMaxIndexErrorException();
        }

        log.debug("{} 最大index：{}", job.getFileName(), maxIndex);
        job.setMaxIndex(maxIndex);
    }

    private int findMaxIndex(String urlPrefix, int beginIndex, int endIndex){
        if (endIndex - beginIndex < 2){

            return beginIndex;
        }

        int index = ( endIndex - beginIndex ) / 2 + beginIndex;

        int responseCode = HttpUtil.getResponseCode(String.format("%s%d.ts", urlPrefix, index));

        if (responseCode == 404){
            return findMaxIndex(urlPrefix, beginIndex, index );

        }else if(responseCode == 200){
            return findMaxIndex(urlPrefix, index, endIndex );
        }

        return 0;
    }




}
