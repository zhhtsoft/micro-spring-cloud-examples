package com.zhht.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Contract;

@Configuration
public class UserFeignConfiguration {

	@Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }
}
