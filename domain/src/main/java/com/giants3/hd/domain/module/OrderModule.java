package com.giants3.hd.domain.module;

import com.giants3.hd.domain.datasource.QuotationDataSourceImpl;
import com.giants3.hd.domain.datasource.QuotationDataStore;
import com.giants3.hd.domain.repository.OrderRepository;
import com.giants3.hd.domain.repository.QuotationRepository;
import com.giants3.hd.domain.repositoryImpl.OrderRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.QuotationRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 * Created by david on 2015/9/15.
 */
public class OrderModule extends AbstractModule {





    @Override
    protected void configure() {


        bind(OrderRepository.class).to(OrderRepositoryImpl.class);



    }

//    @Provides
//    Account providePurchasingAccount(Product product) {
//        return product.quotations();
//    }
}