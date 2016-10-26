package com.giants3.hd.server.controller;

import com.giants3.hd.appdata.AUser;
import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.noEntity.FileInfo;
import com.giants3.hd.server.parser.DataParser;
import com.giants3.hd.server.parser.RemoteDataParser;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.UserService;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.RemoteData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * 应用更新
* Created by davidleen29 on 20151015
*/
@Controller

@RequestMapping("/update")
public class UpdateController extends  BaseController{

    public static final String ANDOIR_APK = ".apk";
    @Value("${appfilepath}")
    private String appFilePath;

    /**
     * 提供移动端  省略很多数据
     *
     * @param appVersion
     * @return
     */
    @RequestMapping(value = "/getNewAndroidApk", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    RemoteData<FileInfo> getNewAndroidApk(@RequestParam(value = "appVersion") int appVersion) {

        File f=new File(appFilePath);
        if(f.isDirectory())
        {

            File[] files=f.listFiles();

            for(File aFile : files) {

                final String name = aFile.getName();
                if (name.endsWith(ANDOIR_APK));
                {

                    int index=name.indexOf("_");
                    if(index==-1 ) continue;
                    int lastIndex=name.indexOf(ANDOIR_APK);

                    String versionString=name.substring(index+1,lastIndex);
                    try {
                        int version = Integer.valueOf(versionString);
                        if(version>appVersion) {

                            FileInfo fileInfo=new FileInfo();
                            fileInfo.url="api/file/download/androidApp/"+name;
                            fileInfo.name=name;
                            fileInfo.length=aFile.length();
                            return wrapData(fileInfo);
                        }

                    }catch ( Throwable t)
                    {}


                }
            }
            }

        return wrapData();
    }

}