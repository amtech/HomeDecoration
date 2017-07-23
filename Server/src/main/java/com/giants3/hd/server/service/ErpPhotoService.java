package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.ErpPhotoRepository;
import com.giants3.hd.server.repository.ErpWorkRepository;
import com.giants3.hd.server.utils.FileUtils;
import de.greenrobot.common.io.IoUtils;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by davidleen29 on 2017/7/5.
 */
@Service
public class ErpPhotoService extends  AbstractService {


    EntityManager manager;
    ErpPhotoRepository erpPhotoRepository;
    @Value("${erpPhotolFilePath}")
    private String erpPhotolFilePath;
    @Override
    public void destroy() throws Exception {


        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        erpPhotoRepository = new ErpPhotoRepository(manager);

    }

    public File getErpPhotoResource(String id_no,String updateTime)
    {



        //查找对应的文件存在 直接返回文件

        String fileName= (id_no+"XXXXX"+updateTime).replace("->","-");


        String filePath=erpPhotolFilePath+fileName;

        final File file = new File(filePath);
        if(file.exists())
            return file;


        //否则 从数据库读取文件 存放在指定文件， 然后返回该文件

        return   readErpPhotToFile(id_no,filePath);





    }


    /**
     * 该方法同步进行
     * @param id_no
     * @return
     */
    private  synchronized  File  readErpPhotToFile(String id_no,String filePath)
    {
        File file=new File(filePath);
        if(file.exists())return file;

        FileUtils.makeDirs(filePath);



        byte[] bytes=erpPhotoRepository.findProductPhotoByIdNo(id_no);


        FileOutputStream fileOutputStream= null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            IoUtils.safeClose(fileOutputStream);
        }





         return new File(filePath);



    }
}
