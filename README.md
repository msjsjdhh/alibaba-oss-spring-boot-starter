# Alibaba OSS Spring Boot Starter

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)

开箱即用的阿里云 OSS（Object Storage Service）Spring Boot 自动装配组件。旨在简化凭证管理、流式文件上传、删除及目录列举操作，减少样板代码，契合云原生最佳实践。

---

## ✨ 功能特性

- 🚀 **开箱即用**：自动装配线程安全的 `OSSClient` 单例，容器关闭时自动释放资源。
- 🔐 **智能凭证解析**：优先读取配置文件中的 AccessKey；若留空，则自动回退读取环境变量（`OSS_ACCESS_KEY_ID` / `OSS_ACCESS_KEY_SECRET`），完美适配 K8s / 容器化部署。
- 🛡️ **防 OOM 设计**：上传接口直接接收 `InputStream` 进行流式透传，避免大文件全量加载到 JVM 内存。
- 📁 **自动路径防冲突**：内置基于日期（`yyyy/MM/dd`）和 UUID 的文件名生成策略，避免文件覆盖。
- 📋 **基础操作支持**：支持文件流式上传、删除、获取访问 URL 以及按日期前缀的分页列举。

---

## 🚀 快速开始

### 1. 添加依赖

将以下依赖添加到你的 `pom.xml` 中，并将 `最新版本` 替换为实际的 Release 版本号：

```xml
<dependency>
    <groupId>com.github.yaolongma</groupId>
    <artifactId>alibaba-oss-spring-boot-starter</artifactId>
    <version>最新版本</version>
</dependency>
```

### 2. 添加配置

在 `application.yml` 中配置 OSS 基础信息：

```yaml
alibaba:
  oss:
    region: oss-cn-hangzhou
    bucket: your-bucket-name
    access-key-id: ${OSS_ACCESS_KEY_ID:}
    access-key-secret: ${OSS_ACCESS_KEY_SECRET:}
```

### 3. 注入使用

直接在需要的地方注入 `OssTemplate` 即可使用：

```java
import com.alibaba.core.service.OssTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class FileController {

    private final OssTemplate ossTemplate;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        return ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String fileName) {
        ossTemplate.delete(fileName);
        return "删除成功";
    }

    @GetMapping("/files")
    public Object getFiles(@RequestParam(required = false) String continuationToken) {
        return ossTemplate.getFiles(continuationToken);
    }
}
```

---

## ⚙️ 配置项说明

| 配置项 | 说明 | 必填 | 默认值 / 行为 |
| --- | --- | --- | --- |
| `alibaba.oss.region` | 阿里云 OSS 地域节点，如 `oss-cn-hangzhou` | 是 | 无 |
| `alibaba.oss.bucket` | 目标 Bucket 名称 | 是 | 无 |
| `alibaba.oss.access-key-id` | AccessKey ID | 否 | 留空时自动读取环境变量 `OSS_ACCESS_KEY_ID` |
| `alibaba.oss.access-key-secret` | AccessKey Secret | 否 | 留空时自动读取环境变量 `OSS_ACCESS_KEY_SECRET` |

---

## 🧩 核心 API 说明

| 方法签名 | 说明 | 返回值 |
| --- | --- | --- |
| `upload(InputStream in, String fileName)` | 流式上传文件。自动附加 `yyyy/MM/dd/uuid_` 前缀防冲突 | `String`，文件的完整公网访问 URL |
| `getUrl(String key)` | 根据已知的 Object Key 拼接并返回公网访问 URL | `String` |
| `delete(String fileName)` | 根据完整的 Object Key（包含路径）删除文件 | `void` |
| `getFiles()` | 列举当天（`yyyy/MM/dd`）目录下的文件，默认分页大小 1000 | `ListObjectsV2Result` |
| `getFiles(String continuationToken)` | 带分页 Token 列举当天目录下的文件 | `ListObjectsV2Result` |

---

## ⚠️ 当前版本限制与注意事项

- **类名拼写**：请确保引入的类名为 `OssTemplate`。部分早期版本可能存在 `OssTemplete` 的拼写错误，请以最新代码为准。
- **流式上传**：为了保证大文件上传时的内存安全，API 设计为接收 `InputStream`。如需上传 `byte[]` 或 `MultipartFile`，请在业务层调用 `.getInputStream()` 后传入。
- **URL 生成规则**：`getUrl` 方法目前基于标准规则拼接，例如 `https://{bucket}.{region}.aliyuncs.com/{key}`，其中 `region` 形如 `oss-cn-hangzhou`。如果你的 Bucket 绑定了自定义域名（CNAME），当前版本暂不支持自动替换，需在业务层自行拼接。
- **未包含的功能**：当前轻量级版本暂未包含文件下载流（`download`）、签名 URL 生成（`generateSignedUrl`）及文件扩展名 / 大小校验。如有需要，欢迎在业务层基于注入的 `OSSClient` 自行扩展，或提交 PR 贡献代码。

---

## License

This project is licensed under the [Apache License 2.0](https://opensource.org/licenses/Apache-2.0).
