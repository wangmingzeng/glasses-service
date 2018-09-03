package cn.com.zach.demo.glasses.mode;

public class PageInfo {

	// 开始页
	private Integer curPage = 1;
	// 每页数据量
	private Integer pageLimit = 10;
	// 总共多少条数据
	private long total = 0;
	// 排序
	private String orderBy = "id desc";

	public PageInfo() {
	}

	public PageInfo(Integer curPage, Integer pageLimit, long total) {
		this.curPage = curPage;
		this.pageLimit = pageLimit;
		this.total = total;
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(Integer pageLimit) {
		if (pageLimit > 1000) {
			this.pageLimit = 1000;
		} else {
			this.pageLimit = pageLimit;
		}
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public static PageInfo getPageInfo(Integer curPage, Integer pageLimit, long total) {
		return new PageInfo(curPage, pageLimit, total);
	}

	@Override
	public String toString() {
		return "PageInfo [curPage=" + curPage + ", pageLimit=" + pageLimit + ", total=" + total + ", orderBy=" + orderBy
				+ "]";
	}
}
