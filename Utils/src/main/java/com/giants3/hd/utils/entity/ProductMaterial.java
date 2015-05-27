package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品材料列表
 */
@Entity(name="T_ProductMaterial")
public class ProductMaterial  implements Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public long id;
	/**
	 * 产品id
	 */
	@Basic
	public long productId;
	/**
	 * 配给额度。  重要字段。
	 */
	@Basic
	public float quota;
	/**
	 * 材料id
	 */
	@Basic
	public long materialId;

	/**
	 * 材料编码
	 */
	@Basic
	public String materialCode;

	/**
	 * 材料名称
	 */
	@Basic
	public String materialName;
	/**
	 * 单位用量
	 */
	@Basic
	public float quantity;
	/**
	 * 材料规格长
	 */
	@Basic
	public float mLong;
	/**
	 * 材料规格 宽
	 */
	@Basic
	public float mWidth;
	/**
	 * 材料规格 高
	 */
	@Basic
	public float mHeight;
	/**
	 * 毛料长度
	 */
	@Basic
	public float wLong;
	/**
	 * 毛料宽度
	 */
	@Basic
	public float wWidth;
	/**
	 * 毛料高度
	 */
	@Basic
	public float wHeight;
	/**
	 * 利用率
	 */
	@Basic
	public float available;
	/**
	 * 单位名称
	 */
	@Basic
	public String unitName;
	/**
	 * 材料类别   重要  参与定额计算
	 */
	@Basic
	public int type;
	/**
	 * 材料单价
	 */
	@Basic
	public float price;
	/**
	 * 金额字段  
	 */
	@Basic
	public float amount;
	/**
	 * 分件备注字段
	 */
	@Basic
	public String memo;


	/**
	 * 流程id
	 */
	@Basic
	public  long flowId;

	/**
	 * 流程名称
	 */
	@Basic
	public String flowName;






	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public float getQuota() {
		return quota;
	}

	public void setQuota(float quota) {
		this.quota = quota;
	}

	public long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(long materialId) {
		this.materialId = materialId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public float getmLong() {
		return mLong;
	}

	public void setmLong(float mLong) {
		this.mLong = mLong;
	}

	public float getmWidth() {
		return mWidth;
	}

	public void setmWidth(float mWidth) {
		this.mWidth = mWidth;
	}

	public float getmHeight() {
		return mHeight;
	}

	public void setmHeight(float mHeight) {
		this.mHeight = mHeight;
	}

	public float getwLong() {
		return wLong;
	}

	public void setwLong(float wLong) {
		this.wLong = wLong;
	}

	public float getwWidth() {
		return wWidth;
	}

	public void setwWidth(float wWidth) {
		this.wWidth = wWidth;
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

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}


	public long getFlowId() {
		return flowId;
	}

	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/**
	 * 设置新材料  更新各项数据
	 * @param material
	 */
	public void updateMaterial(Material material) {

		materialCode=material.code;
		materialName=material.name;
		materialId=material.id;

		available=material.available;
		memo=material.memo;
		unitName=material.unitName;


		calculateQuota();



	}

	/**
	 * 更新用量
	 * @param quantity
	 */
	public void updateQuantity(float quantity)
	{
		this.quantity=quantity;
		calculateQuota();
	}

	/**
	 * 计算定额
	 */
	public void calculateQuota()
	{





	}
}