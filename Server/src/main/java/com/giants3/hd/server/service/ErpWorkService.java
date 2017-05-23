package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * 订单生产流程， 物料采购流程相关业务
 * Created by davidleen29 on 2017/3/9.
 */
@Service
public class ErpWorkService extends AbstractService implements InitializingBean, DisposableBean {

    EntityManager manager;
    ErpWorkRepository erpWorkRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WorkFlowMessageRepository workFlowMessageRepository;

    private Date today;

    @Autowired
    ErpWorkFlowReportRepository erpWorkFlowReportRepository;

    @Autowired
    WorkFlowWorkerRepository workFlowWorkerRepository;
    @Autowired
    ErpOrderItemProcessRepository erpOrderItemProcessRepository;
//    //流程排序
//    private Comparator<ErpOrderItemProcess> comparator    = new Comparator<ErpOrderItemProcess>() {
//        @Override
//        public int compare(ErpOrderItemProcess o1, ErpOrderItemProcess o2) {
//            int o1Index= ErpWorkFlow.findIndexByCode(o1.mrp_no.substring(0,1));
//            int o2Index=ErpWorkFlow.findIndexByCode(o2.mrp_no.substring(0,1));
//            return o1Index-o2Index;
//        }
//    };;

    @Override
    public void destroy() throws Exception {


        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        erpWorkRepository = new ErpWorkRepository(manager);
        today = Calendar.getInstance().getTime();
    }


    public RemoteData<Zhilingdan> searchZhilingdan(String osName, String startDate, String endDate) {


        final List<Zhilingdan> datas = erpWorkRepository.searchZhilingdan(osName, startDate, endDate);

        for (Zhilingdan zhilingdan : datas) {


            zhilingdan.isCaigouOverDue = isCaigouOverDue(zhilingdan);
            zhilingdan.isJinhuoOverDue = isJinhuoOverDue(zhilingdan);

            //日期截断处理
            if (!StringUtils.isEmpty(zhilingdan.caigou_dd))
                zhilingdan.caigou_dd = zhilingdan.caigou_dd.substring(0, 10);
            if (!StringUtils.isEmpty(zhilingdan.jinhuo_dd))
                zhilingdan.jinhuo_dd = zhilingdan.jinhuo_dd.substring(0, 10);
            if (!StringUtils.isEmpty(zhilingdan.mo_dd))
                zhilingdan.mo_dd = zhilingdan.mo_dd.substring(0, 10);


        }
        return wrapData(datas);
    }


