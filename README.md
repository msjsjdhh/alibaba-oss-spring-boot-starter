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
```

**Gradle:**

```groovy
implementation 'com.github.yaolongma:alibaba-oss-spring-boot-starter:最新版本'
```

> 如果尚未发布到 Maven Central，可以先使用 GitHub Packages 或本地 `mvn install`。

### 2. 添加配置

`application.yml`：

```yaml
alibaba:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: ${ALIYUN_OSS_ACCESS_KEY_ID}
    access-key-secret: ${ALIYUN_OSS_ACCESS_KEY_SECRET}
    bucket-name: your-bucket-name

    # 可选配置
    # dir-prefix: upload/
    # max-file-size: 10MB
    # allowed-extension: jpg,jpeg,png,pdf
```

`application.properties`：

```properties
alibaba.oss.endpoint=oss-cn-hangzhou.aliyuncs.com
alibaba.oss.access-key-id=${ALIYUN_OSS_ACCESS_KEY_ID}
alibaba.oss.access-key-secret=${ALIYUN_OSS_ACCESS_KEY_SECRET}
alibaba.oss.bucket-name=your-bucket-name
```

> 配置前缀请按你项目实际实现调整，例如可能是 `aliyun.oss.*`。

### 3. 注入使用

```java
@RestController
@RequiredArgsConstructor
public class FileController {

    private final AlibabaOssTemplate ossTemplate;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return ossTemplate.upload(file);
    }

    @GetMapping("/download")
    public void download(@RequestParam String objectName, HttpServletResponse response) {
        ossTemplate.download(objectName, response);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String objectName) {
        ossTemplate.delete(objectName);
    }
}
```

> 类名 `AlibabaOssTemplate` 请替换为你项目中实际暴露的模板类名，例如 `OssTemplate`、`OssService`。

---

## ⚙️ 配置项说明

| 配置项 | 说明 | 必填 | 默认值 |
|---|---|---|---|
| `alibaba.oss.endpoint` | 阿里云 OSS Endpoint | 是 | 无 |
| `alibaba.oss.access-key-id` | AccessKey ID | 是 | 无 |
| `alibaba.oss.access-key-secret` | AccessKey Secret | 是 | 无 |
| `alibaba.oss.bucket-name` | Bucket 名称 | 是 | 无 |
| `alibaba.oss.dir-prefix` | 上传目录前缀 | 否 | 空 |
| `alibaba.oss.max-file-size` | 单文件大小限制 | 否 | 无 |
| `alibaba.oss.allowed-extension` | 允许的扩展名 | 否 | 无 |

---

## 🧩 核心 API 示例

### 上传文件

```java
String url = ossTemplate.upload(multipartFile);
```

### 上传字节数组

```java
String url = ossTemplate.upload(bytes, "test.png");
```

### 下载文件

```java
InputStream inputStream = ossTemplate.download("upload/test.png");
```

### 删除文件

```java
ossTemplate.delete("upload/test.png");
```

### 判断文件是否存在

```java
boolean exists = ossTemplate.exists("upload/test.png");
```

### 生成签名 URL

```java
String signedUrl = ossTemplate.generateSignedUrl("upload/test.png", 3600);
```

---

## 🔧 自定义配置

如果你需要覆盖默认的 OSS 客户端或模板 Bean，可以自行声明：

```java
@Configuration
public class OssConfiguration {

    @Bean
    public AlibabaOssTemplate alibabaOssTemplate(OSS ossClient) {
        return new AlibabaOssTemplate(ossClient);
    }
}
```

如果不想启用自动配置，可以排除：

```java
@SpringBootApplication(exclude = AlibabaOssAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

## 📁 项目结构

```text
alibaba-oss-spring-boot-starter
├── alibaba-oss-spring-boot-starter
│   ├── src/main/java
│   ├── src/main/resources
│   └── pom.xml
├── README.md
└── LICENSE
```

---

## ❓ 常见问题

### 1. 必须使用 JDK 21 吗？

当前项目基于 JDK 21 开发。如果你的项目使用更低版本，需要确认编译目标和依赖兼容性。

### 2. AccessKey 如何安全配置？

不要硬编码在代码中。推荐使用环境变量、配置中心或 CI/CD Secret：

```yaml
alibaba:
  oss:
    access-key-id: ${ALIYUN_OSS_ACCESS_KEY_ID}
    access-key-secret: ${ALIYUN_OSS_ACCESS_KEY_SECRET}
```

### 3. 如何限制上传文件类型？

可以通过配置项限制扩展名，也可以在业务代码中自行校验。

### 4. 如何自定义上传路径？

可以使用 `dir-prefix`，或在上传时传入完整 `objectName`。

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request。

1. Fork 本仓库
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 提交修改：`git commit -m "feat: add your feature"`
4. 推送分支：`git push origin feature/your-feature`
5. 提交 Pull Request

---

## 📄 License

本项目基于 Apache License 2.0 开源。详情请查看 [LICENSE](LICENSE)。
