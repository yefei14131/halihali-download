package com.pers.yefei.halihali.components;

import com.pers.yefei.halihali.model.bean.Job;
import com.pers.yefei.halihali.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

/**
 * @author: yefei
 * @date: 2019/1/20 19:01
 */
@Component
@Slf4j
public class DownloadedComponent {
    private LinkedHashMap<String, byte[]> data = new LinkedHashMap<>();

    @Autowired
    private WriteComponent writeComponent;

    public byte[] getCacheContent(Job job, int index) {
        try {
            if(!hasDownloaded(job, index)){
                return null;
            }

            String cachefilePath = writeComponent.getCacheFilePath(job.getFileName(), index);
            return FileUtil.readContentByteArray(cachefilePath);

        } catch (FileNotFoundException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

    public boolean hasDownloaded(Job job, int index){
        String cachefilePath = writeComponent.getCacheFilePath(job.getFileName(), index);
        File cacheFile = new File(cachefilePath);
        if ( cacheFile.exists() && cacheFile.length() > 0 ){
            return true;
        }

        return false;
    }

    public void removeCache(Job job){
        File cacheDir = new File(writeComponent.getCacheFileDir(job.getFileName()));
        for ( String cacheFileName : cacheDir.list()){
            File cacheFile = new File(cacheDir, cacheFileName);
            cacheFile.delete();
        }
        cacheDir.delete();
    }


    public void put(String jobHashCode, int index, byte[] filmContent){
        String key = getKey(jobHashCode, index);
        data.put(key, filmContent);

    }

    public byte[] get(String jobHashCode, int index)  {
        String key = getKey(jobHashCode, index);
        return data.get(key);
    }

    public byte[] getAndDelete(String jobHashCode, int index) {
        String key = getKey(jobHashCode, index);
        byte[] bytes = data.get(key);
        data.remove(key);

        return bytes;
    }


    public void remove(String jobHashCode, int index){
        String key = getKey(jobHashCode, index);
        data.remove(key);
    }

    public int size(String jobHashCode){
        return (int) data.keySet().stream().filter(
                key -> key.startsWith(jobHashCode)

        ).count();
    }

    private String getKey(String jobHashCode, int index) {
        return String.format("%s_%d", jobHashCode, index);
    }



}
