package com.giants.hd.desktop.reports.jasper;

import java.util.HashMap;

public class ReportData extends HashMap<String,Object>
{


  OrderItemReportData orderItemReportData;
  public ReportData(OrderItemReportData orderItemReportData) {
    this.orderItemReportData=orderItemReportData;
  }

  @Override
  public Object get(Object key) {

    try {
      return OrderItemReportData.class.getField(key.toString()).get(orderItemReportData);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return super.get(key);

  }

  @Override
  public boolean containsKey(Object key) {

    try {
      return OrderItemReportData.class.getField(key.toString())!=null;
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return super.containsKey(key);
  }
}