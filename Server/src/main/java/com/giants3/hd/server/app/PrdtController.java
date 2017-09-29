package com.giants3.hd.server.app;


import com.giants3.hd.appdata.AProduct;
import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.controller.BaseController;
import com.giants3.hd.server.parser.DataParser;
import com.giants3.hd.server.parser.RemoteDataParser;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.ProductService;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 产品信息
 */
@Controller
@RequestMapping("/prdt")
public class PrdtController extends BaseController {


    @Autowired
    ProductService productService;


    @Autowired
    @Qualifier("productParser")
    private DataParser<Product, AProduct> dataParser;




    /**
     * 提供移动端接口  查询
     *
     * @param name
     * @param pageIndex
     * @return
     * @Param pageSize
     */
    @RequestMapping(value = "/search", method = {RequestMethod.GET})
    public
    @ResponseBody
    RemoteData<AProduct> appSearch(@RequestParam(value = "name", required = false, defaultValue = "") String name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    )   {


        RemoteData<Product> productRemoteData = productService.searchProductList(name, pageIndex, pageSize);
        RemoteData<AProduct> result = RemoteDataParser.parse(productRemoteData, dataParser);


        return result;


    }




}
