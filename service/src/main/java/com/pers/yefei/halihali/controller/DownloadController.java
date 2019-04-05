package com.pers.yefei.halihali.controller;

import com.pers.yefei.halihali.exception.ServerBaseException;
import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.services.DownloadService;
import com.pers.yefei.halihali.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author: yefei
 * @date: 2018/11/9 22:13
 */
@Controller
@Slf4j
@RequestMapping(value = "/move/jobs")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @RequestMapping(value = "/add")
    public void execCase(@RequestParam("url") String url
            , @RequestParam("fileName") String fileName
            , HttpServletResponse response ) {

        try {

            String regex = String.format("^(.*?/\\w+?)\\d{%d}.\\w+$", 3);

            if ( url.matches(regex) ){

                Job job = new Job();
                job.setFileName(fileName);
                job.setDemoUrl(url);

                downloadService.addJob(job);

                downloadService.startJob(job);

                HashMap msgData = new HashMap();
                msgData.put("maxIndex",  job.getMaxIndex());

                ResponseUtils.writeResponseSuccess(response, msgData);
            }else{
                ResponseUtils.writeResponseFailure(response, "url格式不正确");

            }



        }catch (ServerBaseException e){
            log.error(ExceptionUtils.getStackTrace(e));
            ResponseUtils.writeResponseFailure(response, e);
        } catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
            ResponseUtils.writeResponseFailure(response, ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/add2")
    public void addJob(@RequestParam("url") String url, @RequestParam("fileName") String fileName, HttpServletResponse response ) {

        try {

            if ( url.matches("^.*?/\\w+?\\d{3}.ts$") ){
                String urlPrefix = url.replaceAll("^(.*?/\\w+?)\\d{3}.ts$", "$1");

                Job job = new Job();
                job.setUrlPrefix(urlPrefix);
                job.setFileName(fileName);

                downloadService.addJob(job);

                downloadService.startJob(job);

                HashMap msgData = new HashMap();
                msgData.put("maxIndex",  job.getMaxIndex());

                ResponseUtils.writeResponseSuccess(response, msgData);
            }



        }catch (ServerBaseException e){
            log.error(ExceptionUtils.getStackTrace(e));
            ResponseUtils.writeResponseFailure(response, e);
        } catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
            ResponseUtils.writeResponseFailure(response, ExceptionUtils.getStackTrace(e));
        }
    }


}
