package com.giants3.report.jasper;

import java.util.HashMap;

public class ReportData<T> extends HashMap<String, Object> {


    T data;
    private Class<T> tClass;

    public ReportData(T data, Class<T> tClass) {
        this.data = data;
        this.tClass = tClass;
        init();
    }

    @Override
    public Object get(Object key) {

        try {
            return tClass.getField(key.toString()).get(data);
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
            return tClass.getField(key.toString()) != null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return super.containsKey(key);
    }


    private  void init()
    {

        put("companyName","云飞公司");
        put("companyEName","YUNFEI COMPANY");
        put("companyEAddress","YUNFEI COMPANY NIUTOUSHANxxx");
        put("tel","15659169570");
        put("fax","156591");
        put("email","lin09111@sina.com");
    }

}