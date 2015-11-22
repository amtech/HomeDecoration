package com.giants3.hd.utils;

/**
 * 数组方法的功能类
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;



/**
 * 数组相关
 * 算法类
 *
 * @author david
 */
public class ArrayUtils {
    private static final String TAG = ArrayUtils.class.getName();

    /**
     * 封装 对列表数据进行排序
     *
     * @param <T>
     * @param <T>
     * @param datas
     * @param comparator
     */

    @SuppressWarnings("unchecked")
    public static <T> void SortList(List<T> datas, Comparator<T> comparator) {

        if (datas == null || comparator == null)
            return;

        int size = datas.size();
        T[] array = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = datas.get(i);
        }

        Arrays.sort(array, comparator);

        datas.clear();
        for (int i = 0; i < size; i++) {
            datas.add(array[i]);
        }




    }

    /**
     * 封装 由数组转换成 列表
     *
     * @param <T>
     * @param array
     */

    public static <T> List<T> changeArrayToList(T[] array) {

        List<T> list = new ArrayList<T>();

        if (array != null && array.length > 0) {
            for (T data : array) {
                list.add(data);
            }
        }

        return list;

    }

    /**
     * 封装 由列表转换成数组
     *
     * @param <T>
     * @param list
     */

    public static <T> T[] changeArrayToList(List<T> list) {

        int size = list.size();
        T[] result = (T[]) new Object[size];


        for (int i = 0; i < size; i++) {
            result[i] = list.get(i);
        }


        return result;

    }

    public static boolean isNotEmpty(List array) {
        return array!=null&&array.size()>0;
    }

    public static boolean isEmpty(List  list
    ) {
        return list==null||list.size()==0;
    }


    /**
     * 将字符数组转换成带分隔符的字符串
     * @param fields
     * @param divider
     * @return
     */
    public static String toDividerString(String[] fields, String divider) {


        int length=fields==null?0:fields.length;

        if(length==0) return "";
        String s="";
        for(int i=0;i<length-1;i++)
        {
            s+=fields[i]+divider;


        }
        s+=fields[length-1];

        return s;


    }


    /**
     * 返回 给定值在数组中的位置
     * @param array
     * @param value
     * @param <T>
     * @return
     */
    public static <T> int indexOnArray(T[] array,T value)
    {

        int length = array.length;

        for (int i = 0; i < length; i++) {
            if(value.equals(array[i]))
                return i;
        }
        return -1;
    }
}

