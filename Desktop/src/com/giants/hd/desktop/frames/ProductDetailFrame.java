package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants.hd.desktop.view.BasePanel;
import com.giants.hd.desktop.view.Panel_ProductDetail;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductDelete;
import com.giants3.hd.utils.entity.ProductDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *  产品详细模块
 */
public class ProductDetailFrame extends BaseFrame implements  BasePanel.PanelListener {



    @Inject
    ApiManager apiManager;
    @Inject
    Panel_ProductDetail panel_productDetail;
    ProductDelete productDelete =null;
   public   ProductDetailFrame(ProductDetail productDetail )
    {


        this(productDetail, null);


    }

    public   ProductDetailFrame(ProductDetail productDetail,ProductDelete productDelete )
    {


        super();
        this.productDelete =productDelete;


        setTitle("产品详情[" + (productDetail.product == null ? "新增" : ("货号：" + productDetail.product.getName() + "---版本号：" + productDetail.product.getpVersion())) + "]" + (productDelete!=null ? "    [已删除]   " : ""));


        init( );

        panel_productDetail.setProductDetail(productDetail,productDelete);
    }



    public void init()
    {






        panel_productDetail.setListener(this);
        setContentPane(panel_productDetail.getRoot());
        setMinimumSize(new Dimension(1024, 768));
        pack();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                try {


                    if(panel_productDetail.productDetail==null|| productDelete!=null)
                    {
                        dispose();
                        return;
                    }

                    if(panel_productDetail.isModified())
                    {

                     int option=   JOptionPane.showConfirmDialog(ProductDetailFrame.this,"数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

                        if (JOptionPane.OK_OPTION == option) {
                            //点击了确定按钮

                            ProductDetailFrame.this.dispose();
                        }

                    }else
                    {
                        //点击了确定按钮

                        ProductDetailFrame.this.dispose();
                    }
                } catch (HdUIException uiEx) {
                    JOptionPane.showMessageDialog(uiEx.component, uiEx.message);
                    uiEx.component.requestFocus();
                }


            }
        });




    }
    /**
     * 显示产品详情
     * @param product
     * @Param component
     *
     */
    public      ProductDetailFrame(final Product product  )
    {

        super("产品详情[" + (product == null ? "新增" : ("货号：" + product.getName() + "---版本号：" + product.getpVersion())) + "]");
        init( );
        if(product==null)
        {

            panel_productDetail.setProductDetail(null);

        }
         else
        {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    loadProductDetail(product);
                }
            });

        }
    }


    @Override
    public void save() {


    }

    @Override
    public void delete() {



        if(productDelete!=null) return;


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

    @Override
    public void close() {
        setVisible(false);
        dispose();
    }




    /**
     * 加载产品详情信息
     */
    private void loadProductDetail(final Product product) {


        new HdSwingWorker<ProductDetail, Long>(this) {
            @Override
            protected RemoteData<ProductDetail> doInBackground() throws Exception {
                return apiManager.loadProductDetail(product.id);
            }

            @Override
            public void onResult(RemoteData<ProductDetail> data) {

                if(data.isSuccess()) {

                    ProductDetail detail = data.datas.get(0);

                    panel_productDetail.setProductDetail(detail);
                }else
                {

                    JOptionPane.showMessageDialog(ProductDetailFrame.this,data.message);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            close();
                        }
                    });

                }
            }
        }.go();


    }
}
