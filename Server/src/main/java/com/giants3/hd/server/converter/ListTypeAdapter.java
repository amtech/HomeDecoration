package com.giants3.hd.server.converter;

import com.giants3.hd.utils.RemoteData;
import com.google.gson.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2015-05-19.
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


//    @Override
//    public JsonElement serialize(RemoteData<?> src, Type typeOfSrc, JsonSerializationContext context) {
//
//
//        JsonObject map = new JsonObject();
//        Type childGenericType = null;
//        if (typeOfSrc instanceof ParameterizedType) {
//            Class<?> rawTypeOfSrc = Types.getRawType(typeOfSrc);
//            childGenericType = Types.getMapKeyAndValueTypes(typeOfSrc, rawTypeOfSrc)[1];
//        }
//
//        context.serialize()
//
//        for (Map.Entry entry : (Set<Map.Entry>) src.entrySet()) {
//            Object value = entry.getValue();
//
//            JsonElement valueElement;
//            if (value == null) {
//                valueElement = JsonNull.createJsonNull();
//            } else {
//                Type childType = (childGenericType == null)
//                        ? value.getClass() : childGenericType;
//                valueElement = context.serialize(value, childType);
//            }
//            map.add(String.valueOf(entry.getKey()), valueElement);
//        }
//        return map;
//
//
//
//        return null;
//    }
}
