package com.bjpowernode.microtimedtask;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableDubbo
@EnableScheduling
@SpringBootApplication
public class MicroTimedtaskApplication {

	public static void main(String[] args) {

		ApplicationContext ctx=SpringApplication.run(MicroTimedtaskApplication.class, args);
		TaskManager taskManager= (TaskManager) ctx.getBean("taskManager");
	}

}