    /**
     * 采购超期判断
     *
     * @param zhilingdan
     * @return
     */
    private boolean isCaigouOverDue(Zhilingdan zhilingdan) {

        if (StringUtils.isEmpty(zhilingdan.mo_no)) return false;
        if (zhilingdan.need_dd == 0) return false;
        Date mmDate = null;
        try {
            mmDate = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.mo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (mmDate == null) return false;
        Date caigouDd = today;
        try {
            caigouDd = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.caigou_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }


        int day = de.greenrobot.common.DateUtils.getDayDifference(mmDate.getTime(), caigouDd.getTime());

        return day > zhilingdan.need_dd;


    }

    /**
     * 进货超期判断
     *
     * @param zhilingdan
     * @return
     */
    private boolean isJinhuoOverDue(Zhilingdan zhilingdan) {
        if (StringUtils.isEmpty(zhilingdan.mo_no)) return false;
        if (zhilingdan.need_days == 0) return false;


        Date mmDate = null;
        try {
            mmDate = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.mo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (mmDate == null) return false;

        Date jinhuoDd = today;
        try {
            jinhuoDd = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.jinhuo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }


        int day = de.greenrobot.common.DateUtils.getDayDifference(mmDate.getTime(), jinhuoDd.getTime());


        //进货单超期 要大于
        return day > zhilingdan.need_days + zhilingdan.need_dd;

    }


//    /**
//     * 查找指定订单的排厂数据
//     *
//     * @param os_no
//     * @return
//     */
//
//    public List<ErpOrderItemProcess> findOrderItemProcess(String os_no, String prd_no, String pVersion) {
//
//
//        return erpWorkRepository.findOrderItemProcesses(os_no, prd_no, pVersion);
//
//
//    }
    /**
     * 查找指定订单的排厂数据
     *
     * @param os_no
     * @return
     */

    public List<ErpOrderItemProcess> findOrderItemProcess(String os_no, int itm  ) {


        return erpWorkRepository.findOrderItemProcesses(os_no, itm);


    }


    /**
     * 查找xxxx的进度报表
     */
    public RemoteData<ErpWorkFlowReport> findErpWorkFlowReport(String os_no, int itm  ) {


        //查询本地数据库的报表记录
        final List<ErpWorkFlowReport> byOsNoEqualsAndPrdNoEquals = erpWorkFlowReportRepository.findByOsNoEqualsAndItmEquals(os_no,itm);


        //本地不存在 查询erp。
        if (ArrayUtils.isEmpty(byOsNoEqualsAndPrdNoEquals)) {

            List<ErpOrderItemProcess> processes = findOrderItemProcess(os_no,itm);
            if (ArrayUtils.isEmpty(processes))
                return wrapError("该订单未排厂");


            //erp 排厂数据整合临时数据  已经排厂，未进行流程活动， 默认进度都是0，进行过流程活动了， 才保存在 ErpWorkFlowReport 表中。


            //不以abcd开头的process 数据，就是成品数据


            for (ErpWorkFlow erpWorkFlow : ErpWorkFlow.WorkFlows) {


                //判断排中是否有当前流程
                boolean hasThisFlow = false;
                ErpOrderItemProcess findProcess=null;

                int typeCount=0;
                for (ErpOrderItemProcess process : processes) {


                    if (process.mrpNo.startsWith(erpWorkFlow.code)) {
                        findProcess=process;
                        typeCount++;
                        hasThisFlow = true;

                    }
                }

                if (!hasThisFlow) continue;
                ErpWorkFlowReport erpWorkFlowReport = new ErpWorkFlowReport();
                erpWorkFlowReport.workFlowCode = erpWorkFlow.code;
                erpWorkFlowReport.workFlowName = erpWorkFlow.name;
                erpWorkFlowReport.workFlowStep = erpWorkFlow.step;
                erpWorkFlowReport.osNo = os_no;
                erpWorkFlowReport.itm=itm;
                erpWorkFlowReport.prdNo=findProcess.prdNo;
                erpWorkFlowReport.pVersion=findProcess.pVersion;
                erpWorkFlowReport.typeCount=typeCount;

                erpWorkFlowReport.percentage = 0;
                byOsNoEqualsAndPrdNoEquals.add(erpWorkFlowReport);
            }


        }


        return wrapData(byOsNoEqualsAndPrdNoEquals);
    }


    /**
     *
     */
    public List<ErpWorkFlowReport> listErpWorkFlowReport(int pageIndex, int pageSize) {

        return null;


    }

    /**
     * 查找指定节点可发送的订单流程数据
     *
     * @param os_no
     * @param itm
     * @param flowStep
     * @return
     */
    public RemoteData<ErpOrderItemProcess> getAvailableOrderItemProcess(String os_no, int itm  , int flowStep) {


        ErpWorkFlow workFlow = ErpWorkFlow.findByStep(flowStep);



        List<ErpOrderItemProcess> processes = erpWorkRepository.findOrderItemProcesses(os_no, itm);

        if(processes.size()<=0)
            return wrapData();

        Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(processes.get(0).prdNo, processes.get(0).pVersion);


        List<ErpOrderItemProcess> result = new ArrayList<>();


        ErpWorkFlow nextFlow = null;
        boolean found = false;
        for (ErpOrderItemProcess process : processes) {
            if (process.mrpNo.startsWith(workFlow.code)) {
                found = true;
                result.add(process);
            } else {
                if (found) {

                    nextFlow = ErpWorkFlow.findByCode(process.mrpNo.substring(0, 1));
                    found = false;
                }
            }


        }

        List<ErpOrderItemProcess> availableResult = new ArrayList<>();
        for (ErpOrderItemProcess process : result) {

            process.currentWorkFlowCode = workFlow.code;
            process.currentWorkFlowStep = workFlow.step;
            process.currentWorkFlowName = workFlow.name;
            process.unSendQty=process.qty;

            if (nextFlow != null) {
                process.nextWorkFlowCode = nextFlow.code;
                process.nextWorkFlowStep = nextFlow.step;
                process.nextWorkFlowName = nextFlow.name;
            }

            process.photoUrl = product == null ? "" : product.url;
            process.photoThumb = product == null ? "" : product.thumbnail;




            ErpOrderItemProcess localProcess=erpOrderItemProcessRepository.findFirstByMrpNoEquals(process.mrpNo);
            if(localProcess!=null)
             attachData(process,localProcess);


            if(process.unSendQty>0) {

                availableResult.add(process);
            }
        }





        return wrapData(availableResult);


    }


    public void attachData(ErpOrderItemProcess erpOrderItemProcess,ErpOrderItemProcess localData)
    {

        erpOrderItemProcess.id=localData.id;
        erpOrderItemProcess.unSendQty=localData.unSendQty;
        erpOrderItemProcess.sendingQty=localData.sendingQty;
        erpOrderItemProcess.sentQty=localData.sentQty;
//        erpOrderItemProcess.sentQty=localData.sentQty;
    }

    /**
     * 向指定流程发起生产提交
     *
     * @param user
     * @param erpOrderItemProcess 订单项对应流程状态
     * @param tranQty             传递数量
     * @param memo                备注
     */


    @Transactional
    public RemoteData<Void> sendWorkFlowMessage(User user, ErpOrderItemProcess erpOrderItemProcess, int tranQty, String memo) {


        if (erpOrderItemProcess.nextWorkFlowStep <= 0)
            return wrapError("该订单下一流程数据不存在");

//        WorkFlow workFlow = workFlowRepository.findFirstByFlowStepEquals(workFlowOrderItemState.currentWorkFlowStep);
//        if (workFlow == null)
//            return wrapError("数据异常，该订单当前流程不存在");
//        WorkFlow newWorkFlow = workFlowRepository.findFirstByFlowStepEquals(workFlowOrderItemState.nextWorkFlowStep);
//        if (newWorkFlow == null)
//            return wrapError("数据异常，该订单下一流程数据不存在");

        if (tranQty > erpOrderItemProcess.unSendQty) {

            return wrapError("提交数量超过当前流程数量");
        }


        //验证人员

        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowCodeEqualsAndSendEquals(user.id, erpOrderItemProcess.currentWorkFlowCode, true);
        if (workFlowWorker == null) {
            return wrapError("无权限在当前节点:" + erpOrderItemProcess.currentWorkFlowName + " 发送流程");
        }


//        OrderItem orderItem = orderItemRepository.findOne(workFlowOrderItemState.orderItemId);
//        //设置当前为初始状态
//        Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(orderItem.prdNo, StringUtils.isEmpty(orderItem.pVersion) ? "" : orderItem.pVersion);
//        if (product == null)
//            return wrapError("该订单中有货款，没有产品资料，请补全。");


        //扣减数量
        erpOrderItemProcess.unSendQty -= tranQty;
        erpOrderItemProcess.sendingQty += tranQty;

        erpOrderItemProcessRepository.save(erpOrderItemProcess);


        //构建信息发出消息
        WorkFlowMessage workFlowMessage = new WorkFlowMessage();


        workFlowMessage.orderItemProcessId = erpOrderItemProcess.id;

        workFlowMessage.orderName = erpOrderItemProcess.osNo;

        workFlowMessage.orderItemQty = erpOrderItemProcess.qty;
        workFlowMessage.transportQty = tranQty;
        workFlowMessage.memo = memo==null?"":memo;
        workFlowMessage.name = WorkFlowMessage.NAME_SUBMIT;

        workFlowMessage.fromFlowStep = erpOrderItemProcess.currentWorkFlowStep;
        workFlowMessage.fromFlowName = erpOrderItemProcess.currentWorkFlowName;
        workFlowMessage.fromFlowCode = erpOrderItemProcess.currentWorkFlowCode;
        workFlowMessage.toFlowStep = erpOrderItemProcess.nextWorkFlowStep;
        workFlowMessage.toFlowName = erpOrderItemProcess.nextWorkFlowName;
        workFlowMessage.toFlowCode = erpOrderItemProcess.nextWorkFlowCode;
        workFlowMessage.createTime = Calendar.getInstance().getTimeInMillis();
        workFlowMessage.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
        workFlowMessage.state = WorkFlowMessage.STATE_SEND;

        workFlowMessage.productName = erpOrderItemProcess.prdNo ;
        workFlowMessage.itm = erpOrderItemProcess.itm ;
        workFlowMessage.mrpNo = erpOrderItemProcess.mrpNo ;
        workFlowMessage.pVersion = erpOrderItemProcess.pVersion;
        workFlowMessage.url = erpOrderItemProcess.photoUrl;
        workFlowMessage.thumbnail = erpOrderItemProcess.photoThumb;

        workFlowMessage.factoryName = erpOrderItemProcess.jgh;

        workFlowMessageRepository.save(workFlowMessage);



        RemoteData<ErpWorkFlowReport> workFlowReports= findErpWorkFlowReport(erpOrderItemProcess.osNo,erpOrderItemProcess.itm);

        if( workFlowReports.isSuccess())
        {
           erpWorkFlowReportRepository.save(workFlowReports.datas);
        }


        return wrapData();


    }

    /**
     * 接收产品订单的递交数据
     * 如果该流程未配置审核人， 则自动通过审核。并使该订单进入下一个流程
     */
    @Transactional
    public RemoteData<Void> receiveOrderItemWorkFlow(User loginUser, long messageId) {





        WorkFlowMessage message = workFlowMessageRepository.findOne(messageId);
        if (message == null) {
            return wrapError("消息不存在：" + messageId);
        }



        //人员验证
        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowCodeEqualsAndReceiveEquals(loginUser.id, message.toFlowCode, true);
        boolean canOperate = workFlowWorker != null;

        if (!canOperate) {
            return wrapError("未配置对该流程处理的权限");

        }


        ErpOrderItemProcess erpOrderItemProcess = erpOrderItemProcessRepository.findOne(message.orderItemProcessId);
        if (erpOrderItemProcess == null) {
            return wrapError("未找到对应的流程状态信息");
        }


        //上流程 数量整理
        erpOrderItemProcess.sendingQty -= message.transportQty;
//        erpOrderItemProcess.unSendQty -= message.transportQty;
        erpOrderItemProcess.sentQty += message.transportQty;


        erpOrderItemProcessRepository.save(erpOrderItemProcess);
        ErpWorkFlowReport   workFlowReport=     erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName,message.itm,message.fromFlowStep);







        workFlowReport.percentage+= (float)message.transportQty/erpOrderItemProcess.orderQty/(workFlowReport.typeCount==0?1:workFlowReport.typeCount);






        erpWorkFlowReportRepository.save(workFlowReport);


//
//
//        String[] productTypes = StringUtils.split(orderItemWorkFlow.productTypes);
//
//
//        OrderItemWorkFlowState newState = orderItemWorkFlowStateRepository.findFirstByQtyIsGreaterThanAndOrderItemIdEqualsAndWorkFlowStepEqualsAndFactoryIdEqualsAndProductTypeEqualsOrderByOrderNameDescCreateTimeDesc(0, state.orderItemId, state.nextWorkFlowStep, state.factoryId, state.productType);
//        if (newState == null) {
//            newState = GsonUtils.fromJson(GsonUtils.toJson(state), OrderItemWorkFlowState.class);
//            newState.id = 0;
//            newState.qty = message.transportQty;
//            newState.sendingQty = 0;
//            newState.unSendQty = newState.qty;
//            newState.sentQty = 0;
//            newState.workFlowStep = newState.nextWorkFlowStep;
//            newState.workFlowName = newState.nextWorkFlowName;
//        } else {
//
//            //数量合并累加
//            newState.qty += message.transportQty;
//            newState.unSendQty += message.transportQty;
//        }
//
//
//        newState.createTime = Calendar.getInstance().getTimeInMillis();
//        newState.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
//
//
//
//        //判断是否是汇总节点。
//        boolean needGroupUp = newState.workFlowStep == orderItemWorkFlow.groupStep && productTypes != null;
//
//        //获取下个节点流程数据
//        String[] nextWorkFlow = getNextWorkFlow(newState.workFlowStep, newState.productType, orderItemWorkFlow);
//
//        int nextWorkFlowStep;
//        String nextWorkFlowName;
//
//        if (nextWorkFlow != null) {
//            nextWorkFlowStep
//                    = Integer.valueOf(nextWorkFlow[0]);
//            nextWorkFlowName = nextWorkFlow[1];
//        } else {
//
//            //节点到此结束
//            nextWorkFlowStep
//                    = 0;
//            nextWorkFlowName = "";
//
//        }
//
//
//        if (needGroupUp) {
//            //附上下一节点流程数据
//            newState.nextWorkFlowStep = 0;
//            newState.nextWorkFlowName = "";
//
//        } else {
//            //附上下一节点流程数据
//            newState.nextWorkFlowStep = nextWorkFlowStep;
//            newState.nextWorkFlowName = nextWorkFlowName;
//
//        }
//        newState = orderItemWorkFlowStateRepository.save(newState);
//
//
//        //检查数据合并
//
//        //查找当前订单 当前节点的所有状态数据
//        if (needGroupUp) {
//            List<OrderItemWorkFlowState> relateStates = orderItemWorkFlowStateRepository.findByQtyIsGreaterThanAndOrderItemIdEqualsAndWorkFlowStepEqualsOrderByOrderNameDescCreateTimeDesc(0, newState.orderItemId, newState.workFlowStep);
//
//            //需要合并
//            if (!StringUtils.isEmpty(newState.factoryId)) {// && "0".equals(newState.workFlowType)
//
//                //多少个产品类型 就会派发多少个厂家跟进度。
//                if (relateStates.size() == productTypes.length) {
//                    boolean isAllPrepared = true;
//                    for (OrderItemWorkFlowState aState : relateStates) {
//                        if (aState.qty != state.orderQty) {
//                            isAllPrepared = false;
//                            break;
//                        }
//                    }
//
//
//                    //清除分厂家的进度信息（设置零）， 汇总数据（新数据 配置数量，清除厂家数据）。
//                    if (isAllPrepared) {
//                        newState = GsonUtils.fromJson(GsonUtils.toJson(newState), OrderItemWorkFlowState.class);
//                        for (OrderItemWorkFlowState aState : relateStates) {
//                            aState.qty = 0;
//                            aState.sendingQty = 0;
//                            aState.unSendQty = 0;
//                        }
//                        orderItemWorkFlowStateRepository.save(relateStates);
//
//                        newState.id = 0;
//                        newState.nextWorkFlowStep = nextWorkFlowStep;
//                        newState.nextWorkFlowName = nextWorkFlowName;
//
//                        newState.qty = newState.orderQty;
//                        newState.unSendQty = newState.orderQty;
//                        newState.sendingQty = 0;
//
//                        newState.factoryId = "";
//                        newState.factoryName = "";
//                        newState.productType = "";
//                        newState.productTypeName = "";
//                        orderItemWorkFlowStateRepository.save(newState);
//                        //删除记录， 合成新记录。
//
//                    }
//
//
//                }
//
//
//            }
//
//
//        }


        message.receiveTime = Calendar.getInstance().getTimeInMillis();
        message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        if (message.state == WorkFlowMessage.STATE_SEND) {

            //无审核人 系统自动审核通过
            message.state = WorkFlowMessage.STATE_PASS;
            message.checkTime = Calendar.getInstance().getTimeInMillis();
            message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        } else if (message.state == WorkFlowMessage.STATE_REWORK) {
            //返工 状态 自动通过。
            message.state = WorkFlowMessage.STATE_PASS;
            message.receiveTime = Calendar.getInstance().getTimeInMillis();
            message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        }

        workFlowMessageRepository.save(message);


        return wrapData();
    }


    /**
     * 流程拒绝接收处理。
     *
     * @param user
     * @param workFlowMsgId
     * @param toWorkFlowStep
     * @param reason
     * @return
     */
    @Transactional
    public RemoteData<Void> rejectWorkFlowMessage(User user, long workFlowMsgId, int toWorkFlowStep, String reason) {

        WorkFlowMessage message = workFlowMessageRepository.findOne(workFlowMsgId);

        ErpWorkFlow to = ErpWorkFlow.findByStep(toWorkFlowStep);

//        message.state = WorkFlowMessage.STATE_REJECT;
//        message.memo = reason;
//        message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
//        message.checkTime = Calendar.getInstance().getTimeInMillis();
//        workFlowMessageRepository.save(message);
//
//
//        //构建返工发出消息
//        WorkFlowMessage workFlowMessage = new WorkFlowMessage();
//        workFlowMessage.orderId = message.orderId;
//        workFlowMessage.orderName = message.orderName;
//        workFlowMessage.orderItemId = message.orderItemId;
//        workFlowMessage.orderItemWorkFlowId = message.orderItemWorkFlowId;
//        workFlowMessage.orderItemQty = message.orderItemQty;
//        workFlowMessage.transportQty = message.transportQty;
//        workFlowMessage.memo = reason;
//        workFlowMessage.name = WorkFlowMessage.NAME_REWORK;
//
//        workFlowMessage.fromFlowStep = message.fromFlowStep;
//        workFlowMessage.fromFlowName = message.fromFlowName;
//
//
//        workFlowMessage.toFlowStep = to.flowStep;
//        workFlowMessage.toFlowName = to.name;
//
//        workFlowMessage.createTime = Calendar.getInstance().getTimeInMillis();
//        workFlowMessage.createTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
//        workFlowMessage.state = WorkFlowMessage.STATE_REWORK;
//        workFlowMessage.unit = message.unit;
//        workFlowMessage.productId = message.productId;
//        workFlowMessage.productName = message.productName;
//        workFlowMessage.url = message.url;
//
//        workFlowMessageRepository.save(workFlowMessage);

        return wrapData();


    }
}
