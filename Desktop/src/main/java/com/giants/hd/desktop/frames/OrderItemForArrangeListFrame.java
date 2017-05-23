package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.ErpOrderItemStateDialog;
import com.giants.hd.desktop.mvp.presenter.OrderItemForArrangeListIPresenter;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.mvp.viewer.OrderItemForArrangeListViewer;
import com.giants.hd.desktop.viewImpl.Panel_OrderItemForArrangeList;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.ModuleConstant;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.ErpOrderItem;
import com.giants3.hd.utils.entity.OrderItemWorkFlowState;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 订单排厂列表界面
 * Created by david on 20160925
 */
public class OrderItemForArrangeListFrame extends BaseInternalFrame implements OrderItemForArrangeListIPresenter {
    OrderItemForArrangeListViewer viewer;


    java.util.List<OrderItemWorkFlowState> items = null;

    public OrderItemForArrangeListFrame() {
        super(ModuleConstant.TITLE_ORDER_ITEM_FOR_ARRANGE);

        init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                search("",0,20);
            }
        });

    }

    public void init() {

//        setMinimumSize(new Dimension(1024, 768));
//        pack();
    }


    @Override
    protected Container getCustomContentPane() {
        viewer = new Panel_OrderItemForArrangeList(this);
        return viewer.getRoot();
    }





    /**
     * 导出报表
     */

    public void export() {

        if (items == null) {
            viewer.showMesssage("无数据导出");
            return;
        }

        final File file = SwingFileUtils.getSelectedDirectory();
        if (file == null) return;



    }


    @Override
    public void search(String key, int pageIndex, int pageSize) {


        UseCaseFactory.getInstance().searchOrderItemListUseCase(key, pageIndex, pageSize).execute(new Subscriber<RemoteData<ErpOrderItem>>() {
            @Override
            public void onCompleted() {
                viewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                viewer.hideLoadingDialog();
                viewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ErpOrderItem> orderReportItemRemoteData) {

                viewer.setData(orderReportItemRemoteData);
            }
        });

        viewer.showLoadingDialog();
    }


    @Override
    public void search(String key, long salesId, int pageIndex, int pageSize) {

    }

    @Override
    public void onListItemClick(ErpOrderItem data) {





        Window window= SwingUtilities.getWindowAncestor(this);
        ErpOrderItemStateDialog dialog=new ErpOrderItemStateDialog(window,data.os_no,data.prd_no);
        dialog.setVisible(true);








    }
}
