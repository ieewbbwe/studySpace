package cn.android_mobile.core.net.http.yjweb;

import java.util.List;

/**
 * 分页实现类
 * @author fangzhen
 *
 * @param <T>
 */
public class PageList<T> {
	
	//返回结果
	private List<T> rows;
	
	//总记录数
	private int total;
	
	//每页显示多少数据
	private int pageSize;
	
	//第几页
	private int pageNo;

	/**
	 * 返回总页数 
	 * @return
	 */
	public int getTotalPages() {
		return (total/pageSize+(total%pageSize>0?1:0));
	}
	
	/**
	 * 首页
	 * @return
	 */
	public int getTopPageNo() {
		return 1;
	}
	
	/**
	 * 上一页 
	 * @return
	 */
	public int getPreviousPageNo() {
		if (this.pageNo <= 1) {
			return 1;
		}
		return this.pageNo - 1;
	}
	
	/**
	 * 下一页
	 * @return
	 */
	public int getNextPageNo() {
		if (this.pageNo >= getButtomPageNo()) {
			return getButtomPageNo();
		}
		return this.pageNo + 1;
	}
	
	/**
	 * 尾页
	 * @return
	 */
	public int getButtomPageNo() {
		return getTotalPages();
	}
	
	public List<T> getList() {
		return rows;
	}

	public void setList(List<T> list) {
		this.rows = list;
	}

	public int getTotalRecords() {
		return total;
	}

	public void setTotalRecords(int totalRecords) {
		this.total = totalRecords;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
}
