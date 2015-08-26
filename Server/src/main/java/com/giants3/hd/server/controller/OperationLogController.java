package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.OperationLogRepository;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.OperationLog;
import com.giants3.hd.utils.entity.Pack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* 材料信息控制类。
*/
 @Controller
@RequestMapping("/operationLog")
public class OperationLogController extends BaseController {

    @Autowired
    private OperationLogRepository operationLogRepository;


    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<OperationLog> search(@RequestParam("className") String className,@RequestParam("recordId") long id)   {

       return wrapData(  operationLogRepository.findByTableNameEqualsAndRecordIdEqualsOrderByTimeDesc(className,id));

    }

}
