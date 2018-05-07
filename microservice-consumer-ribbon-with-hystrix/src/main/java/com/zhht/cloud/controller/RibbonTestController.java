package com.zhht.cloud.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zhht.cloud.entity.User;

@RestController
@RequestMapping("/ribbontest")
public class RibbonTestController {
	
	private static Logger logger =LoggerFactory.getLogger(RibbonTestController.class);

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@Autowired
	public RestTemplate RestTemplate;
	@RequestMapping("/getUserById/{id}")
	@HystrixCommand(fallbackMethod="findUserByIdFallback")
	public User getUserInfo(@PathVariable("id") int id){
		return RestTemplate.getForObject("http://microservice-provider/user/getUserById?id="+id, User.class
				);
		
	}
	
	public User findUserByIdFallback(int id) {
		return new User("zhangguolun", 20, new Date());
	}
	
	@GetMapping("/getServiceInstance")
	public String getServiceInstance(){
		ServiceInstance serviceInstance = loadBalancerClient.choose("microservice-provider");
		String serviceIntanceInfo = "服务提供者一："+serviceInstance.getServiceId()+":"+serviceInstance.getHost()+":"+serviceInstance.getPort();
		logger.info(serviceIntanceInfo);
		
		ServiceInstance serviceInstance2 = loadBalancerClient.choose("microservice-provider2");
		String serviceIntanceInfo2 = "服务提供者二："+serviceInstance2.getServiceId()+":"+serviceInstance2.getHost()+":"+serviceInstance2.getPort();
		logger.info(serviceIntanceInfo2);
		return serviceIntanceInfo+serviceIntanceInfo2;
		
	}
}
