package com.yq.tools.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 语音数据导出excel model</p>
 * @author youq  2020/1/11 13:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceExcelModel extends BaseRowModel {

    @ExcelProperty(value = "主叫", index = 0)
    private String caller;

    @ExcelProperty(value = "被叫", index = 1)
    private String called;

    @ExcelProperty(value = "原始主叫", index = 2)
    private String originalCaller;

    @ExcelProperty(value = "原始被叫", index = 3)
    private String originalCalled;

    @ExcelProperty(value = "拆线时间", index = 4)
    private String releaseTime;

    @ExcelProperty(value = "通话时长", index = 5)
    private Integer talkDuration;

    @ExcelProperty(value = "主叫IP", index = 6)
    private String callerIP;

    @ExcelProperty(value = "被叫IP", index = 7)
    private String calledIP;

    @ExcelProperty(value = "节点", index = 8)
    private String node;

}
