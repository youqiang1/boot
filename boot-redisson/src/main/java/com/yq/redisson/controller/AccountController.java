package com.yq.redisson.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yq.kernel.model.ResultData;
import com.yq.redisop.model.AccountModel;
import com.yq.redisop.service.AccountRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * <p> 测试</p>
 * @author youq  2019/4/29 17:22
 */
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    private ExecutorService pool;

    @Autowired
    private AccountRedisService accountRedisService;

    @PostConstruct
    public void init() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-demo-%d").build();
        pool = new ThreadPoolExecutor(100, 200, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024 * 10), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    @GetMapping("/incr")
    public ResultData<?> incr(Long incr) {
        AccountModel model = AccountModel.builder()
                .account("youq")
                .increment(incr)
                .build();
        Long balance = accountRedisService.increment(model);
        return ResultData.success(balance);
    }

    /**
     * <p> max < 10000</p>
     * @param max  最大循环次数
     * @param incr 正数为加，负数为减
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/4/29 18:03
     */
    @GetMapping("/incr2")
    public ResultData<?> incr2(Integer max, Long incr) {
        if (max == null || max < 1 || max > 10000 || incr == null || incr == 0) {
            return ResultData.failMsg("请求参数异常！");
        }
        AccountModel model = AccountModel.builder()
                .account("youq")
                .build();
        for (int i = 0; i < max; i++) {
            pool.execute(() -> {
                model.setIncrement(incr);
                accountRedisService.increment(model);
            });
        }
        try {
            boolean flag = pool.awaitTermination(3, TimeUnit.SECONDS);
            log.info("flag: {}", flag);
        } catch (InterruptedException ignored) {
        }
        model.setIncrement(0L);
        Long balance = accountRedisService.increment(model);
        return ResultData.success(balance);
    }

}
