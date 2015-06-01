package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.PClass;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductDetail;
import com.giants3.hd.utils.entity.ProductPack;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

/**
* 产品类别
*/
@Controller
@RequestMapping("/productClass")
public class ProductClassController extends BaseController{


    @Autowired
    private JpaRepository<PClass,Long> productClassRepository;






    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<PClass> list( )   {

        return  wrapData(productClassRepository.findAll());
    }




}
