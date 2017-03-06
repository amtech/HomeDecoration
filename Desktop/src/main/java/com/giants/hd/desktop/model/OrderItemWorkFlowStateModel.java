package com.giants.hd.desktop.model;

import com.giants3.hd.utils.entity.HdTaskLog;
import com.giants3.hd.utils.entity.OrderItemWorkFlowState;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 *  定时任务数据模型
 */
public class OrderItemWorkFlowStateModel extends  BaseTableModel<OrderItemWorkFlowState> {

    public static String[] columnNames = new String[]{     "订单号 ",  "产品号" ,"订单数量" ,"当前流程" ,"二级流程","生产厂家","当前数量","创建时间","错误信息" };
    public static int[] columnWidth=new int[]{   150,        150 ,150,100,100, 120,200};


    public static final String TIME_SPEND = "timeSpend";
    public static String[] fieldName = new String[]{ "orderName", "productFullName", "orderQty","workFlowName","productTypeName", "factoryName","qty","createTimeString","memo"  };

    public  static Class[] classes = new Class[]{Object.class,Object.class, Object.class };


    @Inject
    public OrderItemWorkFlowStateModel() {
        super(columnNames, fieldName, classes, OrderItemWorkFlowState.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return false;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {


    }


    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }
    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT ;
    }
}
