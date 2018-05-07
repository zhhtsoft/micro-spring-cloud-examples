package com.zhht.cloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.zhht.cloud.entity.User;
import com.zhht.config.UserFeignConfiguration;

import feign.Param;
import feign.RequestLine;

@FeignClient(name="microservice-provider",configuration=UserFeignConfiguration.class)
public interface UserFeignClient {
	/*@RequestMapping(value="/user/getUserById",method=RequestMethod.GET)
	public User getUserById(@RequestParam("id") int id);

	*/
	@RequestLine("GET /user/getUserById")
	public User findUserById(@Param("id") int id);
}
