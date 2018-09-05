package cn.com.zach.demo.glasses.common.utils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;

/**
 * 反射工具类
 */
public class ClassUtil {

	/**
	 * 把基类类型字段装入javabean对象中,字符串值自动转换为对象
	 * 
	 * @param obj		javabean对象
	 * @param fieldName 	字段名称
	 * @param value 		字符串类型值
	 */
	public final static void setPrimitiveValue(Object obj, String fieldName, String value) {
		Field field = getField0(obj.getClass(), fieldName, true);
		if (field != null) {
			setPrimitiveValue(obj, field, value);
		}
	}

	/**
	 * 把基类类型字段装入javabean对象中,字符串值自动转换为对象
	 * 
	 * @param obj 	javabean对象
	 * @param field	字段
	 * @param value	字符串类型值
	 */
	public final static void setPrimitiveValue(Object obj, Field field, String value) {
		Object prim = primitiveToObject(field.getType(), value);
		if (prim != null) {
			Method method = getSetter(obj.getClass(), field);
			if (method != null) {
				invoke(obj, method, prim);
			}
		}
	}

	/**
	 * 把字符串转换成常见基本类型: long int boolean float double short char Date Enum BigDecimal
	 * 
	 * @param type	类
	 * @param value	字符串类型值
	 * @return
	 */
	public final static Object primitiveToObject(Class<?> type, String value) {
		if (value == null || value.length() == 0)
			return null;
		if (String.class == type) {
			return value;
		} else if (Long.class == type || long.class == type) {
			return Long.parseLong(value);
		} else if (Integer.class == type || int.class == type) {
			return Integer.parseInt(value);
		} else if (Boolean.class == type || boolean.class == type) {
			return "true".equalsIgnoreCase(value) || "t".equalsIgnoreCase(value) || "1".equals(value)
					|| "yes".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
		} else if (Float.class == type || float.class == type) {
			return Float.parseFloat(value);
		} else if (Short.class == type || short.class == type) {
			return Short.parseShort(value);
		} else if (Character.class == type || char.class == type) {
			return value.charAt(0);
		} else if (Double.class == type || double.class == type) {
			return Double.parseDouble(value);
		} else if (Date.class == type) {
			return TimeUtil.parse(value);
		} else if (type.isEnum()) {
			return getEnum(type, value);
		} else if (BigDecimal.class == type) {
			return new BigDecimal(value);
		} else {
			throw new IllegalArgumentException("无法把" + value + "转换到" + type + "对象");
		}
	}

