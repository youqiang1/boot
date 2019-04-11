package com.yq.task.schedule.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p> 测试，不建议一个类里面写多个定时，更倾向于一个定时写一个类，也看实际情况</p>
 * @author youq  2018/12/10 11:11
 */
@Slf4j
@Component
public class DemoTask {

    /**
     * <p> 定时执行</p>
     * "0 0 12 * * ? "	每天12点运行
     * "0 15 10 * * ?"	每天10:15运行
     * "0 15 10 * * ? 2011"	2011年的每天10：15运行
     * "0 * 14 * * ?"	每天14点到15点之间每分钟运行一次，开始于14:00，结束于14:59。
     * "0 0/5 14 * * ?"	每天14点到15点每5分钟运行一次，开始于14:00，结束于14:55。
     * "0 0/5 14,18 * * ?"	每天14点到15点每5分钟运行一次，此外每天18点到19点每5钟也运行一次。
     * "0 0-5 14 * * ?"	每天14:00点到14:05，每分钟运行一次。
     * @author youq  2018/12/10 11:30
     */
    @Scheduled(cron = "0 * 13 * * ?")
    public void cron() {
        log.info("...........cron:【{}】", LocalDateTime.now());
    }

    /**
     * <p> 5000毫秒执行一次</p>
     * fixedDelay：在上一次执行完之后等xxx毫秒(xxx就是fixedDelay = 5000中的5000)再执行，
     * 循环下去,上一次执行多久都没关系 ，反正上一次执行完后xxx毫秒我才执行
     * @author youq  2018/12/10 11:16
     */
    @Scheduled(fixedDelay = 5000)
    public void fixedDelay() throws InterruptedException {
        log.info(".....5000毫秒执行一次........fixedDelay:【{}】", LocalDateTime.now());
        Thread.sleep(8000);
    }

    /**
     * <p> 5000毫秒执行一次,第一次执行延时8000毫秒</p>
     * fixedDelay：在上一次执行完之后等xxx毫秒(xxx就是fixedDelay = 5000中的5000)再执行，循环下去
     * initialDelay：第一次运行次要等xxx毫秒才能执行，比如：假设原来是14:00:00 开始执行这个，
     * 但是设置了initialDelay=8000，那么要在8000毫秒后才能执行这个方法，也就是14:00:08才真正执行
     * @author youq  2018/12/10 11:16
     */
    @Scheduled(fixedDelay = 5000, initialDelay=8000)
    public void fixedDelayAndInitialDelay() {
        log.info(".....5000毫秒执行一次,第一次执行延时8000毫秒........fixedDelayAndInitialDelay:【{}】", LocalDateTime.now());
    }

    /**
     * <p> fixedRate:这个有点蛋疼...,举个例子：比如：假设有5个执行时间点 间隔是5000毫秒：分别是：
     * T1:14:00:00
     * T2:14:00:05
     * T3:14:00:10
     * T4:14:00:15
     * T5:14:00:20
     * 如果T1执行时间花了4秒，也就是到了14:00:04,那么你会看到14:00:05分就开始执行了T2,很正常，此时T1结束时间和T2开始时间只差1000毫秒，没毛病
     * 如果T1执行时间花了8秒，怎么办？这时T1执行完的时间是14:00:08，已经覆盖了T2的时间，T2在14:00:05到14:00:08是等等状态。现在是14:00:08,看起来接着是T3执行，
     * 但实际不是，而是立马执行T2，立马执行T2，立马执行T2（T2说:我不管，T1搞我超时了，我无论也是执行），这时你会发现T2的执行时间（也就是第2次执行时间 ）是：14:00:08，真的是立马。。。
     * 如此类推，只要时执行时间被覆盖了的，到它了就立马执行</p>
     * @author youq  2018/12/10 11:17
     */
    @Scheduled(fixedRate = 5000)
    public void fixedRate() throws InterruptedException {
        log.info("..fixedRate.....每5000毫秒执行一次:【{}】", LocalDateTime.now());
    }

}
