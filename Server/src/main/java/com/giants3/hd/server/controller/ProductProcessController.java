package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.ProductProcessRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.entity.ProductProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 工序
 */
@Controller
@RequestMapping("/process")
public class ProductProcessController extends BaseController {


    @Autowired
    private ProductProcessRepository processRepository;





    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ProductProcess> list() {


        return wrapData(processRepository.findAll());
    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<ProductProcess> search(@RequestParam(value = "name", required = false, defaultValue = "") String name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<ProductProcess> pageValue = processRepository.findByNameLikeOrCodeLike("%" + name + "%", "%" + name + "%", pageable);

        List<ProductProcess> products = pageValue.getContent();
        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }



    @RequestMapping(value = "/saveList",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<ProductProcess> saveList(@RequestBody List<ProductProcess> productProcesses )   {

        for(ProductProcess process:productProcesses)
        {




            ProductProcess oldData=processRepository.findOne(process.id);
            if(oldData==null)
            {
                process.id=-1;


                //如果是空数据  略过添加
                if(process.isEmpty())
                {
                    continue;
                }



            }else
            {




                process.id=oldData.id;

            }

            processRepository.save(process);


        }



        return list();
    }



}
