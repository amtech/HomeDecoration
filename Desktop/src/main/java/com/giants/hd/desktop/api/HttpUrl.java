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

    public static final String BaseUrl="http://localhost:8080/";

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
}
