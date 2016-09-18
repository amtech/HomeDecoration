package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 生产流程管理业务处理类
 * Created by david on 2016/2/15.
 */
@Service
public class WorkFlowService extends AbstractService implements InitializingBean, DisposableBean {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private QuoteAuthRepository quoteAuthRepository;
    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    WorkFlowRepository workFlowRepository;

    @Autowired
    OrderItemWorkFlowRepository orderItemWorkFlowRepository;
    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    /**
     * 获取所有流程数据
     * @return
     */
    public List<WorkFlow> getAllWorkFlow()
    {
        return workFlowRepository.findAll();
    }


    /**
     * 获取产品的生产流程
     * @param orderItemId
     * @return
     */
    public List<WorkFlowOrderItem> getOrderItemWorkFlows(long orderItemId)
    {
        return orderItemWorkFlowRepository.findByOrderItemIdEquals(orderItemId);
    }


    public  WorkFlowOrderItem createOrderItemWorkFlow(long orderItemId,Long[] workFlowIds)
    {

        WorkFlowOrderItem workFlowOrderItem=new WorkFlowOrderItem();
        workFlowOrderItem.orderItemId=orderItemId;

       // workFlowOrderItem.processFlowIds= StringUtils.combine(workFlowIds);


        return workFlowOrderItem;

    }
}
