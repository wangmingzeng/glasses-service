package cn.com.zach.demo.glasses.mode;

/**
 * 返回码/状态码定义
 * @author
 * 
<code>
	0			表示成功
	-1			未知系统异常(Exception)
	
	1~1000		保留编码
</code>
 *
 * 
 *
 */
public class ReturnCode {
	
	/**未知系统异常*/
	public static final Integer SYSTEM_ERROR = -1;
	
	/**成功*/
	public static final Integer SUCCESS = 200;

	/**参数错误*/
	public static final Integer PARAMS_ERROR = 201;
	
	/** 例如 */
	public static final Integer UNDEFINE = 4000;
	
	
}
