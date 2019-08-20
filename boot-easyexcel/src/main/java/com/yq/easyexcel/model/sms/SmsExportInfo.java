package com.yq.easyexcel.model.sms;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <p> 导出测试model</p>
 * @author youq  2019/5/14 14:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsExportInfo extends BaseRowModel {

    @ExcelProperty(value = "日期", index = 0)
    private String date;

    @ExcelProperty(value = "用户id", index = 1)
    private String userId;

    @ExcelProperty(value = "用户名称", index = 2)
    private String username;

    @ExcelProperty(value = "总扣费", index = 3)
    private String fee;

    @ExcelProperty(value = "总返费", index = 4)
    private String rfee;

    @ExcelProperty(value = "实际消费金额", index = 5)
    private BigDecimal money;

    @ExcelProperty(value = "单价", index = 6)
    private String price;

    @ExcelProperty(value = "计费条数", index = 7)
    private Integer feeCount;

    @ExcelProperty(value = "返还条数", index = 8)
    private Integer rfeeCount;

    @ExcelProperty(value = "实际计费条数", index = 9)
    private Integer count;

    @ExcelProperty(value = "付费类型", index = 10)
    private String xfType;

}
