package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants.hd.desktop.model.WorkFlowModel;
import com.giants.hd.desktop.presenter.WorkFlowPresenter;
import com.giants.hd.desktop.view.WorkFlowViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 生产流程 界面层实现
 * Created by davidleen29 on 2016/9/17.
 */
public class Panel_Work_Flow extends BasePanel implements WorkFlowViewer {
    private JComponent rootPanel;
    private JButton save;
    private JHdTable tb;
    private JButton saveType;
    private JHdTable jt_arrageType;


    WorkFlowModel workFlowModel;
    WorkFlowPresenter presenter;

    public Panel_Work_Flow(final WorkFlowPresenter presenter) {
        this.presenter = presenter;
        workFlowModel = new WorkFlowModel();
        workFlowModel.setRowAdjustable(false);
        tb.setModel(workFlowModel);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.save( );
            }
        });



//
    }








    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return rootPanel;
    }

    @Override
    public void setData(List<WorkFlow> datas) {

        workFlowModel.setDatas(datas);
    }

    @Override
    public void setUserList(List<User> users) {

        JComboBox<User> comboBox = new JComboBox<>();
        User empty =new User();
        comboBox.addItem(empty);
        for (User user : users)
            comboBox.addItem(user);
        DefaultCellEditor comboboxEditor = new DefaultCellEditor(comboBox);
        tb.setDefaultEditor(User.class , comboboxEditor);

    }
}
