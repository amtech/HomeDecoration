package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.presenter.OrderItemWorkFlowPresenter;
import com.giants.hd.desktop.view.OrderItemWorkFlowViewer;
import com.giants.hd.desktop.viewImpl.Panel_OrderItemWorkFlow;
import com.giants3.hd.domain.BaseSubscriber;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * d订单排产界面
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowOrderArrangeDialog extends BaseDialog<OrderItemWorkFlow> implements OrderItemWorkFlowPresenter {
    private final ErpOrderItem erpOrderItem;
    OrderItemWorkFlowViewer workFlowViewer;


    private String oldData;
    private WorkFlowProduct data;


    List<OutFactory> outFactories;

    List<WorkFlowSubType> subTypes;
    private WorkFlowProduct workFlowProduct;
    OrderItemWorkFlow orderItemWorkFlow;

    public WorkFlowOrderArrangeDialog(Window window, ErpOrderItem erpOrderItem) {
        super(window, "订单生产流程配置");
        this.erpOrderItem = erpOrderItem;
        setContentPane(getCustomContentPane());

        setMinimumSize(new Dimension(800, 600));
        orderItemWorkFlow = new OrderItemWorkFlow();
        orderItemWorkFlow.orderId = 0;
        orderItemWorkFlow.orderName = erpOrderItem.os_no;
        orderItemWorkFlow.orderItemId = erpOrderItem.id;
        orderItemWorkFlow.orderItemIndex = erpOrderItem.itm;
        readOutFactoryData();

        checkProductConfig(erpOrderItem.productId, erpOrderItem.prd_name);



    }


    /**
     * 检查产品是否已经配置流程
     */
    private void checkProductConfig(final long productId, final String productName) {

        UseCaseFactory.getInstance().createGetWorkFlowOfProduct(productId).execute(new BaseSubscriber<RemoteData<WorkFlowProduct>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                super.onError(e);

                // workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<WorkFlowProduct> data) {

                WorkFlowProduct workFlowProduct = null;
                if (data.isSuccess() && data.datas.size() > 0) {

                    workFlowProduct = data.datas.get(0);

                }


                if (workFlowProduct.id <= 0) {


                    //显示产品流程配置界面

                    WorkFlowProductDialog workFlowProductFrame = new WorkFlowProductDialog(WorkFlowOrderArrangeDialog.this, productId, productName);
                    workFlowProductFrame.setModal(true);
                    workFlowProductFrame.setVisible(true);
                    workFlowProduct = workFlowProductFrame.getResult();
                    if (workFlowProduct == null) {

                        setVisible(false);

                    }

                }

                setWorkFlowProduct(workFlowProduct);


            }

        });


    }

    private void setWorkFlowProduct(WorkFlowProduct workFlowProduct) {

        this.workFlowProduct = workFlowProduct;


        orderItemWorkFlow.workFlowIds = workFlowProduct.workFlowIds;
        orderItemWorkFlow.workFlowNames = workFlowProduct.workFlowNames;
        orderItemWorkFlow.workFlowTypes = workFlowProduct.workFlowTypes;
        orderItemWorkFlow.productTypes = workFlowProduct.productTypes;
        orderItemWorkFlow.productTypeNames = workFlowProduct.productTypeNames;






        final String[] productTypes=orderItemWorkFlow.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        final String[] productTypeNames=orderItemWorkFlow.productTypeNames.split(ConstantData.STRING_DIVIDER_SEMICOLON);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                workFlowViewer.setProductTypes(productTypes,productTypeNames,outFactories);
            }
        });

    }


    /**
     * 读取外厂家数据
     */
    private void readOutFactoryData() {

        UseCaseFactory.getInstance().createGetOutFactoryUseCase().execute(new Subscriber<RemoteData<OutFactory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<OutFactory> data) {


                if (data.isSuccess()) {
                    outFactories = data.datas;
                }
            }

        });

    }


    private void readData() {

//        UseCaseFactory.getInstance().createGetWorkFlowOfProduct(productId).execute(new Subscriber<RemoteData<WorkFlowProduct>>() {
//            @Override
//            public void onCompleted() {
//                workFlowViewer.hideLoadingDialog();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                workFlowViewer.hideLoadingDialog();
//                workFlowViewer.showMesssage(e.getMessage());
//            }
//
//
//            @Override
//            public void onNext(RemoteData<WorkFlowProduct> workFlowRemoteData) {
//
//                if (workFlowRemoteData.isSuccess()) {
//
//                    if (workFlowRemoteData.totalCount > 0)
//                        setData(workFlowRemoteData.datas.get(0));
//                    else {
//
//                        final WorkFlowProduct data = new WorkFlowProduct();
//                        data.productId = productId;
//                        setData(data);
//                        workFlowViewer.showMesssage("该产品未建立生产进度信息");
//                        return;
//                    }
//                }
//
//
//            }
//
//
//        });
//        workFlowViewer.showLoadingDialog();
    }


    public void setData(WorkFlowProduct datas) {
        oldData = GsonUtils.toJson(datas);
        this.data = datas;
        // workFlowViewer.setData(datas);
    }


    protected Container getCustomContentPane() {
        workFlowViewer = new Panel_OrderItemWorkFlow(this);
        return workFlowViewer.getRoot();
    }


