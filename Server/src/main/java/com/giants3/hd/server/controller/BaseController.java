package com.giants3.hd.server.controller;

import com.giants3.hd.utils.RemoteData;

import com.giants3.hd.utils.entity.ProductDetail;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有控制类的基类，提供共有方法。
 */
public class BaseController {

    protected static final int NUMBER_OF_PERSONS_PER_PAGE = 20;



    /**
     * 封装正常的返回结果。
     * @param data
     * @param <T>
     * @return
     */
    public <T>        RemoteData<T> wrapData(T data)
    {

            List<T> datas=new ArrayList<>();
        datas.add(data);

        return wrapData(0,datas.size(),1,datas.size(),datas);


    }

    /**
     * 封装正常的返回结果。
     * @param datas
     * @param <T>
     * @return
     */
        public <T>        RemoteData<T> wrapData(List<T> datas)
        {



            return wrapData(0,datas==null?0:datas.size(),1,datas==null?0:datas.size(),datas);


        }


    /**
     * 封装正常的返回结果。
     * @param <T>
     * @return
     */
    public <T>        RemoteData<T> wrapData( )
    {



        return wrapData(null);


    }
    /**
     * 封装正常的返回结果。
     * @param datas
     * @param <T>
     * @return
     */

    public <T>        RemoteData<T> wrapData(int pageIndex,int pageSize,int pageCount,int totalCount,List<T> datas)
    {

        RemoteData<T> remoteData=new RemoteData<T>();
        remoteData.pageIndex=pageIndex;
        remoteData.pageSize=pageSize;
        remoteData.pageCount=pageCount;
        remoteData.totalCount=totalCount;

        remoteData.datas                =datas;
        remoteData.code=RemoteData.CODE_SUCCESS;

        remoteData.message="operation sucessed";
        return remoteData;


    }

    /**
     * Returns a new object which specifies the the wanted result page.
     * @param pageIndex The index of the wanted result page
     * @return
     */
    protected Pageable constructPageSpecification(int pageIndex, int pageSize,Sort sort) {
        Pageable pageSpecification = new PageRequest(pageIndex,  pageSize, sort);
        return pageSpecification;
    }


    /**
     * Returns a new object which specifies the the wanted result page.
     * @param pageIndex The index of the wanted result page
     * @return
     */
    protected
    Pageable constructPageSpecification(int pageIndex) {
        return constructPageSpecification(pageIndex, NUMBER_OF_PERSONS_PER_PAGE);
    }


    /**
     * 构造分页数据
     * @param pageIndex
     * @param pageSize
     * @return
     */
    protected Pageable constructPageSpecification(int pageIndex,int pageSize) {
        return constructPageSpecification(pageIndex, pageSize,sortByParam(Sort.Direction.ASC,"id"));
    }


    /**
     * 构造排序方法
     * @param direction
     * @param column
     * @return
     */
    protected Sort sortByParam(Sort.Direction direction,String column) {
        return new Sort(direction, column);
    }


    /**
     * 封装错误信息类。
     * @param message
     * @param <T>
     * @return
     */

    protected  <T> RemoteData<T> wrapError(String message) {


        RemoteData<T> remoteData=new RemoteData<T>();



        remoteData.code=RemoteData.CODE_FAIL;

        remoteData.message=message;
        return remoteData;

    }


}
