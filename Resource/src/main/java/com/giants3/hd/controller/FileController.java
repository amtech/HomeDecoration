package com.giants3.hd.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * 文件上传控制类
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {

    @Value("${filepath}")
    private String productFilePath;

    @Value("${deletedProductPhotoFilePath}")
    private String deleteProductFilePath;
    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;

    //附件文件夹
    @Value("${attachfilepath}")
    private String attachfilepath;
    //订单唛头文件夹
    @Value("${maitoufilepath}")
    private String maitoufilepath;

    @Value("${materialfilepath}")
    private String materialFilePath;
    @Value("${quotationfilepath}")
    private String quotationfilepath;
    @Value("${workflowfilepath}")
    private String workflowfilepath;

    @Value("${appfilepath}")
    private String appFilePath;


    @RequestMapping(value = "/download/product/{name}/{pVersion}/{updateTime}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable String name, @PathVariable String pVersion, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        FileSystemResource resource = new FileSystemResource(FileUtils.getProductPicturePath(productFilePath, name, pVersion, type));


        //  FileSystemResource resource= new FileSystemResource("F://products//lintw.jpg");

        return resource;
    }


    @RequestMapping(value = "/download/product/thumbnail/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductThumbnailFile(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {

        if (StringUtils.isEmpty(name)) return null;

        FileSystemResource resource = new FileSystemResource(productFilePath + "thumbnail" + FileUtils.SEPARATOR + name.replace(FileUtils.URL_PATH_SEPARATOR, FileUtils.SEPARATOR) + "." + type);

        return resource;
    }


    /**
     * 读取erp数据库的图片
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/download/erpProduct/id_no/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Resource getErpPhotoFile(@PathVariable String name, @RequestParam(value = "updateTime", defaultValue = "") String updateTime) {


        if (StringUtils.isEmpty(name)) return null;

        File file = null;
        //  file = erpPhotoService.getErpPhotoResource(name, updateTime);
        if (file == null || !file.exists()) return null;


        return new FileSystemResource(file);

    }

    @RequestMapping(value = "/download/product/{name}/{updateTime}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        return getProductFile(name, "", type);


    }

    @RequestMapping(value = "/download/workflows/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getWorkFlowPicture(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {

        return new FileSystemResource(workflowfilepath + name + "." + type);


    }


    /**
     * 读取临时文件
     *
     * @param name
     * @param type
     * @return
     */

    @RequestMapping(value = "/download/temp/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getTempFile(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        FileSystemResource resource = new FileSystemResource(tempFilePath + name + "." + type);
        //  FileSystemResource resource= new FileSystemResource("F://products//lintw.jpg");

        return resource;
    }

    /**
     * 读取附件文件
     *
     * @param name
     * @param type
     * @return
     */

    @RequestMapping(value = "/download/attach/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getAttachFile(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        FileSystemResource resource = new FileSystemResource(attachfilepath + name.replace(AttachFileUtils.FILE_SEPARATOR, File.pathSeparator) + "." + type);
        //  FileSystemResource resource= new FileSystemResource("F://products//lintw.jpg");

        return resource;
    }

    /**
     * @param code   材料编码
     * @param mClass 材料类别   类别即为文件归类的文件夹
     * @param type   图片类型  默认jpg
     * @return
     */
    @RequestMapping(value = "/download/material/{code}/{updateTime}", method = RequestMethod.GET)
    @ResponseBody

    public FileSystemResource getMaterialFile(@PathVariable String code, @RequestParam(value = "mClass", defaultValue = "") String mClass, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        FileSystemResource resource = new FileSystemResource(FileUtils.getMaterialPicturePath(materialFilePath, code, mClass, type));
        //  FileSystemResource resource= new FileSystemResource("F://materials//lintw.jpg");

        return resource;
    }


    /**
     *
     *
     */
    @RequestMapping(value = "/download/quotation", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getQuotationFile(@RequestParam(value = "name", defaultValue = "") String name, @RequestParam(value = "appendix", defaultValue = "xls") String appendix) {


        FileSystemResource resource = new FileSystemResource(FileUtils.getQuotationFile(quotationfilepath, name, appendix));
        //  FileSystemResource resource= new FileSystemResource("F://materials//lintw.jpg");

        return resource;
    }


    @RequestMapping(value = "/download/androidApp/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getAndroidApp(@PathVariable String name) {


        FileSystemResource resource = new FileSystemResource(appFilePath + name + ".apk");
        return resource;


    }


    /**
     * 嘜頭文件上传
     *
     * @return
     */
    @RequestMapping(value = "/download/order/maitou", method = RequestMethod.GET)
    public
    @ResponseBody
    FileSystemResource downloadMaitouUpload(@RequestParam("os_no") String os_no) {


        return new FileSystemResource(maitoufilepath + os_no + ".xls");


    }

}
