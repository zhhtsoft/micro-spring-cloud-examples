package com.zhht.cloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private EurekaClient eurekaClient;

	@RequestMapping("/getServiceUrl")
	public String serviceUrl() {
	    InstanceInfo instance = eurekaClient.getNextServerFromEureka("microservice-provider", false);
	    return instance.getHomePageUrl();
	}
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@RequestMapping("/getServiceUrl2")
	public String serviceUrl2() {
	    List<ServiceInstance> list = discoveryClient.getInstances("microservice-provider");
	    if (list != null && list.size() > 0 ) {
	        return list.get(0).getUri().toString();
	    }
	    return null;
	}
}
