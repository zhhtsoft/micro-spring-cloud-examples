package com.zhht.cloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.zhht.cloud.factory.FooConfiguration;

@FeignClient(name="microservice-provider2",configuration=FooConfiguration.class)
public interface UserFeignClient2 {

}
