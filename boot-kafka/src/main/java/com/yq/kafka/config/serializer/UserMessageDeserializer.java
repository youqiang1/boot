package com.yq.kafka.config.serializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yq.kafka.proto.user.UserMessage;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * <p> 反序列化</p>
 * @author youq  2019/4/10 10:17
 */
public class UserMessageDeserializer extends Adapter implements Deserializer<UserMessage.user> {

    @Override
    public UserMessage.user deserialize(String topic, byte[] data) {
        try {
            return UserMessage.user.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("received impassable message " + e.getMessage(), e);
        }
    }

}
