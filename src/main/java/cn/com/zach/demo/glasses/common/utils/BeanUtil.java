package cn.com.zach.demo.glasses.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.BeanUtils;

/**
 * javaBean的一些工具类 
 * */
public class BeanUtil {

	/**
     * 通用的属性拷贝
     *
     * */
    public static <T,K> void copy(T target,K src){
        BeanUtils.copyProperties(src,target);
    }
    
    /**
	 * 复制集合
	 * @param target		目标集合
	 * @param initial	源集合
	 * @param initial	目标类属性
	 * @throws UCenterException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void copyCollection(Collection target, Collection initial, Class<?> tarClass) throws Exception{
		if(!StringUtil.isEmpty(initial)) {
			if(target == null){
				target = new ArrayList();
			}
			Object dist = null;
			for(Object src : initial) {
				dist = tarClass.newInstance();
				BeanUtil.copy(dist, src);
				target.add(dist);
			}
		}
	}

	// Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean  
    public static void transMap2Bean(Map<String, Object> map, Object obj) throws Exception  {  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
        for(PropertyDescriptor property : propertyDescriptors) {  
            String key = property.getName();  
            if(map.containsKey(key)) {  
                Object value = map.get(key);  
                // 得到property对应的setter方法  
                Method setter = property.getWriteMethod();  
                setter.invoke(obj, value);  
            }  
        }  
    }
    
    public static Object mapToBean(Map<String, Object> map, Class<?> beanClass) throws Exception {    
    	 	if (map == null)     
    	 		return null;   
    	 	Object obj = ClassUtil.newInstance(beanClass.getName()) ;
    	 	Field[] fields = ClassUtil.getFields(beanClass);
    	 	Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
    	 	if(!StringUtil.isEmpty(fields)) {
    	 		while(iter.hasNext()) {
        	 		Map.Entry<String, Object> entry = iter.next();
        	 		String key = entry.getKey();
        	 		key = NameCalculator.calculator(key);	//调整成驼峰命名法
        	 		
        	 		for(Field field : fields) {
        				String fieldName = field.getName();
        				if(key.equals(fieldName)) {
        					//设值
        					Object value = entry.getValue();
                	 		if(value != null) {
                	 			if(value instanceof Date || value instanceof Timestamp) {
                	 				value =((Date)value).getTime()+"L";
                	 			}
                	 			String str = value.toString();
                	 			ClassUtil.setPrimitiveValue(obj, field, str);
                	 		}
        				}
    	 			}
        	 	}
    	 	}
    	 	return obj;
    }
    
    /**  
     * 使用反射进行转换  
     */
	public static Object map2Bean(Map<String, Object> map, Class<?> beanClass) throws Exception {      
        if (map == null)     
            return null;      
        Object obj = beanClass.newInstance();    
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());      
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();      
        for (PropertyDescriptor property : propertyDescriptors) {    
        	 	String key = property.getName();  
             if(map.containsKey(key)) {  
                 Object value = map.get(key);  
                 // 得到property对应的setter方法  
                 Method setter = property.getWriteMethod();  
                 if (setter != null) {   
                	 	setter.invoke(obj, value);  
                 }
             }  
        }    
        return obj;    
    }
}
