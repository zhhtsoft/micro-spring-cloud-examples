package com.zhht.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhht.cloud.entity.User;

@FeignClient("microservice-consumer-feign")
public interface UserFeignClient {
	
	@RequestMapping(value="/usertest/getUserById/{id}")
	public User getUserById(@PathVariable("id") int id) ;	

}
