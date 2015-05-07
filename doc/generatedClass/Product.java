import java.util.Vector;

/** 
 *  产品信息类  
 */
public class Product {
  /* {author=davidleen29, version=1.0, since=2015-05-01}*/


  /** 
   *  产品名称   基本规则 以 10A3091 形式   10 表示20xx年份  A/B 表示 a 春节广交会  b 秋季   3091 为编号
   */
  public String name;

  /** 
   *  产品单位  以S/ 表示形式
   */
  public Integer unit;

  /** 
   *  成分说明   例如【  杉木40% MDF60%】
   */
  public String componentMemo;

  /** 
   *  产品图片路径
   */
  public String url;

  /** 
   *  净重
   */
  public Integer netWeight;

  /** 
   *  毛重
   */
  public Integer roughWeight;

  /** 
   *  id字段　　标记值
   */
  public id;

    /**
   * 
   * @element-type ProductMaterials
   */
  public Vector  materials;

}