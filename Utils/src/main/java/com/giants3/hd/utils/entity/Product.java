package com.giants3.hd.utils.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品表
 */
@Entity(name="Product")
public class Product implements Serializable {

	/**
	 * 备注
	 */
	@Basic
	public String memo;
	/**
	 * 图片，存放缩略图
	 */
	@Basic
	public byte[] photo;
	/**
	 * 图片路径 大图片存放地址
	 */
	@Basic
	public String url;
	/**
	 * 产品类别id
	 */
	@Basic
	public long pClassId;
	/**
	 * 产品类别名称
	 */
	@Basic
	public String pClassName;
	/**
	 * 产品单位id
	 */
	@Basic
	public String pTypeId;
	/**
	 * 产品单位名称
	 */
	@Basic
	public String pTypeName;
	/**
	 * 净重
	 */
	@Basic
	public float weight;
	/**
	 * 人工成本
	 */
	@Basic
	public float costWage;
	/**
	 * 板料成本
	 */
	@Basic
	public float cost8;
	/**
	 * 配件成本
	 */
	@Basic
	public float cost7;
	/**
	 * 木料成本
	 */
	@Basic
	public float cost1;
	/**
	 * 玻璃镜片成本
	 */
	@Basic
	public float cost6;
	/**
	 * 铁配件成本
	 */
	@Basic
	public float cost11_15;
	/**
	 * 木油成本
	 */
	@Basic
	public float cost5;
	@Basic
	public String attribute;
	/**
	 * 包装组合
	 */
	@Basic
	public String packGroup;
	/**
	 * id 标识字段
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public long id;
	/**
	 * 产品名称
	 */
	@Basic
	public String  name;
	/**
	 * 登记日期
	 */
	@Basic
	public String rDate;


	/**
	 * 规格描述
	 * @return
	 */
	@Basic
	public String spec;


	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getpClassId() {
		return pClassId;
	}

	public void setpClassId(long pClassId) {
		this.pClassId = pClassId;
	}

	public String getpClassName() {
		return pClassName;
	}

	public void setpClassName(String pClassName) {
		this.pClassName = pClassName;
	}

	public String getpTypeId() {
		return pTypeId;
	}

	public void setpTypeId(String pTypeId) {
		this.pTypeId = pTypeId;
	}

	public String getpTypeName() {
		return pTypeName;
	}

	public void setpTypeName(String pTypeName) {
		this.pTypeName = pTypeName;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getCostWage() {
		return costWage;
	}

	public void setCostWage(float costWage) {
		this.costWage = costWage;
	}

	public float getCost8() {
		return cost8;
	}

	public void setCost8(float cost8) {
		this.cost8 = cost8;
	}

	public float getCost7() {
		return cost7;
	}

	public void setCost7(float cost7) {
		this.cost7 = cost7;
	}

	public float getCost1() {
		return cost1;
	}

	public void setCost1(float cost1) {
		this.cost1 = cost1;
	}

	public float getCost6() {
		return cost6;
	}

	public void setCost6(float cost6) {
		this.cost6 = cost6;
	}

	public float getCost11_15() {
		return cost11_15;
	}

	public void setCost11_15(float cost11_15) {
		this.cost11_15 = cost11_15;
	}

	public float getCost5() {
		return cost5;
	}

	public void setCost5(float cost5) {
		this.cost5 = cost5;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getPackGroup() {
		return packGroup;
	}

	public void setPackGroup(String packGroup) {
		this.packGroup = packGroup;
	}

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

	public String getrDate() {
		return rDate;
	}

	public void setrDate(String rDate) {
		this.rDate = rDate;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
}