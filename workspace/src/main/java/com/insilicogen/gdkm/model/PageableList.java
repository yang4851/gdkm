package com.insilicogen.gdkm.model;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PageableList<T> {

	/**
	 * 검색 조건에 해당하는 전체 엔트리 개수
	 * 페이징 기능이 필요하지 않은 경우는 0으로 설정
	 */
	private int total = 0;
	
	/**
	 * 현재 페이지 번호 , 0인 경우는 페이징 기능이 없는 경우는 0으로 설정 
	 */
	private int page = 0;
	
	/**
	 * 한 페이지에 보여주는 목록 개수
	 */
	private int rowSize = 10;
	
	/**
	 * 검색된 대상 객체 목록 
	 */
	private List<T> list = Collections.emptyList();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRowSize() {
		return rowSize;
	}

	public void setRowSize(int rowSize) {
		this.rowSize = rowSize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
