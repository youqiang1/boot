package com.yq.kernel.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 对象工具集
 * @author fengshaoyun
 */
public class ObjectUtils {

    /**
     * Jackson ObjectMapper(通用)
     */
    public static final ObjectMapper om = new ObjectMapper();

    /**
     * Jackson ObjectMapper(用于比较)
     */
    public static final ObjectMapper omextra = new ObjectMapper();

    /**
     * Jackson ObjectMapper(用于Log输出)
     */
    public static final ObjectMapper omlog = new ObjectMapper();

    static {
        om.setSerializationInclusion(Include.NON_NULL);
        om.enable(SerializationFeature.INDENT_OUTPUT);
        om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        omextra.setSerializationInclusion(Include.NON_NULL);

        omextra.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            private static final long serialVersionUID = -5757179836234679988L;

            @Override
            public Object findFilterId(Annotated a) {
                return "extra_ingore";
            }
        });

        omlog.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * 转为JSON字符串
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return om.writeValueAsString(obj);
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    /**
     * 转为JSON字符串，排除使用JsonExtraIngore标注的字段
     * @param obj 对象
     * @return JSON字符串
     * @see JsonExtraIngore
     */
    public static String toJsonExtraIngore(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return omextra.writeValueAsString(obj);
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    /**
     * 转为JSON字符串，Log输出用，不格式化
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJsonUnformatted(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return omlog.writeValueAsString(obj);
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    /**
     * 自JSON解析对象
     * @param json JSON字符串
     * @param cls  数据类
     * @return 目标对象
     */
    public static <T> T fromJson(String json, Class<T> cls) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return om.readValue(json, cls);
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    /**
     * 自JSON解析对象,适用于泛型类
     * @param json             JSON
     * @param parametrized     泛型类
     * @param parameterClasses 泛型参数类
     * @return 目标对象
     */
    public static <P> P fromJson(String json, Class<P> parametrized, Class<?>... parameterClasses) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            if (parameterClasses == null || parameterClasses.length == 0) {
                return om.readValue(json, parametrized);
            } else {
                return om.readValue(json, TypeFactory.defaultInstance().constructParametrizedType(parametrized,
                        parametrized, parameterClasses));
            }
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    /**
     * 转化JSON字符串为JsonNode
     * @param jsonStr JSON字符创
     * @return JsonNode对象
     */
    public static JsonNode readTree(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        try {
            return om.readTree(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 不可变Set
     * @param src 源
     * @return 目标Set
     */
    public static <E> Set<E> unmodifiableSet(Set<E> src) {
        if (src == null) {
            return null;
        }
        return Collections.unmodifiableSet(src);
    }

    /**
     * 不可变List
     * @param src 源
     * @return 目标List
     */
    public static <E> List<E> unmodifiableList(List<E> src) {
        if (src == null) {
            return null;
        }
        return Collections.unmodifiableList(src);
    }

    /**
     * 不可变Map
     * @param src 源
     * @return 目标Map
     */
    public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> src) {
        if (src == null) {
            return null;
        }
        return Collections.unmodifiableMap(src);
    }

    /**
     * 将异常包装为RuntimeException
     * @param e 异常
     */
    public static RuntimeException runtimeException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }

    /**
     * 取得包括基类在内的所有字段
     * @param clazz 类
     * @return Field数组
     */
    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        Class<?> cls = clazz;
        while (cls != null) {
            Field[] fs = cls.getDeclaredFields();
            if (fs != null) {
                fields.addAll(0, Arrays.asList(fs));
            }
            cls = cls.getSuperclass();
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * 取得包括基类在内的所有方法
     * @param clazz 类
     * @return Method数组
     */
    public static Method[] getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<Method>();
        Class<?> cls = clazz;
        while (cls != null) {
            Method[] ms = cls.getDeclaredMethods();
            if (ms != null) {
                methods.addAll(0, Arrays.asList(ms));
            }
            cls = cls.getSuperclass();
        }
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * 设置字段值
     * @param bean  对象
     * @param field 字段
     * @param value 值
     */
    public static void setField(Object bean, String field, Object value) {
        // 先尝试调用setter
        Class<?> cls = bean.getClass();
        Method m = null;
        String mname = "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
        while (m == null && !Object.class.equals(cls)) {
            Method[] ms = cls.getDeclaredMethods();
            for (Method mm : ms) {
                if (mm.getName().equals(mname) && mm.getParameterCount() == 1) {
                    m = mm;
                    break;
                }
            }
            cls = cls.getSuperclass();
        }
        if (m != null) {
            try {
                m.setAccessible(true);
                m.invoke(bean, value);
            } catch (Exception e) {
                throw runtimeException(e);
            }
            return;
        }
        // 没有setter，直接操作field
        cls = bean.getClass();
        Field f = null;
        while (f == null && !Object.class.equals(cls)) {
            try {
                f = cls.getDeclaredField(field);
            } catch (Exception e) {
                f = null;
            }
            cls = cls.getSuperclass();
        }
        try {
            if (f == null) {
                throw new NoSuchFieldException(field);
            }
            f.setAccessible(true);
            f.set(bean, value);
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    /**
     * 不区分大小写定位枚举
     * @param enumValues 枚举值
     * @param name       名称
     * @return 枚举对象
     */
    public static <T extends Enum<T>> T valueOfIngoreCase(T[] enumValues, String name) {
        if (name == null) {
            throw new NullPointerException("Name is null");
        }
        for (T t : enumValues) {
            if (name.equalsIgnoreCase(t.name())) {
                return t;
            }
        }
        throw new IllegalArgumentException("No enum constant " + name);
    }

    /**
     * 序列化为字节数组
     * @param obj 要序列化的对象
     * @return 序列化后的字节数组，若传入的对象为空，则返回空
     */
    public static byte[] toBytes(Object obj) {
        try {
            return obj != null ? om.writeValueAsBytes(obj) : null;
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    /**
     * 从字节数组反序列为对象
     * @param bs    对象字节数组
     * @param clazz 对象类型，若传入的bs为null，则返回null
     * @return
     */
    public static <T> T fromBytes(byte[] bs, Class<T> clazz) {
        try {
            return bs != null ? om.readValue(bs, clazz) : null;
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

}