	/**
	 * 尝试把字符串转换为基本类型
	 * 
	 * @param string		类
	 * @return
	 */
	public final static Object stringToPrimitiveValue(String string) {
		Object answer = null;
		if (string != null) {
			try {
				if (string.length() > 0) {
					int length = string.length(), end = length - 1;
					char first = string.charAt(0), last = string.charAt(end);
					if ((first >= '0' && first <= '9') || first == '-') {
						switch (last) {
						case 'l': // long l = 0l;
						case 'L': // long l = 0L;
							Long l = new Long(string.substring(0, end));
							if (l.toString().regionMatches(false, 0, string, 0, end)) {
								answer = l;
							}
							break;
						case 'd': // double l = 0.0d;
						case 'D': // double l = 0.0D;
							Double d = new Double(string.substring(0, end));
							if (d.toString().regionMatches(false, 0, string, 0, end)) {
								answer = d;
							}
							break;
						case 'f': // float l = 0.0f;
						case 'F': // float l = 0.0F;
							Float f = new Float(string.substring(0, end));
							if (f.toString().regionMatches(false, 0, string, 0, end)) {
								answer = f;
							}
							break;
						// 扩展short和int简写类型
						case 's': // short l = 0s; //奇怪long double float在java中都有对应的简写,唯独short类型没有,在这里自己定义实现
						case 'S': // Short l = 0S; //奇怪long double float在java中都有对应的简写,唯独short类型没有,在这里自己定义实现
							Short s = new Short(string.substring(0, end));
							if (s.toString().regionMatches(false, 0, string, 0, end)) {
								answer = s;
							}
							break;
						case 'i': // int l = 0i; //定义int类型简写形式, java不支持这种写法
						case 'I': // int l = 0I; //定义int类型简写形式, java不支持这种写法
							Integer i = new Integer(string.substring(0, end));
							if (i.toString().regionMatches(false, 0, string, 0, end)) {
								answer = i;
							}
							break;
						default:
							if (last >= 48 && last <= 57) {// ASCII表中数字范围
								if (string.indexOf('.') > -1 || string.indexOf('e') > -1 || string.indexOf('E') > -1) {
									Double du = Double.valueOf(string);
									if (!du.isInfinite() && !du.isNaN()) {
										answer = du;
									}
								} else {
									Long myLong = new Long(string);
									if (myLong.toString().regionMatches(false, 0, string, 0, length)) {
										if (myLong == myLong.intValue()) {
											answer = myLong.intValue();
										} else {
											answer = myLong;
										}
									}
								}
							} else {
								answer = string;
							}
						}
					} else {
						if (length == 4 && (first == 'n' || first == 'N') && (last == 'l' || last == 'L')
								&& string.equalsIgnoreCase("null")) {
							answer = null;
						} else if (length == 4 && (first == 't' || first == 'T') && (last == 'e' || last == 'E')
								&& string.equalsIgnoreCase("true")) {
							answer = true;
						} else if (length == 5 && (first == 'f' || first == 'F') && (last == 'e' || last == 'E')
								&& string.equalsIgnoreCase("false")) {
							answer = false;
						} else {
							answer = string;
						}
					}
				} else {
					answer = string;
				}
			} catch (Exception e) {
				// 解析失败
				answer = string;
			}
		}
		return answer;
	}

	/**
	 * 获取8中基本类型默认值
	 * 
	 * @param type	基本类型
	 * @return
	 */
	public final static Object getPrimitiveDefautValue(Class<?> type) {
		Object answer = null;
		if (type == int.class) {
			answer = 0;
		} else if (type == long.class) {
			answer = 0L;
		} else if (type == double.class) {
			answer = 0.0D;
		} else if (type == float.class) {
			answer = 0.0F;
		} else if (type == short.class) {
			answer = (short) 0;
		} else if (type == boolean.class) {
			answer = false;
		} else if (type == char.class) {
			answer = 0;
		} else if (type == byte.class) {
			answer = (byte) 0;
		}
		return answer;
	}

	/**
	 * 获取枚举对象
	 * 
	 * @param enumClass	枚举类
	 * @param keys		多个key数组
	 * @return 返回枚举对象
	 */
	public final static <T> T getEnum(Class<T> enumClass, String value) {
		T answer = null;
		if (enumClass.isEnum()) {
			Method method = getMethod0(enumClass, "getInstance", true, String.class);
			if (method == null) {
				for (T en : enumClass.getEnumConstants()) {
					if (en.toString().equalsIgnoreCase(value)) {
						answer = en;
						break;
					}
				}
			} else {
				answer = invoke(method, value);
			}
		}
		return answer;
	}

	/**
	 * 获取静态属性设置值
	 * 
	 * @param field
	 */
	public final static void getValue(Field field) {
		getValue(null, field);
	}

	/**
	 * 获取属性设置值
	 * 
	 * @param obj		对象
	 * @param fieldName	字段名称
	 */
	public final static Object getValue(Object obj, Field field) {
		try {
			boolean b = field.isAccessible();
			if (!b) {
				field.setAccessible(true);
			}
			return field.get(obj);
		} catch (Exception e) {
			throw new IllegalArgumentException("无法获取属性赋值 " + field, e);
		}
	}

	/**
	 * 获取属性设置值
	 * 
	 * @param obj		对象
	 * @param fieldName	字段名称
	 */
	public final static Object getValue(Object obj, String fieldName) {
		return getValue(obj.getClass(), obj, fieldName);
	}

