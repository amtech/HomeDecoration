package com.giants.hd.desktop.api;

import com.giants3.hd.utils.entity.BufferData;

/**
 * Created by davidleen29 on 2015/8/8.
 */
public class CacheManager {
    private static CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    private CacheManager() {
    }


    public  BufferData bufferData;
}
