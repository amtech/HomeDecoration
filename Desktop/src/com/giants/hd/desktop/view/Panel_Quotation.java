package com.giants.hd.desktop.view;



import javax.swing.*;

/**
 * 报价列表界面
 */
public class Panel_Quotation extends BasePanel{
    private JPanel panel1;
    private JTable tb_quotation;
    private JTextField jtf_product;
    private JButton btn_search;
    private JCheckBox cb_date;





    public Panel_Quotation()
    {}


    @Override
    public JComponent getRoot() {
        return panel1;
    }
}
