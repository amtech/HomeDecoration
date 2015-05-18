package com.giants.hd.desktop.api;

/**
 *wangl
 */
public class HttpUrl {



    public static  final String URL_ENCODING="UTF-8";

    public static final String BaseUrl="http://localhost:8080/";

    public static String readProductList(String productName, int pageIndex, int pageSize) {
        return BaseUrl+"api/product/search/"+productName+"?pageIndex="+pageIndex+",pageSize="+pageSize;
    }
}
