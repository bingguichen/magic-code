package com.bin.csp.demo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jackson封装JSON工具
 *
 * @author BinChan
 */
public class Jackson extends AbstractJson {
    // Jackson 生成 json 的默认行为是生成 null value，可设置此值全局改变默认行为
    private static boolean defaultGenerateNullValue = true;

    // generateNullValue 通过设置此值，可临时改变默认生成 null value 的行为
    protected Boolean generateNullValue = null;

    protected ObjectMapper objectMapper;

    public Jackson() {
        objectMapper = new ObjectMapper();
        config();
    }

    protected void config() {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 解决jackson2无法反序列化LocalDateTime的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static void setDefaultGenerateNullValue(boolean defaultGenerateNullValue) {
        Jackson.defaultGenerateNullValue = defaultGenerateNullValue;
    }

    public Jackson setGenerateNullValue(boolean generateNullValue) {
        this.generateNullValue = generateNullValue;
        return this;
    }

    /**
     * 通过获取 ObjectMapper 进行更个性化设置，满足少数特殊情况
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static Jackson getJson() {
        return new Jackson();
    }

    @Override
    public String toJson(Object object) {
        try {
            // 优先使用对象级的属性 datePattern, 然后才是全局性的 defaultDatePattern
            String dp = datePattern != null ? datePattern : getDefaultDatePattern();
            if (dp != null) {
                objectMapper.setDateFormat(new SimpleDateFormat(dp));
            }

            // 优先使用对象属性 generateNullValue，决定转换 json时是否生成 null value
            boolean pnv = generateNullValue != null ? generateNullValue : defaultGenerateNullValue;
            if (!pnv) {
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }

            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    @Override
    public <T> T parse(String jsonString, Class<T> type) {
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (Exception e) {
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    @Override
    public <T> T parse(byte[] jsonString, Class<T> type) {
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (Exception e) {
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> parseArray(String jsonString, Class<T> type) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, type);
            return objectMapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    @Override
    public <K, V> Map<K, V> parseMap(String jsonString, Class<K> keyType, Class<V> valueType) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType);
            return objectMapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    @Override
    public <K, V> Map<K, V> parseMap(byte[] jsonByte, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Map.class, keyType, valueType);
        try {
            return objectMapper.readValue(jsonByte, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <K, V> List<Map<K, V>> parseMapArray(String jsonStr, Class<K> keyType, Class<V> valType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Map.class);
        try {
            List<Map> list = objectMapper.readValue(jsonStr, javaType);
            List<Map<K,V>> values = new ArrayList<>();
            list.forEach(row -> {
                values.add(parseMap(toJson(row), keyType, valType));
            });
            return values;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
