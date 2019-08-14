//package com.pers.yefei.halihali.scheduler;
//
//import com.pers.yefei.halihali.model.bean.Job;
//import com.pers.yefei.halihali.services.DownloadService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * @author: yefei
// * @date: 2019/1/21 09:37
// */
//@Slf4j
//@Component
//public class InitJobScheduler {
//
//    @Autowired
//    private DownloadService downloadService;
//
////    @Scheduled(fixedRate = 10000000, initialDelay = 1000)
//    private void process() throws IOException {
//        System.out.println("InitJobScheduler schedulering");
//
//
//        Job job = new Job();
////        job.setUrlPrefix("https://kakazy-yun.com/20181219/13192_8c10a795/1000k/hls/53e4c451578");
////        job.setUrlPrefix("https://kakazy-yun.com/20181221/13331_8fdf6ccc/1000k/hls/23d6e4ddf49");
//
//
////        job.setFileName("我知道你是谁第三集");
//
//
//        job.setUrlPrefix("https://kakazy-yun.com/20181225/13739_4df9ec61/1000k/hls/47f946e501b");
//        job.setFileName("调琴师");
//
//
////        downloadService.addJob(job);
////        downloadService.startJob(job);
//    }
//}
