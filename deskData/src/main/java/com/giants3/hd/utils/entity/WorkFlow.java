package com.giants3.hd.utils.entity;

/**
 * 生产流程
 */
public class WorkFlow {


    public long id;

    public String name;

    /**
     * 当前序号
     */
    public int flowIndex;



    //流程负责人相关数据
    public long userId;
    public String userName;
    public String userCName;
    public String userCode;

    //审核人相关数据
    public long checkerId;
    public String checkerName;
    public String checkerCName;
    public String checkerCode;
}