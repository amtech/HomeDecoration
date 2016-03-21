package com.giants.hd.desktop.reports.jasper;

import de.greenrobot.common.io.FileUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 颜料清单报表
 * Created by david on 2016/3/13.
 */
public class ProductPaintReport {




    public void export( )
    {
        String fileName="F:\\hd\\jasper\\Blank_A4.jrxml";



        try {
            JasperReport jr= JasperCompileManager.compileReport( fileName);
            Map data=new HashMap();
            data.put("ReportTitle","aaaaaaaa");
           JasperPrint jp= JasperFillManager.fillReport(jr,data,new CustomBeanDataSource(TestFactory.generateCollection()));
        //    JasperViewer viewer =JasperViewer.viewReport(jp);
            JasperViewer jasperViewer = new JasperViewer(jp);
            jasperViewer.setVisible(true);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
