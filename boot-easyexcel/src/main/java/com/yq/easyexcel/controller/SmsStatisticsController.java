package com.yq.easyexcel.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yq.easyexcel.config.ExcelUtils;
import com.yq.easyexcel.model.sms.OldSmsImportInfo;
import com.yq.easyexcel.model.sms.SmsExportInfo;
import com.yq.easyexcel.model.sms.SmsImportInfo;
import com.yq.easyexcel.model.sms.UserImportInfo;
import com.yq.kernel.model.ResultData;
import com.yq.kernel.utils.BigDecimalUtil;
import com.yq.kernel.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 测试</p>
 * @author youq  2019/6/18 14:41
 */
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsStatisticsController {

    private Map<String, UserImportInfo> map = new ConcurrentHashMap<>();

    private Map<String, UserImportInfo> nameMap = new ConcurrentHashMap<>();

    private Map<String, String> oldMap = new ConcurrentHashMap<>();

    private List<SmsExportInfo> exportInfos;

    /**
     * <p> 处理</p>
     * @param excel       文件
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/6/18 15:16
     */
    @RequestMapping(value = "process", method = RequestMethod.POST)
    public ResultData<?> process(MultipartFile excel) {
        List<Object> objects = ExcelUtils.readExcel(excel, new SmsImportInfo());
        if (CollectionUtils.isEmpty(objects)) {
            return ResultData.fail();
        }
        Map<String, SmsExportInfo> newMap = Maps.newHashMap();
        for (Object object : objects) {
            SmsImportInfo smsImportInfo = (SmsImportInfo) object;
            String userId = smsImportInfo.getUserId();
            if (StringUtils.isEmpty(userId) || userId.contains("user_id")) {
                log.info("跳过表头或者空行: {}", smsImportInfo);
                continue;
            }
            if (StringUtils.isEmpty(smsImportInfo.getFee())) {
                log.info("跳过空列：{}", smsImportInfo);
                continue;
            }
            if (StringUtils.isEmpty(smsImportInfo.getFee())) {
                log.info("跳过空扣费：{}", smsImportInfo);
                continue;
            }
            if (StringUtils.isEmpty(smsImportInfo.getRfee())) {
                smsImportInfo.setRfee("0");
            } else if (Long.parseLong(smsImportInfo.getRfee()) > 5000) {
                String rate = acquireRate(smsImportInfo.getDate());
                smsImportInfo.setRfee(BigDecimalUtil.multiply(smsImportInfo.getRfee(), rate).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            }
            log.info("用户：{}，实际金额：{}", userId, BigDecimalUtil.subtract(smsImportInfo.getFee(), smsImportInfo.getRfee()));
            UserImportInfo userImportInfo = map.get(userId);
            if (userImportInfo == null) {
                log.info("找不到商户信息？{}", userId);
                continue;
            }
            BigDecimal money = BigDecimalUtil.subtract(smsImportInfo.getFee(), smsImportInfo.getRfee());
            if (money.intValue() < 0) {
                money = new BigDecimal(smsImportInfo.getFee());
            }
            SmsExportInfo smsExportInfo = new SmsExportInfo();
            smsExportInfo.setDate(smsImportInfo.getDate());
            smsExportInfo.setUserId(userId);
            smsExportInfo.setUsername(userImportInfo.getUsername());
            smsExportInfo.setPrice(userImportInfo.getPrice());
            smsExportInfo.setFee(smsImportInfo.getFee());
            smsExportInfo.setRfee(smsImportInfo.getRfee());
            smsExportInfo.setMoney(money);
            smsExportInfo.setFeeCount(BigDecimalUtil.divide(smsImportInfo.getFee(), userImportInfo.getPrice()).intValue());
            smsExportInfo.setRfeeCount(BigDecimalUtil.divide(smsImportInfo.getRfee(), userImportInfo.getPrice()).intValue());
            smsExportInfo.setCount(BigDecimalUtil.divide(money.toString(), userImportInfo.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
            newMap.put(smsImportInfo.getDate() + userImportInfo.getUsername(), smsExportInfo);
        }
        log.info("待导出数据条数：{}", exportInfos.size());
        int updateSize = 0;
        for (SmsExportInfo smsExportInfo : exportInfos) {
            if (!smsExportInfo.getXfType().equals("1")) {
                log.info("非预付费用户，不处理");
                continue;
            }
            if (smsExportInfo.getDate().equals("2019-05-14")
                    || smsExportInfo.getDate().equals("2019-05-15") || smsExportInfo.getDate().equals("2019-05-16")) {
                log.info("过滤异常日期的数据");
                continue;
            }
            String key = smsExportInfo.getDate() + smsExportInfo.getUsername();
            SmsExportInfo newExportInfo = newMap.get(key);
            if (newExportInfo != null && newExportInfo.getMoney().intValue() > 0) {
                smsExportInfo.setMoney(newExportInfo.getMoney());
                smsExportInfo.setCount(newExportInfo.getCount());
                smsExportInfo.setFee(newExportInfo.getFee());
                smsExportInfo.setRfee(newExportInfo.getRfee());
                smsExportInfo.setFeeCount(newExportInfo.getFeeCount());
                smsExportInfo.setRfeeCount(newExportInfo.getRfeeCount());
                updateSize++;
            }
        }
        log.info("修改的数据条数：{}", updateSize);
        return ResultData.success(objects);
    }

    private String acquireRate(String date) {
        switch (date) {
            default:
                return "1";
        }
    }

    /**
     * <p> 处理</p>
     * @param excel       文件
     * @param sheetNo     从1开始
     * @param headLineNum 表头行数
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/6/18 15:16
     */
    @RequestMapping(value = "process2", method = RequestMethod.POST)
    public ResultData<?> process2(MultipartFile excel, int sheetNo, String date, String rate,
                                 @RequestParam(defaultValue = "1") int headLineNum) {
        List<Object> objects = ExcelUtils.readExcel(excel, new SmsImportInfo(), sheetNo, headLineNum);
        exportInfos = Lists.newArrayListWithCapacity(objects.size());
        for (Object object : objects) {
            SmsImportInfo smsImportInfo = (SmsImportInfo) object;
            String userId = smsImportInfo.getUserId();
            if (StringUtils.isEmpty(userId) || userId.contains("user_id")) {
                log.info("跳过表头或者空行: {}", smsImportInfo);
                continue;
            }
            if (StringUtils.isEmpty(smsImportInfo.getFee())) {
                log.info("跳过空列：", smsImportInfo);
                continue;
            }
            if (StringUtils.isEmpty(smsImportInfo.getRfee())) {
                smsImportInfo.setRfee("0");
            } else if (Long.parseLong(smsImportInfo.getRfee()) > 5000) {
                smsImportInfo.setRfee(BigDecimalUtil.multiply(smsImportInfo.getRfee(), rate).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            }
            log.info("用户：{}，实际金额：{}", userId, BigDecimalUtil.subtract(smsImportInfo.getFee(), smsImportInfo.getRfee()));
            UserImportInfo userImportInfo = map.get(userId);
            if (userImportInfo == null) {
                return ResultData.failMsg("没执行处理用户信息方法");
            }
            BigDecimal money = BigDecimalUtil.subtract(smsImportInfo.getFee(), smsImportInfo.getRfee());
            SmsExportInfo smsExportInfo = new SmsExportInfo();
            smsExportInfo.setDate(date);
            smsExportInfo.setUserId(userId);
            smsExportInfo.setUsername(userImportInfo.getUsername());
            smsExportInfo.setPrice(userImportInfo.getPrice());
            if (money.intValue() == 0) {
                String key = date + userImportInfo.getUsername();
                String count = oldMap.get(key);
                if (StringUtils.isEmpty(count)) {
                    log.info("找不到老的统计数据：{}", key);
                    smsExportInfo.setMoney(money);
                    smsExportInfo.setCount(0);
                } else {
                    log.info("老的统计数据：{} - {}", key, count);
                    smsExportInfo.setCount(Integer.parseInt(count));
                    smsExportInfo.setMoney(BigDecimalUtil.multiply(count, userImportInfo.getPrice()));
                }
            } else {
                smsExportInfo.setFee(smsImportInfo.getFee());
                smsExportInfo.setRfee(smsImportInfo.getRfee());
                smsExportInfo.setMoney(money);
                smsExportInfo.setFeeCount(BigDecimalUtil.divide(smsImportInfo.getFee(), userImportInfo.getPrice()).intValue());
                smsExportInfo.setRfeeCount(BigDecimalUtil.divide(smsImportInfo.getRfee(), userImportInfo.getPrice()).intValue());
                smsExportInfo.setCount(BigDecimalUtil.divide(money.toString(), userImportInfo.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue());
            }
            exportInfos.add(smsExportInfo);
        }
        log.info("待导出数据：{}", ObjectUtils.toJson(exportInfos));
        return ResultData.success(objects);
    }

    /**
     * 导出 Excel（一个 sheet）
     */
    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ResultData<?> write(HttpServletResponse response, String filename, String name, Integer sheetNo) {
        if (CollectionUtils.isEmpty(exportInfos)) {
            return ResultData.fail();
        }
        String fileName = "e:\\temp\\" + filename;
        ExcelUtils.writeExcel2(response, exportInfos, fileName, name, new SmsExportInfo(), sheetNo);
        return ResultData.success();
    }

    /**
     * <p> 处理用户信息</p>
     * @param excel       文件
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/6/18 15:16
     */
    @RequestMapping(value = "processUser", method = RequestMethod.POST)
    public ResultData<?> processUser(MultipartFile excel) {
        List<Object> objects = ExcelUtils.readExcel(excel, new UserImportInfo());
        for (Object o : objects) {
            UserImportInfo userImportInfo = (UserImportInfo) o;
            if (!StringUtils.isEmpty(userImportInfo.getPrice())) {
                map.put(userImportInfo.getUserId(), userImportInfo);
                nameMap.put(userImportInfo.getUsername(), userImportInfo);
            }
        }
        return ResultData.success(objects.size());
    }

    /**
     * <p> 处理老的信息</p>
     * @param excel       文件
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/6/18 15:16
     */
    @RequestMapping(value = "processOld", method = RequestMethod.POST)
    public ResultData<?> processOld(MultipartFile excel) {
        List<Object> objects = ExcelUtils.readExcel(excel, new OldSmsImportInfo());
        if (CollectionUtils.isEmpty(objects)) {
            return ResultData.fail();
        }
        exportInfos = Lists.newArrayListWithCapacity(objects.size());
        String date = null;
        for (Object o : objects) {
            OldSmsImportInfo oldSmsImportInfo = (OldSmsImportInfo) o;
            if (!StringUtils.isEmpty(oldSmsImportInfo.getDate())) {
                if (oldSmsImportInfo.getDate().contains("汇总") || oldSmsImportInfo.getDate().contains("总计")) {
                    log.info("过滤数据：{}", oldSmsImportInfo);
                    continue;
                }
                date = oldSmsImportInfo.getDate();
            }
            if (date != null && !StringUtils.isEmpty(oldSmsImportInfo.getUsername())) {
                oldMap.put(date + oldSmsImportInfo.getUsername(), oldSmsImportInfo.getCount());
                //写入list
                UserImportInfo userImportInfo = nameMap.get(oldSmsImportInfo.getUsername());
                if (userImportInfo == null) {
                    log.info("userImportInfo为空？{}", oldSmsImportInfo);
                    continue;
                }
                SmsExportInfo smsExportInfo = new SmsExportInfo();
                smsExportInfo.setDate(date);
                smsExportInfo.setUserId(userImportInfo.getUserId());
                smsExportInfo.setUsername(userImportInfo.getUsername());
                smsExportInfo.setPrice(userImportInfo.getPrice());
                smsExportInfo.setMoney(BigDecimalUtil.multiply(oldSmsImportInfo.getCount(), userImportInfo.getPrice()));
                smsExportInfo.setCount(StringUtils.isEmpty(oldSmsImportInfo.getCount()) ? 0 : Integer.parseInt(oldSmsImportInfo.getCount()));
                smsExportInfo.setXfType(userImportInfo.getXfType());
                exportInfos.add(smsExportInfo);
            }
        }
        log.info("objects: {}", oldMap);
        return ResultData.success(oldMap.size());
    }

}
