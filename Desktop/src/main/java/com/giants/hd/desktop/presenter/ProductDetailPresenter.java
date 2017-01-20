package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.noEntity.ProductDetail;

import java.io.File;

/**
 * presenter layer of MVP  订单详情界面
 */
public interface ProductDetailPresenter extends  Presenter {


    void addPackagePicture(File[] selectedFiles);

    void save(ProductDetail productDetail);

    void showProductWorkFlow();
}
