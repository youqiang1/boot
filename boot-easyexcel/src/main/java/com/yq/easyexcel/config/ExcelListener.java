package com.yq.easyexcel.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;
import com.yq.easyexcel.db.User;
import com.yq.easyexcel.model.user.UserExcelModel;
import com.yq.easyexcel.service.UserService;
import com.yq.kernel.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * <p> 监听器</p>
 * @author youq  2019/5/14 10:40
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelListener extends AnalysisEventListener {

    /**
     * 批量操作阀值
     */
    private static final Integer BATCH_COUNT = 1000;

    //自定义用于暂时存储data，可以通过实例获取该值
    private List<Object> data = new ArrayList<>();

    private String name;

    private UserService userService;

    private Map<String, String> definedMap;

    private List<String> definedFields = new LinkedList<>();

    private List<UserExcelModel> importInfos = new ArrayList<>();

    public ExcelListener(String name, UserService userService) {
        this.name = name;
        this.userService = userService;
    }

    @Override
    public void invoke(Object object, AnalysisContext context) {
        if (StringUtils.isBlank(name) && !"user-defined".equals(name)) {
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            data.add(object);
        } else {
            //根据业务自行 do something
            doSomething(object);
        }
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

    private void doSomething(Object object) {
        UserExcelModel importInfo = (UserExcelModel) object;
        if (definedMap == null && CollectionUtils.isEmpty(definedFields)) {
            definedMap = new HashMap<>();
            if (StringUtils.isNotBlank(importInfo.getDefined1())) {
                definedFields.add("defined1");
                definedMap.put("defined1", importInfo.getDefined1());
            }
            if (StringUtils.isNotBlank(importInfo.getDefined2())) {
                definedFields.add("defined2");
                definedMap.put("defined2", importInfo.getDefined2());
            }
            if (StringUtils.isNotBlank(importInfo.getDefined3())) {
                definedFields.add("defined3");
                definedMap.put("defined3", importInfo.getDefined3());
            }
            if (StringUtils.isNotBlank(importInfo.getDefined4())) {
                definedFields.add("defined4");
                definedMap.put("defined4", importInfo.getDefined4());
            }
            if (StringUtils.isNotBlank(importInfo.getDefined5())) {
                definedFields.add("defined5");
                definedMap.put("defined5", importInfo.getDefined5());
            }
        } else {
            importInfos.add(importInfo);
        }
        if (importInfos.size() >= BATCH_COUNT) {
            save();
        }
    }

    private void save() {
        List<User> users = Lists.newArrayListWithCapacity(importInfos.size());
        for (UserExcelModel importInfo : importInfos) {
            User user = new User();
            user.setUsername(importInfo.getUsername());
            user.setPhone(importInfo.getPhone());
            user.setAge(importInfo.getAge() == null ? null : Integer.parseInt(importInfo.getAge()));
            user.setEmail(importInfo.getEmail());
            user.setSex(importInfo.getSex());
            Map<String, Object> json = new HashMap<>();
            Map<String, Integer> definedMark = new HashMap<>();
            for (String definedField : definedFields) {
                switch (definedField) {
                    case "defined1":
                        json.put(definedMap.get("defined1"), importInfo.getDefined1());
                        definedMark.put(definedMap.get("defined1"), 1);
                        break;
                    case "defined2":
                        json.put(definedMap.get("defined2"), importInfo.getDefined2());
                        definedMark.put(definedMap.get("defined2"), 2);
                        break;
                    case "defined3":
                        json.put(definedMap.get("defined3"), importInfo.getDefined3());
                        definedMark.put(definedMap.get("defined3"), 3);
                        break;
                    case "defined4":
                        json.put(definedMap.get("defined4"), importInfo.getDefined4());
                        definedMark.put(definedMap.get("defined4"), 4);
                        break;
                    case "defined5":
                        json.put(definedMap.get("defined5"), importInfo.getDefined5());
                        definedMark.put(definedMap.get("defined5"), 5);
                        break;
                    default:
                        break;
                }
            }
            user.setJson(ObjectUtils.toJson(json));
            user.setJsonName(StringUtils.join(json.keySet(), ","));
            user.setDefinedMark(ObjectUtils.toJson(definedMark));
            users.add(user);
        }
        userService.save(users);
        importInfos.clear();
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //解析结束销毁不用的资源
        // data.clear();
        if (CollectionUtils.isNotEmpty(importInfos)) {
            save();
        }
        definedFields.clear();
        definedMap = null;
    }

}
