package com.alibaba.core.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.autoConfiguration.OssProperties;
import com.alibaba.core.exception.OssException;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.models.*;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import com.aliyun.sdk.service.oss2.transport.ByteArrayBinaryData;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @Author: Myl
 * @Date: 2026-10-10 17:25
 * @Description:
 */
public class OssTemplete {

    /** 由自动配置注入的单例客户端，不要在方法里反复构建。 */
    private final OSSClient ossClient;
    private final OssProperties ossProperties;

    public OssTemplete(OSSClient ossClient, OssProperties ossProperties) {
        this.ossClient = ossClient;
        this.ossProperties = ossProperties;
    }

    public String upload(InputStream in, String fileName) {
        String finalFileName = fileNameUpdate(fileName);
        try {
            // 上传文件
            byte[] content = IoUtil.readBytes(in);
            BinaryData data = new ByteArrayBinaryData(content);
            PutObjectRequest req = PutObjectRequest.newBuilder()
                    .bucket(ossProperties.getBucket())
                    .key(finalFileName)
                    .body(data)
                    .build();
            ossClient.putObject(req);
        } catch (Exception e) {
            throw new OssException("阿里云文件上传异常，异常信息如下" + e.getMessage());
        }
        return getUrl(finalFileName);
    }

    /**
     * 由 object key 拼出公网访问地址，与 upload 返回格式保持一致。
     * 使用方无需再自行拼接 bucket / region。
     */
    public String getUrl(String key) {
        return "https://" + ossProperties.getBucket() + ".oss-" + ossProperties.getRegion()
                + ".aliyuncs.com/" + key;
    }

    public void delete(String fileName) {
        try {
            // 删除文件
            DeleteObjectRequest req = DeleteObjectRequest.newBuilder()
                    .bucket(ossProperties.getBucket())
                    .key(fileName)
                    .build();
            ossClient.deleteObject(req);
        } catch (Exception e) {
            throw new OssException("删除文件出错，错误信息如下" + e.getMessage());
        }
    }

    public ListObjectsV2Result getFiles(){
        ListObjectsV2Result result;
        try{

            String format = DateUtil.format(LocalDateTime.now(), "yyyy/MM/dd");

            ListObjectsV2Request req = ListObjectsV2Request.newBuilder()
                    .bucket(ossProperties.getBucket())
                    .prefix(format)     // 注意结尾带斜杠，表示目录前缀
                    .build();

            result = ossClient.listObjectsV2(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public ListObjectsV2Result getFiles(String continuationToken) {
        String format = DateUtil.format(LocalDateTime.now(), "yyyy/MM/dd");


        ListObjectsV2Request.Builder builder = ListObjectsV2Request.newBuilder()
                .bucket(ossProperties.getBucket())
                .prefix(format)
                .maxKeys(1000L);
        if (continuationToken != null) {
            builder.continuationToken(continuationToken);
        }
        return ossClient.listObjectsV2(builder.build());

    }


    private String fileNameUpdate(String fileName) {
        String format = DateUtil.format(LocalDateTime.now(), "yyyy/MM/dd");
        String uuid = IdUtil.fastUUID();
        return format + "/" + uuid + fileName;
    }

}
