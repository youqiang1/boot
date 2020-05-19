package com.yq.ftps.controller;

import com.yq.ftps.util.FtpsUtil;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 测试</p>
 * @author youq  2019/9/6 17:40
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * <p> 文件上传</p>
     * @param file     上传的文件
     * @param filename 上传后的文件名
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/9/6 18:08
     */
    @RequestMapping("/upload")
    public ResultData<?> upload(MultipartFile file, String filename) {
        try {
            if (file.isEmpty() || StringUtils.isEmpty(filename)) {
                return ResultData.failMsg("请求参数异常");
            }
            FtpsUtil ftpsUtil = new FtpsUtil();
            ftpsUtil.connect("", 22, "", "");
            ftpsUtil.cd("");
            ftpsUtil.upload(file, filename);
            return ResultData.success();
        } catch (Exception e) {
            log.error("upload exception: ", e);
            return ResultData.failMsg(e.getMessage());
        }
    }

    /**
     * <p> 文件上传</p>
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/9/6 18:08
     */
    @RequestMapping("/uploadDir")
    public ResultData<?> uploadDir(String filePath) {
        try {
            if (StringUtils.isEmpty(filePath)) {
                return ResultData.failMsg("请求参数异常");
            }
            File file = new File(filePath);
            FtpsUtil ftpsUtil = new FtpsUtil();
            ftpsUtil.connect("", 22, "", "");
            ftpsUtil.cd("");
            ftpsUtil.uploadDir(file);
            boolean delete = file.delete();
            return ResultData.success(delete);
        } catch (Exception e) {
            log.error("upload exception: ", e);
            return ResultData.failMsg(e.getMessage());
        }
    }

    /**
     * <p> 文件上传</p>
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/9/6 18:08
     */
    @RequestMapping("/upload2")
    public ResultData<?> upload2(String fileName) throws Exception {
        File tempFile = new File("D:/temp/cmcc/" + fileName);
        boolean createFileFlag = tempFile.createNewFile();
        try (FileWriter writer = new FileWriter(tempFile, true)) {
            for (int index = 0; index < 2; index++) {
                String line;
                // 将话单转为话单对应的数据
                line = "123\r\n";
                // 将话单写入TXT文件
                writer.write(line);
            }
        }
        // 调用政企SFTP接口上传话单
        FtpsUtil ftpsUtil = new FtpsUtil();
        ftpsUtil.connect("", 22, "", "");
        ftpsUtil.cd("");
        ftpsUtil.uploadDir(tempFile);
        ftpsUtil.disconnect();
        boolean delete = tempFile.delete();
        return ResultData.success(delete);
    }

    @RequestMapping("/test")
    public ResultData<?> test() {
        try {
            File file = new File("e:/temp/userbalance.txt");
            InputStream inputStream = new FileInputStream(file);
            byte b[] = new byte[(int) file.length()];
            int len = inputStream.read(b);
            inputStream.close();
            String content = new String(b);
            String[] balances = content.split("\r\n");
            log.info("数据长度：{}-{}", len, balances.length);
            Map<String, BigDecimal> userBalanceMap = new HashMap<>();
            for (String balance : balances) {
                String[] userBalances = balance.split(",");
                // log.info("balance:{}-{}-{}", balance, userBalances[0], userBalances[1]);
                BigDecimal userBalance = userBalanceMap.get(userBalances[0]);
                if (userBalance != null) {
                    userBalanceMap.put(userBalances[0], userBalance.add(new BigDecimal(userBalances[1])));
                } else {
                    userBalanceMap.put(userBalances[0], new BigDecimal(userBalances[1]));
                }
            }
            for (Map.Entry<String, BigDecimal> entry : userBalanceMap.entrySet()) {
                log.info("商户id：【{}】，应返金额：【{}】", entry.getKey(), entry.getValue());
            }
            return ResultData.success();
        } catch (Exception e) {
            log.error("异常：", e);
            return ResultData.fail();
        }
    }

}
