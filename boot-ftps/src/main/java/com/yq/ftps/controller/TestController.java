package com.yq.ftps.controller;

import com.yq.ftps.util.FtpsUtil;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p> 测试</p>
 * @author youq  2019/9/6 17:40
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * <p> 文件上传</p>
     * @param file     上传的文件
     * @param filename 上传后的文件名
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/9/6 18:08
     */
    @RequestMapping("/upload")
    public ResultData<?> upload(MultipartFile file, String filename) {
        try {
            if (file.isEmpty() || StringUtils.isEmpty(filename)) {
                return ResultData.failMsg("请求参数异常");
            }
            FtpsUtil ftpsUtil = new FtpsUtil();
            ftpsUtil.connect("", 22, "", "");
            ftpsUtil.upload("/", file, filename);
            return ResultData.success();
        } catch (Exception e) {
            log.error("upload exception: ", e);
            return ResultData.failMsg(e.getMessage());
        }
    }

}
