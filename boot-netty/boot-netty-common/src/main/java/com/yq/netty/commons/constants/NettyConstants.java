package com.yq.netty.commons.constants;

/**
 * <p> netty常量</p>
 * @author youq  2019/4/19 14:49
 */
public class NettyConstants {

    /**
     * 当在指定的时间段内没有执行任何读取操作时，将触发状态为IdleState.READER_IDLE，值为0时禁用
     */
    public static final int SERVER_READER_IDLE_TIME = 5;

    /**
     * 当在指定的时间段内没有执行写操作时，将触发状态为IdleState.WRITER_IDLE，值为0时禁用
     */
    public static final int SERVER_WRITER_IDLE_TIME = 0;

    /**
     * 当在指定的时间段内既不执行读操作也不执行写操作时，将触发状态为IdleState.ALL_IDLE，值为0时禁用
     */
    public static final int SERVER_ALL_IDLE_TIME = 0;

    /**
     * 当在指定的时间段内没有执行任何读取操作时，将触发状态为IdleState.READER_IDLE，值为0时禁用
     */
    public static final int CLIENT_READER_IDLE_TIME = 10;

    /**
     * 当在指定的时间段内没有执行写操作时，将触发状态为IdleState.WRITER_IDLE，值为0时禁用
     */
    public static final int CLIENT_WRITER_IDLE_TIME = 10;

    /**
     * 当在指定的时间段内既不执行读操作也不执行写操作时，将触发状态为IdleState.ALL_IDLE，值为0时禁用
     */
    public static final int CLIENT_ALL_IDLE_TIME = 0;

    /**
     * 预写长度字段的长度。只允许1、2、3、4和8。
     */
    public static final int LENGTH_FIELD_LENGTH = 2;

    /**
     * 数据包最大长度
     */
    public static final int MAX_FRAME_LENGTH = 65535;

    /**
     * 默认重连机制为10次
     */
    public static final int MAX_RETRY_TIMES = 10;

    /**
     * 心跳包
     */
    public static final String HEARTBEAT = "ping-pong-ping-pong";

}
