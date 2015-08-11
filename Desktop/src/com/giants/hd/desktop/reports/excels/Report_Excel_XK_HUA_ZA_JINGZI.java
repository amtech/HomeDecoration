package com.giants.hd.desktop.reports.excels;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.HttpUrl;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import jxl.format.*;
import jxl.write.*;
import jxl.write.BorderLineStyle;
import jxl.write.biff.RowsExceededException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**�̿� ����  ������ģ��
 * Created by davidleen29 on 2015/8/6.
 */
public class Report_Excel_XK_HUA_ZA_JINGZI extends ExcelReportor {


    public Report_Excel_XK_HUA_ZA_JINGZI(String modelName) {
        super(modelName);
    }

    @Override
    protected void writeOnExcel(QuotationDetail quotationDetail, WritableSheet writableSheet) throws WriteException, IOException, HdException {






        int defaultRowCount=10;

        int startRow=5;


        int rowHeight = writableSheet.getRowHeight(startRow);



        int dataSize=quotationDetail.XKItems.size();



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
        WritableImage image;

        int rowCount=  writableSheet.getRows();
        int columnCount=  writableSheet.getColumns();
        WritableCellFormat format=new WritableCellFormat();
        format.setAlignment(jxl.format.Alignment.CENTRE);
        format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        format.setWrap(true);
        format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        //��������
        label1 = new Label(5, 1, quotationDetail.quotation.qDate,format);
        writableSheet.addCell(label1);



                //��������
                label1 = new Label(14, 1, "Verdoer YUNFEI",format);
        writableSheet.addCell(label1);



        ApiManager apiManager= Guice.createInjector().getInstance(ApiManager.class);
        float pictureGap=0.1f;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(int i=0;i<dataSize;i++)
        {
            int rowUpdate=startRow+i;
            QuotationXKItem item=quotationDetail.XKItems.get(i);



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

            //��ȡ�̿�����
            ProductDetail productDetail2=    apiManager.loadProductDetail(item.productId2).datas.get(0);

            //�к�
            label1 = new Label(0, rowUpdate,String.valueOf(i+1),format);
            writableSheet.addCell(label1);

            //��ƺ�  �汾��
            label1 = new Label(1, rowUpdate, item.pVersion,format);
            writableSheet.addCell(label1);

            //����
            label1 = new Label(2, rowUpdate, item.productName.trim(),format);
            writableSheet.addCell(label1);







            //��λ
            label1 = new Label(8, rowUpdate,  item.unit ,format);
            writableSheet.addCell(label1);

            if(productDetail.product.xiankang!=null)
            {


                //ͬ�����
                label1 = new Label(3, rowUpdate, productDetail.product.xiankang.getQitahuohao() ,format);
                writableSheet.addCell(label1);
                //���ϱ���

                label1 = new Label(6, rowUpdate,  productDetail.product.xiankang.getCaizhibaifenbi(),format);
                writableSheet.addCell(label1);
                //��ȩ��ʾ
                label1 = new Label(7, rowUpdate,  productDetail.product.xiankang.getJiaquan(),format);
                writableSheet.addCell(label1);

                //�߿�
                label1 = new Label(12, rowUpdate,  productDetail.product.xiankang.getBiankuang(),format);
                writableSheet.addCell(label1);


                //�ۿ�

                label1 = new Label(13, rowUpdate,  productDetail.product.xiankang.getCaokuan(),format);
                writableSheet.addCell(label1);

                //����
                label1 = new Label(14, rowUpdate,  productDetail.product.xiankang.getCaokuan(),format);
                writableSheet.addCell(label1);

                //���ӳߴ�	��
                label1 = new Label(15, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);

                //���ӳߴ�	��
                label1 = new Label(16, rowUpdate,  productDetail.product.xiankang.getJingzi_kuan(),format);
                writableSheet.addCell(label1);


                //ĥ��
                label1 = new Label(17, rowUpdate,  productDetail.product.xiankang.getMobian(),format);
                writableSheet.addCell(label1);
            }



            //�ۺм۸�
            label1 = new Label(10, rowUpdate, String.valueOf(item.price),format);
            writableSheet.addCell(label1);

            //��ǿ�۸�
            label1 = new Label(11, rowUpdate,  String.valueOf(item.price2),format);
            writableSheet.addCell(label1);





            //�ܳ�
            label1 = new Label(18, rowUpdate, String.valueOf(item.spec),format);
            writableSheet.addCell(label1);


            //�ܿ�
            label1 = new Label(19, rowUpdate, String.valueOf(item.spec),format);
            writableSheet.addCell(label1);

            //����
            label1 = new Label(20, rowUpdate,  String.valueOf(item.spec) ,format);
            writableSheet.addCell(label1);


            //����
            label1 = new Label(21, rowUpdate,  String.valueOf(item.weight) ,format);
            writableSheet.addCell(label1);



            //�ۺа�װ����
            label1 = new Label(22, rowUpdate,  String.valueOf(productDetail.product.xiankang.pack_memo) ,format);
            writableSheet.addCell(label1);

            //����װ
            label1 = new Label(28, rowUpdate,  String.valueOf(item.packQuantity) ,format);
            writableSheet.addCell(label1);

            float[] packValue=  StringUtils.decouplePackageString(item.packageSize);

            //�ۺа�װl
            label1 = new Label(29, rowUpdate,  String.valueOf(packValue[0]) ,format);
            writableSheet.addCell(label1);
            //�ۺа�װw
            label1 = new Label(30, rowUpdate,  String.valueOf(packValue[1]) ,format);
            writableSheet.addCell(label1);

            //�ۺа�װh
            label1 = new Label(31, rowUpdate,  String.valueOf(packValue[2]) ,format);
            writableSheet.addCell(label1);


            //�ۺа�װl cm
            label1 = new Label(33, rowUpdate,  String.valueOf(StringUtils.cmToInch(packValue[0])) ,format);
            writableSheet.addCell(label1);
            //�ۺа�װw cm
            label1 = new Label(34, rowUpdate,  String.valueOf(StringUtils.cmToInch(packValue[1])) ,format);
            writableSheet.addCell(label1);

            //�ۺа�װh cm
            label1 = new Label(35, rowUpdate,  String.valueOf(StringUtils.cmToInch(packValue[2])) ,format);
            writableSheet.addCell(label1);





            //��ǿ��װ����
            label1 = new Label(37, rowUpdate,  String.valueOf(productDetail2.product.xiankang.pack_memo) ,format);
            writableSheet.addCell(label1);

            //����װ
            label1 = new Label(38, rowUpdate,  String.valueOf(item.packQuantity2) ,format);
            writableSheet.addCell(label1);

            float[] packValue2=  StringUtils.decouplePackageString(item.packageSize2);

            //��ǿ��װ��װl
            label1 = new Label(39, rowUpdate,  String.valueOf(packValue2[0]) ,format);
            writableSheet.addCell(label1);
            //��ǿ��װw
            label1 = new Label(40, rowUpdate,  String.valueOf(packValue2[1]) ,format);
            writableSheet.addCell(label1);

            //��ǿ��װh
            label1 = new Label(41, rowUpdate,  String.valueOf(packValue2[2]) ,format);
            writableSheet.addCell(label1);


            //��ǿ��װl cm
            label1 = new Label(45, rowUpdate,  String.valueOf(StringUtils.cmToInch(packValue2[0])) ,format);
            writableSheet.addCell(label1);
            //��ǿ��װw cm
            label1 = new Label(46, rowUpdate,  String.valueOf(StringUtils.cmToInch(packValue2[1])) ,format);
            writableSheet.addCell(label1);

            //��ǿ��װh cm
            label1 = new Label(47, rowUpdate,  String.valueOf(StringUtils.cmToInch(packValue2[2])) ,format);
            writableSheet.addCell(label1);



            //�ҹ�����
            label1 = new Label(49, rowUpdate,  String.valueOf(productDetail.product.xiankang.getGuaju()) ,format);
            writableSheet.addCell(label1);
            //�ҹ�����
            label1 = new Label(57, rowUpdate,  String.valueOf(productDetail.product.memo) ,format);
            writableSheet.addCell(label1);


        }





    }
}
