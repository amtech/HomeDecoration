package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Flow;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.entity.ProductWage;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.noEntity.ProductDetail;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * quotation 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class ErpService extends AbstractService implements InitializingBean, DisposableBean {


    ErpOrderRepository repository;

    EntityManager manager;


    @Override
    public void destroy() throws Exception {


        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        repository = new ErpOrderRepository(manager);
    }



    public RemoteData<ErpOrder> findByKey(String key,int pageIndex,int pageSize)
    {

      List<ErpOrder> orders=  repository.findOrders(key,pageIndex,pageSize);
        return wrapData(orders);

    }

}
