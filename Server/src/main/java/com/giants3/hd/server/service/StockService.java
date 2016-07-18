package com.giants3.hd.server.service;

import com.giants3.hd.server.entity_erp.ErpStockOut;
import com.giants3.hd.server.entity_erp.ErpStockOutItem;
import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.noEntity.ErpStockOutDetail;
import com.giants3.hd.server.repository.ErpStockOutRepository;
import com.giants3.hd.utils.RemoteData;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户  业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class StockService extends AbstractService  {


    EntityManager manager;

    ErpStockOutRepository repository;
    @Override
    public void destroy() throws Exception {
        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        repository = new ErpStockOutRepository(manager);
    }
    /**
     * 查询出库列表
     * @param key
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<ErpStockOut> search(String key, int pageIndex, int pageSize) {




        List<ErpStockOut> erpStockOuts=repository.stockOutList(key,pageIndex,pageSize);


        RemoteData<ErpStockOut> remoteData=new RemoteData<>();


        remoteData.datas=erpStockOuts;
        return remoteData;
    }


    /**
     * 查询出库列表
     * @param ck_no

     * @return
     */
    public RemoteData<ErpStockOutDetail> findDetail(String ck_no) {




        List<ErpStockOutItem> erpStockOuts=repository.stockOutItemsList(ck_no );



        ErpStockOutDetail detail=new ErpStockOutDetail();
        ArrayList<ErpStockOutDetail> list=new ArrayList();
        list.add(detail);

        RemoteData<ErpStockOutDetail> remoteData=new RemoteData<>();

        detail.erpStockOut=repository.findStockOut(ck_no);
        detail.items=erpStockOuts;
        remoteData.datas=list;
        return remoteData;
    }


}
