package com.giants.hd.desktop.reports.jasper;

import com.giants.hd.desktop.local.LocalFileHelper;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.entity.ProductPaint;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.noEntity.ProductDetail;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 颜料清单报表
 * Created by david on 2016/3/13.
 */
public class ProductPaintReport {




    public void export(ProductDetail productDetail, ErpOrder order, ErpOrderItem orderItem)
    {
        String fileName="F:\\hd\\jasper\\Blank_A4.jrxml";


        try {
            InputStream inputStream= ProductPaintReport.class.getClassLoader().getResourceAsStream("jasper/Blank_A4.jrxml");


            JasperReport jr= JasperCompileManager.compileReport( inputStream);
            OrderItemReportData orderItemReportData=new OrderItemReportData();
            orderItemReportData.orderName=order.os_no;
            int qty=orderItem.qty;
            orderItemReportData.itemQty=qty;
            orderItemReportData.amount= FloatHelper.scale((productDetail.product.paintCost+productDetail.product.paintWage)*qty);
            orderItemReportData.deliverDate=order.est_dd;
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
            LocalFileHelper.printThrowable(e);
        }
    }
}
