package com.giants3.hd.server.parser;

import com.giants3.hd.appdata.AProduct;
import com.giants3.hd.appdata.AUser;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**  桌面端Product 转换成app 端AProduct数据
 *
 * Created by david on 2016/1/2.
 */

@Component
//默认的 qualifier  为首字母小写的类名  productParser
public class ProductParser implements DataParser<Product,AProduct> {


    @Override
    public AProduct parse(Product data) {
        AProduct aProduct=new AProduct();

        aProduct.name=data.name;
        aProduct.id=data.id;
        aProduct.pVersion=data.pVersion;
        aProduct.pClassName=data.pClassName;
        aProduct.pUnitName=data.pUnitName;
        aProduct.weight=data.weight;
        aProduct.cost=data.cost;
        aProduct.fob
                =data.fob;

        aProduct.price
                =data.price;
        aProduct.url
                =data.url;
        return aProduct;
    }
}
