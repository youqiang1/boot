package com.yq.activiti.util;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricVariableInstance;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p> 工具类</p>
 * @author youq  2020/5/7 17:36
 */
@Slf4j
public class ActivitiUtil {

    public static <T> void setVars(T entity, List<HistoricVariableInstance> variableInstances) {
        Class<?> tClass = entity.getClass();
        try {
            for (HistoricVariableInstance varInstance : variableInstances) {
                Field field = tClass.getDeclaredField(varInstance.getVariableName());
                if (field == null) {
                    continue;
                }
                field.setAccessible(true);
                field.set(entity, varInstance.getValue());
            }
        } catch (Exception e) {
            log.error("setVars exception: ", e);
        }
    }

}
