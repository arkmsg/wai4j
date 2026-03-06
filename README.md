# WAI4J - Whale AI for Java

简洁、优雅的 Java AI SDK，提供统一的接口集成多个主流 AI 大语言模型。

## 特性

- ✅ 简洁的 API 设计，2 行代码即可使用
- ✅ 支持多个主流 AI 模型提供商
- ✅ 统一的配置管理
- ✅ 响应式编程支持
- ✅ Spring Boot 自动配置
- ✅ 插件化架构，按需引入

## 支持的 AI 模型

| 提供商 | 模型系列 | 模块 | 状态 |
|--------|---------|------|------|
| DeepSeek | R1/V3 | wai4j-deepseek | ✅ 已完成 |
| OpenAI | GPT-4/GPT-3.5 | wai4j-openai | ✅ 已完成 |
| Google | Gemini Ultra/Pro | wai4j-gemini | ✅ 已完成 |
| 智谱 AI | GLM-4 | wai4j-glm | ✅ 已完成 |
| 阿里云 | Qwen3 | wai4j-qwen | ✅ 已完成 |
| 字节豆包 | Doubao-Pro | wai4j-doubao | ✅ 已完成 |
| X 平台 | Grok-1/Grok-2 | wai4j-grok | ✅ 已完成 |

## 快速开始

### Maven 依赖

```xml
<!-- 核心模块 -->
<dependency>
    <groupId>com.whaleal.ai</groupId>
    <artifactId>wai4j-core</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- 按需引入模型适配器 -->
<dependency>
    <groupId>com.whaleal.ai</groupId>
    <artifactId>wai4j-deepseek</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 使用示例

```java
import com.whaleal.ai.Ai;
import com.whaleal.ai.AiConfig;

public class Demo {
    public static void main(String[] args) {
        // DeepSeek
        AiConfig config = AiConfig.create()
            .setProvider("deepseek")
            .setApiKey("your-api-key")
            .setModel("deepseek-chat");
        
        String response = Ai.chat(config, "你好");
        System.out.println(response);
    }
}
```

## 项目结构

```
wai4j/
├── wai4j-core              # 核心模块
├── wai4j-deepseek          # DeepSeek 适配器
├── wai4j-openai            # OpenAI 适配器
├── wai4j-gemini            # Google Gemini 适配器
├── wai4j-glm               # 智谱 GLM 适配器
├── wai4j-qwen              # 阿里云 Qwen 适配器
├── wai4j-doubao            # 字节豆包适配器
├── wai4j-grok              # X 平台 Grok 适配器
├── wai4j-router            # 模型路由
├── wai4j-spring-boot-starter  # Spring Boot 自动配置
└── wai4j-example           # 示例代码
```

## 许可证

Apache License 2.0
