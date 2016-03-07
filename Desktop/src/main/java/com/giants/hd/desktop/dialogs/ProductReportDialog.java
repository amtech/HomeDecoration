package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.ProductTableModel;
import com.giants.hd.desktop.reports.products.Excel_ProductReport;
import com.giants.hd.desktop.utils.HdSwingUtils;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.viewImpl.LoadingDialog;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Product;
import rx.Subscriber;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

public class ProductReportDialog extends BaseDialog<Void> {
    private JPanel contentPane;
    private JTextField start;
    private JTextField end;
    private JCheckBox include;
    private JButton export;
    private JButton search;
    private JHdTable jt;
    private JButton export1;
    private JTextField tf_random;
    private JButton search_random;
    ProductTableModel model;


    private java.util.List<Product> products;

    //加载进度条
    LoadingDialog dialog;


    public ProductReportDialog(Window window) {
        super(window,"产品信息批量导出");
        setMinimumSize(new Dimension(1000,600));
        setContentPane(contentPane);

        model=new ProductTableModel();
        jt.setModel(model );
        dialog=new LoadingDialog(this, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jt.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {


                    int row = jt.getSelectedRow();
                    Product product = model.getItem(row);


                    int column = jt.convertColumnIndexToModel(jt.getSelectedColumn());
                    //单击第一列 显示原图
                    if (column == 0) {
                        ImageViewDialog.showProductDialog(ProductReportDialog.this, product.getName(), product.getpVersion(),product.url);
                    } else {

                        HdSwingUtils.showDetailPanel(product, ProductReportDialog.this);

                    }


                }

            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String startNum = start.getText().trim();
                String endNum = end.getText().trim();
                boolean withCopy = include.isSelected();


                UseCaseFactory.getInstance().createProductByNameBetween(startNum, endNum, withCopy).execute(new Subscriber<java.util.List<Product>>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                       dialog.setVisible(false);
                        JOptionPane.showMessageDialog(ProductReportDialog.this, e.getMessage());

                    }

                    @Override
                    public void onNext(java.util.List<Product> newProducts) {

                        products=newProducts;
                        dialog.setVisible(false);
                        model.setDatas(products);


                    }
                });
                dialog.setVisible(true);

            }


        });



        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(products==null||products.size()==0)
                {
                    JOptionPane.showMessageDialog(ProductReportDialog.this, "无数据导出");
                    return;
                }

                final    File file= SwingFileUtils.getSelectedDirectory();
                if(file==null) return;
                new HdSwingWorker<Void,Void>(ProductReportDialog.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {
                        new Excel_ProductReport().reportProduct2(products, file.getPath());
                        return new RemoteData<Void>();
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {


                        JOptionPane.showMessageDialog(ProductReportDialog.this, "导出成功");
                    }
                }.go();

            }
        });


        export1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(products==null||products.size()==0)
                {
                    JOptionPane.showMessageDialog(ProductReportDialog.this, "无数据导出");
                    return;
                }

              final    File file= SwingFileUtils.getSelectedDirectory();
                if(file==null) return;
                new HdSwingWorker<Void,Void>(ProductReportDialog.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {
                        new Excel_ProductReport().reportProduct1(products, file.getPath());
                        return new RemoteData<Void>();
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {


                        JOptionPane.showMessageDialog(ProductReportDialog.this, "导出成功");
                    }
                }.go();





            }
        });


        search_random.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String s=tf_random.getText().trim();
                if(StringUtils.isEmpty(s))
                {
                    JOptionPane.showMessageDialog(ProductReportDialog.this, "请输入产品货号");
                    tf_random.requestFocus();
                    return ;
                }
                UseCaseFactory.getInstance().createProductByNameRandom(s).execute(new Subscriber<java.util.List<Product>>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.setVisible(false);
                        JOptionPane.showMessageDialog(ProductReportDialog.this, e.getMessage());

                    }

                    @Override
                    public void onNext(java.util.List<Product> newProducts) {

                        products=newProducts;
                        dialog.setVisible(false);
                        model.setDatas(products);


                    }
                });
                dialog.setVisible(true);

            }



        });
    }












}
