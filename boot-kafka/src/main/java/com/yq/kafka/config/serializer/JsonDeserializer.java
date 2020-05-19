package com.yq.kafka.config.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yq.kafka.proto.user.UserMessage;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

/**
 * <p> json序列化</p>
 *
 * @author youq  2020/5/19 下午9:33
 */
public class JsonDeserializer extends Adapter implements Deserializer<Object> {

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("received impassable message " + e.getMessage(), e);
        }
    }

}