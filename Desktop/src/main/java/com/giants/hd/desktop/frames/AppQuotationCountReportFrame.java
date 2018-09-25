package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.mvp.RemoteDataSubscriber;
import com.giants.hd.desktop.mvp.presenter.AppQuotationCountReportPresenter;
import com.giants.hd.desktop.mvp.viewer.AppQuotationCountReportViewer;
import com.giants.hd.desktop.reports.excels.MapTableToExcelReporter;
import com.giants.hd.desktop.viewImpl.Panel_AppQuotation_Count_Report;
import com.giants.hd.desktop.viewImpl.Panel_AppQuotation_Report;
import com.giants3.hd.domain.interractor.UseCase;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 2015/11/23.
 */
public class AppQuotationCountReportFrame extends BaseMVPFrame<AppQuotationCountReportViewer> implements AppQuotationCountReportPresenter {



    private  RemoteData<Map> mData;
    public AppQuotationCountReportFrame() {
        super("区间报价次数报表");


    }





    @Override
    protected AppQuotationCountReportViewer createViewer() {
        return new Panel_AppQuotation_Count_Report(this);
    }

    @Override
    public void searchReport(String startDate, String endDate) {

        final UseCase appQuoteCountReportUseCase = UseCaseFactory.getInstance().createAppQuoteCountReportUseCase(startDate, endDate);
        appQuoteCountReportUseCase.execute(new RemoteDataSubscriber<Map>(getViewer()) {
            @Override
            protected void handleRemoteData(RemoteData<Map> data) {

                if(data.isSuccess())
                {
                    mData=data;

                    getViewer().bindData(data);

                }




            }


        });


        getViewer().showLoadingDialog();
    }

    @Override
    public void exportExcel(List<TableField> fieldList, String directory, String fileName) {

        if(mData==null||mData.datas.size()==0) {
            getViewer().showMesssage("无数据可导出");
            return ;
        }
        MapTableToExcelReporter tableToExcelReporter=new MapTableToExcelReporter(fileName,fieldList );
        try {
            tableToExcelReporter.report(mData.datas,directory);
            getViewer().showMesssage("导出成功");
        } catch (Throwable e) {

            e.printStackTrace();
            getViewer().showError(e.getMessage());
        }



    }
}
