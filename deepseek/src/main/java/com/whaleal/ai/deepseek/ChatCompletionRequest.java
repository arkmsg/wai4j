package com.whaleal.ai.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DeepSeek 聊天完成请求
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletionRequest {

    /**
     * 模型名称
     */
    @JsonProperty("model")
    private String model;

    /**
     * 消息列表
     */
    @JsonProperty("messages")
    private List<ChatMessage> messages;

    /**
     * 是否流式
     */
    @JsonProperty("stream")
    private Boolean stream = false;

    /**
     * 温度
     */
    @JsonProperty("temperature")
    private Double temperature;

    /**
     * 最大 token 数
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;
}
