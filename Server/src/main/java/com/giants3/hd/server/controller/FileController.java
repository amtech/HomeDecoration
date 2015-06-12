package com.giants3.hd.server.controller;

import com.giants3.hd.server.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 文件上传控制类
 */
@Controller
@RequestMapping("/file")
public class FileController  extends BaseController{



    @Value("${filepath}")
    private String productFilePath;

    @Value("${materialfilepath}")
    private String materialFilePath;

    @RequestMapping(value="/upload", method= RequestMethod.GET)
    public @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/testFile",method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "uploadfile";
    }
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }



    @RequestMapping(value="/download/product/{name}", method=RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable  String name,@RequestParam(value = "type",defaultValue = "jpg") String  type) {


        FileSystemResource resource= new FileSystemResource( FileUtils.getProductPicturePath(productFilePath,name,"",type));
        //  FileSystemResource resource= new FileSystemResource("F://products//lintw.jpg");

        return resource;
    }

    /**
     *
     * @param code  材料编码
     * @param mClass 材料类别   类别即为文件归类的文件夹
     * @param type   图片类型  默认jpg
     * @return
     */
    @RequestMapping(value="/download/material/{code}", method=RequestMethod.GET)
    @ResponseBody

    public FileSystemResource getMaterialFile(@PathVariable  String code,@RequestParam(value = "mClass",defaultValue = "") String mClass,@RequestParam(value = "type",defaultValue = "jpg") String  type) {





        FileSystemResource resource= new FileSystemResource( FileUtils.getMaterialPicturePath(materialFilePath, code,mClass, type));
        //  FileSystemResource resource= new FileSystemResource("F://materials//lintw.jpg");

        return resource;
    }

}
