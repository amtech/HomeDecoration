package com.giants.hd.desktop.reports;

import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by david on 2015/9/11.
 */
public class JXLOperator implements ExcelOperator {



    private WritableSheet writableSheet;

    public JXLOperator(WritableSheet writableSheet) {
        this.writableSheet = writableSheet;
    }

    @Override
    public void addString(String value, int x, int y) {
        Label label1 = new Label(x, y, value,writableSheet.getWritableCell(x,y).getCellFormat());
        try {
            writableSheet.addCell(label1);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNumber(float value, int x, int y) {
        jxl.write.Number number = new  jxl.write.Number(x, y, value,writableSheet.getWritableCell(x,y).getCellFormat());
        try {
            writableSheet.addCell(number);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNumber(double value, int x, int y) {
        jxl.write.Number number = new  jxl.write.Number(x, y, value,writableSheet.getWritableCell(x,y).getCellFormat());
        try {
            writableSheet.addCell(number);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNumber(int value, int x, int y) {
        jxl.write.Number number = new  jxl.write.Number(x, y, value,writableSheet.getWritableCell(x,y).getCellFormat());
        try {
            writableSheet.addCell(number);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPicture(String url, int x, int y, int width, int height) {
        byte[] data=null;
        try {
            // data=  ImageUtils.scale(new URL(url), cellWidth * 4, cellHeight * 4, true);

              //data=  ImageUtils.scale(new URL(url), 640, 640, true);

            BufferedImage bufferedImage= ImageLoader.getInstance().loadImage(url );
            data=   ImageUtils.scale(bufferedImage,640, 640,true);
        } catch (HdException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WritableImage image;




        if(data!=null) {
            image = new WritableImage(x, y, width, height,data);
            image.setImageAnchor(WritableImage.MOVE_AND_SIZE_WITH_CELLS);
            writableSheet.addImage(image);

        }
    }

    @Override
    public void duplicateRow(int startRow, int defaultRowCount, int dataSize) {




        int rowHeight = writableSheet.getRowView(startRow).getSize();

        //实际数据超出范围 插入空行
        if(dataSize>defaultRowCount)
        {

            try {
            //插入空行
            for(int j=0;j<dataSize-defaultRowCount;j++) {

                int rowToInsert=startRow+defaultRowCount+j;
                writableSheet.insertRow(rowToInsert);

                    writableSheet.setRowView(rowToInsert, rowHeight);


                //复制表格。
                for (int i = 0, count = writableSheet.getColumns(); i < count; i++) {
                    WritableCell cell = (WritableCell) writableSheet.getCell(i, startRow);
                    cell = cell.copyTo(i, rowToInsert);
                    writableSheet.addCell(cell);

                }


            }


        } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }
}
