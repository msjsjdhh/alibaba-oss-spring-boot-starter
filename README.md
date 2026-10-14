# alibaba-oss-spring-boot-starter

> 阿里云 OSS 的 Spring Boot Starter，开箱即用。  
> 自动装配阿里云 OSS 客户端，简化文件上传、下载、删除、生成签名 URL 等常用操作。

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

---

## ✨ 功能特性

- 自动装配阿里云 OSS 客户端
- 支持 `MultipartFile`、`InputStream`、`byte[]` 上传
- 支持文件下载、删除、判断是否存在
- 支持生成公开访问 URL / 签名 URL
- 支持自定义配置与 Bean 覆盖
- 提供 Spring Boot 配置元数据提示
- 开箱即用，减少样板代码

---

## 🚀 快速开始

### 1. 添加依赖

**Maven:**

```xml
<dependency>
    <groupId>com.github.yaolongma</groupId>
    <artifactId>alibaba-oss-spring-boot-starter</artifactId>
    <version>最新版本</version>
</dependency>
