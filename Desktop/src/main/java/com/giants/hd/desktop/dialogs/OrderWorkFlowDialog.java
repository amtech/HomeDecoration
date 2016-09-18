package com.giants.hd.desktop.dialogs;

import javax.swing.*;
import java.awt.*;

public class OrderWorkFlowDialog extends JDialog {
    private JPanel contentPane;
    private JButton 启动;
    private JPanel workFlows;


    public OrderWorkFlowDialog() {
        setContentPane(contentPane);
        setModal(true);
        workFlows.setLayout(new GridLayout(0, 1, 10, 10));
    }
}
