package com.alibaba.autoConfiguration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Myl
 * @Date: 2026-10-10 17:19
 * @Description:
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "alibaba.oss")
public class OssProperties {
    private String region;
    private String bucket;

    /**
     * 显式 AccessKey。两个字段都填了才生效；
     * 留空则回退读取环境变量（OSS_ACCESS_KEY_ID / OSS_ACCESS_KEY_SECRET）。
     */
    private String accessKeyId;
    private String accessKeySecret;
}
