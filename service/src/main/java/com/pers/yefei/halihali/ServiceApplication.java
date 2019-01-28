package com.pers.yefei.halihali;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude= {})
@EnableScheduling
@ComponentScan(basePackages = {"com.pers.yefei"})
public class ServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServiceApplication.class, args);
	}
}
