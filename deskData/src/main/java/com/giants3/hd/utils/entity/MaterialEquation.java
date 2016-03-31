package com.giants3.hd.utils.entity;

/**
 * 公式定义  定额计算公式
 */

public class MaterialEquation {


    /**
     * 流程id
     */

    public long id;


    /**
     * 公式编号
     */

    public int equationId;


    /**
     * 公式描述
     */

    public String equationName;


    @Override
    public String toString() {
        return
                " [" +equationId+"]    "+ equationName  ;
    }
}
