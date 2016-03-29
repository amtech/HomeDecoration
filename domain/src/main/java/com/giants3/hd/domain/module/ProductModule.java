package com.giants3.hd.domain.module;

import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.domain.repositoryImpl.ProductRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 * Created by david on 2015/9/15.
 */
public class ProductModule extends AbstractModule {


    @Override
    protected void configure() {


        bind(ProductRepository.class).to(ProductRepositoryImpl.class);


    }

//    @Provides
//    Account providePurchasingAccount(Product product) {
//        return product.quotations();
//    }
}
