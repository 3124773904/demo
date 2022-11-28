package com.robot.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:GlobalUtil
 * @Description:自定义的通用工具类
 * @Author:linwei
 * @Date:2021/5/8 上午8:36
 * @Version:1.0
 */
public class GlobalUtil {
    public static boolean listIsBlack(List list) {
        if (!listIsNull(list)) {
            return list.size() <= 0;
        }
        return true;
    }

    public static boolean listIsNull(List list) {
        return list == null;
    }

    public static boolean IntegerIsNull(Integer i) {
        return i == null;
    }

    public static boolean objectIsNull(Object o) {
        return o == null;
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}
