package cn.com.zach.demo.glasses.mode;

public class ReturnPage {

	// 开始页
	private Integer page = 1;

	// 每页数据量
	private Integer pageSize = 10;

	// 总共多少条数据
	private long total = 0;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
	public ReturnPage() {}
	
	public ReturnPage(Integer page, Integer pageSize, long total) {
		this.page = page;
		this.pageSize = pageSize;
		this.total = total;
	}
}
