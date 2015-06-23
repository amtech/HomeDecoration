package com.giants.hd.desktop.local;

import com.giants3.hd.utils.FloatHelper;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期输入格式化
 */
public class HdFloatComponentFormatter extends JFormattedTextField.AbstractFormatter {






    public HdFloatComponentFormatter(){

    }

    @Override
    public String valueToString(Object value) throws ParseException {

        return value.toString();
    }

    @Override
    public Object stringToValue(String text) throws ParseException {

        return  Float.valueOf(text);
    }
}
