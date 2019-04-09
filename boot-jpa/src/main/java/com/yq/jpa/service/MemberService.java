package com.yq.jpa.service;

import com.yq.jpa.common.SearchType;
import com.yq.jpa.console.qo.CertificateQO;
import com.yq.jpa.console.vo.CertificateVO;
import com.yq.jpa.db.FnMember;
import com.yq.jpa.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 会员信息Service</p>
 * @author yq  2018/5/19 14:24
 */
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * <p> 根据条件查询会员信息</p>
     * @param certificateQO 查询类型，条件
     * @return com.yq.boot.console.vo.CertificateVO
     * @author yq  2018/5/19 14:46
     */
    public CertificateVO certificateSearch(CertificateQO certificateQO) {
        FnMember member;
        if (certificateQO.getSearchType().equals(SearchType.number)) {
            member = memberRepository.findByUsername(certificateQO.getCondition());
        } else {
            member = memberRepository.findByName(certificateQO.getCondition());
        }

        if (member != null) {
            return CertificateVO.builder()
                    .username(member.getUsername())
                    .name(member.getName())
                    .build();
        }
        return null;
    }

}
