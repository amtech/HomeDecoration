package com.giants3.hd.noEntity;

import com.giants3.hd.entity.Product;

/**
 * Created by davidleen29 on 2018/4/14.
 */
public class ProductAgent {

    public static String getProductFullPackageInfo(Product product)
    {
       return product.insideBoxQuantity+"/"+product.packQuantity+"/"+product.packLong+"*"+product.packWidth+"*"+product.packHeight;

    }
}
