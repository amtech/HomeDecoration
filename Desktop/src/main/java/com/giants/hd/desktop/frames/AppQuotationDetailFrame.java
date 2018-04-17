package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.AppQuotationDetailPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationDetailViewer;
import com.giants.hd.desktop.viewImpl.Panel_AppQuotation_Detail;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.app.QuotationDetail;

import java.awt.*;
import java.util.ArrayList;

/**
 * 订单详情界面
 * Created by davidleen29 on 2015/8/24.
 */
public class AppQuotationDetailFrame extends BaseMVPFrame<AppQuotationDetailViewer> implements AppQuotationDetailPresenter {



    private QuotationDetail quotationDetail;

    public AppQuotationDetailFrame(Quotation quotation) {
        super("广交会报价单:" + quotation.qNumber);
        initPanel(quotation);

    }

    @Override
    protected AppQuotationDetailViewer createViewer() {
        return new Panel_AppQuotation_Detail(this);
    }


    private void initPanel(Quotation quotation) {

        setMinimumSize(new Dimension(1080, 800));
        getViewer().bindData(quotation);
        loadDetail(quotation);
    }


    @Override
    public boolean hasModifyData() {

        if (getViewer() == null || quotationDetail == null) {
            return false;
        }

        return false;
    }

    public void loadDetail(Quotation quotation) {


        UseCaseFactory.getInstance().createGetAppQuotationDetailUseCase(quotation.id, quotation.qNumber).execute(new RemoteDataSubscriber<QuotationDetail>(getViewer()) {

            @Override
            protected void handleRemoteData(RemoteData<QuotationDetail> data) {
                getViewer().bindDetail(data.datas.get(0));
            }
        });

       // getViewer().showLoadingDialog();
    }


}



