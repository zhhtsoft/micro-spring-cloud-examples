package com.zhht.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhht.cloud.client.ServiceInstanceFeignClient;
import com.zhht.cloud.client.UserFeignClient;
import com.zhht.cloud.entity.User;

@RestController
@RequestMapping("/usertest")
public class TestController {
	
	@Autowired
	private UserFeignClient userFeignClient;
	
	@Autowired
	private ServiceInstanceFeignClient serviceInstanceFeignClient;

	@RequestMapping("/getUserById/{id}")
	public User getUserInfo(@PathVariable("id") int id){
		return userFeignClient.findUserById(id);
		
	}
	
	@RequestMapping("/getServiceInstanceInfo/{serviceName}")
	public String getServiceInstanceInfo(@PathVariable("serviceName") String serviceName){
		return serviceInstanceFeignClient.getServiceApplicationByServerName(serviceName);
		
	}
	
	
}
