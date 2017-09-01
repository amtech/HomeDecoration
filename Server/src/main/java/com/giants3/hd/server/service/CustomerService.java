package com.giants3.hd.server.service;

import com.giants3.hd.entity.Customer;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.exception.HdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户  业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class CustomerService extends AbstractService  {


    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private QuotationRepository quotationRepository;




    /**
     * 保存用户列表
     * 添加新增的
     * 修改存在的值
     * 不存在的，删除
     *
     * @param newCustomers
     * @return
     */
    @Transactional(rollbackFor = {HdException.class})
    public List<Customer> saveCustomers(List<Customer> newCustomers) throws HdException {

        List<Customer> oldCustomers = customerRepository.findAll();

        List<Customer> removed = new ArrayList<>();

        for (Customer oldCustomer : oldCustomers) {
            boolean foundInNew = false;
            for (Customer newUser : newCustomers) {

                if (oldCustomer.id == newUser.id) {
                    foundInNew = true;
                }


            }
            if (!foundInNew) {
                removed.add(oldCustomer);
            }

        }

        for(Customer customer: newCustomers)
        {


            Customer oldData=customerRepository.findFirstByCodeEquals(customer.code);
            if(oldData==null)
            {
                customer.id=-1;


            }else
            {
                customer.id=oldData.id;

            }
            customerRepository.save(customer);


            /**
             *  删除不存在item   标记delete
             */
            for(Customer deletedCustomer:removed)
            {

                if(quotationRepository.findFirstByCustomerIdEquals(deletedCustomer.id)!=null)
                {
                    throw HdException.create("不能删除客户【"+deletedCustomer.name+"】,目前有报价单关联着");
                }
                customerRepository.delete(deletedCustomer );


            }

        }


        return  list();
    }



    /**
     * 查询用户列表
     * @return
     */
    public List<Customer> list()
    {

        return  customerRepository.findAll(new Sort("code")) ;
    }



    /**
     * 查询是否可以删除   是否有关联的数据
     *
     * @param customerId
     * @return
     */
    public boolean canDelete(long customerId) {


        return false
                ;
    }





}
