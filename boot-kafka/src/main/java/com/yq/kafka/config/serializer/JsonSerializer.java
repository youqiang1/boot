package com.yq.kafka.config.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * <p> json序列化</p>
 *
 * @author youq  2020/5/19 下午9:33
 */
public class JsonSerializer extends Adapter implements Serializer<Object> {

    @Override
    public byte[] serialize(String s, Object o) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(o).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

}