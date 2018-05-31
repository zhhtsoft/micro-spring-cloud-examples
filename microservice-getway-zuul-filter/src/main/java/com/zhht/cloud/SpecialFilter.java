package com.zhht.cloud;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 白名单过滤器
 * @author quan
 *
 */

@Component
public class SpecialFilter extends ZuulFilter {

	private static final Logger logger = LoggerFactory.getLogger(SpecialFilter.class);
	
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		Object success = ctx.get("success");
		logger.debug("success={}",success);
		return (boolean)success;
	}

	@Override
	public Object run() {
		logger.debug("开始执行：SpecialFilter");
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String specialHost = request.getParameter("host");
		if(specialHost != null && specialHost.equals("nqpc")) {
			ctx.setSendZuulResponse(true);
			ctx.setResponseStatusCode(HttpStatus.OK.value());
			//添加requestheader
			ctx.addZuulRequestHeader("Request-Authorization", specialHost);
			ctx.set("success",true);
		}else {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
			ctx.getResponse().setContentType("application/json; charset=UTF-8");
			ctx.setResponseBody("{\"result\":\"当前host不在名名单中！\"}");
			ctx.set("success",false);
			logger.debug("当前host为空，不允许访问");
		}
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 2;
	}

}