	/**
	 * 获取静态属性设置值
	 * 
	 * @param clazz		class类
	 * @param fieldName	字段名称
	 */
	public final static void getValue(Class<?> clazz, String fieldName) {
		getValue(clazz, null, fieldName);
	}

	/**
	 * 获取属性设置值
	 * 
	 * @param clazz		class类
	 * @param obj		对象
	 * @param fieldName	字段名称
	 */
	public final static Object getValue(Class<?> clazz, Object obj, String fieldName) {
		Field field = getField0(clazz, fieldName, true);
		if (field != null) {
			return getValue(obj, field);
		}
		return null;
	}

	/**
	 * 给静态属性设置值
	 * 
	 * @param field
	 * @param value
	 */
	public final static void setValue(Field field, Object value) {
		setValue(null, field, value);
	}

	/**
	 * 给属性设置值
	 * 
	 * @param obj		对象
	 * @param fieldName	字段名称
	 * @param value		值
	 */
	public final static void setValue(Object obj, Field field, Object value) {
		try {
			boolean b = field.isAccessible();
			if (!b) {
				field.setAccessible(true);
			}
			field.set(obj, value);
		} catch (Exception e) {
			throw new IllegalArgumentException("无法给属性赋值 " + field, e);
		}
	}

	/**
	 * 给属性设置值
	 * 
	 * @param obj
	 *            对象
	 * @param fieldName
	 *            字段名称
	 * @param value
	 *            值
	 */
	public final static void setValue(Object obj, String fieldName, Object value) {
		setValue(obj.getClass(), obj, fieldName, value);
	}

	/**
	 * 给静态属性设置值
	 * 
	 * @param clazz		class类
	 * @param fieldName	字段名称
	 * @param value		值
	 */
	public final static void setValue(Class<?> clazz, String fieldName, Object value) {
		setValue(clazz, null, fieldName, value);
	}

	/**
	 * 给属性设置值
	 * 
	 * @param clazz		class类
	 * @param obj		对象
	 * @param fieldName	字段名称
	 * @param value		值
	 */
	public final static void setValue(Class<?> clazz, Object obj, String fieldName, Object value) {
		Field field = getField0(clazz, fieldName, true);
		if (field != null) {
			setValue(obj, field, value);
		}
	}

	/**
	 * 执行静态方法,无参数
	 * 
	 * @param method		静态方法
	 * @return
	 */
	public final static <T> T invoke(Method method) {
		return invoke(null, method, new Object[0]);
	}

	/**
	 * 执行对象方法,无参数
	 * 
	 * @param obj		对象
	 * @param method		方法
	 * @return
	 */
	public final static <T> T invoke(Object obj, Method method) {
		return invoke(obj, method, new Object[0]);
	}

	/**
	 * 执行对象方法,有参数
	 * 
	 * @param obj		对象
	 * @param method		方法
	 * @param parameter	参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T invoke(Object obj, Method method, Object... parameter) {
		T answer = null;
		try {
			boolean b = method.isAccessible();
			if (!b) {
				method.setAccessible(true);
			}
			answer = (T) method.invoke(obj, parameter);
		} catch (Exception e) {
			throw new IllegalArgumentException("无法执行指定方法 " + method, e);
		}
		return answer;
	}

	/**
	 * 执行对象方法,无参数
	 * 
	 * @param obj	对象
	 * @param method 方法名称
	 * @return
	 */
	public final static <T> T invoke(Object obj, String method) {
		return invoke(obj.getClass(), obj, method, new Object[0]);
	}

	/**
	 * 执行对象方法,有参数
	 * 
	 * @param obj		对象
	 * @param method		方法名称
	 * @param parameter	方法需要参数
	 * @return
	 */
	public final static <T> T invoke(Object obj, String method, Object... parameter) {
		return invoke(obj.getClass(), obj, method, parameter);
	}

