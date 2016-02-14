package com.giants3.hd.server.app;


import com.giants3.hd.server.controller.BaseController;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.BackDataHelper;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import org.springframework.beans.factory.annotation.Autowired;
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
    private QuotationRepository quotationRepository;
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Quotation> listPrdtJson() {


        return wrapData(quotationRepository.findAll());
    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Quotation> search(@RequestParam(value = "searchValue", required = false, defaultValue = "") String searchValue, @RequestParam(value = "salesmanId", required = false, defaultValue = "-1") long salesmanId
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

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


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> detail(@RequestParam(value = "id", required = false, defaultValue = "") long id) {


        QuotationDetail detail = loadDetailById(id);


        if (detail == null)
            return wrapError("未找到id=" + id + "的报价记录数据");


        return wrapData(detail);
    }


    /**
     * 读取详情数据
     *
     * @param id
     * @return
     */
    private QuotationDetail loadDetailById(long id) {


        Quotation quotation = quotationRepository.findOne(id);

        if (quotation == null)
            return null;

        QuotationDetail detail = new QuotationDetail();
        detail.quotation = quotation;
        detail.quotationLog = quotationLogRepository.findFirstByQuotationIdEquals(id);
        detail.items = quotationItemRepository.findByQuotationIdEqualsOrderByIIndex(id);

        detail.XKItems = quotationXKItemRepository.findByQuotationIdEqualsOrderByIIndex(id);


        return detail;

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


        Quotation newQuotation = quotationDetail.quotation;

        if (!quotationRepository.exists(newQuotation.id)) {
            //检查单号唯一性
            if (quotationRepository.findFirstByqNumberEquals(newQuotation.qNumber) != null) {

                return wrapError("报价单号:" + newQuotation.qNumber
                        + "已经存在,请更换");
            }

            newQuotation.id = -1;
        } else {


            Quotation oldQuotation = quotationRepository.findOne(newQuotation.id);

            if (!oldQuotation.qNumber.equals(newQuotation.qNumber)) {

                //检查单号唯一性
                if (quotationRepository.findFirstByqNumberEquals(newQuotation.qNumber) != null) {

                    return wrapError("报价单号:" + newQuotation.qNumber
                            + "已经存在,请更换");
                }

            }


            newQuotation.id = oldQuotation.id;
        }




        //空白行过滤
        if(quotationDetail.quotation.quotationTypeId== Quotation.QUOTATION_TYPE_NORMAL)
        {
            List<QuotationItem> valuableItem=new ArrayList<>();
            for(QuotationItem item:quotationDetail.items)
            {
                if(!item.isEmpty())
                valuableItem.add(item);
            }
            quotationDetail.items.clear();
            quotationDetail.items.addAll(valuableItem);


        }else
        {
            List<QuotationXKItem> valuableItem=new ArrayList<>();
            for(QuotationXKItem item:quotationDetail.XKItems)
            {
                if(!item.isEmpty())
                    valuableItem.add(item);
            }
            quotationDetail.XKItems.clear();
            quotationDetail.XKItems.addAll(valuableItem);
        }

        //保存基本数据
        Quotation savedQuotation = quotationRepository.save(newQuotation);


        //更新修改记录
        updateQuotationLog(savedQuotation, user);

        long newId = savedQuotation.id;
        {

        //find items that removed
        List<QuotationItem> oldQuotationItems = quotationItemRepository.findByQuotationIdEqualsOrderByIIndex(newId);


        List<QuotationItem> removeItems = new ArrayList<>();
        for (QuotationItem oldQuotationItem : oldQuotationItems) {
            boolean find = false;
            for (QuotationItem item : quotationDetail.items) {

                if (oldQuotationItem.id == item.id) {
                    find = true;
                    break;
                }
            }

            if (!find) {
                removeItems.add(oldQuotationItem);
            }
        }
        //移除被删除的记录
        quotationItemRepository.deleteInBatch(removeItems);
        removeItems.clear();
            int newIndex=0;
        for (QuotationItem item : quotationDetail.items) {

            item.quotationId = newId;
            item.iIndex=newIndex++;

        }
            quotationItemRepository.save( quotationDetail.items);
    }

        {
            //find items that removed
            List<QuotationXKItem> oldQuotationXKItems = quotationXKItemRepository.findByQuotationIdEqualsOrderByIIndex(newId);


            List<QuotationXKItem> removeXKItems = new ArrayList<>();
            for (QuotationXKItem oldQuotationXKItem : oldQuotationXKItems) {
                boolean find = false;
                for (QuotationXKItem item : quotationDetail.XKItems) {

                    if (oldQuotationXKItem.id == item.id) {
                        find = true;
                        break;
                    }
                }

                if (!find) {
                    removeXKItems.add(oldQuotationXKItem);
                }
            }
            //移除被删除的记录
            quotationXKItemRepository.deleteInBatch(removeXKItems);
            removeXKItems.clear();


            int newIndex=0;
            for (QuotationXKItem item : quotationDetail.XKItems) {

                item.quotationId = newId;
                item.iIndex=newIndex++;
            }

            quotationXKItemRepository.save( quotationDetail.XKItems);
        }

        return detail(newId);


    }


    /**
     * 记录报价修改信息
     */
    private void updateQuotationLog(Quotation quotation, User user) {

        //记录数据当前修改着相关信息
        QuotationLog quotationLog = quotationLogRepository.findFirstByQuotationIdEquals(quotation.id);
        if (quotationLog == null) {
            quotationLog = new QuotationLog();
            quotationLog.quotationId = quotation.id;

        }

        quotationLog.quotationNumber = quotation.qNumber;
        quotationLog.setUpdator(user);
        quotationLogRepository.save(quotationLog);


        //增加历史操作记录
        operationLogRepository.save(OperationLog.createForQuotationModify(quotation, user));


    }

    /**
     * 删除产品信息
     *
     * @param quotationId
     * @return
     */
    @RequestMapping(value = "/logicDelete", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> logicDelete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam("id") long quotationId) {

        //检查是否有关联数据


        QuotationDetail detail = loadDetailById(quotationId);
        if (detail == null) {
            return wrapError(" 该报价单不存在，请刷新数据");
        }


        Quotation quotation = quotationRepository.findOne(quotationId);

        quotationRepository.delete(quotation.id);

        int affectedRow = quotationItemRepository.deleteByQuotationIdEquals(quotationId);
        Logger.getLogger("TEST").info("quotationItemRepository delete affectedRow:" + affectedRow);
        affectedRow = quotationXKItemRepository.deleteByQuotationIdEquals(quotationId);
        Logger.getLogger("TEST").info("quotationXKItemRepository delete affectedRow:" + affectedRow);


        //增加历史操作记录
        operationLogRepository.save(OperationLog.createForQuotationDelete(quotation, user));


        //保存数据备份
        QuotationDelete quotationDelete = new QuotationDelete();
        quotationDelete.setQuotationAndUser(quotation, user);
        quotationDelete = quotationDeleteRepository.save(quotationDelete);
        BackDataHelper.backQuotation(detail, deleteQuotationFilePath, quotationDelete);


        return wrapData();

    }


    @RequestMapping(value = "/searchDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDelete> listDelete(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageCount) {


        Pageable pageable = constructPageSpecification(pageIndex, pageCount);
        String likeWord = "%" + keyword.trim() + "%";
        Page<QuotationDelete> pageValue = quotationDeleteRepository.findByQNumberLikeOrSaleManLikeOrCustomerLikeOrderByTimeDesc(likeWord, likeWord, likeWord, pageable);

        List<QuotationDelete> products = pageValue.getContent();

        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }

    @RequestMapping(value = "/detailDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<QuotationDetail> detailDelete(@RequestParam(value = "id") long quotationDeleteId) {


        QuotationDelete quotationDelete = quotationDeleteRepository.findOne(quotationDeleteId);

        if (quotationDelete == null) {
            return wrapError("该报价单已经不在删除列表中");
        }


        QuotationDetail detail = null;

        detail = BackDataHelper.getQuotationDetail(deleteQuotationFilePath, quotationDelete);

        if (detail == null)
            return wrapError("备份文件读取失败");

        return wrapData(detail);

    }

    @RequestMapping(value = "/resumeDelete", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    RemoteData<QuotationDetail> resumeDelete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "deleteQuotationId") long deleteQuotationId) {


        QuotationDelete quotationDelete = quotationDeleteRepository.findOne(deleteQuotationId);

        if (quotationDelete == null) {
            return wrapError("该报价单已经不在删除列表中");
        }


        QuotationDetail detail = BackDataHelper.getQuotationDetail(deleteQuotationFilePath, quotationDelete);

        if (detail == null)
            return wrapError("备份文件读取失败");


        //新增记录
        //移除id
        detail.quotation.id = 0;

        RemoteData<QuotationDetail> result = saveQuotationDetail(user, detail);

        if (result.isSuccess()) {


            QuotationDetail newQuotationDetail = result.datas.get(0);
            long newQuotationId = newQuotationDetail.quotation.id;
            //更新修改记录中所有旧productId 至新id；
            if (detail.quotationLog != null) {
                detail.quotationLog.quotationId = newQuotationId;
                quotationLogRepository.save(detail.quotationLog);
                newQuotationDetail.quotationLog = detail.quotationLog;
            }

            //更新修改记录中所有旧productId 至新id；
            operationLogRepository.updateProductId(quotationDelete.quotationId, Quotation.class.getSimpleName(), newQuotationId);

            //添加恢复消息记录。
            OperationLog operationLog = OperationLog.createForQuotationResume(newQuotationDetail.quotation, user);
            operationLogRepository.save(operationLog);


            //移除删除记录
            quotationDeleteRepository.delete(deleteQuotationId);

            //移除备份的文件
            BackDataHelper.deleteQuotation(deleteQuotationFilePath, quotationDelete);

        }


        return wrapData(detail);

    }


    @RequestMapping(value = "/unVerify", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<QuotationDetail> unVerify(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestParam(value = "quotationId") long quotationId) {


        Quotation quotation = quotationRepository.findOne(quotationId);
        if (quotation == null) {
            return wrapError("id：" + quotationId + "，对应的报价单不存在");
        }

        if (!quotation.isVerified) {

            return wrapError("id" + quotationId + "，对应的报价单，状态已经为非审核，请刷新数据");
        }

        quotation.isVerified = false;
        quotationRepository.save(quotation);


        //记录修改
        updateQuotationLog(quotation, user);


        //添加取消审核记录。
        OperationLog operationLog = OperationLog.createForQuotationUnVerify(quotation, user);
        operationLogRepository.save(operationLog);


        return detail(quotationId);

    }

    /**
     * 产品完整信息的保存
     *
     * @param quotationDetail 报价单全部信息
     * @return
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<QuotationDetail> verifyQuotationDetail(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user, @RequestBody QuotationDetail quotationDetail) {


        Quotation newQuotation = quotationDetail.quotation;

        if (!quotationRepository.exists(newQuotation.id)) {


            return wrapError("报价单号:" + newQuotation.qNumber
                    + "不存在,不能审核，请刷新数据");

        } else {



            //设定为已经审核
            quotationDetail.quotation.isVerified = true;


            RemoteData<QuotationDetail> result = saveQuotationDetail(user, quotationDetail);

            //记录修改记录

            //记录修改
            updateQuotationLog(newQuotation, user);
            //添加审核记录。
            OperationLog operationLog = OperationLog.createForQuotationVerify(newQuotation, user);
            operationLogRepository.save(operationLog);


            return result;

        }


    }
}
