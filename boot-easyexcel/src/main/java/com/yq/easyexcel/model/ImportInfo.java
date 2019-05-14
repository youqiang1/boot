package com.yq.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.yq.kernel.utils.ObjectUtils;
import lombok.*;

/**
 * <p> 导入测试model</p>
 * @author youq  2019/5/14 14:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportInfo extends BaseRowModel {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "年龄", index = 1)
    private Integer age;

    @ExcelProperty(value = "邮箱", index = 2)
    private String email;

    @ExcelProperty(value = "地址", index = 3)
    private String address;

    @Override
    public String toString() {
        return ObjectUtils.toJson(this);
    }

}
