package com.giants3.hd.server.service;

import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.ErpWorkRepository;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Zhilingdan;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by davidleen29 on 2017/3/9.
 */
@Service
public class ErpWorkService extends AbstractService implements InitializingBean, DisposableBean {

    EntityManager manager;


    ErpWorkRepository erpWorkRepository;

    private Date today;

    @Override
    public void destroy() throws Exception {


        manager.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        erpWorkRepository = new ErpWorkRepository(manager);
        today = Calendar.getInstance().getTime();
    }


    public RemoteData<Zhilingdan> searchZhilingdan(String osName, String startDate, String endDate) {


        final List<Zhilingdan> datas = erpWorkRepository.searchZhilingdan(osName, startDate, endDate);

        for (Zhilingdan zhilingdan : datas) {


            zhilingdan.isCaigouOverDue = isCaigouOverDue(zhilingdan);
            zhilingdan.isJinhuoOverDue = isJinhuoOverDue(zhilingdan);

            //日期截断处理
            if(!StringUtils.isEmpty(zhilingdan.caigou_dd))
            zhilingdan.caigou_dd= zhilingdan.caigou_dd.substring(0,10);
            if(!StringUtils.isEmpty(zhilingdan.jinhuo_dd))
            zhilingdan.jinhuo_dd= zhilingdan.jinhuo_dd.substring(0,10);
            if(!StringUtils.isEmpty(zhilingdan.mo_dd))
            zhilingdan.mo_dd= zhilingdan.mo_dd.substring(0,10);


        }
        return wrapData(datas);
    }


    /**
     * 采购超期判断
     *
     * @param zhilingdan
     * @return
     */
    private boolean isCaigouOverDue(Zhilingdan zhilingdan) {

        if (StringUtils.isEmpty(zhilingdan.mo_no)) return false;
        if (zhilingdan.need_dd == 0) return false;
        Date mmDate = null;
        try {
            mmDate = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.mo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (mmDate == null) return false;
        Date caigouDd = today;
        try {
            caigouDd = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.caigou_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }





        int day = de.greenrobot.common.DateUtils.getDayDifference(mmDate.getTime(), caigouDd.getTime());

        return day > zhilingdan.need_dd;


    }

    /**
     * 进货超期判断
     *
     * @param zhilingdan
     * @return
     */
    private boolean isJinhuoOverDue(Zhilingdan zhilingdan) {
        if (StringUtils.isEmpty(zhilingdan.mo_no)) return false;
        if (zhilingdan.need_days == 0) return false;


        Date mmDate = null;
        try {
            mmDate = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.mo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (mmDate == null) return false;

        Date jinhuoDd = today;
        try {
            jinhuoDd = DateFormats.FORMAT_YYYY_MM_DD.parse(zhilingdan.jinhuo_dd);
        } catch (Throwable e) {
            e.printStackTrace();
        }


        int day = de.greenrobot.common.DateUtils.getDayDifference(mmDate.getTime(), jinhuoDd.getTime());


        //进货单超期 要大于
        return day > zhilingdan.need_days+zhilingdan.need_dd;

    }
}
