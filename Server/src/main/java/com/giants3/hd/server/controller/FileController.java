package com.giants3.hd.server.controller;

import com.giants3.hd.server.repository.AppVersionRepository;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.AppVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    @Value("${quotationfilepath}")
    private String quotationfilepath;

    @Value("${appfilepath}")
    private String appFilePath;

    @Autowired
    AppVersionRepository repository;

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
    @RequestMapping(value="/uploadProduct", method=RequestMethod.POST)
    public @ResponseBody
    RemoteData<Void> handleProductUpload(@RequestParam("name") String name,@RequestParam("doesOverride") boolean doesOverride,
                                                 @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {


            int indexOfDot=name.indexOf(".");

            String filePath=FileUtils.getProductPicturePath(productFilePath,name.substring(0,indexOfDot),"",name.substring(indexOfDot+1));


            if (new File(filePath).exists() && !doesOverride)
            {
                return wrapMessageData(filePath+" 已经存在 ，并且要求不覆盖。");
            }

         return   handleFileUpload(file,filePath);

        } else {
            return wrapError("You failed to upload " + name + " because the file was empty.");
        }
    }


    @RequestMapping(value="/uploadMaterial", method=RequestMethod.POST)
    public @ResponseBody
    RemoteData<Void> handleMaterialUpload(@RequestParam("name") String name,@RequestParam("doesOverride") boolean doesOverride,
                                         @RequestParam("file") MultipartFile file){

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if (!file.isEmpty()) {


            String newPath = FileUtils.getMaterialPicturePath(materialFilePath, name);
            if (new File(newPath).exists() && !doesOverride)
            {
                return wrapMessageData(newPath+" 已经存在 ，并且要求不覆盖。");
            }


            return   handleFileUpload(file,newPath);

        } else {
            return wrapError("You failed to upload " + name + " because the file was empty.");
        }
    }


    /**
     * 处理上传的文件  存放到文件中。
     *
     * @param file
     * @param newFilePath
     * @return
     */
    private  RemoteData<Void> handleFileUpload( MultipartFile file,String newFilePath)
    {


        byte[] bytes  ;
        try {

            File newFile=new File(newFilePath);
            File parentFile=newFile.getParentFile();
            if(!parentFile.exists())
                parentFile.mkdirs();
            bytes = file.getBytes();
            FileOutputStream out = new FileOutputStream(newFile);
            BufferedOutputStream stream =
                    new BufferedOutputStream(out);

            stream.write(bytes);
            stream.close();
            out.close();
            return  wrapMessageData("成功上传文件到" + newFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return  wrapError("You failed to upload " + newFilePath + " => " + e.getMessage());
        }


    }



    @RequestMapping(value="/download/product/{name}/{pVersion}", method=RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable  String name,@PathVariable  String pVersion,@RequestParam(value = "type",defaultValue = "jpg") String  type) {


        FileSystemResource resource= new FileSystemResource( FileUtils.getProductPicturePath(productFilePath,name,pVersion,type));
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





        FileSystemResource resource= new FileSystemResource( FileUtils.getMaterialPicturePath(materialFilePath, code, mClass, type));
        //  FileSystemResource resource= new FileSystemResource("F://materials//lintw.jpg");

        return resource;
    }


    /**
     *
     *
     */
    @RequestMapping(value="/download/quotation", method=RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getQuotationFile(@RequestParam(value = "name",defaultValue = "") String name, @RequestParam(value = "appendix",defaultValue = "xls") String appendix) {





        FileSystemResource resource= new FileSystemResource(  FileUtils.getQuotationFile(quotationfilepath, name,appendix));
        //  FileSystemResource resource= new FileSystemResource("F://materials//lintw.jpg");

        return resource;
    }



    @RequestMapping(value="/download/app", method=RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getAppFile( )  {


        AppVersion appVersion=repository.findFirstByAppNameLikeOrderByVersionCodeDescUpdateTimeDesc("%%");


            FileSystemResource resource= new FileSystemResource(  appFilePath+appVersion.appName);
            return resource;






    }


    /**
     * Upload multiple file using Spring Controller
     */
    @Deprecated
    @RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
    public @ResponseBody
    String uploadMultipleFileHandler(@RequestParam("name") String[] names,
                                     @RequestParam("file") MultipartFile[] files) {

        if (files.length != names.length)
            return "Mandatory information missing";

        String message = "";
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String name = names[i];
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
//
//                 info("Server File Location="
//                        + serverFile.getAbsolutePath());

                message = message + "You successfully uploaded file=" + name
                        + "<br />";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        }
        return message;
    }

}
