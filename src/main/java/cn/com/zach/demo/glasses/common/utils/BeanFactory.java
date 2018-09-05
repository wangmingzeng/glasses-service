package cn.com.zach.demo.glasses.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	public BeanFactory() {
	}

	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		applicationContext = ac;
	}

	public static <T> T getBean(Class<T> beanClass) {
		return applicationContext.getBean(beanClass);
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
}