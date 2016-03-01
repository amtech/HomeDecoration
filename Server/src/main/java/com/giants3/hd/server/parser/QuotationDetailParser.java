package com.giants3.hd.server.parser;

import com.giants3.hd.appdata.AQuotationDetail;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import org.springframework.stereotype.Component;

/**
 * 桌面端Product 转换成app 端AProduct数据
 * <p/>
 * Created by david on 2016/1/2.
 */

@Component
//默认的 qualifier  为首字母小写的类名
public class QuotationDetailParser implements DataParser<QuotationDetail, AQuotationDetail> {


    @Override
    public AQuotationDetail parse(QuotationDetail data) {
        AQuotationDetail aQuotation = new AQuotationDetail();


        return aQuotation;
    }


}
