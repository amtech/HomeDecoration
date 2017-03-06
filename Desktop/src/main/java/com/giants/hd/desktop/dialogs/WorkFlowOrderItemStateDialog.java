package com.giants.hd.desktop.dialogs;


import com.giants.hd.desktop.presenter.OrderItemWorkFlowStatePresenter;
import com.giants.hd.desktop.view.OrderItemWorkFlowStateViewer;
import com.giants.hd.desktop.viewImpl.Panel_OrderItemWorkFlow;
import com.giants.hd.desktop.viewImpl.Panel_OrderItemWorkFlowState;
import com.giants3.hd.domain.BaseSubscriber;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.entity.OutFactory;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 订单流程状态界面
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowOrderItemStateDialog extends BaseDialog implements OrderItemWorkFlowStatePresenter {

    private final long orderItemId;
    OrderItemWorkFlowStateViewer workFlowViewer;


    List<OutFactory> outFactories;

    List<OrderItemWorkFlowState> OrderItemWorkFlowStates;



    public WorkFlowOrderItemStateDialog(Window window, long orderItemId) {
        super(window, "订单生产流程配置");
        this.orderItemId = orderItemId;
        workFlowViewer = new Panel_OrderItemWorkFlowState(this);
        setContentPane(workFlowViewer.getRoot());

        setMinimumSize(new Dimension(800, 600));

        readOrderItemState(orderItemId);



    }


    /**
     * 检查产品是否已经配置流程
     */
    private void readOrderItemState(final long orderItemId) {

        UseCaseFactory.getInstance().createGetOrderItemWorkFlowState(orderItemId).execute(new BaseSubscriber<RemoteData<OrderItemWorkFlowState>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                super.onError(e);

                // workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<OrderItemWorkFlowState> data) {


                if (data.isSuccess()  ) {



                    setData(data.datas);

                }



            }

        });


    }

    private void setData(List<OrderItemWorkFlowState> datas) {


        workFlowViewer.setData(datas);
    }


}
