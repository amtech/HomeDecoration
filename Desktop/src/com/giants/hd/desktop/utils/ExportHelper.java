package com.giants.hd.desktop.utils;

import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.ProductDetail;
import com.giants3.hd.utils.entity.ProductMaterial;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Boolean;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 导出功能帮助类。
 */
public class ExportHelper {


    /**
     * 导出材料清单至指定路劲下。
     * @param detail
     * @param path
     */

    public static void export(ProductDetail detail,String path) throws IOException, WriteException {








        export(detail, path, 0);

        export(detail, path,1);
        export(detail, path,3);




















    }


    private static void export(ProductDetail detail,String path,int index) throws IOException, WriteException {


        String nameAppendix="";
        List<ProductMaterial>  datas = null;
        switch (index)
        {
            case 0:
                nameAppendix="白胚";
                datas=detail.conceptusMaterials;
                break;
            case 1:
                nameAppendix="组装";
                datas=detail.assembleMaterials;
            break;
            case 2:



                break;
            case 3:
                nameAppendix="包装";
                datas=detail.packMaterials;
                break;
        }

        String  fileName=path+ File.separator+detail.product.name+ (StringUtils.isEmpty(detail.product.pVersion)?"":("-"+detail.product.pVersion))+"_"+nameAppendix+".xls";
        WritableWorkbook wwb= Workbook.createWorkbook(new File(fileName));
        WritableSheet writableSheet= wwb.createSheet(detail.product.name, 0);


        writableSheet.setColumnView(0,50);
        writableSheet.setColumnView(1,20);

        writableSheet.setColumnView(2,40);
        writableSheet.setColumnView(3,20);
        //Create Cells with contents of different data types.
        //Also specify the Cell coordinates in the constructor
        Label label1 = new Label(0, 0, "子件代码");
        Label label2 = new Label(1, 0, "用量");
        Label label3 = new Label(02, 0, "特征");
        Label label4 = new Label(3, 0, "损耗");
//        DateTime date = new DateTime(1, 0, new Date());
//        Boolean bool = new Boolean(2, 0, true);
//        Number num = new Number(3, 0, 9.99);

        //Add the created Cells to the sheet
        writableSheet.addCell(label1);
        writableSheet.addCell(label2);
        writableSheet.addCell(label3);
        writableSheet.addCell(label4);

        if(datas==null) return;

        WritableCellFormat discountFormat = new WritableCellFormat(new NumberFormat("0.00"));
        WritableCellFormat quotaFormat = new WritableCellFormat(new NumberFormat("0.00000"));

        Number num;
        int startRow=1;

        //白胚
        for(ProductMaterial productMaterial:datas)
        {


            label1 = new Label(0, startRow, productMaterial.materialCode);
            writableSheet.addCell(label1);

            num = new Number(1, startRow,  productMaterial.quota ,quotaFormat);
            writableSheet.addCell(num);
            if(index==3)
            {
                label1 = new Label(2, startRow, (productMaterial.pLong>0?(productMaterial.pLong):"")+(productMaterial.pWidth>0?("*"+productMaterial.pWidth):"")+(productMaterial.pHeight>0?("*"+productMaterial.pHeight ):""));
                writableSheet.addCell(label1);
            }



            num = new Number(3, startRow, 1-productMaterial.available,discountFormat);
            writableSheet.addCell(num);


            startRow++;
        }


        //Write and close the workbook
        wwb.write();
        wwb.close();

    }




}
