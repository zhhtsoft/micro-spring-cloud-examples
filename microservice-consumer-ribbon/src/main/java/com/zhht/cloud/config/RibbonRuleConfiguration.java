package com.zhht.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;

@Configuration
@ExludeFromComponentScan
public class RibbonRuleConfiguration {
	
	@Bean
	public IRule ribbonRule(){
		return new RandomRule();
	}

}
