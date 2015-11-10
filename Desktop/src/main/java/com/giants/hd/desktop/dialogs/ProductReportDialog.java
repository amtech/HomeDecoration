package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.ProductTableModel;
import com.giants.hd.desktop.reports.products.Excel_ProductReport;
import com.giants.hd.desktop.utils.SwingFileUtils;
import com.giants.hd.desktop.viewImpl.LoadingDialog;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.interractor.UseCaseFactory;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import rx.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ProductReportDialog extends BaseDialog {
    private JPanel contentPane;
    private JTextField start;
    private JTextField end;
    private JCheckBox include;
    private JButton export;
    private JButton search;
    private JHdTable jt;
    private JButton export1;
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

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String startNum = start.getText().trim();
                String endNum = end.getText().trim();
                boolean withCopy = include.isSelected();


                UseCaseFactory.createProductByNameBetween(startNum, endNum, withCopy).execute(new Subscriber<java.util.List<Product>>() {
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
    }









}
