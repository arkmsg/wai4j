# WAI 使用指南

## 目录

1. [快速开始](#快速开始)
2. [核心概念](#核心概念)
3. [支持的 AI 模型](#支持的-ai-模型)
4. [使用示例](#使用示例)
5. [配置说明](#配置说明)
6. [高级特性](#高级特性)
7. [最佳实践](#最佳实践)
8. [故障排查](#故障排查)

## 快速开始

### 1. 添加依赖

```xml
<!-- 核心模块 -->
<dependency>
    <groupId>com.whaleal.ai</groupId>
    <artifactId>wai4j-core</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- 按需添加模型适配器 -->
<dependency>
    <groupId>com.whaleal.ai</groupId>
    <artifactId>wai4j-deepseek</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 基础使用

```java
import com.whaleal.ai.Ai;
import com.whaleal.ai.AiConfig;

public class Demo {
    public static void main(String[] args) {
        AiConfig config = AiConfig.create()
            .setProvider("deepseek")
            .setApiKey("your-api-key")
            .setModel("deepseek-chat");
        
        String response = Ai.chat(config, "你好，请介绍一下自己");
        System.out.println(response);
    }
}
```

## 核心概念

### AiConfig - AI 配置

```java
AiConfig config = AiConfig.create()
    .setProvider("deepseek")      // 提供商：deepseek, openai, gemini, glm, qwen, doubao, grok, local
    .setApiKey("your-api-key")    // API 密钥
    .setModel("deepseek-chat")    // 模型名称
    .setBaseUrl("https://...")    // 基础 URL (可选)
    .setConnectTimeout(30)        // 连接超时 (秒)
    .setReadTimeout(60)           // 读取超时 (秒)
    .setCallTimeout(120);         // 调用超时 (秒)
```

### Message - 消息对象

```java
import com.whaleal.ai.Message;

// 系统消息
Message.system("你是一位 AI 助手");

// 用户消息
Message.user("你好");

// 助手消息
Message.assistant("你好，有什么可以帮助你的吗？");
```

### Ai - 统一工具类

```java
// 单次对话
String response = Ai.chat(config, "你好");

// 多轮对话
List<Message> messages = Arrays.asList(
    Message.system("你是一位助手"),
    Message.user("你好")
);
String response = Ai.chat(config, messages);

// 流式对话
Flux<String> stream = Ai.chatStream(config, "写一首诗");
stream.subscribe(System.out::print);
```

## 支持的 AI 模型

### 三方 API 模型

| 提供商 | 模块 | 模型示例 | 状态 |
|--------|------|---------|------|
| DeepSeek | wai4j-deepseek | deepseek-chat, deepseek-reasoner | ✅ 已完成 |
| OpenAI | wai-openai | gpt-4, gpt-3.5-turbo | 📋 框架已建 |
| Google Gemini | wai-gemini | gemini-ultra, gemini-pro | 📋 框架已建 |
| 智谱 GLM | wai-glm | glm-4, glm-3-turbo | 📋 框架已建 |
| 阿里云 Qwen | wai-qwen | qwen-max, qwen-plus | 📋 框架已建 |
| 字节豆包 | wai-doubao | doubao-pro | 📋 框架已建 |
| X 平台 Grok | wai-grok | grok-1, grok-2 | 📋 框架已建 |

### 本地模型

| 类型 | 模块 | 框架 | 状态 |
|------|------|------|------|
| LGBM | wai-local | LightGBM | 📋 框架已建 |
| ONNX | wai-local | ONNX Runtime | 📋 框架已建 |

## 使用示例

### 1. 单次对话

```java
AiConfig config = AiConfig.create()
    .setProvider("deepseek")
    .setApiKey("sk-123456")
    .setModel("deepseek-chat");

String response = Ai.chat(config, "用 Java 实现冒泡排序");
System.out.println(response);
```

### 2. 多轮对话

```java
AiConfig config = AiConfig.create()
    .setProvider("deepseek")
    .setApiKey("sk-123456");

List<Message> messages = new ArrayList<>();
messages.add(Message.system("你是一位 Java 专家"));
messages.add(Message.user("什么是 Spring 框架？"));

String response = Ai.chat(config, messages);
System.out.println(response);

// 继续对话
messages.add(Message.assistant(response));
messages.add(Message.user("它有哪些核心特性？"));

response = Ai.chat(config, messages);
System.out.println(response);
```

### 3. 流式对话

```java
AiConfig config = AiConfig.create()
    .setProvider("deepseek")
    .setApiKey("sk-123456");

Flux<String> stream = Ai.chatStream(config, "写一首关于春天的诗");

stream.subscribe(
    content -> System.out.print(content),
    error -> System.err.println("Error: " + error),
    () -> System.out.println("\nComplete!")
);
```

### 4. Spring Boot 集成

```java
@RestController
@RequestMapping("/api")
public class ChatController {
    
    @Autowired
    private WaiTemplate waiTemplate;
    
    @PostMapping("/chat")
    public String chat(@RequestParam String prompt) {
        return waiTemplate.chat(prompt);
    }
    
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam String prompt) {
        return waiTemplate.chatStream(prompt);
    }
}
```

## 配置说明

### Spring Boot 配置

```yaml
# application.yml
wai:
  # 默认提供商
  default-provider: deepseek
  
  # 启用状态
  enabled: true
  
  # 各提供商配置
  configs:
    deepseek:
      api-key: ${DEEPSEEK_API_KEY}
      model: deepseek-chat
      base-url: https://api.deepseek.com/v1
      connect-timeout: 30
      read-timeout: 60
      call-timeout: 120
    
    openai:
      api-key: ${OPENAI_API_KEY}
      model: gpt-4
      base-url: https://api.openai.com/v1
    
    gemini:
      api-key: ${GEMINI_API_KEY}
      model: gemini-ultra
    
    glm:
      api-key: ${GLM_API_KEY}
      model: glm-4
    
    qwen:
      api-key: ${QWEN_API_KEY}
      model: qwen-max
    
    doubao:
      api-key: ${DOUBAO_API_KEY}
      model: doubao-pro
    
    grok:
      api-key: ${GROK_API_KEY}
      model: grok-1
```

### 环境变量

```bash
# 设置 API 密钥
export DEEPSEEK_API_KEY=sk-xxx
export OPENAI_API_KEY=sk-xxx
export GEMINI_API_KEY=xxx
export GLM_API_KEY=xxx
export QWEN_API_KEY=xxx
export DOUBAO_API_KEY=xxx
export GROK_API_KEY=xxx
```

## 高级特性

### 1. 自定义 Provider

实现 `AiServiceProvider` SPI 接口：

```java
public class CustomAiService implements AiServiceProvider {
    
    @Override
    public boolean support(String provider) {
        return "custom".equalsIgnoreCase(provider);
    }
    
    @Override
    public AiService getService() {
        return new AiService() {
            @Override
            public String chat(AiConfig config, String prompt) {
                // 实现自定义逻辑
                return "Custom response";
            }
            
            // 实现其他方法...
        };
    }
}
```

在 `META-INF/services/com.whaleal.ai.spi.AiServiceProvider` 中注册：

```
com.yourcompany.CustomAiService
```

### 2. 本地模型集成

```java
AiConfig config = AiConfig.create()
    .setProvider("local")
    .setModel("lgbm-model")
    .setExtra("modelPath", "/path/to/model.txt");

String prediction = Ai.chat(config, "预测数据");
```

### 3. 错误处理

```java
try {
    String response = Ai.chat(config, "你好");
} catch (AiException e) {
    if (e.getCode() == 401) {
        System.err.println("API Key 无效");
    } else if (e.getCode() == 429) {
        System.err.println("请求限流");
    } else if (e.getCode() == 500) {
        System.err.println("服务器错误");
    } else {
        System.err.println("其他错误：" + e.getMessage());
    }
}
```

## 最佳实践

### 1. 密钥管理

```java
// 使用环境变量
String apiKey = System.getenv("DEEPSEEK_API_KEY");

// 或使用配置文件
Setting setting = new Setting("ai.setting");
String apiKey = setting.getStr("deepseek.apiKey");

// 不要硬编码
// ❌ 错误示例
// String apiKey = "sk-123456";

// ✅ 正确示例
String apiKey = System.getenv("DEEPSEEK_API_KEY");
```

### 2. 超时配置

```java
AiConfig config = AiConfig.create()
    .setConnectTimeout(30)   // 连接超时 30 秒
    .setReadTimeout(60)      // 读取超时 60 秒
    .setCallTimeout(120);    // 总超时 120 秒
```

### 3. 连接池优化

```java
// 在配置中设置合理的超时和连接数
AiConfig config = AiConfig.create()
    .setConnectTimeout(30)
    .setReadTimeout(60)
    .setExtra("maxIdleConnections", 5)
    .setExtra("keepAliveDuration", 300);
```

### 4. 日志配置

```yaml
# application.yml
logging:
  level:
    com.whaleal.ai: DEBUG  # 开发环境
    # com.whaleal.ai: WARN # 生产环境
```

## 故障排查

### 常见问题

#### 1. API Key 无效

**错误**: `401 Unauthorized`

**解决**:
- 检查 API Key 是否正确
- 确认账户余额充足
- 验证 API Key 权限

#### 2. 网络超时

**错误**: `TimeoutException`

**解决**:
```java
AiConfig config = AiConfig.create()
    .setConnectTimeout(60)   // 增加超时时间
    .setReadTimeout(120);
```

#### 3. 找不到 Provider

**错误**: `No AiService found for provider: xxx`

**解决**:
- 确认已添加对应模块依赖
- 检查 provider 名称拼写
- 验证 SPI 配置文件存在

#### 4. 限流错误

**错误**: `429 Too Many Requests`

**解决**:
```java
// 实现重试逻辑
int maxRetries = 3;
for (int i = 0; i < maxRetries; i++) {
    try {
        return Ai.chat(config, prompt);
    } catch (AiException e) {
        if (e.getCode() == 429 && i < maxRetries - 1) {
            Thread.sleep(1000 * (i + 1)); // 指数退避
        } else {
            throw e;
        }
    }
}
```

## 更多资源

- [GitHub 仓库](https://github.com/whaleal/wai)
- [示例代码](https://github.com/whaleal/wai/tree/master/example)
- [问题反馈](https://github.com/whaleal/wai/issues)
