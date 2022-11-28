package com.robot.tool;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by houxianghua on 2015/9/25.
 */
public class ClassUtil {
    /**
     * 根据 属性名 获取属性值
     *
     * @param o         实体对象
     * @param fieldName 属性名
     */
    public static Object getFieldValueByName(Object o, String fieldName) {
        Object value = null;
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            value = method.invoke(o, new Object[]{});
        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据 属性名 设置属性值
     *
     * @param o          实体对象
     * @param fieldName  属性名
     * @param fieldValue 属性值
     */
    public static Object setFieldValueByName(Object o, String fieldName, Object fieldValue) {
        Field f = null;
        try {
            f = o.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(o, fieldValue);
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }

        return o;
    }


    /**
     * 根据 属性名 获取属性类型
     *
     * @param clazz     实体对象
     * @param fieldName 属性名
     */
    public static Class<?> getFieldTypeByName(Class clazz, String fieldName) throws NoSuchFieldException {
        Field[] fields = clazz.getDeclaredFields();
        fields[0].getType();
        for (int i = 0; i < fields.length; i++) {
//            System.out.println(fields[i].getName());
            if (fields[i].getName().equals(fieldName)) {
//                System.out.println(fields[i].getType().toString());
                return fields[i].getType();
            }
        }

        if(clazz.getSuperclass() == null){
            throw new NoSuchFieldException("Object o exclusive field name = "+fieldName);
        }else{
            return getFieldTypeByName(clazz.getSuperclass(), fieldName);
        }

    }

    /**
     * 将值为null的obj参数转换成空的String值
     *
     * @param obj         参数
     */
    public static String objToStringNotNull(Object obj) {
        if(obj==null){
            return "";
        }
        return String.valueOf(obj);
    }

//    /**
//     * 获取类实例的父类的属性值
//     * @param map
//     *            类实例的属性值Map
//     * @param clazz
//     *            类名
//     * @return 类名.属性名=属性类型
//     */
//    private static Map<String, Class> getParentClassFields(Map<String, Class> map, Class clazz) {
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            map.put(clazz.getName() + "." + field.getName(), field.getType());
//        }
//        if (clazz.getSuperclass() == null) {
//            return map;
//        }
//        getParentClassFields(map, clazz.getSuperclass());
//        return map;
//    }
}
