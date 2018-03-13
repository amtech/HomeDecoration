package com.giants3.hd.server.service;

import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.entity.User;
import com.giants3.hd.server.repository.QuotationRepository;
import com.giants3.hd.server.repository.QuoteAuthRepository;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Quotation;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.utils.StringUtils;
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
public class QuotationService extends AbstractService   {


    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    QuoteAuthRepository quoteAuthRepository;




    public RemoteData<Quotation> search(User loginUser, String searchValue, long salesmanId
            , int pageIndex, int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<Quotation> pageValue;



        if (salesmanId < 0) {

         List<QuoteAuth> quoteAuths= quoteAuthRepository.findByUser_IdEquals(loginUser.id);
            if(quoteAuths.size()>0&&!StringUtils.isEmpty(quoteAuths.get(0).relatedSales))
            {

               String[] ids=quoteAuths.get(0).relatedSales.split(",|，");
                long[] longIds=new long[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    longIds[i]=Long.valueOf(ids[i]);
                }

                 pageValue = quotationRepository.findByCustomerNameLikeAndSalesmanIdInOrQNumberLikeAndSalesmanIdInOrderByQDateDesc("%" + searchValue + "%",longIds, "%" + searchValue + "%", longIds, pageable);



            }else {
                //表示所有人
                pageValue = quotationRepository.findByCustomerNameLikeOrQNumberLikeOrderByQDateDesc("%" + searchValue + "%", "%" + searchValue + "%", pageable);
            }
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
