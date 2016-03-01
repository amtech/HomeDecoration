package com.giants3.hd.server.app;


import com.giants3.hd.appdata.AProduct;
import com.giants3.hd.appdata.AQuotation;
import com.giants3.hd.appdata.AQuotationDetail;
import com.giants3.hd.server.controller.BaseController;
import com.giants3.hd.server.parser.DataParser;
import com.giants3.hd.server.parser.RemoteDataParser;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.QuotationService;
import com.giants3.hd.server.utils.BackDataHelper;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 报价
 */
@Controller
@RequestMapping("/app/quotation")
public class AQuotationController extends BaseController {


    @Autowired
    private QuotationService quotationService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QuotationXKItemRepository quotationXKItemRepository;
    @Autowired
    private QuotationItemRepository quotationItemRepository;
    @Autowired
    private QuotationLogRepository quotationLogRepository;
    @Autowired
    private OperationLogRepository operationLogRepository;
    @Autowired
    private QuotationDeleteRepository quotationDeleteRepository;

    @Value("${deleteQuotationFilePath}")
    private String deleteQuotationFilePath;

    @Autowired
    @Qualifier("quotationParser")
    private DataParser<Quotation, AQuotation> dataParser;

    @Autowired
    @Qualifier("quotationDetailParser")
    private DataParser<QuotationDetail, AQuotationDetail> detailParser;

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<AQuotation> search(@RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue, @RequestParam(value = "salesmanId", required = false, defaultValue = "-1") long salesmanId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {

        RemoteData<Quotation> result = quotationService.search(searchValue, salesmanId, pageIndex, pageSize);


        RemoteData<AQuotation> aResult = RemoteDataParser.parse(result, dataParser);


        return aResult;

    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<AQuotationDetail> detail(@RequestParam(value = "id", required = false, defaultValue = "") long id) {


        QuotationDetail detail = quotationService.loadAQuotationDetail(id);


        if (detail == null)
            return wrapError("未找到id=" + id + "的报价记录数据");


        RemoteData<QuotationDetail> result = wrapData(detail);

        RemoteData<AQuotationDetail> aResult = RemoteDataParser.parse(result, detailParser);

        return aResult;


    }


    /**
     * 产品完整信息的保存
     *
     * @param quotationDetail 报价单全部信息
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<QuotationDetail> saveQuotationDetail(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody QuotationDetail quotationDetail) {


//        Quotation newQuotation = quotationDetail.quotation;
//
//        if (!quotationRepository.exists(newQuotation.id)) {
//            //检查单号唯一性
//            if (quotationRepository.findFirstByqNumberEquals(newQuotation.qNumber) != null) {
//
//                return wrapError("报价单号:" + newQuotation.qNumber
//                        + "已经存在,请更换");
//            }
//
//            newQuotation.id = -1;
//        } else {
//
//
//            Quotation oldQuotation = quotationRepository.findOne(newQuotation.id);
//
//            if (!oldQuotation.qNumber.equals(newQuotation.qNumber)) {
//
//                //检查单号唯一性
//                if (quotationRepository.findFirstByqNumberEquals(newQuotation.qNumber) != null) {
//
//                    return wrapError("报价单号:" + newQuotation.qNumber
//                            + "已经存在,请更换");
//                }
//
//            }
//
//
//            newQuotation.id = oldQuotation.id;
//        }
//
//
//
//
//        //空白行过滤
//        if(quotationDetail.quotation.quotationTypeId== Quotation.QUOTATION_TYPE_NORMAL)
//        {
//            List<QuotationItem> valuableItem=new ArrayList<>();
//            for(QuotationItem item:quotationDetail.items)
//            {
//                if(!item.isEmpty())
//                valuableItem.add(item);
//            }
//            quotationDetail.items.clear();
//            quotationDetail.items.addAll(valuableItem);
//
//
//        }else
//        {
//            List<QuotationXKItem> valuableItem=new ArrayList<>();
//            for(QuotationXKItem item:quotationDetail.XKItems)
//            {
//                if(!item.isEmpty())
//                    valuableItem.add(item);
//            }
//            quotationDetail.XKItems.clear();
//            quotationDetail.XKItems.addAll(valuableItem);
//        }
//
//        //保存基本数据
//        Quotation savedQuotation = quotationRepository.save(newQuotation);
//
//
//        //更新修改记录
//        updateQuotationLog(savedQuotation, user);
//
//        long newId = savedQuotation.id;
//        {
//
//        //find items that removed
//        List<QuotationItem> oldQuotationItems = quotationItemRepository.findByQuotationIdEqualsOrderByIIndex(newId);
//
//
//        List<QuotationItem> removeItems = new ArrayList<>();
//        for (QuotationItem oldQuotationItem : oldQuotationItems) {
//            boolean find = false;
//            for (QuotationItem item : quotationDetail.items) {
//
//                if (oldQuotationItem.id == item.id) {
//                    find = true;
//                    break;
//                }
//            }
//
//            if (!find) {
//                removeItems.add(oldQuotationItem);
//            }
//        }
//        //移除被删除的记录
//        quotationItemRepository.deleteInBatch(removeItems);
//        removeItems.clear();
//            int newIndex=0;
//        for (QuotationItem item : quotationDetail.items) {
//
//            item.quotationId = newId;
//            item.iIndex=newIndex++;
//
//        }
//            quotationItemRepository.save( quotationDetail.items);
//    }
//
//        {
//            //find items that removed
//            List<QuotationXKItem> oldQuotationXKItems = quotationXKItemRepository.findByQuotationIdEqualsOrderByIIndex(newId);
//
//
//            List<QuotationXKItem> removeXKItems = new ArrayList<>();
//            for (QuotationXKItem oldQuotationXKItem : oldQuotationXKItems) {
//                boolean find = false;
//                for (QuotationXKItem item : quotationDetail.XKItems) {
//
//                    if (oldQuotationXKItem.id == item.id) {
//                        find = true;
//                        break;
//                    }
//                }
//
//                if (!find) {
//                    removeXKItems.add(oldQuotationXKItem);
//                }
//            }
//            //移除被删除的记录
//            quotationXKItemRepository.deleteInBatch(removeXKItems);
//            removeXKItems.clear();
//
//
//            int newIndex=0;
//            for (QuotationXKItem item : quotationDetail.XKItems) {
//
//                item.quotationId = newId;
//                item.iIndex=newIndex++;
//            }
//
//            quotationXKItemRepository.save( quotationDetail.XKItems);
//        }
//
//        return detail(newId);

        return null;
    }


}
