package com.pers.yefei.halihali.scheduler;

import com.pers.yefei.halihali.services.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: yefei
 * @date: 2019/1/21 09:37
 */
@Slf4j
@Component
public class FlushScheduler  {

    private final int sleepTime = 10 * 1000;

    @Autowired
    private DownloadService downloadService;

//    @Scheduled(fixedRate = 10000, initialDelay = 25000)
    FlushScheduler(){
//        process();
    }

    private void process(){
        while (true) {
            try {
                Thread.sleep(sleepTime);
                System.out.println("flush schedulering");
                downloadService.flushTofile();

            }catch (Exception e){
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
