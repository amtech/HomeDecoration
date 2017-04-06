package com.giants3.hd.domain.repository;

import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.MaterialClass;
import rx.Observable;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public interface MaterialRepository {

    /**
     * 更新材料分类
     * @param materialClass
     * @return
     */
    Observable<RemoteData<MaterialClass>> updateMaterialClass(MaterialClass materialClass);

    Observable<RemoteData<Void>> deleteMaterialClass(long materialClassId);
}
