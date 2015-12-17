package com.giants3.hd.utils.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by david on 2015/12/10.
 */

/**
 * 任务定时
 */
@Entity(name="T_HDTask")
public class HdTask implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public int taskType;
    public String taskName;
    public long date;
    public String dateString;
    public String activator;
    public String activateTime;
    public String memo;



    public static final int TYPE_SYNC_ERP=100;
    public static final String NAME_SYNC_ERP="ERP材料同步";

}
