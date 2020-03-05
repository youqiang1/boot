package com.yq.easyexcel.model.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 用户导入信息</p>
 * @author youq  2020/3/5 14:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExcelModel extends BaseRowModel {

    @ExcelProperty(value = "姓名", index = 0)
    private String username;

    @ExcelProperty(value = "手机号", index = 1)
    private String phone;

    @ExcelProperty(value = "年龄", index = 2)
    private String age;

    @ExcelProperty(value = "邮箱", index = 3)
    private String email;

    @ExcelProperty(value = "性别", index = 4)
    private SexEnum sex;

    @ExcelProperty(value = "defined1", index = 5)
    private String defined1;

    @ExcelProperty(value = "defined2", index = 6)
    private String defined2;

    @ExcelProperty(value = "defined3", index = 7)
    private String defined3;

    @ExcelProperty(value = "defined4", index = 8)
    private String defined4;

    @ExcelProperty(value = "defined5", index = 9)
    private String defined5;

    @Override
    public String toString() {
        return ObjectUtils.toJson(this);
    }

}
