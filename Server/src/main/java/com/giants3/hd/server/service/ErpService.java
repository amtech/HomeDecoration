package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.entity_erp.ErpOrderItem;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Erp 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class ErpService extends AbstractService implements InitializingBean, DisposableBean {


    ErpOrderRepository repository;

    EntityManager manager;

    @Autowired
    ProductRepository productRepository;


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


        List<ErpOrder>  result=  repository.findOrders(key,pageIndex,pageSize);

        int totalCount=  repository.getOrderCountByKey(key );
        return wrapData(pageIndex,pageSize,(totalCount-1)/pageSize+1,totalCount, result );

    }


    public RemoteData<ErpOrderItem> findItemsByOrderNo(String orderNo )
    {

        List<ErpOrderItem> orderItems=  repository.findItemsByOrderNo(orderNo);
        //从报价系统读取产品的单位信息，图片信息
        for(ErpOrderItem item:orderItems)
        {


            Product  product=   productRepository.findFirstByNameEqualsAndPVersionEquals(item.prd_name,"");
          //  item.prd_no

            if(product!=null)
            {
                item.ut=product.pUnitName;
                item.url=product.url;
            }
        }

        return wrapData(orderItems);

    }

}
