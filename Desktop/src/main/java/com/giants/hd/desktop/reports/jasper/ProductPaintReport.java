package com.giants.hd.desktop.reports.jasper;

import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.entity.ProductPaint;
import com.giants3.hd.utils.noEntity.ProductDetail;
import de.greenrobot.common.io.FileUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.eclipse.jdt.internal.compiler.util.FloatUtil;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 颜料清单报表
 * Created by david on 2016/3/13.
 */
public class ProductPaintReport {




    public void export(ProductDetail productDetail,String orderName, int qty)
    {
        String fileName="F:\\hd\\jasper\\Blank_A4.jrxml";



        try {
            JasperReport jr= JasperCompileManager.compileReport( fileName);
            OrderItemReportData orderItemReportData=new OrderItemReportData();
            orderItemReportData.orderName=orderName;
            orderItemReportData.itemQty=qty;
            orderItemReportData.amount= FloatHelper.scale((productDetail.product.paintCost+productDetail.product.paintWage)*qty);
            orderItemReportData.deliverDate="2016-07-08";
            orderItemReportData.unit=productDetail.product.pUnitName;
            orderItemReportData.pcAmount=FloatHelper.scale(productDetail.product.paintCost+productDetail.product.paintWage);
            orderItemReportData.materialCost=FloatHelper.scale(productDetail.product.paintCost);
            orderItemReportData.salary=FloatHelper.scale(productDetail.product.paintWage);

            orderItemReportData.reportDate= DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime());
            orderItemReportData.url= HttpUrl.loadProductPicture(productDetail.product.url);
            orderItemReportData.prdName=productDetail.product==null?"test":productDetail.product.name;
            ReportData reportData=new ReportData(orderItemReportData);

            java.util.List<ProductPaint> paintList=productDetail.paints;
            int paintSize=paintList.size();
            java.util.List<OrderProductPaint> orderProductPaints=new ArrayList<>(paintSize);
            for(ProductPaint productPaint:paintList)
            {
                OrderProductPaint orderProductPaint=new OrderProductPaint();
                orderProductPaint.setProductPaint(productPaint);
                orderProductPaint.setQty(qty);
                orderProductPaints.add(orderProductPaint);
            }

           JasperPrint jp= JasperFillManager.fillReport(jr,reportData,new CustomBeanDataSource(orderProductPaints));
        //    JasperViewer viewer =JasperViewer.viewReport(jp);

            JasperViewer jasperViewer = new JasperViewer(jp,false);

            jasperViewer.setVisible(true);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
