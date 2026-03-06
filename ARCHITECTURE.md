# WAI 架构设计文档

## 1. 项目概述

WAI (WhAlaI - Whale AI) 是一个参考 Hutool-AI 设计的简洁、优雅的 Java AI SDK，提供统一的接口来集成多个主流 AI 模型提供商。

### 1.1 设计目标

- **简洁性**: 类似 Hutool 的静态工具类风格，2-3 行代码即可调用 AI 能力
- **统一性**: 一套 API 支持所有主流 AI 模型
- **可扩展性**: 插件化架构，按需引入模型适配器
- **零配置**: 基于配置文件和 AutoConfig 自动装配
- **本地模型支持**: 支持 LGBM 等本地机器学习模型

### 1.2 核心特性

- ✅ 简洁的静态工具类 API (参考 Hutool)
- ✅ SPI 服务加载机制
- ✅ 响应式编程支持 (Reactor)
- ✅ Spring Boot 自动配置
- ✅ 多模型路由和负载均衡
- ✅ 本地和云端模型统一接口

## 2. 项目结构

```
wai/
├── core                      # 核心模块 (1.5KB)
│   ├── Ai                    # 静态工具类 (类似 Hutool)
│   ├── AiConfig              # 配置类
│   ├── AiService             # 服务接口
│   ├── Message               # 消息模型
│   ├── spi/                  # SPI 接口
│   └── exception/            # 异常类
│
├── deepseek                  # DeepSeek 适配器
├── openai                    # OpenAI GPT 适配器
├── gemini                    # Google Gemini 适配器
├── glm                       # 智谱 GLM 适配器
├── qwen                      # 阿里云 Qwen 适配器
├── doubao                    # 字节豆包适配器
├── grok                      # X 平台 Grok 适配器
├── local                     # 本地模型 (LGBM 等)
├── router                    # 模型路由和负载均衡
├── spring                    # Spring Boot Starter
│   ├── WaiAutoConfiguration  # 自动配置
│   ├── WaiProperties         # 配置属性
│   └── WaiTemplate           # 模板类
│
└── example                   # 示例项目
```

## 3. 模块说明

### 3.1 核心模块 (wai4j-core)

**包名**: `com.whaleal.ai`

**核心类**:

```java
// 静态工具类 (类似 Hutool)
public class Ai {
    public static String chat(AiConfig config, String prompt);
    public static String chat(AiConfig config, List<Message> messages);
    public static Flux<String> chatStream(AiConfig config, String prompt);
}

// 配置类
public class AiConfig {
    private String provider;  // deepseek, openai, gemini...
    private String apiKey;
    private String model;
    private String baseUrl;
    // ... 其他配置
}

// SPI 接口
public interface AiServiceProvider {
    boolean support(String provider);
    AiService getService();
}
```

**设计特点**:
- 基于 SPI 的服务发现机制
- 静态方法封装，零学习成本
- 统一的异常处理

### 3.2 模型适配器模块

每个模型提供商一个独立模块，互不依赖。

**示例**: `wai4j-deepseek`

**包名**: `com.whaleal.ai.deepseek`

**实现**:
```java
public class DeepSeekService implements AiService {
    @Override
    public boolean support(String provider) {
        return "deepseek".equalsIgnoreCase(provider);
    }
    
    @Override
    public AiService getService() {
        return this;
    }
    
    @Override
    public String chat(AiConfig config, String prompt) {
        // 实现 DeepSeek API 调用
    }
}
```

**SPI 注册**:
```
META-INF/services/com.whaleal.ai.spi.AiServiceProvider
内容：com.whaleal.ai.deepseek.DeepSeekService
```

### 3.3 Spring Boot Starter (wai-spring)

**包名**: `com.whaleal.ai.autoconfig`

**自动配置**:
```java
@Configuration
@EnableConfigurationProperties(WaiProperties.class)
@ConditionalOnClass(AiService.class)
public class WaiAutoConfiguration {
    
    @Bean
    public AiService aiService() {
        // 自动初始化所有配置的提供商
    }
    
    @Bean
    public WaiTemplate waiTemplate() {
        // 提供简化的模板类
    }
}
```

**配置属性**:
```yaml
wai:
  default-provider: deepseek
  configs:
    deepseek:
      api-key: ${DEEPSEEK_API_KEY}
      model: deepseek-chat
```

## 4. 使用示例

### 4.1 基础使用 (类似 Hutool)

