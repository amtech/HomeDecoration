package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.presenter.OrderItemWorkFlowPresenter;
import com.giants.hd.desktop.view.OrderItemWorkFlowViewer;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.ProduceType;
import com.giants3.hd.utils.entity.ErpOrderItem;
import com.giants3.hd.utils.entity.OutFactory;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowSubType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 订单排厂界面    自制 与 外购
 * <p/>
 * Created by davidleen29 on 2017/1/11.
 */
public class Panel_OrderItemWorkFlow extends BasePanel implements OrderItemWorkFlowViewer {
    private JPanel root;
    private JPanel panel_types;
    private JButton save;
    private JLabel photo;
    private JLabel order;
    private JLabel product;
    private JRadioButton isSelfMade;
    private JRadioButton isPurchased;

    private JComboBox jbFactory;
    private JPanel panel_purchase;
    private JPanel panel_work_flow;


    //流程相关的panel列表
    private List<JComponent> workFlowPanels = new ArrayList<>();
    private List<JComboBox<OutFactory>> factories;
    private OrderItemWorkFlowPresenter presenter;


    private List<WorkFlowSubType> workFlowSubTypes;
    private List<JCheckBox> workFlowSubTypeChecks;
    private WorkFlow[] workFlows;
    private Boolean[] workFlowStates;


    /**
     * 自制生产流程列表
     */
    List<WorkFlow> selfMadeWorkFlowList;

    /**
     * 外购产品的流程列表
     */
    List<WorkFlow> purchaseWorkFlowList;
    //是否配置一级选择框
    private List<JCheckBox> workFlowCheckBoxes;

    //是否配置二级菜单选择框
    private List<JCheckBox> configSubtypes;


