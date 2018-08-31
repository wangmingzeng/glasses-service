package cn.com.zach.demo.glasses.common.property;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class PropertiesSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	
	public PropertiesSourceInitializer() {}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		LocalProperties.initInstance(applicationContext.getEnvironment());
	}
	
}