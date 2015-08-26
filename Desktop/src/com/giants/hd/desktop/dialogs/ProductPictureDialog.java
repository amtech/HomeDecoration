package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import java.awt.*;
import java.io.File;

/**
 * Created by davidleen29 on 2015/8/24.
 */
public class ProductPictureDialog extends UploadPictureDialog {
    @Inject
    ApiManager apiManager;
    public ProductPictureDialog(Window window) {
        super(window);
        setTitle("产品图片管理");
    }


    @Override
    protected RemoteData<Void> uploadPicture(File file, boolean doesOverride) throws HdException {

        return apiManager.uploadProductPicture(file,doesOverride);

    }

    @Override
    protected RemoteData<Void> syncPicture() throws HdException {
        return apiManager.syncProductPhoto();
    }
}
