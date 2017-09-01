package com.giants3.hd.server.service;

import com.giants3.hd.entity.Factory;
import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.FactoryRepository;
import com.giants3.hd.server.repository.OutFactoryRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by davidleen29 on 2017/1/14.
 */
@Service
public class FactoryService extends AbstractService {


    @Autowired
    private FactoryRepository factoryRepository;

    EntityManager manager;


    private OutFactoryRepository outFactoryRepository;

    @Override
    public void destroy() throws Exception {
        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        outFactoryRepository = new OutFactoryRepository(manager);

    }
    /**
     * 获取最新的全局配置数据表
     *
     * @return
     */
    public RemoteData<Factory> listFactory() {

        return wrapData(factoryRepository.findAll());


    }

    /**
     * 获取外厂数据列表
     *
     * @return
     */
    public RemoteData<OutFactory> listOutFactory() {


        return wrapData(outFactoryRepository.findAll());


    }


    /**
     * @param factories
     */
    @Transactional
    public void save(List<Factory> factories) {


        for (Factory factory : factories) {


            Factory oldData = factoryRepository.findFirstByCodeEquals(factory.code);
            if (oldData == null) {
                factory.id = -1;


            } else {
                factory.id = oldData.id;


            }
            factoryRepository.save(factory);


        }
    }

    /**
     * 保存外厂家列表
     * 添加新增的
     * 修改存在的值
     * 不存在的，删除
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = {HdException.class})
    public void saveOutList(List<OutFactory> factories) {

        //加工户信息 来自erp，不做修改维护


    }
}
