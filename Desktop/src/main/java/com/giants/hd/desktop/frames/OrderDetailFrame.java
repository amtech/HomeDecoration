package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.presenter.OrderDetailPresenter;
import com.giants.hd.desktop.reports.jasper.ProductPaintReport;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.view.OrderDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_Order_Detail;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.ProductDetail;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * 订单详情界面
 * Created by davidleen29 on 2015/8/24.
 */
public class OrderDetailFrame extends BaseFrame implements OrderDetailPresenter {

    java.util.List<String> attachStrings = new ArrayList<>();

    private ErpOrderDetail orderDetail;
    private String oldData;
    private OrderDetailViewer orderDetailViewer;
    public OrderDetailFrame(String orderNo  ) {
        super();
        initPanel(orderNo);

    }
    public OrderDetailFrame(ErpOrder order) {
        super();
        setTitle(order == null ? "订单详情" : "订单：" + order.os_no);
        initPanel(order.os_no);

    }
    private void initPanel(String os_no)
    {
        setMinimumSize(new Dimension(1080, 800));
        orderDetailViewer = new Panel_Order_Detail(this);
        setContentPane(orderDetailViewer.getRoot());



        //设置权限相关
        orderDetailViewer.setEditable(AuthorityUtil.getInstance().editStockOut());

        loadOrderDetail(os_no);
    }


    @Override
    public boolean hasModifyData() {

        if (orderDetailViewer == null || orderDetail == null) {
            return false;
        }


        String newAttachString = StringUtils.combine(attachStrings);
        if (!StringUtils.isEmpty(newAttachString))
            orderDetail.erpOrder.attaches = newAttachString;
        return !GsonUtils.toJson(orderDetail).equals(oldData) ;
    }

    public void loadOrderDetail(String os_no) {


        UseCaseFactory.getInstance().createOrderDetailUseCase(os_no).execute(new Subscriber<ErpOrderDetail>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(ErpOrderDetail orderDetail) {
                setOrderDetail(orderDetail);

            }
        });

        orderDetailViewer.showLoadingDialog();
    }


    private void setOrderDetail(ErpOrderDetail orderDetail) {
        setTitle(orderDetail == null ||orderDetail.erpOrder==null? "订单详情" : "订单：" + orderDetail.erpOrder.os_no);
        oldData = GsonUtils.toJson(orderDetail);
        this.orderDetail = orderDetail;

        orderDetailViewer.setOrderDetail(orderDetail);
        attachStrings = StringUtils.isEmpty(orderDetail.erpOrder.attaches) ? new ArrayList<String>() : (ArrayUtils.changeArrayToList(orderDetail.erpOrder.attaches.split(";")));
        orderDetailViewer.showAttachFiles(attachStrings);

    }

    /**
     * 打印产品油漆数据
     */
    @Override
    public void printPaint(final ErpOrderItem orderItem) {

        //读取产品详细

        UseCaseFactory.getInstance().createGetProductByPrdNo(orderItem.prd_name).execute(new Subscriber<ProductDetail>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(final ProductDetail productDetail) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        new ProductPaintReport().export(productDetail, orderDetail.erpOrder, orderItem);
                    }
                });

            }
        });

        orderDetailViewer.showLoadingDialog();


    }


    /**
     * 显示产品分析表明细
     */
    @Override
    public void showProductDetail(ErpOrderItem orderItem) {
        UseCaseFactory.getInstance().createGetProductByPrdNo(orderItem.prd_name).execute(new Subscriber<ProductDetail>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(ProductDetail productDetail) {


                HdSwingUtils.showDetailPanel(OrderDetailFrame.this, productDetail);

            }
        });
        orderDetailViewer.showLoadingDialog();
    }

    @Override
    public void save() {

        orderDetail.erpOrder.attaches = StringUtils.combine(attachStrings);
        UseCaseFactory.getInstance().saveOrderDetail(orderDetail).execute(new Subscriber<RemoteData<ErpOrderDetail>>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();

            }

            @Override
            public void onError(Throwable e) {

                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());

            }

            @Override
            public void onNext(RemoteData<ErpOrderDetail> remoteData) {
                orderDetailViewer.hideLoadingDialog();
                if (remoteData.isSuccess()) {
                    setOrderDetail(remoteData.datas.get(0));

                    orderDetailViewer.showMesssage("保存成功!");
                } else {

                    orderDetailViewer.showMesssage("保存失败!" + remoteData.message);
                }


            }
        });

        orderDetailViewer.showLoadingDialog("正在保存。。。");

    }

    @Override
    public void delete() {

    }

    @Override
    public void verify() {

    }


    @Override
    public void addPictureClick(File[] file) {


        //上传图片
        UseCaseFactory.getInstance().uploadTempFileUseCase(file).execute(new Subscriber<java.util.List<String>>() {
            @Override
            public void onCompleted() {
                orderDetailViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderDetailViewer.hideLoadingDialog();
                orderDetailViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(java.util.List<String> stringList) {
                attachStrings.addAll(stringList);
                orderDetailViewer.showAttachFiles(attachStrings);
            }
        });
        orderDetailViewer.showLoadingDialog("正在上传图片。。。");


    }


    @Override
    public void onCemaiChange(String value) {

        if (orderDetail == null || orderDetail.erpOrder == null) return;
        orderDetail.erpOrder.cemai = value;
    }

    @Override
    public void onNeihemaiChange(String value) {
        if (orderDetail == null || orderDetail.erpOrder == null) return;

        orderDetail.erpOrder.neheimai = value;

    }

    @Override
    public void onZhengmaiChange(String value) {
        if (orderDetail == null || orderDetail.erpOrder == null) return;

        orderDetail.erpOrder.zhengmai = value;

    }

    @Override
    public void onMemoChange(String value) {
        if (orderDetail == null || orderDetail.erpOrder == null) return;

        orderDetail.erpOrder.memo = value;


    }

    @Override
    public void onDeleteAttach(String url) {
        attachStrings.remove(url);
    }


}
