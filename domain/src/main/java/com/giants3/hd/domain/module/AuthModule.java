package com.giants3.hd.domain.module;

import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.domain.repository.StockRepository;
import com.giants3.hd.domain.repositoryImpl.AuthRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.StockRepositoryImpl;
import com.google.inject.AbstractModule;

/**
 * 权限模块绑定
 * Created by david on 2015/9/15.
 */
public class AuthModule extends AbstractModule {





    @Override
    protected void configure() {
        bind(AuthRepository.class).to(AuthRepositoryImpl.class);
    }

//    @Provides
//    Account providePurchasingAccount(Product product) {
//        return product.quotations();
//    }
}
