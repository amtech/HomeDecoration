package com.giants3.hd.domain.repository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.OrderItemWorkFlowState;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity.WorkFlowProduct;
import com.giants3.hd.utils.entity.WorkFlowSubType;
import rx.Observable;

import java.util.List;

/** 生产进度接口
 * Created by david on 20160917
 */
public interface WorkFlowRepository {


    Observable<RemoteData<WorkFlow>> getWorkFlowList();

    Observable<RemoteData<WorkFlow>> saveWorkFlowList(List<WorkFlow> workFlows);

    /**
     * 启动订单跟踪
     * @param os_no
     * @return
     */
    Observable<RemoteData<Void>> startOrderTrack(String os_no);

    Observable<RemoteData<WorkFlowSubType>> getWorkFlowSubTypes();

    Observable<RemoteData<WorkFlowProduct>>  getWorkFlowOfProduct(long productId);

    Observable<RemoteData<WorkFlowProduct>> saveWorkFlowProduct(WorkFlowProduct workFlowProduct);

    /**
     * 获取货款的进度数据
     * @param orderItemId
     * @return
     */
    Observable<RemoteData<OrderItemWorkFlowState>> getOrderItemWorkFlowState(long orderItemId);

    /**
     * 报春
     * @param datas
     * @return
     */
    Observable saveWorkFlowSubTypeList(List<WorkFlowSubType> datas);
}
