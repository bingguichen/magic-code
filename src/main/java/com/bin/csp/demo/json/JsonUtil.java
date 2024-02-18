package com.bin.csp.demo.json;


import java.util.List;
import java.util.Map;

/**
 * JSON转换工具
 *
 * @author BinChan
 */
public class JsonUtil {
    public static String toJson(Object object) {
        if (object instanceof String) {
            return String.valueOf(object);
        }
        return AbstractJson.getJson().toJson(object);
    }

    private static String replaceNoneRow(String str) {
        return str.replaceAll("\u00a0", "");
    }

    public static <T> T parse(String jsonString, Class<T> type) {

        return AbstractJson.getJson().parse(replaceNoneRow(jsonString), type);
    }

    public static<T> T parse(byte[] jsonString, Class<T> type) {
        return AbstractJson.getJson().parse(jsonString, type);
    }
    public static<T> List<T> parseArray(String jsonString, Class<T> type){
        return AbstractJson.getJson().parseArray(replaceNoneRow(jsonString), type);
    }

    public static <K, V> Map<K, V> parseMap(String jsonString, Class<K> keyType, Class<V> valueType) {
        return AbstractJson.getJson().parseMap(replaceNoneRow(jsonString), keyType, valueType);
    }

    public static <K, V> Map<K, V> parseMap(byte[] jsonByte, Class<K> keyType, Class<V> valueType) {
        return AbstractJson.getJson().parseMap(jsonByte, keyType, valueType);
    }

    public static<K, V> List<Map<K, V>> parseMapArray(String jsonStr, Class<K> keyType, Class<V> valType) {
        return AbstractJson.getJson().parseMapArray(replaceNoneRow(jsonStr), keyType, valType);
    }

    public static boolean isJson(String str) {
        return isJsonObj(str) || isJsonArray(str);
    }

    public static boolean isJsonObj(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        return str.charAt(0) == '{' && str.charAt(str.length() - 1) == '}';
    }

    public static boolean isJsonArray(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        return str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']';
    }
}
