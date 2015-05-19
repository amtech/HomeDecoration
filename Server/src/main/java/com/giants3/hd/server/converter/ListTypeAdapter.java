package com.giants3.hd.server.converter;

import com.giants3.hd.utils.RemoteData;
import com.google.gson.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 *   �б�ķ���ת����GSON�ִ��� �����б��Զ��� ������   ֱ��ʹ��ʵ�����͡�
 */

public class ListTypeAdapter implements JsonSerializer<List<?>> {
    @Override
    public JsonElement serialize(List<?> src, Type typeOfSrc, JsonSerializationContext context) {


        JsonArray map = new JsonArray();


        for(Object o:src)
        {
            map.add(context.serialize(o, o.getClass()));
        }



        return map;
    }


}
