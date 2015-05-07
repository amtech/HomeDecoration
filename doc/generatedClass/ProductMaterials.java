/** 
 *  产品的材料列表
 */
public class ProductMaterials {

  /** 
   *  id　字段
   */
  public Integer id;

  /** 
   *  关联的产品id
   */
  public Integer productId;

  /** 
   *  关联的材料id
   */
  public Integer materialId;

    public Product product;
    public Material material;

}