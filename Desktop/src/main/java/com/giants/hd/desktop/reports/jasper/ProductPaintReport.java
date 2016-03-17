package com.giants.hd.desktop.reports.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;
import java.util.Map;

/**
 * 颜料清单报表
 * Created by david on 2016/3/13.
 */
public class ProductPaintReport {




    public void export()
    {
        String fileName="F:\\hd\\jasper\\Blank_A4.jrxml";
        try {
            JasperReport jr= JasperCompileManager.compileReport(fileName);
            Map data=new HashMap();
           JasperPrint jp= JasperFillManager.fillReport(jr,data,new CustomBeanDataSource(TestFactory.generateCollection()));
        //    JasperViewer viewer =JasperViewer.viewReport(jp);
            JasperViewer jasperViewer = new JasperViewer(jp);
            jasperViewer.setVisible(true);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
