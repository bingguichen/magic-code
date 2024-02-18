package com.bin.csp.demo.json;

import java.util.List;
import java.util.Map;

/**
 * JSON抽象
 *
 * @author BinChan
 */
public abstract class AbstractJson {

    private static JsonFactory defaultJsonFactory = new JacksonFactory();

    /**
     * 当对象级的 datePattern 为 null 时使用 defaultDatePattern
     * 暂定 defaultDatePattern 值为 null，即 jackson、fastjson
     * 默认使用自己的 date 转换策略
     */
    private static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * Json 继承类优先使用对象级的属性 datePattern, 然后才是全局性的 defaultDatePattern
     */
    protected String datePattern = null;

    static void setDefaultJsonFactory(JsonFactory defaultJsonFactory) {
        if (defaultJsonFactory == null) {
            throw new IllegalArgumentException("defaultJsonFactory can not be null.");
        }
        AbstractJson.defaultJsonFactory = defaultJsonFactory;
    }

    static void setDefaultDatePattern(String defaultDatePattern) {
        if (defaultDatePattern == null || defaultDatePattern.isEmpty()) {
            throw new IllegalArgumentException("defaultDatePattern can not be blank.");
        }
        AbstractJson.defaultDatePattern = defaultDatePattern;
    }

    public AbstractJson setDatePattern(String datePattern) {
        if (datePattern == null || datePattern.isEmpty()) {
            throw new IllegalArgumentException("datePattern can not be blank.");
        }
        this.datePattern = datePattern;
        return this;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public String getDefaultDatePattern() {
        return defaultDatePattern;
    }

    public static AbstractJson getJson() {
        return defaultJsonFactory.getJson();
    }

    /**
     * 对象转json字符串
     *
     * @param object
     * @return
     */
    public abstract String toJson(Object object);

    /**
     * 将json字符串转为指定类型
     *
     * @param jsonString
     * @param type
     * @param <T>
     * @return
     */
    public abstract <T> T parse(String jsonString, Class<T> type);

    public abstract <T> T parse(byte[] jsonString, Class<T> type);

    /**
     * 将json字符串转为指定类型的数组
     *
     * @param jsonString
     * @param type
     * @param <T>
     * @return
     */
    public abstract <T> List<T> parseArray(String jsonString, Class<T> type);

    /**
     * 转换为指定类型的map
     *
     * @param jsonString
     * @param keyType
     * @param valueType
     * @param <K>
     * @param <V>
     * @return
     */
    public abstract <K, V> Map<K, V> parseMap(String jsonString, Class<K> keyType, Class<V> valueType);

    /**
     * 转换为指定类型的map
     *
     * @param jsonByte
     * @param keyType
     * @param valueType
     * @param <K>
     * @param <V>
     * @return Map<K, V>
     */
    public abstract <K, V> Map<K, V> parseMap(byte[] jsonByte, Class<K> keyType, Class<V> valueType);

    /**
     * 泛型转换
     *
     * @param jsonStr
     * @param keyType
     * @param valType
     * @param <K,V>
     * @return
     */
    public abstract <K, V> List<Map<K, V>> parseMapArray(String jsonStr, Class<K> keyType, Class<V> valType);
}