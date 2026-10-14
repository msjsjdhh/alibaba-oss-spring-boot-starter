package com.alibaba;

import cn.hutool.core.io.FileUtil;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.*;
import com.aliyun.sdk.service.oss2.paginator.ListBucketsIterable;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import com.aliyun.sdk.service.oss2.transport.ByteArrayBinaryData;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: Myl
 * @Date: 2026-10-09 20:55
 * @Description:
 */
@SpringBootTest
public class TestPlus {
    public static void main(String[] args) {
        String region = "cn-hangzhou";

        CredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
        OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(provider)
                .region(region);

        try (OSSClient client = clientBuilder.build()) {

            //上传文件
            // byte[] content = FileUtil.readBytes("\u202AC:\\Users\\Myl\\Desktop\\1.png");
            // BinaryData data = new ByteArrayBinaryData(content);
            // PutObjectRequest req = PutObjectRequest.newBuilder()
            //         .bucket("it-fileupload")
            //         .key("test.png")
            //         .body(data)
            //         .build();
            // client.putObject(req);

            //删除文件
            // DeleteObjectRequest req1 = DeleteObjectRequest.newBuilder()
            //         .bucket("it-fileupload")
            //         .key("test")
            //         .build();
            // client.deleteObject(req1);

            GetObjectRequest req2 = GetObjectRequest.newBuilder()
                    .key("test.png")
                    .bucket("it-fileupload")
                    .build();
            GetObjectResult object = client.getObject(req2);




        } catch (Exception e) {
            System.out.printf("error:\n%s", e);
        }
    }
}
