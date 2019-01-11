/**
 * 
 */
package com.zhht.cloud.factory;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author quan
 *
 */
@ConfigurationProperties(prefix="com.zhht.micro")
public class MicroServerStateProperties {
	
	public Map<String,Boolean> serverstates;

	public Map<String, Boolean> getServerstates() {
		return serverstates;
	}

	public void setServerstates(Map<String, Boolean> serverstates) {
		this.serverstates = serverstates;
	}

}
