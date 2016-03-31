package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.frames.BaseFrame;
import com.giants.hd.desktop.reports.jasper.ProductPaintReport;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.viewImpl.Panel_Order_Detail;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.google.inject.Inject;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

/**订单详情界面
 * Created by davidleen29 on 2015/8/24.
 */
public class OrderDetailFrame extends BaseFrame  {
    @Inject
    ApiManager apiManager;

    private ErpOrder order;
    private Panel_Order_Detail panelOrderDetail;
    public OrderDetailFrame(ErpOrder order) {
        super( );
        this.order=order;
        setMinimumSize(new Dimension(800, 600));
        setTitle(order==null?"订单详情":"订单："+order.os_no);
        panelOrderDetail=new Panel_Order_Detail(this);
        setContentPane(panelOrderDetail.getRoot());

        panelOrderDetail.setErpOrder(order);
        loadOrderItems(order);
    }




    public void loadOrderItems(ErpOrder order)
    {


        UseCaseFactory.getInstance().createOrderItemListUseCase(order.os_no).execute(new Subscriber<java.util.List<ErpOrderItem>>() {
            @Override
            public void onCompleted() {
                panelOrderDetail.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panelOrderDetail.hideLoadingDialog();
                panelOrderDetail.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(java.util.List<ErpOrderItem> orderItems) {

                panelOrderDetail.setOrderItemList(orderItems);
            }
        });

        panelOrderDetail.showLoadingDialog();
    }


    /**
     * 打印产品油漆数据
     */
    public void printPaint(final ErpOrderItem orderItem) {

        //读取产品详细

        UseCaseFactory.getInstance().createGetProductByPrdNo(orderItem.prd_name).execute(new Subscriber<ProductDetail>() {
            @Override
            public void onCompleted() {
                panelOrderDetail.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panelOrderDetail.hideLoadingDialog();
                panelOrderDetail.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(final ProductDetail productDetail) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        new ProductPaintReport().export(productDetail,order,orderItem);
                    }
                });

            }
        });

        panelOrderDetail.showLoadingDialog();



    }


    /**
     * 显示产品分析表明细
     */
    public void showProductDetail(ErpOrderItem orderItem)
    {
        UseCaseFactory.getInstance().createGetProductByPrdNo(orderItem.prd_name).execute(new Subscriber<ProductDetail>() {
            @Override
            public void onCompleted() {
                panelOrderDetail.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                panelOrderDetail.hideLoadingDialog();
                panelOrderDetail.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(ProductDetail productDetail) {


                HdSwingUtils.showDetailPanel(OrderDetailFrame.this,productDetail);

            }
        });
        panelOrderDetail.showLoadingDialog();
    }
}
