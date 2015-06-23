package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.view.Panel_Material_Detail;
import com.giants.hd.desktop.view.Panel_ProductProcess;
import com.giants.hd.desktop.widget.TableMouseAdapter;
import com.giants.hd.desktop.widget.TablePopMenu;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductProcess;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  工序对话框
 */
public class ProductProcessDialog extends  BaseDialog<ProductProcess> {


    @Inject
    ApiManager apiManager;
    @Inject
    Panel_ProductProcess panel_productProcess;

    public ProductProcessDialog(Window window) {
        super(window,"工序列表");


        setContentPane(panel_productProcess.getRoot());
        init();
    }


    /**
     * 初始，加载数据
     */
    private void init()
    {


        loadData();


        panel_productProcess.btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                new HdSwingWorker<ProductProcess,Object>(ProductProcessDialog.this)
                {
                    @Override
                    protected RemoteData<ProductProcess> doInBackground() throws Exception {




                        return apiManager.saveProductProcess(panel_productProcess.productProcessModel.getDatas());
                    }

                    @Override
                    public void onResult(RemoteData<ProductProcess> data) {


                        if(data.isSuccess())
                        {

                            JOptionPane.showMessageDialog(ProductProcessDialog.this,"保存成功");


                            panel_productProcess.setData(data.datas);


                        }else
                        {
                            JOptionPane.showMessageDialog(ProductProcessDialog.this,data.message);
                        }





                    }
                }.go();



            }
        });



        TableMouseAdapter adapter=new TableMouseAdapter(new TablePopMenu.TableMenuLister() {
            @Override
            public void onTableMenuClick(int index, BaseTableModel tableModel, int[] rowIndex) {

                switch (index) {


                    case TablePopMenu.ITEM_INSERT:

                        tableModel.addNewRow(rowIndex[0]);

                        break;
                    case TablePopMenu.ITEM_DELETE:




                        tableModel.deleteRows(rowIndex);
                        break;
                }



            }
        });


        panel_productProcess.jt_process.addMouseListener(adapter);


    }

    private void loadData() {
        new HdSwingWorker<ProductProcess,Object>(this)
        {
            @Override
            protected RemoteData<ProductProcess> doInBackground() throws Exception {
                return apiManager.loadProductProcess();
            }

            @Override
            public void onResult(RemoteData<ProductProcess> data) {


                if(data.isSuccess())
                {

                    panel_productProcess.setData(data.datas);
                }







            }
        }.go();
    }


    /**
     * 删除工序
     * @param rows
     */
    private void deleteRows(int rows)
    {
//        new HdSwingWorker<ProductProcess,Object>(this)
//        {
//            @Override
//            protected RemoteData<ProductProcess> doInBackground() throws Exception {
//                return apiManager.loadProductProcess();
//            }
//
//            @Override
//            public void onResult(RemoteData<ProductProcess> data) {
//
//
//                if(data.isSuccess())
//                {
//
//                    panel_productProcess.setData(data.datas);
//                }
//
//
//
//                dispose();
//
//
//
//            }
//        }.go();


    }



}