```java
import com.whaleal.ai.Ai;
import com.whaleal.ai.AiConfig;

// 单次对话
AiConfig config = AiConfig.create()
    .setProvider("deepseek")
    .setApiKey("your-api-key");

String response = Ai.chat(config, "你好");

// 多轮对话
List<Message> messages = Arrays.asList(
    Message.system("你是一位助手"),
    Message.user("你好")
);
response = Ai.chat(config, messages);
```

### 4.2 Spring Boot 使用

```java
@RestController
public class ChatController {
    
    @Autowired
    private WaiTemplate wai;
    
    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return wai.chat(prompt);
    }
}
```

### 4.3 多模型路由

```java
import com.whaleal.ai.router.AiRouter;

AiRouter router = AiRouter.builder()
    .addProvider("deepseek", config1)
    .addProvider("openai", config2)
    .strategy("priority")  // 优先级策略
    .build();

String response = router.chat("你好");  // 自动选择可用模型
```

### 4.4 本地模型 (LGBM)

```java
import com.whaleal.ai.local.LgbmModel;

// 加载本地模型
LgbmModel model = LgbmModel.load("path/to/model.txt");

// 预测
double prediction = model.predict(features);

// 通过统一接口调用
AiConfig config = AiConfig.create()
    .setProvider("local")
    .setModel("lgbm");

String response = Ai.chat(config, "预测结果");
```

## 5. 支持的模型

### 5.1 云端 API

| 提供商 | 配置项 | 模型示例 | 状态 |
|--------|--------|---------|------|
| DeepSeek | `deepseek` | deepseek-chat | ✅ |
| OpenAI | `openai` | gpt-4, gpt-3.5-turbo | 📋 |
| Google | `gemini` | gemini-ultra, gemini-pro | 📋 |
| 智谱 AI | `glm` | glm-4, glm-3-turbo | 📋 |
| 阿里云 | `qwen` | qwen-max, qwen-plus | 📋 |
| 字节豆包 | `doubao` | doubao-pro | 📋 |
| X 平台 | `grok` | grok-1, grok-2 | 📋 |

### 5.2 本地模型

| 模型类型 | 配置项 | 说明 | 状态 |
|----------|--------|------|------|
| LightGBM | `lgbm` | 梯度提升树 | 📋 |
| XGBoost | `xgb` | 极端梯度提升 | 📋 |
| ONNX | `onnx` | 开放神经网络交换 | 📋 |

## 6. 扩展指南

### 6.1 添加新的模型提供商

1. 创建新模块: `wai-provider-name`
2. 实现 `AiService` 接口
3. 实现 `AiServiceProvider` SPI
4. 添加 `META-INF/services/com.whaleal.ai.spi.AiServiceProvider` 文件

### 6.2 添加本地模型

1. 在 `wai-local` 模块中添加模型实现
2. 实现统一的预测接口
3. 适配到 `AiService` 接口

## 7. 最佳实践

### 7.1 密钥管理

```java
// 使用环境变量
String apiKey = System.getenv("DEEPSEEK_API_KEY");

// 或使用 Spring Boot 配置
@Value("${wai.configs.deepseek.api-key}")
private String apiKey;
```

### 7.2 错误处理

```java
try {
    String response = Ai.chat(config, prompt);
} catch (AiException e) {
    if (e.getCode() == 429) {
        // 限流处理
    } else if (e.getCode() == 401) {
        // 认证失败
    }
}
```

### 7.3 并发处理

```java
// 使用 Reactor 并行调用
Flux<String> responses = Flux.fromIterable(prompts)
    .flatMap(prompt -> Ai.chatStream(config, prompt), 3);
```

## 8. 性能优化

- 连接池复用
- 响应式流式处理
- 本地缓存
- 批量请求

## 9. 后续计划

- [ ] 完成所有云端 API 适配器
- [ ] 实现本地模型支持 (LGBM, XGBoost)
- [ ] 添加模型路由策略
- [ ] 实现请求限流和重试
- [ ] 添加监控和指标
- [ ] 完善文档和测试

## 10. 参考项目

- [Hutool-AI](https://github.com/dromara/hutool) - 简洁的静态工具类设计
- [deepseek4j](https://github.com/arkmsg/deepseek4j) - 优秀的响应式架构
- [LangChain4j](https://github.com/langchain4j/langchain4j) - 完整的 AI 集成框架
