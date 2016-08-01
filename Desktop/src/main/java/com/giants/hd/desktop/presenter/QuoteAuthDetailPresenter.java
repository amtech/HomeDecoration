package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.entity.QuoteAuth;

import java.util.List;

/**
 *
 *   报价权限明细详情展示层接口
 * Created by davidleen29 on 2016/7/14.
 */
public interface QuoteAuthDetailPresenter extends    Presenter {




    void onRelateUsesSelected(List<Integer> indexes);
    /**
     * 报价明细记录被选中
     * @param row
     */
    void onQuoteAuthRowSelected(int row);
}
