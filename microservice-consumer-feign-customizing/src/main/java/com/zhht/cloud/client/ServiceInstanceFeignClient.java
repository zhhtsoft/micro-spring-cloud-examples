package com.zhht.cloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhht.config.ServerConfiguration;

/**
 * 获取服务配置，通过url访问，
 * url访问必须需要name属性
 * 如果eureka设置了安全访问，需要配置configuration参数,否则无权限访问401错误
 * 
 * config需要指定到扫描包的范围之外的包中声明
 * 
 * @author quan
 *
 */
@FeignClient(name="eurekaServiceXXX",url="http://localhost:7000",configuration=ServerConfiguration.class)
public interface ServiceInstanceFeignClient {

	@RequestMapping(value="/eureka/apps/{serviceName}")
	public String getServiceApplicationByServerName(@PathVariable("serviceName") String serviceName);
}
