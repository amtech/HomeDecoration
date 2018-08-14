package com.giants3.hd.noEntity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/** RemoteData 泛型应用情况下，抓取实际泛型类型类型
 *
 * Created by davidleen29 on 2018/8/13.
 */
public class RemoteDateParameterizedType implements ParameterizedType {

    private Class<?>[] tClasses;

    /**
     *
     * @param tClasses 泛型参数对应的实际类型列表   比如泛型<T,K,J>    T,K,J 实际类型class
     */
    public RemoteDateParameterizedType(Class<?>... tClasses) {
        super();


        this.tClasses = tClasses;
    }


    @Override
    public Type[] getActualTypeArguments() {
        return tClasses;
    }

    @Override
    public Type getRawType() {
        return RemoteData.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
