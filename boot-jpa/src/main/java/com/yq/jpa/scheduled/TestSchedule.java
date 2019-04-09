package com.yq.jpa.scheduled;

import com.yq.jpa.db.Location;
import com.yq.jpa.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p> 测试</p>
 * @author youq  2019/4/2 19:12
 */
@Slf4j
@Component
public class TestSchedule {

    @Autowired
    private LocationRepository locationRepository;

    @Scheduled(fixedDelay = 60 * 1000)
    public void executor() {
        List<Location> list = locationRepository.findAll();
        log.info("listSize:{}", list.size());
    }

}