	/**
	 * 执行静态方法,无参数
	 * 
	 * @param clazz		类对象
	 * @param method		静态方法名称
	 * @return
	 */
	public final static <T> T invoke(Class<?> clazz, String method) {
		return invoke(clazz, null, method, new Object[0]);
	}

	/**
	 * 执行静态方法,有参数
	 * 
	 * @param clazz		类对象
	 * @param method		静态方法名称
	 * @param parameter	方法需要参数
	 * @return
	 */
	public final static <T> T invoke(Class<?> clazz, String method, Object... parameter) {
		return invoke(clazz, null, method, parameter);
	}

	/**
	 * 执行方法
	 * 
	 * @param clazz		类对象
	 * @param obj		对象
	 * @param method		方法名称
	 * @param parameter	方法需要参数
	 * @return
	 */
	public final static <T> T invoke(Class<?> clazz, Object obj, String method, Object... parameter) {
		T answer = null;
		Class<?>[] types = null;
		if (parameter != null) {
			types = new Class<?>[parameter.length];
			for (int i = 0; i < parameter.length; i++) {
				types[i] = parameter[i].getClass();
			}
		} else {
			types = new Class<?>[0];
		}
		Method m = getMethod0(clazz, method, true, types);
		if (m != null) {
			answer = invoke(obj, m, parameter);
		}
		return answer;
	}

	/**
	 * 获取javabean对象的getter方法,
	 * 
	 * @param obj	javabean对象
	 * @param field	字段
	 * @return
	 */
	public final static Method getGetter(Object obj, Field field) {
		Class<?> c = field.getType();
		return getGetter(obj.getClass(), field.getName(), (c == boolean.class || c == Boolean.class));
	}

	/**
	 * 获取javabean对象的getter方法
	 * 
	 * @param obj		javabean对象
	 * @param fieldName	字段名称
	 * @return
	 */
	public final static Method getGetter(Object obj, String fieldName) {
		return getGetter(obj.getClass(), fieldName, false);
	}

	/**
	 * 获取javabean对象的getter方法,
	 * 
	 * @param clazz	javabean对象
	 * @param field	字段
	 * @return
	 */
	public final static Method getGetter(Class<?> clazz, Field field) {
		Class<?> c = field.getType();
		return getGetter(clazz, field.getName(), (c == boolean.class || c == Boolean.class));
	}

	/**
	 * 获取javabean对象的getter方法
	 * 
	 * @param clazz		javabean对象
	 * @param fieldName	字段名称
	 * @return
	 */
	public final static Method getGetter(Class<?> clazz, String fieldName) {
		return getGetter(clazz, fieldName, false);
	}

	/**
	 * 获取javabean对象的getter方法
	 * 
	 * @param clazz	javabean对象
	 * @param fieldName
	 * @param is
	 * @return
	 */
	private final static Method getGetter(Class<?> clazz, String fieldName, boolean is) {
		Method method = null;
		char c = fieldName.charAt(0);
		if (c >= 97 && c <= 122) { // a=97 z=122
			char chars[] = fieldName.toCharArray();
			chars[0] = (char) (c - 32);// ASCII中大写字母和小写字母相差32
			fieldName = new String(chars);
		}
		if (is) {
			method = getMethod0(clazz, "is" + fieldName, false);
		} else {
			method = getMethod0(clazz, "get" + fieldName, false);
			if (method == null) {
				method = getMethod0(clazz, "is" + fieldName, false);
			}
		}
		return method;
	}

	/**
	 * 获取javabean对象的setter方法
	 * 
	 * @param obj	javabean对象
	 * @param field	字段
	 * @return
	 */
	public final static Method getSetter(Object obj, Field field) {
		return getSetter(obj.getClass(), field.getName(), field.getType());
	}

