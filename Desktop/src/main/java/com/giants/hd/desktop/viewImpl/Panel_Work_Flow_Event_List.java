package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.WorkFlowEventTableModel;
import com.giants.hd.desktop.presenter.WorkFlowEventConfigPresenter;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.view.WorkFlowEventConfigViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.entity.WorkFlowEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public class Panel_Work_Flow_Event_List extends BasePanel implements WorkFlowEventConfigViewer {
    private JPanel root;
    private JButton btn_add;
    private JHdTable jt;


    WorkFlowEventTableModel model;
    private WorkFlowEventConfigPresenter presenter;

    public Panel_Work_Flow_Event_List(final WorkFlowEventConfigPresenter presenter) {
        this.presenter = presenter;
        this.model = new WorkFlowEventTableModel();
        jt.setModel(model);
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.addOne();
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
                        WorkFlowEvent workFlowWorker = model.getItem(row[0]);
                        presenter.showOne(workFlowWorker);
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
    public void bindData(List<WorkFlowEvent> workFlowWorkers) {
        model.setDatas(workFlowWorkers);
    }
}
