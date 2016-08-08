package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.presenter.OrderReportPresenter;
import com.giants.hd.desktop.view.OrderReportViewer;
import com.giants.hd.desktop.viewImpl.Panel_Order_List;
import com.giants.hd.desktop.viewImpl.Panel_Order_Report;
import com.giants.hd.desktop.viewImpl.Panel_Stock_List;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Module;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;

/** 订单报表业务逻辑层
 * Created by david on 2015/11/23.
 */
public class OrderReportInternalFrame extends BaseInternalFrame implements OrderReportPresenter{
    OrderReportViewer orderReportViewer;
    public OrderReportInternalFrame( ) {
        super(Module.TITLE_ORDER_REPORT);

        init();

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                search("",,0,20);
//            }
//        });

    }
    public void init() {

//        setMinimumSize(new Dimension(1024, 768));
//        pack();
    }


    @Override
    protected Container getCustomContentPane() {
        orderReportViewer=new Panel_Order_Report( this);
        return orderReportViewer.getRoot();
    }
    @Override
    public void search(String key, String dateStart,String dateEnd, int pageIndex, int pageSize) {





        UseCaseFactory.getInstance().createOrderReportUseCase(key,dateStart,dateEnd,pageIndex,pageSize).execute(new Subscriber<RemoteData<ErpOrder>>() {
            @Override
            public void onCompleted() {
                orderReportViewer.hideLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                orderReportViewer.hideLoadingDialog();
                orderReportViewer.showMesssage(e.getMessage());
            }

            @Override
            public void onNext(RemoteData<ErpOrder> erpOrderRemoteData) {

                orderReportViewer.setData(erpOrderRemoteData);
            }
        });

        orderReportViewer.showLoadingDialog();


    }

    /**
     * 订单详情读取
     * @param erpOrder
     */
    @Override
    public void loadOrderDetail(ErpOrder erpOrder) {


        Frame frame=new OrderDetailFrame(erpOrder);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);






    }


}
