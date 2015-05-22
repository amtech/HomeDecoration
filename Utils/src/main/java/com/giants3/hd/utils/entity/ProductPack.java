
package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品包装信息
 * 
 * 含产品fob 成本 包装规格  装箱数  等参数u
 */
@Entity
public class ProductPack   implements Serializable {

	/**
	 * 包装记录id
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public long id;
	/**
	 * 产品id
	 */
	@Basic
	public long productId;
	/**
	 * 包装类型id
	 */
	@Basic
	public long packId;


	/**
	 * 包装名称
	 */
	@Basic
	public String
			packName;
	/**
	 * 出口价  即是FOB
	 */
	@Basic
	public float fob;
	/**
	 * 成本价
	 */
	@Basic
	public float cost;
	/**
	 * 出产价格 公式  成本价/ 利润比例
	 */
	@Basic
	public float price;
	/**
	 * 包装产品数量  即一个包装内有几个产品
	 */
	@Basic
	public int quantity;
	/**
	 * 包装体积
	 */
	@Basic
	public float packVolume;
	/**
	 * 包装的宽
	 */
	@Basic
	public float packWidth;
	/**
	 * 包装的高
	 */
	@Basic
	public float packHeight;
	/**
	 * 包装长
	 */
	@Basic
	public float packLong;
	/**
	 * 包材成本
	 */
	@Basic
	public float packMaterialCost;


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

	public long getPackId() {
		return packId;
	}

	public void setPackId(long packId) {
		this.packId = packId;
	}

	public float getFob() {
		return fob;
	}

	public void setFob(float fob) {
		this.fob = fob;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPackVolume() {
		return packVolume;
	}

	public void setPackVolume(float packVolume) {
		this.packVolume = packVolume;
	}

	public float getPackWidth() {
		return packWidth;
	}

	public void setPackWidth(float packWidth) {
		this.packWidth = packWidth;
	}

	public float getPackHeight() {
		return packHeight;
	}

	public void setPackHeight(float packHeight) {
		this.packHeight = packHeight;
	}

	public float getPackLong() {
		return packLong;
	}

	public void setPackLong(float packLong) {
		this.packLong = packLong;
	}

	public float getPackMaterialCost() {
		return packMaterialCost;
	}

	public void setPackMaterialCost(float packMaterialCost) {
		this.packMaterialCost = packMaterialCost;
	}


	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}
}