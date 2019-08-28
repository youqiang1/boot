package com.yq.sms.commons.task;

import lombok.extern.slf4j.Slf4j;

/**
 * <p> 通道任务接口</p>
 * @author youq  2019/8/26 11:38
 */
@Slf4j
public abstract class ChannelTaskInterface implements Runnable {

    private volatile boolean isRun = true;

    private long delayTime = 1000;

    @Override
    public void run() {
        while (isRun) {
            Loop();
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException ignored) {}
        }
    }

    // 子类业务逻辑
    public abstract void Loop();

    public void start() {
        isRun = true;
    }

    public void stop() {
        isRun = false;
    }

    public boolean state() {
        return isRun;
    }

}
