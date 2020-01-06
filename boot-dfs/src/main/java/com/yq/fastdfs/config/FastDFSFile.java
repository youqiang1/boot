package com.yq.fastdfs.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 文件基础信息</p>
 * @author youq  2020/1/6 14:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastDFSFile {

    private String name;

    private byte[] content;

    private String ext;

    private String md5;

    private String author;

    public FastDFSFile(String name, byte[] content, String ext) {
        super();
        this.name = name;
        this.content = content;
        this.ext = ext;
    }

}
