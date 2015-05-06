package com.giants3.hd.server.controller;


import com.giants3.hd.server.entity.Prdt;
import com.giants3.hd.server.entity.Prdt1;
import com.giants3.hd.server.entity.PrdtResult;
import com.giants3.hd.server.repository.Prdt1Repository;
import com.giants3.hd.server.repository.PrdtRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* 产品信息
*/
@Controller
@RequestMapping("/api/prdts")
public class Prdt1Controller {

    @Autowired
    private Prdt1Repository prdt1Repository;
    @Autowired
     private PrdtRepository prdtRepository;

    @RequestMapping( method = RequestMethod.GET)
    public
    @ResponseBody
    String listPrdtJson(ModelMap model)   {

        JsonArray array=new JsonArray();

        for (Prdt1 prdt1 : prdt1Repository.findAll()) {
            Gson gson=new Gson( );
            array.add(  gson.toJsonTree(prdt1));
        }
        return array.toString();
    }

    //   /api/prdts/2.209e%2B007     这个 。 请求中会出现错误    实际中  prd_no 得到的参数是2
    @RequestMapping(value = "/{prd_no}", method = RequestMethod.GET)
    public
    @ResponseBody
    String listPrdtJson(@PathVariable String prd_no)   {


        JsonArray array=new JsonArray();

        for (Prdt1 prdt1 : prdt1Repository.findByPrd_no(prd_no)) {
            Gson gson=new Gson( );
            array.add(  gson.toJsonTree(prdt1));
        }
        return array.toString();
    }




    //   /api/prdts/equal?prd_no=2.209e%2B007
    @RequestMapping( value = "/equal", method = RequestMethod.GET,params="prd_no")
    public
    @ResponseBody
    byte[] listPrdtJsonByRequestParam(@RequestParam("prd_no") String prd_no) throws UnsupportedEncodingException {



        return getFullData( prdt1Repository.findByPrd_no(prd_no));

    }
    //   /api/prdts/like?prd_no=2.209e%2B007
    @RequestMapping( value = "/like", method = RequestMethod.GET,params="prd_no")
    public
    @ResponseBody
    byte[] listPrdtJsonByRequestParamLike(@RequestParam("prd_no") String prd_no) throws UnsupportedEncodingException {

        List<Prdt1> prdt1s=prdt1Repository.findByPrd_noLike("%" + prd_no + "%" );




        return getFullData(prdt1s);
        //return  "林太伟"  ;
    }

    private byte[] getFullData(List<Prdt1> prdt1s) throws UnsupportedEncodingException {
        JsonArray array=new JsonArray();
        Gson gson= new Gson();
        int length=prdt1s.size();
        length=Math.min(length,50);
        for (int i=0;i<length;i++ ) {

            Prdt1 prdt1=prdt1s.get(i);
            List<Prdt> units=prdtRepository.findByPrd_no(prdt1.getPrd_no());

            Prdt prdt=units.size()>0?units.get(0):new Prdt();

            PrdtResult result=new PrdtResult(prdt,prdt1);

            array.add(  gson.toJsonTree(result));
        }
        return array.toString().getBytes("UTF-8");
    }

}
