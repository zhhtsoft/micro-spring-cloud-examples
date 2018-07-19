package com.zhht.cloud.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhht.cloud.UserFeignClient;
import com.zhht.cloud.entity.User;

@RestController
@RequestMapping("/usertest")
public class TestController {

	private static Logger logger = LoggerFactory.getLogger(TestController.class);

	private SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private UserFeignClient userFeignClient;

	@RequestMapping("/getUserById/{id}")
	public User getUserInfo(@PathVariable("id") int id) {
		logger.info("microservice-consumer-feign-日志输出到zipkin,测试时间{}", dft.format(new Date()));
		return userFeignClient.getUserById(id);

	}
	
	@RequestMapping("/getUserById2/{id}")
	public User getUserById2(@PathVariable("id") int id) {
		logger.info("microservice-consumer-feign-日志输出到zipkin,测试时间{}", dft.format(new Date()));
		return new User("ningquan",22,new Date());
//		return userFeignClient.getUserById(id);

	}
}