    public Panel_OrderItemWorkFlow(final OrderItemWorkFlowPresenter presenter) {

        this.presenter = presenter;


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                presenter.save();


            }
        });


        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(isPurchased);
        buttonGroup.add(isSelfMade);

        final ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.print(e.getSource());

                boolean isSelfMadeClick = e.getSource() == isSelfMade;
                showWorkFlows(isSelfMadeClick);


            }
        };
        isPurchased.addActionListener(l);
        isSelfMade.addActionListener(l);
        isSelfMade.setSelected(true);
        int size = CacheManager.getInstance().bufferData.workFlows.size();
        //排厂类型列表
        workFlowSubTypes = CacheManager.getInstance().bufferData.workFlowSubTypes;
        workFlowStates = new Boolean[size];
        workFlows = new WorkFlow[size];
        for (int i = 0; i < size; i++) {

            workFlows[i] = CacheManager.getInstance().bufferData.workFlows.get(i);
            workFlowStates[i] = false;
        }


        selfMadeWorkFlowList = new ArrayList<>();
        purchaseWorkFlowList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            final WorkFlow workFlow = CacheManager.getInstance().bufferData.workFlows.get(i);
            if (workFlow.isSelfMade)
                selfMadeWorkFlowList.add(workFlow);
            if (workFlow.isPurchased)
                purchaseWorkFlowList.add(workFlow);
        }


        init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showWorkFlows(true);
            }
        });


    }

    private void showWorkFlows(boolean isSelfMade) {

        panel_types.setVisible(isSelfMade);
        panel_purchase.setVisible(!isSelfMade);


        int size = workFlowPanels.size();
        int visibleSize = 0;
        panel_work_flow.removeAll();
        for (int i = 0; i < size; i++) {

            WorkFlow workFlow = CacheManager.getInstance().bufferData.workFlows.get(i);

            JComponent jComponent = workFlowPanels.get(i);
            final boolean visible = workFlow.isSelfMade && isSelfMade || workFlow.isPurchased && !isSelfMade;
            jComponent.setVisible(visible);
            if (visible) {
                visibleSize++;
                panel_work_flow.add(jComponent);
            }

        }

        panel_work_flow.setLayout(new GridLayout(visibleSize, 1, 10, 5));
        panel_work_flow.setPreferredSize(new Dimension(600, visibleSize * 40));
        panel_work_flow.setEnabled(isSelfMade);



        for(JCheckBox comboBox:configSubtypes)
        {
            comboBox.setVisible(isSelfMade);

        }
        presenter.pack();

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
    public List<OutFactory> getArrangedFactories() throws Exception {


        List<OutFactory> result = new ArrayList<>();
        for (JComboBox<OutFactory> jComboBox : factories) {


            OutFactory factory = (OutFactory) jComboBox.getSelectedItem();
            if (factory == null) {

                throw new Exception("还有厂家未选择");
            }


            result.add((OutFactory) jComboBox.getSelectedItem());
        }


        return result;

    }


    @Override
    public void setOutFactories(List<OutFactory> outFactories) {
        Vector<OutFactory> outFactoryVector = ArrayUtils.changeListToVector(outFactories);
        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(outFactoryVector);
        jbFactory.setModel(comboBoxModel);
    }


    @Override
    public OutFactory getProduceFactory() {


        return (OutFactory) jbFactory.getSelectedItem();
    }

    @Override
    public List<WorkFlow> getSelectedWorkFlows() {




        return purchaseWorkFlowList;



    }

    @Override
    public int getSelectArrangeType() {



        if(isSelfMade.isSelected())
            return ProduceType.SELF_MADE;
            else
         return ProduceType.PURCHASE;

    }

    @Override
    public void bindOrderData(ErpOrderItem erpOrderItem) {

        product.setText(erpOrderItem.prd_no);
        order.setText(erpOrderItem.getOs_no());

        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon) {
                photo.setIcon(icon);
            }

            @Override
            public void onError(String message) {
                photo.setText("");
            }
        }, HttpUrl.loadPicture(erpOrderItem.thumbnail), 100, 100);

    }

    @Override
    public void setProductTypes(String[] productTypes, String[] productTypeNames, java.util.List<OutFactory> outFactoryList) {



        int size = productTypes.length;
        panel_types.setLayout(new GridLayout(size, 1, 10, 10));
        List<WorkFlowSubType> subTypes = CacheManager.getInstance().bufferData.workFlowSubTypes;
        factories = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String workFlowType = String.valueOf(subTypes.get(i).typeId);
            String workFlowTypeName = subTypes.get(i).typeName;
            final JComponent panelBaseOnWorkFlow = createPanelBaseOnWorkFlow(workFlowType, workFlowTypeName, outFactoryList);
            final Dimension preferredSize = new Dimension(600, 30);
            panelBaseOnWorkFlow.setPreferredSize(preferredSize);
            panelBaseOnWorkFlow.setMinimumSize(preferredSize);
            panel_types.add(panelBaseOnWorkFlow);
        }
        presenter.pack();
    }

    private JComponent createPanelBaseOnWorkFlow(String workFlowType, String workFlowTypeName, List<OutFactory> outFactoryList) {


        JPanel jPanel = new JPanel();

        jPanel.setLayout(new FlowLayout());
        JLabel jLabel = new JLabel(workFlowTypeName);
        jPanel.add(jLabel);


        jPanel.add(new JLabel("   选择厂家:"));
        JComboBox jComboBox = new JComboBox();

        Vector<OutFactory> outFactoryVector = ArrayUtils.changeListToVector(outFactoryList);

        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(outFactoryVector) {

        };

        jComboBox.setModel(comboBoxModel);

        factories.add(jComboBox);

        jComboBox.setMinimumSize(new Dimension(200, 60));
        //   jComboBox.setPreferredSize(new Dimension(150,60));
        jPanel.add(jComboBox);
        return jPanel;


    }


    private void init() {


        workFlowSubTypeChecks = new ArrayList<>();
        for (WorkFlowSubType subType : workFlowSubTypes) {
            JCheckBox jCheckBox = new JCheckBox(subType.typeName);
            workFlowSubTypeChecks.add(jCheckBox);


        }

        int size = workFlows.length;
        panel_work_flow.setLayout(new GridLayout(size, 1, 10, 5));

        configSubtypes = new ArrayList<>();
        workFlowCheckBoxes = new ArrayList<>();
        workFlowPanels = new ArrayList<>();
        for (WorkFlow workFlow : workFlows) {
            final JComponent panelBaseOnWorkFlow = createPanelBaseOnWorkFlow(workFlow);
            workFlowPanels.add(panelBaseOnWorkFlow);
//            final Dimension preferredSize = new Dimension(600, 30);
//            panelBaseOnWorkFlow.setPreferredSize(preferredSize);
//            panelBaseOnWorkFlow.setMinimumSize(preferredSize);

            panel_work_flow.add(panelBaseOnWorkFlow);
        }
        //默认设置， 前三个流程
        for (int i = 0; i < configSubtypes.size(); i++) {
            configSubtypes.get(i).setSelected(i < 3);
        }


        panel_work_flow.setMaximumSize(new Dimension(600, 500));


    }


    private JComponent createPanelBaseOnWorkFlow(WorkFlow workFlow) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());

        final JCheckBox jCheckBox = new JCheckBox(workFlow.name);
        workFlowCheckBoxes.add(jCheckBox);
        jCheckBox.setEnabled(false);
        //所有流程默认选择
        jCheckBox.setSelected(true);
        jPanel.add(jCheckBox);
        final Dimension preferredSize = new Dimension(200, 30);
        jCheckBox.setPreferredSize(preferredSize);
        jCheckBox.setMinimumSize(preferredSize);


        final JCheckBox subType = new JCheckBox("是否按类型排厂");
        jPanel.add(subType);
        subType.setEnabled(false);
        configSubtypes.add(subType);
        return jPanel;
    }
}