	/**
	 * 获取javabean对象的setter方法
	 * 
	 * @param obj		javabean对象
	 * @param fieldName	字段名称
	 * @return
	 */
	public final static Method getSetter(Object obj, String fieldName) {
		Method answer = null;
		Field field = getField(obj, fieldName);
		if (field != null) {
			answer = getSetter(obj.getClass(), field.getName(), field.getType());
		}
		return answer;
	}

	/**
	 * 获取javabean对象的setter方法
	 * 
	 * @param clazz	javabean类对象
	 * @param field	字段
	 * @return
	 */
	public final static Method getSetter(Class<?> clazz, Field field) {
		return getSetter(clazz, field.getName(), field.getType());
	}

	/**
	 * 获取javabean对象的setter方法
	 * 
	 * @param clazz		javabean类对象
	 * @param fieldName	字段名称
	 * @return
	 */
	public final static Method getSetter(Class<?> clazz, String fieldName) {
		Method answer = null;
		Field field = getField(clazz, fieldName);
		if (field != null) {
			answer = getSetter(clazz, field.getName(), field.getType());
		}
		return answer;
	}

	/**
	 * 获取javabean对象的setter方法,
	 * 
	 * @param clazz		对象
	 * @param fieldName	字段名称
	 * @param parameterType	字段类型
	 * @return
	 */
	public final static Method getSetter(Class<?> clazz, String fieldName, Class<?> parameterType) {
		char c = fieldName.charAt(0);
		if (c >= 97 && c <= 122) { // a=97 z=122
			char chars[] = fieldName.toCharArray();
			chars[0] = (char) (c - 32);// ASCII中大写字母和小写字母相差32
			fieldName = new String(chars);
		}
		return getMethod0(clazz, "set" + fieldName, false, parameterType);
	}

	/**
	 * 从对象的的继承链中查询public修饰method方法,也可以查询静态方法
	 * 
	 * @param obj	对象
	 * @param name	方法名称
	 * @return 查询到的方法对象
	 */
	public final static Method getPublicMethod(Object obj, String name) {
		return getMethod0(obj.getClass(), name, false);
	}

	/**
	 * 从对象的的继承链中查询public修饰method方法,也可以查询静态方法
	 * 
	 * @param obj	对象
	 * @param name	方法名称
	 * @param parameterTypes	方法参数类数组
	 * @return 查询到的方法对象
	 */
	public final static Method getPublicMethod(Object obj, String name, Class<?>... parameterTypes) {
		return getMethod0(obj.getClass(), name, false, parameterTypes);
	}

	/**
	 * 从对象的的继承链中查询public修饰method方法,也可以查询静态方法
	 * 
	 * @param clazz	类
	 * @param name	方法名称
	 * @return 查询到的方法对象
	 */
	public final static Method getPublicMethod(Class<?> clazz, String name) {
		return getMethod0(clazz, name, false);
	}

