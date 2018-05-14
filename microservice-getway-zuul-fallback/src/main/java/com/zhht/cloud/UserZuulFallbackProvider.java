/**
 * 
 */
package com.zhht.cloud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.exception.HystrixTimeoutException;

/**
 * @author quan
 *zuul fallback实现
 *
 */

@Component
public class UserZuulFallbackProvider implements FallbackProvider/*ZuulFallbackProvider*/ {

	private static final Logger logger = LoggerFactory.getLogger(UserZuulFallbackProvider.class);
	@Override
	public String getRoute() {
		return "microservice-provider-v1";
//		return "*"; //如果针对所有微服务回退，可以支持 *
	}

	/*@Override
	public ClientHttpResponse fallbackResponse() {
	}*/

	@Override
	public ClientHttpResponse fallbackResponse(Throwable cause) {
		logger.debug("回退异常 exception={}",cause.getMessage());
		if(cause instanceof HystrixTimeoutException) {
			return response(HttpStatus.GATEWAY_TIMEOUT);
		}else {
			return fallbackResponse();
		}
		
	}

	@Override
	public ClientHttpResponse fallbackResponse() {
				return this.response(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ClientHttpResponse response(final HttpStatus httpStatus) {
		return new ClientHttpResponse() {
			
			@Override
			public InputStream getBody() throws IOException {
				ByteArrayInputStream byteArrayInputStream = 
						new ByteArrayInputStream("{\"fallback\":\"content is null\",\"code\":\"400\"}".getBytes()) ;
				return byteArrayInputStream;
			}
			
			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return headers;
			}
			
			@Override
			public HttpStatus getStatusCode() throws IOException {
				// TODO Auto-generated method stub
				return HttpStatus.BAD_REQUEST;
			}
			
			@Override
			public int getRawStatusCode() throws IOException {
				// TODO Auto-generated method stub
				return HttpStatus.BAD_REQUEST.value();
			}
			
			@Override
			public String getStatusText() throws IOException {
				// TODO Auto-generated method stub
				return HttpStatus.BAD_REQUEST.getReasonPhrase();
			}
			
			@Override
			public void close() {
				// TODO Auto-generated method stub
				
			}
			
		};
		
	}
}
