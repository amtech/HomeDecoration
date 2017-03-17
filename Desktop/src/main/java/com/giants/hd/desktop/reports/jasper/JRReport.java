package com.giants.hd.desktop.reports.jasper;

import com.giants.hd.desktop.reports.Reportable;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * jasper 报表基类
 *
 * Created by davidleen29 on 2017/3/16.
 */
public  abstract  class JRReport implements Reportable {


    @Override
    public   void report()
    {

        try {
            final InputStream inputStream = getReportFile();
            JasperReport jr= JasperCompileManager.compileReport(inputStream);


            JasperPrint jp= JasperFillManager.fillReport(jr,getParameters(),getDataSource());
            JasperViewer jasperViewer = new JasperViewer(jp,false);
            jasperViewer.setVisible(true);

            //inputStream.close();
        } catch (JRException e) {
            e.printStackTrace();
        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    public  abstract  JRDataSource getDataSource();
    public  abstract InputStream getReportFile();
    public  abstract Map<String,Object> getParameters();
}
