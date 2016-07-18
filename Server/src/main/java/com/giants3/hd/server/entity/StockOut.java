package com.giants3.hd.server.entity;

import java.io.Serializable;

/**
 *   库存表
 */
public class StockOut implements Serializable {



    public long id;

    /**
     * 任务id
     */
    public long taskId;

    /**
     * 任务类型名称
     */
    public String  taskTypeName;


    public long executeTime;
    public String executeTimeString;

    /**
     *   耗时 秒为单位
     */
    public long timeSpend;




    /**
     * 任务状态
     */
    public int state;


    /**
     * 状态名称
     */

    public String stateName;










    /**
     * 任务执行失败
     */
    public static final int STATE_FAIL=2;

    public static final int STATE_SUCCESS = 1;
    /**
     * 执行错误信息
     */
    public String errorMessage;
}
