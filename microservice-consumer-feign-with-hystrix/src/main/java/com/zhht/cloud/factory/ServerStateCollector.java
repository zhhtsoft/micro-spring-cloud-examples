package com.zhht.cloud.factory;

import java.util.HashMap;
import java.util.Map;

public class ServerStateCollector {

	public static final Map<String,Boolean> SERVER_CONTROL = new HashMap<String,Boolean>();
	
	public void setServerControl(String key,Boolean state) {
		SERVER_CONTROL.put(key, state);
	}
}
