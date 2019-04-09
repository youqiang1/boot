package com.yq.jpa.console;

import com.yq.jpa.common.SearchType;
import com.yq.jpa.console.qo.CertificateQO;
import com.yq.jpa.console.vo.CertificateVO;
import com.yq.jpa.service.MemberService;
import com.yq.kernel.utils.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 会员信息Controller</p>
 * @author yq  2018/5/19 14:24
 */
@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * <p> 证书查询</p>
     * @param certificateQO 证书查询请求参数
     * @return com.yq.boot.common.ResultData<?>
     * @author yq  2018/5/19 14:32
     */
    @PostMapping("/certificateSearch")
    public ResultData<?> certificateSearch(CertificateQO certificateQO) {
        if (certificateQO.getSearchType() == null || StringUtils.isEmpty(certificateQO.getCondition())) {
            return ResultData.failMsg("查询条件不全，请补全查询条件!");
        }
        try {
            CertificateVO certificateVO = memberService.certificateSearch(certificateQO);
            //根据名称查询编号，根据编号查询名称
            if (certificateVO != null) {
                return certificateQO.getSearchType().equals(SearchType.name) ?
                        ResultData.success(certificateVO.getUsername()) : ResultData.success(certificateVO.getName());
            } else {
                return ResultData.successMsg("没有查询到相关信息！");
            }
        } catch (Exception e) {
            log.error("证书查询异常：", e);
            return ResultData.failMsg("证书查询异常!");
        }
    }

}
