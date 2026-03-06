package com.whaleal.ai.autoconfig;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * WAI 模板类 (简化 Spring Boot 中的使用)
 */
@RequiredArgsConstructor
public class WaiTemplate {

    private final AiService aiService;

    /**
     * 单次对话
     */
    public String chat(String prompt) {
        AiConfig config = AiConfig.create();
        return aiService.chat(config, prompt);
    }

    /**
     * 单次对话（指定提供商）
     */
    public String chat(String provider, String prompt) {
        AiConfig config = AiConfig.create().setProvider(provider);
        return aiService.chat(config, prompt);
    }

    /**
     * 多轮对话
     */
    public String chat(List<Message> messages) {
        AiConfig config = AiConfig.create();
        return aiService.chat(config, messages);
    }

    /**
     * 多轮对话（指定提供商）
     */
    public String chat(String provider, List<Message> messages) {
        AiConfig config = AiConfig.create().setProvider(provider);
        return aiService.chat(config, messages);
    }

    /**
     * 流式对话
     */
    public Flux<String> chatStream(String prompt) {
        AiConfig config = AiConfig.create();
        return aiService.chatStream(config, prompt);
    }

    /**
     * 流式对话（指定提供商）
     */
    public Flux<String> chatStream(String provider, String prompt) {
        AiConfig config = AiConfig.create().setProvider(provider);
        return aiService.chatStream(config, prompt);
    }
}
