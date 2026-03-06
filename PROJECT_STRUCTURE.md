# WAI 项目结构

## 目录结构

```
wai/
├── README.md                    # 项目说明
├── USAGE.md                     # 使用指南
├── pom.xml                      # 父 POM
├── core/                        # 核心模块
│   ├── pom.xml
│   └── src/main/java/com/whaleal/ai/
│       ├── Ai.java             # 统一工具类 (类似 Hutool 风格)
│       ├── AiService.java      # AI 服务接口
│       ├── AiConfig.java       # AI 配置类
│       ├── Message.java        # 消息类
│       ├── exception/
│       │   └── AiException.java
│       └── spi/
│           └── AiServiceProvider.java  # SPI 接口
│
├── deepseek/                    # DeepSeek 适配器
│   ├── pom.xml
│   └── src/main/java/com/whaleal/ai/deepseek/
│       ├── DeepSeekService.java
│       ├── DeepSeekApi.java
│       ├── ChatMessage.java
│       ├── ChatCompletionRequest.java
│       └── ChatCompletionResponse.java
│
├── openai/                      # OpenAI 适配器
│   └── pom.xml                  # (框架已建，待实现)
│
├── gemini/                      # Google Gemini 适配器
│   └── pom.xml                  # (框架已建，待实现)
│
├── glm/                         # 智谱 GLM 适配器
│   └── pom.xml                  # (框架已建，待实现)
│
├── qwen/                        # 阿里云 Qwen 适配器
│   └── pom.xml                  # (框架已建，待实现)
│
├── doubao/                      # 字节豆包适配器
│   └── pom.xml                  # (框架已建，待实现)
│
├── grok/                        # X 平台 Grok 适配器
│   └── pom.xml                  # (框架已建，待实现)
│
├── local/                       # 本地模型适配器
│   ├── pom.xml
│   └── (待实现 LGBM, ONNX 等)
│
├── router/                      # 模型路由模块
│   ├── pom.xml
│   └── (待实现路由和负载均衡)
│
├── spring/                      # Spring Boot Starter
│   ├── pom.xml
│   └── src/main/java/com/whaleal/ai/autoconfig/
│       ├── WaiAutoConfiguration.java
│       ├── WaiProperties.java
│       └── WaiTemplate.java
│
└── example/                     # 示例项目
    ├── pom.xml
    └── src/main/java/com/whaleal/ai/demo/
        └── WaiDemoApplication.java
```

## 模块说明

### 核心模块 (wai4j-core)

**职责**: 提供统一的 API 接口、配置管理、SPI 机制

**核心类**:
- `Ai`: 统一工具类，类似 Hutool 的静态方法风格
- `AiService`: AI 服务接口
- `AiConfig`: AI 配置类
- `Message`: 消息类
- `AiServiceProvider`: SPI 接口

**特点**:
- 零依赖 (仅依赖 Hutool、Reactor、Lombok)
- 基于 SPI 的插件化架构
- 支持多 Provider 动态加载

### Provider 模块

每个 AI 提供商一个独立模块，按需引入：

| 模块 | artifactId | 提供商 | 状态 |
|------|-----------|--------|------|
| DeepSeek | wai4j-deepseek | 深度求索 | ✅ 已完成 |
| OpenAI | wai-openai | OpenAI GPT | 📋 框架已建 |
| Gemini | wai-gemini | Google Gemini | 📋 框架已建 |
| GLM | wai-glm | 智谱 AI | 📋 框架已建 |
| Qwen | wai-qwen | 阿里云 Qwen | 📋 框架已建 |
| Doubao | wai-doubao | 字节豆包 | 📋 框架已建 |
| Grok | wai-grok | X 平台 Grok | 📋 框架已建 |
| Local | wai-local | 本地模型 (LGBM 等) | 📋 框架已建 |

**实现规范**:
1. 实现 `AiServiceProvider` SPI 接口
2. 在 `META-INF/services/com.whaleal.ai.spi.AiServiceProvider` 注册
3. 使用 Retrofit + OkHttp 实现 HTTP 客户端
4. 支持同步、异步、流式调用

### Spring Boot Starter (wai4j-spring-boot-starter)

**职责**: 提供 Spring Boot 自动配置

**核心类**:
- `WaiAutoConfiguration`: 自动配置类
- `WaiProperties`: 配置属性类
- `WaiTemplate`: 简化模板类

**配置前缀**: `wai`

**自动装配**:
```java
@Bean
@ConditionalOnMissingBean
public AiService aiService() {
    // 自动初始化配置的 Provider
}

@Bean
@ConditionalOnMissingBean
public WaiTemplate waiTemplate(AiService aiService) {
    return new WaiTemplate(aiService);
}
```

### 路由模块 (wai-router)

**职责**: 模型路由、负载均衡、故障转移

**待实现功能**:
- 优先级路由
- 轮询负载均衡
- 加权随机
- 故障自动转移
- 熔断降级

### 示例项目 (wai-example)

**职责**: 提供完整的使用示例

**内容**:
- Spring Boot 集成示例
- 多种对话模式示例
- 配置文件示例

## 设计特点

### 1. 简洁的命名

- 项目名：WAI (Whale AI)
- artifactId: 简短易记 (wai4j-core, wai4j-deepseek 等)
- 包名：扁平化 (com.whaleal.ai)

### 2. Hutool 风格

- 静态工具类 `Ai`，类似 `StrUtil`、`HttpUtil`
- 链式配置 `AiConfig.create().setXxx()`
- 零配置即可使用

### 3. 插件化架构

- 基于 SPI 的 Provider 加载
- 核心模块零 Provider 依赖
- 按需引入 Provider 模块

### 4. 响应式支持

- 完整的 Reactor 支持
- 流式对话 `Flux<String>`
- 背压支持

### 5. Spring Boot 友好

- 自动配置
- 配置属性绑定
- 条件化装配

## 扩展指南

### 添加新的 AI Provider

1. 创建新模块：`wai-xxx`
2. 实现 `AiServiceProvider` 接口
3. 实现 `AiService` 接口
4. 在 `META-INF/services/` 注册
5. 添加测试用例

### 添加本地模型

1. 在 `wai-local` 模块中添加实现
2. 支持 LGBM、ONNX、TensorFlow 等格式
3. 实现模型加载和推理

### 自定义路由策略

1. 实现 `AiRouter` 接口
2. 配置路由规则
3. 实现负载均衡算法

## 构建说明

### 编译项目

```bash
cd wai
mvn clean install
```

### 构建示例

```bash
cd example
mvn spring-boot:run
```

### 发布到 Maven Central

```bash
mvn clean deploy -P release
```

## 版本规划

- **v1.0.0** (当前): 核心框架 + DeepSeek 实现
- **v1.1.0**: 完成所有三方 API Provider
- **v1.2.0**: 本地模型支持 (LGBM)
- **v1.3.0**: 路由和负载均衡
- **v2.0.0**: 多模态支持 (图像、语音)

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交变更
4. 推送到分支
5. 创建 Pull Request

## 许可证

Apache License 2.0

## 联系方式

- GitHub: https://github.com/whaleal/wai
- Email: arkmsg@github.com
