package com.giants3.hd.utils.entity;

import java.io.Serializable;

/**
 * 单位 数据  单位 表
 */


public class Unit implements Serializable{

	/**
	 * 单位 id
	 */

	public long id;
	/**
	 * 单位 名称
	 */

	public String name;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}