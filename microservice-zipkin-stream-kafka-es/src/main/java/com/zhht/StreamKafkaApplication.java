package com.zhht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableZipkinServer
@zipkin.server.internal.EnableZipkinServer
@EnableEurekaClient
public class StreamKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamKafkaApplication.class, args);
	}
	
/*	
	@Bean
    public MySQLStorage mySQLStorage(DataSource datasource) {
        return MySQLStorage.builder().datasource(datasource).executor(Runnable::run).build();
    }*/
}
