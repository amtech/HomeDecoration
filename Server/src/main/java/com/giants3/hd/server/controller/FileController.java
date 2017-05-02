package com.giants3.hd.server.controller;

import com.giants3.hd.utils.entity.AppVersion;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.server.repository.AppVersionRepository;
import com.giants3.hd.server.service.MaterialService;
import com.giants3.hd.server.service.ProductService;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import de.greenrobot.common.io.IoUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Calendar;

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

    @Value("${materialfilepath}")
    private String materialFilePath;
    @Value("${quotationfilepath}")
    private String quotationfilepath;

    @Value("${appfilepath}")
    private String appFilePath;

    @Autowired
    AppVersionRepository repository;
    @Autowired
    ProductService productService;

    @Autowired
    MaterialService materialService;


    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/testFile", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "uploadfile";
    }

    @RequestMapping(value = "/uploadProduct", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> handleProductUpload(@RequestParam("name") String name, @RequestParam("doesOverride") boolean doesOverride,
                                         @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            int indexOfDot = name.indexOf(".");
            String productName = name.substring(0, indexOfDot);
            String suffix = name.substring(indexOfDot + 1);
            String filePath = FileUtils.getProductPicturePath(productFilePath, productName, "", suffix);


            File newFile = new File(filePath);
            if (newFile.exists() && !doesOverride) {
                return wrapMessageData(filePath + " 已经存在 ，并且要求不覆盖。");
            }

            backUpProductPhoto(productName, suffix);


            RemoteData<Void> remoteData = handleFileUpload(file, filePath);
            if (remoteData.isSuccess()) {
                //同步产品图片
                productService.updateProductPhoto(productName);

            }


            return remoteData;
        } else {
            return wrapError("You failed to upload " + name + " because the file was empty.");
        }
    }


    private void backUpProductPhoto(String productName, String suffix) {
        String filePath = FileUtils.getProductPicturePath(productFilePath, productName, "", suffix);
        File newFile = new File(filePath);
        //防止图片被误删  备份图片
        if (newFile.exists()) {
            String coveredPath = FileUtils.getProductPicturePath(deleteProductFilePath, productName + "_back_" + DateFormats.FORMAT_YYYY_MM_DD_HH_MM_SS_LOG.format(Calendar.getInstance().getTime()), "", suffix);
            File coverFile = new File(coveredPath);
            //构建文件路径
            File parentFile = coverFile.getParentFile();
            if (!parentFile.exists()) {
                boolean makeDir = parentFile.mkdirs();
            }
            //复制文件
            try {
                de.greenrobot.common.io.FileUtils.copyFile(newFile, coverFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @RequestMapping(value = "/uploadMaterial", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> handleMaterialUpload(@RequestParam("name") String name, @RequestParam("doesOverride") boolean doesOverride,
                                          @RequestParam("file") MultipartFile file) {


        if (!file.isEmpty()) {


            String newPath = FileUtils.getMaterialPicturePath(materialFilePath, name);
            if (new File(newPath).exists() && !doesOverride) {
                return wrapMessageData(newPath + " 已经存在 ，并且要求不覆盖。");
            }


            return handleFileUpload(file, newPath);

        } else {
            return wrapError("You failed to upload " + name + " because the file was empty.");
        }
    }

    /**
     * 上传一条材料图片 并同步数据库缩略图， url
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/uploadMaterialPicture", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Void> uploadMaterialPicture(@RequestParam("materialId") long materialId,
                                           @RequestBody byte[] data) {

        Material material = materialService.findMaterial(materialId);
        if (material == null) {
            return wrapError("当前材料不存在");
        }

        String newPath = FileUtils.getMaterialPicturePath(materialFilePath, material.code, material.classId);

        File file = new File(newPath);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long lastUpdatePhotoTime = FileUtils.getFileLastUpdateTime(new File(newPath));
        String newUrl = FileUtils.getMaterialPictureURL(material.code, material.classId, lastUpdatePhotoTime);
        material.url = newUrl;

        material.lastPhotoUpdateTime = lastUpdatePhotoTime;
        materialService.save(material);
        //RemoteData<Void> result= handleFileUpload(new File(""), newPath);
        return wrapData();

    }

    /**
     * 处理上传的文件  存放到文件中。
     *
     * @param file
     * @param newFilePath
     * @return
     */
    private RemoteData<Void> handleFileUpload(MultipartFile file, String newFilePath) {
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            File newFile = new File(newFilePath);
            File parentFile = newFile.getParentFile();
            if (!parentFile.exists())
                parentFile.mkdirs();
            fileOutputStream = new FileOutputStream(newFile);
            inputStream = file.getInputStream();
            IOUtils.copy(inputStream, fileOutputStream);
            fileOutputStream.flush();
            return wrapMessageData("成功上传文件到" + newFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return wrapError("You failed to upload " + newFilePath + " => " + e.getMessage());
        } finally {

            IoUtils.safeClose(inputStream);
            IoUtils.safeClose(fileOutputStream);

        }


    }


    @RequestMapping(value = "/download/product/{name}/{pVersion}/{updateTime}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable String name, @PathVariable String pVersion, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        FileSystemResource resource = new FileSystemResource(FileUtils.getProductPicturePath(productFilePath, name, pVersion, type));


        //  FileSystemResource resource= new FileSystemResource("F://products//lintw.jpg");

        return resource;
    }


    @RequestMapping(value = "/download/product/thumbnail/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductThumbnailFile(@PathVariable String name , @RequestParam(value = "type", defaultValue = "jpg") String type) {

        if(StringUtils.isEmpty(name)) return null;

        FileSystemResource resource = new FileSystemResource(productFilePath+ "thumbnail"+FileUtils.SEPARATOR+name.replace(FileUtils.URL_PATH_SEPARATOR,FileUtils.SEPARATOR)+"."+type);

        return resource;
    }

    @RequestMapping(value = "/download/product/{name}/{updateTime}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProductFile(@PathVariable String name, @RequestParam(value = "type", defaultValue = "jpg") String type) {


        return getProductFile(name, "", type);


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


        FileSystemResource resource = new FileSystemResource(attachfilepath +  name.replace(AttachFileUtils.FILE_SEPARATOR,File.pathSeparator) + "." + type);
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


    @RequestMapping(value = "/download/app", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getAppFile() {


        AppVersion appVersion = repository.findFirstByAppNameLikeOrderByVersionCodeDescUpdateTimeDesc("%%");


        FileSystemResource resource = new FileSystemResource(appFilePath + appVersion.appName);
        return resource;


    }

    @RequestMapping(value = "/download/androidApp/{name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getAndroidApp(@PathVariable String name) {




        FileSystemResource resource = new FileSystemResource(appFilePath + name+".apk");
        return resource;


    }





    /**
     * Upload multiple file using Spring Controller
     */
    @Deprecated
    @RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
    public
    @ResponseBody
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


    /**
     * 临时图片上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadTemp", method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<String> handleMaterialUpload(@RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {


            String tempFileName = "TEMP_" + Calendar.getInstance().getTimeInMillis();
            String newPath = tempFilePath + tempFileName + ".JPG";

            RemoteData<Void> result = handleFileUpload(file, newPath);

            String url=FileUtils.getDownloadTempUrl(tempFileName);
            if (result.isSuccess())
                return wrapData(url);
            return wrapError("上传失败");
        } else {
            return wrapError("You failed to upload " + file.getName() + " because the file was empty.");
        }
    }
}
