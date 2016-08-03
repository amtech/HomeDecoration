package com.giants3.hd.domain.repository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.OrderAuth;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.entity.StockOutAuth;
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
     * 保存报价权限
     * @param quoteAuths
     * @return
     */
    Observable<RemoteData<QuoteAuth>>  saveQuoteAuthList(List<QuoteAuth> quoteAuths);

    /**
     * 获取订单权限
     * @return
     */
    Observable<RemoteData<OrderAuth>>  getOrderAuthList();

    /**
     * 保存订单权限
     * @param orderAuths
     * @return
     */
    Observable<RemoteData<OrderAuth>>  saveOrderAuthList(List<OrderAuth> orderAuths);


    /**
     * 获取出库权限
     * @return
     */
    Observable<RemoteData<StockOutAuth>>  getStockOutAuthList();

    /**
     * 保存出库权限
     * @param stockOutAuths
     * @return
     */
    Observable<RemoteData<StockOutAuth>>  saveStockOutAuthList(List<StockOutAuth> stockOutAuths);

}