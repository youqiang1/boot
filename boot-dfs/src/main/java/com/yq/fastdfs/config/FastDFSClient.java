package com.yq.fastdfs.config;

import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p> client</p>
 * @author youq  2020/1/6 14:35
 */
@Slf4j
public class FastDFSClient {

    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            log.error("FastDFS Client Init Fail!", e);
        }
    }

    /**
     * <p> 文件上传</p>
     * @param file 文件实体
     * @return java.lang.String[]
     * @author youq  2020/1/6 14:41
     */
    public static String[] upload(FastDFSFile file) {
        log.info("File Name: {}, File Length: {}", file.getName(), file.getContent().length);
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author", file.getAuthor());

        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        StorageClient storageClient = null;
        try {
            storageClient = getTrackerClient();
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (IOException e) {
            log.error("IO Exception when uploading the file: {}", file.getName(), e);
        } catch (Exception e) {
            log.error("Non IO Exception when uploading the file: {}", file.getName(), e);
        }
        log.info("upload_file time used: {}ms", (System.currentTimeMillis() - startTime));
        if (uploadResults == null && storageClient != null) {
            log.error("upload file fail, error code: {}", storageClient.getErrorCode());
        }
        if (uploadResults != null) {
            log.info("upload file successfully!!! group_name: {}, remoteFileName: {}", uploadResults[0], uploadResults[1]);
        }
        return uploadResults;
    }

    /**
     * <p> 根据 groupName 和文件名获取文件信息</p>
     * @param groupName      the group name
     * @param remoteFileName 文件名
     * @return org.csource.fastdfs.FileInfo
     * @author youq  2020/1/6 14:58
     */
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getTrackerClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            log.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /**
     * <p> 下载文件</p>
     * @param groupName      组名
     * @param remoteFileName 文件名
     * @return java.io.InputStream
     * @author youq  2020/1/6 15:02
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getTrackerClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            return new ByteArrayInputStream(fileByte);
        } catch (IOException e) {
            log.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /**
     * <p> 删除文件</p>
     * @param groupName      组名
     * @param remoteFileName 文件名
     * @author youq  2020/1/6 15:03
     */
    public static void deleteFile(String groupName, String remoteFileName)
            throws Exception {
        StorageClient storageClient = getTrackerClient();
        int i = storageClient.delete_file(groupName, remoteFileName);
        log.info("delete file successfully!!! {}", i);
    }

    public static StorageServer[] getStoreStorages(String groupName)
            throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getStoreStorages(trackerServer, groupName);
    }

    public static ServerInfo[] getFetchStorages(String groupName,
                                                String remoteFileName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    public static String getTrackerUrl() throws IOException {
        return "http://" + getTrackerServer().getInetSocketAddress().getHostString() + ":" + ClientGlobal.getG_tracker_http_port() + "/";
    }

    private static StorageClient getTrackerClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        return new StorageClient(trackerServer, null);
    }

    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getConnection();
    }

}
