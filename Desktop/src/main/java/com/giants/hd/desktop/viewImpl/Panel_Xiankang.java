package com.giants.hd.desktop.viewImpl;

import com.giants3.hd.utils.entity.Xiankang;

import javax.swing.*;

/**
 * �̿��������
 */
public class Panel_Xiankang {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private Panel_XK panel_xk;

    public void setData(Xiankang data) {


        panel_xk.setData(data);
    }

    public Xiankang getData() {

        return panel_xk.getData( );

    }

    public boolean isModified(Xiankang data) {
        return panel_xk.isModified(data);
    }


    /**
     * ��������Ƿ�ɼ�
     * @param visible
     */
    public void setVisible(boolean visible) {

        panel1.setVisible(visible);
        panel_xk.setVisible(visible);

    }
}
