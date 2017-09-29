package com.giants3.hd.server.service;

import com.giants3.hd.noEntity.ProduceType;
import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.*;

import com.giants3.hd.entity_erp.WorkFlowMaterial;
import com.giants3.hd.entity_erp.Zhilingdan;
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
//    @Autowired
//    OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemWorkStateRepository orderItemWorkStateRepository;

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

                zhilingdan.caigou_dd =StringUtils.clipSqlDateData( zhilingdan.caigou_dd) ;

                zhilingdan.jinhuo_dd = StringUtils.clipSqlDateData(zhilingdan.jinhuo_dd) ;

                zhilingdan.mo_dd = StringUtils.clipSqlDateData(zhilingdan.mo_dd) ;


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


    public RemoteData<ErpWorkFlowReport> findErpWorkFlowReport(String os_no, int itm)
    {
        ErpOrderItem erpOrderItem=erpWorkRepository.findOrderItem(os_no,itm);
        if(erpOrderItem==null) return wrapError("未查找到订单货款:"+os_no+",item="+itm);


        if(erpOrderItem.produceType== ProduceType.NOT_SET)
        {
            return wrapError("该订单未排厂");

        }

       return findErpWorkFlowReport( erpOrderItem );
    }

    /**
     * 查找xxxx的进度报表
     */
    public RemoteData<ErpWorkFlowReport> findErpWorkFlowReport(ErpOrderItem erpOrderItem ) {



        //查询本地数据库的报表记录
        final List<ErpWorkFlowReport> erpWorkFlowReports = erpWorkFlowReportRepository.findByOsNoEqualsAndItmEquals(erpOrderItem.os_no, erpOrderItem.itm);




        //本地不存在 查询erp。
        if (ArrayUtils.isEmpty(erpWorkFlowReports)) {










            //查詢是否排廠
            if(erpOrderItem.produceType== ProduceType.SELF_MADE) {
                //内厂的排厂数据处理

                List<ErpOrderItemProcess> processes = erpWorkRepository.findOrderItemProcesses(erpOrderItem.os_no, erpOrderItem.itm, true);

                if (ArrayUtils.isEmpty(processes))
                    return wrapError("该订单未排厂");


                //erp 排厂数据整合临时数据  已经排厂，未进行流程活动， 默认进度都是0，进行过流程活动了， 才保存在 ErpWorkFlowReport 表中。


                //不以abcd开头的process 数据，就是成品数据


                boolean hasZuzhuang = false;
                for (ErpOrderItemProcess process : processes) {
                    if (process.mrpNo.startsWith(ErpWorkFlow.CODE_ZUZHUANG)) {
                        hasZuzhuang = true;
                        break;

                    }

                }


                for (ErpWorkFlow erpWorkFlow : ErpWorkFlow.WorkFlows) {


                    //判断排厂中是否有当前流程
                    boolean hasThisFlow = false;
                    ErpOrderItemProcess findProcess = null;


                    Set<String> typeSet = new HashSet<>();
                    for (ErpOrderItemProcess process : processes) {


                        if (process.mrpNo.startsWith(erpWorkFlow.code)) {
                            findProcess = process;
                            typeSet.add(process.mrpNo.substring(0, 2));// 取头两位

                            hasThisFlow = true;

                        }
                    }

                    if (!hasThisFlow) continue;
                    ErpWorkFlowReport erpWorkFlowReport = new ErpWorkFlowReport();
                    erpWorkFlowReport.workFlowCode = erpWorkFlow.code;

                    //包装类型， 检查是否有组装
                    if (ErpWorkFlow.CODE_BAOZHUANG.equals(erpWorkFlow.code)) {


                        erpWorkFlowReport.workFlowName = (hasZuzhuang ? ErpWorkFlow.NAME_ZUZHUANG : "") + ErpWorkFlow.NAME_BAOZHUANG;
                    } else {
                        erpWorkFlowReport.workFlowName = erpWorkFlow.name;
                    }

                    erpWorkFlowReport.workFlowStep = erpWorkFlow.step;
                    erpWorkFlowReport.osNo = erpOrderItem.os_no;
                    erpWorkFlowReport.itm = erpOrderItem.itm;
                    erpWorkFlowReport.prdNo = findProcess.prdNo;
                    erpWorkFlowReport.pVersion = findProcess.pVersion;
                    erpWorkFlowReport.typeCount = typeSet.size();
                    erpWorkFlowReport.percentage = 0;

                    erpWorkFlowReports.add(erpWorkFlowReport);
                }


            }else


            //外购数据
            if(erpOrderItem.produceType== ProduceType.PURCHASE)
            {

             for(ErpWorkFlow erpWorkFlow: ErpWorkFlow.purchaseWorkFLows)
             {

                 ErpWorkFlowReport erpWorkFlowReport = new ErpWorkFlowReport();
                 erpWorkFlowReport.workFlowCode = erpWorkFlow.code;

                 erpWorkFlowReport.workFlowStep = erpWorkFlow.step;
                 erpWorkFlowReport.workFlowName = erpWorkFlow.name;
                 erpWorkFlowReport.osNo = erpOrderItem.os_no;
                 erpWorkFlowReport.itm = erpOrderItem.itm;
                 erpWorkFlowReport.prdNo = erpOrderItem.prd_no;
                 erpWorkFlowReport.pVersion =erpOrderItem.pVersion;
                 erpWorkFlowReport.typeCount = 1;
                 erpWorkFlowReport.percentage = 0;

                 erpWorkFlowReports.add(erpWorkFlowReport);
             }




            }




            for(ErpWorkFlowReport erpWorkFlow: erpWorkFlowReports)
            {
                erpWorkFlow.produceType=erpOrderItem.produceType;
                erpWorkFlow.produceTypeName=erpOrderItem.produceTypeName;

            }


        }








        return wrapData(erpWorkFlowReports);
    }


    /**
     * 查找 ，并且未完成的订单款项  款项包含已下单，未启动流程
     */
    public List<ErpOrderItem> searchUnCompleteOrderItems(String key) {



        //配置 url
        return getErpOrderItems(key);


    }  /**
     * 查找 ，并且未完成的订单款项  款项包含已下单，未启动流程
     * @param type  类型   下单未排  白胚未入  白胚  颜色  包装  产品库
     */
    public List<ErpOrderItem> searchUnCompleteOrderItems(String key,int type) {




        final List<ErpOrderItem> erpOrderItems = getErpOrderItems(key);









        return erpOrderItems;


    }

    private List<ErpOrderItem> getErpOrderItems(String key) {
        //配置 url
        final List<ErpOrderItem> erpWorkFlowOrderItems = erpWorkRepository.searchUnCompleteOrderItems(key);


        for (ErpOrderItem orderItem:erpWorkFlowOrderItems) {


            orderItem.url = FileUtils.getErpProductPictureUrl(orderItem.id_no,String.valueOf(orderItem.photoUpdateTime));
            orderItem.thumbnail =orderItem.url ;
        }

        return erpWorkFlowOrderItems;
    }


    /**
     *  查找已经开启流程活动，并且未完成的订单款项
     */
    public List<ErpOrderItem> searchHasStartWorkFlowUnCompleteOrderItems(String key) {



        //配置 url
        final List<ErpOrderItem> erpWorkFlowOrderItems = erpWorkRepository.searchHasStartWorkFlowUnCompleteOrderItems(key);


        for (ErpOrderItem orderItem:erpWorkFlowOrderItems) {


            orderItem.url = FileUtils.getErpProductPictureUrl(orderItem.id_no, String.valueOf(orderItem.photoUpdateTime));
            orderItem.thumbnail =orderItem.url ;
        }

        return erpWorkFlowOrderItems;


    }

    /**
     * 外购 可用流程处理
     * @param loginUser
     * @param os_no
     * @param itm
     * @param flowStep
     * @return
     */
    public RemoteData<ErpOrderItemProcess> getAvailablePurchaseOrderItemProcess(User loginUser,String os_no, int itm, int flowStep)
    {



        //外购 流程
        //查找前一个流程
        if(flowStep!=ErpWorkFlow.FIRST_STEP )  //第一道 不需要上一道100%完成
        {
            int previousStep=  ErpWorkFlow.findPurchasePrevious(flowStep);

            final ErpWorkFlowReport erpWorkFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(os_no, itm, previousStep);
            if(erpWorkFlowReport==null||erpWorkFlowReport.percentage<1)
            {
                return wrapError("上一道流程未交接完毕，不能发起交接");
            }

        }



        List<ErpOrderItemProcess> orderItemProcesses=erpWorkRepository.findPurchaseOrderItemProcesses(os_no,itm);



        for (ErpOrderItemProcess process:orderItemProcesses)
        {

            ErpOrderItemProcess localProcess = erpOrderItemProcessRepository.findFirstByMoNoEqualsAndMrpNoEquals(process.moNo,process.mrpNo);
            if (localProcess != null)
                attachData(process, localProcess);


        }




        //下一个节点
        ErpWorkFlow nextFlow = flowStep== ErpWorkFlow.LAST_STEP?null:ErpWorkFlow.findPurchaseNext(flowStep);
        ErpWorkFlow workFlow=ErpWorkFlow.findPurchaseByStep(flowStep);
        for (ErpOrderItemProcess process:orderItemProcesses) {
            process.currentWorkFlowCode = workFlow.code;
            process.currentWorkFlowStep = workFlow.step;
            process.currentWorkFlowName = workFlow.name;
            process.unSendQty = process.qty;


            process.nextWorkFlowCode = nextFlow == null ? "" : nextFlow.code;
            process.nextWorkFlowStep = nextFlow == null ? 0 : nextFlow.step;
            process.nextWorkFlowName = nextFlow == null ? "" : nextFlow.name;
        }




        return wrapData(orderItemProcesses);
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




        ErpOrderItem erpOrderItem=erpWorkRepository.findOrderItem(os_no,itm);
        WorkFlowWorker workFlowWorker=workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowStepEquals(loginUser.id,erpOrderItem.produceType,flowStep);

        if(workFlowWorker==null)

            return wrapError("当前节点未配置工作人员");

        if(!workFlowWorker.send)
        {
            return wrapError("当前流程无发送权限");
        }




        if(erpOrderItem.produceType==ProduceType.PURCHASE)
        {


            return getAvailablePurchaseOrderItemProcess(loginUser,os_no,itm,flowStep);
        }


        ErpWorkFlow workFlow = ErpWorkFlow.findByStep(flowStep);







        //自制 流程
        //查找前一个流程
        if(flowStep!=ErpWorkFlow.FIRST_STEP&&flowStep!= ErpWorkFlow.SECOND_STEP)  //第一道第二道 都不需要上一道100%完成
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


        //最后一道不需要验证排厂
        if(workFlow.step!=ErpWorkFlow.LAST_STEP) {
            for (ErpOrderItemProcess process : result) {

                if (StringUtils.isEmpty(process.jgh)) {
                    return wrapError("当前流程未排厂加工户");
                }
            }
        }

        List<ErpOrderItemProcess> availableResult = new ArrayList<>();
        for (ErpOrderItemProcess process : result) {
            process.currentWorkFlowCode = workFlow.code;
            process.currentWorkFlowStep = workFlow.step;
            process.currentWorkFlowName = workFlow.name;
            process.unSendQty = process.qty;


                process.nextWorkFlowCode =nextFlow==null?"": nextFlow.code;
                process.nextWorkFlowStep = nextFlow==null?0: nextFlow.step;
                process.nextWorkFlowName =  nextFlow==null?"":nextFlow.name;





            ErpOrderItemProcess localProcess = erpOrderItemProcessRepository.findFirstByMoNoEqualsAndMrpNoEquals(process.moNo,process.mrpNo);
            if (localProcess != null)
                attachData(process, localProcess);



            //判断判断类型 铁木
            //只有白胚，颜色才区分铁木
            if(process.mrpNo.startsWith(ErpWorkFlow.CODE_YANSE)||process.mrpNo.startsWith(ErpWorkFlow.SECOND_STEP_CODE)

                    ||process.mrpNo.startsWith(ErpWorkFlow.FIRST_STEP_CODE)  //第一道也增加鐵木

                    )
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
        erpOrderItemProcess.so_zxs = localData.so_zxs;
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








        WorkFlowArea workFlowArea   = workFlowAreaRepository.findOne(areaId);;
        if(erpOrderItemProcess.currentWorkFlowStep!=ErpWorkFlow.LAST_STEP) {

            if (workFlowArea == null) {
                return wrapError("未选择交接区域");
            }
            //最后一个节点不需要验证下一流程数据
            if (erpOrderItemProcess.nextWorkFlowStep <= 0  )
                return wrapError("该订单下一流程数据不存在");

//        WorkFlow workFlow = workFlowRepository.findFirstByFlowStepEquals(workFlowOrderItemState.currentWorkFlowStep);
//        if (workFlow == null)
//            return wrapError("数据异常，该订单当前流程不存在");
//        WorkFlow newWorkFlow = workFlowRepository.findFirstByFlowStepEquals(workFlowOrderItemState.nextWorkFlowStep);
//        if (newWorkFlow == null)
//            return wrapError("数据异常，该订单下一流程数据不存在");
        }

        if (tranQty > erpOrderItemProcess.unSendQty) {

            return wrapError("提交数量超过当前流程数量");
        }

        ErpOrderItem erpOrderItem=erpWorkRepository.findOrderItem(erpOrderItemProcess.osNo,erpOrderItemProcess.itm);

        //验证人员

        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndSendEquals(user.id, erpOrderItem.produceType,erpOrderItemProcess.currentWorkFlowCode, true);
        if (workFlowWorker == null) {
            return wrapError("无权限在当前节点:" + erpOrderItemProcess.currentWorkFlowName + " 发送流程");
        }



        OrderItemWorkState orderItem=     orderItemWorkStateRepository.findFirstByOsNoEqualsAndItmEquals(erpOrderItemProcess.osNo,erpOrderItemProcess.itm);

        if(orderItem==null) {
            orderItem = new OrderItemWorkState();
            orderItem.osNo = erpOrderItemProcess.osNo;
            orderItem.itm = erpOrderItemProcess.itm;
            orderItem.url = erpOrderItemProcess.photoUrl;
            orderItem.prdNo = erpOrderItemProcess.prdNo;
            orderItem.maxWorkFlowStep=erpOrderItemProcess.currentWorkFlowStep;
            orderItem.maxWorkFlowCode=erpOrderItemProcess.currentWorkFlowCode;
            orderItem.maxWorkFlowName=erpOrderItemProcess.currentWorkFlowName;
        }

        //标记当前订单生产状态
        orderItem.workFlowState=ErpWorkFlow.STATE_WORKING;
        orderItem.workFlowDescribe="消息发送自:"+erpOrderItemProcess.currentWorkFlowCode+erpOrderItemProcess.currentWorkFlowName;




        orderItem= orderItemWorkStateRepository.save(orderItem);


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
        workFlowMessage.area =workFlowArea==null?"": workFlowArea.name  ;
        workFlowMessage.name = WorkFlowMessage.NAME_SUBMIT;

        workFlowMessage.senderId=user.id;
        workFlowMessage.senderName=user.toString();

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
        workFlowMessage.bat_no = erpOrderItem.bat_no;
        workFlowMessage.cus_no = erpOrderItem.cus_no;

        workFlowMessage=  workFlowMessageRepository.save(workFlowMessage);


        RemoteData<ErpWorkFlowReport> workFlowReports = findErpWorkFlowReport(erpOrderItem);

        if (workFlowReports.isSuccess()) {
            erpWorkFlowReportRepository.save(workFlowReports.datas);
        }













        if(erpOrderItemProcess.currentWorkFlowStep==ErpWorkFlow.LAST_STEP)
        {




           //自动审核通过
            workFlowMessage.state = WorkFlowMessage.STATE_PASS;
            workFlowMessage.receiveTime = Calendar.getInstance().getTimeInMillis();
            workFlowMessage.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

            workFlowMessage.receiverId=user.id;
            workFlowMessage.receiverName=user.toString();


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
                orderItemWorkStateRepository.save(orderItem);
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



        ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName, message.itm, message.fromFlowStep);

        //人员验证
        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndReceiveEquals(loginUser.id,workFlowReport.produceType, message.toFlowCode, true);
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



        //更新生产进度
        workFlowReport.percentage += (float) message.transportQty / erpOrderItemProcess.orderQty / (workFlowReport.typeCount == 0 ? 1 : workFlowReport.typeCount);
        erpWorkFlowReportRepository.save(workFlowReport);


        handleOnMessage(message,files,memo);








        if (message.state == WorkFlowMessage.STATE_SEND) {

            //无审核人 系统自动审核通过
            message.state = WorkFlowMessage.STATE_PASS;
            message.checkTime = Calendar.getInstance().getTimeInMillis();
            message.checkTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

            message.receiverId=loginUser.id;
            message.receiverName=loginUser.toString();



        } else if (message.state == WorkFlowMessage.STATE_REWORK) {
            //返工 状态 自动通过。
            message.state = WorkFlowMessage.STATE_PASS;
            message.receiveTime = Calendar.getInstance().getTimeInMillis();
            message.receiveTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

            message.receiverId=loginUser.id;
            message.receiverName=loginUser.toString();
        }

        workFlowMessageRepository.save(message);



        OrderItemWorkState orderItem=     orderItemWorkStateRepository.findFirstByOsNoEqualsAndItmEquals(erpOrderItemProcess.osNo,erpOrderItemProcess.itm);

        if(orderItem!=null)
        {


            if(orderItem.maxWorkFlowStep<message.toFlowStep)
            {
                orderItem.maxWorkFlowStep=message.toFlowStep;
                orderItem.maxWorkFlowCode=message.toFlowCode;
                orderItem.maxWorkFlowName=message.toFlowName;
                orderItemWorkStateRepository.save(orderItem);
            }

        }



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

        ErpWorkFlowReport workFlowReport = erpWorkFlowReportRepository.findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(message.orderName, message.itm, message.fromFlowStep);



        //人员验证
        WorkFlowWorker workFlowWorker = workFlowWorkerRepository.findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndReceiveEquals(loginUser.id,workFlowReport.produceType, message.toFlowCode, true);
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

    public RemoteData<Void> saveWorkMemo(User loginUser,int workFlowStep, String os_no, int itm, String orderItemWorkMemo, String prd_name, String pVersion, String productWorkMemo) {


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
        orderItemWorkMemoData.lastModifierId = loginUser.id;
        orderItemWorkMemoData.lastModifierName=loginUser.toString();
        orderItemWorkMemoData.lastModifyTime=Calendar.getInstance().getTimeInMillis();
        orderItemWorkMemoData.lastModifyTimeString=DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());


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

        productWorkMemoData.lastModifierId = loginUser.id;
        productWorkMemoData.lastModifierName=loginUser.toString();
        productWorkMemoData.lastModifyTime=Calendar.getInstance().getTimeInMillis();
        productWorkMemoData.lastModifyTimeString=DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(Calendar.getInstance().getTime());

        productWorkMemoRepository.save(productWorkMemoData);



        return wrapData();

    }


    public RemoteData<WorkFlowMaterial> getWorkFlowMaterials(String osNo, int itm, String workFlowCode) {





      List<WorkFlowMaterial>  result=erpWorkRepository.searchWorkFlowMaterials(osNo,itm,workFlowCode);





        return wrapData(result);


    }



}