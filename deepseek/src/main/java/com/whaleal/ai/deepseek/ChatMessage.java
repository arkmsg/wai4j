package com.whaleal.ai.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DeepSeek 消息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {

    /**
     * 角色：system, user, assistant
     */
    @JsonProperty("role")
    private String role;

    /**
     * 内容
     */
    @JsonProperty("content")
    private String content;

    public ChatMessage() {
    }

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
