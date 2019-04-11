package com.yq.kernel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p> 返回给前端分页使用的list model</p>
 * @author youq  2019/4/11 10:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListViewData<T> {

    /**
     * 数据条数
     */
    private long total;

    /**
     * 数据内容
     */
    private List<T> list;

}
