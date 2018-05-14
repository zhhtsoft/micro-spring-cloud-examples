package com.zhht.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ZuulFilterApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ZuulFilterApplication.class, args);
	}
}
