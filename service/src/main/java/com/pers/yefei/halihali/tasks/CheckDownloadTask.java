package com.pers.yefei.halihali.tasks;

import com.pers.yefei.halihali.components.DownloadedComponent;
import com.pers.yefei.halihali.components.WriteComponent;
import com.pers.yefei.halihali.model.bean.DownloadingJob;
import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author: yefei
 * @date: 2019/1/20 23:48
 */
@Slf4j
public class CheckDownloadTask implements Runnable {

    private Job job;

    private DownloadedComponent downloadedComponent;

    private WriteComponent writeComponent;

    private final static int TIMEOUT = 30000; //下载任务超时时间， 超过此时间，启动新线程下载
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
    public synchronized void run() {

        Integer finishedIndex = job.getFinishedIndexlist().peekLast();
        int nextIndex = finishedIndex == null ? 0 : finishedIndex + 1;

        boolean nextIndexIsDownloading = downloadedComponent.hasDownloaded(job, nextIndex);

        if(!nextIndexIsDownloading){
            log.info("下载任务{} - {} 未下载完毕 启动新线程继续下载, {}{}.ts", job.getFileName(), nextIndex, job.getUrlPrefix(), nextIndex);

            new Thread(new DownloadTask(job, nextIndex, downloadedComponent, writeComponent)).start();
        }

    }

    public CheckDownloadTask(Job job, DownloadedComponent downloadedComponent, WriteComponent writeComponent){
        this.job = job;
        this.downloadedComponent = downloadedComponent;
        this.writeComponent = writeComponent;
    }


}
