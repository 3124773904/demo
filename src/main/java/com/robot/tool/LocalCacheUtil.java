package com.robot.tool;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linwei
 * @title LocalCacheUtil
 * @date 2021年11月17日 下午6:23
 */
public class LocalCacheUtil {
    // 缓存map
    private static Map<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();
    // 缓存有效期map
    private static Map<String, Long> expireTimeMap = new ConcurrentHashMap<String, Long>();


    /**
     * 获取指定的value，如果key不存在或者已过期，则返回null
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        if (!cacheMap.containsKey(key)) {
            return null;
        }
        if (expireTimeMap.containsKey(key)) {
            if (expireTimeMap.get(key) < System.currentTimeMillis()) { // 缓存失效，已过期
                return null;
            }
        }
        return cacheMap.get(key);
    }

    /**
     * @param key
     * @param
     * @return
     */
    public static <T> T getT(String key) {
        Object obj = get(key);
        return obj == null ? null : (T) obj;
    }

    /**
     * 设置value（不过期）
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        cacheMap.put(key, value);
    }

    /**
     * 设置value
     *
     * @param key
     * @param value
     * @param second 过期时间（秒）
     */
    public static void set(final String key, Object value, int second) {
        final long expireTime = System.currentTimeMillis() + second * 1000;
        cacheMap.put(key, value);
        expireTimeMap.put(key, expireTime);
        if (cacheMap.size() > 2) { // 清除过期数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 此处若使用foreach进行循环遍历，删除过期数据，会抛出java.util.ConcurrentModificationException异常
                    Iterator<Map.Entry<String, Object>> iterator = cacheMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> entry = iterator.next();
                        if (expireTimeMap.containsKey(entry.getKey())) {
                            long expireTime = expireTimeMap.get(key);
                            if (System.currentTimeMillis() > expireTime) {
                                iterator.remove();
                                expireTimeMap.remove(entry.getKey());
                            }
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * key是否存在
     *
     * @param key
     * @return
     */
    public static boolean isExist(String key) {
        return cacheMap.containsKey(key);
    }

    /**
     * 清除缓存
     *
     * @param key
     */
    public static void remove(String key) {
        cacheMap.remove(key);
    }
}
