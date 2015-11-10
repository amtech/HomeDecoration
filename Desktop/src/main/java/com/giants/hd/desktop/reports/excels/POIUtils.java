package com.giants.hd.desktop.reports.excels;



import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Iterator;

public class POIUtils {
//	/**
//	 * ��һ��excel�е�cellstyletable���Ƶ���һ��excel������ᱨ�����������ַ�����������ѽ����������
//	 * @param fromBook
//	 * @param toBook
//	 */
//	public static void copyBookCellStyle(HSSFWorkbook fromBook,HSSFWorkbook toBook){
//		for(short i=0;i<fromBook.getNumCellStyles();i++){
//			HSSFCellStyle fromStyle=fromBook.getCellStyleAt(i);
//			HSSFCellStyle toStyle=toBook.getCellStyleAt(i);
//			if(toStyle==null){
//				toStyle=toBook.createCellStyle();
//			}
//			copyCellStyle(fromStyle,toStyle);
//		}
//	}
    /**
     * ����һ����Ԫ����ʽ��Ŀ�ĵ�Ԫ����ʽ
     * @param fromStyle
     * @param toStyle
     */
    public static void copyCellStyle(CellStyle fromStyle,
                                     CellStyle toStyle) {
        toStyle.setAlignment(fromStyle.getAlignment());
        //�߿�ͱ߿���ɫ
        toStyle.setBorderBottom(fromStyle.getBorderBottom());
        toStyle.setBorderLeft(fromStyle.getBorderLeft());
        toStyle.setBorderRight(fromStyle.getBorderRight());
        toStyle.setBorderTop(fromStyle.getBorderTop());
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

        //������ǰ��
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

        toStyle.setDataFormat(fromStyle.getDataFormat());
        toStyle.setFillPattern(fromStyle.getFillPattern());
//		toStyle.setFont(fromStyle.getFont(null));
        toStyle.setHidden(fromStyle.getHidden());
        toStyle.setIndention(fromStyle.getIndention());//��������
        toStyle.setLocked(fromStyle.getLocked());
        toStyle.setRotation(fromStyle.getRotation());//��ת
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
        toStyle.setWrapText(fromStyle.getWrapText());

    }
    /**
     * Sheet����
     * @param fromSheet
     * @param toSheet
     * @param copyValueFlag
     */
    public static void copySheet(Workbook wb,Sheet fromSheet, Sheet toSheet,
                                 boolean copyValueFlag) {
        //�ϲ�������
        mergerRegion(fromSheet, toSheet);
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
            Row tmpRow = (Row) rowIt.next();
            Row newRow = toSheet.createRow(tmpRow.getRowNum());
            //�и���
            copyRow(wb,tmpRow,newRow,copyValueFlag);
        }
    }

    /**
     * �и��ƹ���
     * @param fromRow
     * @param toRow
     */
    public static void copyRow(Workbook wb,Row fromRow,Row toRow,boolean copyValueFlag)
    {

        copyRow(  wb,  fromRow,  toRow,  copyValueFlag,  true);

    }
    /**
     * �и��ƹ���
     * @param fromRow
     * @param toRow
      * @param copyValueFlag
     * @param useSourceStyle  �Ƿ�ʹ��Դ���ӵ� style
     *
     */
    public static void copyRow(Workbook wb,Row fromRow,Row toRow,boolean copyValueFlag,boolean useSourceStyle){
        for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
            Cell tmpCell =  (Cell) cellIt.next();
            Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
            toRow.setHeight(fromRow.getHeight());

            copyCell(wb, tmpCell, newCell, copyValueFlag,useSourceStyle);
        }
    }
    /**
     * ����ԭ��sheet�ĺϲ���Ԫ���´�����sheet
     *
     * @param fromSheet �´���sheet
     * @param toSheet      ԭ�е�sheet
     */
    public static void mergerRegion(Sheet fromSheet, Sheet toSheet) {
        int sheetMergerCount = fromSheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
            toSheet.addMergedRegion(mergedRegionAt);
        }
    }
    public static void copyCell(Workbook wb,Cell srcCell, Cell distCell,
                                boolean copyValueFlag) {


        copyCell(  wb,  srcCell,   distCell,
          copyValueFlag,false);
    }

    /**
     * ���Ƶ�Ԫ��
     *
     * @param srcCell
     * @param distCell
     * @param copyValueFlag
     *            true����ͬcell������һ����
     */
    public static void copyCell(Workbook wb,Cell srcCell, Cell distCell,
                                boolean copyValueFlag,boolean useFromStyle) {

        CellStyle oldCellStyle = srcCell.getCellStyle();

        if(useFromStyle)
        {

            distCell.setCellStyle(oldCellStyle);
        }else
        {

            CellStyle newstyle=wb.createCellStyle();



            copyCellStyle(oldCellStyle, newstyle);
            //��ʽ
            distCell.setCellStyle(newstyle);
        }

        //����
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }

        // ��ͬ�������ʹ���
        int srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        if (copyValueFlag) {
            if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {
                if (DateUtil.isCellDateFormatted(srcCell)) {
                    distCell.setCellValue(srcCell.getDateCellValue());
                } else {
                    distCell.setCellValue(srcCell.getNumericCellValue());
                }
            } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {
                distCell.setCellValue(srcCell.getRichStringCellValue());
            } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {
                // nothing21
            } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
                distCell.setCellValue(srcCell.getBooleanCellValue());
            } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {
                distCell.setCellErrorValue(srcCell.getErrorCellValue());
            } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {
                distCell.setCellFormula(srcCell.getCellFormula());
            } else { // nothing29
            }
        }
    }
}