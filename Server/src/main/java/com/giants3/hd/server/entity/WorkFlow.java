package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产流程
 * Created by davidleen29 on 2016/9/1.
 */
@Entity(name = "T_WorkFlow")
public class WorkFlow {

    public static final String STEP_1 = "白胚制作";
    public static final int STEP_INDEX_1 = 0;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public static final int STEP_INDEX_2 = 1;
    public static final String STEP_2 = "白胚仓";

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



    public static final List<WorkFlow> initWorkFlowData() {

        ArrayList<WorkFlow> workFlows = new ArrayList<>();


        WorkFlow workFlow;

        workFlow = new WorkFlow();
        workFlow.name = STEP_1;
        workFlow.flowIndex = STEP_INDEX_1;
        workFlows.add(workFlow);


        workFlow = new WorkFlow();
        workFlow.name = STEP_2;
        workFlow.flowIndex = STEP_INDEX_2;
        workFlows.add(workFlow);

        workFlow = new WorkFlow();
        workFlow.name = "喷塑";
        workFlow.flowIndex = 2;
        workFlows.add(workFlow);

        workFlow = new WorkFlow();
        workFlow.name = "颜色";
        workFlow.flowIndex = 3;
        workFlows.add(workFlow);


        workFlow = new WorkFlow();
        workFlow.name = "组装";
        workFlow.flowIndex = 4;
        workFlows.add(workFlow);


        workFlow = new WorkFlow();
        workFlow.name = "验收";
        workFlow.flowIndex = 5;
        workFlows.add(workFlow);

        workFlow = new WorkFlow();
        workFlow.name = "包装";
        workFlow.flowIndex = 6;
        workFlows.add(workFlow);

        workFlow = new WorkFlow();
        workFlow.name = "成品仓";
        workFlow.flowIndex = 7;
        workFlows.add(workFlow);

        return workFlows;


    }

}
