package com.zhht.cloud;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogIpConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        return "";
	}

}
