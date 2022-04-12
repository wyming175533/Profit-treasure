package com.bjpowernode.microweb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MicroWebApplicationTests {

	@Test
	public void testSmsInfo(){
		String s="你的验证码位%s";
		s=String.format(s,"123456");
		System.out.println(s);
	}

}
