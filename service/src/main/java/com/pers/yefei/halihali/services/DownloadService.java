package com.pers.yefei.halihali.services;

import com.pers.yefei.halihali.components.DownloadedComponent;
import com.pers.yefei.halihali.components.JobComponent;
import com.pers.yefei.halihali.components.WriteComponent;
import com.pers.yefei.halihali.exception.FindMaxIndexErrorException;
import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.tasks.DownloadTask;
import com.pers.yefei.halihali.tasks.FlushTask;
import com.pers.yefei.halihali.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author: yefei
 * @date: 2019/1/20 19:32
 */
@Service
@Slf4j
public class DownloadService {

    private final static int initMaxIndex = 3000;
    private final static int downloadPoolNum = 10;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(downloadPoolNum, downloadPoolNum * 2, 200, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(initMaxIndex * 2));

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

        int suffixByte = getSuffixByte(job);
        String regex = String.format("^(.*?/\\w+?)\\d{%d}.\\w+$", suffixByte);
        String urlPrefix = job.getDemoUrl().replaceAll(regex, "$1");

        job.setSuffixByte(suffixByte);
        job.setUrlPrefix(urlPrefix);

        //检查写入文件名是否重复，重复则修复
        String fileName = writeComponent.initDistFile(job.getFileName());
        job.setFileName(fileName);

        findMaxIndex(job);
        for (int i=0; i <= job.getMaxIndex(); i++)
            job.getIndexlist().add(i);

        jobComponent.add(job);
    }

    public void startJob(Job job) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(job.getMaxIndex());

        for(int i=0; i <= job.getMaxIndex(); i++){

            DownloadTask downloadTask = new DownloadTask(job, i, countDownLatch, downloadedComponent, writeComponent);
            executor.execute(downloadTask);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if (countDownLatch.getCount() == 0){
                            break;
                        }
                        Thread.sleep(5000);
                        log.info("queue size:{}, active:{} , max: {}, core:{}", executor.getQueue().size(), executor.getActiveCount(), executor.getMaximumPoolSize(), executor.getCorePoolSize());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        countDownLatch.await();
        // 缓存写文件
        new Thread(new FlushTask(job, downloadedComponent, writeComponent, jobComponent)).start();


    }



    public void flushTofile(){
        jobComponent.getJobs().forEach(job -> {
            new Thread(new FlushTask(job, downloadedComponent, writeComponent, jobComponent)).start();
        });
    }


    private int getSuffixByte(Job job){
        log.debug("正在计算后缀索引被替换的位数：{}", job.getFileName());

        String urlPrefix = job.getUrlPrefix();

        String url999 =  job.getDemoUrl().replaceAll("\\d{3}(\\.\\w+)$", "999$1");
        String url1000 =  job.getDemoUrl().replaceAll("\\d{3}(\\.\\w+)$", "1000$1");

        log.debug("{} 测试url：{}", job.getFileName(), url999);
        log.debug("{} 测试url：{}", job.getFileName(), url1000);

        int responseCode999 = HttpUtil.getResponseCode(url999);
        int responseCode1000 = HttpUtil.getResponseCode(url1000);
        int suffixByte = 0;
        if(responseCode999 == 404 || responseCode1000 == 200){
            suffixByte = 3;
        }else{
            suffixByte = 4;
        }

        log.debug("{} 后缀索引被替换的位数：{}", job.getFileName(), suffixByte);
        return suffixByte;
    }

    private void findMaxIndex(Job job){
        log.debug("正在查找最大index：{}", job.getFileName());

//        String urlPrefix = job.getUrlPrefix();

        int maxIndex = findMaxIndex(job, 0, initMaxIndex);
        if(maxIndex == 0 ){
            throw new FindMaxIndexErrorException();
        }

        log.debug("{} 最大index：{}", job.getFileName(), maxIndex);
        job.setMaxIndex(maxIndex);
    }

    private int findMaxIndex(Job job, int beginIndex, int endIndex){
        if (endIndex - beginIndex < 2){

            return beginIndex;
        }

        int index = ( endIndex - beginIndex ) / 2 + beginIndex;
        int responseCode = 0;
        try {
//            log.info("{} 正在尝试索引:{}",  job.getFileName(), index);
            String url = job.getUrl(index);
            responseCode = HttpUtil.getResponseCode(url);
            log.info("{} 正在尝试索引:{}, 状态码：{}, url:{}",  job.getFileName(), index, responseCode, url);
        }catch (Exception e){
            responseCode = HttpUtil.getResponseCode(job.getUrl(index));
        }

        if (responseCode == 404){
            return findMaxIndex(job, beginIndex, index );

        }else if(responseCode == 200){
            return findMaxIndex(job, index, endIndex );
        }

        return 0;
    }




}
