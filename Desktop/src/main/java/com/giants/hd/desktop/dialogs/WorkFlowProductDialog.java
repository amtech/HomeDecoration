package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.presenter.WorkFlowProductPresenter;
import com.giants.hd.desktop.view.WorkFlowProductViewer;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlow_Product;
import com.giants3.hd.domain.BaseSubscriber;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowProduct;
import com.giants3.hd.utils.entity.WorkFlowSubType;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 产品关联的生产流程数据
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowProductDialog extends BaseDialog<WorkFlowProduct> implements WorkFlowProductPresenter {
    WorkFlowProductViewer workFlowViewer;


    private String oldData;
    private WorkFlowProduct data;

    long productId;
    String productName;


    List<WorkFlowSubType> subTypes;

    public WorkFlowProductDialog(Window window, long productId, String productName) {
        super(window, productName + "产品生产流程配置详情");
        setContentPane(getCustomContentPane());
        this.productId = productId;
        this.productName = productName;

        setMinimumSize(new Dimension(800,600));
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {

                readData();
//            }
//        });

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                loadUsers();
//            }
//        });


//     readSubTypeData();


    }

    private void readSubTypeData() {
        UseCaseFactory.getInstance().createGetWorkFlowSubTypeUseCase().execute(new BaseSubscriber<RemoteData<WorkFlowSubType>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                super.onError(e);

               // workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(RemoteData<WorkFlowSubType> data) {


//                if(data.isSuccess())
//                workFlowViewer.setSubData(data.datas);

            }

        });

    }

    private void loadUsers() {

        UseCaseFactory.getInstance().createGetUserListUseCase().execute(new Subscriber<List<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                workFlowViewer.showMesssage(e.getMessage());

            }


            @Override
            public void onNext(List<User> users) {


                //  workFlowViewer.setUserList(users);

            }

        });

    }


    private void readData() {

        UseCaseFactory.getInstance().createGetWorkFlowOfProduct(productId).execute(new Subscriber<RemoteData<WorkFlowProduct>>() {
            @Override
            public void onCompleted() {
                workFlowViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<WorkFlowProduct> workFlowRemoteData) {

                if (workFlowRemoteData.isSuccess()) {

                    if (workFlowRemoteData.totalCount > 0)
                        setData(workFlowRemoteData.datas.get(0));
                    else {

                        final WorkFlowProduct data = new WorkFlowProduct();
                        data.productId=productId;
                        setData(data);
                        workFlowViewer.showMesssage("该产品未建立生产进度信息");
                        return;
                    }
                }


            }


        });
        workFlowViewer.showLoadingDialog();
    }


    public void setData(WorkFlowProduct datas) {
        oldData = GsonUtils.toJson(datas);
        this.data = datas;
         workFlowViewer.setData(datas);
    }


    protected Container getCustomContentPane() {
        workFlowViewer = new Panel_WorkFlow_Product(this);
        return workFlowViewer.getRoot();
    }


    @Override
    public void save() {



        workFlowViewer.getData(data);


        if (!hasModifyData()) {
            workFlowViewer.showMesssage("数据无改动");
            return;
        }

        UseCaseFactory.getInstance().createSaveWorkProductUseCase(data).execute(new Subscriber<RemoteData<WorkFlowProduct>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                workFlowViewer.hideLoadingDialog();
                workFlowViewer.showMesssage(e.getMessage());
            }


            @Override
            public void onNext(RemoteData<WorkFlowProduct> workFlowRemoteData) {
                workFlowViewer.hideLoadingDialog();
                 if (workFlowRemoteData.isSuccess()) {
                     final WorkFlowProduct workFlowProduct = workFlowRemoteData.datas.get(0);
                     setData(workFlowProduct);
                     setResult(workFlowProduct);
                 workFlowViewer.showMesssage("保存成功");
                     dispose();

              }


            }


        });
        workFlowViewer.showLoadingDialog();
    }

    @Override
    public boolean hasModifyData() {

        return data != null && !GsonUtils.toJson(data).equals(oldData);
    }
}
