package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.WorkFlowTimeLimitModel;
import com.giants.hd.desktop.mvp.presenter.WorkFlowLimitConfigIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowLimitConfigViewer;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.entity.WorkFlowTimeLimit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public class Panel_Work_Flow_Limit extends BasePanel implements WorkFlowLimitConfigViewer {
    private JPanel root;
    private JButton btn_save;
    private JHdTable jt;
    private JCheckBox cb_updateComplete;


    WorkFlowTimeLimitModel model;

    private WorkFlowLimitConfigIPresenter presenter;

    public Panel_Work_Flow_Limit(final WorkFlowLimitConfigIPresenter presenter) {

        this.presenter = presenter;
        this.model = new WorkFlowTimeLimitModel();


        jt.setModel(model);
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean updateComplete=cb_updateComplete.isSelected();
                presenter.save(updateComplete);
            }
        });
        jt.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    int[] row = JTableUtils.getSelectedRowSOnModel(jt);
                    if (row != null && row.length > 0) {

                    }
                }
            }
        });
    }

    /**
     * 获取实际控件
     *
     * @return
     */
    @Override
    public JComponent getRoot() {
        return root;
    }

    @Override
    public void bindData(java.util.List<WorkFlowTimeLimit> flowLimit) {
        model.setDatas(flowLimit);

    }
}
