package com.dragonchang.domain.dto;

import lombok.Data;

@Data
public class PageRequestDTO {


	public PageRequestDTO(){}

	public PageRequestDTO(int page, int size, String order){
		this.page = page;
		this.size = size;
		this.order = order;
	}

	/**
	 * 页码
	 */
	private Integer page = 1 ;
	
	/**
	 * 每页记录数
	 */
	private Integer size = 10;

	/**
	 * 排序字段
	 */
	private String order;

}
