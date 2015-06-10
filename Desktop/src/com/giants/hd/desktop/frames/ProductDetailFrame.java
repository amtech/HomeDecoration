package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.interf.Operatable;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.view.BasePanel;
import com.giants.hd.desktop.view.Panel_ProductDetail;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;

/**
 *  产品详细模块
 */
public class ProductDetailFrame extends BaseFrame implements  BasePanel.PanelListener {



    @Inject
    ApiManager apiManager;
    Panel_ProductDetail panel_productDetail;
  public   ProductDetailFrame(ProductDetail productDetail )
    {



        super("产品详情[" + (productDetail.product == null ? "新增" : ("货号：" + productDetail.product.getName() + "---版本号：" + productDetail.product.getpVersion())) + "]");
          panel_productDetail = new Panel_ProductDetail(productDetail);
        init();
    }



    public void init( )
    {


        panel_productDetail.setListener(this);

        setContentPane(panel_productDetail.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();


    }
    /**
     * 显示产品详情
     * @param product
     * @Param component
     *
     */
    public      ProductDetailFrame(Product product  )
    {

        super("产品详情[" + (product == null ? "新增" : ("货号：" + product.getName() + "---版本号：" + product.getpVersion())) + "]");
          panel_productDetail = new Panel_ProductDetail(product);
        init( );

    }


    @Override
    public void save() {


    }

    @Override
    public void delete() {




        final ProductDetail detail=panel_productDetail.getData();

        if(detail.product.id<=0)
        {

            JOptionPane.showMessageDialog(this, "产品数据未建立，请先保存");
            return;

        }



     int res=   JOptionPane.showConfirmDialog(this, "是否删除产品？（导致数据无法恢复）", "删除产品", JOptionPane.OK_CANCEL_OPTION);
        if(res==JOptionPane.YES_OPTION)
        {
        new HdSwingWorker<Void,Void>(this)
        {

            @Override
            protected RemoteData<Void> doInBackground() throws Exception {

                return     apiManager.deleteProductLogic(detail.product.id);

            }

            @Override
            public void onResult(RemoteData<Void> data) {

                if(data.isSuccess())
                {

                    JOptionPane.showMessageDialog(ProductDetailFrame.this,"删除成功！");

                    ProductDetailFrame.this.dispose();



                }else
                {
                    JOptionPane.showMessageDialog(ProductDetailFrame.this,data.message);
                }

            }
        }.go();



        }


    }
}
