package com.giants3.hd.utils.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name="T_Material")
public class Material  implements Serializable {

	/**
	 * 材料id
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public long id;



	/**
	 * 材料名称
	 */
	@Basic
	public String name;
	/**
	 * 材料单价
	 */
	@Basic
	public String price;
	/**
	 * 材料单位id
	 */
	@Basic
	public String unitId;
	/**
	 * 材料单位名称
	 */
	@Basic
	public String unitName;
	/**
	 * 材料类型id
	 */
	@Basic
	public int typeId;
	/**
	 * 材料类型名称
	 */
	@Basic
	public String typeName;
	/**
	 * 材料备注
	 */
	@Basic
	public String memo;
	/**
	 * 材料毛料规格 宽
	 */
	@Basic
	public float wWidth;
	/**
	 * 材料规格  长
	 */
	@Basic
	public float wLong;
	/**
	 * 材料规格高
	 */
	@Basic
	public float wHeight;
	/**
	 * 材料利用率  默认
	 */
	@Basic
	public float available;
	/**
	 * 物料 编码
	 */
	@Basic
	public String  code;


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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public float getwWidth() {
		return wWidth;
	}

	public void setwWidth(float wWidth) {
		this.wWidth = wWidth;
	}

	public float getwLong() {
		return wLong;
	}

	public void setwLong(float wLong) {
		this.wLong = wLong;
	}

	public float getwHeight() {
		return wHeight;
	}

	public void setwHeight(float wHeight) {
		this.wHeight = wHeight;
	}

	public float getAvailable() {
		return available;
	}

	public void setAvailable(float available) {
		this.available = available;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}