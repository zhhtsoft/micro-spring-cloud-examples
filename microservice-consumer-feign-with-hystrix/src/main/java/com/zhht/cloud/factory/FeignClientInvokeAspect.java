/**
 * 
 */
package com.zhht.cloud.factory;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * @author ningquan
 *类对象:com.zhht.cloud.factory.FeignClientInvokeAspect
 *处理对于每个微服务是否需要部署,如果没有部署则关闭就行，设置如下：
 *com:
 * zhht:
 *   micro:
 *     serverstates:
 *       microservice-provider: false #(或者off)
 *       microservice-provider2: true #(或者on)
 *       #如果没有设置默认是打开的，只需要把需要关闭的微服务设置即可；
 */
@Component
@ConditionalOnProperty(name = "com.zhht.micro.enabled", havingValue = "true")
@Aspect
public class FeignClientInvokeAspect {

	private static Logger log = LoggerFactory.getLogger(FeignClientInvokeAspect.class);
    /**
     * 拦截规则
     */
    private final String FeignPonitCutExpress = "execution(* com.zhht.cloud.client.*.*(..))";
    
    @Around(value=FeignPonitCutExpress)
    public Object feignAround(ProceedingJoinPoint joinPoint) {
    	long start = System.currentTimeMillis();
    	Object target = joinPoint.getTarget();
    	Signature sig = joinPoint.getSignature();
    	MethodSignature msig = null;
    	if(! (sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("run exception");
    	}
    	msig = (MethodSignature)sig;
    	try {
			Method targetMethod = target.getClass().getMethod(sig.getName(), msig.getParameterTypes());
			Class<?> declaringClass = targetMethod.getDeclaringClass();
			FeignClient feignClientAnnotation = AnnotationUtils.findAnnotation(declaringClass, FeignClient.class);
			if(feignClientAnnotation !=null) {
				String servername = feignClientAnnotation.name();
				boolean serverAvalible = checkServerAvalible(servername);
				if(!serverAvalible) {
					return null ;
				}
			}
			return joinPoint.proceed();
		} catch (Throwable e) {
			log.error("excute \r\n" + 
					"com.zhht.cloud.factory.FeignAspect.feignAround(ProceedingJoinPoint joinPoint) occur exception :{}",e);		
			throw new RuntimeException(e);
		}finally {
			long costtime = System.currentTimeMillis()-start;
			log.debug("feignclient  aspect invoke cost time={}ms",costtime);		
		}
    }
    
private boolean checkServerAvalible(String servername) {
	Boolean state = ServerStateCollector.SERVER_CONTROL.get(servername);
	return  state== null?true:state;
}

}
