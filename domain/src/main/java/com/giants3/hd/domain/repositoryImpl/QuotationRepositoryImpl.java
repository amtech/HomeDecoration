package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.datasource.QuotationDataSourceImpl;
import com.giants3.hd.domain.datasource.QuotationDataStore;
import com.giants3.hd.domain.repository.QuotationRepository;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.noEntity.QuotationDetail;
import rx.Observable;

import java.util.List;

/**
 * Created by david on 2015/9/14.
 */
public class QuotationRepositoryImpl implements QuotationRepository {
    public Observable<List<Quotation>> quotations() {



        return null;
    }

    public Observable<QuotationDetail> detail(long quotationId) {

        QuotationDataStore quotationDataStore=new QuotationDataSourceImpl();

        return quotationDataStore.quotationDetail(quotationId);
    }
}
