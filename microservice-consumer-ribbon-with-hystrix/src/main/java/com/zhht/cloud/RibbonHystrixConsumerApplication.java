package com.zhht.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

import com.zhht.cloud.config.ExludeFromComponentScan;
import com.zhht.cloud.config.RibbonRuleConfiguration;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker //依赖 spring-boot-starter-actuator 监控
@RibbonClient(name="microservice-provider",configuration=RibbonRuleConfiguration.class)
@ComponentScan(excludeFilters={@ComponentScan.Filter(type=FilterType.ANNOTATION,value=ExludeFromComponentScan.class)})
public class RibbonHystrixConsumerApplication {

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RibbonHystrixConsumerApplication.class, args);
	}
}
