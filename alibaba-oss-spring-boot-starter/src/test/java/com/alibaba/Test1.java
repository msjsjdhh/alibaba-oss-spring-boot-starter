package com.alibaba;

import cn.hutool.core.io.FileUtil;
import com.alibaba.autoConfiguration.OssProperties;
import com.alibaba.core.service.OssTemplete;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;

import java.io.BufferedInputStream;

/**
 * @Author: Myl
 * @Date: 2026-10-10 17:52
 * @Description:
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        OssProperties ossProperties = new OssProperties();
        ossProperties.setRegion("cn-hangzhou");
        ossProperties.setBucket("it-fileupload");
        ossProperties.setAccessKeyId("你的AccessKeyId");
        ossProperties.setAccessKeySecret("你的AccessKeySecret");

        // 容器外手工构造时 client 需要自己建；Spring 环境里由 OssAutoConfiguration 注入单例
        try (OSSClient ossClient = OSSClient.newBuilder()
                .credentialsProvider(new StaticCredentialsProvider(
                        ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret()))
                .region(ossProperties.getRegion())
                .build()) {

            OssTemplete ossTemplete = new OssTemplete(ossClient, ossProperties);
            BufferedInputStream inputStream = FileUtil.getInputStream("C:\\Users\\Myl\\Desktop\\1.png");

            String upload = ossTemplete.upload(inputStream, "1.png");
            System.out.println(upload);
        }
    }
}
