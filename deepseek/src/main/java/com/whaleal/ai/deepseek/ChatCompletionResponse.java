package com.whaleal.ai.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DeepSeek 聊天完成响应
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletionResponse {

    /**
     * 选择列表
     */
    @JsonProperty("choices")
    private List<Choice> choices;

    /**
     * 使用情况
     */
    @JsonProperty("usage")
    private Usage usage;

    /**
     * 选择
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        
        @JsonProperty("index")
        private Integer index;

        @JsonProperty("message")
        private ChatMessage message;

        @JsonProperty("delta")
        private ChatMessage delta;

        @JsonProperty("finish_reason")
        private String finishReason;
    }

    /**
     * 使用情况
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
