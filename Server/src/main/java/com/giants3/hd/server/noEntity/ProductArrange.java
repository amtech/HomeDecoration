package com.giants3.hd.server.noEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 产品排厂数据
 *
 * 一个产品可能有多个排厂类型
 *
 * 铁木pu， 也有可能是混合类型
 * Created by davidleen29 on 2016/12/24.
 */
public class ProductArrange {


    long productId;
    String productName;

    List<ArrangeType>  types;
}
