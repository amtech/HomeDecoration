package com.giants3.hd.utils.entity;

import com.giants3.hd.utils.FloatHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * 产品材料列表
 */
@Entity(name="T_ProductMaterial")
public class ProductMaterial  implements Serializable,Summariable {

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
	 * 产品规格长
	 */
	@Basic
	public float pLong;
	/**
	 * 产品规格 宽
	 */
	@Basic
	public float pWidth;
	/**
	 * 产品规格 高
	 */
	@Basic
	public float pHeight;
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


	/**
	 * 材质类型选择   当材质是包装类型 即  flowId=Flow.FLOW_PACK 时候 必填
	 */
	@ManyToOne
	public PackMaterialType packMaterialType;


	/**
	 * 包装材质位置选择  可选
	 */
	@ManyToOne
	public PackMaterialPosition packMaterialPosition;


	/**
	 * 材料分类 名称
	 */
	@Basic
	public String className;


	/**
	 * 材料分类id
	 */
	@Basic
	public String classId;


	public PackMaterialType getPackMaterialType() {
		return packMaterialType;
	}

	public void setPackMaterialType(PackMaterialType packMaterialType) {
		this.packMaterialType = packMaterialType;
	}

	public PackMaterialPosition getPackMaterialPosition() {
		return packMaterialPosition;
	}

	public void setPackMaterialPosition(PackMaterialPosition packMaterialPosition) {
		this.packMaterialPosition = packMaterialPosition;
	}

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

		price=new BigDecimal(material.price).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
		available=material.available;

		unitName=material.unitName;
		type=material.typeId;

		mWidth=material.wWidth;
		mHeight=material.wHeight;
		mLong =material.wLong;
		classId=material.classId;
		className=material.className;
		memo=material.spec;
		update();






	}

	public float getpLong() {
		return pLong;
	}

	public void setpLong(float pLong) {
		this.pLong = pLong;
	}

	public float getpWidth() {
		return pWidth;
	}

	public void setpWidth(float pWidth) {
		this.pWidth = pWidth;
	}

	public float getpHeight() {
		return pHeight;
	}

	public void setpHeight(float pHeight) {
		this.pHeight = pHeight;
	}

	/**
	 * 更新材料相关数据
	 */
	public void update()
	{




		wLong=mLong+pLong;
		wHeight=mHeight+pHeight;
		wWidth=mWidth+pWidth;






		//计算定额



		float newQuota=0;





		if(materialId<=0)
			newQuota=0;
		else
		if(quantity<=0)
			newQuota=0;
		else


		//如果是包装材料  采用特殊计算方式
		if(flowId==Flow.FLOW_PACK)
		{



			//箱子类计算
			if(classId.equals(Material.MCLass.C_BAZZ))
			{

				if(pWidth<15) wWidth=pWidth*2;
				if( pLong+pWidth>130)
				{
					newQuota=(pLong/100+pWidth/100+0.17f)*(wWidth/100+pHeight/100+0.07f)*2*quantity;
				}else
				{
					newQuota=(pLong/100+pWidth/100+0.09f)*(wWidth/100+pHeight/100+0.07f)*2*quantity;
				}



			}
			else
			//内盒计算公式
			if(classId.equals(Material.MCLass.C_2201))
			{

				newQuota=(pLong/100+pWidth/100+0.07f)*(pWidth/100+pHeight/100+0.04f)*2*quantity;


			}
			else
				//单瓦彩色内盒特殊计算
				if(materialCode.equals("BZAF0013"))
				{
					newQuota=(pLong/100+pWidth/100*4)*(pWidth/100+pHeight/100+0.06f)*2*quantity;
				}else
				//彩色内盒计算公式
				if(classId.equals(Material.MCLass.C_BZAF))
				{
					newQuota=(pLong/100+pWidth/100*2+0.42f)*(pWidth/100*4+pHeight/100+0.02f)*quantity;
				}
			else
			//胶带计算公式
			if(classId.equals(Material.MCLass.C_BZAE))
			{
//TODO  胶带计算公式
//				if(pLong<80&&pWidth>=20)
//					newQuota=()
//					else


			}
			else
			{
				//默认计算公式
					if(pLong<=0&&pWidth<=0&&pHeight<=0)
					{
						newQuota=available<=0?0:quantity/available;

					}else
						if(pWidth<=0&&pHeight<=0)
						{
							newQuota=available<=0?0:quantity*wLong/100/available;
						}
				else
							if(pHeight<=0)
							{
								newQuota=available<=0?0:quantity*wLong*wWidth/10000/available;
							}
							else
							{newQuota=available<=0?0:quantity*wLong*wWidth*wHeight/1000000/available;}


			}






		}else {


			if (type == 1 && pHeight <= 0) {

				newQuota = 0;

			} else if (type == 15 && pWidth <= 0 && pHeight <= 0) {


				//分件备注指 参与类型15的计算
				float r15 = 0;
				try {
					r15 = Float.valueOf(memo);
				} catch (Throwable t) {
					Logger.getLogger("TEST").info("分件備註值不是一個浮點數");
				}
				newQuota = quantity * wLong * r15 / 100 / available;   //TODO  R15 未确定值

			} else if (pWidth <= 0 && pLong <= 0 && pHeight <= 0) {
				newQuota = quantity / available;
			} else if (
					pHeight <= 0 && pWidth <= 0
					) {
				newQuota = quantity * wLong / 100 / available;

			} else if (pHeight <= 0) {


				newQuota = quantity * wLong * wWidth / 10000 / available;
			} else {
				newQuota = quantity * wLong * wWidth * wHeight / 1000000 / available;
			}

		}



		  quota = FloatHelper.scale(newQuota,5);


		   amount= FloatHelper.scale(quota*price );






	}
}