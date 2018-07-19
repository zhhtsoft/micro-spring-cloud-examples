package com.zhht.cloud.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhht.cloud.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private static Logger log = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value="/getUserById",method=RequestMethod.GET)
	public User getUserById(@RequestParam(name="id") int id,HttpServletRequest request){
		
		//Request-Authorization
		String authHeader = request.getHeader("Request-Authorization");
		log.info("微服务：microservice-provider-user:zuul pass header={}",authHeader);
		log.info("微服务：microservice-provider-user:UserController.getUserById");
		if(id == 1){
			return new User("张三",10,new Date());
		}
			return new User("王五",10,new Date());
	}

}
