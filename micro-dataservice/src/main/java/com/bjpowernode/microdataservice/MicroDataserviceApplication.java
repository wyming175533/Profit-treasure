package com.bjpowernode.microdataservice;

import com.bjpowernode.api.po.DicInfo;
import com.bjpowernode.microdataservice.mapper.DicInfoMapper;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MapperScan("com.bjpowernode.microdataservice.mapper")
@EnableDubbo
@SpringBootApplication
public class MicroDataserviceApplication implements CommandLineRunner {
	@Resource
	private DicInfoMapper dicInfoMapper;

	@Resource
	private StringRedisTemplate stringRedisTemplate;//redis操作对象

	public static void main(String[] args) {

		ApplicationContext applicationContext=SpringApplication.run(MicroDataserviceApplication.class, args);


	}

	@Override
	public void run(String... args) throws Exception {
		List<DicInfo> list=dicInfoMapper.selectListByCategory("1");
		System.out.println(list.toString());
		//获取redis中hash类型操作对象
		HashOperations<String, String, String> stringHashOperations = stringRedisTemplate.opsForHash();
		Map<String,String> ProductTypeMap=new HashMap<>();//Dic集合内容转存map
		list.forEach(dic->{
			ProductTypeMap.put(dic.getName(),dic.getVal());
		});
		//map存入redis中
		stringHashOperations.putAll("DIC:PRODUCT:TYPE",ProductTypeMap);
	}
}
