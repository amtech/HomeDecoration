package com.giants.hd.desktop.view;

import com.giants.hd.desktop.model.MaterialTableModel;
import com.giants.hd.desktop.model.ProductProcessModel;
import com.giants3.hd.utils.entity.ProductProcess;
import com.google.inject.Inject;

import javax.swing.*;
import java.util.List;

/**
 * 工序列表
 */
public class Panel_ProductProcess extends BasePanel {
    private JPanel root;
    public  JTable jt_process;
    public JButton btn_save;

    @Inject
   public ProductProcessModel productProcessModel;
    public Panel_ProductProcess() {



        jt_process.setModel(productProcessModel);


    }

    @Override
    public JComponent getRoot() {
        return root;
    }

    public void setData(List<ProductProcess> datas) {

        productProcessModel.setDatas(datas);
        jt_process.setModel(productProcessModel);


    }
}
