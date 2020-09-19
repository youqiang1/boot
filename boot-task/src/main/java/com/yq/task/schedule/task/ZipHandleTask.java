package com.yq.task.schedule.task;

import com.yq.kernel.utils.file.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * <p> zip文件处理定时任务</p>
 * @author youq  2020/9/19 9:17
 */
@Slf4j
@Component
public class ZipHandleTask {

    @Value("${zip.source}")
    private String source;

    @Value("${zip.temp}")
    private String temp;

    @Value("${zip.target}")
    private String target;

    @Value("${zip.password}")
    private String password;

    /**
     * <p> 定时任务，3秒执行一次</p>
     * @author youq  2020/9/19 9:55
     */
    @Scheduled(fixedDelay = 3000, initialDelay = 2000)
    public void execute() {
        if (source.equals(target) || source.equals(temp)) {
            log.error("源文件目录不允许与目标文件目录相同，请更新配置并重启服务");
            return;
        }
        File sourceFolder = new File(source);
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            log.error("源文件目录地址错误，请更新配置并重启服务");
            return;
        }
        File targetFolder = new File(target);
        if (!targetFolder.exists()) {
            boolean mkdirs = targetFolder.mkdirs();
            if (!mkdirs) {
                log.error("目标文件目录不存在，且创建失败，请手动创建目录文件目录");
                return;
            }
        }
        File[] files = sourceFolder.listFiles();
        if (null == files || files.length <= 0) {
            log.error("源目录暂无需要处理的文件");
            return;
        }
        long st = System.currentTimeMillis();
        try {
            //处理
            process(files);
        } catch (Exception e) {
            log.error("文件处理异常：", e);
        }
        log.info("文件处理完成，耗时：{}", System.currentTimeMillis() - st);
    }

    /**
     * <p> 文件处理</p>
     * @param files 文件列表
     * @author youq  2020/9/19 9:55
     */
    private void process(File[] files) throws ZipException {
        for (File file : files) {
            //只处理zip文件
            if (!isZip(file)) {
                continue;
            }
            //解密解压
            File[] unzip = ZipUtils.unzip(file, temp, password);
            //只加密
            ZipUtils.zip(temp, target + File.separator + file.getName());
            //删除临时文件
            for (File tempFile : unzip) {
                boolean flag = tempFile.delete();
                log.info("临时文件【" + tempFile + "】删除：" + flag);
            }
        }
        //处理完删除源文件
        for (File file : files) {
            if (isZip(file)) {
                boolean flag = file.delete();
                log.info("源文件【" + file + "】删除：" + flag);
            }
        }
    }

    /**
     * <p> 是否zip文件</p>
     * @param file 文件
     * @return boolean
     * @author youq  2020/9/19 9:55
     */
    private boolean isZip(File file) {
        return file.getName().endsWith(".zip");
    }

}
