import java.util.Vector;

/** 
 *  ��Ʒ��Ϣ��  
 */
public class Product {
  /* {author=davidleen29, version=1.0, since=2015-05-01}*/


  /** 
   *  ��Ʒ����   �������� �� 10A3091 ��ʽ   10 ��ʾ20xx���  A/B ��ʾ a ���ڹ㽻��  b �＾   3091 Ϊ���
   */
  public String name;

  /** 
   *  ��Ʒ��λ  ��S/ ��ʾ��ʽ
   */
  public Integer unit;

  /** 
   *  �ɷ�˵��   ���硾  ɼľ40% MDF60%��
   */
  public String componentMemo;

  /** 
   *  ��ƷͼƬ·��
   */
  public String url;

  /** 
   *  ����
   */
  public Integer netWeight;

  /** 
   *  ë��
   */
  public Integer roughWeight;

  /** 
   *  id�ֶΡ������ֵ
   */
  public id;

    /**
   * 
   * @element-type ProductMaterials
   */
  public Vector  materials;

}