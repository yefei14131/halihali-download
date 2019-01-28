package com.pers.yefei.halihali.tasks;

import com.pers.yefei.halihali.components.DownloadedComponent;
import com.pers.yefei.halihali.components.WriteComponent;
import com.pers.yefei.halihali.model.bean.DownloadingJob;
import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.utils.FileUtil;
import com.pers.yefei.halihali.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yefei
 * @date: 2019/1/20 23:00
 */
@Slf4j
public class DownloadTask implements Runnable {

    private final static int timeout = 60; //超时时间

    private Job job;

    private DownloadedComponent downloadedComponent;

    private WriteComponent writeComponent;

    private List<Integer> indexList = new ArrayList<>();

    private List<Integer> retryIndexList = new ArrayList<>();


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        List<Integer> failIndexList = download(indexList);
        if (indexList.size() == 1){
            //补偿进程，直接退出
            return;
        }

        List<Integer> retryIndexList = download(failIndexList);
        while (retryIndexList.size() > 0){
            retryIndexList = download(retryIndexList);
        }

    }

    private List<Integer> download(List<Integer> indexList){
        List<Integer> retryIndexList = new ArrayList<>();

        for (int i=0; i < indexList.size(); i++){
            int index = indexList.get(i);

            try {
                if (downloadedComponent.hasDownloaded(job, index)){
                    log.debug("{} index {} 已经下载过，跳过下载任务", job.getFileName(), index);
                    continue;
                }

//                DownloadingJob downloadingJob = new DownloadingJob(job.getJobHashCode(), index);
//                job.getDownloadingJobs().put(index, downloadingJob);

                download(job, index);

                log.info("{} index {} 缓存完毕", job.getFileName(), index);

            }catch (Exception e){
                log.error(ExceptionUtils.getStackTrace(e));
                retryIndexList.add(index);
            }
        }

        return retryIndexList;
    }


    public DownloadTask(Job job, List<Integer> indexList, DownloadedComponent downloadedComponent, WriteComponent writeComponent){
        this.job = job;
        this.indexList = indexList;
        this.downloadedComponent = downloadedComponent;
        this.writeComponent = writeComponent;
    }


    public DownloadTask(Job job, int index, DownloadedComponent downloadedComponent, WriteComponent writeComponent){
        this.job = job;
        this.indexList.add(index);
        this.downloadedComponent = downloadedComponent;
        this.writeComponent = writeComponent;
    }

    private boolean download(Job job, int index) throws IOException {
        String url = String.format("%s%s.ts", job.getUrlPrefix(), fillZero(index, 3));
        String cachefilePath = writeComponent.getCacheFilePath(job.getFileName(), index);

        byte[] bytes = HttpUtil.doGet(url, timeout);
        FileUtil.writeFileData(cachefilePath, bytes);

//            downloadedComponent.put(job.getJobHashCode(), index, bytes);
//            job.getDownloadingJobs().remove(index);


        return true;
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
