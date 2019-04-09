package com.yq.jpa.repository;

import com.yq.jpa.db.FnMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> 会员表Repository</p>
 * @author yq  2018/5/19 14:22
 */
@Repository
public interface MemberRepository extends JpaRepository<FnMember, Integer> {

    /**
     * <p> 根据证书编号查询会员信息</p>
     * @param username 证书编号
     * @return com.yq.boot.db.FnMember
     * @author yq  2018/5/19 15:13
     */
    FnMember findByUsername(String username);

    /**
     * <p> 根据会员单位名称查询会员信息</p>
     * @param name 会员单位名称
     * @return com.yq.boot.db.FnMember
     * @author yq  2018/5/19 15:14
     */
    FnMember findByName(String name);

}
