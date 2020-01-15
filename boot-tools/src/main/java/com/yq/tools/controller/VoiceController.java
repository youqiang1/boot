package com.yq.tools.controller;

import com.yq.kernel.model.ResultData;
import com.yq.kernel.utils.DateUtils;
import com.yq.tools.config.excel.ExcelUtils;
import com.yq.tools.entity.VoiceModel;
import com.yq.tools.model.excel.VoiceExcelModel;
import com.yq.tools.service.VoiceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p> voice controller</p>
 * @author youq  2020/1/11 13:27
 */
@Slf4j
@RestController
@RequestMapping("voice")
public class VoiceController {

    @Autowired
    private VoiceService voiceService;

    @Value("${excelPath}")
    private String excelPath;

    /**
     * <p> 导出语音话单</p>
     * @param beginTime     开始时间 yyyyMMddHHmmss
     * @param endTime       结束时间 yyyyMMddHHmmss
     * @param callerIp      主叫IP
     * @param voice1119Node 节点，例：0575_1_20191219
     * @param valid         是否只查询有效话单，1是0否
     * @param sheetName     sheet name
     * @param response      HttpServletResponse
     * @param browser       浏览器请求，1是0否，默认为非浏览器请求，写入默认存放目录
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2020/1/11 13:50
     */
    @RequestMapping(value = "exportVoice", method = RequestMethod.GET)
    public ResultData<?> exportVoice(String beginTime, String endTime, String callerIp, String voice1119Node,
                                     Integer valid, String sheetName, HttpServletResponse response, Integer browser) {
        if (valid == null) {
            return ResultData.failMsg("请求参数异常");
        }
        LocalDateTime begin = DateUtils.string2LocalDateTime(beginTime, "yyyyMMddHHmmss");
        LocalDateTime end = DateUtils.string2LocalDateTime(endTime, "yyyyMMddHHmmss");
        beginTime = DateUtils.localDateTimeDefaultFormat(begin);
        endTime = DateUtils.localDateTimeDefaultFormat(end);
        log.info("查询租户消费历史话单，时间【{} - {}】，主叫IP：【{}】，节点：【{}】，是否只查询有效话单：【{}】", beginTime, endTime, callerIp, voice1119Node, valid == 1);
        List<VoiceModel> models = voiceService.findByTime(beginTime, endTime, valid == 1, callerIp, voice1119Node);
        if (CollectionUtils.isEmpty(models)) {
            return ResultData.failMsg("根据条件未查询到话单数据");
        }
        log.info("根据条件查询到语音话单条数：{}，准备导出", models.size());
        //转成导出对象
        List<VoiceExcelModel> list = buildExcelModel(models);
        //导出
        String fileName = excelPath + System.currentTimeMillis();
        if (browser != null && browser == 1) {
            ExcelUtils.writeExcel(response, list, fileName, sheetName, new VoiceExcelModel());
        } else {
            ExcelUtils.writeExcelToFolder(list, fileName, sheetName, new VoiceExcelModel());
        }
        log.info("导出文件：{}", fileName + ".xlsx");
        return ResultData.success();
    }

    /**
     * <p> 转成导出对象</p>
     * @param models es查询出来的数据实体集合
     * @return java.util.List<com.yq.tools.model.excel.VoiceExcelModel>
     * @author youq  2020/1/11 14:05
     */
    private List<VoiceExcelModel> buildExcelModel(List<VoiceModel> models) {
        return models.stream().map(
                v -> {
                    VoiceExcelModel ve = new VoiceExcelModel();
                    BeanUtils.copyProperties(v, ve);
                    ve.setNode(StringUtils.isNotBlank(v.getNode()) ? v.getNode().split("_")[0] : "");
                    return ve;
                }
        ).sorted(Comparator.comparing(VoiceExcelModel::getReleaseTime)).collect(toList());
    }

}
