package com.zhht.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zhht.cloud.entity.User;

@RestController
@RequestMapping("/ribbontest")
public class RibbonTestController {
	
	


	@Autowired
	public RestTemplate RestTemplate;
	@RequestMapping("/getUserById/{id}")
	public User getUserInfo(@PathVariable("id") int id){
		return RestTemplate.getForObject("http://microservice-provider/user/getUserById?id="+id, User.class
				);
		
	}
}
