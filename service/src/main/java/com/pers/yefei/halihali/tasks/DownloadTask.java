package com.pers.yefei.halihali.tasks;

import com.pers.yefei.halihali.components.DownloadedComponent;
import com.pers.yefei.halihali.components.WriteComponent;
import com.pers.yefei.halihali.config.ApplicationContextProvider;
import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.utils.FileUtil;
import com.pers.yefei.halihali.utils.OkHttpHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author: yefei
 * @date: 2019/1/20 23:00
 */
@Slf4j
public class DownloadTask implements Runnable {

    private DownloadedComponent downloadedComponent = ApplicationContextProvider.getBean(DownloadedComponent.class);

    private WriteComponent writeComponent = ApplicationContextProvider.getBean(WriteComponent.class);

    private OkHttpHelper okHttpHelper = ApplicationContextProvider.getBean(OkHttpHelper.class);

    private Job job;

    private int index;

    private CountDownLatch countDownLatch;

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

        if (downloadedComponent.hasDownloaded(job, index)){
            log.info("{} index {} 已经下载过，跳过下载任务", job.getFileName(), index);
            countDownLatch.countDown();
            return;
        }

        boolean success = false;
        while (!success){

            try {
                download();
                log.info("{} index {} 缓存完毕", job.getFileName(), index);
                countDownLatch.countDown();
                success = true;
            } catch (IOException e) {
                log.info("{} index {} 下载失败，重新开始", job.getFileName(), index);

            }

        }
    }



    public DownloadTask(Job job, int index, CountDownLatch countDownLatch){
        this.job = job;
        this.index = index;
        this.countDownLatch = countDownLatch;
    }


    private boolean download() throws IOException {
        String url = job.getUrl(index);
        String cachefilePath = writeComponent.getCacheFilePath(job.getFileName(), index);
//        okHttpHelper.getResponseCode(url);
//        byte[] bytes = HttpUtil.doGet(url, timeout);
        byte[] bytes = okHttpHelper.getResponseByte(url);
        FileUtil.writeFileData(cachefilePath, bytes);

        return true;
    }


}
