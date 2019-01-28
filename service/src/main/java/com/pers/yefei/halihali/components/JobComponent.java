package com.pers.yefei.halihali.components;

import com.pers.yefei.halihali.model.bean.Job;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * @author: yefei
 * @date: 2019/1/20 19:01
 */
@Data
@Component
public class JobComponent {
    private LinkedList<Job> jobs = new LinkedList<>();

    public void add(Job job) {
        jobs.add(job);

    }

    public void remove(Job job){
        jobs.remove(job);
    }

}
