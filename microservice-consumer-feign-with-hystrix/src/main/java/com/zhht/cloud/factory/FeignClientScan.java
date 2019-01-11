package com.zhht.cloud.factory;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author ningquan
 * Class：com.zhht.cloud.factory.FeignClientScan
 * 处理feignClient包下的类对象，对其使用的microservice 开关配置进行collect；
 */

@Configuration
@EnableConfigurationProperties(MicroServerStateProperties.class)
@ConditionalOnProperty(name = "com.zhht.micro.enabled", havingValue = "true")
public class FeignClientScan implements InitializingBean{
	private static Logger log = LoggerFactory.getLogger(FeignClientScan.class);
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private MicroServerStateProperties serverStateProperties;
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> beansWithAnnotations = applicationContext.getBeansWithAnnotation(FeignClient.class);
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();//ClassLoader.getSystemClassLoader();;
		Iterator<String> iterator = beansWithAnnotations.keySet().iterator();
		for(;iterator.hasNext();)
		{
			String next = iterator.next();
			if(!next.startsWith("com.zhht")) {
				continue;
			}
			log.info("initial class={}",next);
			Class<?> loadClass = contextClassLoader.loadClass(next);
//			Object object = beansWithAnnotations.get(next);
//			Class<?> targetClass = AopUtils.getTargetClass(object);
//			log.info(targetClass.getName());
//			Class<?>[] declaredClasses = object.getClass().getDeclaredClasses();
//			AnnotationUtils.findAnnotation(declaredClasses, FeignClient.class);
//			Class<?> targetClass = AopUtils.getTargetClass(object);
//			Object target = AopTargetUtils.getTarget(object);
			FeignClient annotation = loadClass.getAnnotation(FeignClient.class);
			
//			Reflections reflections = new Reflections();
//			Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(FeignClient.class);
//			for(Class<?> clazz : typesAnnotatedWith) {
//				System.out.println(clazz.getName());
//			}
			if(annotation == null) {
				continue;
			}
			String microName = annotation.name();
			if(null == microName) {
				continue;
			}
			initServerControl(microName);
		}
	}

	private void initServerControl(String microName) {
		Map<String, Boolean> serverstates = serverStateProperties.getServerstates();
		Boolean state = serverstates.get(microName);
		if(state!=null && !state) {
			ServerStateCollector.SERVER_CONTROL.put(microName, false);
			return;
		}
		ServerStateCollector.SERVER_CONTROL.put(microName, true);
	}
	

}
