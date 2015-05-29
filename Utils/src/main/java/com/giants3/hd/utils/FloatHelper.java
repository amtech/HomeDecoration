package com.giants3.hd.utils;

import java.math.BigDecimal;

/**
 * 浮点数处理类。
 */
public class FloatHelper {

    public static final int DEFAULT_SCALE_SIZE=2;
    public static float scale(float floatValue)
    {


      return   scale(floatValue,DEFAULT_SCALE_SIZE);
    }

    public static float scale(float floatValue,int scaleSize)
    {

        return new BigDecimal(floatValue).setScale(scaleSize,BigDecimal.ROUND_HALF_UP).floatValue();

    }

}
