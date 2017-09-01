package com.giants3.hd.domain.repository;

import com.giants3.hd.entity.Quotation;
import com.giants3.hd.noEntity.QuotationDetail;
import rx.Observable;

import java.util.List;

/**
 * Created by david on 2015/9/14.
 */
public interface QuotationRepository {



    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Quotation}.
     */
    Observable<List<Quotation>> quotations();

    /**
     * Get an {@link rx.Observable} which will emit a {@link QuotationDetail}.
     *
     * @param quotationId The user id used to retrieve user data.
     */
     Observable<QuotationDetail> detail(final long quotationId);

}
