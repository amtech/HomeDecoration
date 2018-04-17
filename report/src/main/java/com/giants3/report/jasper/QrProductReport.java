package com.giants3.report.jasper;

import com.giants3.hd.entity.Product;
import com.giants3.hd.noEntity.ProductAgent;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davidleen29 on 2018/4/14.
 */
public class QrProductReport extends JRPreviewReport {


    private Product product;
    private final String jasperFileName;
    private final int count;
    private String qrFilePath;

    public QrProductReport(Product product,String qrFilePath,String jasperFileName,int count) {
        this.qrFilePath = qrFilePath;


        this.product = product;
        this.jasperFileName = jasperFileName;
        this.count = count;
    }

    @Override
    public JRDataSource getDataSource() {
        java.util.List<Product> list = new ArrayList<>();

        //a4 一版 3*6
        for (int i = 0; i < count; i++) {

            list.add(product);

        }

        return new CustomBeanDataSource(list){


            @Override
            public Object getFieldValue(JRField field) throws JRException {


                String propertiyName=super.getPropertyName(field);
            //    System.out.println(propertiyName);
                switch (propertiyName)
                {
                    case "unit":
                        return product.getpUnitName();
                     case "pack":
                        return ProductAgent.getProductFullPackageInfo(product);
                    case "name":
                        return product.name;
                    case "url":

                        return qrFilePath;


                }

               return "";
            }


        };
    }

    @Override
    public InputStream getReportFile() {
        return QrProductReport.class.getClassLoader().getResourceAsStream("jasper/"+jasperFileName+".jrxml");
    }

    @Override
    public Map<String, Object> getParameters() {

      return new HashMap<>();



    }




}
