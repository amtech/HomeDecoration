package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.presenter.WorkFlowProductPresenter;
import com.giants.hd.desktop.view.WorkFlowProductViewer;
import com.giants.hd.desktop.widget.VerticalFlowLayoutManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowProduct;
import com.giants3.hd.utils.entity.WorkFlowSubType;
import com.sun.java.swing.SwingUtilities3;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品关联的生产流程配置信息
 * <p/>
 * Created by davidleen29 on 2017/1/4.
 */
public class Panel_WorkFlow_Product extends BasePanel implements WorkFlowProductViewer {
    private JPanel root;
    private JPanel subtypes;
    private JPanel panel_work_flow;
    private JButton save;
    private WorkFlowProductPresenter presenter;


    private List<WorkFlowSubType> workFlowSubTypes;
    private List<JCheckBox> workFlowSubTypeChecks;


    private WorkFlow[] workFlows;
    private Boolean[] workFlowStates;


    //是否配置一级选择框
    private List<JCheckBox> workFlowCheckBoxes;

    //是否配置二级菜单选择框
    private List<JCheckBox> configSubtypes;

    public Panel_WorkFlow_Product(final WorkFlowProductPresenter presenter) {

        this.presenter = presenter;
        FlowLayout f;
        int size = CacheManager.getInstance().bufferData.workFlows.size();
        workFlowStates = new Boolean[size];
        workFlows = new WorkFlow[size];
        for (int i = 0; i < size; i++) {

            workFlows[i] = CacheManager.getInstance().bufferData.workFlows.get(i);
            workFlowStates[i] = false;
        }


        workFlowSubTypes = CacheManager.getInstance().bufferData.workFlowSubTypes;

        init();


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                presenter.save();
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
    public void setData(WorkFlowProduct workFlowProduct) {



        //新数据不绑定
        if (workFlowProduct.id <= 0) return;
        String[] temp = workFlowProduct.productTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        int size = workFlowSubTypes.size();
        for (int i = 0; i < size; i++) {
            WorkFlowSubType subType = workFlowSubTypes.get(i);
            final int indexOnArray = ArrayUtils.indexOnArray(temp, String.valueOf(subType.typeId));
            workFlowSubTypeChecks.get(i).setSelected(indexOnArray >= 0);
        }



        //一级流程
        temp = workFlowProduct.workFlowSteps.split(ConstantData.STRING_DIVIDER_SEMICOLON);
        String[] configSubTypeStrings=workFlowProduct.workFlowTypes==null?null:workFlowProduct.workFlowTypes.split(ConstantData.STRING_DIVIDER_SEMICOLON);
          size = workFlows.length;

        for (int i = 0; i < size; i++) {
            WorkFlow workFlow = workFlows[i];
            final int indexOnArray = ArrayUtils.indexOnArray(temp, String.valueOf(workFlow.flowStep));
            workFlowCheckBoxes.get(i).setSelected(indexOnArray >= 0);
            configSubtypes.get(i).setEnabled(indexOnArray>=0);



              boolean config = indexOnArray > -1 && configSubTypeStrings != null && configSubTypeStrings.length > indexOnArray && "1".equals(configSubTypeStrings[indexOnArray]);

            configSubtypes.get(i).setSelected(config);

            //设置默认值
            if(configSubTypeStrings==null||configSubTypeStrings.length==0)
            {
                configSubtypes.get(i).setSelected(i<3);
            }


        }



    }


    /**
     * 获取当前用户数据
     *
     * @param workFlowProduct
     */
    @Override
    public void getData(WorkFlowProduct workFlowProduct) {


        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder nameStringBuilder=new StringBuilder();
        int size = workFlowSubTypes.size();
        for (int i = 0; i < size; i++) {
            if (workFlowSubTypeChecks.get(i).isSelected()) {
                WorkFlowSubType subType = workFlowSubTypes.get(i);
                stringBuilder.append(subType.typeId).append(ConstantData.STRING_DIVIDER_SEMICOLON);
                nameStringBuilder.append(subType.typeName).append(ConstantData.STRING_DIVIDER_SEMICOLON);

            }

        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
            nameStringBuilder.setLength(nameStringBuilder.length() - 1);
        }
        workFlowProduct.productTypes = stringBuilder.toString();
        workFlowProduct.productTypeNames = nameStringBuilder.toString();






        //一级流程
        stringBuilder.setLength(0);
          size = workFlows.length;
        nameStringBuilder.setLength(0);

        StringBuilder configSubTypeStrings=new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (workFlowCheckBoxes.get(i).isSelected()) {
                WorkFlow workFlow = workFlows[i] ;
                stringBuilder.append(workFlow.flowStep).append(ConstantData.STRING_DIVIDER_SEMICOLON);
                nameStringBuilder.append(workFlow.name).append(ConstantData.STRING_DIVIDER_SEMICOLON);
                configSubTypeStrings.append(configSubtypes.get(i).isSelected()?1:0).append(ConstantData.STRING_DIVIDER_SEMICOLON);
            }

        }
        if (stringBuilder.length() > 0)
        {
            stringBuilder.setLength(stringBuilder.length() - 1);
            nameStringBuilder.setLength(nameStringBuilder.length() - 1);
            configSubTypeStrings.setLength(configSubTypeStrings.length() - 1);
        }

        workFlowProduct.workFlowSteps = stringBuilder.toString();
        workFlowProduct.workFlowNames=nameStringBuilder.toString();
        workFlowProduct.workFlowTypes=configSubTypeStrings.toString();







    }


//    @Override
//    public void setSubData(List<WorkFlowSubType> datas) {
//        workFlowSubTypes=datas;
//        workFlowSubTypeChecks=new ArrayList<>();
//        for (WorkFlowSubType subType:datas)
//        {
//            JCheckBox jCheckBox=new JCheckBox(subType.typeName);
//            workFlowSubTypeChecks.add(jCheckBox);
//            subtypes.add(jCheckBox);
//
//        }
//
//
//
//
//    }


