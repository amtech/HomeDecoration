package com.giants3.hd.utils.entity;

import java.util.List;

/**
 * 生产流程
 * Created by davidleen29 on 2016/9/1.
 */
public class ErpWorkFlow {


    public static String[] CODES = new String[]{"A", "B", "C", "D"};

    public static String[] NAMES = new String[]{"白胚", "组装", "包装", "颜色"};


    public String code;
    public String name;


    public static List<ErpWorkFlow> WorkFlows;

    static {


        int count = CODES.length;
        for (int i = 0; i < count; i++) {

            ErpWorkFlow workFlow;
            workFlow = new ErpWorkFlow();
            workFlow.code = CODES[0];
            workFlow.name = NAMES[0];
            WorkFlows.add(workFlow);
        }


    }


}
