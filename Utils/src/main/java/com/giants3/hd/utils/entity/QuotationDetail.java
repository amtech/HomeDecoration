package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 报价详细数据
 */
public class QuotationDetail implements Serializable {

        public Quotation quotation;
       public List<QuotationItem> items;





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuotationDetail)) return false;

        QuotationDetail that = (QuotationDetail) o;

        if (quotation != null ? !quotation.equals(that.quotation) : that.quotation != null) return false;
        return !(items != null ? !items.equals(that.items) : that.items != null);

    }

    @Override
    public int hashCode() {
        int result = quotation != null ? quotation.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    public void init() {
        quotation=new Quotation();
        items=new ArrayList<>();
    }
}
