package com.zhht.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhht.entity.User;


@RestController
@RequestMapping("/sleuth")
public class TestController {


	private static Logger logger = LoggerFactory.getLogger(TestController.class);
	
	private SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private UserFeignClient userfeignClient;
	
	@RequestMapping(value="/getUserById/{id}")
	public User getUserById(@PathVariable("id") int id) {
		logger.info("microservice-sleuth-kafka:测试getUserById输出到zipkin,测试时间{}", dft.format(new Date()));
		User user = userfeignClient.getUserById2(id);
		return user;
	}
	
	@RequestMapping(value="/print")
	public String getSleuthTrace() {
		logger.info("microservice-sleuth-kafka:测试getSleuthTrace输出到zipkin,测试时间{}", dft.format(new Date()));
		return "test ok";
	}
}
