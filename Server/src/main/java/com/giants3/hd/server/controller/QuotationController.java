package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 报价
 */
@Controller
@RequestMapping("/quotation")
public class QuotationController extends BaseController {


    @Autowired
    private QuotationRepository quotationRepository;





    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Quotation> listPrdtJson() {


        return wrapData(quotationRepository.findAll());
    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Quotation> listPrdtJson(@RequestParam(value = "proName", required = false, defaultValue = "") String prd_name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<Quotation> pageValue = quotationRepository.findByCustomerNameLike("%" + prd_name + "%", pageable);

        List<Quotation> products = pageValue.getContent();




        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }



}
