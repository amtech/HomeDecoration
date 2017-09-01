package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.mvp.presenter.WorkFlowWorkerUpdateIPresenter;
import com.giants.hd.desktop.mvp.viewer.WorkFlowWorkerUpdateViewer;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.WorkFlowWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 流程节点人员改添加 删除
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_WorkFlowWorkerUpdate extends BasePanel implements WorkFlowWorkerUpdateViewer {

    private final WorkFlowWorkerUpdateIPresenter presenter;
    private JPanel root;
    private JButton btn_save;
    private JButton btn_delete;
    private JComboBox cb_workFlow;
    private JCheckBox receive;
    private JCheckBox send;
    private JCheckBox tie;
    private JCheckBox mu;
    private JCheckBox pu;
    private JComboBox cb_user;
    private List<User> users;
    private List<ErpWorkFlow> workFlows;


    public Panel_WorkFlowWorkerUpdate(final WorkFlowWorkerUpdateIPresenter presenter) {
        super();

        this.presenter = presenter;


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.save();

            }
        });

        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.delete();

            }
        });

    }
    @Override
    public void bindWorkFlows(List<ErpWorkFlow> workFlows)
    {
        this.workFlows = workFlows;

        cb_workFlow.setModel(new DefaultComboBoxModel(ArrayUtils.changeListToVector(ErpWorkFlow.WorkFlows)));


    }

    @Override
    public void bindUsers(List<User> users)
    {
        this.users = users;

        cb_user.setModel(new DefaultComboBoxModel(ArrayUtils.changeListToVector(users)));


    }
    @Override
    public void bindData(WorkFlowWorker workFlowWorker) {


        btn_delete.setVisible(workFlowWorker != null && workFlowWorker.id > 0);
         if (workFlowWorker == null) return;

        User selected=null;
        for(User user:users)
        {
            if(user.id==workFlowWorker.userId)
            {
                selected=user;
                break;
            }
        }
        if(selected!=null)
        cb_user.setSelectedItem(selected);


        ErpWorkFlow  selectedWorkFlow=null;
        for(ErpWorkFlow workFlow:workFlows)
        {
            if(workFlow.step==workFlowWorker.workFlowStep)
            {
                selectedWorkFlow=workFlow;
                break;
            }
        }
        if(selectedWorkFlow!=null)
            cb_workFlow.setSelectedItem(selectedWorkFlow);



        receive.setSelected(workFlowWorker.receive);
        send.setSelected(workFlowWorker.send);
        tie.setSelected(workFlowWorker.tie);
        mu.setSelected(workFlowWorker.mu);
        pu.setSelected(workFlowWorker.pu);





    }

    @Override
    public void getData(WorkFlowWorker workFlowWorker) {


     User user= (User) cb_user.getSelectedItem();
        if(user!=null)
        {
            workFlowWorker.userId=user.id;
            workFlowWorker.userName=user.code+user.name+user.chineseName;
        } ErpWorkFlow workFlow= (ErpWorkFlow) cb_workFlow.getSelectedItem();
        if(workFlow!=null)
        {
            workFlowWorker.workFlowCode=workFlow.code;
            workFlowWorker.workFlowStep=workFlow.step;
            workFlowWorker.workFlowName=workFlow.name;
        }
        workFlowWorker.receive=receive.isSelected();
        workFlowWorker.send=send.isSelected();
        workFlowWorker.tie=tie.isSelected();
        workFlowWorker.mu=mu.isSelected();
        workFlowWorker.pu=pu.isSelected();
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
}
