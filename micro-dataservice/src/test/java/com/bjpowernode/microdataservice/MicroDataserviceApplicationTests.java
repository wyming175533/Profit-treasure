package com.bjpowernode.microdataservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class MicroDataserviceApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	public void sout(){
		System.out.println(new Date().getTime());
	}

}
