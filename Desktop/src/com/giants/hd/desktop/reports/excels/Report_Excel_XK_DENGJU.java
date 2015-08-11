package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.HttpUrl;
import com.giants3.hd.utils.entity.ProductDetail;
import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationItem;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.write.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/** ���۵����̿��ƾ�ģ��
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_DENGJU extends ExcelReportor {


    public Report_Excel_XK_DENGJU(String modelName) {
        super(modelName);
    }

    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException, HdException {

        int defaultRowCount=10;

        int startRow=4;


        int rowHeight = writableSheet.getRowHeight(startRow);



        int dataSize=quotationDetail.items.size();



        //ʵ�����ݳ�����Χ �������
        if(dataSize>defaultRowCount)
        {
            //�������
            for(int j=0;j<dataSize-defaultRowCount;j++) {

                int rowToInsert=startRow+defaultRowCount+j;
                writableSheet.insertRow(rowToInsert);
                writableSheet.setRowView(rowToInsert, rowHeight);
                //���Ʊ��
                for (int i = 0, count = writableSheet.getColumns(); i < count; i++) {
                    WritableCell cell = (WritableCell) writableSheet.getCell(i, startRow);
                    cell = cell.copyTo(i, rowToInsert);
                    writableSheet.addCell(cell);

                }
            }
        }




        //�������

        Label label1;
        jxl.write.Number num;
        WritableImage  image;

        int rowCount=  writableSheet.getRows();
        int columnCount=  writableSheet.getColumns();
        WritableCellFormat format=new WritableCellFormat();
        format.setAlignment(jxl.format.Alignment.CENTRE);
        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        format.setWrap(true);
        format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        //��������
        //��ƺ�  �汾��
        label1 = new Label(7, 1, quotationDetail.quotation.qDate,format);
        writableSheet.addCell(label1);




        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap=0.1f;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationItem item=quotationDetail.items.get(i);



            //ͼƬ
            if(item.productPhoto!=null) {

                baos.reset();
                BufferedImage bufferedImage= ImageIO.read(new URL(HttpUrl.loadProductPicture(item.productName, item.pVersion)));
                ImageIO.write(bufferedImage, "PNG", baos);
                image = new WritableImage(4+pictureGap/2, rowUpdate+pictureGap/2,1-pictureGap, 1-pictureGap, baos.toByteArray());
                image.setImageAnchor(WritableImage.MOVE_AND_SIZE_WITH_CELLS);
                writableSheet.addImage(image);
            }


            //��ȡ�̿�����
            ProductDetail productDetail=    apiManager.loadProductDetail(item.productId).datas.get(0);



            //�к�
            //��ƺ�  �汾��
            label1 = new Label(0, rowUpdate,String.valueOf(i+1),format);
            writableSheet.addCell(label1);

            //��ƺ�  �汾��
            label1 = new Label(1, rowUpdate, item.pVersion,format);
            writableSheet.addCell(label1);

            //����
            label1 = new Label(2, rowUpdate, item.productName.trim(),format);
            writableSheet.addCell(label1);





            //���ϱ���
            //����
            label1 = new Label(8, rowUpdate,  productDetail.product.constitute);
            writableSheet.addCell(label1);

            if(productDetail.product.xiankang!=null)
            {


                //ͬ�����
                label1 = new Label(3, rowUpdate, productDetail.product.xiankang.getQitahuohao() ,format);
                writableSheet.addCell(label1);

                //��ȩ��ʾ
                label1 = new Label(9, rowUpdate,  productDetail.product.xiankang.getJiaquan(),format);
                writableSheet.addCell(label1);

                //����
                label1 = new Label(10, rowUpdate,  productDetail.product.xiankang.getCaizhi(),format);
                writableSheet.addCell(label1);
            }

        }





    }
}
