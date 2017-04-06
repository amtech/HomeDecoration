package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.model.TableField;
import com.giants.hd.desktop.model.WorkFlowArrangeTableModel;
import com.giants.hd.desktop.presenter.WorkFlowProductPresenter;
import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.view.WorkFlowConfigViewer;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowProduct;
import com.giants3.hd.utils.entity.WorkFlowSubType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程配置。
 * Created by davidleen29 on 2017/4/2.
 */
public class Panel_WorkFlow_Config extends BasePanel implements WorkFlowConfigViewer {


    private static final String MUJIAN = "木件";
    private static final String PU = "PU";
    private static final String TIEJIAN = "铁件";
    List<TableField> tableFields;

    List<WorkFlowConfig> data;

    WorkFlowArrangeTableModel tableModel;

    public Panel_WorkFlow_Config(final WorkFlowProductPresenter presenter) {
        this.tableFields = TableStructureUtils.getWorkFlowArrange();
        tableModel = new WorkFlowArrangeTableModel(WorkFlowConfig.class, tableFields);

        table.setModel(tableModel);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.save();
            }
        });


    }

    private JPanel root;
    private JHdTable table;
    private JPanel check_group;
    private JButton save;

    List<JCheckBox> jCheckBoxes;
    List<WorkFlowSubType> workFlowSubTypes;


    public static class WorkFlowConfig {
        public int workFlowStep;
        public String workFlowName;
        public boolean tiejian;
        public boolean mujian;
        public boolean pu;
    }

    @Override
    public void setWorkFlows(List<WorkFlow> workFlows, List<WorkFlowSubType> subTypes) {

        data = new ArrayList<>();
        for (WorkFlow workFlow : workFlows) {
            WorkFlowConfig workFlowConfig = new WorkFlowConfig();
            workFlowConfig.workFlowStep = workFlow.flowStep;
            workFlowConfig.workFlowName = workFlow.name;
            data.add(workFlowConfig);
        }
        tableModel.setDatas(data);

        this.workFlowSubTypes = subTypes;
        jCheckBoxes = new ArrayList<>();
        check_group.removeAll();
        for (WorkFlowSubType subType : subTypes) {
            final JCheckBox checkBox = new JCheckBox(subType.typeName);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {

                    updateTableColumn();
                }
            });
            jCheckBoxes.add(checkBox);
            check_group.add(checkBox);
        }


    }

    /**
     *
     */
    private void updateTableColumn() {

        List<WorkFlowSubType> configSubType = new ArrayList<>();
        List<TableField> configField = new ArrayList<>();
        configField.addAll(tableFields);
        for (int i = 0; i < jCheckBoxes.size(); i++) {
            JCheckBox checkBox = jCheckBoxes.get(i);
            if (!checkBox.isSelected()) {
                final WorkFlowSubType workFlowSubType = workFlowSubTypes.get(i);

                for (TableField tableField :
                        tableFields) {

                    if (tableField.columnName.equals(workFlowSubType.typeName)) {
                        configField.remove(tableField);
                        break;
                    }


                }

                configSubType.add(workFlowSubType);


            }

        }


        tableModel.updateStructure(configField);


    }


    public void setProductTypes() {
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


        String[] steps = StringUtils.split(workFlowProduct.workFlowSteps);
        if (StringUtils.isEmpty(workFlowProduct.configs))
            return;

        String[] configs = StringUtils.split(workFlowProduct.configs);
        String[] productTypeNames = StringUtils.split(workFlowProduct.productTypeNames);


        int workFlowSubTypeSize=workFlowSubTypes.size();
        for (int i = 0; i < workFlowSubTypeSize; i++) {

            WorkFlowSubType subType=workFlowSubTypes.get(i);

            jCheckBoxes.get(i).setSelected(StringUtils.index(productTypeNames,subType.typeName)>-1);

        }



        int length = productTypeNames.length;

        int workFlowLength = steps.length;
        for (int i = 0; i < workFlowLength; i++) {
            String config = configs[i];


            String[] productTypeConfig = StringUtils.split(config, StringUtils.STRING_SPLIT_COMMA);

            for (int j = 0; j < length; j++) {

                final WorkFlowConfig workFlowConfig = this.data.get(i);
                final boolean selected = "1".equals(  productTypeConfig[j])  ;
                System.out.println("  "+productTypeNames[j]+"  "+selected );
                switch (productTypeNames[j]) {
                    case TIEJIAN:


                        workFlowConfig.tiejian = selected;

                        break;
                    case MUJIAN:

                        workFlowConfig.mujian = selected;

                        break;
                    case PU:


                        workFlowConfig.pu = selected;
                        break;
                }


            }


        }

        tableModel.setDatas(this.data);


    }

    /**
     * 获取用户编辑结果
     *
     * @param data
     */
    @Override
    public void getData(WorkFlowProduct data) {

        int length = this.data.size();
        String[] workFlowSteps = new String[length];
        String[] workFLowNames = new String[length];


        List<WorkFlowSubType> checkSubTypes = new ArrayList<>();

        int typeLength = workFlowSubTypes.size();
        for (int i = 0; i < typeLength; i++) {
            JCheckBox jCheckBox = jCheckBoxes.get(i);
            if (jCheckBox.isSelected())
                checkSubTypes.add(workFlowSubTypes.get(i));
        }


        typeLength = checkSubTypes.size();
        String[] productTypes = new String[typeLength];
        String[] productTypeNames = new String[typeLength];
        for (int i = 0; i < typeLength; i++) {
            productTypes[i] = String.valueOf(checkSubTypes.get(i).typeId);
            productTypeNames[i] = checkSubTypes.get(i).typeName;
        }
        data.productTypes = StringUtils.combine(productTypes);
        data.productTypeNames = StringUtils.combine(productTypeNames);


        String[] configs = new String[length];
        StringBuilder config = new StringBuilder();
        for (int i = 0; i < length; i++) {
            WorkFlowConfig workFlowConfig = this.data.get(i);
            workFlowSteps[i] = String.valueOf(workFlowConfig.workFlowStep);
            workFLowNames[i] = workFlowConfig.workFlowName;
            if (typeLength > 0) {
                config.setLength(0);
                for (int j = 0; j < typeLength; j++) {

                    WorkFlowSubType subType = checkSubTypes.get(j);
                    switch (subType.typeName) {
                        case TIEJIAN:
                            config.append(workFlowConfig.tiejian ? 1 : 0);
                            break;
                        case MUJIAN:
                            config.append(workFlowConfig.mujian ? 1 : 0);
                            break;
                        case  PU:
                            config.append(workFlowConfig.pu ? 1 : 0);
                            break;
                    }
                    config.append(StringUtils.STRING_SPLIT_COMMA);


                }
                config.setLength(config.length() - 1);
                configs[i] = config.toString();

            }

        }


        data.workFlowSteps = StringUtils.combine(workFlowSteps);
        data.workFlowNames = StringUtils.combine(workFLowNames);
        data.configs = StringUtils.combine(configs);

    }
}
