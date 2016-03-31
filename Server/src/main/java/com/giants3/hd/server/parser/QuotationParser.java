package com.giants3.hd.server.parser;

import com.giants3.hd.appdata.AQuotation;
import com.giants3.hd.server.entity.Quotation;
import org.springframework.stereotype.Component;

/**  桌面端Quotation 转换成app AQuotation
 *
 * Created by david on 2016/1/2.
 */

@Component
//默认的 qualifier  为首字母小写的类名  productParser
public class QuotationParser implements DataParser<Quotation,AQuotation> {
    @Override
    public AQuotation parse(Quotation data) {
        AQuotation aQuotation=new AQuotation();

        aQuotation.qName=data.qNumber;
        aQuotation.id=data.id;
        aQuotation.salesman=data.salesman;
        aQuotation.vDate=data.vDate;
        aQuotation.qDate=data.qDate;

        return aQuotation;
    }
}