	/**
	 * 从对象的的继承链中查询public修饰method方法,也可以查询静态方法
	 * 
	 * @param clazz	类
	 * @param name	方法名称
	 * @param parameterTypes		方法参数类数组
	 * @return 查询到的方法对象
	 */
	public final static Method getPublicMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		return getMethod0(clazz, name, false, parameterTypes);
	}

	/**
	 * 从对象的的继承链中查询public protected private和默认修饰method方法,也可以查询静态方法.
	 * 
	 * @param obj	对象
	 * @param name	方法名称
	 * @return 查询到的方法对象
	 */
	public final static Method getMethod(Object obj, String name) {
		return getMethod0(obj.getClass(), name, true);
	}

	/**
	 * 从对象的的继承链中查询public protected private和默认修饰method方法,也可以查询静态方法.
	 * 
	 * @param obj	对象
	 * @param name	方法名称
	 * @param parameterTypes	方法参数class数组
	 * @return 查询到的方法对象
	 */
	public final static Method getMethod(Object obj, String name, Class<?>... parameterTypes) {
		return getMethod0(obj.getClass(), name, true, parameterTypes);
	}

	/**
	 * 从class的继承链中查询public protected private和默认修饰method方法,也可以查询静态方法.
	 * 
	 * @param clazz	类
	 * @param name	方法名称
	 * @return 查询到的方法对象
	 */
	public final static Method getMethod(Class<?> clazz, String name) {
		return getMethod0(clazz, name, true);
	}

	/**
	 * 从class的继承链中查询public protected private和默认修饰method方法,也可以查询静态方法.
	 * 
	 * @param clazz	类
	 * @param name	方法名称
	 * @param parameterTypes	方法参数class数组
	 * @return 查询到的方法对象
	 */
	public final static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		return getMethod0(clazz, name, true, parameterTypes);
	}

	/**
	 * 从class的继承链中查询声明的method方法,也可以查询静态方法.
	 * 
	 * @param declared
	 *            是否查询声明方法,true:可以查询public protected private
	 *            static和默认修饰的方法;false:只能查询public修饰方法
	 * @param clazz
	 *            类
	 * @param name	方法名称
	 * @param parameterTypes	方法参数class数组
	 * @return 查询到的方法对象
	 */
	private final static Method getMethod0(Class<?> clazz, String name, boolean declared, Class<?>... parameterTypes) {
		Method method = null;
		while (clazz != null && clazz != Object.class) {
			try {
				method = declared ? clazz.getDeclaredMethod(name, parameterTypes)
						: clazz.getMethod(name, parameterTypes);
				if (method != null)
					break;
				clazz = clazz.getSuperclass();
			} catch (NoSuchMethodException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return method;
	}

	/**
	 * 获取所有不包含static修饰的字段
	 * 
	 * @param obj	对象
	 * @return
	 */
	public final static Field[] getFields(Object obj) {
		return getFields(obj.getClass());
	}

	/**
	 * 获取所有不包含static修饰的字段
	 * 
	 * @param clazz	类
	 * @return
	 */
	public final static Field[] getFields(Class<?> clazz) {
		Field[] answer = null;
		// 获取所有声明的字段
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			answer = new Field[fields.length];
			int pos = 0;
			for (Field field : fields) {
				// 跳过static修饰的属性
				if ((field.getModifiers() & Modifier.STATIC) != 0) {
					continue;
				}
				answer[pos++] = field;
			}
			if (pos != fields.length) {
				Field[] dest = new Field[pos];
				System.arraycopy(answer, 0, dest, 0, pos);
				answer = dest;
			}
		}
		return answer;
	}

	/**
	 * 从对象的继承链中查询public protected private和默认修饰method属性,也可以查询静态属性.
	 * 
	 * @param obj	对象
	 * @param name	方法名称
	 * @return 查询到的方法对象
	 */
	public final static Field getField(Object obj, String name) {
		return getField0(obj.getClass(), name, true);
	}

	/**
	 * 从class的继承链中查询public protected private和默认修饰method属性,也可以查询静态属性.
	 * 
	 * @param clazz	类
	 * @param name	方法名称
	 * @return 查询到的方法对象
	 */
	public final static Field getField(Class<?> clazz, String name) {
		return getField0(clazz, name, true);
	}

	/**
	 * 从class的继承链中查询声明的属性,也可以查询静态属性.
	 * 
	 * @param declared
	 *            是否查询声明属性,true:可以查询public protected private
	 *            static和默认修饰的属性;false:只能查询public修饰属性
	 * @param clazz
	 * @param name
	 * @return
	 */
	private final static Field getField0(Class<?> clazz, String name, boolean declared) {
		Field field = null;
		while (clazz != null && clazz != Object.class) {
			try {
				field = declared ? clazz.getDeclaredField(name) : clazz.getField(name);
				if (field != null)
					break;
				clazz = clazz.getSuperclass();
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return field;
	}

	/**
	 * 调用默认构造函数实例化对象
	 * 
	 * @param className	对象名称
	 * @return
	 */
	public static <T> T newInstance(String className) {
		return newInstance(className, new Object[0]);
	}

	/**
	 * 调用带有参数构造函数实例化对象
	 * 
	 * @param className	对象名称
	 * @param parameter	构造函数参数,如果为null调用默认构造函数
	 * @return
	 */
	public static <T> T newInstance(String className, Object... parameter) {
		T answer = null;
		Class<T> clazz = forName(className);
		if (clazz != null) {
			try {
				Class<?>[] types = null;
				if (parameter != null) {
					types = new Class<?>[parameter.length];
					for (int i = 0; i < parameter.length; i++) {
						types[i] = parameter[i].getClass();
					}
				} else {
					types = new Class<?>[0];
				}
				Constructor<T> constructor = null;
				try {
					constructor = clazz.getConstructor(types);
					if (constructor != null) {
						answer = constructor.newInstance(parameter);
					}
				} catch (NoSuchMethodException e) {
					throw new IllegalArgumentException("没有找到参数对应的构造函数,无法实例化对象 " + className, e);
				}
			} catch (Exception e) {

				throw new IllegalArgumentException("无法实例化对象 " + className, e);
			}
		}
		return answer;
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> forName(String className) {
		Class<T> clazz = null;
		try {
			clazz = (Class<T>) Class.forName(className);
		} catch (ClassNotFoundException e1) {
			clazz = forName(className, getDefaultClassLoader());
		} catch (Throwable e) {
			throw new IllegalArgumentException("不能加载class: " + className, e);
		}
		return clazz;

	}

	/**
	 * 加载class
	 * 
	 * @param className 类名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> forName(String className, ClassLoader loader) {
		Class<T> clazz = null;
		try {
			clazz = (Class<T>) loader.loadClass(className);
//			try {
//				clazz = (Class<T>) Class.forName(className);
//			} catch (ClassNotFoundException e1) {
//				try {
//					clazz = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(className);
//				} catch (ClassNotFoundException e2) {
//					try {
//						clazz = (Class<T>) ClassUtil.class.getClassLoader().loadClass(className);
//					} catch (ClassNotFoundException e3) {
//
//					}
//				}
//			}
		} catch (Throwable e) {
			throw new IllegalArgumentException("不能加载class: " + className, e);
		}
		return clazz;
	}

	/**
	 * 获取classPath内的输入流
	 * 
	 * @param name
	 *            流名称
	 * @return
	 */
	public static InputStream getResourceAsStream(String name) {
		InputStream in = ClassUtil.class.getResourceAsStream(name);
		if (in == null) {
			getDefaultClassLoader().getResourceAsStream(name);
		}
		return in;
	}

	/**
	 * 获取默认的classloader
	 * 
	 * @return
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			try {
				cl = Thread.currentThread().getContextClassLoader();
			} catch (Throwable ex) {}
			if (cl == null) {
				cl = ClassLoader.getSystemClassLoader();
				if (cl == null) {
					cl = ClassUtil.class.getClassLoader();
				}
			}
		} catch (Throwable ex) {}
		return cl;
	}

	/**
	 * 加载jar文件中指定的类
	 * 
	 * @param clazz
	 * @param jars
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String clazz, File... jars) {
		Class<T> answer = null;
		ClassLoader loader = loadLibrary(jars);
		try {
			answer = (Class<T>) loader.loadClass(clazz);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("不能加载class: " + clazz, e);
		}
		return answer;
	}

	/**
	 * 加载jar文件,注意不能使用Class.forName(className)实例化类
	 * 
	 * @param jars
	 * @return
	 */
	public static ClassLoader loadLibrary(File... jars) {
		ClassLoader answer = null;
		try {
			URL[] libray = new URL[jars.length];
			int pos = 0;
			for (File jar : jars) {
				if (jar != null && jar.exists()) {
					libray[pos++] = jar.toURI().toURL();
				}
			}
			if (pos > 0) {
				if (pos < jars.length) {
					URL[] dest = new URL[pos];
					System.arraycopy(libray, 0, dest, 0, pos);
					libray = dest;
				}
				answer = new URLClassLoader(libray);
			}
		} catch (Throwable ex) {
			throw new IllegalArgumentException("不能加载jar: " + jars, ex);
		}
		return answer;
	}

	/**
	 * 加载远程jar文件,到系统的类加载器中. 可以使用Class.forName(className)实例化类
	 * 
	 * @param jar	jar文件
	 */
	public static void loadClassLibrary(File... jars) {
		try {
			URL[] libray = new URL[jars.length];
			int pos = 0;
			for (File jar : jars) {
				if (jar != null && jar.exists()) {
					libray[pos++] = jar.toURI().toURL();
				}
			}
			if (pos > 0) {
				if (pos < jars.length) {
					URL[] dest = new URL[pos];
					System.arraycopy(libray, 0, dest, 0, pos);
					libray = dest;
				}
				loadClassLibrary(libray, getDefaultClassLoader());
			}
		} catch (Throwable ex) {
			throw new IllegalArgumentException("不能加载jar: " + jars, ex);
		}
	}

	/**
	 * 加载远程jar文件,到系统的类加载器中. 可以使用Class.forName(className)实例化类
	 * 
	 * @param libray		jar文件数组
	 */
	public static void loadClassLibrary(URL[] libray) {
		try {
			loadClassLibrary(libray, getDefaultClassLoader());
		} catch (Throwable e) {
			throw new IllegalArgumentException("不能加载jar: " + libray, e);
		}
	}

	/**
	 * 加载远程jar文件到指定类加载器中
	 * 
	 * @param libray		jar文件数组
	 * @param classLoader	类加载器
	 */
	private static void loadClassLibrary(URL[] libray, ClassLoader classLoader) {
		try {
			if (classLoader == null) {
				classLoader = getDefaultClassLoader();
			}
			Method m = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			m.setAccessible(true);
			for (URL url : libray) {
				if (url != null)
					m.invoke(classLoader, new Object[] { url });
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException("不能加载jar: " + libray, ex);
		}
	}

	/**
	 * 获取父类泛型类. 条件 1. 必须有父类 2. 父类必须有泛型 3. 索引位置必须小于泛型数量
	 * 
	 * @param clazz	需要反射的类,该类必须继承泛型父类
	 * @param index	泛型索引
	 * @return
	 */
	public final static <T> Class<T> getGenericForSuperclass(final Class<?> clazz, final int index) {
		Class<T> answer = null;
		// 得到泛型父类
		Type genType = clazz.getGenericSuperclass();
		// 如果没有实现ParameterizedType接口,也就是不支持泛型,直接返回null
		if (genType != null && genType instanceof ParameterizedType) {
			answer = getParameterizedType((ParameterizedType) genType, index);
		}
		return answer;
	}

	/**
	 * 获取方法上泛型类
	 * 
	 * @param method		方法
	 * @param index		索引
	 * @return
	 */
	public final static <T> Class<T> getGenericForMethod(Method method, final int index) {
		Class<T> answer = null;
		Type[] genType = method.getGenericParameterTypes();
		Type generic = genType[index];
		if (genType != null && generic instanceof ParameterizedType) {
			answer = getParameterizedType((ParameterizedType) generic, index);
		}
		return answer;
	}

	/**
	 * 获取字段上泛型类
	 * 
	 * @param field	字段
	 * @param index 索引
	 * @return
	 */
	public final static <T> Class<T> getGenericForField(Field field, final int index) {
		Class<T> answer = null;
		Type genType = field.getGenericType();
		if (genType != null && genType instanceof ParameterizedType) {
			answer = getParameterizedType((ParameterizedType) genType, index);
		}
		return answer;
	}

	/**
	 * 获取泛型类
	 * 
	 * @param parameterized	泛型字段
	 * @param index			索引
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private final static <T> Class<T> getParameterizedType(ParameterizedType parameterized, final int index) {
		Type[] params = parameterized.getActualTypeArguments();
		return (Class<T>) params[index];
	}
}
