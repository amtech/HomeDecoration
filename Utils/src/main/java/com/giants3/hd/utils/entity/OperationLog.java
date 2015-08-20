package com.giants3.hd.utils.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by davidleen29 on 2015/8/21.
 */

@Entity(name = "T_OperationLog")
public class OperationLog {

    public String module;

    public long recordId;

    public String userId;
    public String userCode;
    public String userName;
    public String userChineseName;

    public long time;

    //添加 修改  删除
    public int type;
    private long id;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
