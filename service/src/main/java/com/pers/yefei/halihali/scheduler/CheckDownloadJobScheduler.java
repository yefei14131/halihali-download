package com.pers.yefei.halihali.scheduler;

import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.services.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: yefei
 * @date: 2019/1/21 09:37
 */
@Slf4j
@Component
public class CheckDownloadJobScheduler {

    @Autowired
    private DownloadService downloadService;

    @Scheduled(fixedRate = 15000, initialDelay = 1000)
    private void process() throws IOException {
        System.out.println("checkdownload schedulering");

        downloadService.checkDownloadingJob();
    }
}
