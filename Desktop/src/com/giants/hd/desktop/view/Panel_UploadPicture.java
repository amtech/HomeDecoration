package com.giants.hd.desktop.view;

import javax.swing.*;

/**
 * 图片上传模板
 */
public class Panel_UploadPicture extends BasePanel {
    public JButton btn_uploadProduct;
    public JCheckBox cb_productReplace;
    public JButton btn_uploadMaterial;
    public JCheckBox cb_MaterialReplace;
    public JPanel root;

    @Override
    public JComponent getRoot() {
        return root;
    }
}
