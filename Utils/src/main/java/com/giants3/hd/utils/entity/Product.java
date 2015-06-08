package com.giants3.hd.utils.entity;


import com.giants3.hd.utils.FloatHelper;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品表
 */
@Entity(name="T_Product")
public class Product implements Serializable {

	/**
	 * 备注
	 */
	@Basic
	public String memo;
	/**
	 * 图片，存放缩略图
	 */
	@Lob  @Basic
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
	public String pUnitId;
	/**
	 * 产品单位名称
	 */
	@Basic
	public String pUnitName;
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
	 *包装类型  必选
	 */
	@ManyToOne(optional = false)
	public Pack pack;
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



	/**
	 * 版本号     手动录入 规格目前不限定
	 * @return
	 */
	@Basic
	public String pVersion;

	@Basic
	public long lastPhotoUpdateTime;



	/**
	 * 材料构成 成分
	 * @return
	 */
	@Basic
	public String constitute;






	/**
	 * 油漆材料成本
	 */
	@Basic
	public
	float paintCost;
	/**
	 * 油漆工资成本
	 */
	@Basic
	public float paintWage;


	/**
	 * 产品成本 不含包装。 裸品
	 */
	@Basic
	public float productCost;

	/**
	 * 组装工资汇总
	 */
	@Basic
	public float assembleWage;

	/**
	 * 组装材料汇总
	 */
	@Basic
	public float assembleCost;

	/**
	 * 胚体材料汇总
	 */
	@Basic
	public float conceptusCost;
	/**
	 * 胚体工资汇总
	 */
	@Basic
	public float conceptusWage;



	@OneToOne(cascade={CascadeType.ALL})
	public Xiankang xiankang;


	/**
	 * 包装才材料成本
	 */
	@Basic
	public float packCost;

	/**
	 * 包装才材料工资
	 */
	@Basic
	public float packWage;



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
	public int packQuantity;
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


	/**
	 * 内盒数量
	 */
	@Basic
	public int insideBoxQuantity;
	/**
	 * 包材统计
	 */
	@Basic
	public float cost4;


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

	public int getPackQuantity() {
		return packQuantity;
	}

	public void setPackQuantity(int quantity) {
		this.packQuantity = quantity;
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





	public String getpUnitId() {
		return pUnitId;
	}

	public void setpUnitId(String pUnitId) {
		this.pUnitId = pUnitId;
	}

	public String getpUnitName() {
		return pUnitName;
	}

	public void setpUnitName(String pUnitName) {
		this.pUnitName = pUnitName;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getAssembleWage() {
		return assembleWage;
	}

	public void setAssembleWage(float assembleWage) {
		this.assembleWage = assembleWage;
	}

	public float getAssembleCost() {
		return assembleCost;
	}

	public void setAssembleCost(float assembleCost) {
		this.assembleCost = assembleCost;
	}

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

	public String getpPUnitId() {
		return pUnitId;
	}

	public void setpPUnitId(String pTypeId) {
		this.pUnitId = pTypeId;
	}

	public String getpPUnitName() {
		return pUnitName;
	}

	public void setpPUnitName(String pTypeName) {
		this.pUnitName = pTypeName;
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


	public long getLastPhotoUpdateTime() {
		return lastPhotoUpdateTime;
	}

	public void setLastPhotoUpdateTime(long lastPhotoUpdateTime) {
		this.lastPhotoUpdateTime = lastPhotoUpdateTime;
	}


	public String getpVersion() {
		return pVersion;
	}

	public void setpVersion(String pVersion) {
		this.pVersion = pVersion;
	}


	public String getConstitute() {
		return constitute;
	}

	public void setConstitute(String constitute) {
		this.constitute = constitute;
	}

	public void setPaintCost(float paintCost) {
		this.paintCost = paintCost;
	}

	public float getPaintCost() {
		return paintCost;
	}


	public void setPaintWage(float paintWage) {
		this.paintWage = paintWage;
	}

	public float getPaintWage() {
		return paintWage;
	}


	public float getConceptusCost() {
		return conceptusCost;
	}

	public void setConceptusCost(float conceptusCost) {
		this.conceptusCost = conceptusCost;
	}

	public float getConceptusWage() {
		return conceptusWage;
	}

	public void setConceptusWage(float conceptusWage) {
		this.conceptusWage = conceptusWage;
	}

	/**
	 * 更新油漆的汇总信息
	 * @param paintCost
	 * @param paintWage
	 */
	public void updatePaintData(float paintCost, float paintWage) {

		this.paintCost=paintCost;
		this.paintWage=paintWage;
		calculateTotalCost();
	}


	/**
	 * 计算总成本
	 */
	public void calculateTotalCost()
	{

		//TODO  目前紧紧累加 油漆数据
		productCost= FloatHelper.scale( paintCost+paintWage+assembleCost+assembleWage+conceptusCost+conceptusWage+packCost+packWage);



		packVolume= FloatHelper.scale(packLong*packWidth*packHeight/1000000,3);

		//ConfigData configData=new ConfigData();
		cost=productCost;


		//TODO  make these value  configerrable
		float ratio=0.65f;
		price=FloatHelper.scale(cost/ratio);
		float addition=0.15f;
		float payForTransform=95f;
		float exportRate=6;
		if(packQuantity <=0)
			fob=0;
		else
		//售价+海外运费
		fob=FloatHelper.scale((price * (1 + addition) + packVolume * payForTransform / packQuantity)/exportRate);



	}








}