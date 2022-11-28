package com.robot.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linwei
 * @title ConvertUtil
 * @date 2021年11月18日 下午1:24
 */
public class ConvertUtil {
    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public static Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }
}
