package com.yq.easyexcel.model.sms;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.yq.kernel.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 导入测试model</p>
 * @author youq  2019/5/14 14:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsImportInfo extends BaseRowModel {

    @ExcelProperty(value = "日期", index = 0)
    private String date;

    @ExcelProperty(value = "用户id", index = 1)
    private String userId;

    @ExcelProperty(value = "扣费", index = 2)
    private String fee;

    @ExcelProperty(value = "返费", index = 3)
    private String rfee;

    @Override
    public String toString() {
        return ObjectUtils.toJson(this);
    }

}
