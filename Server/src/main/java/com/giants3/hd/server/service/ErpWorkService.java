package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.entity_erp.ErpWorkFlowOrderItem;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
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

    @Autowired
    ProductWorkMemoRepository productWorkMemoRepository;
    @Autowired
    WorkFlowAreaRepository workFlowAreaRepository;

    @Autowired
    OrderItemWorkMemoRepository orderItemWorkMemoRepository;

    private Date today;
    @Value("${workflowfilepath}")
    private String workflowfilepath;
    @Value("${workflowfileurl}")
    private String workflowfileurl;
    @Autowired
    ErpWorkFlowReportRepository erpWorkFlowReportRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

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

    public List<ErpOrderItemProcess> findOrderItemProcess(String os_no, int itm) {


        return erpWorkRepository.findOrderItemProcesses(os_no, itm);


    }


    /**
     * 查找xxxx的进度报表
     */
    public RemoteData<ErpWorkFlowReport> findErpWorkFlowReport(String os_no, int itm) {


        //查询本地数据库的报表记录
        final List<ErpWorkFlowReport> byOsNoEqualsAndPrdNoEquals = erpWorkFlowReportRepository.findByOsNoEqualsAndItmEquals(os_no, itm);


        //本地不存在 查询erp。
        if (ArrayUtils.isEmpty(byOsNoEqualsAndPrdNoEquals)) {

            List<ErpOrderItemProcess> processes = findOrderItemProcess(os_no, itm);
            if (ArrayUtils.isEmpty(processes))
                return wrapError("该订单未排厂");


            //erp 排厂数据整合临时数据  已经排厂，未进行流程活动， 默认进度都是0，进行过流程活动了， 才保存在 ErpWorkFlowReport 表中。


            //不以abcd开头的process 数据，就是成品数据


            for (ErpWorkFlow erpWorkFlow : ErpWorkFlow.WorkFlows) {


                //判断排厂中是否有当前流程
                boolean hasThisFlow = false;
                ErpOrderItemProcess findProcess = null;


                Set<String> typeSet=new HashSet<>();
                for (ErpOrderItemProcess process : processes) {


                    if (process.mrpNo.startsWith(erpWorkFlow.code)) {
                        findProcess = process;
                        typeSet.add(process.mrpNo.substring(0,1));// 取头两位

                        hasThisFlow = true;

                    }
                }

                if (!hasThisFlow) continue;
                ErpWorkFlowReport erpWorkFlowReport = new ErpWorkFlowReport();
                erpWorkFlowReport.workFlowCode = erpWorkFlow.code;
                erpWorkFlowReport.workFlowName = erpWorkFlow.name;
                erpWorkFlowReport.workFlowStep = erpWorkFlow.step;
                erpWorkFlowReport.osNo = os_no;
                erpWorkFlowReport.itm = itm;
                erpWorkFlowReport.prdNo = findProcess.prdNo;
                erpWorkFlowReport.pVersion = findProcess.pVersion;
                erpWorkFlowReport.typeCount = typeSet.size();

                erpWorkFlowReport.percentage = 0;
                byOsNoEqualsAndPrdNoEquals.add(erpWorkFlowReport);
            }


        }


        return wrapData(byOsNoEqualsAndPrdNoEquals);
    }


    /**
     *
     */
    public List<ErpWorkFlowOrderItem> searchUnCompleteOrderItems(String key) {

        return erpWorkRepository.searchUnCompleteOrderItems(key);


    }

    /**
     * 查找指定节点可发送的订单流程数据
     *
     * @param os_no
     * @param itm
     * @param flowStep
     * @return
     */
    public RemoteData<ErpOrderItemProcess> getAvailableOrderItemProcess(User loginUser,String os_no, int itm, int flowStep) {


        ErpWorkFlow workFlow = ErpWorkFlow.findByStep(flowStep);


        WorkFlowWorker workFlowWorker=workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowStepEquals(loginUser.id,flowStep);




        if(!workFlowWorker.send)
        {
            return wrapError("当前流程无发送权限");
        }


        //查找前一个流程
        if(flowStep!=ErpWorkFlow.FIRST_STEP)
        {
          int previousStep=  ErpWorkFlow.findPrevious(flowStep);

            final ErpWorkFlowReport erpWorkFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(os_no, itm, previousStep);
            if(erpWorkFlowReport==null||erpWorkFlowReport.percentage<1)
            {
                return wrapError("上一道流程未交接完毕，不能发起交接");
            }

        }
        List<ErpOrderItemProcess> processes = erpWorkRepository.findOrderItemProcesses(os_no, itm);

        if (processes.size() <= 0)
            return wrapError("当前无可以提交流程");


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


        for (ErpOrderItemProcess process : result)
        {

            if(StringUtils.isEmpty(process.jgh))
            {
                return wrapError("当前流程未排厂加工户");
            }
        }

        List<ErpOrderItemProcess> availableResult = new ArrayList<>();
        for (ErpOrderItemProcess process : result) {
            process.currentWorkFlowCode = workFlow.code;
            process.currentWorkFlowStep = workFlow.step;
            process.currentWorkFlowName = workFlow.name;
            process.unSendQty = process.qty;

            if (nextFlow != null) {
                process.nextWorkFlowCode = nextFlow.code;
                process.nextWorkFlowStep = nextFlow.step;
                process.nextWorkFlowName = nextFlow.name;
            }

            process.photoUrl = product == null ? "" : product.url;
            process.photoThumb = product == null ? "" : product.thumbnail;


            ErpOrderItemProcess localProcess = erpOrderItemProcessRepository.findFirstByMoNoEqualsAndMrpNoEquals(process.moNo,process.mrpNo);
            if (localProcess != null)
                attachData(process, localProcess);



            //判断判断类型 铁木
            //只有白胚，颜色才区分铁木
            if(process.mrpNo.startsWith(ErpWorkFlow.CODE_YANSE)||process.mrpNo.startsWith(ErpWorkFlow.SECOND_STEP_CODE))
            {

                String code=process.mrpNo.substring(1,2);
                if(code.equals(ErpWorkFlow.CODE_MU)||code.equals(ErpWorkFlow.CODE_TIE))
                {

                    //木流程，当前用户没有木权限
                    if(!workFlowWorker.mu&&code.equals(ErpWorkFlow.CODE_MU))
                    {
                        continue;
                    }
//铁流程，当前用户没有铁权限
                    if(!workFlowWorker.tie&&code.equals(ErpWorkFlow.CODE_TIE))
                    {
                        continue;
                    }
                }

            }


            if (process.unSendQty > 0  ) {

                availableResult.add(process);
            }
        }


        return wrapData(availableResult);


    }


    public void attachData(ErpOrderItemProcess erpOrderItemProcess, ErpOrderItemProcess localData) {

        erpOrderItemProcess.id = localData.id;
        erpOrderItemProcess.unSendQty = localData.unSendQty;
        erpOrderItemProcess.sendingQty = localData.sendingQty;
        erpOrderItemProcess.sentQty = localData.sentQty;
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
    public RemoteData<Void> sendWorkFlowMessage(User user, ErpOrderItemProcess erpOrderItemProcess, int tranQty, long areaId,String memo) {






          WorkFlowArea workFlowArea=workFlowAreaRepository.findOne(areaId);
        if(workFlowArea==null)
        {
            return wrapError("未选择交接区域");
        }
        //最后一个节点不需要验证下一流程数据
        if (erpOrderItemProcess.nextWorkFlowStep <= 0&&erpOrderItemProcess.currentWorkFlowStep!=ErpWorkFlow.LAST_STEP)
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



        OrderItem orderItem=     orderItemRepository.findFirstByOsNoEqualsAndItmEquals(erpOrderItemProcess.osNo,erpOrderItemProcess.itm);

        if(orderItem==null) {
            orderItem = new OrderItem();
            orderItem.osNo = erpOrderItemProcess.osNo;
            orderItem.itm = erpOrderItemProcess.itm;
            orderItem.prdNo = erpOrderItemProcess.prdNo;
            orderItem.pVersion = erpOrderItemProcess.pVersion;
            orderItem.url = erpOrderItemProcess.photoUrl;
            orderItem.thumbnail = erpOrderItemProcess.photoThumb;
        }

        //标记当前订单生产状态
        orderItem.workFlowState=ErpWorkFlow.STATE_WORKING;
        orderItem.workFlowDescribe=erpOrderItemProcess.currentWorkFlowCode+erpOrderItemProcess.currentWorkFlowName;
        orderItem= orderItemRepository.save(orderItem);


        //扣减数量
        erpOrderItemProcess.unSendQty -= tranQty;
        erpOrderItemProcess.sendingQty += tranQty;

        erpOrderItemProcess=   erpOrderItemProcessRepository.save(erpOrderItemProcess);


        //构建信息发出消息
        WorkFlowMessage workFlowMessage = new WorkFlowMessage();


        workFlowMessage.orderItemProcessId = erpOrderItemProcess.id;

        workFlowMessage.orderName = erpOrderItemProcess.osNo;

        workFlowMessage.orderItemQty = erpOrderItemProcess.qty;
        workFlowMessage.transportQty = tranQty;
        workFlowMessage.sendMemo = memo == null ? "" : memo;
        workFlowMessage.area = workFlowArea.name  ;
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

        workFlowMessage.productName = erpOrderItemProcess.prdNo;
        workFlowMessage.itm = erpOrderItemProcess.itm;
        workFlowMessage.mrpNo = erpOrderItemProcess.mrpNo;
        workFlowMessage.pVersion = erpOrderItemProcess.pVersion;
        workFlowMessage.url = erpOrderItemProcess.photoUrl;
        workFlowMessage.thumbnail = erpOrderItemProcess.photoThumb;

        workFlowMessage.factoryName = erpOrderItemProcess.jgh;

        workFlowMessage=  workFlowMessageRepository.save(workFlowMessage);


        RemoteData<ErpWorkFlowReport> workFlowReports = findErpWorkFlowReport(erpOrderItemProcess.osNo, erpOrderItemProcess.itm);

        if (workFlowReports.isSuccess()) {
            erpWorkFlowReportRepository.save(workFlowReports.datas);
        }






        if(erpOrderItemProcess.currentWorkFlowStep==ErpWorkFlow.LAST_STEP)
        {




           //自动审核通过
            workFlowMessage.state = WorkFlowMessage.STATE_PASS;
            workFlowMessage.receiveTime = Calendar.getInstance().getTimeInMillis();
            workFlowMessage.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());
            workFlowMessageRepository.save(workFlowMessage);



            //上流程 数量整理
            erpOrderItemProcess.sendingQty -= tranQty;
//        erpOrderItemProcess.unSendQty -= message.transportQty;
            erpOrderItemProcess.sentQty += tranQty;


            erpOrderItemProcessRepository.save(erpOrderItemProcess);
            ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(workFlowMessage.orderName, workFlowMessage.itm, workFlowMessage.fromFlowStep);


            //更新生产进度
            workFlowReport.percentage += (float) workFlowMessage.transportQty / erpOrderItemProcess.orderQty / (workFlowReport.typeCount == 0 ? 1 : workFlowReport.typeCount);
            workFlowReport=   erpWorkFlowReportRepository.save(workFlowReport);



            if(workFlowReport.percentage>=1)
            {
                //最后一个流程发出信息  标记出货   当进度达到1该货款生产结束

                orderItem.workFlowState=ErpWorkFlow.STATE_COMPLETE;
                orderItem.workFlowDescribe="生产完成";
                orderItemRepository.save(orderItem);
            }

        }
        return wrapData();


    }

    /**
     * 接收产品订单的递交数据
     * 如果该流程未配置审核人， 则自动通过审核。并使该订单进入下一个流程
     */
    @Transactional
    public RemoteData<Void> receiveOrderItemWorkFlow(User loginUser, long messageId, MultipartFile[] files, String memo) {


        final int length = files.length;
        if (length < ErpWorkFlow.PICTURE_COUNT) {
            return wrapError("接收，要传递三张图片");
        }


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
        ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName, message.itm, message.fromFlowStep);


        //更新生产进度
        workFlowReport.percentage += (float) message.transportQty / erpOrderItemProcess.orderQty / (workFlowReport.typeCount == 0 ? 1 : workFlowReport.typeCount);
        erpWorkFlowReportRepository.save(workFlowReport);


        handleOnMessage(message,files,memo);








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


    private  void handleOnMessage(  WorkFlowMessage message, MultipartFile[] files, String memo)
    {

        if(files==null) return ;
        final int length = files.length;

        message.receiveTime = Calendar.getInstance().getTimeInMillis();
        message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


        message.memo = memo;
        //图片文件处理， 生成url‘
        String[] urlPaths = new String[length];

        for (int i = 0; i < length; i++) {
            MultipartFile file = files[i];


            String fileName = Calendar.getInstance().getTimeInMillis() + file.getName() + FileUtils.SUFFIX_JPG;
            final String absoluteFilePath = workflowfilepath + fileName;

            try {
                FileUtils.copy(file, absoluteFilePath);
                urlPaths[i] = workflowfileurl + fileName;
            } catch (IOException e) {
                e.printStackTrace();

                logger.error(e.getMessage());
            }

        }

        message.pictures = StringUtils.combine(urlPaths);

        message.receiveTime = Calendar.getInstance().getTimeInMillis();
        message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());



    }


    /**
     * 流程拒绝接收处理。
     *

     * @param workFlowMsgId
     * @param files
     * @param memo
     * @return
     */
    @Transactional
    public RemoteData<Void> rejectWorkFlowMessage(User loginUser, long workFlowMsgId,  MultipartFile[] files, String memo) {

        final int length = files.length;
        if (length < 3) {
            return wrapError("接收，要传递三张图片");
        }

        WorkFlowMessage message = workFlowMessageRepository.findOne(workFlowMsgId);
        if (message == null) {
            return wrapError("消息不存在：" + workFlowMsgId);
        }


        //人员验证
        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndWorkFlowCodeEqualsAndReceiveEquals(loginUser.id, message.toFlowCode, true);
        boolean canOperate = workFlowWorker != null;

        if (!canOperate) {
            return wrapError("未配置对该流程处理的权限");

        }



        handleOnMessage(message,files,memo);

        ErpOrderItemProcess erpOrderItemProcess = erpOrderItemProcessRepository.findOne(message.orderItemProcessId);
        if (erpOrderItemProcess == null) {
            return wrapError("未找到对应的流程状态信息");
        }


        //上流程 数量整理
        erpOrderItemProcess.sendingQty -= message.transportQty;
        erpOrderItemProcess.unSendQty  += message.transportQty;
     //   erpOrderItemProcess.sentQty += message.transportQty;


        erpOrderItemProcessRepository.save(erpOrderItemProcess);




        if (message.state == WorkFlowMessage.STATE_SEND) {

            //无审核人 系统自动审核通过
            message.state = WorkFlowMessage.STATE_REJECT;
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



    public RemoteData<ProductWorkMemo> getProductWorkMemo(String productName,String pVersion)
    {


        return wrapData(productWorkMemoRepository.findByProductNameEqualsAndPVersionEquals(productName,pVersion));
    }

    public RemoteData<OrderItemWorkMemo> getOrderItemWorkMemo(String osNo,int  itm)
    {
        return wrapData(orderItemWorkMemoRepository.findByOsNoEqualsAndItmEquals(osNo,itm));
    }

    public RemoteData<Void> saveWorkMemo(int workFlowStep, String os_no, int itm, String orderItemWorkMemo, String prd_name, String pVersion, String productWorkMemo) {


        ErpWorkFlow erpWorkFlow=ErpWorkFlow.findByStep(workFlowStep);

        if(erpWorkFlow==null)
            return wrapError("未找到流程："+workFlowStep);

      OrderItemWorkMemo orderItemWorkMemoData=  orderItemWorkMemoRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(os_no,itm,workFlowStep);

        if(orderItemWorkMemoData==null)
        {
            orderItemWorkMemoData=new OrderItemWorkMemo();
            orderItemWorkMemoData.workFlowCode=erpWorkFlow.code;
            orderItemWorkMemoData.workFlowName=erpWorkFlow.name;
            orderItemWorkMemoData.workFlowStep=erpWorkFlow.step;
            orderItemWorkMemoData.osNo=os_no;
            orderItemWorkMemoData.itm=itm;

        }
        orderItemWorkMemoData.memo=orderItemWorkMemo;

        orderItemWorkMemoRepository.save(orderItemWorkMemoData);



        ProductWorkMemo productWorkMemoData=  productWorkMemoRepository.findFirstByProductNameEqualsAndPVersionEqualsAndWorkFlowStepEquals(prd_name,pVersion,workFlowStep);


        if(productWorkMemoData==null)
        {
            productWorkMemoData=  new ProductWorkMemo();
            productWorkMemoData.workFlowCode=erpWorkFlow.code;
            productWorkMemoData.workFlowName=erpWorkFlow.name;
            productWorkMemoData.workFlowStep=erpWorkFlow.step;
            productWorkMemoData.productName=prd_name;
            productWorkMemoData.pVersion=pVersion;

        }
        productWorkMemoData.memo=productWorkMemo;

        productWorkMemoRepository.save(productWorkMemoData);



        return wrapData();

    }
}
