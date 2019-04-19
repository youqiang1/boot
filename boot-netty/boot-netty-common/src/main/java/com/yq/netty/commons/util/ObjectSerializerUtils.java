package com.yq.netty.commons.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * <p> 序列化工具</p>
 * @author youq  2019/4/19 16:50
 */
public class ObjectSerializerUtils {

    /**
     * <p> 反序列化</p>
     * @param data 字节数组
     * @return java.lang.Object
     * @author youq  2019/4/19 16:51
     */
    public static Object deserializer(byte[] data) {
        if (data != null && data.length > 0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * <p> 序列化对象</p>
     * @param obj 对象
     * @return byte[]
     * @author youq  2019/4/19 16:52
     */
    public static byte[] serializer(Object obj) {
        if (obj != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                oos.close();
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
}