//    @Override
//    public void save() {
//
//
//        workFlowViewer.getData(data);
//
//
//        if (!hasModifyData()) {
//            workFlowViewer.showMesssage("数据无改动");
//            return;
//        }
//
//        UseCaseFactory.getInstance().createSaveWorkProductUseCase(data).execute(new Subscriber<RemoteData<WorkFlowProduct>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                workFlowViewer.hideLoadingDialog();
//                workFlowViewer.showMesssage(e.getMessage());
//            }
//
//
//            @Override
//            public void onNext(RemoteData<WorkFlowProduct> workFlowRemoteData) {
//                workFlowViewer.hideLoadingDialog();
//                if (workFlowRemoteData.isSuccess()) {
//                    setData(workFlowRemoteData.datas.get(0));
//                    workFlowViewer.showMesssage("保存成功");
//                }
//
//
//            }
//
//
//        });
//        workFlowViewer.showLoadingDialog();
//    }

    @Override
    public boolean hasModifyData() {

        return data != null && !GsonUtils.toJson(data).equals(oldData);
    }

    @Override
    public void save() {


        if(orderItemWorkFlow==null) return;
        if(orderItemWorkFlow.productTypes==null)
            return ;
        List<OutFactory> factories;
        try {
            factories=workFlowViewer.getArrangedFactories();
        } catch (Exception e) {
            e.printStackTrace();
            workFlowViewer.showMesssage(e.getMessage());
            return ;
        }


        String[] productTypes=orderItemWorkFlow.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        if(factories==null||factories.size()!=productTypes.length)
        {
            workFlowViewer.showMesssage("数据异常");
            return;

        }


        StringBuilder ids=new StringBuilder();
        StringBuilder names=new StringBuilder();
        int size=productTypes.length;
        for(int i=0;i<size;i++)
        {
            OutFactory outFactory=factories.get(i);
            ids.append(outFactory.id).append(ConstantData.STRING_DIVIDER_SEMICOLON);

            names.append(outFactory.name).append(ConstantData.STRING_DIVIDER_SEMICOLON);


        }


        if(size>0)
        {
            ids.setLength(ids.length()-1);
            names.setLength(names.length()-1);
        }


        orderItemWorkFlow.productFactoryIds=ids.toString();
        orderItemWorkFlow.productFactoryNames=names.toString();




        UseCaseFactory.getInstance().createStartOrderItemWorkFlowUseCase(orderItemWorkFlow).execute(new Subscriber<RemoteData<OrderItemWorkFlow>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<OrderItemWorkFlow> data) {

                workFlowViewer.hideLoadingDialog();

                if(data.isSuccess()&&data.datas.size()>0)
                {
                    setResult(data.datas.get(0));

                    workFlowViewer.showMesssage("排厂成功！");
                    setVisible(false);

                }

            }

        });




        workFlowViewer.showLoadingDialog("正在启动订单生产流程。。。");





    }
}
