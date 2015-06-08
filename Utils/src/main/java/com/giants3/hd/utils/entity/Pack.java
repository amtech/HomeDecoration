package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 包装，
 * 
 * 主要有三种包装类型
 * 
 * 普通包装  加强包装
 * 
 * 特殊包装
 */
@Entity(name="T_Pack")
public class Pack  implements Serializable {

	/**
	 * 包装类型id
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public long id;
	/**
	 * 包装名称
	 */

	@Basic
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


	@Override
	public String toString() {
		return name;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Pack)) return false;

		Pack pack = (Pack) o;

		return id == pack.id;

	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}
}