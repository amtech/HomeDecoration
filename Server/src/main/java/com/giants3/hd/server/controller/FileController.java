package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.ProductRepository;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.convert.WritingConverter;
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
    private String filePath;



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


        FileSystemResource resource= new FileSystemResource( FileUtils.getProductPicturePath(filePath,name,"",type));
        //  FileSystemResource resource= new FileSystemResource("E://photos//lintw.jpg");

        return resource;
    }



}
