package com.daitian.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by 代天 on 2019/12/1.
 */
public class PmsUploadUtil {
    private static final String FASTDFS_URL = "192.168.6.128";
    public static String uploadImage(MultipartFile multipartFile) throws IOException, MyException {
        ClientGlobal.init("tracker.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer,null);
        byte[] bytes = multipartFile.getBytes();
        String fileName = multipartFile.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        String extName = fileName.substring(index+1);
        String[] returnUrls = storageClient.upload_file(bytes,extName,null);
        StringBuffer imgUrl = new StringBuffer("http://"+FASTDFS_URL);
        for (String r:returnUrls) {
            imgUrl.append("/"+r);
        }
        return imgUrl.toString();
    }
}
