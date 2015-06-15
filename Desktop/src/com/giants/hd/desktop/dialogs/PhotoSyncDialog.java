package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.view.BasePanel;
import com.giants.hd.desktop.view.Panel_CopyProduct;
import com.giants.hd.desktop.view.Panel_PhotoSync;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 图片同步对话框
 */
public class PhotoSyncDialog extends BaseDialog<Void>  implements BasePanel.PanelListener {


    @Inject
    ApiManager apiManager;


    @Inject
    Panel_PhotoSync panel_photoSync;

    public PhotoSyncDialog(Window window )
    {
        super(window,"图片同步");
        setMinimumSize(new Dimension(400,400));
        setLocationRelativeTo(window);


        setContentPane(   panel_photoSync.getRoot());
        init();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }




    public void init()
    {


        panel_photoSync.btn_material.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new HdSwingWorker<Void,Object>(PhotoSyncDialog.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {


                        return   apiManager.syncMaterialPhoto();

                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {




                            JOptionPane.showMessageDialog(PhotoSyncDialog.this,data.message);



                    }
                }.go();
            }
        });

        panel_photoSync.btn_product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new HdSwingWorker<Void,Object>(PhotoSyncDialog.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {


                        return   apiManager.syncProductPhoto();

                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        JOptionPane.showMessageDialog(PhotoSyncDialog.this,data.message);

                    }
                }.go();
            }
        });
    }




    @Override
    public void save() {









    }

    @Override
    public void delete() {

    }

    @Override
    public void close() {

        dispose();
    }

}
