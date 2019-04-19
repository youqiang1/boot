package com.yq.netty.server.constants;

/**
 * <p> netty常量</p>
 * @author youq  2019/4/19 14:49
 */
public class NettyConstants {

    /**
     * 当在指定的时间段内没有执行任何读取操作时，将触发状态为IdleState.READER_IDLE，值为0时禁用
     */
    public static final int READER_IDLE_TIME = 5;

    /**
     * 当在指定的时间段内没有执行写操作时，将触发状态为IdleState.WRITER_IDLE，值为0时禁用
     */
    public static final int WRITER_IDLE_TIME = 0;

    /**
     * 当在指定的时间段内既不执行读操作也不执行写操作时，将触发状态为IdleState.ALL_IDLE，值为0时禁用
     */
    public static final int ALL_IDLE_TIME = 0;

    /**
     * 预写长度字段的长度。只允许1、2、3、4和8。
     */
    public static final int LENGTH_FIELD_LENGTH = 2;

    /**
     * 核心线程池大小
     */
    private static final int CORE_POOL_SIZE = 256;

    /**
     * 最大线程量
     */
    public static final int MAX_THREADS = 1024;

    /**
     * 线程等待队列大小
     */
    public static final int BLOCK_QUEUE_SIZE = 2048;

    /**
     * 数据包最大长度
     */
    public static final int MAX_FRAME_LENGTH = 65535;

    public static int getCorePoolSize() {
        return CORE_POOL_SIZE;
    }

    public static int getMaxThreads() {
        return MAX_THREADS;
    }

    public static int getBlockQueueSize() {
        return BLOCK_QUEUE_SIZE;
    }

    public static int getMaxFrameLength() {
        return MAX_FRAME_LENGTH;
    }

}
