package com.giants3.hd.server.service;

import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.server.app.service.AppQuotationService;
import com.giants3.hd.server.repository.AppQuotationRepository;
//import com.giants3.report.jasper.quotation.AppQuotationReport;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.report.PictureUrl;
import com.giants3.report.jasper.quotation.AppQuotationReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by davidleen29 on 2018/2/17.
 */
@Service
public class ReportService extends  AbstractService {


    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;
    @Autowired
    private AppQuotationService quotationService;


    /**
     * 打印报价单pdf报表， 返回报表所在路径
     * @param quotationId
     * @return
     */
    public String  printAppQuotationToPdf(long quotationId)
    {

        QuotationDetail quotationDetail=quotationService.getDetail(quotationId);

        String destFilePath=tempFilePath+"report/temp"+ Calendar.getInstance().getTimeInMillis()+".pdf";
          String path = null;
        try {
             path = new ClassPathResource("report/jasper/appQuotation.jrxml").getFile().getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }


        FileUtils.makeDirs(destFilePath);


        for(QuotationItem item:quotationDetail.items)
        {
            item.thumbnail= PictureUrl.completeUrl(item.thumbnail);
        }

       new AppQuotationReport(quotationDetail,path,destFilePath).report();


        return destFilePath;







    }


}
