package com.giants3.hd.utils.entity;

/**
 * Created by davidleen29 on 2015/8/5.
 */
public class Product2  {

    public Product product;

    public Product2(Product product)
    {
        this.product=product;

    }
    @Override
    public String toString() {
        return product.pVersion;
    }
}
