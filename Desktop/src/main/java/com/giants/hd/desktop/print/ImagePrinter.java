package com.giants.hd.desktop.print;

import com.giants3.hd.utils.FileUtils;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by davidleen29 on 2018/4/8.
 */
public class ImagePrinter implements  Printable {

    private final String fileName;
    private final int count;

    public ImagePrinter(String fileName, int count)
    {

        this.fileName = fileName;
        this.count = count;
    }


    public void doPrint() {

        DocFlavor dof = null;
        if (fileName.endsWith(".gif")) {
            dof = DocFlavor.INPUT_STREAM.GIF;
        } else if (fileName.endsWith(".jpg")) {
            dof = DocFlavor.INPUT_STREAM.JPEG;
        } else if (fileName.endsWith(".png")) {
            dof = DocFlavor.INPUT_STREAM.PNG;
        }
        PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(OrientationRequested.PORTRAIT);
        pras.add(new Copies(count));
        pras.add(PrintQuality.HIGH);
        DocAttributeSet das = new HashDocAttributeSet();
        // 设置打印纸张的大小（以毫米为单位）
         das.add(new MediaPrintableArea(0, 0, 35, 55, MediaPrintableArea.MM));
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Doc doc = new SimpleDoc(fin, dof, das);
        DocPrintJob job = ps.createPrintJob();

        try {
            job.print(doc, pras);
        } catch (PrintException e) {
            e.printStackTrace();
        }

        FileUtils.safeClose(fin);
    }




    public  void doPrint2()
    {
        PrinterJob printerJob=PrinterJob.getPrinterJob();
        printerJob.setPrintable(this);
        if (printerJob.printDialog()) {


            try {
                printerJob.print(); // 进行每一页的具体打印操作
            } catch (PrinterException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        return 0;
    }
}
