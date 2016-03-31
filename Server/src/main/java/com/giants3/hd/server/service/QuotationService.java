package com.giants3.hd.server.service;

import com.giants3.hd.server.repository.QuotationRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.entity.Quotation;
import com.giants3.hd.server.noEntity.QuotationDetail;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * quotation 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class QuotationService extends AbstractService implements InitializingBean, DisposableBean {


    @Autowired
    private QuotationRepository quotationRepository;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    public RemoteData<Quotation> search(String searchValue, long salesmanId
            , int pageIndex, int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<Quotation> pageValue;
        if (salesmanId < 0) {
            pageValue = quotationRepository.findByCustomerNameLikeOrQNumberLikeOrderByQDateDesc("%" + searchValue + "%", "%" + searchValue + "%", pageable);
        } else {
            pageValue = quotationRepository.findByCustomerNameLikeAndSalesmanIdEqualsOrQNumberLikeAndSalesmanIdEqualsOrderByQDateDesc("%" + searchValue + "%", salesmanId, "%" + searchValue + "%", salesmanId, pageable);

        }
        List<Quotation> products = pageValue.getContent();

        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }

    /**
     *
     * @param id
     * @return
     */
    public QuotationDetail loadAQuotationDetail(long id) {
        return null;
    }
}
