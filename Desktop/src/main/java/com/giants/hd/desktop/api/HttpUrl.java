package com.giants.hd.desktop.api;

/**
 *wangl
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
    public static String loadProductPicture(String productName) {
        return BaseUrl+"api/file/download/product/"+productName+".jpg";
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

        return BaseUrl+"api/material/search?codeOrName="+value+"&pageIndex="+pageIndex+"&pageSize="+pageSize;

    }
}
