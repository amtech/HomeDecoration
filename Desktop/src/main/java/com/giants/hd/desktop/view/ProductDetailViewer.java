package com.giants.hd.desktop.view;

import com.giants3.hd.utils.noEntity.ProductDetail;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Interface representing a View that will use to load data.
 */
public interface ProductDetailViewer extends AbstractViewer {


    /**
     * 显示包装附件列表
     * @param attachStrings
     */
    void showPackAttachFiles(List<String> attachStrings);


          void setProductDetail(ProductDetail productDetail);
}
