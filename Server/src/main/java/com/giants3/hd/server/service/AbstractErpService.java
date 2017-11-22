package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;

import javax.persistence.EntityManager;

/**
 * Created by davidleen29 on 2017/11/4.
 */
public class AbstractErpService extends AbstractService {


    private EntityManager manager;

    @Override
    public void destroy() throws Exception {
        super.destroy();
        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        onEntityManagerCreate(manager);

    }


    protected void onEntityManagerCreate(EntityManager manager) {
    }
}
