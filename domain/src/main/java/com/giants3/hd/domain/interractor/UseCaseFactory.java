package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repositoryImpl.ProductRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.QuotationRepositoryImpl;
import com.giants3.hd.domain.repositoryImpl.XiankangRepositoryImpl;
import com.giants3.hd.utils.entity.Xiankang;
import rx.schedulers.Schedulers;


/**
 * Created by david on 2015/9/16.
 */
public class UseCaseFactory  {








    public static UseCase createQuotationDetail(long qutationId )
    {


        return new GetQuotationDetail( Schedulers.newThread()    ,Schedulers.immediate(),qutationId, new QuotationRepositoryImpl());
    }


    public static UseCase createProductByNameBetween(String startName,String endName,boolean withCopy)
    {




        return new ProductUseCase( Schedulers.newThread()    ,Schedulers.immediate(),startName,endName,withCopy, new ProductRepositoryImpl());
    }


    public static UseCase createUpdateXiankang()
    {

        return new UpdateXiankangUseCase(Schedulers.newThread(),Schedulers.immediate(),new XiankangRepositoryImpl());
    }
}
