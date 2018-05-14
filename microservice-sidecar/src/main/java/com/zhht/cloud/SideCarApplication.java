package com.zhht.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.sidecar.EnableSidecar;

@SpringBootApplication
@EnableSidecar  //sidecar启用
public class SideCarApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(SideCarApplication.class, args);
	}
}
