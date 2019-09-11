package com.pers.yefei.halihali.services;

import com.pers.yefei.halihali.components.JobComponent;
import com.pers.yefei.halihali.components.WriteComponent;
import com.pers.yefei.halihali.exception.FindMaxIndexErrorException;
import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.tasks.DownloadTask;
import com.pers.yefei.halihali.tasks.FlushTask;
import com.pers.yefei.halihali.utils.OkHttpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author: yefei
 * @date: 2019/1/20 19:32
 */
@Component
@Slf4j
@ConditionalOnBean(name = "okHttpHelper")
public class DownloadService {

    private final static int initMaxIndex = 3000;
    private final static int downloadPoolNum = 10;

    @Autowired
    private OkHttpHelper okHttpHelper;

    private ExecutorService executor = Executors.newFixedThreadPool(downloadPoolNum);


    @Autowired
    private JobComponent jobComponent;


    @Autowired
    private WriteComponent writeComponent;


    public void addJob(Job job) throws IOException {

        int suffixByte = getSuffixByte(job);
        String regex = String.format("^(.*?/\\w+?)\\d{%d}\\.\\w+$", suffixByte);
        String urlPrefix = job.getDemoUrl().replaceAll(regex, "$1");

        job.setSuffixByte(suffixByte);
        job.setUrlPrefix(urlPrefix);

        // 文件扩展名
        job.setSuffixName(job.getDemoUrl().replaceAll("^(.*?/\\w+?)(\\.\\w+)$", "$2"));

        //检查写入文件名是否重复，重复则修复
        String fileName = writeComponent.initDistFile(job.getFileName(), job.getSuffixName());
        job.setFileName(fileName);

        findMaxIndex(job);
        for (int i=0; i <= job.getMaxIndex(); i++)
            job.getIndexlist().add(i);

        jobComponent.add(job);
    }

    public void startJob(Job job) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(job.getMaxIndex());

        for(int i=0; i <= job.getMaxIndex(); i++){

            DownloadTask downloadTask = new DownloadTask(job, i, countDownLatch);
            executor.execute(downloadTask);
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    try {
//                        if (countDownLatch.getCount() == 0){
//                            break;
//                        }
//                        Thread.sleep(5000);
//                        log.info("queue size:{}, active:{} , max: {}, core:{}", executor.getQueue().size(), executor.getActiveCount(), executor.getMaximumPoolSize(), executor.getCorePoolSize());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

        countDownLatch.await();
        long durationTime = System.currentTimeMillis() - startTime;
        log.info("{} 下载耗时 {}s, connectionCount : {}", job.getFileName(), durationTime / 1000, okHttpHelper.connectionCount());
        // 缓存写文件
        new FlushTask(job).run();


    }



    public void flushTofile(){
        jobComponent.getJobs().forEach(job -> {
            new Thread(new FlushTask(job)).start();
        });
    }


    private int getSuffixByte(Job job){
        log.debug("正在计算后缀索引被替换的位数：{}", job.getFileName());
        int suffixByte = 0;
        if( job.getDemoUrl().matches("^(.*?/\\w+?)[a-z,A-Z]\\d{3}\\.\\w+$") ){
            suffixByte = 3;
        }else{


            String url999 =  job.getDemoUrl().replaceAll("\\d{3}(\\.\\w+)$", "999$1");
            String url1000 =  job.getDemoUrl().replaceAll("\\d{3}(\\.\\w+)$", "1000$1");

            log.debug("{} 测试url：{}", job.getFileName(), url999);
            log.debug("{} 测试url：{}", job.getFileName(), url1000);

            int responseCode999 = okHttpHelper.getResponseCode(url999);
            int responseCode1000 = okHttpHelper.getResponseCode(url1000);

            if(responseCode999 == 404 || responseCode1000 == 200){
                suffixByte = 3;
            }else{
                suffixByte = 4;
            }
        }

        log.debug("{} 后缀索引被替换的位数：{}", job.getFileName(), suffixByte);
        return suffixByte;
    }

    private void findMaxIndex(Job job){
        log.debug("正在查找最大index：{}", job.getFileName());


        int maxIndex = findMaxIndex(job, 0, initMaxIndex);
        if(maxIndex == 0 ){
            throw new FindMaxIndexErrorException();
        }

        log.debug("{} 最大index：{}", job.getFileName(), maxIndex);
        job.setMaxIndex(maxIndex);
    }


    private int findMaxIndex(Job job, int beginIndex, int endIndex){
        if (endIndex - beginIndex < 2){
            if (endIndex == initMaxIndex){
                return findMaxIndex(job, beginIndex, initMaxIndex * 2);
            }else {
                return beginIndex;
            }
        }

        int index = ( endIndex - beginIndex ) / 2 + beginIndex;
        int responseCode = 0;
        try {
//            log.info("{} 正在尝试索引:{}",  job.getFileName(), index);
            String url = job.getUrl(index);
            responseCode = okHttpHelper.getResponseCode(url);
            log.info("{} 正在尝试索引:{}, 状态码：{}, url:{}",  job.getFileName(), index, responseCode, url);

        }catch (Exception e){
            responseCode = okHttpHelper.getResponseCode(job.getUrl(index));
        }

        if (responseCode == 404){
            return findMaxIndex(job, beginIndex, index );

        }else if(responseCode == 200){
            return findMaxIndex(job, index, endIndex );
        }

        return 0;
    }




}
