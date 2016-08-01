package com.giants3.hd.domain.repository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.entity_erp.ErpOrder;
import com.giants3.hd.utils.noEntity.ErpOrderDetail;
import rx.Observable;

import java.util.List;

/**
 *
 * 权限接口
 * Created by david on 2015/10/6.
 */
public interface AuthRepository {


    /**
     *读取报价明细权限

     */
    Observable<RemoteData<QuoteAuth>> getQuoteAuthList( );

    /**
     * 保存订单详情
     * @param quoteAuths
     * @return
     */
    Observable<RemoteData<QuoteAuth>>  saveQuoteAuthList(List<QuoteAuth> quoteAuths);
}
