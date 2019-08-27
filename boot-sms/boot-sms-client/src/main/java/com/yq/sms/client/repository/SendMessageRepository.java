package com.yq.sms.client.repository;

import com.yq.sms.client.entity.SendMessageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> 短信repository</p>
 * @author youq  2019/8/27 11:45
 */
@Repository
public interface SendMessageRepository extends JpaRepository<SendMessageInfo, Integer> {
}
