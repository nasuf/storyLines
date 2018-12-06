package com.story;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class ApplicationTests {

	@Test
	public void contextLoads() {
		StringEncryptor ss = new StandardPBEStringEncryptor();
		((StandardPBEStringEncryptor) ss).setPassword("nasuf713!");
		String encrypt = ss.encrypt("Welcome2citi!");
		System.out.println(encrypt);
	}

}
