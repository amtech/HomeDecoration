package com.giants3.hd.domain.repository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.entity_erp.ErpStockOut;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;
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
}
