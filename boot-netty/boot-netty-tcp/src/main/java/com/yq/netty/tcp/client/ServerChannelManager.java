package com.yq.netty.tcp.client;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 服务通道管理</p>
 * @author youq  2019/4/22 16:10
 */
public class ServerChannelManager {

    private static Map<String, Channel> map = new ConcurrentHashMap<>();

    /**
     * <p> add</p>
     * @author youq  2019/4/22 16:13
     */
    public static void add(String key, Channel channel) {
        map.put(key, channel);
    }

    /**
     * <p> get</p>
     * @author youq  2019/4/22 16:13
     */
    public static Channel get(String key) {
        return map.get(key);
    }

    /**
     * <p> remove</p>
     * @author youq  2019/4/22 16:13
     */
    public static void remove(String key) {
        map.remove(key);
    }

}
