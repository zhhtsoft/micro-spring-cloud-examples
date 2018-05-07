package com.zhht.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zhht.cloud.entity.User;

@FeignClient("microservice-provider")
public interface UserFeignClient {
	@RequestMapping(value="/user/getUserById",method=RequestMethod.GET)
	public User getUserById(@RequestParam("id") int id);

}
