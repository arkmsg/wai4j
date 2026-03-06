package com.whaleal.ai;

import com.whaleal.ai.exception.AiException;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * AI 服务接口
 */
public interface AiService {

    /**
     * 单次对话
     * @param config 配置
     * @param prompt 提示词
     * @return 响应
     */
    String chat(AiConfig config, String prompt);

    /**
     * 多轮对话
     * @param config 配置
     * @param messages 消息列表
     * @return 响应
     */
    String chat(AiConfig config, List<Message> messages);

    /**
     * 流式对话
     * @param config 配置
     * @param prompt 提示词
     * @return 响应流
     */
    Flux<String> chatStream(AiConfig config, String prompt);

    /**
     * 流式对话
     * @param config 配置
     * @param messages 消息列表
     * @return 响应流
     */
    Flux<String> chatStream(AiConfig config, List<Message> messages);
}
