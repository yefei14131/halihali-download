package com.pers.yefei.halihali.tasks;

import com.pers.yefei.halihali.components.DownloadedComponent;
import com.pers.yefei.halihali.components.JobComponent;
import com.pers.yefei.halihali.components.WriteComponent;
import com.pers.yefei.halihali.config.ApplicationContextProvider;
import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.util.LinkedList;

/**
 * @author: yefei
 * @date: 2019/1/20 23:48
 */
@Slf4j
public class FlushTask implements Runnable {

    private Job job;

    private DownloadedComponent downloadedComponent = ApplicationContextProvider.getBean(DownloadedComponent.class);

    private WriteComponent writeComponent = ApplicationContextProvider.getBean(WriteComponent.class);

    private JobComponent jobComponent = ApplicationContextProvider.getBean(JobComponent.class);;

    private static final int sleepTime = 10 * 1000;

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

        while (true) {
            try {

                Thread.sleep(sleepTime);

                LinkedList<Integer> finishedIndexlist = job.getFinishedIndexlist();
                if (finishedIndexlist.size() >= job.getMaxIndex() + 1) {
                    log.info("{} 下载完成，maxIndex:{}, finishedSize:{}", job.getFileName(), job.getMaxIndex(), finishedIndexlist.size());
//                    log.info("{} 下载完成，finishedIndexList:{}", job.getFileName(), finishedIndexlist);
                    jobComponent.remove(job);
                    downloadedComponent.removeCache(job);
                    break;
                }
//
//                if (downloadedComponent.size(job.getJobHashCode()) == 0) {
//                    log.info("{} 没有下载就绪的文件, max:{}, 已缓存:{}", job.getFileName(), job.getMaxIndex(), job.getFinishedIndexlist().size());
//                    continue;
//                }

                Integer lastIndex = finishedIndexlist.peekLast();
                lastIndex = lastIndex == null ? -1 : lastIndex;

                for (int index = lastIndex + 1; index <= job.getMaxIndex(); index++) {
                    byte[] content = downloadedComponent.getCacheContent(job, index);
                    if (content == null || content.length == 0) {
                        log.info("{} 当前index（{}）下载未就绪！{}{}.ts", job.getFileName(), index, job.getUrlPrefix(), index);
                        log.info(downloadedComponent.toString());
                        break;
                    }

                    String fileName = job.getFileName();

                    try {

                        File distFile = new File(writeComponent.getDistFilePath(fileName, job.getSuffixName()));

//                        FileWriter fileWriter = new FileWriter(distFile, true);
//                        fileWriter.write(new String(content));

                        FileUtil.appendFileData(distFile.getPath(), content);

                        finishedIndexlist.add(index);

                    } catch (Exception e) {
                        log.error(ExceptionUtils.getStackTrace(e));
                    }
                }

            }catch (Exception e){
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }

    }

    public FlushTask(Job job){
        this.job = job;
    }


}
