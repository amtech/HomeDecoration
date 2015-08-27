package com.giants.hd.desktop.utils;

import java.util.HashMap;

/**
 * Created by davidleen29 on 2015/8/18.
 */
public class AccumulateMap extends HashMap<Object , Integer> {


    @Override
    public Integer get(Object key) {
        Integer integer= super.get(key);
        return integer==null?0:integer.intValue();
    }


    public void accumulate(Object key)
    {
     int value=   get(key);

        put(key,value+1);

    }
}
