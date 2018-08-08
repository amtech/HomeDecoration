package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.model.WorkFlowMessageModel;
import com.giants.hd.desktop.model.WorkFlowTimeLimitModel;
import com.giants.hd.desktop.mvp.presenter.WorkFlowMessageReportPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowEventConfigViewer;
import com.giants.hd.desktop.mvp.viewer.WorkFlowMessageReportViewer;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.WorkFlowMessage;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by davidleen29 on 2018/7/7.
 */
public class Panel_Work_Flow_Message_Report  extends BasePanel implements WorkFlowMessageReportViewer {

    private JPanel root;
    private JHdTable jt;
    private WorkFlowMessageReportPresenter presenter;
    WorkFlowMessageModel model;

    public Panel_Work_Flow_Message_Report(WorkFlowMessageReportPresenter presenter) {
        this.presenter = presenter;

        model=new WorkFlowMessageModel();
        jt.setModel(model);




        jt.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = jt.getSelectedRow();
                    WorkFlowMessage mesage = model.getItem(row);


                    int column = jt.convertColumnIndexToModel(jt.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 0) {
                        ImageViewDialog.showProductDialog(getWindow(getRoot()), mesage.productName, mesage.pVersion,mesage.url);
                    }


                }

            }
        });
    }

    @Override
    public JComponent getRoot() {
        return root;
    }

    @Override
    public void bindData(List<WorkFlowMessage> datas) {

        model.setDatas(datas);
    }
}
