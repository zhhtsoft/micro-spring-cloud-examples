package com.zhht.cloud;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 登录信息认证filter
 * @author quan
 *
 */

@Component
public class LoginFilter extends ZuulFilter {

	private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);
	@Override
	public Object run() {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String sessionId = request.getParameter("session-id");
		if(sessionId == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
//			ctx.setResponseBody("{\"result\":\"no access!\"}");
			logger.debug("当前用户登录信息为空！");
			ctx.set("success", false);
			writeResponse();
		}else {
			ctx.set("success", true);
		}
		return null;
	}

	private void writeResponse() {
		HttpServletResponse response = RequestContext.getCurrentContext().getResponse();
		try {
			logger.debug("当前zuul filter code="+response.getCharacterEncoding());
			logger.debug("当前zuul filter contenttype="+response.getContentType());
			
			//通过zuul出现中文乱码问题，需要设置content-type
			response.setContentType("application/json; charset=UTF-8");
			OutputStream outStream = response.getOutputStream();
			outStream.write("当前用户未登录，请 先登录".getBytes());
			outStream.flush();
			//TODO 验证失败需要return 
		} catch (IOException ex) {
			ReflectionUtils.rethrowRuntimeException(ex);
//			ex.printStackTrace();
		}
	}
	/**
	 * 设置是否使用filter
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	/**
	 * pre
	 * post
	 * error
	 * static
	 * route
	 */
	@Override
	public String filterType() {
		return "pre";
	}

}
