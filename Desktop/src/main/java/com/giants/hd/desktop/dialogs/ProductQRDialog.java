package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.utils.QRHelper;
import com.giants3.hd.appdata.QRProduct;
import com.giants3.hd.utils.entity.Product;

import javax.swing.*;
import java.awt.*;

public class ProductQRDialog extends BaseDialog<Product> {
    private final Window window;
    private final Product product;
    private JPanel contentPane;
    private JLabel iv_qr;



    public ProductQRDialog(Window window,Product product) {
        super(window, "二维码");
        this.window = window;
        this.product = product;
        setContentPane(contentPane);


        QRProduct qrProduct=  generate(product);







        iv_qr.setIcon(new ImageIcon( QRHelper.generateQRCode(qrProduct)));

       ;




    }


    private QRProduct generate(Product product)
    {
        QRProduct qrProduct=new QRProduct();


            qrProduct.id=product.id;
            qrProduct.name=product.name;

            qrProduct.pVersion=product.pVersion;
            qrProduct. unitName=product.pUnitName;
            qrProduct.className=product.pClassName;
            return qrProduct;

    }






}
