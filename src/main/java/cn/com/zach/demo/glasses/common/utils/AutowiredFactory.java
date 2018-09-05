package cn.com.zach.demo.glasses.common.utils;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;

public class AutowiredFactory{
	
	/**
	 * 根据类实例化，将注解了@Autowired的bean注入
	 * @param clazz 类对象
	 * @return
	 */
	public static <T extends Object> T newInstance(Class<T> clazz) {
		T object = null;
		try {
			object = clazz.newInstance();
			Field [] fields = clazz.getDeclaredFields();
			for(Field f : fields  ) {
				Autowired a = f.getAnnotation(Autowired.class);
				if(a != null) {
					autowired(object,f );
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return object ;
	}
	
	/**
	 * 根据类实例化，将注解了@Autowired的bean注入
	 * @param className	类全路径
	 * @return
	 */
	public static <T extends Object> T newInstance(String className) {
		T object = null;
		try {
			object = ClassUtil.newInstance(className);
			Class<?> filterClazz = object.getClass();
			Field [] fields = filterClazz.getDeclaredFields();
			for(Field f : fields) {
				Autowired a = f.getAnnotation(Autowired.class);
				if(a != null) {
					autowired(object, f);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return object ;
	}
	
	/**
	 * 注入对象中引用的bean
	 * @param obj
	 * @param field
	 */
	private static void autowired(Object object, Field field) {
		String name = field.getName();
		Object value = BeanFactory.getBean(name);
		ClassUtil.setValue(object, field, value);
	}
	
}
