package com.giants3.hd.utils;

import com.giants3.hd.utils.entity.Unit;

/**
 * 字符串的功能类。
 */

public class StringUtils {



    public static boolean  isEmpty(String s)
    {
        return s==null||s.trim().length()==0;
    }

    public static final String row_separator="\n";
    public static final String spec_separator="*";

    public static final String spec_separator_pattern="\\*";


    /**
     * 厘米字符串 转换成 英寸字符串
     *格式如下 999*999*999\n 888*888*88
     *
     * @return
     */

    public static String convertCmStringToInchString(String cmString)
    {

        String[] rows = cmString.split("["+row_separator+"]+");

        String inchString="";

        for (int i = 0; i <rows.length; i++) {

            String[] specs=rows[i].split(spec_separator_pattern);

            int length = specs.length;
            for (int j = 0; j < length; j++) {
                String spec=specs[j];
                try {
                    float cmValue = Float.valueOf(spec.trim());
                    float inchValue=UnitUtils.cmToInch(cmValue);
                    inchString+=inchValue;

                }catch (Throwable t)
                {

                    inchString+=spec;
                }
                if(j<length-1)
                inchString+=spec_separator;
            }





            inchString+=row_separator;


        }


        return inchString;

    }


    /**
     * 厘米字符串 转换成 英寸字符串
     *格式如下 999*999*999\n 888*888*888
     *
     * @return
     */

    public static String convertInchStringToCmString(String inchString)
    {

        String[] rows = inchString.split("["+row_separator+"]+");

        String cmString="";

        for (int i = 0; i <rows.length; i++) {

            String[] specs=rows[i].split(spec_separator_pattern);

            int length = specs.length;
            for (int j = 0; j < length; j++) {
                String spec=specs[j];
                try {
                    float inchValue = Float.valueOf(spec.trim());
                    float cmValue= UnitUtils.inchToCm(inchValue);
                    cmString+=cmValue;

                }catch (Throwable t)
                {

                    cmString+=spec;
                }
                if(j<length-1)
                    cmString+=spec_separator;
            }





            cmString+=row_separator;


        }


        return  cmString;


    }


    /**
     * 将数字用* 串联起来
     * @param value
     * @return
     */
    public static String combineNumberValue(Number... value)
    {

        String result="";

        int length = value.length;
        for (int i = 0; i < length; i++) {

            result+=value[i];
            if(i<length-1) {

                result+= spec_separator;
            }
        }

        return result;

    }


    /**
     * 解析包装串
     * @param packageString
     * @return
     */
    public static float[] decouplePackageString(String packageString)
    {

        int firstIndex=packageString.indexOf(spec_separator);
        int lastIndex=packageString.lastIndexOf(spec_separator);

        float[] result=new float[3];

        try {
            result[0] = Float.valueOf(packageString.substring(0, firstIndex));
            result[1] = Float.valueOf(packageString.substring(firstIndex + 1, lastIndex));
            result[2] = Float.valueOf(packageString.substring(lastIndex + 1));
        }catch (Throwable t)
        {
            t.printStackTrace();
        }
        return result;

    }






    /**
     * 解析产品规格串   23*66*99 69*89*12 95*78*12
     * @param specString
     * @return
     */
    public static float[][] decoupleSpecString(String specString)
    {

        String[] rows = specString.trim().split("[" + row_separator + "]+");

        int length = rows.length;
        float[][] result=new float[length][];

        for (int i = 0; i < length; i++) {

           result[i]= decouplePackageString(rows[i]);
        }


        return result;




    }



    public static int index(String[] array,String s)
    {

        int length = array.length;
        for (int i = 0; i < length; i++) {

            if(array[i].equals(s))
                return i;

        }
        return -1;
    }

}
