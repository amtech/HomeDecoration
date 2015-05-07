package com.giants.hd.desktop;

import javafx.application.Application;

import javax.swing.*;

/**
 * Created by davidleen29 on 2015/5/6.
 */
public class Main extends  JFrame  {
    private JPanel panel1;
    private JCheckBox CheckBox;
    private JTabbedPane tabbedPane1;
    private JComboBox comboBox1;
    private JTable table1;


    public static void main(String[] args) {
        Main frame = new Main();
        frame.pack();
        frame.setSize(800, 600);
        frame.setBounds(200, 200, 800, 600);
        frame.setContentPane(frame.panel1);
        frame.setVisible(true);

        //System.exit(0);
    }


}
