package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.view.BasePanel;
import com.giants.hd.desktop.view.Panel_CopyProduct;
import com.giants.hd.desktop.api.ApiManager;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;

/**
 * 翻样对话框
 */
public class CopyProductDialog extends BaseDialog<ProductDetail>  implements BasePanel.PanelListener {


    @Inject
    ApiManager apiManager;
    @Inject
    Panel_CopyProduct panel_copyProduct;


    Product product;
    Product newProduct=new Product();
    public CopyProductDialog(Window window, Product product)
    {
        super(window,"产品翻单");
        this.product=product;
        setContentPane(   panel_copyProduct.getRoot());
        init();


    }




    public void init()
    {


        panel_copyProduct.setListener(this);
        panel_copyProduct.setData(product);


    }




    @Override
    public void save() {




        panel_copyProduct.getData(newProduct);


        new HdSwingWorker<ProductDetail,Object>(this)
        {

            @Override
            protected RemoteData<ProductDetail> doInBackground() throws Exception {


                return   apiManager.copyProductDetail(product.id,newProduct.name,newProduct.pVersion);

            }

            @Override
            public void onResult(RemoteData<ProductDetail> data) {


                if(data.code==RemoteData.CODE_FAIL)
                {


                    JOptionPane.showMessageDialog(CopyProductDialog.this,data.message);
                    return;
                }
                JOptionPane.showMessageDialog(CopyProductDialog.this,"翻单成功！");

                setResult(data.datas.get(0));
                dispose();



            }
        }.go();

    }

    @Override
    public void delete() {

    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public void verify() {

    }

    @Override
    public void unVerify() {

    }

}