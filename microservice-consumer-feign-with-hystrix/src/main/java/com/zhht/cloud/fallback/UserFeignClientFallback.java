package com.zhht.cloud.fallback;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.zhht.cloud.client.UserFeignClient;
import com.zhht.cloud.entity.User;

@Component
public class UserFeignClientFallback implements UserFeignClient {

	@Override
	public User getUserById(int id) {
		return new User("jiang kai",14,new Date());
	}

}
