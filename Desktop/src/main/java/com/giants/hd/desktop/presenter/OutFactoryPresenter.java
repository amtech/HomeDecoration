package com.giants.hd.desktop.presenter;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.OutFactory;
import com.giants3.hd.utils.exception.HdException;

import java.util.List;

/**
 * Created by davidleen29 on 2016/7/12.
 */
public interface OutFactoryPresenter   extends  Presenter {


    void saveData(List<OutFactory> datas)  ;
}
