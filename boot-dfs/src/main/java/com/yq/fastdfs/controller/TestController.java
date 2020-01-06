package com.yq.fastdfs.controller;

import com.yq.fastdfs.config.FastDFSClient;
import com.yq.fastdfs.config.FastDFSFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p> test</p>
 * @author youq  2020/1/6 15:19
 */
@Slf4j
@Controller
public class TestController {

    @RequestMapping("/")
    public String index() {
        return "upload";
    }

    @RequestMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
        try {
            // Get the file and save it somewhere
            String path = saveFile(file);
            if (StringUtils.isNotEmpty(path)) {
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded '" + file.getOriginalFilename() + "'");
                redirectAttributes.addFlashAttribute("path",
                        "file path url '" + path + "'");
            }
        } catch (Exception e) {
            log.error("upload file failed", e);
        }
        return "redirect:/uploadStatus";
    }

    /**
     * <p> 文件上传操作</p>
     * @param multipartFile MultipartFile
     * @return java.lang.String
     * @author youq  2020/1/6 15:31
     */
    private String saveFile(MultipartFile multipartFile) throws IOException {
        String[] fileAbsolutePath = {};
        //文件名
        String fileName = multipartFile.getOriginalFilename();
        //文件后缀，文件类型
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        //文件输入流
        InputStream inputStream = multipartFile.getInputStream();
        int len1 = inputStream.available();
        byte[] file_buff = new byte[len1];
        inputStream.read(file_buff);
        inputStream.close();
        //拼接DFS文件上传基础信息
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        try {
            //upload to fastDFS
            fileAbsolutePath = FastDFSClient.upload(file);
        } catch (Exception e) {
            log.error("upload file Exception!", e);
        }
        if (fileAbsolutePath == null) {
            log.error("upload file failed,please upload again!");
            return null;
        }
        return FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
    }

}
