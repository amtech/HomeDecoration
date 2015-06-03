package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品油漆信息
 * 包括 材料工序
 */


@Entity(name="T_ProductPaint")
public class ProductPaint  implements Serializable,Summariable {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	public long id;
	public long productId;
	/**
	 * 工序id
	 */
	@Basic
	private long processId;
	/**
	 * 物料id
	 */
	@Basic
	public long materialId;
	/**
	 * 产品名称
	 */
	@Basic
	private String productName;

	/**
	 * 流程id
	 */
	@Basic
	public long flowId;


	public long getFlowId() {
		return flowId;
	}

	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	/**
	 * 物料编码
	 */
	@Basic
	public String materialCode;
	/**
	 * 物料名称
	 */
	@Basic
	public String materialName;


	/**
	 * 物料单位
	 */
	@Basic
	public String unitName;




	/**
	 * 工序编码
	 */
	@Basic
	public String processCode;

	/**
	 * 工序名称
	 */
	@Basic
	public String processName;



	/**
	 * 材料单价
	 */
	@Basic
	public float materialPrice;
	/**
	 * 工序单价
	 */
	@Basic
	public float processPrice;
	/**
	 * 配料比例  值【0，】  配料都是稀释剂
	 */
	@Basic
	public float ingredientRatio;
	/**
	 * 材料用量  
	 */
	@Basic
	public float materialQuantity;
	/**
	 * 材料费用=（materialPrice*materialQuantity）
	 */
	@Basic
	public float materialCost;
	/**
	 * 配料（稀释剂）费用（配料用量×配料单价）
	 */
	@Basic
	public float ingredientCost;
	/**
	 * 配料（稀释剂）的用量   =材料用量×（配料比例/（1+配料比例））
	 * 
	 * =materialQuantity*(ingredientRatio/(1+ingredientRatio))
	 */
	@Basic
	public float ingredientQuantity;


	/**
	 * 材料分类类型
	 */
	@Basic
	public int materialType;


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

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(long materialId) {
		this.materialId = materialId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public float getMaterialPrice() {
		return materialPrice;
	}

	public void setMaterialPrice(float materialPrice) {
		this.materialPrice = materialPrice;
	}

	public float getProcessPrice() {
		return processPrice;
	}

	public void setProcessPrice(float processPrice) {
		this.processPrice = processPrice;
	}

	public float getIngredientRatio() {
		return ingredientRatio;
	}

	public void setIngredientRatio(float ingredientRatio) {
		this.ingredientRatio = ingredientRatio;
	}

	public float getMaterialQuantity() {
		return materialQuantity;
	}

	public void setMaterialQuantity(float materialQuantity) {
		this.materialQuantity = materialQuantity;
	}

	public float getMaterialCost() {
		return materialCost;
	}

	public void setMaterialCost(float materialCost) {
		this.materialCost = materialCost;
	}

	public float getIngredientCost() {
		return ingredientCost;
	}

	public void setIngredientCost(float ingredientCost) {
		this.ingredientCost = ingredientCost;
	}

	public float getIngredientQuantity() {
		return ingredientQuantity;
	}

	public void setIngredientQuantity(float ingredientQuantity) {
		this.ingredientQuantity = ingredientQuantity;
	}


	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}


	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	/**
	 * 更新材料数据
	 * @param material
	 */
	public void updateMaterial(Material material) {




		this.materialCode=material.code;
		this.materialName=material.name;
		this.materialId=material.id;
		this.materialPrice=material.price;
		this.unitName=material.unitName;
	this.materialType=material.typeId;


		//

		updateMaterialAndIngredientCost();





	}


	/**
	 * 更新材料费用与配料费用
	 */
	public  void updateMaterialAndIngredientCost()
	{
		materialCost=materialQuantity*materialPrice;
		ingredientQuantity=materialQuantity*(ingredientRatio/(1+ingredientRatio));
		ingredientCost=ingredientQuantity*1.2f;


	}

	@Override
	public int getType() {
		return materialType;
	}

	@Override
	public float getAmount() {
		return materialCost;
	}
}