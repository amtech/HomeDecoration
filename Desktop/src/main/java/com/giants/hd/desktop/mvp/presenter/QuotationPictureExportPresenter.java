package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.utils.entity.WorkFlowArea;
import com.giants3.hd.utils.entity.WorkFlowEvent;

import java.io.File;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface QuotationPictureExportPresenter extends IPresenter {


    void searchProduct(String key);

    void exportPicture(File directory);

    void removeRows(int[] rows);
}
