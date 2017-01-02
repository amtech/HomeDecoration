package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.GlobalData;
import com.giants3.hd.server.repository.GlobalDataRepository;
import com.giants3.hd.utils.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 *
 * Created by davidleen29 on 2016/12/21.
 */
@Service
public class GlobalDataService extends AbstractService {


    @Autowired
    private GlobalDataRepository globalDataRepository;


    /**
     * 获取最新的全局配置数据表
     *
     * @return
     */
    public GlobalData findCurrentGlobalData() {
        if (globalDataRepository.count() == 0)
            return null;
      Pageable pageable= constructPageSpecification(0,1,new Sort(Sort.Direction.DESC,"updateTime"));
        return globalDataRepository.findAll(pageable).getContent().get(0);
    }

    public GlobalData find(long id) {
        return globalDataRepository.findOne(id);
    }

    public GlobalData save(GlobalData globalData) {

        globalData.id=0;
        globalData.updateTime= DateFormats.FORMAT_YYYY_MM_DD_HH_MM_SS.format(Calendar.getInstance().getTime());


        globalDataRepository.save(globalData);
        return globalData;
    }
}
