package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
import com.google.inject.Guice;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 导出情感发票单
 * Created by davidleen29 on 2016/8/7.
 */
public class Report_Excel_StockOut_Qingguan_Invoice extends AbstractExcelReporter<ErpStockOutDetail> {

    public static final String FILE_NAME = "福州云飞清关发票";
    private static final String TEMPLATE_FILE_NAME = "excel/" + FILE_NAME + ".xls";
    int styleRow = 10;

    Workbook workbook;

    public Report_Excel_StockOut_Qingguan_Invoice() {


    }


    @Override
    public void report(ErpStockOutDetail data, String fileOutputDirectory) throws IOException, HdException {


        //以包起始的地方开始   jar 根目录开始。
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_NAME);
        String fileName = fileOutputDirectory + File.separator + data.erpStockOut.ck_no + "_" + FILE_NAME + "_" + DateFormats.FORMAT_YYYY_MM_DD.format(Calendar.getInstance().getTime()) + ".xls";
        //Create Workbook instance holding reference to .xlsx file
        workbook = new HSSFWorkbook(inputStream);
        writeOnExcel(data, workbook);
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        workbook.close();


        fos.close();


    }

    /**
     * 报表
     *
     * @param data
     * @param workbook
     */
    private void writeOnExcel(ErpStockOutDetail data, Workbook workbook) {


//        Sheet writableSheet = workbook.getSheetAt(0);
//
//
//
//        List<ErpStockOutItem> items = data.items;
//        int dataSize = items.size();
//        int defaultRowCount = 1;
//        int startItemRow = 14;
////
//        //实际数据超出范围 插入空行
//        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);
//        int row = 0;
//        for (int i = 0; i < dataSize; i++) {
//            row = i + startItemRow;
//            ErpStockOutItem outItem = items.get(i);
//            addString(writableSheet, outItem.bat_no, 0, row);
//            addString(writableSheet, outItem.describe, 1, row);
//
//            //包装
//            addString(writableSheet, outItem.khxg, 3, row);
//            addString(writableSheet, outItem.unit, 4, row);
//
//            addNumber(writableSheet, outItem.stockOutQty, 5, row);
//            addNumber(writableSheet, outItem.up, 6, row);
//            float amt = FloatHelper.scale(outItem.stockOutQty * outItem.up
//            );
//            addNumber(writableSheet, amt, 7, row);
//            addString(writableSheet, outItem.cus_os_no, 8, row);
//
////            //材料长宽高
////            addNumber(writableSheet, outItem.pLong, 4, row);
////            addNumber(writableSheet, outItem.pWidth, 5, row);
////            addNumber(writableSheet, outItem.pHeight, 6, row);
////            //材料长宽高
////            addNumber(writableSheet, outItem.wLong, 7, row);
////            addNumber(writableSheet, outItem.wWidth, 8, row);
////            addNumber(writableSheet, outItem.wHeight, 9, row);
////
////            //定额
////            addNumber(writableSheet, outItem.quota, 10, row);
////            //单位
////            addString(writableSheet, outItem.unitName, 11, row);
////            //利用率
////            addNumber(writableSheet, outItem.available, 12, row);
////
////            //类型
////            addNumber(writableSheet, outItem.type, 13, row);
////            //单价
////            addNumber(writableSheet, outItem.price, 14, row);
////            //金额
////            addNumber(writableSheet, outItem.amount, 15, row);
////
////            //备注
////            addString(writableSheet, outItem.memo, 16, row);
//        }
//
//
//        row++;
//        //添加上汇总行
//        int totalQty = 0;
//        float totalAmt = 0;
//        for (int i = 0; i < dataSize; i++) {
//
//            totalQty += items.get(i).stockOutQty;
//            totalAmt += FloatHelper.scale(items.get(i).stockOutQty * items.get(i).up
//            );
//            ;
//        }
//        addNumber(writableSheet, totalQty, 5, row);
//        addNumber(writableSheet, totalAmt, 7, row);


        exportList(data, workbook);
        exportInvoice(data, workbook);
        exportOrder(data, workbook);

    }


    /**
     * 导出sheet头部部分。三个sheet 头部一样
     */
    public void exportSheetHead(ErpStockOutDetail data, Sheet writableSheet) {
        //发票单号
        addString(writableSheet, "Invoice No.:" + data.erpStockOut.ck_no, 5, 4);
        //Po号
        addString(writableSheet, "PO#:", 5, 5);
        // S/C NO:
        addString(writableSheet, "S/C NO:", 5, 6);
        //Date:
        addString(writableSheet, "Date:" + data.erpStockOut.ck_dd, 5, 7);
        //Country of origin:
        //  addString(writableSheet, "Country of origin:"+"FUZHOU", 5, 8);

        //客户信息

        addString(writableSheet, data.erpStockOut.adr2, 0, 5);
        addString(writableSheet, data.erpStockOut.tel1, 0, 6);
        addString(writableSheet, data.erpStockOut.fax, 0, 7);


        //目的港
        String mdgText = "FROM FUZHOU TO %s BY SEA";
        addString(writableSheet, String.format(mdgText, data.erpStockOut.mdg), 0, 10);
        //所有唛头数据
        StringBuilder  maitous=new StringBuilder();

        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);

        Set<String> orders=new HashSet<>();
        Set<String> pos=new HashSet<>();
        for(ErpStockOutItem item :data.items)
        {
            orders.add(item.os_no);
            pos.add(item.cus_os_no);
        }
        for(String os_no:orders)
        {

            try {
                RemoteData<ErpOrderDetail>  remoteData=apiManager.getOrderDetail(os_no);
                if (remoteData.isSuccess())
                {
                    String zhengmai=remoteData.datas.get(0).erpOrder.zhengmai;
                    if(!StringUtils.isEmpty(zhengmai))
                        maitous.append(zhengmai).append(",");
                }
            } catch (HdException e) {
                e.printStackTrace();
            }
        }

        if(maitous.length()>1)
            maitous.setLength(maitous.length()-1);
        //
        addString(writableSheet, maitous.toString(), 1, 11);

        addString(writableSheet,"PO#:"+ StringUtils.combine(pos), 5, 5);

    }


    /**
     * 合同sheet数据
     */
    public void exportOrder(ErpStockOutDetail data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(2);
        exportSheetHead(data, writableSheet);

        List<ErpStockOutItem> items = data.items;
        int dataSize = items.size();
        int defaultRowCount = 1;
        int startItemRow = 24;
//
        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize);
        int row = 0;
        for (int i = 0; i < dataSize; i++) {
            row = i + startItemRow;
            ErpStockOutItem outItem = items.get(i);
            addString(writableSheet, outItem.bat_no, 0, row);
            addString(writableSheet, outItem.prd_no, 1, row);
            addString(writableSheet, outItem.unit, 2, row);
            //净重
            addNumber(writableSheet, outItem.stockOutQty, 3, row);
            //数量
            addNumber(writableSheet, outItem.qty, 4, row);
//描述
           // addString(writableSheet, outItem.describe, 5, row);
            //fob

            addNumber(writableSheet, outItem.up, 6, row);

            //amount
            addNumber(writableSheet, FloatHelper.scale(outItem.up * outItem.stockOutQty), 7, row);

            //cbm
            addNumber(writableSheet, outItem.xgtj, 8, row);

            addString(writableSheet, outItem.khxg, 9, row);

        }


        row++;
        //添加上汇总行
        int totalQty = 0;
        int totalStockOutQty = 0;
        float totalAmt = 0;
        for (int i = 0; i < dataSize; i++) {

            totalStockOutQty += items.get(i).stockOutQty;
            totalQty += items.get(i).qty;
            totalAmt += FloatHelper.scale(items.get(i).stockOutQty * items.get(i).up
            );
            ;
        }
        addString(writableSheet, totalStockOutQty + " /CTNS", 3, row);
        addString(writableSheet, totalQty + " /PCS", 4, row);
        addNumber(writableSheet, totalAmt, 7, row);


    }

    /**
     * 发票sheet数据
     */
    public void exportInvoice(ErpStockOutDetail data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(1);
        exportSheetHead(data, writableSheet);

        //所关联的所有订单号。
        Set<String> orders = new HashSet<>();
        //分类柜号 封签号
        Set<String> guihaoSet = new HashSet<>();
        Map<String, String> guihaoMap = new HashMap();
        Map<String, List<ErpStockOutItem>> groupMaps = new HashMap<>();

        for (ErpStockOutItem item : data.items) {

            String guihao = StringUtils.isEmpty(item.guihao) ? "" : item.guihao;
            guihaoSet.add(guihao);
            guihaoMap.put(guihao, item.fengqianhao);
            List<ErpStockOutItem> guiItems = groupMaps.get(guihao);
            if (guiItems == null) {
                guiItems = new ArrayList<>();
                groupMaps.put(guihao, guiItems);
            }
            orders.add(item.os_no);
            guiItems.add(item);

        }


        //
        //   addString(writableSheet, maitous.toString(), 0, 10);
        //未进行分柜的 显示在前


        Set<String> keys = groupMaps.keySet();
        List<ErpStockOutItem> items = data.items;
        int dataSize = items.size();
        int defaultRowCount = 1;
        int startItemRow = 24;


        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize+keys.size());



        int row = startItemRow;
        for (String guihao :keys) {


           String fengqianhao=guihaoMap.get(guihao);
            //合并单元格
            combineRowAndCell(writableSheet,row,row,0,8);
            setCellAlignLeftCenter(workbook,writableSheet,row,0);
            addString(writableSheet,guihao+"& seal # : "+fengqianhao,0,row );
            row++;
           List<ErpStockOutItem> groupItems = groupMaps.get(guihao);
            for (ErpStockOutItem outItem : groupItems) {

                addString(writableSheet, outItem.bat_no, 0, row);
                addString(writableSheet, outItem.prd_no, 1, row);
                addString(writableSheet, outItem.unit, 2, row);
                //净重
                addNumber(writableSheet, outItem.stockOutQty, 3, row);
                //数量
                addNumber(writableSheet, outItem.qty, 4, row);
//描述
              //  addString(writableSheet, outItem.describe, 5, row);
                //fob

                addNumber(writableSheet, outItem.up, 6, row);

                //amount
                addNumber(writableSheet, FloatHelper.scale(outItem.up * outItem.stockOutQty), 7, row);

                //Product size (inch)
                addString(writableSheet, outItem.khxg, 8, row);



                row++;

            }


        }


        //添加上汇总行
        int totalQty = 0;
        int totalStockOutQty = 0;
        float totalAmt = 0;
        for (int i = 0; i < dataSize; i++) {

            totalStockOutQty += items.get(i).stockOutQty;
            totalQty += items.get(i).qty;
            totalAmt += FloatHelper.scale(items.get(i).stockOutQty * items.get(i).up
            );
            ;
        }
        addString(writableSheet, totalStockOutQty + " /CTNS", 3, row);
        addString(writableSheet, totalQty + " /PCS", 4, row);
        addNumber(writableSheet, totalAmt, 7, row);

    }

    /**
     * 清单sheet数据
     */
    public void exportList(ErpStockOutDetail data, Workbook workbook) {


        Sheet writableSheet = workbook.getSheetAt(0);
        exportSheetHead(data, writableSheet);

        //所关联的所有订单号。
        Set<String> orders = new HashSet<>();
        //分类柜号 封签号
        Set<String> guihaoSet = new HashSet<>();
        Map<String, String> guihaoMap = new HashMap();
        Map<String, List<ErpStockOutItem>> groupMaps = new HashMap<>();

        for (ErpStockOutItem item : data.items) {

            String guihao = StringUtils.isEmpty(item.guihao) ? "" : item.guihao;
            guihaoSet.add(guihao);
            guihaoMap.put(guihao, item.fengqianhao);
            List<ErpStockOutItem> guiItems = groupMaps.get(guihao);
            if (guiItems == null) {
                guiItems = new ArrayList<>();
                groupMaps.put(guihao, guiItems);
            }
            orders.add(item.os_no);
            guiItems.add(item);

        }
        //所有唛头数据
        StringBuilder  maitous=new StringBuilder();

        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        for(String os_no:orders)
        {

            try {
                RemoteData<ErpOrderDetail>  remoteData=apiManager.getOrderDetail(os_no);
                if (remoteData.isSuccess())
                {
                    String zhengmai=remoteData.datas.get(0).erpOrder.zhengmai;
                    if(!StringUtils.isEmpty(zhengmai))
                        maitous.append(zhengmai).append(",");
                }
            } catch (HdException e) {
                e.printStackTrace();
            }
        }

        if(maitous.length()>1)
            maitous.setLength(maitous.length()-1);
        //
        addString(writableSheet, maitous.toString(), 1, 11);

        //未进行分柜的 显示在前


        Set<String> keys = groupMaps.keySet();
        List<ErpStockOutItem> items = data.items;
        int dataSize = items.size();
        int defaultRowCount = 1;
        int startItemRow = 25;


        //实际数据超出范围 插入空行
        duplicateRow(workbook, writableSheet, startItemRow, defaultRowCount, dataSize+keys.size());


        //体积
        float tiji=0;
        //净重
        float jz=0;
        //净重
        float mz=0;
        int row = startItemRow;
        for (String guihao :keys) {


            String fengqianhao=guihaoMap.get(guihao);
            //合并单元格
            combineRowAndCell(writableSheet,row,row,0,8);
            setCellAlignLeftCenter(workbook,writableSheet,row,0);
            addString(writableSheet,guihao+"& seal # : "+fengqianhao,0,row );
            row++;
            List<ErpStockOutItem> groupItems = groupMaps.get(guihao);
            for (ErpStockOutItem outItem : groupItems) {

                addString(writableSheet, outItem.bat_no, 0, row);
                addString(writableSheet, outItem.prd_no, 1, row);
                addString(writableSheet, outItem.unit, 2, row);
                //净重
                addNumber(writableSheet, outItem.stockOutQty, 3, row);
                //数量
                addNumber(writableSheet, outItem.qty, 4, row);
//描述
           //     addString(writableSheet, outItem.describe, 5, row);


                //包装相关
                addNumber(writableSheet, Float.valueOf(outItem.so_zxs), 6, row);

                addString(writableSheet, "/", 7, row);


                float[] xg = StringUtils.decouplePackageString(outItem.khxg);
                addNumber(writableSheet, xg[0], 8, row);
                addString(writableSheet, "*", 9, row);
                addNumber(writableSheet, xg[1], 10, row);
                addString(writableSheet, "*", 11, row);
                addNumber(writableSheet, xg[2], 12, row);





                tiji+= (xg[0]*xg[1]*xg[2])/1000000*outItem.stockOutQty;
                //ttl cbm
                addNumber(writableSheet, FloatHelper.scale((xg[0]*xg[1]*xg[2])/1000000*outItem.stockOutQty), 13, row);


                //ttl nw
                addNumber(writableSheet, FloatHelper.scale(outItem.jz1*outItem.stockOutQty), 14, row);
                //TTL G.W       (kgs)
                addNumber(writableSheet, FloatHelper.scale(outItem.mz*outItem.stockOutQty), 15, row);

                //nw
                addNumber(writableSheet, outItem.jz1, 16, row);



                //Product size (inch)
                addString(writableSheet, outItem.khxg, 17, row);






                row++;

            }


        }


        //添加上汇总行
        int totalQty = 0;
        int totalStockOutQty = 0;
        float totalAmt = 0;
        for (int i = 0; i < dataSize; i++) {

            totalStockOutQty += items.get(i).stockOutQty;
            totalQty += items.get(i).qty;
            totalAmt += FloatHelper.scale(items.get(i).stockOutQty * items.get(i).up
            );
            jz+= FloatHelper.scale(items.get(i).stockOutQty* items.get(i).jz1);

            mz+= FloatHelper.scale(items.get(i).stockOutQty* items.get(i).mz);

        }
        addString(writableSheet, totalStockOutQty + " /CTNS", 3, row);
        addString(writableSheet, totalQty + " /PCS", 4, row);
        addString( writableSheet, FloatHelper.scale(tiji)+"CBM", 13, row);
        addString( writableSheet, FloatHelper.scale(jz)+"KGS", 14, row);
        addString( writableSheet, FloatHelper.scale(mz)+"KGS", 15, row);



        row++;
        addNumber( writableSheet, FloatHelper.scale(tiji) , 3, row);
        row++;
        addNumber( writableSheet, FloatHelper.scale(jz) , 3, row);
        row++;
        addNumber( writableSheet, FloatHelper.scale(mz) , 3, row);


    }






    private void exportItem(Sheet sheet,ErpStockOutItem item, int  rowIndex)
    {

    }
}
