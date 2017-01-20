package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.dialogs.WorkFlowOrderArrangeDialog;
import com.giants.hd.desktop.model.OutFactoryModel;
import com.giants.hd.desktop.presenter.OrderItemWorkFlowPresenter;
import com.giants.hd.desktop.view.OrderItemWorkFlowViewer;
import com.giants.hd.desktop.widget.VerticalFlowLayoutManager;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.entity.OrderItemWorkFlow;
import com.giants3.hd.utils.entity.OutFactory;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by davidleen29 on 2017/1/11.
 */
public class Panel_OrderItemWorkFlow  extends BasePanel implements OrderItemWorkFlowViewer {
    private JPanel root;
    private JPanel panel_types;
    private JButton save;

    private List<JComboBox<OutFactory>> factories;
    private OrderItemWorkFlowPresenter presenter;


    public Panel_OrderItemWorkFlow(final OrderItemWorkFlowPresenter presenter) {

        this.presenter = presenter;


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
    public List<OutFactory> getArrangedFactories() throws Exception {


        List<OutFactory>  result=new ArrayList<>();
        for(JComboBox<OutFactory> jComboBox:factories)
        {


            OutFactory factory= (OutFactory) jComboBox.getSelectedItem();
            if(factory==null)
            {

                throw new Exception("还有厂家未选择");
            }


            result.add((OutFactory) jComboBox.getSelectedItem());
        }


        return result;

    }




    @Override
    public void setProductTypes(String[] productTypes, String[] productTypeNames, java.util.List<OutFactory> outFactoryList)
    {


        panel_types.setLayout(new VerticalFlowLayoutManager(10));


        int size=productTypes.length;
        factories=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String  workFlowType= productTypes[i];
            String  workFlowTypeName= productTypeNames[i];
            final JComponent panelBaseOnWorkFlow = createPanelBaseOnWorkFlow(workFlowType,workFlowTypeName,outFactoryList);
            final Dimension preferredSize = new Dimension(600, 30);
            panelBaseOnWorkFlow.setPreferredSize(preferredSize);
            panelBaseOnWorkFlow.setMinimumSize(preferredSize);

            panel_types.add(panelBaseOnWorkFlow);
        }

        panel_types.setMaximumSize(new Dimension(600, 500));
        panel_types.setPreferredSize(new Dimension(600, 500));
        panel_types.updateUI();
    }

    private JComponent createPanelBaseOnWorkFlow(String workFlowType, String workFlowTypeName, List<OutFactory> outFactoryList) {



        JPanel jPanel=new JPanel();

        jPanel.setLayout(new FlowLayout());
        JLabel jLabel=new JLabel(workFlowTypeName);
        jPanel.add(jLabel);


        jPanel.add(new JLabel("   选择厂家:"));
        JComboBox jComboBox=new JComboBox();

      Vector<OutFactory>  outFactoryVector= ArrayUtils.changeListToVector(outFactoryList);

        ComboBoxModel comboBoxModel=new DefaultComboBoxModel( outFactoryVector)
        {

        };

        jComboBox.setModel(comboBoxModel);

        factories.add(jComboBox);

        jComboBox.setMinimumSize(new Dimension(200,60));
     //   jComboBox.setPreferredSize(new Dimension(150,60));
        jPanel.add(jComboBox);
        return jPanel;


    }
}
