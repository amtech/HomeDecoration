package com.giants3.hd.domain.repository;


import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.noEntity.ProductDetail;
import rx.Observable;

import java.util.List;

/**
 * Created by david on 2015/10/6.
 */
public interface ProductRepository {


    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Product}.
     *
     * 获取产品信息 根据 产品名称顺序取值
     *
     */
    Observable<List<Product>> loadByProductNameBetween(String startName,String endName,boolean withCopy);


    Observable<List<Product>> loadByProductNameRandom(String productNames, boolean withCopy);

    Observable<ProductDetail> loadByProductNo(String prdNo);

    Observable<RemoteData<Void>> correctThumbnail(long productId);
}
