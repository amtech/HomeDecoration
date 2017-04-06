package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.presenter.WorkFlowProductPresenter;
import com.giants.hd.desktop.view.WorkFlowConfigViewer;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlow_Config;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.WorkFlowProduct;
import com.giants3.hd.utils.entity.WorkFlowSubType;
import rx.Subscriber;

import java.awt.*;
import java.util.List;

/**
 * 产品关联的生产流程数据
 * <p/>
 * Created by david on 20160303
 */
public class WorkFlowConfigDialog extends BaseDialog<WorkFlowProduct> implements WorkFlowProductPresenter {
    WorkFlowConfigViewer workFlowViewer;


    private String oldData;
    private WorkFlowProduct data;

    long productId;
    String productName;


    List<WorkFlowSubType> subTypes;

    public WorkFlowConfigDialog(Window window, long productId, String productName) {
        super(window, productName + "产品生产流程配置详情");


        setMinimumSize(new Dimension(800, 600));
        setContentPane(getCustomContentPane());
        this.productId = productId;
        this.productName = productName;
        workFlowViewer.setWorkFlows(CacheManager.getInstance().bufferData.workFlows,CacheManager.getInstance().bufferData.workFlowSubTypes);

        readData();

    }


    public void readData() {

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
                workFlowViewer.hideLoadingDialog();
                if (workFlowRemoteData.isSuccess()) {

                    if (workFlowRemoteData.totalCount > 0)
                        setData(workFlowRemoteData.datas.get(0));
                    else {

                        final WorkFlowProduct data = new WorkFlowProduct();
                        data.productId = productId;
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
        workFlowViewer = new Panel_WorkFlow_Config(this);
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