    private void init() {


        workFlowSubTypeChecks = new ArrayList<>();
        for (WorkFlowSubType subType : workFlowSubTypes) {
            JCheckBox jCheckBox = new JCheckBox(subType.typeName);
            workFlowSubTypeChecks.add(jCheckBox);
            subtypes.add(jCheckBox);

        }


        panel_work_flow.setLayout(new VerticalFlowLayoutManager(10));

        configSubtypes = new ArrayList<>();
        workFlowCheckBoxes = new ArrayList<>();
        for (WorkFlow workFlow : workFlows) {
            final JComponent panelBaseOnWorkFlow = createPanelBaseOnWorkFlow(workFlow);
            final Dimension preferredSize = new Dimension(600, 30);
            panelBaseOnWorkFlow.setPreferredSize(preferredSize);
            panelBaseOnWorkFlow.setMinimumSize(preferredSize);

            panel_work_flow.add(panelBaseOnWorkFlow);
        }
        //默认设置， 前三个流程
        for(int i=0;i<configSubtypes.size();i++)
        {
            configSubtypes.get(i).setSelected(i<3);
        }


        panel_work_flow.setMaximumSize(new Dimension(600, 500));
        panel_work_flow.setPreferredSize(new Dimension(600, 500));


    }


    private JComponent createPanelBaseOnWorkFlow(WorkFlow workFlow) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());

        final JCheckBox jCheckBox = new JCheckBox(workFlow.name);
        workFlowCheckBoxes.add(jCheckBox);
        //所有流程默认选择
        jCheckBox.setSelected(true);
        jPanel.add(jCheckBox);
        final Dimension preferredSize = new Dimension(200, 30);
        jCheckBox.setPreferredSize(preferredSize);
        jCheckBox.setMinimumSize(preferredSize);

        final JCheckBox subType = new JCheckBox("是否按类型排厂");
        jPanel.add(subType);
        configSubtypes.add(subType);
        return jPanel;
    }

}
