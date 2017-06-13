package com.qianfan365.jcstore.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
@SuppressWarnings("rawtypes")
public class PageForm implements Serializable {
    private static final long serialVersionUID = 587754556498974978L;
    
    //当前页
    private int currentPage;
    //当前显示到的ID, 在mysql limit 中就是第2个参数.
    //pagesize ，每页显示多少
    private int showCount = 10;
    //总页?
    private int totalPage;
    //总记录数
    private Long totalResult;
    //当前页的结果数
    private int currentResult;
    //数据集合
    private List data;
    
    
    
    //获取PageForm
    public static PageForm getPageForm(Page page){
    	PageForm  pageform = new PageForm();
    	pageform.setCurrentPage(page.getPageNum());
    	pageform.setCurrentResult(page.getEndRow());
    	pageform.setShowCount(page.getPageSize());
    	pageform.setTotalPage(page.getPages());
    	pageform.setTotalResult(page.getTotal());
    	pageform.setData(page);
    	return pageform;
    }
    
    
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public int getShowCount() {
		return showCount;
	}
	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public Long getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(Long totalResult) {
		this.totalResult = totalResult;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getCurrentResult() {
		return currentResult;
	}
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public void init(int currentPage, int showCount){
		if(showCount!=0){
			setShowCount(showCount);
		}
		setCurrentPage(currentPage);
		setCurrentResult((currentPage-1)*getShowCount());
	}
	
	public Map<String, Object> toJson(){
		return toJson(null);
	}
	
    @SuppressWarnings("unchecked")
    public Map<String, Object> toJson(Map other){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sEcho", "");
		map.put("iTotalRecords", totalResult);
		map.put("iTotalDisplayRecords", showCount);
		map.put("aaData", data==null?new ArrayList():data);
		if( other!=null )
			map.putAll(other);
		return map;
	}
}
