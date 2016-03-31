package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.viewImpl.Panel_Sync;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.RemoteData;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  同步对话框
 */
public class SyncDialog extends BaseDialog<Void>  {


    @Inject
    ApiManager apiManager;


    @Inject
    Panel_Sync panel_photoSync;

    public SyncDialog(Window window)
    {
        super(window, "数据同步");
        setMinimumSize(new Dimension(400, 400));


        setContentPane(panel_photoSync.getRoot());
        init();


    }




    public void init()
    {






        panel_photoSync.btn_erp_sync.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                new HdSwingWorker<Void, Void>(SyncDialog.this) {

                    private  long time=System.currentTimeMillis();
                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {



                        return apiManager.syncErpMaterial();
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        if(data.isSuccess())
                        {
                            JOptionPane.showMessageDialog(SyncDialog.this,"ERP材料同步成功,耗时："+((System.currentTimeMillis()-time)/1000)+"秒");
                        }else
                        {
                            JOptionPane.showMessageDialog(SyncDialog.this,data.message);
                        }

                    }
                }.go();
            }
        });
    }





}
