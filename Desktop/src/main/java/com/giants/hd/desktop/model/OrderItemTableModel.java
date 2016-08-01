package com.giants.hd.desktop.model;

import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.interf.ImageByteDataReader;
import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.widget.AttachPanel;
import com.giants.hd.desktop.widget.ImageLabel;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.FileUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import com.giants3.hd.utils.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * 订单表格模型
 */

public class OrderItemTableModel extends BaseTableModel<ErpOrderItem> {


    public static final String TITLE_VERIFY_DATE = "验货日期";
    public static final String TITLE_SEND_DATE = "出柜日期";
    public static final String TITLE_PACKAGE_INFO = "包装信息";
    public static final String TITLE_MAITOU = "唛头";
    public static final String TITLE_GUAGOU = "挂钩说明";
    public static String[] columnNames = new String[]{"序号", "图片", "货号", "配方号", "客号", TITLE_VERIFY_DATE, TITLE_SEND_DATE, TITLE_PACKAGE_INFO, "包装图片", TITLE_MAITOU, TITLE_GUAGOU, "单位", "单价", "数量", "金额", "箱数", "每箱数", "箱规", "立方数", "总立方数", "产品尺寸", "备注"};
    public static int[] columnWidth = new int[]{40, ImageUtils.MAX_PRODUCT_MINIATURE_WIDTH, 60, 60, 100, 100, 100, 100, 100, 150, 100, 40, 40, 40, 80, 120, 60, 120, 120, 120, 120, 400};


    public static final String VERIFY_DATE = "verifyDate";
    public static final String SEND_DATE = "sendDate";
    public static final String PACKAGE_INFO = "packageInfo";
    public static final String MAITOU = "maitou";
    public static final String GUAGOU = "guagou";

    public static final String PACK_ATTACHES = "packAttaches";
    public static String[] fieldName = new String[]{"itm",     "photo",        "prd_no",    "pVersion",  "bat_no",     VERIFY_DATE,  SEND_DATE,  PACKAGE_INFO, PACK_ATTACHES, MAITOU, GUAGOU, "ut", "up", "qty", "amt", "htxs", "so_zxs", "khxg", "xgtj", "zxgtj", "hpgg", "memo"};

    public static Class[] classes = new Class[]{Object.class,  ImageIcon.class, Object.class, Object.class, Object.class, String.class, String.class, String.class, ImageIcon.class};


    @Inject
    public OrderItemTableModel() {
        super(columnNames, fieldName, classes, ErpOrderItem.class);
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_PRODUCT_MINIATURE_HEIGHT;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        ErpOrderItem item = getItem(rowIndex);

        //订单明细是不许增加记录的  所有空行都为不可编辑
        if (StringUtils.isEmpty(item.os_no)) {
            return false;
        }

        if (columnIndex == StringUtils.index(fieldName, VERIFY_DATE))
            return true;
        if (columnIndex == StringUtils.index(fieldName, SEND_DATE))
            return true;
        if (columnIndex == StringUtils.index(fieldName, PACKAGE_INFO))
            return true;
        if (columnIndex == StringUtils.index(fieldName, MAITOU))
            return true;
        if (columnIndex == StringUtils.index(fieldName, GUAGOU))
            return true;


        return super.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {

        ErpOrderItem item = getItem(rowIndex);
        if (item == null) return null;


        if (columnIndex == StringUtils.index(fieldName, PACK_ATTACHES)) {


            final String attachString = item.packAttaches;
            if (StringUtils.isEmpty(attachString)) return "";
           ImageIcon data = pictureMaps.get(attachString);
            if (data == null) {


                String[] fileNames = attachString.split(";");
                String url = "";
                if (fileNames[0].startsWith("TEMP_")) {
                    url = HttpUrl.loadTempPicture(fileNames[0]);
                } else {
                    url = HttpUrl.loadAttachPicture(fileNames[0]);
                }

                ImageLoader.getInstance().displayImage(new Iconable() {



                    @Override
                    public void setIcon(ImageIcon icon) {
                        pictureMaps.put(attachString, icon);
                        fireTableCellUpdated(rowIndex, columnIndex);
                    }

                    @Override
                    public void onError(String message) {
                        pictureMaps.put(attachString, new ImageIcon(new byte[0]));

                    }
                }, url,   columnWidth[columnIndex],getRowHeight());

                return null;

            } else {
                return data;
            }
        }

        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


        ErpOrderItem item = getItem(rowIndex);
        if (item == null) return;
        boolean update = false;
        if (columnIndex == StringUtils.index(fieldName, PACKAGE_INFO)) {


            item.packageInfo = String.valueOf(aValue);
            update = true;


        }
        if (columnIndex == StringUtils.index(fieldName, MAITOU)) {
            item.maitou = String.valueOf(aValue);
            update = true;


        }
        if (columnIndex == StringUtils.index(fieldName, GUAGOU)) {
            item.guagou = String.valueOf(aValue);
            update = true;


        }


        if (columnIndex == StringUtils.index(fieldName, VERIFY_DATE)) {
            item.verifyDate = String.valueOf(aValue);
            update = true;


        }

        if (columnIndex == StringUtils.index(fieldName, SEND_DATE)) {
            item.sendDate = String.valueOf(aValue);
            update = true;
        }
        if (update)
            fireTableCellUpdated(rowIndex, columnIndex);
        super.setValueAt(aValue, rowIndex, columnIndex);
    }


    @Override
    public int[] getMultiLineColumns() {
        return new int[]{ArrayUtils.indexOnArray(fieldName, PACKAGE_INFO), ArrayUtils.indexOnArray(fieldName, MAITOU), ArrayUtils.indexOnArray(fieldName, GUAGOU)};
    }


    //异步加载的图片缓存
    private HashMap<String,ImageIcon> pictureMaps = new HashMap<>();
}
