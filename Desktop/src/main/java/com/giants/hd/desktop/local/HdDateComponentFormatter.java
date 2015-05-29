package com.giants.hd.desktop.local;

import net.sourceforge.jdatepicker.impl.DateComponentFormatter;
import net.sourceforge.jdatepicker.util.JDatePickerUtil;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期输入格式化
 */
public class HdDateComponentFormatter extends JFormattedTextField.AbstractFormatter {




    DateFormat format;

    public HdDateComponentFormatter(){
        format =new SimpleDateFormat("YYYY-MM-dd");
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        Calendar cal = (Calendar)value;
        if (cal == null) {
            return "";
        }
        return format.format(cal.getTime());
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null || text.equals("")) {
            return null;
        }
        Date date = format.parse(text);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
