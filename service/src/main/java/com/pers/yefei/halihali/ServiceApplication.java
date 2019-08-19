package com.pers.yefei.halihali;

import com.pers.yefei.halihali.config.ApplicationContextProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude= {})
//@EnableScheduling
@ComponentScan(basePackages = {"com.pers.yefei"})
public class ServiceApplication {

	public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(ServiceApplication.class, args);
		ApplicationContextProvider.setApplicationContext(applicationContext);

	}
}
