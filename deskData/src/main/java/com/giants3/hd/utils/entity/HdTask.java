package com.giants3.hd.utils.entity;

import java.io.Serializable;

/**
 * Created by david on 2015/12/10.
 */

/**
 * 任务定时
 */

public class HdTask implements Serializable {



    public long id;

    public int taskType;
    public String taskName;
    public long startDate;
    public String dateString;
    public String activator;
    public String activateTime;

    public String memo;




    /**
     * 重复次数
     * -2 每月
     *-1 每星期
     * 0 表示每天
     * 1-n 表示重复N次
     *
     */
    public int repeatCount;

    /**
     * 执行次数
     *
     */
    public int executeCount;








    public static final int TYPE_SYNC_ERP=100;
    public static final String NAME_SYNC_ERP="ERP材料同步";





}
