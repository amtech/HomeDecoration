package com.giants3.hd.domain.datasource;

import com.giants3.hd.utils.entity.Quotation;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import rx.Observable;

import java.util.List;

/**
 *
 */
public interface QuotationDataStore {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link com.giants3.hd.utils.entity.Quotation}.
     */
    Observable<List<Quotation>> userEntityList();

    /**
     * Get an {@link rx.Observable} which will emit a {@link com.giants3.hd.utils.noEntity.QuotationDetail} by its id.
     *
     * @param quotationId The id to retrieve user data.
     */
    Observable<QuotationDetail> quotationDetail(final long quotationId);
}
