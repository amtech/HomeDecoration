package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.ErpOrderItemProcessDialog;
import com.giants.hd.desktop.dialogs.WorkFlowConfigDialog;
import com.giants.hd.desktop.dialogs.WorkFlowOrderArrangeDialog;
import com.giants.hd.desktop.presenter.OrderItemForArrangeListPresenter;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.view.OrderItemForArrangeListViewer;
import com.giants.hd.desktop.viewImpl.Panel_OrderItemForArrangeList;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.ModuleConstant;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.ErpOrderItem;
import com.giants3.hd.utils.entity.OrderItemWorkFlow;
import com.giants3.hd.utils.entity.OrderItemWorkFlowState;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 订单排厂列表界面
 * Created by david on 20160925
 */
public class OrderItemForArrangeListFrame extends BaseInternalFrame implements OrderItemForArrangeListPresenter {
    OrderItemForArrangeListViewer viewer;


    java.util.List<OrderItemWorkFlowState> items = null;

    public OrderItemForArrangeListFrame() {
        super(ModuleConstant.TITLE_ORDER_ITEM_FOR_ARRANGE);

        init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                searchUnDoneOrder();
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


    public void searchUnDoneOrder() {


        UseCaseFactory.getInstance().createUnCompleteOrderWorkFlowReportUseCase().execute(new Subscriber<RemoteData<OrderItemWorkFlowState>>() {
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
            public void onNext(RemoteData<OrderItemWorkFlowState> orderReportItemRemoteData) {

                setUnCompleteRemoteData(orderReportItemRemoteData);
            }
        });

        viewer.showLoadingDialog();


    }

    private void setUnCompleteRemoteData(RemoteData<OrderItemWorkFlowState> remoteData) {
        if (remoteData.isSuccess())
            items = remoteData.datas;
        //  viewer.setUnCompleteData(remoteData);
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


//        try {
//            new Report_Excel_StockOutPlan().report(items, file.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//            viewer.showMesssage(e.getMessage());
//        } catch (HdException e) {
//            e.printStackTrace();
//            viewer.showMesssage(e.getMessage());
//        }


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





        //跳转 排厂列表

        ErpOrderItemProcessDialog erpOrderItemProcessDialog;




        if (data.canArrange&&!data.hasProductWorkFlowSet) {

            long
                    productId=data.productId;
            String productName=data.prd_name;
           if( viewer.showConfirmMessage("该产品未设置流程，请前往分析表设置货号："+productName+"的流程信息,马上去设置？"))
           {


               Window window=SwingUtilities.getWindowAncestor(OrderItemForArrangeListFrame.this);
               WorkFlowConfigDialog workFlowProductFrame=new WorkFlowConfigDialog(window,productId,productName);

               workFlowProductFrame.setVisible(true);

           }
            return;
        }
        if (!data.canArrange) {
            viewer.showMesssage("无权限进行排厂");
            return;

        }

        //前往排产界面
        WorkFlowOrderArrangeDialog dialog = new WorkFlowOrderArrangeDialog(SwingUtilities.getWindowAncestor(OrderItemForArrangeListFrame.this), data);
        dialog.setModal(true);
        dialog.setVisible(true);
        OrderItemWorkFlow result = dialog.getResult();
        if (result != null) {

//            finalItem.orderWorkFlowId=result.id;
//            finalItem.workFlowDescribe=result.workFlowDiscribe;workFlowDiscribe


        }





    }
}
