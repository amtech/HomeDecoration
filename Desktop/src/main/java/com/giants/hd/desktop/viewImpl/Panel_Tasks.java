package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.frames.TaskListInternalFrame;
import com.giants.hd.desktop.model.HdTaskModel;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.entity.HdTask;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

/** 任务列表面板
 * Created by david on 2015/12/11.
 */
public class Panel_Tasks  extends BasePanel{
    private JHdTable jtable;
    private JButton add;
    private JButton delete;
    private JPanel root;
    private JComboBox cbTaskType;
    private JSpinner timePicker;
    private JTextField memo;
    @Inject
    HdTaskModel taskModel;



    @Inject
    ApiManager apiManager;
    private TaskListInternalFrame frame;


      int[] taskTypes=new int[]{HdTask.TYPE_SYNC_ERP};
      String[] taskTypeNames=new String[]{HdTask.NAME_SYNC_ERP};
    @Override
    public JComponent getRoot() {
        return root;
    }
    public Panel_Tasks(final TaskListInternalFrame frame) {
        super();
        this.frame = frame;
        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtable.setModel(taskModel);


        timePicker.setModel(new SpinnerDateModel());

        final JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timePicker, "yyyy-MM-dd HH:mm");
        timePicker.setEditor(timeEditor);


        for(String typeName:taskTypeNames)
        {
            cbTaskType.addItem(typeName);
        }

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(cbTaskType.getSelectedItem()==null)
                {
                    showMesssage("请选择一个任务类型");
                    return;
                }

                Date date;
                try {
                      date = (Date) timePicker.getValue();
                }catch (Throwable t)
                {
                    showMesssage("时间格式输入不正确");
                    return;

                }


                if(date==null||date.before(new Date()))
                {

                    showMesssage( "时间不能为过去时刻");
                    return;
                }



                HdTask task=new HdTask();
                int selectIndex=cbTaskType.getSelectedIndex();
                task.taskType=taskTypes[selectIndex];
                task.taskName=taskTypeNames[selectIndex];
                task.date=date.getTime();
                task.memo=memo.getText().trim();
                task.dateString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(date);


                frame.addHdTask(task);




            }
        });



        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               int[]  modelRow= JTableUtils.getSelectedRowSOnModel(jtable);
                if(modelRow.length==0)
                {
                    showMesssage("请选择一条任务进行删除。");
                    return;
                }


                if(showConfirmMessage("确定删除选中任务？")) {


                    HdTask item = taskModel.getItem(modelRow[0]);
                    frame.deleteHdTask(item.id
                    );

                }


            }
        });


    }


    /**
     * 数据绑定
     * @param tasks
     */
    public void setData(List<HdTask> tasks)
    {

        taskModel.setDatas(tasks);
    }
}
