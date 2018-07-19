package com.zhht;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import zipkin.storage.mysql.MySQLStorage;

@SpringBootApplication
//@EnableZipkinServer
@zipkin.server.internal.EnableZipkinServer
@EnableEurekaClient
public class StreamKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamKafkaApplication.class, args);
	}
	
	@Bean
    public MySQLStorage mySQLStorage(DataSource datasource) {
        return MySQLStorage.builder().datasource(datasource).executor(Runnable::run).build();
    }
}
