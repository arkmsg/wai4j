# WAI4J 模型支持说明

## 当前支持的模型类型

### ✅ 文本大语言模型 (LLM)

目前 WAI4J 专注于集成主流的**文本对话型大语言模型**：

#### 三方 API 模型
| 提供商 | 模型系列 | 模块 | 类型 |
|--------|---------|------|------|
| DeepSeek | R1/V3 | wai4j-deepseek | 文本 LLM |
| OpenAI | GPT-4/GPT-3.5 | wai4j-openai | 文本 LLM |
| Google | Gemini Ultra/Pro | wai4j-gemini | 多模态 LLM* |
| 智谱 AI | GLM-4 | wai4j-glm | 文本 LLM |
| 阿里云 | Qwen3 | wai4j-qwen | 多模态 LLM* |
| 字节豆包 | Doubao-Pro | wai4j-doubao | 文本 LLM |
| X 平台 | Grok-1/Grok-2 | wai4j-grok | 文本 LLM |

#### 本地模型
| 类型 | 模块 | 说明 |
|------|------|------|
| LightGBM | wai4j-local (待实现) | 梯度提升决策树 |
| ONNX | wai4j-local (待实现) | 开放神经网络格式 |

## 架构扩展能力

### 当前架构特点

```java
// 统一的文本对话接口
public interface AiService {
    String chat(AiConfig config, String prompt);
    String chat(AiConfig config, List<Message> messages);
    Flux<String> chatStream(AiConfig config, String prompt);
    Flux<String> chatStream(AiConfig config, List<Message> messages);
}
```

✅ **优点**：
- 简洁易用，专注于文本对话场景
- 所有 LLM 提供商使用统一接口
- 易于扩展新的文本模型

### 未来扩展方向

#### 1. 多模态支持 (Multimodal)

```java
// 图像生成
public interface ImageService {
    ImageResponse generateImage(AiConfig config, String prompt);
    Flux<ImageResponse> generateImageStream(AiConfig config, String prompt);
}

// 图像理解
public interface VisionService {
    String analyzeImage(AiConfig config, byte[] imageData, String question);
}

// 语音识别 (STT)
public interface SpeechToTextService {
    String transcribe(AiConfig config, byte[] audioData);
}

// 语音合成 (TTS)
public interface TextToSpeechService {
    byte[] synthesize(AiConfig config, String text);
}
```

#### 2. 本地机器学习模型

```java
// 传统机器学习模型
public interface LocalMLService {
    Prediction predict(AiConfig config, Features features);
    ModelResult train(AiConfig config, Dataset trainingData);
}
```

#### 3. 嵌入模型 (Embeddings)

```java
// 文本嵌入
public interface EmbeddingService {
    EmbeddingResponse embed(AiConfig config, String text);
    List<Float> embedToVector(AiConfig config, String text);
}
```

## 建议的架构演进

### 方案一：保持简洁 (推荐)

**定位**：专注于文本 LLM 集成

```
wai4j/
├── wai4j-core              # 核心 (文本对话)
├── wai4j-provider-xxx      # 各文本模型提供商
└── wai4j-spring-boot-starter
```

**优点**：
- 保持简洁，易于维护
- 专注文本对话场景
- 类似 Hutool-AI 的设计理念

### 方案二：模块化扩展

```
wai4j/
├── wai4j-core              # 核心接口
├── wai4j-llm               # 文本 LLM 模块
├── wai4j-vision            # 视觉模块
├── wai4j-audio             # 音频模块
├── wai4j-ml                # 传统机器学习
└── wai4j-spring-boot-starter
```

**优点**：
- 支持多种 AI 能力
- 按需引入模块
- 完整的 AI SDK

### 方案三：插件化架构

```
wai4j/
├── wai4j-core              # 核心 + SPI
├── wai4j-plugin-llm        # LLM 插件
├── wai4j-plugin-vision     # 视觉插件
├── wai4j-plugin-audio      # 音频插件
└── wai4j-plugin-ml         # 机器学习插件
```

**优点**：
- 高度可扩展
- 第三方可以开发插件
- 类似 IDE 插件生态

## 当前实现建议

### 1. 专注于文本 LLM

继续完善现有的文本 LLM 支持：

```java
// 当前使用方式
AiConfig config = AiConfig.create()
    .setProvider("deepseek")
    .setApiKey("your-key");

String response = Ai.chat(config, "写一首诗");
```

### 2. 预留多模态接口

在核心模块中预留接口，暂不实现：

```java
// 预留接口 (不实现)
public interface ImageGenerationService {
    // 图像生成
}

public interface EmbeddingService {
    // 文本嵌入
}
```

### 3. 本地模型支持

优先实现本地 LLM 推理：

```java
// 本地 LLM (如 GGUF 格式)
AiConfig config = AiConfig.create()
    .setProvider("local")
    .setModel("llama-3-8b")
    .setExtra("modelPath", "/path/to/model.gguf");

String response = Ai.chat(config, "你好");
```

## 总结

**当前定位**：
- ✅ 专注于文本大语言模型 (LLM)
- ✅ 提供统一的对话接口
- ✅ 支持主流三方 API
- ✅ 类似 Hutool-AI 的简洁设计

**未来规划**：
- 📋 多模态支持 (图像、语音)
- 📋 本地 LLM 推理
- 📋 嵌入模型支持
- 📋 传统机器学习模型

**建议**：
保持简洁，先完善文本 LLM 的支持，后续根据实际需求再扩展多模态能力。

---

**WAI4J 版本**: 1.0.0  
**定位**: 文本大语言模型统一 SDK  
**状态**: ✅ 核心功能已完成
