package com.zhht.cloud.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zhht.cloud.client.UserFeignClient;
import com.zhht.cloud.entity.User;

@RestController
@RequestMapping("/usertest")
public class TestController {
	
	@Autowired
	private UserFeignClient userFeignClient;

	@RequestMapping(value="/getUserById/{id}")
	
//	@HystrixCommand(fallbackMethod="fallbackUser")
	public User getUserInfo(@PathVariable("id") int id){
		/*return RestTemplate.getForObject("http://microservice-provider/user/getUserById?id="+id, User.class
				);*/
		User user = userFeignClient.getUserById(id);
		return user==null?new User():user;
		
	}
	
	@HystrixCommand(fallbackMethod="fallbackUser1")
	public User fallbackUser(int id) {
		return new User("zhangsan",23,new Date());
	}
	
	@HystrixCommand(fallbackMethod="fallbackUser1")
	public User fallbackUser1(int id) {
		return new User("zhangsan",23,new Date());
	}
		
}
