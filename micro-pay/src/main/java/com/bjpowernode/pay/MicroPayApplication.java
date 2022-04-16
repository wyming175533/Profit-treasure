package com.bjpowernode.pay;

import com.bjpowernode.Util.RedisOpreation;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@EnableDubbo
@SpringBootApplication
public class MicroPayApplication {
	@Resource
	private RedisTemplate redisTemplate;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Bean
	public RedisOpreation redisOpreation(){
		RedisOpreation redisOpreation=new RedisOpreation(stringRedisTemplate,redisTemplate);
		return redisOpreation;
	}
	public static void main(String[] args) {
		SpringApplication.run(MicroPayApplication.class, args);
	}

}
