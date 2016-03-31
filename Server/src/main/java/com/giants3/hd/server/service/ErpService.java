package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.entity.Product;
import com.giants3.hd.server.entity.User;
import com.giants3.hd.server.entity_erp.ErpOrder;
import com.giants3.hd.server.entity_erp.ErpOrderItem;
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

    @Autowired
    private UserRepository userRepository;


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


    public RemoteData<ErpOrder> findByKey(String key, int pageIndex, int pageSize) {


        List<ErpOrder> result = repository.findOrders(key, pageIndex, pageSize);
        //进行业务员配对。

        for (ErpOrder order : result) {
            User user = userRepository.findFirstByIsSalesmanAndCodeEquals(true, order.sal_no);

            order.sal_name = user == null ? (order.sal_no + "()") : (("(") + user.name + ")" + user.chineseName);

        }


        int totalCount = repository.getOrderCountByKey(key);
        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, result);

    }


    public RemoteData<ErpOrderItem> findItemsByOrderNo(String orderNo, boolean isFromDesk) {

        List<ErpOrderItem> orderItems = repository.findItemsByOrderNo(orderNo);
        //从报价系统读取产品的单位信息，图片信息
        for (ErpOrderItem item : orderItems) {


            Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(item.prd_name, "");
            //  item.prd_no

            if (product != null) {
                item.ut = product.pUnitName;
                //来自桌面端的请求才返回缩微图片，减少传输数据量。
                if (isFromDesk)
                    item.photo = product.photo;
                item.productId = product.id;
                item.url = product.url;
            }
        }

        return wrapData(orderItems);

    }

}
