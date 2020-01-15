package com.yq.tools.config.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 监听器</p>
 * @author youq  2019/5/14 10:40
 */
@Slf4j
@Getter
@Setter
public class ExcelListener extends AnalysisEventListener {

    //自定义用于暂时存储data，可以通过实例获取该值
    private List<Object> data = new ArrayList<>();

    @Override
    public void invoke(Object object, AnalysisContext context) {
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        data.add(object);
        //根据业务自行 do something
        doSomething();
        /*
        如数据过大，可以进行定量分批处理
        if(datas.size()<=100){
            datas.add(object);
        }else {
            doSomething();
            datas = new ArrayList<Object>();
        }
         */
    }

    private void doSomething() {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //解析结束销毁不用的资源
        // data.clear();
    }

}
