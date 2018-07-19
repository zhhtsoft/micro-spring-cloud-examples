package com.zhht.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhht.entity.User;

@FeignClient("microservice-consumer-feign")
public interface UserFeignClient {
	
	@RequestMapping(value="/usertest/getUserById2/{id}")
	public User getUserById2(@PathVariable("id") int id) ;	

}
