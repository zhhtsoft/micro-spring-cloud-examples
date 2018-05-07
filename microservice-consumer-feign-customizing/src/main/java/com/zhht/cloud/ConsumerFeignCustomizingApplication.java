package com.zhht.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
//添加注解@EnableFeignClients

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerFeignCustomizingApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ConsumerFeignCustomizingApplication.class, args);
	}
}
