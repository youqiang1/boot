package com.yq.sms.client.repository;

import com.yq.sms.client.entity.ChannelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> 通道信息dao</p>
 * @author youq  2019/8/23 17:35
 */
@Repository
public interface ChannelRepository extends JpaRepository<ChannelInfo, Integer> {
}
