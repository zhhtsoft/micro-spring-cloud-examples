package com.zhht.cloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zhht.cloud.entity.User;
import com.zhht.cloud.factory.UserFeignClientFactory;
import com.zhht.cloud.fallback.UserFeignClientFallback;

/**
 * feign 对hystrix的支持
 * 回退时候调用UserFeignClientFallback对应的方法
 * 注意：UserFeignClientFallback需要注入到bean中
 * 属性 fallback,fallbackFactory只能一个有效，fallbackfactory应该是fallback的增强版
 * @author quan
 *
 */
@FeignClient(name="microservice-provider", /*fallback=UserFeignClientFallback.class,*/fallbackFactory=UserFeignClientFactory.class)
public interface UserFeignClient {
	@RequestMapping(value="/user/getUserById",method=RequestMethod.GET)
	public User getUserById(@RequestParam("id") int id);

}
