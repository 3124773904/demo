package com.robot.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BeanListFomart
 * @Author lin
 * @create 2022/9/19 3:30 PM
 */
public class BeanListFormat {
    public static <T> List<T> listFormat(List list, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        list.forEach(item -> {
            result.add(JSON.parseObject(JSON.toJSONString(item, SerializerFeature.WriteNullStringAsEmpty), clazz, Feature.InitStringFieldAsEmpty));
        });
        return result;
    }
}
