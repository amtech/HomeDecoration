package com.giants.hd.desktop.api;

import com.giants3.hd.utils.StringUtils;
import com.ning.http.util.UTF8UrlEncoder;

/**
 *网络请求  url 串
 *
 * 读取 load 开头
 *
 * 保存 save 开头
 */
public class HttpUrl {



    public static  final String URL_ENCODING="UTF-8";

    public  static
      String BaseUrl="http://localhost:8080/";

    public static final void iniBaseUrl
            (String configUrl)
    {BaseUrl=configUrl;}

    public static String loadProductList(String productName, int pageIndex, int pageSize) {
        return BaseUrl+"api/product/search?proName="+productName+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
    }

    /**
     * 保存产品信息
     * @return
     */
    public static String saveProduct() {
         return BaseUrl+"api/product/save";
    }


    /**
     * 读取产品详情
     * @param id
     * @return
     */
    public static String loadProductDetail(long id) {
        return BaseUrl+"api/product/detail?id="+id;
    }

    /**
     * 生成读取产品图片的url
     * @param productName
     * @return
     */
    public static String loadProductPicture(String productName,String version) {
        String url= BaseUrl+"api/file/download/product/"+productName;

        if(!StringUtils.isEmpty(version))
            url+="_"+version.trim();
        url+=  ".jpg";
        return  url;
    }

    /**
     * 读取产品类型
     * @return
     */
    public static String loadProductClass() {

        return BaseUrl+"api/productClass/list";

    }

    public static String loadMaterialByCodeOrName(String value,int pageIndex,int pageSize
    ) {

        return BaseUrl+"api/material/search?codeOrName="+ UTF8UrlEncoder.encode(value)+"&pageIndex="+pageIndex+"&pageSize="+pageSize;

    }


    /**
     * 包装材料类型
     *
     * @return
     */
    public static String loadPackMaterialType() {
        return BaseUrl+"api/packMaterialType/list";
    }

    /**
     * 包装材料大分类
     *
     * @return
     */
    public static String loadPackMaterialClass() {
        return BaseUrl+"api/packMaterialClass/list";
    }

    /**
     * 包装材料使用位置
     * @return
     */
    public static String loadPackMaterialPosition() {
          return BaseUrl+"api/packMaterialPosition/list";
    }


    /**
     * 保存材料列表
     * @return
     */
    public static String saveMaterials() {
        return BaseUrl+"api/material/saveList";
    }


    /**
     *  根据材料编码列表 查询材料列表
     */
    public static String loadMaterialListByCodeEquals() {
        return BaseUrl+"api/material/findListByCodes";
    }

    /**
     *  根据材料名称列表 查询材料列表
     */
    public static String loadMaterialListByNameEquals() {
        return BaseUrl+"api/material/findListByNames";
    }


    /**
     * 读取包装列表
     * @return
     */
    public static String loadPacks() {

        return BaseUrl+"api/pack/list";

    }


    /**
     * 获取材料类型
     * @return
     */
    public static String loadMaterialTypes() {

        return BaseUrl+"api/material/listType";
    }


    /**
     * 获取材料分类
     * @return
     */
    public static String loadMaterialClasses() {

        return BaseUrl+"api/material/listClass";
    }

    public static String saveMaterial() {
        return BaseUrl+"api/material/save";
    }

    /**
     * 读取材料计算公式
     * @return
     */
    public static String loadMaterialEquations() {
        return BaseUrl+"api/material/listEquation";

    }


    /**
     * 复制产品  翻新
     * @return
     */
    public static String copyProductDetail(long id, String productName,String version) {
        return BaseUrl+"api/product/copy?id="+id+"&name="+productName+"&version="+version;
    }

    public static String deleteProductLogic(long productId) {
        return BaseUrl+"api/product/logicDelete?id="+productId ;
    }


    public static String deleteMaterialLogic(long materialId) {
        return BaseUrl+"api/material/logicDelete?id="+materialId ;
    }

    /**
     * 同步材料图片
     * @return
     */
    public static String syncMaterialPhoto() {
        return BaseUrl+"api/material/syncPhoto" ;
    }

    /**
     * 同步产品图片
     * @return
     */
    public static String syncProductPhoto() {

        return BaseUrl+"api/product/syncPhoto" ;
    }


    /**
     * 读取材料图片
     * @param materialCode
     * @return
     */
    public static String loadMaterialPicture(String materialCode) {
        String url= BaseUrl+"api/file/download/material/"+materialCode;
        url+=  ".jpg";
        return  url;
    }
}
