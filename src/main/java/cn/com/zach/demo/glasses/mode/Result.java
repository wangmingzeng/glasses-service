package cn.com.zach.demo.glasses.mode;

import java.util.Collection;

/**
 * Controller 返回
 */
public class Result {

	/**
	 * 结果编码
	 */
	private long errcode = 200;

	/**
	 * 描述
	 */
	private String errmsg;

	public Result() {
	}

	public Result(long errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	public long getErrcode() {
		return errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	/**
	 * 返回成功
	 * 
	 * @return
	 */
	public static Result success() {
		return new Result(ReturnCode.SUCCESS, "OK");
	}

	/**
	 * 成功返回对象, 支持对象 数组 集合 null等等返回
	 * 
	 * @param obj
	 * @return
	 */
	public static Result success(Object obj) {
		Result answer = null;
		if (obj == null) {
			// 空对象
			answer = new Result(ReturnCode.SUCCESS, "OK");
		} else if (obj instanceof Collection) {
			// 集合对象
			answer = new ListResult(ReturnCode.SUCCESS, "OK", (Collection<?>) obj);
		} else if (obj.getClass().isArray()) {
			// 数组对象
			answer = new ArrayResult(ReturnCode.SUCCESS, "OK", obj);
		} else {
			// 普通对象
			answer = new ObjectResult(ReturnCode.SUCCESS, "OK", obj);
		}
		return answer;
	}

	/**
	 * 成功返回对象, 支持对象 数组 集合 null等等返回
	 * 
	 * @param obj
	 * @return
	 */
	public static Result success(Object obj, ReturnPage page) {
		Result answer = null;
		if (obj == null) {
			// 空对象
			answer = new Result(ReturnCode.SUCCESS, "OK");
		} else if (obj instanceof Collection) {
			// 集合对象
			if (page != null)
				answer = new ListResult(ReturnCode.SUCCESS, "OK", (Collection<?>) obj, page);
			else
				answer = new ListResult(ReturnCode.SUCCESS, "OK", (Collection<?>) obj);
		} else if (obj.getClass().isArray()) {
			// 数组对象
			if (page != null)
				answer = new ArrayResult(ReturnCode.SUCCESS, "OK", obj, page);
			else
				answer = new ArrayResult(ReturnCode.SUCCESS, "OK", obj);
		} else {
			// 普通对象
			answer = new ObjectResult(ReturnCode.SUCCESS, "OK", obj);
		}
		return answer;
	}

	/**
	 * 返回对象
	 * 
	 * @param obj
	 * @return
	 */
	public static Result out(Object obj) {
		return success(obj);
	}

	/**
	 * 返回对象
	 * 
	 * @param obj
	 * @param page
	 *            分页信息
	 * @return
	 */
	public static Result out(Object obj, ReturnPage page) {
		return success(obj, page);
	}

	/**
	 * 返回失败
	 * 
	 * @param errcode
	 *            错误编码
	 * @param t
	 *            异常
	 * @return
	 */
	public static Result fail(long errcode, Throwable t) {
		return fail(errcode, t.getMessage());
	}

	/**
	 * 返回失败
	 * 
	 * @param msg
	 *            错误原因
	 * @return
	 */
	public static Result fail(String msg) {
		return fail(-1, msg);
	}

	/**
	 * 返回失败
	 * 
	 * @param errcode
	 *            错误编码,如果为0强制转换-1
	 * @param msg
	 *            错误原因
	 * @return
	 */
	public static Result fail(long errcode, String msg) {
		// errcode不等于-1
		if (errcode == 0) {
			errcode = -1;
		}
		if (msg == null || msg.length() == 0) {
			msg = "未知错误";
		}
		return new Result(errcode, msg);
	}

	/**
	 * 对象返回
	 * 
	 * @param <T>
	 */
	public static class ObjectResult extends Result {
		private Object data = null;

		public ObjectResult() {
		}

		public ObjectResult(long errcode, String errmsg, Object data) {
			super(errcode, errmsg);
			this.data = data;
		}

		public Object getData() {
			return data;
		}
	}

	/**
	 * 列表返回
	 *
	 * @param <T>
	 */
	public static class ListResult extends Result {
		private Collection<?> data = null;
		private ReturnPage page = null;

		public ListResult() {
		}

		public ListResult(long errcode, String errmsg, Collection<?> data) {
			super(errcode, errmsg);
			this.data = data;
		}

		public ListResult(long errcode, String errmsg, Collection<?> data, ReturnPage page) {
			super(errcode, errmsg);
			this.data = data;
			this.page = page;
		}

		public Collection<?> getData() {
			return data;
		}

		public ReturnPage getPage() {
			return page;
		}
	}

	/**
	 * 列表返回
	 *
	 * @param <T>
	 */
	public static class ArrayResult extends Result {
		private Object data = null;
		private ReturnPage page = null;

		public ArrayResult() {
		}

		public ArrayResult(long errcode, String errmsg, Object data) {
			super(errcode, errmsg);
			this.data = data;
		}

		public ArrayResult(long errcode, String errmsg, Object data, ReturnPage page) {
			super(errcode, errmsg);
			this.data = data;
			this.page = page;
		}

		public Object getData() {
			return data;
		}

		public ReturnPage getPage() {
			return page;
		}
	}

}
