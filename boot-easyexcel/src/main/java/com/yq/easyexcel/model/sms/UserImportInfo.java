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
public class UserImportInfo extends BaseRowModel {

    @ExcelProperty(value = "用户id", index = 0)
    private String userId;

    @ExcelProperty(value = "用户名", index = 1)
    private String username;

    @ExcelProperty(value = "默认短信类型", index = 2)
    private String defaultSmsType;

    @ExcelProperty(value = "单价", index = 3)
    private String price;

    @ExcelProperty(value = "类型", index = 4)
    private String xfType;

    @Override
    public String toString() {
        return ObjectUtils.toJson(this);
    }

}
