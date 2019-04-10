package com.yq.kafka.config.serializer;

import com.yq.kafka.proto.user.UserMessage;
import org.apache.kafka.common.serialization.Serializer;

/**
 * <p> 序列化</p>
 * @author youq  2019/4/10 10:17
 */
public class UserMessageSerializer extends Adapter implements Serializer<UserMessage.user> {

    @Override
    public byte[] serialize(String topic, UserMessage.user data) {
        return data.toByteArray();
    }

}
