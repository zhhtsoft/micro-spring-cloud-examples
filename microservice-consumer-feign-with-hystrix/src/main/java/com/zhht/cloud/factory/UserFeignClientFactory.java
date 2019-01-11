package com.zhht.cloud.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zhht.cloud.client.UserFeignClient;
import com.zhht.cloud.entity.User;

import feign.hystrix.FallbackFactory;

/**
 * 如果需要获取feign调用失败的结果需要重写FallbackFactory create （）方法
 * 
 * @author quan
 *
 */
@Component
public class UserFeignClientFactory implements FallbackFactory<UserFeignClient> {

	private static final Logger logger =LoggerFactory.getLogger(UserFeignClientFactory.class);
	@Override
	public UserFeignClient create(Throwable arg0) {
		logger.info("异常日志："+arg0.getMessage());
		return new UserFeignClientExcpFactory();
	}

}

class UserFeignClientExcpFactory implements UserFeignClient{

	@Override
	public User getUserById(int id) {
		return null;
	}
	
}
