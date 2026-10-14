package com.alibaba.autoConfiguration;

import cn.hutool.core.util.StrUtil;
import com.alibaba.core.service.OssTemplete;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Myl
 * @Date: 2026-10-10 17:19
 * @Description:
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(value = OssProperties.class)
public class OssAutoConfiguration {

    /**
     * 凭证解析：优先用配置文件里的 AccessKey，未配置则回退到环境变量。
     * 单独抽成 bean，使用方可用自己的 CredentialsProvider（STS / RAM 角色等）覆盖。
     */
    @Bean
    @ConditionalOnMissingBean
    public CredentialsProvider ossCredentialsProvider(OssProperties ossProperties) {
        if (StrUtil.isNotBlank(ossProperties.getAccessKeyId())
                && StrUtil.isNotBlank(ossProperties.getAccessKeySecret())) {
            log.info("OSS 凭证来源：配置文件 alibaba.oss.access-key-id");
            return new StaticCredentialsProvider(ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret());
        }
        log.info("OSS 凭证来源：环境变量（未配置 alibaba.oss.access-key-id）");
        return new EnvironmentVariableCredentialsProvider();
    }

    /**
     * OSSClient 线程安全且持有连接池，全局单例复用，容器关闭时自动 close。
     */
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public OSSClient ossClient(OssProperties ossProperties, CredentialsProvider ossCredentialsProvider) {
        return OSSClient.newBuilder()
                .credentialsProvider(ossCredentialsProvider)
                .region(ossProperties.getRegion())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(OssTemplete.class)
    public OssTemplete ossTemplete(OSSClient ossClient, OssProperties ossProperties) {
        return new OssTemplete(ossClient, ossProperties);
    }

}
