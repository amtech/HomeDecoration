package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.Factory;
import com.giants3.hd.server.entity.OutFactory;
import com.giants3.hd.server.repository.FactoryRepository;
import com.giants3.hd.server.repository.OutFactoryRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2017/1/14.
 */
@Service
public class FactoryService extends AbstractService {


    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private OutFactoryRepository outFactoryRepository;


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


        List<OutFactory> oldFactories = outFactoryRepository.findAll();

        List<OutFactory> removed = new ArrayList<>();

        for (OutFactory oldFactory : oldFactories) {
            boolean foundInNew = false;
            for (OutFactory newUser : factories) {

                if (oldFactory.id == newUser.id) {
                    foundInNew = true;
                }


            }
            if (!foundInNew) {
                removed.add(oldFactory);
            }

        }

        for (OutFactory factory : factories) {
            OutFactory oldData = outFactoryRepository.findFirstByNameEquals(factory.name);
            if (oldData == null) {
                factory.id = -1;


            } else {
                factory.id = oldData.id;

            }
            outFactoryRepository.save(factory);


            /**
             *  删除不存在item
             */
            for (OutFactory deleteFactory : removed) {

//                if(quotationRepository.findFirstByCustomerIdEquals(deletedCustomer.id)!=null)
//                {
//                    throw HdException.create("不能删除客户【"+deletedCustomer.name+"】,目前有报价单关联着");
//                }
                outFactoryRepository.delete(deleteFactory);


            }

        }



    }
}
